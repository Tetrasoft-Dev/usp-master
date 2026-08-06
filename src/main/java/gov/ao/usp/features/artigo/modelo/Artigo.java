package gov.ao.usp.features.artigo.modelo;

import java.time.OffsetDateTime;
import java.util.UUID;

import gov.ao.usp.features.categoria.modelo.Categoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "artigo", schema = "public")
public class Artigo {

    @Id
    @Column(name = "pk_artigo", updatable = false, nullable = false)
    private UUID pkArtigo; 

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String nome;

    @Column(name ="quantidade_stock", nullable = true)
    private Integer quantidadeStock;

    @Column(name ="path_imagen", nullable = true)
    private String pathImagen;

    @Column(nullable = true)
    private String descricao;

    @Column(name = "status")
    private Boolean status;

    @Column(name ="quantidade_stock_disponivel", nullable = true)
    private Integer quantidadeStockDisponivel;

    @Column(name ="quantidade_stock_indisponivel", nullable = true)
    private Integer quantidadeStockIndisponivel;

    @Column(name ="quantidade_stock_danificado", nullable = true)
    private Integer quantidadeStockDanificado;

    @Column(name = "data_registro")
    private OffsetDateTime dataRegistro = OffsetDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "fk_categoria", referencedColumnName = "pk_categoria    ", nullable = false )
    private Categoria categoria;
}
