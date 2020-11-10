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

import cr.ac.ucenfotec.fun4fund.domain.PartnerRequest;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.PartnerRequestRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.PartnerRequestCriteria;

/**
 * Service for executing complex queries for {@link PartnerRequest} entities in the database.
 * The main input is a {@link PartnerRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PartnerRequest} or a {@link Page} of {@link PartnerRequest} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartnerRequestQueryService extends QueryService<PartnerRequest> {

    private final Logger log = LoggerFactory.getLogger(PartnerRequestQueryService.class);

    private final PartnerRequestRepository partnerRequestRepository;

    public PartnerRequestQueryService(PartnerRequestRepository partnerRequestRepository) {
        this.partnerRequestRepository = partnerRequestRepository;
    }

    /**
     * Return a {@link List} of {@link PartnerRequest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PartnerRequest> findByCriteria(PartnerRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PartnerRequest> specification = createSpecification(criteria);
        return partnerRequestRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PartnerRequest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PartnerRequest> findByCriteria(PartnerRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PartnerRequest> specification = createSpecification(criteria);
        return partnerRequestRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartnerRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PartnerRequest> specification = createSpecification(criteria);
        return partnerRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link PartnerRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartnerRequest> createSpecification(PartnerRequestCriteria criteria) {
        Specification<PartnerRequest> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PartnerRequest_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), PartnerRequest_.amount));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), PartnerRequest_.status));
            }
            if (criteria.getApplicantId() != null) {
                specification = specification.and(buildSpecification(criteria.getApplicantId(),
                    root -> root.join(PartnerRequest_.applicant, JoinType.LEFT).get(ApplicationUser_.id)));
            }
            if (criteria.getProyectId() != null) {
                specification = specification.and(buildSpecification(criteria.getProyectId(),
                    root -> root.join(PartnerRequest_.proyect, JoinType.LEFT).get(Proyect_.id)));
            }
        }
        return specification;
    }
}
