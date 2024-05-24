package com.reservas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Hotel.
 */
@Entity
@Table(name = "hotel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Hotel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "quantidade_quartos")
    private Long quantidadeQuartos;

    @Column(name = "vagas")
    private Long vagas;

    @Column(name = "classificacao")
    private Long classificacao;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "cep")
    private String cep;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "endereco")
    private String endereco;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hotel")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "fotoQuartos", "reservas", "hotel", "tipoQuarto" }, allowSetters = true)
    private Set<Quarto> quartos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "hotels", "estado" }, allowSetters = true)
    private Municipio municipio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reservas", "hotels" }, allowSetters = true)
    private Pessoa pessoa;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Hotel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Hotel nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getQuantidadeQuartos() {
        return this.quantidadeQuartos;
    }

    public Hotel quantidadeQuartos(Long quantidadeQuartos) {
        this.setQuantidadeQuartos(quantidadeQuartos);
        return this;
    }

    public void setQuantidadeQuartos(Long quantidadeQuartos) {
        this.quantidadeQuartos = quantidadeQuartos;
    }

    public Long getVagas() {
        return this.vagas;
    }

    public Hotel vagas(Long vagas) {
        this.setVagas(vagas);
        return this;
    }

    public void setVagas(Long vagas) {
        this.vagas = vagas;
    }

    public Long getClassificacao() {
        return this.classificacao;
    }

    public Hotel classificacao(Long classificacao) {
        this.setClassificacao(classificacao);
        return this;
    }

    public void setClassificacao(Long classificacao) {
        this.classificacao = classificacao;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Hotel status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCep() {
        return this.cep;
    }

    public Hotel cep(String cep) {
        this.setCep(cep);
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return this.bairro;
    }

    public Hotel bairro(String bairro) {
        this.setBairro(bairro);
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public Hotel endereco(String endereco) {
        this.setEndereco(endereco);
        return this;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Set<Quarto> getQuartos() {
        return this.quartos;
    }

    public void setQuartos(Set<Quarto> quartos) {
        if (this.quartos != null) {
            this.quartos.forEach(i -> i.setHotel(null));
        }
        if (quartos != null) {
            quartos.forEach(i -> i.setHotel(this));
        }
        this.quartos = quartos;
    }

    public Hotel quartos(Set<Quarto> quartos) {
        this.setQuartos(quartos);
        return this;
    }

    public Hotel addQuarto(Quarto quarto) {
        this.quartos.add(quarto);
        quarto.setHotel(this);
        return this;
    }

    public Hotel removeQuarto(Quarto quarto) {
        this.quartos.remove(quarto);
        quarto.setHotel(null);
        return this;
    }

    public Municipio getMunicipio() {
        return this.municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Hotel municipio(Municipio municipio) {
        this.setMunicipio(municipio);
        return this;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Hotel pessoa(Pessoa pessoa) {
        this.setPessoa(pessoa);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hotel)) {
            return false;
        }
        return getId() != null && getId().equals(((Hotel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Hotel{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", quantidadeQuartos=" + getQuantidadeQuartos() +
            ", vagas=" + getVagas() +
            ", classificacao=" + getClassificacao() +
            ", status='" + getStatus() + "'" +
            ", cep='" + getCep() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", endereco='" + getEndereco() + "'" +
            "}";
    }
}
