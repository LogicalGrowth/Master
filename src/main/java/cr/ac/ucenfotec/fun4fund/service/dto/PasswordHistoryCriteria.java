package cr.ac.ucenfotec.fun4fund.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.PasswordStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.PasswordHistory} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.PasswordHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /password-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PasswordHistoryCriteria implements Serializable, Criteria {
    /**
     * Class for filtering PasswordStatus
     */
    public static class PasswordStatusFilter extends Filter<PasswordStatus> {

        public PasswordStatusFilter() {
        }

        public PasswordStatusFilter(PasswordStatusFilter filter) {
            super(filter);
        }

        @Override
        public PasswordStatusFilter copy() {
            return new PasswordStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter password;

    private ZonedDateTimeFilter startDate;

    private ZonedDateTimeFilter endDate;

    private PasswordStatusFilter status;

    public PasswordHistoryCriteria() {
    }

    public PasswordHistoryCriteria(PasswordHistoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.status = other.status == null ? null : other.status.copy();
    }

    @Override
    public PasswordHistoryCriteria copy() {
        return new PasswordHistoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPassword() {
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public ZonedDateTimeFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTimeFilter startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTimeFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTimeFilter endDate) {
        this.endDate = endDate;
    }

    public PasswordStatusFilter getStatus() {
        return status;
    }

    public void setStatus(PasswordStatusFilter status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PasswordHistoryCriteria that = (PasswordHistoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(password, that.password) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        password,
        startDate,
        endDate,
        status
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PasswordHistoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (password != null ? "password=" + password + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
            "}";
    }

}
