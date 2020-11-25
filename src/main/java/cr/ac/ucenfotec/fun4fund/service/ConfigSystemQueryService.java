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

import cr.ac.ucenfotec.fun4fund.domain.ConfigSystem;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.ConfigSystemRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.ConfigSystemCriteria;

/**
 * Service for executing complex queries for {@link ConfigSystem} entities in the database.
 * The main input is a {@link ConfigSystemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ConfigSystem} or a {@link Page} of {@link ConfigSystem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConfigSystemQueryService extends QueryService<ConfigSystem> {

    private final Logger log = LoggerFactory.getLogger(ConfigSystemQueryService.class);

    private final ConfigSystemRepository configSystemRepository;

    public ConfigSystemQueryService(ConfigSystemRepository configSystemRepository) {
        this.configSystemRepository = configSystemRepository;
    }

    /**
     * Return a {@link List} of {@link ConfigSystem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ConfigSystem> findByCriteria(ConfigSystemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ConfigSystem> specification = createSpecification(criteria);
        return configSystemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ConfigSystem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfigSystem> findByCriteria(ConfigSystemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ConfigSystem> specification = createSpecification(criteria);
        return configSystemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConfigSystemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ConfigSystem> specification = createSpecification(criteria);
        return configSystemRepository.count(specification);
    }

    /**
     * Function to convert {@link ConfigSystemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ConfigSystem> createSpecification(ConfigSystemCriteria criteria) {
        Specification<ConfigSystem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ConfigSystem_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), ConfigSystem_.type));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), ConfigSystem_.value));
            }
        }
        return specification;
    }
}
