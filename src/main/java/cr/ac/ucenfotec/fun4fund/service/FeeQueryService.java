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

import cr.ac.ucenfotec.fun4fund.domain.Fee;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.FeeRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.FeeCriteria;

/**
 * Service for executing complex queries for {@link Fee} entities in the database.
 * The main input is a {@link FeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Fee} or a {@link Page} of {@link Fee} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FeeQueryService extends QueryService<Fee> {

    private final Logger log = LoggerFactory.getLogger(FeeQueryService.class);

    private final FeeRepository feeRepository;

    public FeeQueryService(FeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }

    /**
     * Return a {@link List} of {@link Fee} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Fee> findByCriteria(FeeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Fee> specification = createSpecification(criteria);
        return feeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Fee} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Fee> findByCriteria(FeeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Fee> specification = createSpecification(criteria);
        return feeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FeeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Fee> specification = createSpecification(criteria);
        return feeRepository.count(specification);
    }

    /**
     * Function to convert {@link FeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Fee> createSpecification(FeeCriteria criteria) {
        Specification<Fee> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Fee_.id));
            }
            if (criteria.getPercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPercentage(), Fee_.percentage));
            }
            if (criteria.getHighestAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHighestAmount(), Fee_.highestAmount));
            }
        }
        return specification;
    }
}
