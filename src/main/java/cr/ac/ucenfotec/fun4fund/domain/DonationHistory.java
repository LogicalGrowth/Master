package cr.ac.ucenfotec.fun4fund.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A DonationHistory.
 */
@Entity
@Table(name = "donation_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DonationHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Column(name = "time_stamp", nullable = false)
    private ZonedDateTime timeStamp;

    @ManyToOne
    @JsonIgnoreProperties(value = "donations", allowSetters = true)
    private ApplicationUser applicationUser;

    @ManyToOne
    @JsonIgnoreProperties(value = "donations", allowSetters = true)
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

    public DonationHistory amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ZonedDateTime getTimeStamp() {
        return timeStamp;
    }

    public DonationHistory timeStamp(ZonedDateTime timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public void setTimeStamp(ZonedDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public DonationHistory applicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
        return this;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public Proyect getProyect() {
        return proyect;
    }

    public DonationHistory proyect(Proyect proyect) {
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
        if (!(o instanceof DonationHistory)) {
            return false;
        }
        return id != null && id.equals(((DonationHistory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DonationHistory{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", timeStamp='" + getTimeStamp() + "'" +
            "}";
    }
}
