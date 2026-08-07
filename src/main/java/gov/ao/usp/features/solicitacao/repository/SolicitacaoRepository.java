package gov.ao.usp.features.solicitacao.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import gov.ao.usp.features.solicitacao.modelo.Solicitacao;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, UUID>, JpaSpecificationExecutor<Solicitacao> {
    
}
