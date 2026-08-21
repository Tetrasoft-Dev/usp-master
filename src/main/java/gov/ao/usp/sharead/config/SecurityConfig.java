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
import java.nio.charset.StandardCharsets;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    private String jwtSecret;

    @Value("${app.cors.origins}")
    private String corsOrigins;

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
                        // 🔥 CORREÇÃO 1: Permite que TODOS os pedidos OPTIONS passem direto sem pedir
                        // JWT
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                        // Rotas de livre acesso existentes
                        .requestMatchers("/api/usp/publico/**").permitAll()
                        .requestMatchers("/api/usp/v1/departamento/pesquisar/**").permitAll()
                        .requestMatchers("/api/usp/v1/auth/completar-perfil**").permitAll()
                        .requestMatchers("/api/usp/v1/auth/pesquisar/**").permitAll()

                        // 🔥 CORREÇÃO 2: Libera a rota de pesquisa ou criação de artigos caso ela
                        // precise de ser pública
                        // ou ajusta conforme as roles. (Exemplo: se o ecrã está na rota
                        // /api/usp/v1/artigo)
                        // Se queres que RECEPTOR gerencie artigos, adiciona:
                        // .requestMatchers("/api/usp/v1/artigo/**").hasAnyRole("RECEPTOR", "ADMIN")

                        // Bloqueados por Role
                        .requestMatchers("/api/usp/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/receptor/**").hasRole("RECEPTOR")

                        .anyRequest().authenticated() // Qualquer outra rota exige login (JWT)
                )

                // Ativa o Resource Server para processar tokens OAuth2/JWT
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                        .decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8),"HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Une o nosso conversor de perfis personalizado ao fluxo de autenticação do
        // Spring
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(supabaseAuthoritiesConverter);
        return converter;
    }

    // 2. CONFIGURA AS REGRAS DE ORIGEM PERMITIDAS (CORS)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Adicione aqui o URL do seu frontend do Codespaces e localhost (se testar
        // localmente)
        configuration.setAllowedOrigins(List.of(corsOrigins.split(",")));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Cabeçalhos permitidos (essencial incluir Authorization para o JWT e
        // Content-Type)
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));

        // Permite envio de cookies/credenciais se necessário
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração a todos os endpoints da API
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}