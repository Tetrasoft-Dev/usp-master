package gov.ao.usp.features.auditoria.modelo;

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
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @Column(name = "pk_auditoria", updatable = false, nullable = false)
    private UUID pkAuditoria;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "ip_endereco", columnDefinition = "TEXT")
    private String ipEndereco;

    @Column(name = "data_de_registro", insertable = false, updatable = false )
    private LocalDateTime dataDeRegistro;

    @Column(name = "fk_utilizador", updatable = false, nullable = false)
    private UUID fkUtilizador;

    @Column(name = "funcionalidade", columnDefinition = "TEXT")
    private String funcionalidade;

    @Column(name = "fk_operacao", updatable = false, nullable = false)
    private UUID fkOperacao;
}
