package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.Recommendation;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Recommendation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
