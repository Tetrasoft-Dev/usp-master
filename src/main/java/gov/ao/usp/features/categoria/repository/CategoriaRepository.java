package gov.ao.usp.features.categoria.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gov.ao.usp.features.categoria.modelo.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

    boolean existsByAbreviacao(String abreviacao);
}
