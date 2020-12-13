package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.domain.Checkpoint;
import cr.ac.ucenfotec.fun4fund.domain.IProyectAnswerStatistics;
import cr.ac.ucenfotec.fun4fund.domain.Proyect;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Proyect entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProyectRepository extends JpaRepository<Proyect, Long>, JpaSpecificationExecutor<Proyect> {
    @Query(nativeQuery = true, value =
        "SELECT status, count(1) as count FROM fun4fund.proyect " +
        "where owner_id = ?1 " +
        "group by status;")
    List<IProyectAnswerStatistics> getReportsProyectsStatus(ApplicationUser ownner);
}
