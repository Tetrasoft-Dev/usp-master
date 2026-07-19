package gov.ao.usp.features.departamento.modelo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DepartamentoResponse(UUID id, String abreviacao, String descricao, Boolean status,
        LocalDateTime dataDeRegistro) {

}
