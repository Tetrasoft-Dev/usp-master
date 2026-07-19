package gov.ao.usp.features.departamento.modelo.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados para editar o departamento")
public class DepartamentoEditRequest {

    @NotNull(message = "ID é um campo obrigatório.")
    @Schema(example = "123e4567-e89b-12d3-a456-426614174000", description = "ID do departamento")
    private UUID id;

    @Schema(example = "Electr", description = "Abreviaççao do departamento")
    private String abreviacao;

    @Schema(example = "Electronico", description = "Descrição do departamento")
    private String descricao;
}
