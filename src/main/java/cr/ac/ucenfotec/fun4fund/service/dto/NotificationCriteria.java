package cr.ac.ucenfotec.fun4fund.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.NotificationType;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.NotificationStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.Notification} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NotificationCriteria implements Serializable, Criteria {
    /**
     * Class for filtering NotificationType
     */
    public static class NotificationTypeFilter extends Filter<NotificationType> {

        public NotificationTypeFilter() {
        }

        public NotificationTypeFilter(NotificationTypeFilter filter) {
            super(filter);
        }

        @Override
        public NotificationTypeFilter copy() {
            return new NotificationTypeFilter(this);
        }

    }
    /**
     * Class for filtering NotificationStatus
     */
    public static class NotificationStatusFilter extends Filter<NotificationStatus> {

        public NotificationStatusFilter() {
        }

        public NotificationStatusFilter(NotificationStatusFilter filter) {
            super(filter);
        }

        @Override
        public NotificationStatusFilter copy() {
            return new NotificationStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private NotificationTypeFilter type;

    private StringFilter message;

    private ZonedDateTimeFilter timeStamp;

    private NotificationStatusFilter status;

    private LongFilter applicationUserId;

    public NotificationCriteria() {
    }

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.message = other.message == null ? null : other.message.copy();
        this.timeStamp = other.timeStamp == null ? null : other.timeStamp.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.applicationUserId = other.applicationUserId == null ? null : other.applicationUserId.copy();
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public NotificationTypeFilter getType() {
        return type;
    }

    public void setType(NotificationTypeFilter type) {
        this.type = type;
    }

    public StringFilter getMessage() {
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public ZonedDateTimeFilter getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(ZonedDateTimeFilter timeStamp) {
        this.timeStamp = timeStamp;
    }

    public NotificationStatusFilter getStatus() {
        return status;
    }

    public void setStatus(NotificationStatusFilter status) {
        this.status = status;
    }

    public LongFilter getApplicationUserId() {
        return applicationUserId;
    }

    public void setApplicationUserId(LongFilter applicationUserId) {
        this.applicationUserId = applicationUserId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NotificationCriteria that = (NotificationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(message, that.message) &&
            Objects.equals(timeStamp, that.timeStamp) &&
            Objects.equals(status, that.status) &&
            Objects.equals(applicationUserId, that.applicationUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        type,
        message,
        timeStamp,
        status,
        applicationUserId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (message != null ? "message=" + message + ", " : "") +
                (timeStamp != null ? "timeStamp=" + timeStamp + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (applicationUserId != null ? "applicationUserId=" + applicationUserId + ", " : "") +
            "}";
    }

}
