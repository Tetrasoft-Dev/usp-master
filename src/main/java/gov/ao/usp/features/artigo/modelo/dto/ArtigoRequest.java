package gov.ao.usp.features.artigo.modelo.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
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
@Schema(description = "Dados para criação do artigo")
public class  ArtigoRequest {
   
    private String name;
    @NotNull(message = "Nome é um campo é obrigatório.")
    @NotBlank(message = "Nome, este campo não pode ficar em branco.")
    @Schema(example = "Computador", description = "Computador")
    private String nome;
    @NotNull(message = "Quantidade é um campo é obrigatório.")
    private Integer quantidadeStock;
    private String pathImagen;
    private String descricao;
    private Boolean status;
    @NotNull(message = "Categoria é um campo é obrigatório.")
    private UUID idCategoria;
}
