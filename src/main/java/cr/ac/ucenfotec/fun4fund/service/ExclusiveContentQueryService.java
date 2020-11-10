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

import cr.ac.ucenfotec.fun4fund.domain.ExclusiveContent;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.ExclusiveContentRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.ExclusiveContentCriteria;

/**
 * Service for executing complex queries for {@link ExclusiveContent} entities in the database.
 * The main input is a {@link ExclusiveContentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExclusiveContent} or a {@link Page} of {@link ExclusiveContent} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExclusiveContentQueryService extends QueryService<ExclusiveContent> {

    private final Logger log = LoggerFactory.getLogger(ExclusiveContentQueryService.class);

    private final ExclusiveContentRepository exclusiveContentRepository;

    public ExclusiveContentQueryService(ExclusiveContentRepository exclusiveContentRepository) {
        this.exclusiveContentRepository = exclusiveContentRepository;
    }

    /**
     * Return a {@link List} of {@link ExclusiveContent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExclusiveContent> findByCriteria(ExclusiveContentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExclusiveContent> specification = createSpecification(criteria);
        return exclusiveContentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ExclusiveContent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExclusiveContent> findByCriteria(ExclusiveContentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExclusiveContent> specification = createSpecification(criteria);
        return exclusiveContentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExclusiveContentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExclusiveContent> specification = createSpecification(criteria);
        return exclusiveContentRepository.count(specification);
    }

    /**
     * Function to convert {@link ExclusiveContentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExclusiveContent> createSpecification(ExclusiveContentCriteria criteria) {
        Specification<ExclusiveContent> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExclusiveContent_.id));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ExclusiveContent_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), ExclusiveContent_.stock));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildSpecification(criteria.getState(), ExclusiveContent_.state));
            }
            if (criteria.getPrizeId() != null) {
                specification = specification.and(buildSpecification(criteria.getPrizeId(),
                    root -> root.join(ExclusiveContent_.prize, JoinType.LEFT).get(Prize_.id)));
            }
            if (criteria.getProyectId() != null) {
                specification = specification.and(buildSpecification(criteria.getProyectId(),
                    root -> root.join(ExclusiveContent_.proyect, JoinType.LEFT).get(Proyect_.id)));
            }
        }
        return specification;
    }
}
