package gov.ao.usp.features.artigo.mappper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import ao.jcardoso.libs.mapper.BaseMapper;
import gov.ao.usp.features.artigo.modelo.Artigo;
import gov.ao.usp.features.artigo.modelo.dto.ArtigoEditRequest;
import gov.ao.usp.features.artigo.modelo.dto.ArtigoResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ArtigoEditMapper  extends BaseMapper<Artigo, ArtigoEditRequest, ArtigoResponse> {
    
    ArtigoMapper INSTANCE = Mappers.getMapper(ArtigoMapper.class);

}
