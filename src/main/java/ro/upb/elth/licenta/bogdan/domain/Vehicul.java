package ro.upb.elth.licenta.bogdan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import ro.upb.elth.licenta.bogdan.domain.enumeration.Conector;

/**
 * A Vehicul.
 */
@Entity
@Table(name = "vehicul")
public class Vehicul implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "marca", length = 50, nullable = false)
    private String marca;

    @NotNull
    @Size(max = 50)
    @Column(name = "model", length = 50, nullable = false)
    private String model;

    @NotNull
    @Min(value = 1990)
    @Max(value = 2021)
    @Column(name = "an_fabricatie", nullable = false)
    private Integer anFabricatie;

    @NotNull
    @Size(max = 50)
    @Column(name = "culoare", length = 50, nullable = false)
    private String culoare;

    @Size(max = 255)
    @Column(name = "descriere", length = 255)
    private String descriere;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "conector", nullable = false)
    private Conector conector;

    @ManyToOne
    @JsonIgnoreProperties(value = "vehiculs", allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public Vehicul marca(String marca) {
        this.marca = marca;
        return this;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModel() {
        return model;
    }

    public Vehicul model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getAnFabricatie() {
        return anFabricatie;
    }

    public Vehicul anFabricatie(Integer anFabricatie) {
        this.anFabricatie = anFabricatie;
        return this;
    }

    public void setAnFabricatie(Integer anFabricatie) {
        this.anFabricatie = anFabricatie;
    }

    public String getCuloare() {
        return culoare;
    }

    public Vehicul culoare(String culoare) {
        this.culoare = culoare;
        return this;
    }

    public void setCuloare(String culoare) {
        this.culoare = culoare;
    }

    public String getDescriere() {
        return descriere;
    }

    public Vehicul descriere(String descriere) {
        this.descriere = descriere;
        return this;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public Conector getConector() {
        return conector;
    }

    public Vehicul conector(Conector conector) {
        this.conector = conector;
        return this;
    }

    public void setConector(Conector conector) {
        this.conector = conector;
    }

    public User getUser() {
        return user;
    }

    public Vehicul user(User user) {
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
        if (!(o instanceof Vehicul)) {
            return false;
        }
        return id != null && id.equals(((Vehicul) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicul{" +
            "id=" + getId() +
            ", marca='" + getMarca() + "'" +
            ", model='" + getModel() + "'" +
            ", anFabricatie=" + getAnFabricatie() +
            ", culoare='" + getCuloare() + "'" +
            ", descriere='" + getDescriere() + "'" +
            ", conector='" + getConector() + "'" +
            "}";
    }
}
