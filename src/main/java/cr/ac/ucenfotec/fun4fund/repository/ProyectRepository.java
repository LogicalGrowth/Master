package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.*;

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
        "SELECT status, count(1) as count " +
        "FROM Proyect " +
        "where owner_id = ?1 " +
        "group by status")
    List<IProyectAnswerStatistics> getReportsProyectsStatus(ApplicationUser owner);

    @Query(nativeQuery = true, value =
        "SELECT name, (collected*100/goal_amount) as complete " +
        "FROM Proyect " +
        "where owner_id = ?1 " +
        "order by complete")
    List<IProyectCompletedPercentile> getReportsProyectsComplete(ApplicationUser owner);

    @Query(nativeQuery = true, value =
        "SELECT c.name, count(1) as count " +
        "FROM Proyect as p " +
        "LEFT JOIN Category as c " +
        "ON p.category_id = c.id " +
        "Where p.owner_id = ?1 " +
        "GROUP BY category_id")
    List<IProyectCategoryStatistics> getReportsProyectsCategory(ApplicationUser owner);

    @Query(nativeQuery = true, value =
        "SELECT c.name, count(1) as count " +
        "FROM Proyect as p " +
        "LEFT JOIN Category as c " +
        "ON p.category_id = c.id " +
        "GROUP BY category_id")
    List<IProyectCategoryStatistics> getAllReportsProyectsCategory();
}
