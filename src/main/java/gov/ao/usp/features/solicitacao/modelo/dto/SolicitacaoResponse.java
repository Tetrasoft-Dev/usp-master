package gov.ao.usp.features.solicitacao.modelo.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import gov.ao.usp.features.solicitacao.modelo.EstadoSolicitacao;
import gov.ao.usp.features.solicitacao.modelo.TipoSolicitacao;

public record SolicitacaoResponse(
    UUID id,
    String solicitante,
    UUID solicitanteId,
    String departamento,
    UUID departamentoId,
    TipoSolicitacao tipo,
    EstadoSolicitacao estado,
    String notaInformativa,
    LocalDateTime dataInicio,
    LocalDateTime dataTermino,
    OffsetDateTime dataRegistro,
    Boolean status,
    List<ItemSolicitacaoResponse> itens
) {}