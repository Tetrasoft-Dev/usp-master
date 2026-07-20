package gov.ao.usp.features.departamento.service;

import java.util.UUID;

import ao.jcardoso.libs.paginacao.PageRequestDTO;
import ao.jcardoso.libs.paginacao.PageResponseDTO;
import gov.ao.usp.features.depatamento.modelo.dto.DepatamentoRequest;
import gov.ao.usp.features.depatamento.modelo.dto.DepatamentoEditRequest;
import gov.ao.usp.features.depatamento.modelo.dto.DepatamentoResponse;

public interface DepatamentoService {

    DepatamentoResponse criar(DepatamentoRequest req);

    DepatamentoResponse editar(DepatamentoEditRequest req);

    DepatamentoResponse eliminar(UUID id);

    DepatamentoResponse burcarPorID(UUID id);

    PageResponseDTO<DepatamentoResponse> pesguisaEspecifica(PageRequestDTO req, String descricao, Boolean status);
}