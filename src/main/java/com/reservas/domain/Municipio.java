package com.reservas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Municipio.
 */
@Entity
@Table(name = "municipio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Municipio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "municipio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "quartos", "municipio", "pessoa" }, allowSetters = true)
    private Set<Hotel> hotels = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "municipios", "pais" }, allowSetters = true)
    private Estado estado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Municipio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Municipio nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Hotel> getHotels() {
        return this.hotels;
    }

    public void setHotels(Set<Hotel> hotels) {
        if (this.hotels != null) {
            this.hotels.forEach(i -> i.setMunicipio(null));
        }
        if (hotels != null) {
            hotels.forEach(i -> i.setMunicipio(this));
        }
        this.hotels = hotels;
    }

    public Municipio hotels(Set<Hotel> hotels) {
        this.setHotels(hotels);
        return this;
    }

    public Municipio addHotel(Hotel hotel) {
        this.hotels.add(hotel);
        hotel.setMunicipio(this);
        return this;
    }

    public Municipio removeHotel(Hotel hotel) {
        this.hotels.remove(hotel);
        hotel.setMunicipio(null);
        return this;
    }

    public Estado getEstado() {
        return this.estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Municipio estado(Estado estado) {
        this.setEstado(estado);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Municipio)) {
            return false;
        }
        return getId() != null && getId().equals(((Municipio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Municipio{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
