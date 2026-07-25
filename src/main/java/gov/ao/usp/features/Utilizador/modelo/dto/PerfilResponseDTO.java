package gov.ao.usp.features.Utilizador.modelo.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import gov.ao.usp.features.Utilizador.modelo.UserRole;

public record PerfilResponseDTO(

    UUID id,
    String nome,
    String nip,
    String email,
    String username,
    UserRole perfil,
    UUID departamentoId,
    String departamento,
    OffsetDateTime updatedAt
){}
