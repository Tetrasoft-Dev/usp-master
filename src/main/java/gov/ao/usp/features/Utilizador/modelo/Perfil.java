package gov.ao.usp.features.Utilizador.modelo;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "perfis", schema = "public")
public class Perfil {

    @Id
    private UUID id; 

    @Column(nullable = true)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    private UserRole perfil = UserRole.ROLE_USER;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    // Construtores
    public Perfil() {}

    public Perfil(UUID id, String nome, UserRole perfil) {
        this.id = id;
        this.nome = nome;
        this.perfil = perfil;
        this.updatedAt = OffsetDateTime.now();
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public UserRole getPerfil() { return perfil; }
    public void setPerfil(UserRole perfil) { this.perfil = perfil; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}