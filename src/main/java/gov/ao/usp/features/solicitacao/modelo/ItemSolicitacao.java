package gov.ao.usp.features.solicitacao.modelo;

import java.util.UUID;

import gov.ao.usp.features.artigo.modelo.Artigo;
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
@Table(name = "item_solicitacao", schema = "public")
public class ItemSolicitacao {
    
    @Id
    private UUID pkItemSolicitacao;

    @ManyToOne
    private Solicitacao solicitacao;
    @ManyToOne
    private Artigo artigo;
    private Integer quantidadeSolicitada;
    private Integer quantidadeExtraviada;
    private Integer quantidadeDanificado;
}
