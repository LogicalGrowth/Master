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
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.Checkpoint} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.CheckpointResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /checkpoints?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CheckpointCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter completitionPercentage;

    private StringFilter message;

    private BooleanFilter completed;

    private LongFilter proyectId;

    public CheckpointCriteria() {
    }

    public CheckpointCriteria(CheckpointCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.completitionPercentage = other.completitionPercentage == null ? null : other.completitionPercentage.copy();
        this.message = other.message == null ? null : other.message.copy();
        this.completed = other.completed == null ? null : other.completed.copy();
        this.proyectId = other.proyectId == null ? null : other.proyectId.copy();
    }

    @Override
    public CheckpointCriteria copy() {
        return new CheckpointCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getCompletitionPercentage() {
        return completitionPercentage;
    }

    public void setCompletitionPercentage(DoubleFilter completitionPercentage) {
        this.completitionPercentage = completitionPercentage;
    }

    public StringFilter getMessage() {
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public BooleanFilter getCompleted() {
        return completed;
    }

    public void setCompleted(BooleanFilter completed) {
        this.completed = completed;
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
        final CheckpointCriteria that = (CheckpointCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(completitionPercentage, that.completitionPercentage) &&
            Objects.equals(message, that.message) &&
            Objects.equals(completed, that.completed) &&
            Objects.equals(proyectId, that.proyectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        completitionPercentage,
        message,
        completed,
        proyectId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CheckpointCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (completitionPercentage != null ? "completitionPercentage=" + completitionPercentage + ", " : "") +
                (message != null ? "message=" + message + ", " : "") +
                (completed != null ? "completed=" + completed + ", " : "") +
                (proyectId != null ? "proyectId=" + proyectId + ", " : "") +
            "}";
    }

}
