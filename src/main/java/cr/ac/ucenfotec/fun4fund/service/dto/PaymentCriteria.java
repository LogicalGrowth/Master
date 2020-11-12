package cr.ac.ucenfotec.fun4fund.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.ProductType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.Payment} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.PaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ProductType
     */
    public static class ProductTypeFilter extends Filter<ProductType> {

        public ProductTypeFilter() {
        }

        public ProductTypeFilter(ProductTypeFilter filter) {
            super(filter);
        }

        @Override
        public ProductTypeFilter copy() {
            return new ProductTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter amount;

    private ProductTypeFilter type;

    private ZonedDateTimeFilter timeStamp;

    private LongFilter applicationUserId;

    private LongFilter proyectId;

    public PaymentCriteria() {
    }

    public PaymentCriteria(PaymentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.timeStamp = other.timeStamp == null ? null : other.timeStamp.copy();
        this.applicationUserId = other.applicationUserId == null ? null : other.applicationUserId.copy();
        this.proyectId = other.proyectId == null ? null : other.proyectId.copy();
    }

    @Override
    public PaymentCriteria copy() {
        return new PaymentCriteria(this);
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

    public ProductTypeFilter getType() {
        return type;
    }

    public void setType(ProductTypeFilter type) {
        this.type = type;
    }

    public ZonedDateTimeFilter getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(ZonedDateTimeFilter timeStamp) {
        this.timeStamp = timeStamp;
    }

    public LongFilter getApplicationUserId() {
        return applicationUserId;
    }

    public void setApplicationUserId(LongFilter applicationUserId) {
        this.applicationUserId = applicationUserId;
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
        final PaymentCriteria that = (PaymentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(type, that.type) &&
            Objects.equals(timeStamp, that.timeStamp) &&
            Objects.equals(applicationUserId, that.applicationUserId) &&
            Objects.equals(proyectId, that.proyectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        amount,
        type,
        timeStamp,
        applicationUserId,
        proyectId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (timeStamp != null ? "timeStamp=" + timeStamp + ", " : "") +
                (applicationUserId != null ? "applicationUserId=" + applicationUserId + ", " : "") +
                (proyectId != null ? "proyectId=" + proyectId + ", " : "") +
            "}";
    }

}
