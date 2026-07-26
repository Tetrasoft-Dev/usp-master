package gov.ao.usp.features.Utilizador.modelo.dto;

import java.util.UUID;

import gov.ao.usp.features.Utilizador.modelo.UserRole;

public record PerfilRequestDTO(
    String nome,
    String username,
    String nip,
    UUID departamentoId,
    UserRole perfil
) {}
