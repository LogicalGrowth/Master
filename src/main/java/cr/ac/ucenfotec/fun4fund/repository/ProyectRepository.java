package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.Proyect;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Proyect entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProyectRepository extends JpaRepository<Proyect, Long>, JpaSpecificationExecutor<Proyect> {
}
