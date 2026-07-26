package gov.ao.usp.features.Utilizador.service;

import java.util.UUID;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import gov.ao.usp.features.Utilizador.modelo.Perfil;
import gov.ao.usp.features.Utilizador.modelo.UserRole;

public class PerfilSpecifications {

    public static Specification<Perfil> filtrar(
            String nome,
            String username,
            String nip,
            UserRole perfil,
            UUID departamentoId
    ) {

        return (root, query, cb) -> {

            Predicate predicate = cb.conjunction();

            if (nome != null && !nome.isBlank()) {
                predicate = cb.and(
                        predicate,
                        cb.like(
                                cb.lower(root.get("nome")),
                                "%" + nome.toLowerCase() + "%"
                        )
                );
            }

            if (username != null && !username.isBlank()) {
                predicate = cb.and(
                        predicate,
                        cb.like(
                                cb.lower(root.get("username")),
                                "%" + username.toLowerCase() + "%"
                        )
                );
            }

            if (nip != null && !nip.isBlank()) {
                predicate = cb.and(
                        predicate,
                        cb.like(
                                cb.lower(root.get("nip")),
                                "%" + nip.toLowerCase() + "%"
                        )
                );
            }

            if (perfil != null) {
                predicate = cb.and(
                        predicate,
                        cb.equal(root.get("perfil"), perfil)
                );
            }

            if (departamentoId != null) {
                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("fkDepartamento").get("pkDepartamento"),
                                departamentoId
                        )
                );
            }

            return predicate;
        };
    }

}