package gov.ao.usp.features.departamento.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import ao.jcardoso.libs.mapper.BaseMapper;
import gov.ao.usp.features.departamento.modelo.Departamento;
import gov.ao.usp.features.departamento.modelo.dto.DepartamentoEditRequest;
import gov.ao.usp.features.departamento.modelo.dto.DepartamentoResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DepartamentoEditMapper
        extends BaseMapper<Departamento, DepartamentoEditRequest, DepartamentoResponse> {

    DepartamentoMapper INSTANCE = Mappers.getMapper(DepartamentoMapper.class);
}
