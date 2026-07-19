package gov.ao.usp.features.departamento.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import ao.jcardoso.libs.mapper.BaseMapper;
import gov.ao.usp.features.departamento.modelo.Departamento;
import gov.ao.usp.features.departamento.modelo.dto.DepartamentoRequest;
import gov.ao.usp.features.departamento.modelo.dto.DepartamentoResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DepartamentoMapper extends BaseMapper<Departamento, DepartamentoRequest, DepartamentoResponse> {

    DepartamentoMapper INSTANCE = Mappers.getMapper(DepartamentoMapper.class);

    @Mapping(source = "pkDepartamento", target = "id")
    DepartamentoResponse toResponse(Departamento entity);

    @Mapping(target = "pkDepartamento", ignore = true)
    Departamento toEntity(DepartamentoRequest request);

}
