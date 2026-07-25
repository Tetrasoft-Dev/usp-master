package gov.ao.usp.features.Utilizador.modelo.dto;

import java.util.UUID;

import gov.ao.usp.features.Utilizador.modelo.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PerfilRequestDTO {
    @NotBlank(message = "O nome não pode estar vazio")
    private String nome;
    private String username;
    private String nip;
    private String senha;
    private UserRole perfil;
    @NotNull
    private UUID departamentoId;
}