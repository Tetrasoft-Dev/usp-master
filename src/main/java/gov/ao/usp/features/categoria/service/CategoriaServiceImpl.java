package gov.ao.usp.features.categoria.service;

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
import gov.ao.usp.features.categoria.mapper.CategoriaEditMapper;
import gov.ao.usp.features.categoria.mapper.CategoriaMapper;
import gov.ao.usp.features.categoria.modelo.Categoria;
import gov.ao.usp.features.categoria.modelo.dto.CategoriaEditRequest;
import gov.ao.usp.features.categoria.modelo.dto.CategoriaRequest;
import gov.ao.usp.features.categoria.modelo.dto.CategoriaResponse;
import gov.ao.usp.features.categoria.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository reppository;
    private final CategoriaMapper mapper;
    private final CategoriaEditMapper editMapper;
    private final AuditoriaService service;

    @Override
    @Transactional
    public CategoriaResponse criar(CategoriaRequest req) {
        log.info("Criando uma nova categoria: {}", req.getAbreviacao());
       
        
        if(reppository.existsByAbreviacao(req.getAbreviacao())){
            log.warn("Já existe uma categora como a descrição: {}", req.getAbreviacao());
            throw new BusinessException("Já existe uma categora como a descrição: " + req.getAbreviacao());
        }
        
        Categoria entity = mapper.toEntity(req);
        entity.setPkCategoria(UUID.randomUUID());
        entity.setStatus(Boolean.TRUE);
        var entitySalva = reppository.save(entity);

        service.registrar( "Categoria", "Registo Salvo", entity.getPkCategoria() );
        
        log.info("Categoria criada com sucesso: {}", entity.getAbreviacao());

        return mapper.toResponse(entitySalva);
    }

    @Override
    @Transactional
    public CategoriaResponse editar(CategoriaEditRequest req) {
        log.info("Editar uma categoria: {}", req.getAbreviacao());
        var categoriaExistente = reppository.findById(req.getId()).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));
        if(reppository.existsByAbreviacao(req.getAbreviacao())){
            log.warn("Já existe uma categora como a descrição: {}", req.getAbreviacao());
            throw new BusinessException("Já existe uma categora como a descrição: " + req.getAbreviacao());
        }
        editMapper.updateEntityFromDto(req, categoriaExistente);
        categoriaExistente.setStatus(Boolean.TRUE);
        reppository.save(categoriaExistente);
        service.registrar( "Categoria", "Registro Editando", categoriaExistente.getPkCategoria() );
        log.info("Categoria criada com sucesso: {}", categoriaExistente.getAbreviacao());
        return mapper.toResponse(categoriaExistente);
    }

    @Override
    @Transactional
    public CategoriaResponse eliminar(UUID id) {
        log.info("Eliminar categoria: {}", id);
        var categoriaExistente = reppository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));
        categoriaExistente.setStatus(false);
        reppository.save(categoriaExistente);
        service.registrar( "Categoria", "Registro Eliminado", categoriaExistente.getPkCategoria() );
        log.info("Eliminada a categoria: {}", id);
        return mapper.toResponse(categoriaExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse burcarPorID(UUID id) {
        log.info("Buscar categoria por ID: {}", id);
        var categoria = reppository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));
        service.registrar( "Categoria", "BurcarPorID", id );
        log.info("Categoria encontrada: {}",id);       
        return mapper.toResponse(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<CategoriaResponse> pesguisaEspecifica(PageRequestDTO req, String descricao, Boolean status) {
        log.info("Listando categorias filtrados da organização: {}", descricao);
        Pageable pageable = PaginationUtils.buildPageable(req);

        Specification<Categoria> spec = CategoriaSpecifications.filtrar(descricao, status);
        
        Page<Categoria> page = reppository.findAll(spec, pageable);
       // service.registrar( "Categoria", "BurcarListaDeCategoria", null );
        log.info("Categorias listados com sucesso. Total de registos encontrados: {}", page.getTotalElements());
        return PaginationUtils.buildPageResponse(page, mapper::toResponse);
    }

}
