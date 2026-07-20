package gov.ao.usp.features.departamento.service;

import org.springframework.data.jpa.domain.Specification;

import gov.ao.usp.features.departamento.modelo.Departamento;

import jakarta.persistence.criteria.Predicate;

public class DepartamentoSpecifications{
    public static Specification<Departamento> filtrar( String abreviacao, Boolean status) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (abreviacao != null && !abreviacao.isBlank()) {
                Predicate nomePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("abreviacao")),
                        "%" + abreviacao.toLowerCase() + "%");
                predicate = criteriaBuilder.and(predicate, nomePredicate);
            }

            if (status != null) {
                Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), status);
                predicate = criteriaBuilder.and(predicate, statusPredicate);
            }

            return predicate;
        };
    }
}