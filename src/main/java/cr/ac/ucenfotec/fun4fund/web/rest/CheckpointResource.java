package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.Checkpoint;
import cr.ac.ucenfotec.fun4fund.repository.CheckpointRepository;
import cr.ac.ucenfotec.fun4fund.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.Checkpoint}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CheckpointResource {

    private final Logger log = LoggerFactory.getLogger(CheckpointResource.class);

    private static final String ENTITY_NAME = "checkpoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CheckpointRepository checkpointRepository;

    public CheckpointResource(CheckpointRepository checkpointRepository) {
        this.checkpointRepository = checkpointRepository;
    }

    /**
     * {@code POST  /checkpoints} : Create a new checkpoint.
     *
     * @param checkpoint the checkpoint to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new checkpoint, or with status {@code 400 (Bad Request)} if the checkpoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/checkpoints")
    public ResponseEntity<Checkpoint> createCheckpoint(@Valid @RequestBody Checkpoint checkpoint) throws URISyntaxException {
        log.debug("REST request to save Checkpoint : {}", checkpoint);
        if (checkpoint.getId() != null) {
            throw new BadRequestAlertException("A new checkpoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Checkpoint result = checkpointRepository.save(checkpoint);
        return ResponseEntity.created(new URI("/api/checkpoints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /checkpoints} : Updates an existing checkpoint.
     *
     * @param checkpoint the checkpoint to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated checkpoint,
     * or with status {@code 400 (Bad Request)} if the checkpoint is not valid,
     * or with status {@code 500 (Internal Server Error)} if the checkpoint couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/checkpoints")
    public ResponseEntity<Checkpoint> updateCheckpoint(@Valid @RequestBody Checkpoint checkpoint) throws URISyntaxException {
        log.debug("REST request to update Checkpoint : {}", checkpoint);
        if (checkpoint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Checkpoint result = checkpointRepository.save(checkpoint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, checkpoint.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /checkpoints} : get all the checkpoints.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of checkpoints in body.
     */
    @GetMapping("/checkpoints")
    public List<Checkpoint> getAllCheckpoints() {
        log.debug("REST request to get all Checkpoints");
        return checkpointRepository.findAll();
    }

    /**
     * {@code GET  /checkpoints/:id} : get the "id" checkpoint.
     *
     * @param id the id of the checkpoint to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the checkpoint, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/checkpoints/{id}")
    public ResponseEntity<Checkpoint> getCheckpoint(@PathVariable Long id) {
        log.debug("REST request to get Checkpoint : {}", id);
        Optional<Checkpoint> checkpoint = checkpointRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(checkpoint);
    }

    /**
     * {@code DELETE  /checkpoints/:id} : delete the "id" checkpoint.
     *
     * @param id the id of the checkpoint to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/checkpoints/{id}")
    public ResponseEntity<Void> deleteCheckpoint(@PathVariable Long id) {
        log.debug("REST request to delete Checkpoint : {}", id);
        checkpointRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/checkpoints/byproyect")
    public List<Checkpoint> getCheckpointByProyect(@RequestParam(name = "idproyect", required = true) String idproyect,
                                                   @RequestParam(name = "percentile", required = true) String percentile)
    {
        log.debug("REST request to get Checkpoint : {}", idproyect);
        Long id = Long.parseLong(idproyect);
        Double per = Double.parseDouble(percentile);
        return checkpointRepository.findByProyectIdAndCompletitionPercentageLessThanEqual(id,per);
    }
}
