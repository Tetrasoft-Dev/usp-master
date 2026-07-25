package gov.ao.usp.features.Utilizador.service;

import org.springframework.security.oauth2.jwt.Jwt;

import gov.ao.usp.features.Utilizador.modelo.Perfil;
import gov.ao.usp.features.Utilizador.modelo.dto.CompletarPerfilRequest;
import gov.ao.usp.features.Utilizador.modelo.dto.PerfilRequestDTO;
import gov.ao.usp.features.Utilizador.modelo.dto.PerfilResponseDTO;

public interface PerfilService {

    PerfilResponseDTO completarPerfil(Jwt jwt,CompletarPerfilRequest request);
    Perfil atualizarPerfilPrincipal(Jwt jwt, PerfilRequestDTO dto);
}
