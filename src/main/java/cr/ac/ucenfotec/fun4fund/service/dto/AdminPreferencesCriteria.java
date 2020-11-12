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
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.AdminPreferences} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.AdminPreferencesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /admin-preferences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AdminPreferencesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter inactivityTime;

    private IntegerFilter notificationRecurrence;

    public AdminPreferencesCriteria() {
    }

    public AdminPreferencesCriteria(AdminPreferencesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.inactivityTime = other.inactivityTime == null ? null : other.inactivityTime.copy();
        this.notificationRecurrence = other.notificationRecurrence == null ? null : other.notificationRecurrence.copy();
    }

    @Override
    public AdminPreferencesCriteria copy() {
        return new AdminPreferencesCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getInactivityTime() {
        return inactivityTime;
    }

    public void setInactivityTime(IntegerFilter inactivityTime) {
        this.inactivityTime = inactivityTime;
    }

    public IntegerFilter getNotificationRecurrence() {
        return notificationRecurrence;
    }

    public void setNotificationRecurrence(IntegerFilter notificationRecurrence) {
        this.notificationRecurrence = notificationRecurrence;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AdminPreferencesCriteria that = (AdminPreferencesCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(inactivityTime, that.inactivityTime) &&
            Objects.equals(notificationRecurrence, that.notificationRecurrence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        inactivityTime,
        notificationRecurrence
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminPreferencesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (inactivityTime != null ? "inactivityTime=" + inactivityTime + ", " : "") +
                (notificationRecurrence != null ? "notificationRecurrence=" + notificationRecurrence + ", " : "") +
            "}";
    }

}
