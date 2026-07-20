package gov.ao.usp.features.departamento.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gov.ao.usp.features.departamento.modelo.Departamento;


@Repository
public interface CategoriaRepository extends JpaRepository<Departamento, UUID> {

    boolean existsByAbreviacao(String abreviacao);
}