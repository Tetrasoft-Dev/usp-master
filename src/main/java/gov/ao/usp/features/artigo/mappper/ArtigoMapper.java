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

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ArtigoMapper  extends BaseMapper<Artigo, ArtigoRequest, ArtigoResponse> {
    
    ArtigoMapper INSTANCE = Mappers.getMapper(ArtigoMapper.class);

    @Override
    @Mapping(source = "pkArtigo", target = "id")
    // Mapeamentos da Categoria vinculada ao Artigo
    @Mapping(source = "categoria.pkCategoria", target = "idCategoria")
    @Mapping(source = "categoria.abreviacao", target = "nomeCategoria") // Ajusta se na entidade Categoria for outro campo (ex: descricao)
    // Expressão Java inline para gerar a URL dinâmica do Codespaces/Docker num Record imutável
    @Mapping(target = "pathImagen", expression = "java(gov.ao.usp.features.artigo.mappper.ArtigoMapper.gerarUrlPublica(entity.getPathImagen()))")
    ArtigoResponse toResponse(Artigo entity);

    @Override
    @Mapping(target = "pkArtigo", ignore = true)
    @Mapping(target = "pathImagen", ignore = true)
    @Mapping(target = "categoria", ignore = true) // Tratado no Service ao associar pelo ID da categoria
    Artigo toEntity(ArtigoRequest request);

    /**
     * Este método é executado automaticamente pelo MapStruct imediatamente após construir o ArtigoResponse.
     * Ele resolve de forma dinâmica a URL pública do Codespaces baseando-se na requisição HTTP atual.
     */
    static String gerarUrlPublica(String pathImagen) {
        if (pathImagen == null || pathImagen.isEmpty()) {
            return null;
        }
        try {
            return org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(pathImagen)
                    .toUriString();
        } catch (Exception e) {
            // Fallback de segurança para testes unitários ou contextos fora de requisições HTTP
            return pathImagen;
        }
    }
}