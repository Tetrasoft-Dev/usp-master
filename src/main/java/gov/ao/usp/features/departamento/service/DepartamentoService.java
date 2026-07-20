package gov.ao.usp.features.departamento.service;

import java.util.UUID;

import ao.jcardoso.libs.paginacao.PageRequestDTO;
import ao.jcardoso.libs.paginacao.PageResponseDTO;
import gov.ao.usp.features.departamento.modelo.dto.DepartamentoRequest;
import gov.ao.usp.features.departamento.modelo.dto.DepartamentoEditRequest;
import gov.ao.usp.features.departamento.modelo.dto.DepartamentoResponse;

public interface DepartamentoService {

    DepartamentoResponse criar(DepartamentoRequest req);

    DepartamentoResponse editar(DepartamentoEditRequest req);

    DepartamentoResponse eliminar(UUID id);

    DepartamentoResponse burcarPorID(UUID id);

    PageResponseDTO<DepartamentoResponse> pesguisaEspecifica(PageRequestDTO req, String descricao, Boolean status);
}