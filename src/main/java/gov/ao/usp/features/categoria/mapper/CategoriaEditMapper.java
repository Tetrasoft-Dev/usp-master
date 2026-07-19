package gov.ao.usp.features.categoria.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import ao.jcardoso.libs.mapper.BaseMapper;
import gov.ao.usp.features.categoria.modelo.Categoria;
import gov.ao.usp.features.categoria.modelo.dto.CategoriaEditRequest;
import gov.ao.usp.features.categoria.modelo.dto.CategoriaResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CategoriaEditMapper extends BaseMapper<Categoria, CategoriaEditRequest, CategoriaResponse> {

    CategoriaEditMapper INSTANCE = Mappers.getMapper(CategoriaEditMapper.class);
}
