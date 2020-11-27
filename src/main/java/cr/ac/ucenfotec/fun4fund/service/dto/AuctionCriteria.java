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
 * Criteria class for the {@link cr.ac.ucenfotec.fun4fund.domain.Auction} entity. This class is used
 * in {@link cr.ac.ucenfotec.fun4fund.web.rest.AuctionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /auctions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AuctionCriteria implements Serializable, Criteria {
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

    private DoubleFilter initialBid;

    private DoubleFilter winningBid;

    private ZonedDateTimeFilter expirationDate;

    private ActivityStatusFilter state;

    private LongFilter prizeId;

    private LongFilter winnerId;

    private LongFilter proyectId;

    public AuctionCriteria() {
    }

    public AuctionCriteria(AuctionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.initialBid = other.initialBid == null ? null : other.initialBid.copy();
        this.winningBid = other.winningBid == null ? null : other.winningBid.copy();
        this.expirationDate = other.expirationDate == null ? null : other.expirationDate.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.prizeId = other.prizeId == null ? null : other.prizeId.copy();
        this.winnerId = other.winnerId == null ? null : other.winnerId.copy();
        this.proyectId = other.proyectId == null ? null : other.proyectId.copy();
    }

    @Override
    public AuctionCriteria copy() {
        return new AuctionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getInitialBid() {
        return initialBid;
    }

    public void setInitialBid(DoubleFilter initialBid) {
        this.initialBid = initialBid;
    }

    public DoubleFilter getWinningBid() {
        return winningBid;
    }

    public void setWinningBid(DoubleFilter winningBid) {
        this.winningBid = winningBid;
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

    public LongFilter getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(LongFilter winnerId) {
        this.winnerId = winnerId;
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
        final AuctionCriteria that = (AuctionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(initialBid, that.initialBid) &&
            Objects.equals(winningBid, that.winningBid) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(state, that.state) &&
            Objects.equals(prizeId, that.prizeId) &&
            Objects.equals(winnerId, that.winnerId) &&
            Objects.equals(proyectId, that.proyectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        initialBid,
        winningBid,
        expirationDate,
        state,
        prizeId,
        winnerId,
        proyectId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuctionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (initialBid != null ? "initialBid=" + initialBid + ", " : "") +
                (winningBid != null ? "winningBid=" + winningBid + ", " : "") +
                (expirationDate != null ? "expirationDate=" + expirationDate + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (prizeId != null ? "prizeId=" + prizeId + ", " : "") +
                (winnerId != null ? "winnerId=" + winnerId + ", " : "") +
                (proyectId != null ? "proyectId=" + proyectId + ", " : "") +
            "}";
    }

}
