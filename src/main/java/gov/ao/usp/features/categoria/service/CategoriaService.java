package gov.ao.usp.features.categoria.service;

import java.util.UUID;

import ao.jcardoso.libs.paginacao.PageRequestDTO;
import ao.jcardoso.libs.paginacao.PageResponseDTO;
import gov.ao.usp.features.categoria.modelo.dto.CategoriaRequest;
import gov.ao.usp.features.categoria.modelo.dto.CategoriaResponse;

public interface CategoriaService {

    CategoriaResponse criar(CategoriaRequest req);

    CategoriaResponse editar(CategoriaRequest req);

    CategoriaResponse eliminar(UUID id);

    CategoriaResponse burcarPorID(UUID id);

    PageResponseDTO<CategoriaResponse> pesguisaEspecifica(PageRequestDTO req, String descricao, Boolean status);
}
