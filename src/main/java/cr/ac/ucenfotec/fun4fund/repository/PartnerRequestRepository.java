package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.PartnerRequest;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PartnerRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartnerRequestRepository extends JpaRepository<PartnerRequest, Long>, JpaSpecificationExecutor<PartnerRequest> {
}
