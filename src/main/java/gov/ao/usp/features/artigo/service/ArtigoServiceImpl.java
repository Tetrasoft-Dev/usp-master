package gov.ao.usp.features.artigo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ao.jcardoso.libs.paginacao.PageRequestDTO;
import ao.jcardoso.libs.paginacao.PageResponseDTO;
import ao.jcardoso.libs.paginacao.PaginationUtils;
import gov.ao.usp.features.artigo.mappper.ArtigoEditMapper;
import gov.ao.usp.features.artigo.mappper.ArtigoMapper;
import gov.ao.usp.features.artigo.modelo.Artigo;
import gov.ao.usp.features.artigo.modelo.dto.ArtigoEditRequest;
import gov.ao.usp.features.artigo.modelo.dto.ArtigoRequest;
import gov.ao.usp.features.artigo.modelo.dto.ArtigoResponse;
import gov.ao.usp.features.artigo.repository.ArtigoRepository;
import gov.ao.usp.features.auditoria.service.AuditoriaService;
import gov.ao.usp.features.categoria.modelo.Categoria;
import ao.jcardoso.libs.exception.BusinessException;
import ao.jcardoso.libs.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArtigoServiceImpl implements ArtigoService {

    private final ArtigoRepository repository;
    private final ArtigoMapper mapper;
    private final ArtigoEditMapper editMapper;
    private final AuditoriaService auditoriaService;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public ArtigoResponse criar(ArtigoRequest req, MultipartFile imagem) {
        log.info("Criando uma nova artigo: {}", req.getNome());

        if (repository.existsByNome(req.getNome())) {
            throw new BusinessException("Já existe um artigo com a descrição: " + req.getNome());
        }

        Artigo entity = mapper.toEntity(req);
        entity.setPkArtigo(UUID.randomUUID());
        entity.setQuantidadeStockDisponivel(req.getQuantidadeStock());
        entity.setCategoria(new Categoria(req.getIdCategoria()));
        entity.setStatus(Boolean.TRUE);
        entity.setName(req.getNome());

        // LÓGICA DE SALVAR A IMAGEM NO DOCKER
        if (imagem != null && !imagem.isEmpty()) {
            String nomeImagemSalva = salvarImagemNoDisco(imagem);
            entity.setPathImagen("/uploads/" + nomeImagemSalva); // Guarda o URL relativo no banco
        }

        var entitySalva = repository.save(entity);
        auditoriaService.registrar("Artigo", "Registo Salvo", entitySalva.getPkArtigo());
        return mapper.toResponse(entitySalva);
    }

    @Override
    public ArtigoResponse editar(ArtigoEditRequest req, MultipartFile imagem) {
        log.info("Editando um artigo existente: {}", req.getNome());

        var artigoExistente = repository.findById(req.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado."));
        int total = artigoExistente.getQuantidadeStock()+ req.getQuantidadeStock(); 
        int totalDisponivel = artigoExistente.getQuantidadeStockDisponivel();       
        editMapper.updateEntityFromDto(req, artigoExistente);
        // Se o nome mudou, valida duplicação
        if (!artigoExistente.getNome().equalsIgnoreCase(req.getNome()) && repository.existsByNome(req.getNome())) {
            log.warn("Já existe um artigo com a descrição: {}", req.getNome());
            throw new BusinessException("Já existe um artigo com a descrição: " + req.getNome());
        }

        if (req.getQuantidadeStock() != null && req.getQuantidadeStock() != 0) {
            
            System.out.println("total : "+ total);
            artigoExistente.setQuantidadeStock(total);
            artigoExistente.setQuantidadeStockDisponivel(totalDisponivel + req.getQuantidadeStock());
        }

        // LÓGICA DE SALVAR A IMAGEM NO DOCKER
        if (imagem != null && !imagem.isEmpty()) {
            String nomeImagemSalva = salvarImagemNoDisco(imagem);
            artigoExistente.setPathImagen("/uploads/" + nomeImagemSalva); // Guarda o URL relativo no banco
        }
        
        artigoExistente.setName(req.getNome());
        artigoExistente.setStatus(Boolean.TRUE);

        repository.save(artigoExistente);
        auditoriaService.registrar("Artigo", "Registro Editado", artigoExistente.getPkArtigo());

        log.info("Artigo editado com sucesso: {}", artigoExistente.getNome());
        return mapper.toResponse(artigoExistente);
    }

    @Override
    public ArtigoResponse eliminar(UUID id) {
        log.info("Eliminar artigo: {}", id);

        var artigoExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado."));

        artigoExistente.setStatus(false);
        repository.save(artigoExistente);

        auditoriaService.registrar("Artigo", "Registro Eliminado", artigoExistente.getPkArtigo());
        log.info("Eliminado o artigo com sucesso: {}", id);
        return mapper.toResponse(artigoExistente);
    }

    @Override
    public ArtigoResponse buscarPorId(UUID id) {
        log.info("Buscar artigo por ID: {}", id);

        var artigoExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado."));

        auditoriaService.registrar("Artigo", "BuscarPorID", id);
        log.info("Artigo encontrado: {}", id);
        return mapper.toResponse(artigoExistente);
    }

    @Override
    public PageResponseDTO<ArtigoResponse> pesquisaEspecifica(PageRequestDTO req, String nome, UUID idCategoria,
            Boolean status) {
        log.info("Listando artigos filtrados: {}", nome);

        Pageable pageable = PaginationUtils.buildPageable(req);
        Specification<Artigo> spec = ArtigoSpecifications.filtrar(nome, idCategoria, status);
        Page<Artigo> page = repository.findAll(spec, pageable);

        //auditoriaService.registrar("Artigo", "BuscarListaDeArtigos", null);
        log.info("Artigos listados com sucesso. Total de registos encontrados: {}", page.getTotalElements());
        return PaginationUtils.buildPageResponse(page, mapper::toResponse);
    }

    @Override
    public ArtigoResponse atualizarQuantidade(UUID id, Integer quantidade, Integer quantidadeDanificada,String estado) {
        log.info("Operação de atualização de stock: {}", estado);

        var artigoExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado."));

        if ("Aprovado".equalsIgnoreCase(estado)) {
            // Correção da lógica: deve haver stock disponível MAIOR ou IGUAL à quantidade
            // solicitada
            if (artigoExistente.getQuantidadeStockDisponivel() >= quantidade) {
                artigoExistente
                        .setQuantidadeStockDisponivel(artigoExistente.getQuantidadeStockDisponivel() - quantidade);
                artigoExistente
                        .setQuantidadeStockIndisponivel(artigoExistente.getQuantidadeStockIndisponivel() + quantidade);
            } else {
                throw new BusinessException(
                        "Não existe quantidade suficiente para aprovação ou reduza a quantidade solicitada.");
            }
        } else if ("Devolução".equalsIgnoreCase(estado)) {
            if (quantidade != null && quantidade != 0) {
                artigoExistente
                        .setQuantidadeStockDisponivel(artigoExistente.getQuantidadeStockDisponivel() + quantidade);
                artigoExistente
                        .setQuantidadeStockIndisponivel(artigoExistente.getQuantidadeStockIndisponivel() - quantidade);
            }

            if (quantidadeDanificada != null && quantidadeDanificada < artigoExistente.getQuantidadeStockDisponivel()) {
                artigoExistente.setQuantidadeStockDanificado(
                        artigoExistente.getQuantidadeStockDanificado() + quantidadeDanificada);
            }
        }

        var artigoSalvo = repository.save(artigoExistente);
        auditoriaService.registrar("Artigo", "Atualizar Quantidade - " + estado, artigoSalvo.getPkArtigo());

        log.info("Quantidade de stock atualizada com sucesso para o artigo: {}", id);
        return mapper.toResponse(artigoSalvo);
    }

    private String salvarImagemNoDisco(MultipartFile imagem) {
        try {
            String extensao = imagem.getOriginalFilename().substring(imagem.getOriginalFilename().lastIndexOf("."));
            String novoNomeFicheiro = UUID.randomUUID().toString() + extensao;
            
            Path caminhoDiretorio = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(caminhoDiretorio); // Garante que a pasta existe dentro do Docker
            
            Path caminhoFinalFicheiro = caminhoDiretorio.resolve(novoNomeFicheiro);
            Files.copy(imagem.getInputStream(), caminhoFinalFicheiro, StandardCopyOption.REPLACE_EXISTING);
            
            return novoNomeFicheiro;
        } catch (IOException ex) {
            throw new BusinessException("Não foi possível salvar o ficheiro de imagem: " + ex.getMessage());
        }
    }

}