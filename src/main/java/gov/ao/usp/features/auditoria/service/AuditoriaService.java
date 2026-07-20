package gov.ao.usp.features.auditoria.service;

import java.util.UUID;

import gov.ao.usp.features.auditoria.modelo.Auditoria;

public interface AuditoriaService {
    
    void registrar(
            String descricao,
            String funcionalidade,
            UUID operacao
    );
}
