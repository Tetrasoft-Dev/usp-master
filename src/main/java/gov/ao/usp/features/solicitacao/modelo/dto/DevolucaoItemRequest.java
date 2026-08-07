package gov.ao.usp.features.solicitacao.modelo.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DevolucaoItemRequest(

    @NotNull
    UUID itemSolicitacaoId,
    @NotNull
    @Min(0)
    Integer quantidadeDanificada,
    @NotNull
    @Min(0)
    Integer quantidadeExtraviada
) {
}
