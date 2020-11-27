package cr.ac.ucenfotec.fun4fund.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import cr.ac.ucenfotec.fun4fund.domain.Auction;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.AuctionRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.AuctionCriteria;

/**
 * Service for executing complex queries for {@link Auction} entities in the database.
 * The main input is a {@link AuctionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Auction} or a {@link Page} of {@link Auction} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuctionQueryService extends QueryService<Auction> {

    private final Logger log = LoggerFactory.getLogger(AuctionQueryService.class);

    private final AuctionRepository auctionRepository;

    public AuctionQueryService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    /**
     * Return a {@link List} of {@link Auction} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Auction> findByCriteria(AuctionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Auction> specification = createSpecification(criteria);
        return auctionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Auction} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Auction> findByCriteria(AuctionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Auction> specification = createSpecification(criteria);
        return auctionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuctionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Auction> specification = createSpecification(criteria);
        return auctionRepository.count(specification);
    }

    /**
     * Function to convert {@link AuctionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Auction> createSpecification(AuctionCriteria criteria) {
        Specification<Auction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Auction_.id));
            }
            if (criteria.getInitialBid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInitialBid(), Auction_.initialBid));
            }
            if (criteria.getWinningBid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWinningBid(), Auction_.winningBid));
            }
            if (criteria.getExpirationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpirationDate(), Auction_.expirationDate));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildSpecification(criteria.getState(), Auction_.state));
            }
            if (criteria.getPrizeId() != null) {
                specification = specification.and(buildSpecification(criteria.getPrizeId(),
                    root -> root.join(Auction_.prize, JoinType.LEFT).get(Prize_.id)));
            }
            if (criteria.getWinnerId() != null) {
                specification = specification.and(buildSpecification(criteria.getWinnerId(),
                    root -> root.join(Auction_.winner, JoinType.LEFT).get(ApplicationUser_.id)));
            }
            if (criteria.getProyectId() != null) {
                specification = specification.and(buildSpecification(criteria.getProyectId(),
                    root -> root.join(Auction_.proyect, JoinType.LEFT).get(Proyect_.id)));
            }
        }
        return specification;
    }
}
