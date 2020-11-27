package cr.ac.ucenfotec.fun4fund.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.RequestStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.PartnerRequest} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.PartnerRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /partner-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PartnerRequestCriteria implements Serializable, Criteria {
    /**
     * Class for filtering RequestStatus
     */
    public static class RequestStatusFilter extends Filter<RequestStatus> {

        public RequestStatusFilter() {
        }

        public RequestStatusFilter(RequestStatusFilter filter) {
            super(filter);
        }

        @Override
        public RequestStatusFilter copy() {
            return new RequestStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter amount;

    private RequestStatusFilter status;

    private LongFilter applicantId;

    private LongFilter proyectId;

    public PartnerRequestCriteria() {
    }

    public PartnerRequestCriteria(PartnerRequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.applicantId = other.applicantId == null ? null : other.applicantId.copy();
        this.proyectId = other.proyectId == null ? null : other.proyectId.copy();
    }

    @Override
    public PartnerRequestCriteria copy() {
        return new PartnerRequestCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public RequestStatusFilter getStatus() {
        return status;
    }

    public void setStatus(RequestStatusFilter status) {
        this.status = status;
    }

    public LongFilter getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(LongFilter applicantId) {
        this.applicantId = applicantId;
    }

    public LongFilter getProyectId() {
        return proyectId;
    }

    public void setProyectId(LongFilter proyectId) {
        this.proyectId = proyectId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PartnerRequestCriteria that = (PartnerRequestCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(status, that.status) &&
            Objects.equals(applicantId, that.applicantId) &&
            Objects.equals(proyectId, that.proyectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        amount,
        status,
        applicantId,
        proyectId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartnerRequestCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (applicantId != null ? "applicantId=" + applicantId + ", " : "") +
                (proyectId != null ? "proyectId=" + proyectId + ", " : "") +
            "}";
    }

}
