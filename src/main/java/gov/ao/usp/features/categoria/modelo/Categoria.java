package gov.ao.usp.features.categoria.modelo;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "categoria")
public class Categoria {

    @Id
    @Column(name = "pk_categoria", updatable = false, nullable = false)
    private UUID pkCategoria;

    @Column(name = "abreviacao", columnDefinition = "TEXT")
    private String abreviacao;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "data_de_registro", nullable = true)
    private LocalDateTime dataDeRegistro;
}
