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

import cr.ac.ucenfotec.fun4fund.domain.Prize;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.PrizeRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.PrizeCriteria;

/**
 * Service for executing complex queries for {@link Prize} entities in the database.
 * The main input is a {@link PrizeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Prize} or a {@link Page} of {@link Prize} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrizeQueryService extends QueryService<Prize> {

    private final Logger log = LoggerFactory.getLogger(PrizeQueryService.class);

    private final PrizeRepository prizeRepository;

    public PrizeQueryService(PrizeRepository prizeRepository) {
        this.prizeRepository = prizeRepository;
    }

    /**
     * Return a {@link List} of {@link Prize} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Prize> findByCriteria(PrizeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Prize> specification = createSpecification(criteria);
        return prizeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Prize} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Prize> findByCriteria(PrizeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Prize> specification = createSpecification(criteria);
        return prizeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrizeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Prize> specification = createSpecification(criteria);
        return prizeRepository.count(specification);
    }

    /**
     * Function to convert {@link PrizeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Prize> createSpecification(PrizeCriteria criteria) {
        Specification<Prize> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Prize_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Prize_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Prize_.description));
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(buildSpecification(criteria.getImageId(),
                    root -> root.join(Prize_.images, JoinType.LEFT).get(Resource_.id)));
            }
        }
        return specification;
    }
}
