package gov.ao.usp.features.solicitacao.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import ao.jcardoso.libs.mapper.BaseMapper;
import gov.ao.usp.features.artigo.mappper.ArtigoMapper;
import gov.ao.usp.features.solicitacao.modelo.ItemSolicitacao;
import gov.ao.usp.features.solicitacao.modelo.Solicitacao;
import gov.ao.usp.features.solicitacao.modelo.dto.ItemSolicitacaoRequest;
import gov.ao.usp.features.solicitacao.modelo.dto.ItemSolicitacaoResponse;
import gov.ao.usp.features.solicitacao.modelo.dto.SolicitacaoRequest;
import gov.ao.usp.features.solicitacao.modelo.dto.SolicitacaoResponse;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
                ArtigoMapper.class
        }
)
public interface SolicitacaoMapper extends BaseMapper<
        Solicitacao,
        SolicitacaoRequest,
        SolicitacaoResponse> {

    @Override
    @Mapping(source = "pkSolicitacao", target = "id")
    @Mapping(source = "perfil.id", target = "solicitanteId")
    @Mapping(source = "perfil.nome", target = "solicitante")
    @Mapping(source = "perfil.departamento.pkDepartamento", target = "departamentoId")
    @Mapping(source = "perfil.departamento.abreviacao", target = "departamento")
    SolicitacaoResponse toResponse(Solicitacao entity);

    @Override
    @Mapping(target = "pkSolicitacao", ignore = true)
    @Mapping(target = "perfil", ignore = true)
    @Mapping(target = "itens", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dataRegistro", ignore = true)
    @Mapping(target = "estadoDaSolicitacao", ignore = true)
    Solicitacao toEntity(SolicitacaoRequest request);

    //--------------------------------------------------
    // Item da Solicitação
    //--------------------------------------------------

    @Mapping(source = "pkItemSolicitacao", target = "id")
    @Mapping(source = "artigo.pkArtigo", target = "artigoId")
    @Mapping(source = "artigo.nome", target = "artigo")
    @Mapping(source = "artigo.pathImagen", target = "imagem")
    ItemSolicitacaoResponse toResponse(ItemSolicitacao entity);

    @Mapping(target = "pkItemSolicitacao", ignore = true)
    @Mapping(target = "artigo", ignore = true)
    @Mapping(target = "solicitacao", ignore = true)
    @Mapping(target = "quantidadeExtraviada", ignore = true)
    @Mapping(target = "quantidadeDanificado", ignore = true)
    ItemSolicitacao toEntity(ItemSolicitacaoRequest request);

}