package cr.ac.ucenfotec.fun4fund.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import cr.ac.ucenfotec.fun4fund.domain.enumeration.Currency;

/**
 * A ProyectAccount.
 */
@Entity
@Table(name = "proyect_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProyectAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency_type", nullable = false)
    private Currency currencyType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public ProyectAccount number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Currency getCurrencyType() {
        return currencyType;
    }

    public ProyectAccount currencyType(Currency currencyType) {
        this.currencyType = currencyType;
        return this;
    }

    public void setCurrencyType(Currency currencyType) {
        this.currencyType = currencyType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProyectAccount)) {
            return false;
        }
        return id != null && id.equals(((ProyectAccount) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProyectAccount{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", currencyType='" + getCurrencyType() + "'" +
            "}";
    }
}
