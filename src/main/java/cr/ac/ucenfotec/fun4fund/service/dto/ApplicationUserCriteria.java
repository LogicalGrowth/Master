package cr.ac.ucenfotec.fun4fund.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.IdType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.ApplicationUser} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.ApplicationUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /application-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ApplicationUserCriteria implements Serializable, Criteria {
    /**
     * Class for filtering IdType
     */
    public static class IdTypeFilter extends Filter<IdType> {

        public IdTypeFilter() {
        }

        public IdTypeFilter(IdTypeFilter filter) {
            super(filter);
        }

        @Override
        public IdTypeFilter copy() {
            return new IdTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter identification;

    private IdTypeFilter idType;

    private ZonedDateTimeFilter birthDate;

    private StringFilter phoneNumber;

    private BooleanFilter admin;

    private LongFilter internalUserId;

    private LongFilter paymentMethodId;

    private LongFilter proyectId;

    private LongFilter notificationId;

    private LongFilter paymentId;

    private LongFilter favoriteId;

    public ApplicationUserCriteria() {
    }

    public ApplicationUserCriteria(ApplicationUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.identification = other.identification == null ? null : other.identification.copy();
        this.idType = other.idType == null ? null : other.idType.copy();
        this.birthDate = other.birthDate == null ? null : other.birthDate.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.admin = other.admin == null ? null : other.admin.copy();
        this.internalUserId = other.internalUserId == null ? null : other.internalUserId.copy();
        this.paymentMethodId = other.paymentMethodId == null ? null : other.paymentMethodId.copy();
        this.proyectId = other.proyectId == null ? null : other.proyectId.copy();
        this.notificationId = other.notificationId == null ? null : other.notificationId.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
        this.favoriteId = other.favoriteId == null ? null : other.favoriteId.copy();
    }

    @Override
    public ApplicationUserCriteria copy() {
        return new ApplicationUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getIdentification() {
        return identification;
    }

    public void setIdentification(StringFilter identification) {
        this.identification = identification;
    }

    public IdTypeFilter getIdType() {
        return idType;
    }

    public void setIdType(IdTypeFilter idType) {
        this.idType = idType;
    }

    public ZonedDateTimeFilter getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(ZonedDateTimeFilter birthDate) {
        this.birthDate = birthDate;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BooleanFilter getAdmin() {
        return admin;
    }

    public void setAdmin(BooleanFilter admin) {
        this.admin = admin;
    }

    public LongFilter getInternalUserId() {
        return internalUserId;
    }

    public void setInternalUserId(LongFilter internalUserId) {
        this.internalUserId = internalUserId;
    }

    public LongFilter getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(LongFilter paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public LongFilter getProyectId() {
        return proyectId;
    }

    public void setProyectId(LongFilter proyectId) {
        this.proyectId = proyectId;
    }

    public LongFilter getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(LongFilter notificationId) {
        this.notificationId = notificationId;
    }

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
    }

    public LongFilter getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(LongFilter favoriteId) {
        this.favoriteId = favoriteId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ApplicationUserCriteria that = (ApplicationUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(identification, that.identification) &&
            Objects.equals(idType, that.idType) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(admin, that.admin) &&
            Objects.equals(internalUserId, that.internalUserId) &&
            Objects.equals(paymentMethodId, that.paymentMethodId) &&
            Objects.equals(proyectId, that.proyectId) &&
            Objects.equals(notificationId, that.notificationId) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(favoriteId, that.favoriteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        identification,
        idType,
        birthDate,
        phoneNumber,
        admin,
        internalUserId,
        paymentMethodId,
        proyectId,
        notificationId,
        paymentId,
        favoriteId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (identification != null ? "identification=" + identification + ", " : "") +
                (idType != null ? "idType=" + idType + ", " : "") +
                (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (admin != null ? "admin=" + admin + ", " : "") +
                (internalUserId != null ? "internalUserId=" + internalUserId + ", " : "") +
                (paymentMethodId != null ? "paymentMethodId=" + paymentMethodId + ", " : "") +
                (proyectId != null ? "proyectId=" + proyectId + ", " : "") +
                (notificationId != null ? "notificationId=" + notificationId + ", " : "") +
                (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
                (favoriteId != null ? "favoriteId=" + favoriteId + ", " : "") +
            "}";
    }

}
