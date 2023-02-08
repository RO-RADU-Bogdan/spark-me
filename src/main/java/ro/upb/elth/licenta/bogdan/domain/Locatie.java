package ro.upb.elth.licenta.bogdan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import ro.upb.elth.licenta.bogdan.domain.enumeration.TipAcces;

/**
 * A Locatie.
 */
@Entity
@Table(name = "locatie")
public class Locatie implements Serializable {

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
    @Size(max = 255)
    @Column(name = "descriere", length = 255, nullable = false)
    private String descriere;

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
    @Column(name = "tip_acces", nullable = false)
    private TipAcces tipAcces;

    @ManyToOne
    @JsonIgnoreProperties(value = "locaties", allowSetters = true)
    private User user;

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

    public Locatie denumire(String denumire) {
        this.denumire = denumire;
        return this;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getDescriere() {
        return descriere;
    }

    public Locatie descriere(String descriere) {
        this.descriere = descriere;
        return this;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public Double getLatitudine() {
        return latitudine;
    }

    public Locatie latitudine(Double latitudine) {
        this.latitudine = latitudine;
        return this;
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine = latitudine;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public Locatie longitudine(Double longitudine) {
        this.longitudine = longitudine;
        return this;
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine = longitudine;
    }

    public TipAcces getTipAcces() {
        return tipAcces;
    }

    public Locatie tipAcces(TipAcces tipAcces) {
        this.tipAcces = tipAcces;
        return this;
    }

    public void setTipAcces(TipAcces tipAcces) {
        this.tipAcces = tipAcces;
    }

    public User getUser() {
        return user;
    }

    public Locatie user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Locatie)) {
            return false;
        }
        return id != null && id.equals(((Locatie) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Locatie{" +
            "id=" + getId() +
            ", denumire='" + getDenumire() + "'" +
            ", descriere='" + getDescriere() + "'" +
            ", latitudine=" + getLatitudine() +
            ", longitudine=" + getLongitudine() +
            ", tipAcces='" + getTipAcces() + "'" +
            "}";
    }
}
