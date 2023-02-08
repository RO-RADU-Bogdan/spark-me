package ro.upb.elth.licenta.bogdan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import ro.upb.elth.licenta.bogdan.domain.enumeration.StatutStatie;

import ro.upb.elth.licenta.bogdan.domain.enumeration.TipCost;

/**
 * A Statie.
 */
@Entity
@Table(name = "statie")
public class Statie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "denumire", length = 50, nullable = false)
    private String denumire;

    @NotNull
    @Size(max = 50)
    @Column(name = "producator", length = 50, nullable = false)
    private String producator;

    @NotNull
    @Size(max = 50)
    @Column(name = "model", length = 50, nullable = false)
    private String model;

    @DecimalMin(value = "-90")
    @DecimalMax(value = "90")
    @Column(name = "latitudine")
    private Double latitudine;

    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    @Column(name = "longitudine")
    private Double longitudine;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutStatie statut;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tip_cost", nullable = false)
    private TipCost tipCost;

    @Size(max = 255)
    @Column(name = "descriere_cost", length = 255)
    private String descriereCost;

    @ManyToOne
    @JsonIgnoreProperties(value = "staties", allowSetters = true)
    private Locatie locatie;

    @ManyToOne
    @JsonIgnoreProperties(value = "staties", allowSetters = true)
    private Retea retea;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenumire() {
        return denumire;
    }

    public Statie denumire(String denumire) {
        this.denumire = denumire;
        return this;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getProducator() {
        return producator;
    }

    public Statie producator(String producator) {
        this.producator = producator;
        return this;
    }

    public void setProducator(String producator) {
        this.producator = producator;
    }

    public String getModel() {
        return model;
    }

    public Statie model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getLatitudine() {
        return latitudine;
    }

    public Statie latitudine(Double latitudine) {
        this.latitudine = latitudine;
        return this;
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine = latitudine;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public Statie longitudine(Double longitudine) {
        this.longitudine = longitudine;
        return this;
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine = longitudine;
    }

    public StatutStatie getStatut() {
        return statut;
    }

    public Statie statut(StatutStatie statut) {
        this.statut = statut;
        return this;
    }

    public void setStatut(StatutStatie statut) {
        this.statut = statut;
    }

    public TipCost getTipCost() {
        return tipCost;
    }

    public Statie tipCost(TipCost tipCost) {
        this.tipCost = tipCost;
        return this;
    }

    public void setTipCost(TipCost tipCost) {
        this.tipCost = tipCost;
    }

    public String getDescriereCost() {
        return descriereCost;
    }

    public Statie descriereCost(String descriereCost) {
        this.descriereCost = descriereCost;
        return this;
    }

    public void setDescriereCost(String descriereCost) {
        this.descriereCost = descriereCost;
    }

    public Locatie getLocatie() {
        return locatie;
    }

    public Statie locatie(Locatie locatie) {
        this.locatie = locatie;
        return this;
    }

    public void setLocatie(Locatie locatie) {
        this.locatie = locatie;
    }

    public Retea getRetea() {
        return retea;
    }

    public Statie retea(Retea retea) {
        this.retea = retea;
        return this;
    }

    public void setRetea(Retea retea) {
        this.retea = retea;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Statie)) {
            return false;
        }
        return id != null && id.equals(((Statie) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Statie{" +
            "id=" + getId() +
            ", denumire='" + getDenumire() + "'" +
            ", producator='" + getProducator() + "'" +
            ", model='" + getModel() + "'" +
            ", latitudine=" + getLatitudine() +
            ", longitudine=" + getLongitudine() +
            ", statut='" + getStatut() + "'" +
            ", tipCost='" + getTipCost() + "'" +
            ", descriereCost='" + getDescriereCost() + "'" +
            "}";
    }
}
