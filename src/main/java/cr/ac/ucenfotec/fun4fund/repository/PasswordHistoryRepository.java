package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.PasswordHistory;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PasswordHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
}
