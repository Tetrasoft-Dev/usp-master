package gov.ao.usp.features.solicitacao.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ao.jcardoso.libs.exception.BusinessException;
import ao.jcardoso.libs.exception.ResourceNotFoundException;
import ao.jcardoso.libs.paginacao.PageRequestDTO;
import ao.jcardoso.libs.paginacao.PageResponseDTO;
import ao.jcardoso.libs.paginacao.PaginationUtils;
import gov.ao.usp.features.Utilizador.modelo.Perfil;
import gov.ao.usp.features.Utilizador.repository.PerfilRepository;
import gov.ao.usp.features.artigo.modelo.Artigo;
import gov.ao.usp.features.artigo.repository.ArtigoRepository;
import gov.ao.usp.features.artigo.service.ArtigoService;
import gov.ao.usp.features.auditoria.service.AuditoriaService;
import gov.ao.usp.features.solicitacao.mapper.SolicitacaoMapper;
import gov.ao.usp.features.solicitacao.modelo.EstadoSolicitacao;
import gov.ao.usp.features.solicitacao.modelo.ItemSolicitacao;
import gov.ao.usp.features.solicitacao.modelo.Solicitacao;
import gov.ao.usp.features.solicitacao.modelo.TipoSolicitacao;
import gov.ao.usp.features.solicitacao.modelo.dto.DevolucaoItemRequest;
import gov.ao.usp.features.solicitacao.modelo.dto.DevolucaoRequest;
import gov.ao.usp.features.solicitacao.modelo.dto.ItemSolicitacaoRequest;
import gov.ao.usp.features.solicitacao.modelo.dto.SolicitacaoEstatisticasResponse;
import gov.ao.usp.features.solicitacao.modelo.dto.SolicitacaoRequest;
import gov.ao.usp.features.solicitacao.modelo.dto.SolicitacaoResponse;
import gov.ao.usp.features.solicitacao.repository.SolicitacaoRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SolicitacaoServiceImpl implements SolicitacaoService {

    private final SolicitacaoRepository repository;
    private final PerfilRepository perfilRepository;
    private final ArtigoRepository artigoRepository;
    private final SolicitacaoMapper mapper;
    private final AuditoriaService auditoriaService;
    //private final SecurityUtil securityUtil;
    private final ArtigoService artigoService;

    @Override
    public SolicitacaoResponse criar(
            Jwt jwt,
            SolicitacaoRequest request) {

        log.info("Criando nova solicitação.");

        UUID utilizadorId = UUID.fromString(jwt.getSubject());

        Perfil perfil = perfilRepository.findById(utilizadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado."));

        if (request.itens() == null || request.itens().isEmpty()) {
            throw new BusinessException(
                    "A solicitação deve possuir pelo menos um artigo.");
        }

        Solicitacao solicitacao = mapper.toEntity(request);

        solicitacao.setPkSolicitacao(UUID.randomUUID());
        solicitacao.setPerfil(perfil);
        solicitacao.setEstadoDaSolicitacao(EstadoSolicitacao.SOLICITACAO);
        solicitacao.setStatus(true);

        List<ItemSolicitacao> itens = new ArrayList<>();

        for (ItemSolicitacaoRequest itemRequest : request.itens()) {

            Artigo artigo = artigoRepository.findById(itemRequest.artigoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Artigo não encontrado."));

            if (itemRequest.quantidade() <= 0) {
                throw new BusinessException("Quantidade inválida.");
            }

            if (artigo.getQuantidadeStockDisponivel() < itemRequest.quantidade()) {
                throw new BusinessException("O artigo " + artigo.getNome() + " não possui stock suficiente.");
            }

            ItemSolicitacao item = mapper.toEntity(itemRequest);
            item.setPkItemSolicitacao(UUID.randomUUID());
            item.setSolicitacao(solicitacao);
            item.setArtigo(artigo);
            itens.add(item);
        }

        solicitacao.setItens(itens);
        Solicitacao salva = repository.save(solicitacao);
        auditoriaService.registrar(
                "Solicitação",
                "Criar Solicitação",
                salva.getPkSolicitacao());

        log.info("Solicitação criada com sucesso.");

        return mapper.toResponse(salva);
    }

    @Override
    public SolicitacaoResponse aprovar(UUID id) {

        log.info("Aprovando solicitação {}", id);

        Solicitacao solicitacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Solicitação não encontrada."));

        if (solicitacao.getEstadoDaSolicitacao() != EstadoSolicitacao.SOLICITACAO) {
            throw new BusinessException("A solicitação já foi processada.");
        }

        for (ItemSolicitacao item : solicitacao.getItens()) {
            artigoService.atualizarQuantidade(item.getArtigo().getPkArtigo(), item.getQuantidadeSolicitada(), 0,
                    EstadoSolicitacao.APROVADO);
        }

        solicitacao.setEstadoDaSolicitacao(EstadoSolicitacao.APROVADO);
        Solicitacao salva = repository.save(solicitacao);
        auditoriaService.registrar("Solicitação", "Aprovar Solicitação",
                salva.getPkSolicitacao());

        return mapper.toResponse(salva);
    }

    @Override
    public SolicitacaoResponse rejeitar(UUID id) {

        Solicitacao solicitacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada."));

        if (solicitacao.getEstadoDaSolicitacao() != EstadoSolicitacao.SOLICITACAO) {
            throw new BusinessException("A solicitação já foi processada.");
        }

        solicitacao.setEstadoDaSolicitacao(EstadoSolicitacao.REJEITADO);
        Solicitacao salva = repository.save(solicitacao);

        auditoriaService.registrar("Solicitação", "Rejeitar Solicitação",
                salva.getPkSolicitacao());
        return mapper.toResponse(salva);
    }

    @Override
    public SolicitacaoResponse devolver(
            UUID id,
            DevolucaoRequest request) {

        Solicitacao solicitacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada."));

        if (solicitacao.getEstadoDaSolicitacao() != EstadoSolicitacao.APROVADO) {
            throw new BusinessException("Somente solicitações aprovadas podem ser devolvidas.");
        }

        for (DevolucaoItemRequest item : request.itens()) {

            ItemSolicitacao itemSolicitado = solicitacao
                    .getItens()
                    .stream()
                    .filter(i -> i.getPkItemSolicitacao()
                            .equals(item.itemSolicitacaoId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado."));

            itemSolicitado.setQuantidadeDanificado(item.quantidadeDanificada());
            itemSolicitado.setQuantidadeExtraviada(item.quantidadeExtraviada());

            Integer totalDevolvido = item.quantidadeDanificada() + item.quantidadeExtraviada();

            if (totalDevolvido > itemSolicitado.getQuantidadeSolicitada()) {
                throw new BusinessException("A quantidade devolvida é superior à quantidade solicitada.");
            }

            Integer quantidadeBoa = itemSolicitado.getQuantidadeSolicitada() - totalDevolvido;
            itemSolicitado.setQuantidadeDanificado(item.quantidadeDanificada());
            itemSolicitado.setQuantidadeExtraviada(item.quantidadeExtraviada());
            artigoService.atualizarQuantidade(
                    itemSolicitado.getArtigo().getPkArtigo(),
                    quantidadeBoa,
                    item.quantidadeDanificada(),
                    EstadoSolicitacao.DEVOLUCAO);

        }

        solicitacao.setEstadoDaSolicitacao(EstadoSolicitacao.DEVOLUCAO);
        Solicitacao salva = repository.save(solicitacao);
        auditoriaService.registrar("Solicitação", "Devolver Solicitação", salva.getPkSolicitacao());
        return mapper.toResponse(salva);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<SolicitacaoResponse> pesquisar(

            PageRequestDTO req,
            String nome,
            String nip,
            UUID perfilId,
            UUID departamentoId,
            UUID categoriaId,
            TipoSolicitacao tipoSolicitacao,
            EstadoSolicitacao estadoSolicitacao,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Boolean status

    ) {

        Pageable pageable = PaginationUtils.buildPageable(req);

        Specification<Solicitacao> specification = SolicitacaoSpecifications.filtrar(

                nome,
                nip,
                perfilId,
                departamentoId,
                categoriaId,
                tipoSolicitacao,
                estadoSolicitacao,
                dataInicio,
                dataFim,
                status);

        Page<Solicitacao> page = repository.findAll(specification, pageable);

        return PaginationUtils.buildPageResponse(
                page,
                mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitacaoResponse buscarPorId(UUID id) {

        log.info("Buscando solicitação {}", id);

        Solicitacao solicitacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Solicitação não encontrada."));

        return mapper.toResponse(solicitacao);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<SolicitacaoResponse> minhasSolicitacoes(
            Jwt jwt, PageRequestDTO req ) {

        UUID utilizadorId = UUID.fromString(jwt.getSubject());

        Pageable pageable = PaginationUtils.buildPageable(req);

        Specification<Solicitacao> specification = SolicitacaoSpecifications.filtrar(

                null,
                null,
                utilizadorId,
                null,
                null,
                null,
                null,
                null,
                null,
                true);

        Page<Solicitacao> page = repository.findAll(specification, pageable);

        return PaginationUtils.buildPageResponse(
                page,
                mapper::toResponse);
    }

    @Override
    @Transactional
    public SolicitacaoResponse cancelar(UUID id, Jwt jwt) {

        UUID utilizadorId = UUID.fromString(jwt.getSubject());

        Solicitacao solicitacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada.") );

        if (!solicitacao.getPerfil().getId().equals(utilizadorId)) {
           throw new BusinessException("Não tem permissão para cancelar esta solicitação." );
        }

        if (solicitacao.getEstadoDaSolicitacao() != EstadoSolicitacao.SOLICITACAO
                && solicitacao.getEstadoDaSolicitacao() != EstadoSolicitacao.VISUALIZADOS) {

                throw new BusinessException("Esta solicitação não pode mais ser cancelada.");
        }

        solicitacao.setEstadoDaSolicitacao(EstadoSolicitacao.CANCELADOS);
        Solicitacao salva = repository.save(solicitacao);

        auditoriaService.registrar("Solicitação",
                "Cancelar Solicitação", salva.getPkSolicitacao());

        log.info( "Solicitação {} cancelada pelo utilizador {}", id, utilizadorId);

        return mapper.toResponse(salva);
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitacaoEstatisticasResponse estatisticas(
        Jwt jwt,
        LocalDateTime dataInicio,
        LocalDateTime dataFim) {

        UUID utilizadorId = UUID.fromString(jwt.getSubject());

        List<Solicitacao> solicitacoes =
                repository.findAll(
                        SolicitacaoSpecifications.filtrar(
                        null,
                        null,
                        utilizadorId,
                        null,
                        null,
                        null,
                        null,
                        dataInicio,
                        dataFim,
                        true)
                );

        Integer total = solicitacoes.size();
        int solicitacao = (int) solicitacoes.stream()
                .filter(s -> s.getEstadoDaSolicitacao()
                        == EstadoSolicitacao.SOLICITACAO).count();
        int aprovadas = (int) solicitacoes.stream()
                .filter(s -> s.getEstadoDaSolicitacao()
                        == EstadoSolicitacao.APROVADO).count();
        int rejeitadas = (int) solicitacoes.stream()
                .filter(s -> s.getEstadoDaSolicitacao()
                        == EstadoSolicitacao.REJEITADO).count();
        int devolvidas = (int) solicitacoes.stream()
                .filter(s -> s.getEstadoDaSolicitacao()
                        == EstadoSolicitacao.DEVOLUCAO).count();
        int visualizadas = (int) solicitacoes.stream()
                .filter(s -> s.getEstadoDaSolicitacao()
                        == EstadoSolicitacao.VISUALIZADOS).count();
        int canceladas = (int) solicitacoes.stream()
                .filter(s -> s.getEstadoDaSolicitacao()
                        == EstadoSolicitacao.CANCELADOS).count();
        return new SolicitacaoEstatisticasResponse(
                total, solicitacao, aprovadas,
                rejeitadas, devolvidas, visualizadas,
                canceladas);
    }

}
