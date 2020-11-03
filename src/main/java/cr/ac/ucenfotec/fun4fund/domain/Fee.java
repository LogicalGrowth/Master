package cr.ac.ucenfotec.fun4fund.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Fee.
 */
@Entity
@Table(name = "fee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "percentage", nullable = false)
    private Double percentage;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "highest_amount", nullable = false)
    private Double highestAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPercentage() {
        return percentage;
    }

    public Fee percentage(Double percentage) {
        this.percentage = percentage;
        return this;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Double getHighestAmount() {
        return highestAmount;
    }

    public Fee highestAmount(Double highestAmount) {
        this.highestAmount = highestAmount;
        return this;
    }

    public void setHighestAmount(Double highestAmount) {
        this.highestAmount = highestAmount;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fee)) {
            return false;
        }
        return id != null && id.equals(((Fee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fee{" +
            "id=" + getId() +
            ", percentage=" + getPercentage() +
            ", highestAmount=" + getHighestAmount() +
            "}";
    }
}
