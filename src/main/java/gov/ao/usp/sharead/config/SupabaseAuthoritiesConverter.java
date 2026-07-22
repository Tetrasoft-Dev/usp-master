package gov.ao.usp.sharead.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import gov.ao.usp.features.Utilizador.repository.PerfilRepository;

@Component
public class SupabaseAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final PerfilRepository perfilRepository;

    public SupabaseAuthoritiesConverter(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        try {
            // O sub do JWT contém o UUID do utilizador no Supabase
            UUID userId = UUID.fromString(jwt.getSubject());
            
            // Procura o perfil na tabela pública que criámos por script
            return perfilRepository.findById(userId)
                    .map(perfil -> (GrantedAuthority) new SimpleGrantedAuthority(perfil.getPerfil().name()))
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}