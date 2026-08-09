package gov.ao.usp.features.solicitacao.modelo.dto;

public record SolicitacaoEstatisticasResponse(

    long total,
    long solicitacao,
    long aprovadas,
    long rejeitadas,
    long devolvidas,
    long visualizadas,
    long canceladas

) {
}
