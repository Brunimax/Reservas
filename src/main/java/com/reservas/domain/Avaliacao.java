package com.reservas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Avaliacao.
 */
@Entity
@Table(name = "avaliacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Avaliacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "pontos")
    private Long pontos;

    @Column(name = "comentario")
    private String comentario;

    @JsonIgnoreProperties(value = { "avaliacao", "quarto", "pessoa" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "avaliacao")
    private Reserva reserva;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Avaliacao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPontos() {
        return this.pontos;
    }

    public Avaliacao pontos(Long pontos) {
        this.setPontos(pontos);
        return this;
    }

    public void setPontos(Long pontos) {
        this.pontos = pontos;
    }

    public String getComentario() {
        return this.comentario;
    }

    public Avaliacao comentario(String comentario) {
        this.setComentario(comentario);
        return this;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Reserva getReserva() {
        return this.reserva;
    }

    public void setReserva(Reserva reserva) {
        if (this.reserva != null) {
            this.reserva.setAvaliacao(null);
        }
        if (reserva != null) {
            reserva.setAvaliacao(this);
        }
        this.reserva = reserva;
    }

    public Avaliacao reserva(Reserva reserva) {
        this.setReserva(reserva);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Avaliacao)) {
            return false;
        }
        return getId() != null && getId().equals(((Avaliacao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Avaliacao{" +
            "id=" + getId() +
            ", pontos=" + getPontos() +
            ", comentario='" + getComentario() + "'" +
            "}";
    }
}
