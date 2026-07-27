package gov.ao.usp.features.departamento.modelo;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
@Table(name = "departamento")
public class Departamento {

    @Id
    @Column(name = "pk_departamento", updatable = false, nullable = false)
    private UUID pkDepartamento;

    @Column(name = "abreviacao", columnDefinition = "TEXT")
    private String abreviacao;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "data_de_registro")
    private OffsetDateTime dataDeRegistro = OffsetDateTime.now();
}
