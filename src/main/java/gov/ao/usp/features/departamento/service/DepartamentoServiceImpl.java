package gov.ao.usp.features.departamento.service;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ao.jcardoso.libs.paginacao.PageRequestDTO;
import ao.jcardoso.libs.paginacao.PageResponseDTO;
import ao.jcardoso.libs.paginacao.PaginationUtils;
import ao.jcardoso.libs.exception.BusinessException;
import ao.jcardoso.libs.exception.ResourceNotFoundException;
import gov.ao.usp.features.auditoria.service.AuditoriaService;
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

    private final DepartamentoRepository reppository;
    private final DepartamentoMapper mapper;
    private final DepartamentoEditMapper editMapper;
    private final AuditoriaService service;

    @Override
    @Transactional
    public DepartamentoResponse criar(DepartamentoRequest req) {
        log.info("Criando um novo departamento: {}", req.getAbreviacao());
        if(reppository.existsByAbreviacao(req.getAbreviacao())){
            log.warn("Já existe uma categora como a descrição: {}", req.getAbreviacao());
            throw new BusinessException("Já existe um departamento como a descrição: " + req.getAbreviacao());
        }

        Departamento departamento = mapper.toEntity(req);
        departamento.setPkDepartamento(UUID.randomUUID());
        departamento.setStatus(Boolean.TRUE);
        var departamentoSalva = reppository.save(departamento);
        service.registrar( "Departamento", "Criar", departamento.getPkDepartamento() );
        log.info("Departamento criada com sucesso: {}", departamento.getAbreviacao());

        return mapper.toResponse(departamentoSalva);
    }

    @Override
    @Transactional
    public DepartamentoResponse editar(DepartamentoEditRequest req) {
        log.info("Editar uma departamento: {}", req.getAbreviacao());
        var departamento = reppository.findById(req.getId()).orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado."));

        if(reppository.existsByAbreviacao(departamento.getAbreviacao())){
            log.warn("Já existe um departamento como a descrição: {}", req.getAbreviacao());
            throw new BusinessException("Já existe uma departamento como a descrição: " + req.getAbreviacao());
        }

        editMapper.updateEntityFromDto(req, departamento);
        var departamentoSalva = reppository.save(departamento);
        service.registrar( "Departamento", "Editar", departamento.getPkDepartamento() );
        log.info("Departamento criado com sucesso: {}", departamento.getAbreviacao());
        return mapper.toResponse(departamentoSalva);
    }

    @Override
    @Transactional
    public DepartamentoResponse eliminar(UUID id) {
        log.info("Eliminar departamento: {}", id);
        var departamento = reppository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado."));
        departamento.setStatus(false);
        reppository.save(departamento);
        service.registrar( "Departamento", "Eliminar", departamento.getPkDepartamento() );
        log.info("Eliminado o departamento: {}", id);
        return mapper.toResponse(departamento);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartamentoResponse burcarPorID(UUID id) {
        log.info("Buscar departamento por ID: {}", id);
        var departamento = reppository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado."));
        service.registrar( "Departamento", "BuscarPorID", id );        
        log.info("Departamento encontrada: {}",id);       
        return mapper.toResponse(departamento);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<DepartamentoResponse> pesguisaEspecifica(PageRequestDTO req, String descricao, Boolean status) {
        log.info("Listando departamentos filtrados da organização: {}", descricao);
        Pageable pageable = PaginationUtils.buildPageable(req);

        Specification<Departamento> spec = DepartamentoSpecifications.filtrar(descricao, status);
        
        Page<Departamento> pageEntidade = reppository.findAll(spec, pageable);
        log.info("Departamentos listados com sucesso. Total de registos encontrados: {}", pageEntidade.getTotalElements());
        
        return PaginationUtils.buildPageResponse(pageEntidade, mapper::toResponse);
    }

}
