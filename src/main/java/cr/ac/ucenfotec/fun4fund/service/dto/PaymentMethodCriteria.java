package cr.ac.ucenfotec.fun4fund.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.CardType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.PaymentMethod} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.PaymentMethodResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payment-methods?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentMethodCriteria implements Serializable, Criteria {
    /**
     * Class for filtering CardType
     */
    public static class CardTypeFilter extends Filter<CardType> {

        public CardTypeFilter() {
        }

        public CardTypeFilter(CardTypeFilter filter) {
            super(filter);
        }

        @Override
        public CardTypeFilter copy() {
            return new CardTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cardNumber;

    private StringFilter cardOwner;

    private ZonedDateTimeFilter expirationDate;

    private CardTypeFilter type;

    private StringFilter cvc;

    private LongFilter ownerId;

    public PaymentMethodCriteria() {
    }

    public PaymentMethodCriteria(PaymentMethodCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cardNumber = other.cardNumber == null ? null : other.cardNumber.copy();
        this.cardOwner = other.cardOwner == null ? null : other.cardOwner.copy();
        this.expirationDate = other.expirationDate == null ? null : other.expirationDate.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.cvc = other.cvc == null ? null : other.cvc.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
    }

    @Override
    public PaymentMethodCriteria copy() {
        return new PaymentMethodCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(StringFilter cardNumber) {
        this.cardNumber = cardNumber;
    }

    public StringFilter getCardOwner() {
        return cardOwner;
    }

    public void setCardOwner(StringFilter cardOwner) {
        this.cardOwner = cardOwner;
    }

    public ZonedDateTimeFilter getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(ZonedDateTimeFilter expirationDate) {
        this.expirationDate = expirationDate;
    }

    public CardTypeFilter getType() {
        return type;
    }

    public void setType(CardTypeFilter type) {
        this.type = type;
    }

    public StringFilter getCvc() {
        return cvc;
    }

    public void setCvc(StringFilter cvc) {
        this.cvc = cvc;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PaymentMethodCriteria that = (PaymentMethodCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(cardNumber, that.cardNumber) &&
            Objects.equals(cardOwner, that.cardOwner) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(type, that.type) &&
            Objects.equals(cvc, that.cvc) &&
            Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        cardNumber,
        cardOwner,
        expirationDate,
        type,
        cvc,
        ownerId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentMethodCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (cardNumber != null ? "cardNumber=" + cardNumber + ", " : "") +
                (cardOwner != null ? "cardOwner=" + cardOwner + ", " : "") +
                (expirationDate != null ? "expirationDate=" + expirationDate + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (cvc != null ? "cvc=" + cvc + ", " : "") +
                (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            "}";
    }

}
