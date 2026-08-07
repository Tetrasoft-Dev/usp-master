package gov.ao.usp.sharead.config;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

//@Component
public class SecurityUtil {

   /*  public UUID getUtilizadorLogado() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("Utilizador não autenticado.");
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();

        return UUID.fromString(jwt.getSubject());
    }*/

}