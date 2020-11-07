package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.ExclusiveContent;

import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ExclusiveContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExclusiveContentRepository extends JpaRepository<ExclusiveContent, Long> {
    List<ExclusiveContent> findExclusiveContentsByProyect(Proyect project);
}
