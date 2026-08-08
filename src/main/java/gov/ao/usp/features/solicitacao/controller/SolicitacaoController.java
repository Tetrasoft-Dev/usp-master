package gov.ao.usp.features.solicitacao.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ao.jcardoso.libs.paginacao.PageRequestDTO;
import ao.jcardoso.libs.paginacao.PageResponseDTO;
import ao.jcardoso.libs.utils.http.ResponseHttp;
import ao.jcardoso.libs.utils.http.ResponseHttpBuilder;
import gov.ao.usp.features.solicitacao.modelo.EstadoSolicitacao;
import gov.ao.usp.features.solicitacao.modelo.TipoSolicitacao;
import gov.ao.usp.features.solicitacao.modelo.dto.DevolucaoRequest;
import gov.ao.usp.features.solicitacao.modelo.dto.SolicitacaoRequest;
import gov.ao.usp.features.solicitacao.modelo.dto.SolicitacaoResponse;
import gov.ao.usp.features.solicitacao.service.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usp/v1/solicitacao")
@RequiredArgsConstructor
@Validated
public class SolicitacaoController {

    private final SolicitacaoService service;

    @PostMapping
    public ResponseEntity<ResponseHttp<SolicitacaoResponse>> criar(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid SolicitacaoRequest request) {

        var response = service.criar(jwt, request);

        return ResponseHttpBuilder.ok("Solicitação criada com sucesso.", response);
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<ResponseHttp<SolicitacaoResponse>> aprovar(

            @PathVariable UUID id) {

        var response = service.aprovar(id);

        return ResponseHttpBuilder.ok(
                "Solicitação aprovada com sucesso.",
                response);
    }

    @PatchMapping("/{id}/rejeitar")
    public ResponseEntity<ResponseHttp<SolicitacaoResponse>> rejeitar(

            @PathVariable UUID id) {

        var response = service.rejeitar(id);

        return ResponseHttpBuilder.ok(
                "Solicitação rejeitada com sucesso.",
                response);
    }

    @PatchMapping("/{id}/devolver")
    public ResponseEntity<ResponseHttp<SolicitacaoResponse>> devolver(

            @PathVariable UUID id,

            @RequestBody @Valid DevolucaoRequest request) {

        var response = service.devolver(id, request);

        return ResponseHttpBuilder.ok(
                "Material devolvido com sucesso.",
                response);
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ResponseHttp<PageResponseDTO<SolicitacaoResponse>>> pesquisar(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "dataRegistro") String sortBy,

            @RequestParam(defaultValue = "DESC") String direction,

            @RequestParam(required = false) String nome,

            @RequestParam(required = false) String nip,

            @RequestParam(required = false) UUID perfilId,

            @RequestParam(required = false) UUID departamentoId,

            @RequestParam(required = false) UUID categoriaId,

            @RequestParam(required = false) TipoSolicitacao tipoSolicitacao,

            @RequestParam(required = false) EstadoSolicitacao estadoSolicitacao,

            @RequestParam(required = false) LocalDateTime dataInicio,

            @RequestParam(required = false) LocalDateTime dataFim,

            @RequestParam(required = false) Boolean status

    ) {

        PageRequestDTO dto = new PageRequestDTO();

        dto.setPage(page);
        dto.setSize(size);
        dto.setSortBy(sortBy);
        dto.setDirection(direction);

        var response = service.pesquisar(

                dto,
                nome,
                nip,
                perfilId,
                departamentoId,
                categoriaId,
                tipoSolicitacao,
                estadoSolicitacao,
                dataInicio,
                dataFim,
                status

        );

        return ResponseHttpBuilder.ok(
                "Lista de solicitações carregada com sucesso.",
                response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHttp<SolicitacaoResponse>> buscarPorId(
            @PathVariable UUID id) {

        var response = service.buscarPorId(id);

        return ResponseHttpBuilder.ok(
                "Solicitação encontrada com sucesso.",
                response);
    }

    @GetMapping("/minhas")
    public ResponseEntity<ResponseHttp<PageResponseDTO<SolicitacaoResponse>>> minhasSolicitacoes(

            @AuthenticationPrincipal Jwt jwt,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "dataRegistro") String sortBy,

            @RequestParam(defaultValue = "DESC") String direction

    ) {

        PageRequestDTO dto = new PageRequestDTO();

        dto.setPage(page);
        dto.setSize(size);
        dto.setSortBy(sortBy);
        dto.setDirection(direction);

        var response = service.minhasSolicitacoes(jwt, dto);

        return ResponseHttpBuilder.ok(
                "Solicitações carregadas com sucesso.",
                response);
    }


    @PatchMapping("/{id}/cancelar")
public ResponseEntity<ResponseHttp<SolicitacaoResponse>> cancelar(
        @PathVariable UUID id,
        @AuthenticationPrincipal Jwt jwt) {

    var response = service.cancelar(id, jwt);

    return ResponseHttpBuilder.ok(
            "Solicitação cancelada com sucesso.",
            response
    );
}














}