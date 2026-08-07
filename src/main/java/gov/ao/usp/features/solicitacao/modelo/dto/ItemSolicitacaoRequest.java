package gov.ao.usp.features.solicitacao.modelo.dto;

import java.util.UUID;

public record ItemSolicitacaoRequest(
    UUID artigoId,
    Integer quantidade) {
}