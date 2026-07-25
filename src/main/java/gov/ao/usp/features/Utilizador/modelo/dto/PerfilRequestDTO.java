package gov.ao.usp.features.Utilizador.modelo.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PerfilRequestDTO {
    @NotBlank(message = "O nome não pode estar vazio")
    private String nome;
}