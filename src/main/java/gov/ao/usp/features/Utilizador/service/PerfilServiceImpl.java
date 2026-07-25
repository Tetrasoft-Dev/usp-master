package gov.ao.usp.features.Utilizador.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ao.jcardoso.libs.exception.ResourceNotFoundException;
import gov.ao.usp.features.Utilizador.modelo.Perfil;
import gov.ao.usp.features.Utilizador.modelo.dto.CompletarPerfilRequest;
import gov.ao.usp.features.Utilizador.modelo.dto.PerfilRequestDTO;
import gov.ao.usp.features.Utilizador.modelo.dto.PerfilResponseDTO;
import gov.ao.usp.features.Utilizador.repository.PerfilRepository;
import gov.ao.usp.features.departamento.modelo.Departamento;
import gov.ao.usp.features.departamento.repository.DepartamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PerfilServiceImpl implements PerfilService{
    
    private final PerfilRepository perfilRepository;
    private final DepartamentoRepository departamentoRepository;

    @Transactional
    public Perfil atualizarPerfilPrincipal(Jwt jwt, PerfilRequestDTO dto) {
        // 1. Extrai o UUID do Supabase guardado no subject do Token
        UUID userId = UUID.fromString(jwt.getSubject());

        // 2. Procura o perfil correspondente na tabela do Postgres
        Perfil perfil = perfilRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Perfil do utilizador não encontrado"));

        // 3. Atualiza os campos necessários
        perfil.setNome(dto.getNome());
        perfil.setUpdatedAt(OffsetDateTime.now());

        // 4. Salva as modificações
        return perfilRepository.save(perfil);
    }

    @Override
    @Transactional
    public PerfilResponseDTO completarPerfil(Jwt jwt,CompletarPerfilRequest request) {   

        UUID userId = UUID.fromString(jwt.getSubject());
        Perfil perfil = perfilRepository.findById(userId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Utilizador não encontrado."));

        Departamento departamento = departamentoRepository.findById(request.departamentoId())
            .orElseThrow(() ->
                    new ResourceNotFoundException("Departamento não encontrado."));

        perfil.setNome(request.nome());
        perfil.setUsername(request.username());
        perfil.setNip(request.nip());
        perfil.setPerfil(request.perfil());
        perfil.setDepartamento(departamento);
        perfil = perfilRepository.save(perfil);

        return new PerfilResponseDTO(
            perfil.getId(),
            perfil.getNome(),
            perfil.getNip(),
            "",
            perfil.getUsername(),
            perfil.getPerfil(),
            null,"",
            perfil.getUpdatedAt()
    );

    }
}
