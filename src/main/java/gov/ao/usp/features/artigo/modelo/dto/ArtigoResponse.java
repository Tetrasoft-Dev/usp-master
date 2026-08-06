package gov.ao.usp.features.artigo.modelo.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ArtigoResponse(
    
    UUID id, 
    String name, 
    String nome, 
    Integer quantidadeStock,
    String pathImagen,
    String descricao, 
    Boolean status,
    Integer quantidadeStockDisponivel,
    Integer quantidadeStockIndisponivel,
    Integer quantidadeStockDanificado,
    OffsetDateTime dataRegistro,
    UUID idCategoria, 
    String nomeCategoria

) {
    
}
