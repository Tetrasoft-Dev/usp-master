package gov.ao.usp.features.categoria.modelo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados para criação da categoria")
public class CategoriaRequest {

    @NotNull(message = "Abreviação é um campo é obrigatório.")
    @NotBlank(message = "Abreviação, este campo não pode ficar em branco.")
    @Schema(example = "Electr", description = "Abreviaççao da categoria")
    private String abreviacao;

    @NotNull(message = "Descrição é um campo é obrigatório.")
    @NotBlank(message = "Descrição, este campo não pode ficar em branco.")
    @Schema(example = "Electronico", description = "Descrição da categoria")
    private String descricao;

}
