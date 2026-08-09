package gov.ao.usp.features.solicitacao.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;

import ao.jcardoso.libs.paginacao.PageRequestDTO;
import ao.jcardoso.libs.paginacao.PageResponseDTO;
import gov.ao.usp.features.solicitacao.modelo.EstadoSolicitacao;
import gov.ao.usp.features.solicitacao.modelo.TipoSolicitacao;
import gov.ao.usp.features.solicitacao.modelo.dto.DevolucaoRequest;
import gov.ao.usp.features.solicitacao.modelo.dto.SolicitacaoEstatisticasResponse;
import gov.ao.usp.features.solicitacao.modelo.dto.SolicitacaoRequest;
import gov.ao.usp.features.solicitacao.modelo.dto.SolicitacaoResponse;

public interface SolicitacaoService {

    SolicitacaoResponse criar(Jwt jwt, SolicitacaoRequest request);
    SolicitacaoResponse aprovar(UUID id);
    SolicitacaoResponse rejeitar(UUID id);
    SolicitacaoResponse devolver( UUID id, DevolucaoRequest request);
    PageResponseDTO<SolicitacaoResponse> pesquisar(
        PageRequestDTO req, String nome,
        String nip, UUID perfilId, UUID departamentoId,
        UUID categoriaId, TipoSolicitacao tipoSolicitacao,
        EstadoSolicitacao estadoSolicitacao, LocalDateTime dataInicio,
        LocalDateTime dataFim, Boolean status
    );
    SolicitacaoResponse buscarPorId(UUID id);
    PageResponseDTO<SolicitacaoResponse> minhasSolicitacoes(Jwt jwt,PageRequestDTO dto);
    SolicitacaoResponse cancelar(UUID id, Jwt jwt);
    SolicitacaoEstatisticasResponse estatisticas(Jwt jwt,LocalDateTime dataInicio,LocalDateTime dataFim);

}