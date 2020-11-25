package cr.ac.ucenfotec.fun4fund.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import cr.ac.ucenfotec.fun4fund.domain.Checkpoint;
import cr.ac.ucenfotec.fun4fund.domain.*; // for static metamodels
import cr.ac.ucenfotec.fun4fund.repository.CheckpointRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.CheckpointCriteria;

/**
 * Service for executing complex queries for {@link Checkpoint} entities in the database.
 * The main input is a {@link CheckpointCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Checkpoint} or a {@link Page} of {@link Checkpoint} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CheckpointQueryService extends QueryService<Checkpoint> {

    private final Logger log = LoggerFactory.getLogger(CheckpointQueryService.class);

    private final CheckpointRepository checkpointRepository;

    public CheckpointQueryService(CheckpointRepository checkpointRepository) {
        this.checkpointRepository = checkpointRepository;
    }

    /**
     * Return a {@link List} of {@link Checkpoint} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Checkpoint> findByCriteria(CheckpointCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Checkpoint> specification = createSpecification(criteria);
        return checkpointRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Checkpoint} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Checkpoint> findByCriteria(CheckpointCriteria criteria, Pageable page, Sort sort) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Checkpoint> specification = createSpecification(criteria);
        return checkpointRepository.findAll(specification, page);
    }

    @Transactional(readOnly = true)
    public List<Checkpoint> findByCriteria(CheckpointCriteria criteria, Sort sort) {
        log.debug("find by criteria : {}, page: {}", criteria, sort);
        final Specification<Checkpoint> specification = createSpecification(criteria);
        return checkpointRepository.findAll(specification, sort);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CheckpointCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Checkpoint> specification = createSpecification(criteria);
        return checkpointRepository.count(specification);
    }

    /**
     * Function to convert {@link CheckpointCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Checkpoint> createSpecification(CheckpointCriteria criteria) {
        Specification<Checkpoint> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Checkpoint_.id));
            }
            if (criteria.getCompletitionPercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompletitionPercentage(), Checkpoint_.completitionPercentage));
            }
            if (criteria.getMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessage(), Checkpoint_.message));
            }
            if (criteria.getCompleted() != null) {
                specification = specification.and(buildSpecification(criteria.getCompleted(), Checkpoint_.completed));
            }
            if (criteria.getProyectId() != null) {
                specification = specification.and(buildSpecification(criteria.getProyectId(),
                    root -> root.join(Checkpoint_.proyect, JoinType.LEFT).get(Proyect_.id)));
            }
        }
        return specification;
    }
}
