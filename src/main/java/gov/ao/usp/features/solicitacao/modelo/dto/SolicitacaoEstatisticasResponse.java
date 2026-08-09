package gov.ao.usp.features.solicitacao.modelo.dto;

public record SolicitacaoEstatisticasResponse(

    Integer total,
    Integer solicitacao,
    Integer aprovadas,
    Integer rejeitadas,
    Integer devolvidas,
    Integer visualizadas,
    Integer canceladas
) {
}
