package gov.ao.usp.features.auditoria.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gov.ao.usp.features.auditoria.modelo.Auditoria;

@Repository
public interface AuditoriaRepository  extends JpaRepository<Auditoria, UUID> {
    
}
