package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.Checkpoint;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Checkpoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckpointRepository extends JpaRepository<Checkpoint, Long>, JpaSpecificationExecutor<Checkpoint>, PagingAndSortingRepository<Checkpoint, Long> {
    List<Checkpoint> findByProyectIdAndCompletitionPercentageLessThanEqual(Long proyectId, Double completitionPercentage, Sort sort);
}
