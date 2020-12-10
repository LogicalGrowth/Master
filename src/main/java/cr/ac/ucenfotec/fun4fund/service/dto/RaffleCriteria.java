package cr.ac.ucenfotec.fun4fund.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.ActivityStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.Raffle} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.RaffleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /raffles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RaffleCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ActivityStatus
     */
    public static class ActivityStatusFilter extends Filter<ActivityStatus> {

        public ActivityStatusFilter() {
        }

        public ActivityStatusFilter(ActivityStatusFilter filter) {
            super(filter);
        }

        @Override
        public ActivityStatusFilter copy() {
            return new ActivityStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter price;

    private IntegerFilter totalTicket;

    private ZonedDateTimeFilter expirationDate;

    private ActivityStatusFilter state;

    private LongFilter prizeId;

    private LongFilter ticketId;

    private LongFilter buyerId;

    private LongFilter proyectId;

    public RaffleCriteria() {
    }

    public RaffleCriteria(RaffleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.totalTicket = other.totalTicket == null ? null : other.totalTicket.copy();
        this.expirationDate = other.expirationDate == null ? null : other.expirationDate.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.prizeId = other.prizeId == null ? null : other.prizeId.copy();
        this.ticketId = other.ticketId == null ? null : other.ticketId.copy();
        this.buyerId = other.buyerId == null ? null : other.buyerId.copy();
        this.proyectId = other.proyectId == null ? null : other.proyectId.copy();
    }

    @Override
    public RaffleCriteria copy() {
        return new RaffleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public IntegerFilter getTotalTicket() {
        return totalTicket;
    }

    public void setTotalTicket(IntegerFilter totalTicket) {
        this.totalTicket = totalTicket;
    }

    public ZonedDateTimeFilter getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(ZonedDateTimeFilter expirationDate) {
        this.expirationDate = expirationDate;
    }

    public ActivityStatusFilter getState() {
        return state;
    }

    public void setState(ActivityStatusFilter state) {
        this.state = state;
    }

    public LongFilter getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(LongFilter prizeId) {
        this.prizeId = prizeId;
    }

    public LongFilter getTicketId() {
        return ticketId;
    }

    public void setTicketId(LongFilter ticketId) {
        this.ticketId = ticketId;
    }

    public LongFilter getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(LongFilter buyerId) {
        this.buyerId = buyerId;
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
        final RaffleCriteria that = (RaffleCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(price, that.price) &&
            Objects.equals(totalTicket, that.totalTicket) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(state, that.state) &&
            Objects.equals(prizeId, that.prizeId) &&
            Objects.equals(ticketId, that.ticketId) &&
            Objects.equals(buyerId, that.buyerId) &&
            Objects.equals(proyectId, that.proyectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        price,
        totalTicket,
        expirationDate,
        state,
        prizeId,
        ticketId,
        buyerId,
        proyectId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RaffleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (totalTicket != null ? "totalTicket=" + totalTicket + ", " : "") +
                (expirationDate != null ? "expirationDate=" + expirationDate + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (prizeId != null ? "prizeId=" + prizeId + ", " : "") +
                (ticketId != null ? "ticketId=" + ticketId + ", " : "") +
                (buyerId != null ? "buyerId=" + buyerId + ", " : "") +
                (proyectId != null ? "proyectId=" + proyectId + ", " : "") +
            "}";
    }

}
