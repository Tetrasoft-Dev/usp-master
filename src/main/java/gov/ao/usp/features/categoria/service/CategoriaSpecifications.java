package gov.ao.usp.features.categoria.service;

import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CategoriaSpecifications{
    public static Specification<CategoriaResponse> filtrar( String abreviacao, Boolean status) {
        return (root, query, criteriaBuilder) -> {
            var predicate = null;

            if (abreviacao != null && !abreviacao.isBlank()) {
                var nomePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("abreviacao")),
                        "%" + abreviacao.toLowerCase() + "%");
                predicate = criteriaBuilder.and(predicate, nomePredicate);
            }

            if (status != null) {
                var statusPredicate = criteriaBuilder.equal(root.get("status"), status);
                predicate = criteriaBuilder.and(predicate, statusPredicate);
            }

            return predicate;
        };
    }
}
