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

import cr.ac.ucenfotec.fun4fund.domain.UserPreferences;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.UserPreferencesRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.UserPreferencesCriteria;

/**
 * Service for executing complex queries for {@link UserPreferences} entities in the database.
 * The main input is a {@link UserPreferencesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserPreferences} or a {@link Page} of {@link UserPreferences} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserPreferencesQueryService extends QueryService<UserPreferences> {

    private final Logger log = LoggerFactory.getLogger(UserPreferencesQueryService.class);

    private final UserPreferencesRepository userPreferencesRepository;

    public UserPreferencesQueryService(UserPreferencesRepository userPreferencesRepository) {
        this.userPreferencesRepository = userPreferencesRepository;
    }

    /**
     * Return a {@link List} of {@link UserPreferences} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserPreferences> findByCriteria(UserPreferencesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserPreferences> specification = createSpecification(criteria);
        return userPreferencesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UserPreferences} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserPreferences> findByCriteria(UserPreferencesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserPreferences> specification = createSpecification(criteria);
        return userPreferencesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserPreferencesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserPreferences> specification = createSpecification(criteria);
        return userPreferencesRepository.count(specification);
    }

    /**
     * Function to convert {@link UserPreferencesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserPreferences> createSpecification(UserPreferencesCriteria criteria) {
        Specification<UserPreferences> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserPreferences_.id));
            }
            if (criteria.getNotifications() != null) {
                specification = specification.and(buildSpecification(criteria.getNotifications(), UserPreferences_.notifications));
            }
        }
        return specification;
    }
}
