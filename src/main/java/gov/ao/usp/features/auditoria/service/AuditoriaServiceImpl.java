package gov.ao.usp.features.auditoria.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.ao.usp.features.auditoria.modelo.Auditoria;
import gov.ao.usp.features.auditoria.repository.AuditoriaRepository;
import gov.ao.usp.sharead.utils.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuditoriaServiceImpl implements AuditoriaService  {
    
    private final AuditoriaRepository repository;
    private final HttpServletRequest request;
    private final IpUtil ipUtil;

    @Override
    public void registrar(
            String descricao,
            String funcionalidade,
            UUID operacao
    ) {

        Auditoria auditoria = new Auditoria();

        auditoria.setPkAuditoria(UUID.randomUUID());
        auditoria.setDescricao(descricao);
        auditoria.setFuncionalidade(funcionalidade);
        auditoria.setFkOperacao(operacao);
        auditoria.setDataDeRegistro(LocalDateTime.now());
        auditoria.setIpEndereco(ipUtil.getClientIp(request));

       /* auditoria.setFkUtilizador(
                securityUtil.getUtilizadorLogado()
        );*/

        repository.save(auditoria);

    }

}
