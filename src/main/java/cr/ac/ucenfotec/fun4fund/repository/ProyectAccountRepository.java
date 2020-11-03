package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.ProyectAccount;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProyectAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProyectAccountRepository extends JpaRepository<ProyectAccount, Long> {
}
