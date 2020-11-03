package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.ProyectAccount;
import cr.ac.ucenfotec.fun4fund.repository.ProyectAccountRepository;
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
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.ProyectAccount}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProyectAccountResource {

    private final Logger log = LoggerFactory.getLogger(ProyectAccountResource.class);

    private static final String ENTITY_NAME = "proyectAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProyectAccountRepository proyectAccountRepository;

    public ProyectAccountResource(ProyectAccountRepository proyectAccountRepository) {
        this.proyectAccountRepository = proyectAccountRepository;
    }

    /**
     * {@code POST  /proyect-accounts} : Create a new proyectAccount.
     *
     * @param proyectAccount the proyectAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proyectAccount, or with status {@code 400 (Bad Request)} if the proyectAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/proyect-accounts")
    public ResponseEntity<ProyectAccount> createProyectAccount(@Valid @RequestBody ProyectAccount proyectAccount) throws URISyntaxException {
        log.debug("REST request to save ProyectAccount : {}", proyectAccount);
        if (proyectAccount.getId() != null) {
            throw new BadRequestAlertException("A new proyectAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProyectAccount result = proyectAccountRepository.save(proyectAccount);
        return ResponseEntity.created(new URI("/api/proyect-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /proyect-accounts} : Updates an existing proyectAccount.
     *
     * @param proyectAccount the proyectAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proyectAccount,
     * or with status {@code 400 (Bad Request)} if the proyectAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proyectAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/proyect-accounts")
    public ResponseEntity<ProyectAccount> updateProyectAccount(@Valid @RequestBody ProyectAccount proyectAccount) throws URISyntaxException {
        log.debug("REST request to update ProyectAccount : {}", proyectAccount);
        if (proyectAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProyectAccount result = proyectAccountRepository.save(proyectAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proyectAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /proyect-accounts} : get all the proyectAccounts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proyectAccounts in body.
     */
    @GetMapping("/proyect-accounts")
    public List<ProyectAccount> getAllProyectAccounts() {
        log.debug("REST request to get all ProyectAccounts");
        return proyectAccountRepository.findAll();
    }

    /**
     * {@code GET  /proyect-accounts/:id} : get the "id" proyectAccount.
     *
     * @param id the id of the proyectAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proyectAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/proyect-accounts/{id}")
    public ResponseEntity<ProyectAccount> getProyectAccount(@PathVariable Long id) {
        log.debug("REST request to get ProyectAccount : {}", id);
        Optional<ProyectAccount> proyectAccount = proyectAccountRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(proyectAccount);
    }

    /**
     * {@code DELETE  /proyect-accounts/:id} : delete the "id" proyectAccount.
     *
     * @param id the id of the proyectAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/proyect-accounts/{id}")
    public ResponseEntity<Void> deleteProyectAccount(@PathVariable Long id) {
        log.debug("REST request to delete ProyectAccount : {}", id);
        proyectAccountRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
