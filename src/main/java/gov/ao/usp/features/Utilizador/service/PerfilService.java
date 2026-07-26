package gov.ao.usp.features.Utilizador.service;

import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;

import ao.jcardoso.libs.paginacao.PageRequestDTO;
import ao.jcardoso.libs.paginacao.PageResponseDTO;
import gov.ao.usp.features.Utilizador.modelo.Perfil;
import gov.ao.usp.features.Utilizador.modelo.UserRole;
import gov.ao.usp.features.Utilizador.modelo.dto.CompletarPerfilRequest;
import gov.ao.usp.features.Utilizador.modelo.dto.PerfilRequestDTO;
import gov.ao.usp.features.Utilizador.modelo.dto.PerfilResponseDTO;

public interface PerfilService {

    PerfilResponseDTO completarPerfil(Jwt jwt,CompletarPerfilRequest request);
    PerfilResponseDTO atualizarPerfilPrincipal(Jwt jwt, PerfilRequestDTO dto);
    PageResponseDTO<PerfilResponseDTO> pesquisar(
        PageRequestDTO req,
        String nome,
        String username,
        String nip,
        UserRole perfil,
        UUID departamentoId
);
}
