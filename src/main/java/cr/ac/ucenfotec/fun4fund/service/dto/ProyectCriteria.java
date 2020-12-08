package cr.ac.ucenfotec.fun4fund.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.ProyectType;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.Currency;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.Proyect} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.ProyectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /proyects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProyectCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ProyectType
     */
    public static class ProyectTypeFilter extends Filter<ProyectType> {

        public ProyectTypeFilter() {
        }

        public ProyectTypeFilter(ProyectTypeFilter filter) {
            super(filter);
        }

        @Override
        public ProyectTypeFilter copy() {
            return new ProyectTypeFilter(this);
        }

    }
    /**
     * Class for filtering Currency
     */
    public static class CurrencyFilter extends Filter<Currency> {

        public CurrencyFilter() {
        }

        public CurrencyFilter(CurrencyFilter filter) {
            super(filter);
        }

        @Override
        public CurrencyFilter copy() {
            return new CurrencyFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private ProyectTypeFilter idType;

    private DoubleFilter goalAmount;

    private DoubleFilter collected;

    private DoubleFilter rating;

    private ZonedDateTimeFilter creationDate;

    private ZonedDateTimeFilter lastUpdated;

    private DoubleFilter coordX;

    private DoubleFilter coordY;

    private DoubleFilter fee;

    private StringFilter number;

    private CurrencyFilter currencyType;

    private LongFilter imageId;

    private LongFilter checkpointId;

    private LongFilter reviewId;

    private LongFilter partnerId;

    private LongFilter raffleId;

    private LongFilter auctionId;

    private LongFilter exclusiveContentId;

    private LongFilter paymentId;

    private LongFilter ownerId;

    private LongFilter categoryId;

    private LongFilter proyectId;

    public ProyectCriteria() {
    }

    public ProyectCriteria(ProyectCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.idType = other.idType == null ? null : other.idType.copy();
        this.goalAmount = other.goalAmount == null ? null : other.goalAmount.copy();
        this.collected = other.collected == null ? null : other.collected.copy();
        this.rating = other.rating == null ? null : other.rating.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.lastUpdated = other.lastUpdated == null ? null : other.lastUpdated.copy();
        this.coordX = other.coordX == null ? null : other.coordX.copy();
        this.coordY = other.coordY == null ? null : other.coordY.copy();
        this.fee = other.fee == null ? null : other.fee.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.currencyType = other.currencyType == null ? null : other.currencyType.copy();
        this.imageId = other.imageId == null ? null : other.imageId.copy();
        this.checkpointId = other.checkpointId == null ? null : other.checkpointId.copy();
        this.reviewId = other.reviewId == null ? null : other.reviewId.copy();
        this.partnerId = other.partnerId == null ? null : other.partnerId.copy();
        this.raffleId = other.raffleId == null ? null : other.raffleId.copy();
        this.auctionId = other.auctionId == null ? null : other.auctionId.copy();
        this.exclusiveContentId = other.exclusiveContentId == null ? null : other.exclusiveContentId.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.proyectId = other.proyectId == null ? null : other.proyectId.copy();
    }

    @Override
    public ProyectCriteria copy() {
        return new ProyectCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public ProyectTypeFilter getIdType() {
        return idType;
    }

    public void setIdType(ProyectTypeFilter idType) {
        this.idType = idType;
    }

    public DoubleFilter getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(DoubleFilter goalAmount) {
        this.goalAmount = goalAmount;
    }

    public DoubleFilter getCollected() {
        return collected;
    }

    public void setCollected(DoubleFilter collected) {
        this.collected = collected;
    }

    public DoubleFilter getRating() {
        return rating;
    }

    public void setRating(DoubleFilter rating) {
        this.rating = rating;
    }

    public ZonedDateTimeFilter getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTimeFilter creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTimeFilter getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(ZonedDateTimeFilter lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public DoubleFilter getCoordX() {
        return coordX;
    }

    public void setCoordX(DoubleFilter coordX) {
        this.coordX = coordX;
    }

    public DoubleFilter getCoordY() {
        return coordY;
    }

    public void setCoordY(DoubleFilter coordY) {
        this.coordY = coordY;
    }

    public DoubleFilter getFee() {
        return fee;
    }

    public void setFee(DoubleFilter fee) {
        this.fee = fee;
    }

    public StringFilter getNumber() {
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
    }

    public CurrencyFilter getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyFilter currencyType) {
        this.currencyType = currencyType;
    }

    public LongFilter getImageId() {
        return imageId;
    }

    public void setImageId(LongFilter imageId) {
        this.imageId = imageId;
    }

    public LongFilter getCheckpointId() {
        return checkpointId;
    }

    public void setCheckpointId(LongFilter checkpointId) {
        this.checkpointId = checkpointId;
    }

    public LongFilter getReviewId() {
        return reviewId;
    }

    public void setReviewId(LongFilter reviewId) {
        this.reviewId = reviewId;
    }

    public LongFilter getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(LongFilter partnerId) {
        this.partnerId = partnerId;
    }

    public LongFilter getRaffleId() {
        return raffleId;
    }

    public void setRaffleId(LongFilter raffleId) {
        this.raffleId = raffleId;
    }

    public LongFilter getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(LongFilter auctionId) {
        this.auctionId = auctionId;
    }

    public LongFilter getExclusiveContentId() {
        return exclusiveContentId;
    }

    public void setExclusiveContentId(LongFilter exclusiveContentId) {
        this.exclusiveContentId = exclusiveContentId;
    }

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
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
        final ProyectCriteria that = (ProyectCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(idType, that.idType) &&
            Objects.equals(goalAmount, that.goalAmount) &&
            Objects.equals(collected, that.collected) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(lastUpdated, that.lastUpdated) &&
            Objects.equals(coordX, that.coordX) &&
            Objects.equals(coordY, that.coordY) &&
            Objects.equals(fee, that.fee) &&
            Objects.equals(number, that.number) &&
            Objects.equals(currencyType, that.currencyType) &&
            Objects.equals(imageId, that.imageId) &&
            Objects.equals(checkpointId, that.checkpointId) &&
            Objects.equals(reviewId, that.reviewId) &&
            Objects.equals(partnerId, that.partnerId) &&
            Objects.equals(raffleId, that.raffleId) &&
            Objects.equals(auctionId, that.auctionId) &&
            Objects.equals(exclusiveContentId, that.exclusiveContentId) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(proyectId, that.proyectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        description,
        idType,
        goalAmount,
        collected,
        rating,
        creationDate,
        lastUpdated,
        coordX,
        coordY,
        fee,
        number,
        currencyType,
        imageId,
        checkpointId,
        reviewId,
        partnerId,
        raffleId,
        auctionId,
        exclusiveContentId,
        paymentId,
        ownerId,
        categoryId,
        proyectId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProyectCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (idType != null ? "idType=" + idType + ", " : "") +
                (goalAmount != null ? "goalAmount=" + goalAmount + ", " : "") +
                (collected != null ? "collected=" + collected + ", " : "") +
                (rating != null ? "rating=" + rating + ", " : "") +
                (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
                (lastUpdated != null ? "lastUpdated=" + lastUpdated + ", " : "") +
                (coordX != null ? "coordX=" + coordX + ", " : "") +
                (coordY != null ? "coordY=" + coordY + ", " : "") +
                (fee != null ? "fee=" + fee + ", " : "") +
                (number != null ? "number=" + number + ", " : "") +
                (currencyType != null ? "currencyType=" + currencyType + ", " : "") +
                (imageId != null ? "imageId=" + imageId + ", " : "") +
                (checkpointId != null ? "checkpointId=" + checkpointId + ", " : "") +
                (reviewId != null ? "reviewId=" + reviewId + ", " : "") +
                (partnerId != null ? "partnerId=" + partnerId + ", " : "") +
                (raffleId != null ? "raffleId=" + raffleId + ", " : "") +
                (auctionId != null ? "auctionId=" + auctionId + ", " : "") +
                (exclusiveContentId != null ? "exclusiveContentId=" + exclusiveContentId + ", " : "") +
                (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
                (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
                (proyectId != null ? "proyectId=" + proyectId + ", " : "") +
            "}";
    }

}
