package gov.ao.usp.features.Utilizador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gov.ao.usp.features.Utilizador.modelo.Perfil;
import gov.ao.usp.features.departamento.modelo.Departamento;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, UUID> , JpaSpecificationExecutor<Perfil> {

    Optional<Perfil> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByNip(String nip);
}