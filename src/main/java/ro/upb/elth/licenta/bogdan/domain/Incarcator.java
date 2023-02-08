package ro.upb.elth.licenta.bogdan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import ro.upb.elth.licenta.bogdan.domain.enumeration.Conector;

import ro.upb.elth.licenta.bogdan.domain.enumeration.Disponibilitate;

/**
 * A Incarcator.
 */
@Entity
@Table(name = "incarcator")
public class Incarcator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 55)
    @Column(name = "denumire_conector", length = 55, nullable = false)
    private String denumireConector;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "conector", nullable = false)
    private Conector conector;

    @Size(max = 255)
    @Column(name = "descriere_conector", length = 255)
    private String descriereConector;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "disponibilitate", nullable = false)
    private Disponibilitate disponibilitate;

    @NotNull
    @Min(value = 0)
    @Max(value = 999)
    @Column(name = "putere", nullable = false)
    private Integer putere;

    @ManyToOne
    @JsonIgnoreProperties(value = "incarcators", allowSetters = true)
    private Statie statie;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenumireConector() {
        return denumireConector;
    }

    public Incarcator denumireConector(String denumireConector) {
        this.denumireConector = denumireConector;
        return this;
    }

    public void setDenumireConector(String denumireConector) {
        this.denumireConector = denumireConector;
    }

    public Conector getConector() {
        return conector;
    }

    public Incarcator conector(Conector conector) {
        this.conector = conector;
        return this;
    }

    public void setConector(Conector conector) {
        this.conector = conector;
    }

    public String getDescriereConector() {
        return descriereConector;
    }

    public Incarcator descriereConector(String descriereConector) {
        this.descriereConector = descriereConector;
        return this;
    }

    public void setDescriereConector(String descriereConector) {
        this.descriereConector = descriereConector;
    }

    public Disponibilitate getDisponibilitate() {
        return disponibilitate;
    }

    public Incarcator disponibilitate(Disponibilitate disponibilitate) {
        this.disponibilitate = disponibilitate;
        return this;
    }

    public void setDisponibilitate(Disponibilitate disponibilitate) {
        this.disponibilitate = disponibilitate;
    }

    public Integer getPutere() {
        return putere;
    }

    public Incarcator putere(Integer putere) {
        this.putere = putere;
        return this;
    }

    public void setPutere(Integer putere) {
        this.putere = putere;
    }

    public Statie getStatie() {
        return statie;
    }

    public Incarcator statie(Statie statie) {
        this.statie = statie;
        return this;
    }

    public void setStatie(Statie statie) {
        this.statie = statie;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Incarcator)) {
            return false;
        }
        return id != null && id.equals(((Incarcator) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Incarcator{" +
            "id=" + getId() +
            ", denumireConector='" + getDenumireConector() + "'" +
            ", conector='" + getConector() + "'" +
            ", descriereConector='" + getDescriereConector() + "'" +
            ", disponibilitate='" + getDisponibilitate() + "'" +
            ", putere=" + getPutere() +
            "}";
    }
}
