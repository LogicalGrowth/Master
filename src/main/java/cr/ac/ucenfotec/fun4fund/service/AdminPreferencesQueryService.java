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

import cr.ac.ucenfotec.fun4fund.domain.AdminPreferences;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.AdminPreferencesRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.AdminPreferencesCriteria;

/**
 * Service for executing complex queries for {@link AdminPreferences} entities in the database.
 * The main input is a {@link AdminPreferencesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AdminPreferences} or a {@link Page} of {@link AdminPreferences} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdminPreferencesQueryService extends QueryService<AdminPreferences> {

    private final Logger log = LoggerFactory.getLogger(AdminPreferencesQueryService.class);

    private final AdminPreferencesRepository adminPreferencesRepository;

    public AdminPreferencesQueryService(AdminPreferencesRepository adminPreferencesRepository) {
        this.adminPreferencesRepository = adminPreferencesRepository;
    }

    /**
     * Return a {@link List} of {@link AdminPreferences} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AdminPreferences> findByCriteria(AdminPreferencesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AdminPreferences> specification = createSpecification(criteria);
        return adminPreferencesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AdminPreferences} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AdminPreferences> findByCriteria(AdminPreferencesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AdminPreferences> specification = createSpecification(criteria);
        return adminPreferencesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AdminPreferencesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AdminPreferences> specification = createSpecification(criteria);
        return adminPreferencesRepository.count(specification);
    }

    /**
     * Function to convert {@link AdminPreferencesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AdminPreferences> createSpecification(AdminPreferencesCriteria criteria) {
        Specification<AdminPreferences> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AdminPreferences_.id));
            }
            if (criteria.getInactivityTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInactivityTime(), AdminPreferences_.inactivityTime));
            }
            if (criteria.getNotificationRecurrence() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNotificationRecurrence(), AdminPreferences_.notificationRecurrence));
            }
        }
        return specification;
    }
}
