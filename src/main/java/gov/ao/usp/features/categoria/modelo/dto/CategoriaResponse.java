package gov.ao.usp.features.categoria.modelo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoriaResponse(UUID id, String abreviacao, String descricao, Boolean status,
        LocalDateTime dataDeRegistro) {

}
