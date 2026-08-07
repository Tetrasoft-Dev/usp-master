package gov.ao.usp.features.solicitacao.modelo.dto;

import java.time.LocalDateTime;
import java.util.List;

import gov.ao.usp.features.solicitacao.modelo.TipoSolicitacao;

public record SolicitacaoRequest(

    TipoSolicitacao tipo,
    String notaInformativa,
    LocalDateTime dataInicio,
    LocalDateTime dataTermino,
    List<ItemSolicitacaoRequest> itens
) {}