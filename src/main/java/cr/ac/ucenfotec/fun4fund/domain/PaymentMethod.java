package cr.ac.ucenfotec.fun4fund.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import cr.ac.ucenfotec.fun4fund.domain.enumeration.CardType;

/**
 * A PaymentMethod.
 */
@Entity
@Table(name = "payment_method")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PaymentMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 14, max = 19)
    @Column(name = "card_number", length = 19, nullable = false, unique = true)
    private String cardNumber;

    @NotNull
    @Column(name = "card_owner", nullable = false)
    private String cardOwner;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private ZonedDateTime expirationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CardType type;

    @NotNull
    @Column(name = "cvc", nullable = false)
    private String cvc;

    @NotNull
    @Column(name = "favorite", nullable = false)
    private Boolean favorite;

    @ManyToOne
    @JsonIgnoreProperties(value = "paymentMethods", allowSetters = true)
    private ApplicationUser owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public PaymentMethod cardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardOwner() {
        return cardOwner;
    }

    public PaymentMethod cardOwner(String cardOwner) {
        this.cardOwner = cardOwner;
        return this;
    }

    public void setCardOwner(String cardOwner) {
        this.cardOwner = cardOwner;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public PaymentMethod expirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public CardType getType() {
        return type;
    }

    public PaymentMethod type(CardType type) {
        this.type = type;
        return this;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public String getCvc() {
        return cvc;
    }

    public PaymentMethod cvc(String cvc) {
        this.cvc = cvc;
        return this;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public Boolean isFavorite() {
        return favorite;
    }

    public PaymentMethod favorite(Boolean favorite) {
        this.favorite = favorite;
        return this;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public ApplicationUser getOwner() {
        return owner;
    }

    public PaymentMethod owner(ApplicationUser applicationUser) {
        this.owner = applicationUser;
        return this;
    }

    public void setOwner(ApplicationUser applicationUser) {
        this.owner = applicationUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentMethod)) {
            return false;
        }
        return id != null && id.equals(((PaymentMethod) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentMethod{" +
            "id=" + getId() +
            ", cardNumber='" + getCardNumber() + "'" +
            ", cardOwner='" + getCardOwner() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", type='" + getType() + "'" +
            ", cvc='" + getCvc() + "'" +
            ", favorite='" + isFavorite() + "'" +
            "}";
    }
}
