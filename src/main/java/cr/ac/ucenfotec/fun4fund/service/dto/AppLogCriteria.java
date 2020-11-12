package cr.ac.ucenfotec.fun4fund.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.ActionType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.AppLog} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.AppLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /app-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AppLogCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ActionType
     */
    public static class ActionTypeFilter extends Filter<ActionType> {

        public ActionTypeFilter() {
        }

        public ActionTypeFilter(ActionTypeFilter filter) {
            super(filter);
        }

        @Override
        public ActionTypeFilter copy() {
            return new ActionTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter timeStamp;

    private ActionTypeFilter action;

    private StringFilter user;

    private StringFilter description;

    public AppLogCriteria() {
    }

    public AppLogCriteria(AppLogCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.timeStamp = other.timeStamp == null ? null : other.timeStamp.copy();
        this.action = other.action == null ? null : other.action.copy();
        this.user = other.user == null ? null : other.user.copy();
        this.description = other.description == null ? null : other.description.copy();
    }

    @Override
    public AppLogCriteria copy() {
        return new AppLogCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(ZonedDateTimeFilter timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ActionTypeFilter getAction() {
        return action;
    }

    public void setAction(ActionTypeFilter action) {
        this.action = action;
    }

    public StringFilter getUser() {
        return user;
    }

    public void setUser(StringFilter user) {
        this.user = user;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AppLogCriteria that = (AppLogCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(timeStamp, that.timeStamp) &&
            Objects.equals(action, that.action) &&
            Objects.equals(user, that.user) &&
            Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        timeStamp,
        action,
        user,
        description
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppLogCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (timeStamp != null ? "timeStamp=" + timeStamp + ", " : "") +
                (action != null ? "action=" + action + ", " : "") +
                (user != null ? "user=" + user + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
            "}";
    }

}
