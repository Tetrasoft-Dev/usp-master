package gov.ao.usp.features.artigo.service;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import ao.jcardoso.libs.paginacao.PageRequestDTO;
import ao.jcardoso.libs.paginacao.PageResponseDTO;
import gov.ao.usp.features.artigo.modelo.dto.ArtigoEditRequest;
import gov.ao.usp.features.artigo.modelo.dto.ArtigoRequest;
import gov.ao.usp.features.artigo.modelo.dto.ArtigoResponse;
import gov.ao.usp.features.solicitacao.modelo.EstadoSolicitacao;

public interface ArtigoService {
    
    ArtigoResponse criar(ArtigoRequest req, MultipartFile imagem);

    ArtigoResponse editar(ArtigoEditRequest req, MultipartFile imagem);

    ArtigoResponse eliminar(UUID id);

    ArtigoResponse buscarPorId(UUID id);

    PageResponseDTO<ArtigoResponse> pesquisaEspecifica(PageRequestDTO req, String nome, UUID idCategoria, Boolean status);

    ArtigoResponse atualizarQuantidade(UUID id, Integer quantidade, Integer quantidadeDanificada, EstadoSolicitacao estado);
}
