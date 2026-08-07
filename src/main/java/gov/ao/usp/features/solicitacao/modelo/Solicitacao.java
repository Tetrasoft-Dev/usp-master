package gov.ao.usp.features.solicitacao.modelo;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import gov.ao.usp.features.Utilizador.modelo.Perfil;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "solicitacao", schema = "public")
public class Solicitacao {

    @Id
    @Column(name = "pk_solicitacao", updatable = false, nullable = false)
    private UUID pkSolicitacao;
    @Column(name = "nota_informativa", nullable = true)
    private String notaInformativa;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_perfil", referencedColumnName = "id", nullable = false)
    private Perfil perfil;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_solicitacao", nullable = false)
    private TipoSolicitacao solicitacao = TipoSolicitacao.MATERIAL;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_da_solicitacao", nullable = false)
    private EstadoSolicitacao estadoDaSolicitacao = EstadoSolicitacao.SOLICITACAO;
    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;
    @Column(name = "data_termino")
    private LocalDateTime dataTermino;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "data_registro")
    private OffsetDateTime dataRegistro = OffsetDateTime.now();
    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemSolicitacao> itens = new ArrayList<>();
}
