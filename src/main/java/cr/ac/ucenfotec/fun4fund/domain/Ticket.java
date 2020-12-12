package cr.ac.ucenfotec.fun4fund.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Ticket.
 */
@Entity
@Table(name = "ticket")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = "tickets", allowSetters = true)
    private ApplicationUser buyer;

    @ManyToOne
    @JsonIgnoreProperties(value = "tickets", allowSetters = true)
    private Raffle raffle;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationUser getBuyer() {
        return buyer;
    }

    public Ticket buyer(ApplicationUser applicationUser) {
        this.buyer = applicationUser;
        return this;
    }

    public void setBuyer(ApplicationUser applicationUser) {
        this.buyer = applicationUser;
    }

    public Raffle getRaffle() {
        return raffle;
    }

    public Ticket raffle(Raffle raffle) {
        this.raffle = raffle;
        return this;
    }

    public void setRaffle(Raffle raffle) {
        this.raffle = raffle;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket)) {
            return false;
        }
        return id != null && id.equals(((Ticket) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ticket{" +
            "id=" + getId() +
            "}";
    }
}
