package gov.ao.usp.features.Utilizador.modelo.dto;

import java.util.UUID;

import gov.ao.usp.features.Utilizador.modelo.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CompletarPerfilRequest(
        @NotBlank
        String nome,
        @NotBlank
        String username,
        @NotBlank
        String nip,
        @NotNull
        UUID departamentoId,
        @NotNull
        UserRole perfil
) {}
