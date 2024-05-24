package com.reservas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TipoQuarto.
 */
@Entity
@Table(name = "tipo_quarto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoQuarto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoQuarto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "fotoQuartos", "reservas", "hotel", "tipoQuarto" }, allowSetters = true)
    private Set<Quarto> quartos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoQuarto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public TipoQuarto nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Quarto> getQuartos() {
        return this.quartos;
    }

    public void setQuartos(Set<Quarto> quartos) {
        if (this.quartos != null) {
            this.quartos.forEach(i -> i.setTipoQuarto(null));
        }
        if (quartos != null) {
            quartos.forEach(i -> i.setTipoQuarto(this));
        }
        this.quartos = quartos;
    }

    public TipoQuarto quartos(Set<Quarto> quartos) {
        this.setQuartos(quartos);
        return this;
    }

    public TipoQuarto addQuarto(Quarto quarto) {
        this.quartos.add(quarto);
        quarto.setTipoQuarto(this);
        return this;
    }

    public TipoQuarto removeQuarto(Quarto quarto) {
        this.quartos.remove(quarto);
        quarto.setTipoQuarto(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoQuarto)) {
            return false;
        }
        return getId() != null && getId().equals(((TipoQuarto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoQuarto{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
