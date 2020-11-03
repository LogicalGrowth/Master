package cr.ac.ucenfotec.fun4fund.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import cr.ac.ucenfotec.fun4fund.domain.enumeration.ActivityStatus;

/**
 * A Auction.
 */
@Entity
@Table(name = "auction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Auction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "initial_bid", nullable = false)
    private Double initialBid;

    @DecimalMin(value = "0")
    @Column(name = "winning_bid")
    private Double winningBid;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private ZonedDateTime expirationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private ActivityStatus state;

    @OneToOne
    @JoinColumn(unique = true)
    private Prize prize;

    @OneToOne
    @JoinColumn(unique = true)
    private ApplicationUser winner;

    @ManyToOne
    @JsonIgnoreProperties(value = "auctions", allowSetters = true)
    private Proyect proyect;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getInitialBid() {
        return initialBid;
    }

    public Auction initialBid(Double initialBid) {
        this.initialBid = initialBid;
        return this;
    }

    public void setInitialBid(Double initialBid) {
        this.initialBid = initialBid;
    }

    public Double getWinningBid() {
        return winningBid;
    }

    public Auction winningBid(Double winningBid) {
        this.winningBid = winningBid;
        return this;
    }

    public void setWinningBid(Double winningBid) {
        this.winningBid = winningBid;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public Auction expirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public ActivityStatus getState() {
        return state;
    }

    public Auction state(ActivityStatus state) {
        this.state = state;
        return this;
    }

    public void setState(ActivityStatus state) {
        this.state = state;
    }

    public Prize getPrize() {
        return prize;
    }

    public Auction prize(Prize prize) {
        this.prize = prize;
        return this;
    }

    public void setPrize(Prize prize) {
        this.prize = prize;
    }

    public ApplicationUser getWinner() {
        return winner;
    }

    public Auction winner(ApplicationUser applicationUser) {
        this.winner = applicationUser;
        return this;
    }

    public void setWinner(ApplicationUser applicationUser) {
        this.winner = applicationUser;
    }

    public Proyect getProyect() {
        return proyect;
    }

    public Auction proyect(Proyect proyect) {
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
        if (!(o instanceof Auction)) {
            return false;
        }
        return id != null && id.equals(((Auction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Auction{" +
            "id=" + getId() +
            ", initialBid=" + getInitialBid() +
            ", winningBid=" + getWinningBid() +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
