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
   
    @NotNull(message = "Name é um campo é obrigatório.")
    @NotBlank(message = "Name, este campo não pode ficar em branco.")
    @Schema(example = "Computador", description = "Computador")
    private String name;

    @NotNull(message = "Nome é um campo é obrigatório.")
    @NotBlank(message = "Nome, este campo não pode ficar em branco.")
    @Schema(example = "Computador", description = "Computador")
    private String nome;
    
    @NotNull(message = "Quantidade é um campo é obrigatório.")
    @NotBlank(message = "Quantidade, este campo não pode ficar em branco.")
    @Column(name ="quantidade_stock", nullable = true)
    private Integer quantidadeStock;

    @Column(name ="path_imagen", nullable = true)
    private String pathImagen;

    @Column(nullable = true)
    private String descricao;

    @Column(name = "status")
    private Boolean status;

    @NotNull(message = "Categoria é um campo é obrigatório.")
    @NotBlank(message = "Categoria, este campo não pode ficar em branco.")
    private UUID idCategoria;
}
