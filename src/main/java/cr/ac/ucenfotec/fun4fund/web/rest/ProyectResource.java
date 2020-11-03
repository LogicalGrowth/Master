package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.repository.ProyectRepository;
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
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.Proyect}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProyectResource {

    private final Logger log = LoggerFactory.getLogger(ProyectResource.class);

    private static final String ENTITY_NAME = "proyect";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProyectRepository proyectRepository;

    public ProyectResource(ProyectRepository proyectRepository) {
        this.proyectRepository = proyectRepository;
    }

    /**
     * {@code POST  /proyects} : Create a new proyect.
     *
     * @param proyect the proyect to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proyect, or with status {@code 400 (Bad Request)} if the proyect has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/proyects")
    public ResponseEntity<Proyect> createProyect(@Valid @RequestBody Proyect proyect) throws URISyntaxException {
        log.debug("REST request to save Proyect : {}", proyect);
        if (proyect.getId() != null) {
            throw new BadRequestAlertException("A new proyect cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Proyect result = proyectRepository.save(proyect);
        return ResponseEntity.created(new URI("/api/proyects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /proyects} : Updates an existing proyect.
     *
     * @param proyect the proyect to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proyect,
     * or with status {@code 400 (Bad Request)} if the proyect is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proyect couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/proyects")
    public ResponseEntity<Proyect> updateProyect(@Valid @RequestBody Proyect proyect) throws URISyntaxException {
        log.debug("REST request to update Proyect : {}", proyect);
        if (proyect.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Proyect result = proyectRepository.save(proyect);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proyect.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /proyects} : get all the proyects.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proyects in body.
     */
    @GetMapping("/proyects")
    public List<Proyect> getAllProyects() {
        log.debug("REST request to get all Proyects");
        return proyectRepository.findAll();
    }

    /**
     * {@code GET  /proyects/:id} : get the "id" proyect.
     *
     * @param id the id of the proyect to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proyect, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/proyects/{id}")
    public ResponseEntity<Proyect> getProyect(@PathVariable Long id) {
        log.debug("REST request to get Proyect : {}", id);
        Optional<Proyect> proyect = proyectRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(proyect);
    }

    /**
     * {@code DELETE  /proyects/:id} : delete the "id" proyect.
     *
     * @param id the id of the proyect to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/proyects/{id}")
    public ResponseEntity<Void> deleteProyect(@PathVariable Long id) {
        log.debug("REST request to delete Proyect : {}", id);
        proyectRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
