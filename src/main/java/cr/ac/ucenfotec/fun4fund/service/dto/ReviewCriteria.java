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
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.Review} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.ReviewResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reviews?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReviewCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter timeStamp;

    private StringFilter message;

    private StringFilter user;

    private DoubleFilter rating;

    private LongFilter proyectId;

    public ReviewCriteria() {
    }

    public ReviewCriteria(ReviewCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.timeStamp = other.timeStamp == null ? null : other.timeStamp.copy();
        this.message = other.message == null ? null : other.message.copy();
        this.user = other.user == null ? null : other.user.copy();
        this.rating = other.rating == null ? null : other.rating.copy();
        this.proyectId = other.proyectId == null ? null : other.proyectId.copy();
    }

    @Override
    public ReviewCriteria copy() {
        return new ReviewCriteria(this);
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

    public StringFilter getMessage() {
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public StringFilter getUser() {
        return user;
    }

    public void setUser(StringFilter user) {
        this.user = user;
    }

    public DoubleFilter getRating() {
        return rating;
    }

    public void setRating(DoubleFilter rating) {
        this.rating = rating;
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
        final ReviewCriteria that = (ReviewCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(timeStamp, that.timeStamp) &&
            Objects.equals(message, that.message) &&
            Objects.equals(user, that.user) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(proyectId, that.proyectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        timeStamp,
        message,
        user,
        rating,
        proyectId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReviewCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (timeStamp != null ? "timeStamp=" + timeStamp + ", " : "") +
                (message != null ? "message=" + message + ", " : "") +
                (user != null ? "user=" + user + ", " : "") +
                (rating != null ? "rating=" + rating + ", " : "") +
                (proyectId != null ? "proyectId=" + proyectId + ", " : "") +
            "}";
    }

}
