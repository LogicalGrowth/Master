package cr.ac.ucenfotec.fun4fund.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import cr.ac.ucenfotec.fun4fund.domain.enumeration.ActivityStatus;

/**
 * A ExclusiveContent.
 */
@Entity
@Table(name = "exclusive_content")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExclusiveContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price", nullable = false)
    private Double price;

    @Min(value = 0)
    @Column(name = "stock")
    private Integer stock;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private ActivityStatus state;

    @OneToOne
    @JoinColumn(unique = true)
    private Prize prize;

    @ManyToOne
    @JsonIgnoreProperties(value = "exclusiveContents", allowSetters = true)
    private Proyect proyect;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public ExclusiveContent price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public ExclusiveContent stock(Integer stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public ActivityStatus getState() {
        return state;
    }

    public ExclusiveContent state(ActivityStatus state) {
        this.state = state;
        return this;
    }

    public void setState(ActivityStatus state) {
        this.state = state;
    }

    public Prize getPrize() {
        return prize;
    }

    public ExclusiveContent prize(Prize prize) {
        this.prize = prize;
        return this;
    }

    public void setPrize(Prize prize) {
        this.prize = prize;
    }

    public Proyect getProyect() {
        return proyect;
    }

    public ExclusiveContent proyect(Proyect proyect) {
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
        if (!(o instanceof ExclusiveContent)) {
            return false;
        }
        return id != null && id.equals(((ExclusiveContent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExclusiveContent{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", stock=" + getStock() +
            ", state='" + getState() + "'" +
            "}";
    }
}
