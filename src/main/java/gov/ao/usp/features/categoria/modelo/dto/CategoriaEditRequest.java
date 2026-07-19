package gov.ao.usp.features.categoria.modelo.dto;

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
@Schema(description = "Dados para editar a categoria")
public class CategoriaEditRequest {

    @NotNull(message = "ID é um campo obrigatório.")
    @Schema(example = "123e4567-e89b-12d3-a456-426614174000", description = "ID da categoria")
    private UUID id;

    @Schema(example = "Electr", description = "Abreviaççao da categoria")
    private String abreviacao;

    @Schema(example = "Electronico", description = "Descrição da categoria")
    private String descricao;
}
