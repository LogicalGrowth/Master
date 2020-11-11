package cr.ac.ucenfotec.fun4fund.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Resource.
 */
@Entity
@Table(name = "resource")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @ManyToOne
    @JsonIgnoreProperties(value = "images", allowSetters = true)
    private Proyect proyect;

    @ManyToOne
    @JsonIgnoreProperties(value = "images", allowSetters = true)
    private Prize prize;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public Resource url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public Resource type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Proyect getProyect() {
        return proyect;
    }

    public Resource proyect(Proyect proyect) {
        this.proyect = proyect;
        return this;
    }

    public void setProyect(Proyect proyect) {
        this.proyect = proyect;
    }

    public Prize getPrize() {
        return prize;
    }

    public Resource prize(Prize prize) {
        this.prize = prize;
        return this;
    }

    public void setPrize(Prize prize) {
        this.prize = prize;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resource)) {
            return false;
        }
        return id != null && id.equals(((Resource) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resource{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
