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

import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.ApplicationUserRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.ApplicationUserCriteria;

/**
 * Service for executing complex queries for {@link ApplicationUser} entities in the database.
 * The main input is a {@link ApplicationUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ApplicationUser} or a {@link Page} of {@link ApplicationUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ApplicationUserQueryService extends QueryService<ApplicationUser> {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserQueryService.class);

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserQueryService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    /**
     * Return a {@link List} of {@link ApplicationUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ApplicationUser> findByCriteria(ApplicationUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ApplicationUser> specification = createSpecification(criteria);
        return applicationUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ApplicationUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ApplicationUser> findByCriteria(ApplicationUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ApplicationUser> specification = createSpecification(criteria);
        return applicationUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ApplicationUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ApplicationUser> specification = createSpecification(criteria);
        return applicationUserRepository.count(specification);
    }

    /**
     * Function to convert {@link ApplicationUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ApplicationUser> createSpecification(ApplicationUserCriteria criteria) {
        Specification<ApplicationUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ApplicationUser_.id));
            }
            if (criteria.getIdentification() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdentification(), ApplicationUser_.identification));
            }
            if (criteria.getIdType() != null) {
                specification = specification.and(buildSpecification(criteria.getIdType(), ApplicationUser_.idType));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDate(), ApplicationUser_.birthDate));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), ApplicationUser_.phoneNumber));
            }
            if (criteria.getAdmin() != null) {
                specification = specification.and(buildSpecification(criteria.getAdmin(), ApplicationUser_.admin));
            }
            if (criteria.getInternalUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getInternalUserId(),
                    root -> root.join(ApplicationUser_.internalUser, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getPaymentMethodId() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethodId(),
                    root -> root.join(ApplicationUser_.paymentMethods, JoinType.LEFT).get(PaymentMethod_.id)));
            }
            if (criteria.getProyectId() != null) {
                specification = specification.and(buildSpecification(criteria.getProyectId(),
                    root -> root.join(ApplicationUser_.proyects, JoinType.LEFT).get(Proyect_.id)));
            }
            if (criteria.getNotificationId() != null) {
                specification = specification.and(buildSpecification(criteria.getNotificationId(),
                    root -> root.join(ApplicationUser_.notifications, JoinType.LEFT).get(Notification_.id)));
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentId(),
                    root -> root.join(ApplicationUser_.payments, JoinType.LEFT).get(Payment_.id)));
            }
            if (criteria.getAuctionId() != null) {
                specification = specification.and(buildSpecification(criteria.getAuctionId(),
                    root -> root.join(ApplicationUser_.auctions, JoinType.LEFT).get(Auction_.id)));
            }
            if (criteria.getPartnerRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getPartnerRequestId(),
                    root -> root.join(ApplicationUser_.partnerRequests, JoinType.LEFT).get(PartnerRequest_.id)));
            }
            if (criteria.getTicketId() != null) {
                specification = specification.and(buildSpecification(criteria.getTicketId(),
                    root -> root.join(ApplicationUser_.tickets, JoinType.LEFT).get(Ticket_.id)));
            }
            if (criteria.getFavoriteId() != null) {
                specification = specification.and(buildSpecification(criteria.getFavoriteId(),
                    root -> root.join(ApplicationUser_.favorites, JoinType.LEFT).get(Favorite_.id)));
            }
        }
        return specification;
    }
}
