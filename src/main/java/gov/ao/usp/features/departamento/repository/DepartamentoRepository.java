package gov.ao.usp.features.departamento.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gov.ao.usp.features.departamento.modelo.Departamento;


@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, UUID>, JpaSpecificationExecutor<Departamento> {

    boolean existsByAbreviacao(String abreviacao);
}