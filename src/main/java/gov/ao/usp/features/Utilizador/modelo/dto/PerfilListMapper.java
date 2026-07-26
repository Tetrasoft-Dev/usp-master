package gov.ao.usp.features.Utilizador.modelo.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import gov.ao.usp.features.Utilizador.modelo.Perfil;

@Mapper(componentModel = "spring")
public interface PerfilListMapper {

    @Mapping(target = "departamentoId", source = "departamento.pkDepartamento")
    @Mapping(target = "departamento", source = "departamento.descricao")
    PerfilResponseDTO toResponse(Perfil perfil);
}