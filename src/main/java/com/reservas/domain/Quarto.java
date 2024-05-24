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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quarto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "quarto" }, allowSetters = true)
    private Set<FotoQuarto> fotoQuartos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quarto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "avaliacao", "quarto", "pessoa" }, allowSetters = true)
    private Set<Reserva> reservas = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "quartos", "municipio", "pessoa" }, allowSetters = true)
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "quartos" }, allowSetters = true)
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

    public Set<FotoQuarto> getFotoQuartos() {
        return this.fotoQuartos;
    }

    public void setFotoQuartos(Set<FotoQuarto> fotoQuartos) {
        if (this.fotoQuartos != null) {
            this.fotoQuartos.forEach(i -> i.setQuarto(null));
        }
        if (fotoQuartos != null) {
            fotoQuartos.forEach(i -> i.setQuarto(this));
        }
        this.fotoQuartos = fotoQuartos;
    }

    public Quarto fotoQuartos(Set<FotoQuarto> fotoQuartos) {
        this.setFotoQuartos(fotoQuartos);
        return this;
    }

    public Quarto addFotoQuarto(FotoQuarto fotoQuarto) {
        this.fotoQuartos.add(fotoQuarto);
        fotoQuarto.setQuarto(this);
        return this;
    }

    public Quarto removeFotoQuarto(FotoQuarto fotoQuarto) {
        this.fotoQuartos.remove(fotoQuarto);
        fotoQuarto.setQuarto(null);
        return this;
    }

    public Set<Reserva> getReservas() {
        return this.reservas;
    }

    public void setReservas(Set<Reserva> reservas) {
        if (this.reservas != null) {
            this.reservas.forEach(i -> i.setQuarto(null));
        }
        if (reservas != null) {
            reservas.forEach(i -> i.setQuarto(this));
        }
        this.reservas = reservas;
    }

    public Quarto reservas(Set<Reserva> reservas) {
        this.setReservas(reservas);
        return this;
    }

    public Quarto addReserva(Reserva reserva) {
        this.reservas.add(reserva);
        reserva.setQuarto(this);
        return this;
    }

    public Quarto removeReserva(Reserva reserva) {
        this.reservas.remove(reserva);
        reserva.setQuarto(null);
        return this;
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
