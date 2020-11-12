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

import cr.ac.ucenfotec.fun4fund.domain.Review;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.ReviewRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.ReviewCriteria;

/**
 * Service for executing complex queries for {@link Review} entities in the database.
 * The main input is a {@link ReviewCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Review} or a {@link Page} of {@link Review} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReviewQueryService extends QueryService<Review> {

    private final Logger log = LoggerFactory.getLogger(ReviewQueryService.class);

    private final ReviewRepository reviewRepository;

    public ReviewQueryService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Return a {@link List} of {@link Review} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Review> findByCriteria(ReviewCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Review> specification = createSpecification(criteria);
        return reviewRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Review} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Review> findByCriteria(ReviewCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Review> specification = createSpecification(criteria);
        return reviewRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReviewCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Review> specification = createSpecification(criteria);
        return reviewRepository.count(specification);
    }

    /**
     * Function to convert {@link ReviewCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Review> createSpecification(ReviewCriteria criteria) {
        Specification<Review> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Review_.id));
            }
            if (criteria.getTimeStamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeStamp(), Review_.timeStamp));
            }
            if (criteria.getMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessage(), Review_.message));
            }
            if (criteria.getUser() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUser(), Review_.user));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Review_.rating));
            }
            if (criteria.getProyectId() != null) {
                specification = specification.and(buildSpecification(criteria.getProyectId(),
                    root -> root.join(Review_.proyect, JoinType.LEFT).get(Proyect_.id)));
            }
        }
        return specification;
    }
}
