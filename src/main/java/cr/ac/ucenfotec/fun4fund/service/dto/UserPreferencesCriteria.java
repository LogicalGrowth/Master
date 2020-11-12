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
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.UserPreferences} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.UserPreferencesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-preferences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserPreferencesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter notifications;

    public UserPreferencesCriteria() {
    }

    public UserPreferencesCriteria(UserPreferencesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.notifications = other.notifications == null ? null : other.notifications.copy();
    }

    @Override
    public UserPreferencesCriteria copy() {
        return new UserPreferencesCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getNotifications() {
        return notifications;
    }

    public void setNotifications(BooleanFilter notifications) {
        this.notifications = notifications;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserPreferencesCriteria that = (UserPreferencesCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(notifications, that.notifications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        notifications
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPreferencesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (notifications != null ? "notifications=" + notifications + ", " : "") +
            "}";
    }

}
