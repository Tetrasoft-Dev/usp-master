package gov.ao.usp.features.solicitacao.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import gov.ao.usp.features.Utilizador.modelo.Perfil;
import gov.ao.usp.features.artigo.modelo.Artigo;
import gov.ao.usp.features.solicitacao.modelo.EstadoSolicitacao;
import gov.ao.usp.features.solicitacao.modelo.ItemSolicitacao;
import gov.ao.usp.features.solicitacao.modelo.Solicitacao;
import gov.ao.usp.features.solicitacao.modelo.TipoSolicitacao;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class SolicitacaoSpecifications {

    public static Specification<Solicitacao> filtrar(

            String nome,
            String nip,
            UUID perfilId,
            UUID departamentoId,
            UUID categoriaId,
            TipoSolicitacao tipoSolicitacao,
            EstadoSolicitacao estadoSolicitacao,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Boolean status

    ) {

        return (root, query, builder) -> {

            query.distinct(true);

            Predicate predicate = builder.conjunction();

            Join<Solicitacao, Perfil> perfil =
                    root.join("perfil", JoinType.LEFT);

            Join<Solicitacao, ItemSolicitacao> item =
                    root.join("itens", JoinType.LEFT);

            Join<ItemSolicitacao, Artigo> artigo =
                    item.join("artigo", JoinType.LEFT);

            //-----------------------------------------
            // Nome
            //-----------------------------------------

            if (nome != null && !nome.isBlank()) {

                predicate = builder.and(
                        predicate,
                        builder.like(
                                builder.lower(perfil.get("nome")),
                                "%" + nome.toLowerCase() + "%"
                        )
                );
            }

            //-----------------------------------------
            // NIP
            //-----------------------------------------

            if (nip != null && !nip.isBlank()) {

                predicate = builder.and(
                        predicate,
                        builder.like(
                                perfil.get("nip"),
                                "%" + nip + "%"
                        )
                );
            }

            //-----------------------------------------
            // Perfil
            //-----------------------------------------

            if (perfilId != null) {

                predicate = builder.and(
                        predicate,
                        builder.equal(
                                perfil.get("id"),
                                perfilId
                        )
                );
            }

            //-----------------------------------------
            // Departamento
            //-----------------------------------------

            if (departamentoId != null) {

                predicate = builder.and(
                        predicate,
                        builder.equal(
                                perfil.get("departamento")
                                      .get("pkDepartamento"),
                                departamentoId
                        )
                );
            }

            //-----------------------------------------
            // Categoria
            //-----------------------------------------

            if (categoriaId != null) {

                predicate = builder.and(
                        predicate,
                        builder.equal(
                                artigo.get("categoria")
                                      .get("pkCategoria"),
                                categoriaId
                        )
                );
            }

            //-----------------------------------------
            // Tipo
            //-----------------------------------------

            if (tipoSolicitacao != null) {

                predicate = builder.and(
                        predicate,
                        builder.equal(
                                root.get("solicitacao"),
                                tipoSolicitacao
                        )
                );
            }

            //-----------------------------------------
            // Estado
            //-----------------------------------------

            if (estadoSolicitacao != null) {

                predicate = builder.and(
                        predicate,
                        builder.equal(
                                root.get("estadoDaSolicitacao"),
                                estadoSolicitacao
                        )
                );
            }

            //-----------------------------------------
            // Data Inicial
            //-----------------------------------------

            if (dataInicio != null) {

                predicate = builder.and(
                        predicate,
                        builder.greaterThanOrEqualTo(
                                root.get("dataInicio"),
                                dataInicio
                        )
                );
            }

            //-----------------------------------------
            // Data Final
            //-----------------------------------------

            if (dataFim != null) {

                predicate = builder.and(
                        predicate,
                        builder.lessThanOrEqualTo(
                                root.get("dataTermino"),
                                dataFim
                        )
                );
            }

            //-----------------------------------------
            // Status
            //-----------------------------------------

            if (status != null) {

                predicate = builder.and(
                        predicate,
                        builder.equal(
                                root.get("status"),
                                status
                        )
                );
            }

            return predicate;
        };

    }

}