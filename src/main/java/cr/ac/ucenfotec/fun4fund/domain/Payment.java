package cr.ac.ucenfotec.fun4fund.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import cr.ac.ucenfotec.fun4fund.domain.enumeration.ProductType;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ProductType type;

    @NotNull
    @Column(name = "time_stamp", nullable = false)
    private ZonedDateTime timeStamp;

    @ManyToOne
    @JsonIgnoreProperties(value = "payments", allowSetters = true)
    private ApplicationUser applicationUser;

    @ManyToOne
    @JsonIgnoreProperties(value = "payments", allowSetters = true)
    private Proyect proyect;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public Payment amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ProductType getType() {
        return type;
    }

    public Payment type(ProductType type) {
        this.type = type;
        return this;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public ZonedDateTime getTimeStamp() {
        return timeStamp;
    }

    public Payment timeStamp(ZonedDateTime timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public void setTimeStamp(ZonedDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public Payment applicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
        return this;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public Proyect getProyect() {
        return proyect;
    }

    public Payment proyect(Proyect proyect) {
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
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", type='" + getType() + "'" +
            ", timeStamp='" + getTimeStamp() + "'" +
            "}";
    }
}
