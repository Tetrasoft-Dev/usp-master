package gov.ao.usp.features.solicitacao.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.ao.usp.features.solicitacao.modelo.ItemSolicitacao;

public interface ItemSolicitacaoRepository extends JpaRepository<ItemSolicitacao, UUID> {
    
}
