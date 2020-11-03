package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.DonationHistory;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the DonationHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonationHistoryRepository extends JpaRepository<DonationHistory, Long> {
}
