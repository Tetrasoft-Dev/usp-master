package gov.ao.usp.features.solicitacao.modelo.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record DevolucaoRequest(

    @NotEmpty(message = "Informe pelo menos um item para devolução.")
    @Valid
    List<DevolucaoItemRequest> itens

) {
}