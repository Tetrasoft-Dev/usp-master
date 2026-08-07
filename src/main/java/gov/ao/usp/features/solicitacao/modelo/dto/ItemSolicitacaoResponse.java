package gov.ao.usp.features.solicitacao.modelo.dto;

import java.util.UUID;

public record ItemSolicitacaoResponse(
    UUID id,
    UUID artigoId,
    String artigo,
    String imagem,
    Integer quantidadeSolicitada,
    Integer quantidadeExtraviada,
    Integer quantidadeDanificada
) {}