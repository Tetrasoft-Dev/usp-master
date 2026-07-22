package gov.ao.usp.features.Utilizador.service;

import org.springframework.security.oauth2.jwt.Jwt;

import gov.ao.usp.features.Utilizador.modelo.Perfil;
import gov.ao.usp.features.Utilizador.modelo.dto.PerfilRequestDTO;

public interface PerfilService {
    Perfil atualizarPerfilPrincipal(Jwt jwt, PerfilRequestDTO dto);
}
