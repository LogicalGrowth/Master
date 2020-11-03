package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.AppLog;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AppLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppLogRepository extends JpaRepository<AppLog, Long> {
}
