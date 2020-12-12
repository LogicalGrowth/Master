package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.ConfigSystem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ConfigSystem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigSystemRepository extends JpaRepository<ConfigSystem, Long>, JpaSpecificationExecutor<ConfigSystem> {
    List<ConfigSystem> findByType(String type);
}
