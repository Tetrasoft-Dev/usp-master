package gov.ao.usp.features.Utilizador.modelo.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import gov.ao.usp.features.Utilizador.modelo.Perfil;
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
){

    public static PerfilResponseDTO fromEntity(Perfil perfil) {
        return new PerfilResponseDTO(
            perfil.getId(),
            perfil.getNome(),
            perfil.getNip(),
            "",
            perfil.getUsername(),
            perfil.getPerfil(),
            perfil.getDepartamento() != null ? perfil.getDepartamento().getPkDepartamento() : null,
            perfil.getDepartamento() != null ? perfil.getDepartamento().getAbreviacao() : null,
            perfil.getUpdatedAt()
        );
    }
}
