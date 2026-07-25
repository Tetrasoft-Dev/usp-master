package gov.ao.usp.features.Utilizador.modelo;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

import gov.ao.usp.features.departamento.modelo.Departamento;

@Entity
@Table(name = "perfis", schema = "public")
public class Perfil {

    @Id
    private UUID id; 

    @Column(nullable = true)
    private String nome;

    @Column(nullable = true)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    private UserRole perfil = UserRole.ROLE_USER;

    @Column(nullable = true)
    private String nip;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "fk_departamento", referencedColumnName = "pk_departamento", nullable = false )
    private Departamento departamento;

    // Construtores
    public Perfil() {}

    public Perfil(
        UUID id,
        String nome,
        String username,
        UserRole perfil,
        String nip,
        Departamento departamento
    ) {
        this.id = id;
        this.nome = nome;
        this.username = username;
        this.perfil = perfil;
        this.nip = nip;
        this.departamento = departamento;
        this.updatedAt = OffsetDateTime.now();
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNip() { return nip; }
    public void setNip(String nip) { this.nip = nip; }

    public UserRole getPerfil() { return perfil; }
    public void setPerfil(UserRole perfil) { this.perfil = perfil; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }
}