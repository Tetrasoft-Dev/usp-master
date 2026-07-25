package gov.ao.usp.features.Utilizador.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.ao.usp.features.Utilizador.modelo.Perfil;
import gov.ao.usp.features.Utilizador.modelo.dto.CompletarPerfilRequest;
import gov.ao.usp.features.Utilizador.modelo.dto.PerfilRequestDTO;
import gov.ao.usp.features.Utilizador.modelo.dto.PerfilResponseDTO;
import gov.ao.usp.features.Utilizador.repository.PerfilRepository;
import gov.ao.usp.features.Utilizador.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usp/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PerfilRepository perfilRepository;
    private final PerfilService service;

    /**
     * 1. ROTA PÚBLICA: Acessível por qualquer pessoa (definida no SecurityConfig)
     */
    @GetMapping("/publico/status")
    public ResponseEntity<Map<String, String>> obterStatusPublico() {
        Map<String, String> resposta = new HashMap<>();
        resposta.put("status", "Online");
        resposta.put("mensagem", "Esta rota é pública e não exige token JWT.");
        return ResponseEntity.ok(resposta);
    }

    /**
     * 2. ROTA AUTENTICADA: Exige qualquer JWT válido emitido pelo Supabase
     */
    @GetMapping("/utilizador/dados-token")
    public ResponseEntity<Map<String, Object>> obterDadosDoToken(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> dados = new HashMap<>();
        
        // O "sub" contém o UUID único que o Supabase Auth atribuiu ao utilizador
        dados.put("supabase_uid", jwt.getSubject());
        dados.put("email", jwt.getClaimAsString("email"));
        dados.put("expira_em", jwt.getExpiresAt());
        dados.put("mensagem", "Identidade confirmada localmente com sucesso!");

        return ResponseEntity.ok(dados);
    }

    /**
     * 3. ROTA DE PERFIL COMPLETADA: Busca os dados complementares da nossa tabela do Postgres
     */
    @GetMapping("/utilizador/meu-perfil")
    public ResponseEntity<PerfilResponseDTO> obterPerfilCompleto(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        Perfil perfil = perfilRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Perfil não encontrado.") );

        PerfilResponseDTO dto = new PerfilResponseDTO(
            perfil.getId(),
            perfil.getNome(),
            perfil.getNip(),
            jwt.getClaimAsString("email"),
            jwt.getClaimAsString("email").split("@")[0],
            perfil.getPerfil(),
            perfil.getDepartamento().getPkDepartamento(),
            perfil.getDepartamento().getDescricao(),
            perfil.getUpdatedAt()
        );

        return ResponseEntity.ok(dto);
    }

    /**
     * 4. ROTA ADMINISTRATIVA: Exige que o utilizador tenha 'ROLE_ADMIN' na tabela de perfis
     */
    @GetMapping("/admin/painel")
    @PreAuthorize("hasRole('ADMIN')") // Validação estrita por anotação
    public ResponseEntity<Map<String, String>> acessarPainelAdmin() {
        Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Acesso concedido! Apenas utilizadores com perfil de administrador podem ver isto.");
        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Perfil> atualizarNomeDoUtilizador(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody PerfilRequestDTO request) {
        
        Perfil perfilAtualizado = service.atualizarPerfilPrincipal(jwt, request);
        return ResponseEntity.ok(perfilAtualizado);
    }

    @GetMapping("/receptor/painel")
    @PreAuthorize("hasRole('RECEPTOR')") // Bloqueio estrito via anotação de método
    public ResponseEntity<Map<String, String>> acessarPainelReceptor() {
         Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Acesso Concedido! Bem-vindo ao painel do Receptor de Artigos.");
        return ResponseEntity.ok(resposta);
   }

   @PutMapping("/completar-perfil")
   public ResponseEntity<PerfilResponseDTO> completarPerfil(
        @AuthenticationPrincipal Jwt jwt,
        @Valid @RequestBody CompletarPerfilRequest request) {

       PerfilResponseDTO response = service.completarPerfil(jwt, request);
       return ResponseEntity.ok(response);
   }
}
