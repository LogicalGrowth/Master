package cr.ac.ucenfotec.fun4fund.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import cr.ac.ucenfotec.fun4fund.domain.enumeration.RequestStatus;

/**
 * A PartnerRequest.
 */
@Entity
@Table(name = "partner_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PartnerRequest implements Serializable {

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
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = "partnerRequests", allowSetters = true)
    private ApplicationUser applicant;

    @ManyToOne
    @JsonIgnoreProperties(value = "partners", allowSetters = true)
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

    public PartnerRequest amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public PartnerRequest status(RequestStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public ApplicationUser getApplicant() {
        return applicant;
    }

    public PartnerRequest applicant(ApplicationUser applicationUser) {
        this.applicant = applicationUser;
        return this;
    }

    public void setApplicant(ApplicationUser applicationUser) {
        this.applicant = applicationUser;
    }

    public Proyect getProyect() {
        return proyect;
    }

    public PartnerRequest proyect(Proyect proyect) {
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
        if (!(o instanceof PartnerRequest)) {
            return false;
        }
        return id != null && id.equals(((PartnerRequest) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartnerRequest{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
