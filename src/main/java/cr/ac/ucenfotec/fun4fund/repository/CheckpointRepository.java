package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.Checkpoint;

import cr.ac.ucenfotec.fun4fund.domain.Review;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Checkpoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckpointRepository extends JpaRepository<Checkpoint, Long> {
    List<Checkpoint> findByProyectIdAndCompletitionPercentageLessThanEqual(Long proyectId, Double completitionPercentage);
}
