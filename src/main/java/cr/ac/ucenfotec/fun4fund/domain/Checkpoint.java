package cr.ac.ucenfotec.fun4fund.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Checkpoint.
 */
@Entity
@Table(name = "checkpoint")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Checkpoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "completition_percentage", nullable = false)
    private Double completitionPercentage;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Column(name = "completed", nullable = false)
    private Boolean completed;

    @ManyToOne
    @JsonIgnoreProperties(value = "checkpoints", allowSetters = true)
    private Proyect proyect;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCompletitionPercentage() {
        return completitionPercentage;
    }

    public Checkpoint completitionPercentage(Double completitionPercentage) {
        this.completitionPercentage = completitionPercentage;
        return this;
    }

    public void setCompletitionPercentage(Double completitionPercentage) {
        this.completitionPercentage = completitionPercentage;
    }

    public String getMessage() {
        return message;
    }

    public Checkpoint message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public Checkpoint completed(Boolean completed) {
        this.completed = completed;
        return this;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Proyect getProyect() {
        return proyect;
    }

    public Checkpoint proyect(Proyect proyect) {
        this.proyect = proyect;
        return this;
    }

    public void setProyect(Proyect proyect) {
        this.proyect = proyect;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Checkpoint)) {
            return false;
        }
        return id != null && id.equals(((Checkpoint) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Checkpoint{" +
            "id=" + getId() +
            ", completitionPercentage=" + getCompletitionPercentage() +
            ", message='" + getMessage() + "'" +
            ", completed='" + isCompleted() + "'" +
            "}";
    }
}
