package gov.ao.usp.features.artigo.modelo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ArtigoAtualizarStockRequest {
    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 0, message = "A quantidade não pode ser negativa.")
    private Integer quantidade;

    private Integer quantidadeDanificada = 0;

    @NotBlank(message = "O estado (Aprovado/Devolução) é obrigatório.")
    private String estado;
}

