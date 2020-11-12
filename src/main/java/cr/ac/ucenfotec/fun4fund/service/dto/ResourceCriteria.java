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
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.Resource} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.ResourceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /resources?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ResourceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter url;

    private StringFilter type;

    private LongFilter proyectId;

    private LongFilter prizeId;

    public ResourceCriteria() {
    }

    public ResourceCriteria(ResourceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.proyectId = other.proyectId == null ? null : other.proyectId.copy();
        this.prizeId = other.prizeId == null ? null : other.prizeId.copy();
    }

    @Override
    public ResourceCriteria copy() {
        return new ResourceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getProyectId() {
        return proyectId;
    }

    public void setProyectId(LongFilter proyectId) {
        this.proyectId = proyectId;
    }

    public LongFilter getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(LongFilter prizeId) {
        this.prizeId = prizeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ResourceCriteria that = (ResourceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(url, that.url) &&
            Objects.equals(type, that.type) &&
            Objects.equals(proyectId, that.proyectId) &&
            Objects.equals(prizeId, that.prizeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        url,
        type,
        proyectId,
        prizeId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (proyectId != null ? "proyectId=" + proyectId + ", " : "") +
                (prizeId != null ? "prizeId=" + prizeId + ", " : "") +
            "}";
    }

}
