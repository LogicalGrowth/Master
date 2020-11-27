package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.Checkpoint;
import cr.ac.ucenfotec.fun4fund.repository.CheckpointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Checkpoint}.
 */
@Service
@Transactional
public class CheckpointService {

    private final Logger log = LoggerFactory.getLogger(CheckpointService.class);

    private final CheckpointRepository checkpointRepository;
    private final ProyectService proyectService;

    public CheckpointService(CheckpointRepository checkpointRepository,
                             ProyectService proyectService) {
        this.checkpointRepository = checkpointRepository;
        this.proyectService = proyectService;
    }

    /**
     * Save a checkpoint.
     *
     * @param checkpoint the entity to save.
     * @return the persisted entity.
     */
    public Checkpoint save(Checkpoint checkpoint) {
        log.debug("Request to save Checkpoint : {}", checkpoint);
        proyectService.updateDate(checkpoint.getProyect());
        return checkpointRepository.save(checkpoint);
    }

    /**
     * Get all the checkpoints.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Checkpoint> findAll() {
        log.debug("Request to get all Checkpoints");
        return checkpointRepository.findAll();
    }


    /**
     * Get one checkpoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Checkpoint> findOne(Long id) {
        log.debug("Request to get Checkpoint : {}", id);
        return checkpointRepository.findById(id);
    }

    /**
     * Delete the checkpoint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Checkpoint : {}", id);
        checkpointRepository.deleteById(id);
    }
}
