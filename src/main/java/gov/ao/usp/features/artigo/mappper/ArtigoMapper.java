package gov.ao.usp.features.artigo.mappper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ao.jcardoso.libs.mapper.BaseMapper;
import gov.ao.usp.features.artigo.modelo.Artigo;
import gov.ao.usp.features.artigo.modelo.dto.ArtigoRequest;
import gov.ao.usp.features.artigo.modelo.dto.ArtigoResponse;

@Mapper(componentModel = "spring", uses = UrlMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ArtigoMapper extends BaseMapper<Artigo, ArtigoRequest, ArtigoResponse> {

    ArtigoMapper INSTANCE = Mappers.getMapper(ArtigoMapper.class);

    @Override
    @Mapping(source = "pkArtigo", target = "id")
    // Mapeamentos da Categoria vinculada ao Artigo
    @Mapping(source = "categoria.pkCategoria", target = "idCategoria")
    @Mapping(source = "categoria.abreviacao", target = "nomeCategoria") // Ajusta se na entidade Categoria for outro
                                                                        // campo (ex: descricao)
    // Expressão Java inline para gerar a URL dinâmica do Codespaces/Docker num
    @Mapping(source = "pathImagen", target = "pathImagen", qualifiedByName = "gerarUrlImagem")
    ArtigoResponse toResponse(Artigo entity);

    @Override
    @Mapping(target = "pkArtigo", ignore = true)
    @Mapping(target = "pathImagen", ignore = true)
    @Mapping(target = "categoria", ignore = true) // Tratado no Service ao associar pelo ID da categoria
    Artigo toEntity(ArtigoRequest request);

}