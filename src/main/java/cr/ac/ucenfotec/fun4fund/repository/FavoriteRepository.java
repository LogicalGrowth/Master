package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Favorite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>, JpaSpecificationExecutor<Favorite> {
    List<Favorite> findByProyect(Proyect proyect);
    @Query(nativeQuery = true, value =
        "Select proyect_id, count(1) as count " +
            "FROM Favorite " +
            "where proyect_id is not null " +
            "group by proyect_id " +
            "order by count desc " +
            "limit 5")
    List<ITopFavorites> getTopFavorites();

}
