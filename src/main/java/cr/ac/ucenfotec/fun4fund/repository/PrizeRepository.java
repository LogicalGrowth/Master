package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.Prize;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Prize entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrizeRepository extends JpaRepository<Prize, Long> {
}
