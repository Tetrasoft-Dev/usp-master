package gov.ao.usp.features.Utilizador.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ao.jcardoso.libs.exception.ResourceNotFoundException;
import ao.jcardoso.libs.paginacao.PageRequestDTO;
import ao.jcardoso.libs.paginacao.PageResponseDTO;
import ao.jcardoso.libs.paginacao.PaginationUtils;
import gov.ao.usp.features.Utilizador.modelo.Perfil;
import gov.ao.usp.features.Utilizador.modelo.UserRole;
import gov.ao.usp.features.Utilizador.modelo.dto.CompletarPerfilRequest;
import gov.ao.usp.features.Utilizador.modelo.dto.PerfilListMapper;
import gov.ao.usp.features.Utilizador.modelo.dto.PerfilRequestDTO;
import gov.ao.usp.features.Utilizador.modelo.dto.PerfilResponseDTO;
import gov.ao.usp.features.Utilizador.repository.PerfilRepository;
import gov.ao.usp.features.departamento.modelo.Departamento;
import gov.ao.usp.features.departamento.repository.DepartamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PerfilServiceImpl implements PerfilService{
    
    private final PerfilRepository perfilRepository;
    private final DepartamentoRepository departamentoRepository;
    private final PerfilListMapper mapper;

    @Transactional
    public PerfilResponseDTO atualizarPerfilPrincipal(Jwt jwt, PerfilRequestDTO dto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        Perfil perfil = perfilRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Perfil do utilizador não encontrado"));

        Departamento departamento = departamentoRepository.findById(dto.departamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado"));
        perfil.setId(userId);
        perfil.setNome(dto.nome());
        perfil.setUsername(dto.username());
        perfil.setNip(dto.nip());
        perfil.setDepartamento(departamento);
    
        if (dto.perfil() != null) {
            perfil.setPerfil(dto.perfil());
        }
        perfil.setUpdatedAt(OffsetDateTime.now());
        Perfil perfilSalvo = perfilRepository.save(perfil);
        return PerfilResponseDTO.fromEntity(perfilSalvo);
    }

    @Override
    @Transactional
    public PerfilResponseDTO completarPerfil(Jwt jwt,CompletarPerfilRequest request) {   

        UUID userId = UUID.fromString(jwt.getSubject());
        Perfil perfil = perfilRepository.findById(userId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Utilizador não encontrado."));

        Departamento departamento = departamentoRepository.findById(request.departamentoId())
            .orElseThrow(() ->
                    new ResourceNotFoundException("Departamento não encontrado."));

        perfil.setNome(request.nome());
        perfil.setUsername(request.username());
        perfil.setNip(request.nip());
        perfil.setPerfil(request.perfil());
        perfil.setDepartamento(departamento);
        perfil = perfilRepository.save(perfil);

        return new PerfilResponseDTO(
            perfil.getId(),
            perfil.getNome(),
            perfil.getNip(),
            "",
            perfil.getUsername(),
            perfil.getPerfil(),
            null,"",
            perfil.getUpdatedAt()
    );

    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<PerfilResponseDTO> pesquisar(
        PageRequestDTO req,
        String nome,
        String username,
        String nip,
        UserRole perfil,
        UUID departamentoId
    ) {

     log.info("Pesquisando utilizadores.");

     Pageable pageable = PaginationUtils.buildPageable(req);

     Specification<Perfil> specification =
            PerfilSpecifications.filtrar(
                    nome,
                    username,
                    nip,
                    perfil,
                    departamentoId
            );

     Page<Perfil> page = perfilRepository.findAll(specification, pageable);

        log.info("Total encontrado: {}", page.getTotalElements());

     return PaginationUtils.buildPageResponse(
               page,
               mapper::toResponse
        );
    }

    private PerfilResponseDTO converter(Perfil perfil) {

        return new PerfilResponseDTO(

                perfil.getId(),

                perfil.getNome(),

                perfil.getNip(),

                null, // email vem do JWT apenas no endpoint /meu-perfil

                perfil.getUsername(),

                perfil.getPerfil(),

                perfil.getDepartamento().getPkDepartamento(),

                perfil.getDepartamento().getDescricao(),

                perfil.getUpdatedAt()
        );
    }
}
