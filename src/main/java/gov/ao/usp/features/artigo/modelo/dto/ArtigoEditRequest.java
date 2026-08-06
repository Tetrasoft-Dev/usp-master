package gov.ao.usp.features.artigo.modelo.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Dados para editar artigo")
public class ArtigoEditRequest {

    @NotNull(message = "Artigo é um campo é obrigatório.")
    private UUID id; 
    private String name;
    private String nome;
    private Integer quantidadeStock;
    private String pathImagen;
    private String descricao;
    private Boolean status;
    private UUID idCategoria;
}
