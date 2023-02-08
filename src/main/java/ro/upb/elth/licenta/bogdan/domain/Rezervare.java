package ro.upb.elth.licenta.bogdan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import ro.upb.elth.licenta.bogdan.domain.enumeration.StatutRezervare;

/**
 * A Rezervare.
 */
@Entity
@Table(name = "rezervare")
public class Rezervare implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "data_creare", nullable = false)
    private Instant dataCreare;

    @NotNull
    @Column(name = "data_expirare", nullable = false)
    private Instant dataExpirare;

    @NotNull
    @Column(name = "data_start", nullable = false)
    private Instant dataStart;

    @NotNull
    @Column(name = "data_final", nullable = false)
    private Instant dataFinal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutRezervare statut;

    @ManyToOne
    @JsonIgnoreProperties(value = "rezervares", allowSetters = true)
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = "rezervares", allowSetters = true)
    private Incarcator incarcator;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataCreare() {
        return dataCreare;
    }

    public Rezervare dataCreare(Instant dataCreare) {
        this.dataCreare = dataCreare;
        return this;
    }

    public void setDataCreare(Instant dataCreare) {
        this.dataCreare = dataCreare;
    }

    public Instant getDataExpirare() {
        return dataExpirare;
    }

    public Rezervare dataExpirare(Instant dataExpirare) {
        this.dataExpirare = dataExpirare;
        return this;
    }

    public void setDataExpirare(Instant dataExpirare) {
        this.dataExpirare = dataExpirare;
    }

    public Instant getDataStart() {
        return dataStart;
    }

    public Rezervare dataStart(Instant dataStart) {
        this.dataStart = dataStart;
        return this;
    }

    public void setDataStart(Instant dataStart) {
        this.dataStart = dataStart;
    }

    public Instant getDataFinal() {
        return dataFinal;
    }

    public Rezervare dataFinal(Instant dataFinal) {
        this.dataFinal = dataFinal;
        return this;
    }

    public void setDataFinal(Instant dataFinal) {
        this.dataFinal = dataFinal;
    }

    public StatutRezervare getStatut() {
        return statut;
    }

    public Rezervare statut(StatutRezervare statut) {
        this.statut = statut;
        return this;
    }

    public void setStatut(StatutRezervare statut) {
        this.statut = statut;
    }

    public User getUser() {
        return user;
    }

    public Rezervare user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Incarcator getIncarcator() {
        return incarcator;
    }

    public Rezervare incarcator(Incarcator incarcator) {
        this.incarcator = incarcator;
        return this;
    }

    public void setIncarcator(Incarcator incarcator) {
        this.incarcator = incarcator;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rezervare)) {
            return false;
        }
        return id != null && id.equals(((Rezervare) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rezervare{" +
            "id=" + getId() +
            ", dataCreare='" + getDataCreare() + "'" +
            ", dataExpirare='" + getDataExpirare() + "'" +
            ", dataStart='" + getDataStart() + "'" +
            ", dataFinal='" + getDataFinal() + "'" +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
