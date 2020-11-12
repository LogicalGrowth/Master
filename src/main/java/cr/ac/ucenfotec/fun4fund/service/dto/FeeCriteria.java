package cr.ac.ucenfotec.fun4fund.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.Fee} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.FeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FeeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter percentage;

    private DoubleFilter highestAmount;

    public FeeCriteria() {
    }

    public FeeCriteria(FeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.percentage = other.percentage == null ? null : other.percentage.copy();
        this.highestAmount = other.highestAmount == null ? null : other.highestAmount.copy();
    }

    @Override
    public FeeCriteria copy() {
        return new FeeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getPercentage() {
        return percentage;
    }

    public void setPercentage(DoubleFilter percentage) {
        this.percentage = percentage;
    }

    public DoubleFilter getHighestAmount() {
        return highestAmount;
    }

    public void setHighestAmount(DoubleFilter highestAmount) {
        this.highestAmount = highestAmount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FeeCriteria that = (FeeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(percentage, that.percentage) &&
            Objects.equals(highestAmount, that.highestAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        percentage,
        highestAmount
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (percentage != null ? "percentage=" + percentage + ", " : "") +
                (highestAmount != null ? "highestAmount=" + highestAmount + ", " : "") +
            "}";
    }

}
