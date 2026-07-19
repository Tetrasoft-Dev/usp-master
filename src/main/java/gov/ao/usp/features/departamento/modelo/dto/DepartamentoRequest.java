package gov.ao.usp.features.departamento.modelo.dto;

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
@Schema(description = "Dados para criação do departamento")
public class DepartamentoRequest {

    @NotNull(message = "Abreviação é um campo é obrigatório.")
    @NotBlank(message = "Abreviação, este campo não pode ficar em branco.")
    @Schema(example = "Electr", description = "Abreviaççao do departamento")
    private String abreviacao;

    @NotNull(message = "Descrição é um campo é obrigatório.")
    @NotBlank(message = "Descrição, este campo não pode ficar em branco.")
    @Schema(example = "Electronico", description = "Descrição do departamento")
    private String descricao;

}
