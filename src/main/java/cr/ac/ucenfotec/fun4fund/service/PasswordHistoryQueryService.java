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

import cr.ac.ucenfotec.fun4fund.domain.PasswordHistory;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.PasswordHistoryRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.PasswordHistoryCriteria;

/**
 * Service for executing complex queries for {@link PasswordHistory} entities in the database.
 * The main input is a {@link PasswordHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PasswordHistory} or a {@link Page} of {@link PasswordHistory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PasswordHistoryQueryService extends QueryService<PasswordHistory> {

    private final Logger log = LoggerFactory.getLogger(PasswordHistoryQueryService.class);

    private final PasswordHistoryRepository passwordHistoryRepository;

    public PasswordHistoryQueryService(PasswordHistoryRepository passwordHistoryRepository) {
        this.passwordHistoryRepository = passwordHistoryRepository;
    }

    /**
     * Return a {@link List} of {@link PasswordHistory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PasswordHistory> findByCriteria(PasswordHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PasswordHistory> specification = createSpecification(criteria);
        return passwordHistoryRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PasswordHistory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PasswordHistory> findByCriteria(PasswordHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PasswordHistory> specification = createSpecification(criteria);
        return passwordHistoryRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PasswordHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PasswordHistory> specification = createSpecification(criteria);
        return passwordHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link PasswordHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PasswordHistory> createSpecification(PasswordHistoryCriteria criteria) {
        Specification<PasswordHistory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PasswordHistory_.id));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), PasswordHistory_.password));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), PasswordHistory_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), PasswordHistory_.endDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), PasswordHistory_.status));
            }
        }
        return specification;
    }
}
