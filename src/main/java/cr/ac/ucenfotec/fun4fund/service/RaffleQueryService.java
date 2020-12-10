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

import cr.ac.ucenfotec.fun4fund.domain.Raffle;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.RaffleRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.RaffleCriteria;

/**
 * Service for executing complex queries for {@link Raffle} entities in the database.
 * The main input is a {@link RaffleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Raffle} or a {@link Page} of {@link Raffle} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RaffleQueryService extends QueryService<Raffle> {

    private final Logger log = LoggerFactory.getLogger(RaffleQueryService.class);

    private final RaffleRepository raffleRepository;

    public RaffleQueryService(RaffleRepository raffleRepository) {
        this.raffleRepository = raffleRepository;
    }

    /**
     * Return a {@link List} of {@link Raffle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Raffle> findByCriteria(RaffleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Raffle> specification = createSpecification(criteria);
        return raffleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Raffle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Raffle> findByCriteria(RaffleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Raffle> specification = createSpecification(criteria);
        return raffleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RaffleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Raffle> specification = createSpecification(criteria);
        return raffleRepository.count(specification);
    }

    /**
     * Function to convert {@link RaffleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Raffle> createSpecification(RaffleCriteria criteria) {
        Specification<Raffle> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Raffle_.id));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Raffle_.price));
            }
            if (criteria.getTotalTicket() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalTicket(), Raffle_.totalTicket));
            }
            if (criteria.getExpirationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpirationDate(), Raffle_.expirationDate));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildSpecification(criteria.getState(), Raffle_.state));
            }
            if (criteria.getPrizeId() != null) {
                specification = specification.and(buildSpecification(criteria.getPrizeId(),
                    root -> root.join(Raffle_.prize, JoinType.LEFT).get(Prize_.id)));
            }
            if (criteria.getTicketId() != null) {
                specification = specification.and(buildSpecification(criteria.getTicketId(),
                    root -> root.join(Raffle_.tickets, JoinType.LEFT).get(Ticket_.id)));
            }
            if (criteria.getBuyerId() != null) {
                specification = specification.and(buildSpecification(criteria.getBuyerId(),
                    root -> root.join(Raffle_.buyer, JoinType.LEFT).get(ApplicationUser_.id)));
            }
            if (criteria.getProyectId() != null) {
                specification = specification.and(buildSpecification(criteria.getProyectId(),
                    root -> root.join(Raffle_.proyect, JoinType.LEFT).get(Proyect_.id)));
            }
        }
        return specification;
    }
}
