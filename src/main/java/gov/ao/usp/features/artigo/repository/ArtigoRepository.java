package gov.ao.usp.features.artigo.repository;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gov.ao.usp.features.artigo.modelo.Artigo;


@Repository
public interface ArtigoRepository extends JpaRepository<Artigo, UUID>, JpaSpecificationExecutor<Artigo> {

    boolean existsByNome(String nome);
}