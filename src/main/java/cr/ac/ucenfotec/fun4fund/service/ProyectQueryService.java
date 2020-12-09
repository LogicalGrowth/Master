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

import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.ProyectRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.ProyectCriteria;

/**
 * Service for executing complex queries for {@link Proyect} entities in the database.
 * The main input is a {@link ProyectCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Proyect} or a {@link Page} of {@link Proyect} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProyectQueryService extends QueryService<Proyect> {

    private final Logger log = LoggerFactory.getLogger(ProyectQueryService.class);

    private final ProyectRepository proyectRepository;

    public ProyectQueryService(ProyectRepository proyectRepository) {
        this.proyectRepository = proyectRepository;
    }

    /**
     * Return a {@link List} of {@link Proyect} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Proyect> findByCriteria(ProyectCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Proyect> specification = createSpecification(criteria);
        return proyectRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Proyect} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Proyect> findByCriteria(ProyectCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Proyect> specification = createSpecification(criteria);
        return proyectRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProyectCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Proyect> specification = createSpecification(criteria);
        return proyectRepository.count(specification);
    }

    /**
     * Function to convert {@link ProyectCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Proyect> createSpecification(ProyectCriteria criteria) {
        Specification<Proyect> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Proyect_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Proyect_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Proyect_.description));
            }
            if (criteria.getIdType() != null) {
                specification = specification.and(buildSpecification(criteria.getIdType(), Proyect_.idType));
            }
            if (criteria.getGoalAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGoalAmount(), Proyect_.goalAmount));
            }
            if (criteria.getCollected() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCollected(), Proyect_.collected));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Proyect_.rating));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Proyect_.creationDate));
            }
            if (criteria.getLastUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastUpdated(), Proyect_.lastUpdated));
            }
            if (criteria.getCoordX() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCoordX(), Proyect_.coordX));
            }
            if (criteria.getCoordY() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCoordY(), Proyect_.coordY));
            }
            if (criteria.getFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFee(), Proyect_.fee));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), Proyect_.number));
            }
            if (criteria.getCurrencyType() != null) {
                specification = specification.and(buildSpecification(criteria.getCurrencyType(), Proyect_.currencyType));
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(buildSpecification(criteria.getImageId(),
                    root -> root.join(Proyect_.images, JoinType.LEFT).get(Resource_.id)));
            }
            if (criteria.getCheckpointId() != null) {
                specification = specification.and(buildSpecification(criteria.getCheckpointId(),
                    root -> root.join(Proyect_.checkpoints, JoinType.LEFT).get(Checkpoint_.id)));
            }
            if (criteria.getReviewId() != null) {
                specification = specification.and(buildSpecification(criteria.getReviewId(),
                    root -> root.join(Proyect_.reviews, JoinType.LEFT).get(Review_.id)));
            }
            if (criteria.getPartnerId() != null) {
                specification = specification.and(buildSpecification(criteria.getPartnerId(),
                    root -> root.join(Proyect_.partners, JoinType.LEFT).get(PartnerRequest_.id)));
            }
            if (criteria.getRaffleId() != null) {
                specification = specification.and(buildSpecification(criteria.getRaffleId(),
                    root -> root.join(Proyect_.raffles, JoinType.LEFT).get(Raffle_.id)));
            }
            if (criteria.getAuctionId() != null) {
                specification = specification.and(buildSpecification(criteria.getAuctionId(),
                    root -> root.join(Proyect_.auctions, JoinType.LEFT).get(Auction_.id)));
            }
            if (criteria.getExclusiveContentId() != null) {
                specification = specification.and(buildSpecification(criteria.getExclusiveContentId(),
                    root -> root.join(Proyect_.exclusiveContents, JoinType.LEFT).get(ExclusiveContent_.id)));
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentId(),
                    root -> root.join(Proyect_.payments, JoinType.LEFT).get(Payment_.id)));
            }
            if (criteria.getFavoriteId() != null) {
                specification = specification.and(buildSpecification(criteria.getFavoriteId(),
                    root -> root.join(Proyect_.favorites, JoinType.LEFT).get(Favorite_.id)));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(buildSpecification(criteria.getOwnerId(),
                    root -> root.join(Proyect_.owner, JoinType.LEFT).get(ApplicationUser_.id)));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(Proyect_.category, JoinType.LEFT).get(Category_.id)));
            }
        }
        return specification;
    }
}
