package gov.ao.usp.features.artigo.service;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import gov.ao.usp.features.artigo.modelo.Artigo;
import jakarta.persistence.criteria.Predicate;


public class ArtigoSpecifications {
    
    public static Specification<Artigo> filtrar( String nome,  UUID categoriaId, Boolean status) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (nome != null && !nome.isBlank()) {
                Predicate nomePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")),
                        "%" + nome.toLowerCase() + "%");
                predicate = criteriaBuilder.and(predicate, nomePredicate);
            }

            if (categoriaId != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(
                                root.get("fkCategoria").get("pkCategoria"),
                                categoriaId
                        )
                );
            }

            if (status != null) {
                Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), status);
                predicate = criteriaBuilder.and(predicate, statusPredicate);
            }

            return predicate;
        };
    }
}
