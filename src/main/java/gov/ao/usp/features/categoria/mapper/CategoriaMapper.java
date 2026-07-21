package gov.ao.usp.features.categoria.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import ao.jcardoso.libs.mapper.BaseMapper;
import gov.ao.usp.features.categoria.modelo.Categoria;
import gov.ao.usp.features.categoria.modelo.dto.CategoriaRequest;
import gov.ao.usp.features.categoria.modelo.dto.CategoriaResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CategoriaMapper extends BaseMapper<Categoria, CategoriaRequest, CategoriaResponse> {

    //CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);

    @Mapping(source = "pkCategoria", target = "id")
    CategoriaResponse toResponse(Categoria entity);

    @Mapping(target = "pkCategoria", ignore = true)
    Categoria toEntity(CategoriaRequest request);

}
