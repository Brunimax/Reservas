package com.reservas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reserva.
 */
@Entity
@Table(name = "reserva")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "data_chekin")
    private Instant dataChekin;

    @Column(name = "data_chekout")
    private Instant dataChekout;

    @Column(name = "data_reserva")
    private Instant dataReserva;

    @OneToOne
    @JoinColumn(unique = false)
    private Avaliacao avaliacao;

    @ManyToOne
    @JoinColumn(unique = false)
    private Quarto quarto;

    @ManyToOne
    @JoinColumn(unique = false)
    private Pessoa pessoa;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reserva id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataChekin() {
        return this.dataChekin;
    }

    public Reserva dataChekin(Instant dataChekin) {
        this.setDataChekin(dataChekin);
        return this;
    }

    public void setDataChekin(Instant dataChekin) {
        this.dataChekin = dataChekin;
    }

    public Instant getDataChekout() {
        return this.dataChekout;
    }

    public Reserva dataChekout(Instant dataChekout) {
        this.setDataChekout(dataChekout);
        return this;
    }

    public void setDataChekout(Instant dataChekout) {
        this.dataChekout = dataChekout;
    }

    public Instant getDataReserva() {
        return this.dataReserva;
    }

    public Reserva dataReserva(Instant dataReserva) {
        this.setDataReserva(dataReserva);
        return this;
    }

    public void setDataReserva(Instant dataReserva) {
        this.dataReserva = dataReserva;
    }

    public Avaliacao getAvaliacao() {
        return this.avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Reserva avaliacao(Avaliacao avaliacao) {
        this.setAvaliacao(avaliacao);
        return this;
    }

    public Quarto getQuarto() {
        return this.quarto;
    }

    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }

    public Reserva quarto(Quarto quarto) {
        this.setQuarto(quarto);
        return this;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Reserva pessoa(Pessoa pessoa) {
        this.setPessoa(pessoa);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reserva)) {
            return false;
        }
        return getId() != null && getId().equals(((Reserva) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reserva{" +
            "id=" + getId() +
            ", dataChekin='" + getDataChekin() + "'" +
            ", dataChekout='" + getDataChekout() + "'" +
            ", dataReserva='" + getDataReserva() + "'" +
            "}";
    }
}
