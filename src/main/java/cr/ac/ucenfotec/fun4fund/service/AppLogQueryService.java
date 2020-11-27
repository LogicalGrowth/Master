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

import cr.ac.ucenfotec.fun4fund.domain.AppLog;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.AppLogRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.AppLogCriteria;

/**
 * Service for executing complex queries for {@link AppLog} entities in the database.
 * The main input is a {@link AppLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AppLog} or a {@link Page} of {@link AppLog} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppLogQueryService extends QueryService<AppLog> {

    private final Logger log = LoggerFactory.getLogger(AppLogQueryService.class);

    private final AppLogRepository appLogRepository;

    public AppLogQueryService(AppLogRepository appLogRepository) {
        this.appLogRepository = appLogRepository;
    }

    /**
     * Return a {@link List} of {@link AppLog} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AppLog> findByCriteria(AppLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AppLog> specification = createSpecification(criteria);
        return appLogRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AppLog} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppLog> findByCriteria(AppLogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AppLog> specification = createSpecification(criteria);
        return appLogRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AppLog> specification = createSpecification(criteria);
        return appLogRepository.count(specification);
    }

    /**
     * Function to convert {@link AppLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AppLog> createSpecification(AppLogCriteria criteria) {
        Specification<AppLog> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AppLog_.id));
            }
            if (criteria.getTimeStamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeStamp(), AppLog_.timeStamp));
            }
            if (criteria.getAction() != null) {
                specification = specification.and(buildSpecification(criteria.getAction(), AppLog_.action));
            }
            if (criteria.getUser() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUser(), AppLog_.user));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AppLog_.description));
            }
        }
        return specification;
    }
}
