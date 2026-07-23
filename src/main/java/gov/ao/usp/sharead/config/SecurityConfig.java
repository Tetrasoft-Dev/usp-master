package gov.ao.usp.sharead.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import javax.crypto.spec.SecretKeySpec;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity 
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    private String jwtSecret;

    private final SupabaseAuthoritiesConverter supabaseAuthoritiesConverter;

    public SecurityConfig(SupabaseAuthoritiesConverter supabaseAuthoritiesConverter) {
        this.supabaseAuthoritiesConverter = supabaseAuthoritiesConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Ativa o CORS com a configuração definida abaixo
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Desativa CSRF porque APIs REST com JWT não guardam sessões no servidor
            .csrf(csrf -> csrf.disable())
            
            // Define o Spring como Stateless (sem estado)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Configura as regras de acesso às rotas
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/publico/**").permitAll() // Rotas de livre acesso
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Bloqueado, apenas administradores
                .requestMatchers("/api/receptor/**").hasRole("RECEPTOR")                    
                .anyRequest().authenticated() // Qualquer outra rota exige login (JWT)
            )
            
            // Ativa o Resource Server para processar tokens OAuth2/JWT
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                .decoder(jwtDecoder())
                .jwtAuthenticationConverter(jwtAuthenticationConverter())
            ));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Configura o descodificador local usando o algoritmo HMAC-SHA256 padrão do Supabase
        SecretKeySpec secretKey = new SecretKeySpec(jwtSecret.getBytes(), "HMACSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Une o nosso conversor de perfis personalizado ao fluxo de autenticação do Spring
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(supabaseAuthoritiesConverter);
        return converter;
    }

    // 2. CONFIGURA AS REGRAS DE ORIGEM PERMITIDAS (CORS)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Adicione aqui o URL do seu frontend do Codespaces e localhost (se testar localmente)
        configuration.setAllowedOrigins(List.of(
            "https://cuddly-goggles-v6xrgrv5rvx9hwrrj-3000.app.github.dev",
            "http://localhost:3000"
        ));
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Cabeçalhos permitidos (essencial incluir Authorization para o JWT e Content-Type)
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        
        // Permite envio de cookies/credenciais se necessário
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração a todos os endpoints da API
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}