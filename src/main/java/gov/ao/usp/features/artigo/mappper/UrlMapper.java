package gov.ao.usp.features.artigo.mappper;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class UrlMapper {

    @Value("${app.public-url}")
    private String publicUrl;

    @Named("gerarUrlImagem")
    public String gerarUrlImagem(String caminho) {

        if (caminho == null || caminho.isBlank()) {
            return null;
        }

        if (caminho.startsWith("http://") || caminho.startsWith("https://")) {
            return caminho;
        }

        return publicUrl + caminho;
    }

}