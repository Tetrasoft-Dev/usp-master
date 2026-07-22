package gov.ao.usp.sharead.utils;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public UUID getUtilizadorLogado() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof Jwt jwt)) {
            return null;
        }

        return UUID.fromString(jwt.getSubject());
    }

    private Jwt getJwt() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }

        return null;
    }

    public String getEmail() {

        Jwt jwt = getJwt();

        return jwt != null ? jwt.getClaimAsString("email") : null;
    }

    public String getRole() {

        Jwt jwt = getJwt();

        return jwt != null ? jwt.getClaimAsString("role") : null;
    }

    public UUID getEmpresa() {

        Jwt jwt = getJwt();

        if (jwt == null) {
            return null;
        }

        String empresa = jwt.getClaimAsString("empresa");

        return empresa != null ? UUID.fromString(empresa) : null;
    }

    public List<String> getPermissoes() {

        Jwt jwt = getJwt();

        return jwt != null ? jwt.getClaimAsStringList("permissions") : List.of();
    }

}