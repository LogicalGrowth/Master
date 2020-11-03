package cr.ac.ucenfotec.fun4fund.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Review.
 */
@Entity
@Table(name = "review")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "time_stamp", nullable = false)
    private ZonedDateTime timeStamp;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Column(name = "user", nullable = false)
    private String user;

    @NotNull
    @Column(name = "rating", nullable = false)
    private Double rating;

    @ManyToOne
    @JsonIgnoreProperties(value = "reviews", allowSetters = true)
    private Proyect proyect;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTimeStamp() {
        return timeStamp;
    }

    public Review timeStamp(ZonedDateTime timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public void setTimeStamp(ZonedDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public Review message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public Review user(String user) {
        this.user = user;
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Double getRating() {
        return rating;
    }

    public Review rating(Double rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Proyect getProyect() {
        return proyect;
    }

    public Review proyect(Proyect proyect) {
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
        if (!(o instanceof Review)) {
            return false;
        }
        return id != null && id.equals(((Review) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Review{" +
            "id=" + getId() +
            ", timeStamp='" + getTimeStamp() + "'" +
            ", message='" + getMessage() + "'" +
            ", user='" + getUser() + "'" +
            ", rating=" + getRating() +
            "}";
    }
}
