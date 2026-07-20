package gov.ao.usp.features.categoria.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gov.ao.usp.features.categoria.modelo.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID>, JpaSpecificationExecutor<Categoria> {

    boolean existsByAbreviacao(String abreviacao);
}
