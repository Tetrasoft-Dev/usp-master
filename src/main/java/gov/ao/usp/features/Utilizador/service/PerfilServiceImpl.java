package gov.ao.usp.features.Utilizador.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.ao.usp.features.Utilizador.modelo.Perfil;
import gov.ao.usp.features.Utilizador.modelo.dto.PerfilRequestDTO;
import gov.ao.usp.features.Utilizador.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PerfilServiceImpl implements PerfilService{
    
    private final PerfilRepository perfilRepository;

    @Transactional
    public Perfil atualizarPerfilPrincipal(Jwt jwt, PerfilRequestDTO dto) {
        // 1. Extrai o UUID do Supabase guardado no subject do Token
        UUID userId = UUID.fromString(jwt.getSubject());

        // 2. Procura o perfil correspondente na tabela do Postgres
        Perfil perfil = perfilRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Perfil do utilizador não encontrado"));

        // 3. Atualiza os campos necessários
        perfil.setNome(dto.getNome());
        perfil.setUpdatedAt(OffsetDateTime.now());

        // 4. Salva as modificações
        return perfilRepository.save(perfil);
    }
}
