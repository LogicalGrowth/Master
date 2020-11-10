package cr.ac.ucenfotec.fun4fund.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.ActivityStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.ExclusiveContent} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.ExclusiveContentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /exclusive-contents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExclusiveContentCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ActivityStatus
     */
    public static class ActivityStatusFilter extends Filter<ActivityStatus> {

        public ActivityStatusFilter() {
        }

        public ActivityStatusFilter(ActivityStatusFilter filter) {
            super(filter);
        }

        @Override
        public ActivityStatusFilter copy() {
            return new ActivityStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter price;

    private IntegerFilter stock;

    private ActivityStatusFilter state;

    private LongFilter prizeId;

    private LongFilter proyectId;

    public ExclusiveContentCriteria() {
    }

    public ExclusiveContentCriteria(ExclusiveContentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.stock = other.stock == null ? null : other.stock.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.prizeId = other.prizeId == null ? null : other.prizeId.copy();
        this.proyectId = other.proyectId == null ? null : other.proyectId.copy();
    }

    @Override
    public ExclusiveContentCriteria copy() {
        return new ExclusiveContentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public IntegerFilter getStock() {
        return stock;
    }

    public void setStock(IntegerFilter stock) {
        this.stock = stock;
    }

    public ActivityStatusFilter getState() {
        return state;
    }

    public void setState(ActivityStatusFilter state) {
        this.state = state;
    }

    public LongFilter getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(LongFilter prizeId) {
        this.prizeId = prizeId;
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
        final ExclusiveContentCriteria that = (ExclusiveContentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(price, that.price) &&
            Objects.equals(stock, that.stock) &&
            Objects.equals(state, that.state) &&
            Objects.equals(prizeId, that.prizeId) &&
            Objects.equals(proyectId, that.proyectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        price,
        stock,
        state,
        prizeId,
        proyectId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExclusiveContentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (stock != null ? "stock=" + stock + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (prizeId != null ? "prizeId=" + prizeId + ", " : "") +
                (proyectId != null ? "proyectId=" + proyectId + ", " : "") +
            "}";
    }

}
