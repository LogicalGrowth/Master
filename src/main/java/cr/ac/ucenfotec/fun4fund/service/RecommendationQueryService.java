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

import cr.ac.ucenfotec.fun4fund.domain.Recommendation;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.RecommendationRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.RecommendationCriteria;

/**
 * Service for executing complex queries for {@link Recommendation} entities in the database.
 * The main input is a {@link RecommendationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Recommendation} or a {@link Page} of {@link Recommendation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecommendationQueryService extends QueryService<Recommendation> {

    private final Logger log = LoggerFactory.getLogger(RecommendationQueryService.class);

    private final RecommendationRepository recommendationRepository;

    public RecommendationQueryService(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    /**
     * Return a {@link List} of {@link Recommendation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Recommendation> findByCriteria(RecommendationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Recommendation> specification = createSpecification(criteria);
        return recommendationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Recommendation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Recommendation> findByCriteria(RecommendationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Recommendation> specification = createSpecification(criteria);
        return recommendationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecommendationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Recommendation> specification = createSpecification(criteria);
        return recommendationRepository.count(specification);
    }

    /**
     * Function to convert {@link RecommendationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Recommendation> createSpecification(RecommendationCriteria criteria) {
        Specification<Recommendation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Recommendation_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Recommendation_.description));
            }
        }
        return specification;
    }
}
