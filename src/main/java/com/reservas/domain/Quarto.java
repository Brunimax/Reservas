package com.reservas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Quarto.
 */
@Entity
@Table(name = "quarto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quarto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "quantidade_hospedes")
    private Long quantidadeHospedes;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "classificacao")
    private Long classificacao;

    @ManyToOne
    @JoinColumn(unique = false)
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(unique = false)
    private TipoQuarto tipoQuarto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Quarto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Quarto nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getQuantidadeHospedes() {
        return this.quantidadeHospedes;
    }

    public Quarto quantidadeHospedes(Long quantidadeHospedes) {
        this.setQuantidadeHospedes(quantidadeHospedes);
        return this;
    }

    public void setQuantidadeHospedes(Long quantidadeHospedes) {
        this.quantidadeHospedes = quantidadeHospedes;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Quarto status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getClassificacao() {
        return this.classificacao;
    }

    public Quarto classificacao(Long classificacao) {
        this.setClassificacao(classificacao);
        return this;
    }

    public void setClassificacao(Long classificacao) {
        this.classificacao = classificacao;
    }

    public Hotel getHotel() {
        return this.hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Quarto hotel(Hotel hotel) {
        this.setHotel(hotel);
        return this;
    }

    public TipoQuarto getTipoQuarto() {
        return this.tipoQuarto;
    }

    public void setTipoQuarto(TipoQuarto tipoQuarto) {
        this.tipoQuarto = tipoQuarto;
    }

    public Quarto tipoQuarto(TipoQuarto tipoQuarto) {
        this.setTipoQuarto(tipoQuarto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quarto)) {
            return false;
        }
        return getId() != null && getId().equals(((Quarto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quarto{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", quantidadeHospedes=" + getQuantidadeHospedes() +
            ", status='" + getStatus() + "'" +
            ", classificacao=" + getClassificacao() +
            "}";
    }
}
