package gov.ao.usp.features.departamento.service;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ao.jcardoso.libs.paginacao.PageRequestDTO;
import ao.jcardoso.libs.paginacao.PageResponseDTO;
import gov.ao.usp.features.departamento.mapper.DepartamentoEditMapper;
import gov.ao.usp.features.departamento.mapper.DepartamentoMapper;
import gov.ao.usp.features.departamento.modelo.Departamento;
import gov.ao.usp.features.departamento.modelo.dto.DepartamentoEditRequest;
import gov.ao.usp.features.departamento.modelo.dto.DepartamentoRequest;
import gov.ao.usp.features.departamento.modelo.dto.DepartamentoResponse;
import gov.ao.usp.features.departamento.repository.DepartamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {

    private DepartamentoRepository reppository;
    private DepartamentoMapper mapper;
    private DepartamentoEditMapper editMapper;

    @Override
    @Transactional
    public DepartamentoResponse criar(DepartamentoRequest req) {
        log.info("Criando um novo departamento: {}", req.getAbreviacao());
        if(reppository.existsByAbreviacao(req.getAbreviacao())){
            log.warn("Já existe uma categora como a descrição: {}", req.getAbreviacao());
            throw new ConflictException("Já existe um departamento como a descrição: " + req.getAbreviacao());
        }

        Departamento departamento = mapper.toEntity(req);
        departamento.setPkDepartamento(UUID.randomUUID());
        departamento.setStatus(Boolean.TRUE);
        var departamentoSalva = reppository.save(Departamento);

        log.info("Departamento criada com sucesso: {}", departamentoSalva.getAbreviacao);

        return mapper.toResponse(departamentoSalva);
    }

    @Override
    @Transactional
    public DepartamentoResponse editar(DepartamentoEditRequest req) {
        log.info("Editar uma departamento: {}", req.getAbreviacao());
        var departamento = reppository.findById(req.getId).orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado."));

        if(reppository.existsByAbreviacao(departamento.getAbreviacao())){
            log.warn("Já existe um departamento como a descrição: {}", req.getAbreviacao());
            throw new ConflictException("Já existe uma departamento como a descrição: " + req.getAbreviacao());
        }

        editMapper.updateEntityFromDto(req, departamento);
        var departamentoSalva = reppository.save(departamento);

        log.info("Departamento criado com sucesso: {}", departamentoSalva.getAbreviacao);
        return mapper.toResponse(departamentoSalva);
    }

    @Override
    @Transactional
    public DepartamentoResponse eliminar(UUID id) {
        log.info("Eliminar departamento: {}", req.getAbreviacao());
        var departamento = reppository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado."));
        departamento.setStatus(false);
        reppository.save(departamento);
        log.info("Eliminado o departamento: {}", req.getAbreviacao());
        return mapper.toResponse(departamento);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartamentoResponse burcarPorID(UUID id) {
        log.info("Buscar departamento por ID: {}", id);
        var departamento = reppository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado."));
        log.info("Departamento encontrada: {}",departamento.getAbreviacao());       
        return mapper.toResponse(departamento);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<DepartamentoResponse> pesguisaEspecifica(PageRequestDTO req, String descricao, Boolean status) {
        log.info("Listando departamentos filtrados da organização: {}", descricao);
        Pageable pageable = PaginationUtils.buildPageable(req);

        Specification<DepartamentoResponse> spec = DepartamentoSpecifications.filtrar(nome, status);
        Page<DepartamentoResponse> page = reppository.findAll(spec, pageable);

        log.info("Departamentos listados com sucesso. Total de registos encontrados: {}", page.getTotalElements());
        return PaginationUtils.buildPageResponse(page);
    }

}
