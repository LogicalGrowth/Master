package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.PasswordHistory;
import cr.ac.ucenfotec.fun4fund.service.PasswordHistoryService;
import cr.ac.ucenfotec.fun4fund.web.rest.errors.BadRequestAlertException;
import cr.ac.ucenfotec.fun4fund.service.dto.PasswordHistoryCriteria;
import cr.ac.ucenfotec.fun4fund.service.PasswordHistoryQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.PasswordHistory}.
 */
@RestController
@RequestMapping("/api")
public class PasswordHistoryResource {

    private final Logger log = LoggerFactory.getLogger(PasswordHistoryResource.class);

    private static final String ENTITY_NAME = "passwordHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PasswordHistoryService passwordHistoryService;

    private final PasswordHistoryQueryService passwordHistoryQueryService;

    public PasswordHistoryResource(PasswordHistoryService passwordHistoryService, PasswordHistoryQueryService passwordHistoryQueryService) {
        this.passwordHistoryService = passwordHistoryService;
        this.passwordHistoryQueryService = passwordHistoryQueryService;
    }

    /**
     * {@code POST  /password-histories} : Create a new passwordHistory.
     *
     * @param passwordHistory the passwordHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new passwordHistory, or with status {@code 400 (Bad Request)} if the passwordHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/password-histories")
    public ResponseEntity<PasswordHistory> createPasswordHistory(@RequestBody PasswordHistory passwordHistory) throws URISyntaxException {
        log.debug("REST request to save PasswordHistory : {}", passwordHistory);
        if (passwordHistory.getId() != null) {
            throw new BadRequestAlertException("A new passwordHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PasswordHistory result = passwordHistoryService.save(passwordHistory);
        return ResponseEntity.created(new URI("/api/password-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /password-histories} : Updates an existing passwordHistory.
     *
     * @param passwordHistory the passwordHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passwordHistory,
     * or with status {@code 400 (Bad Request)} if the passwordHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the passwordHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/password-histories")
    public ResponseEntity<PasswordHistory> updatePasswordHistory(@RequestBody PasswordHistory passwordHistory) throws URISyntaxException {
        log.debug("REST request to update PasswordHistory : {}", passwordHistory);
        if (passwordHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PasswordHistory result = passwordHistoryService.save(passwordHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, passwordHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /password-histories} : get all the passwordHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of passwordHistories in body.
     */
    @GetMapping("/password-histories")
    public ResponseEntity<List<PasswordHistory>> getAllPasswordHistories(PasswordHistoryCriteria criteria) {
        log.debug("REST request to get PasswordHistories by criteria: {}", criteria);
        List<PasswordHistory> entityList = passwordHistoryQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /password-histories/count} : count all the passwordHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/password-histories/count")
    public ResponseEntity<Long> countPasswordHistories(PasswordHistoryCriteria criteria) {
        log.debug("REST request to count PasswordHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(passwordHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /password-histories/:id} : get the "id" passwordHistory.
     *
     * @param id the id of the passwordHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the passwordHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/password-histories/{id}")
    public ResponseEntity<PasswordHistory> getPasswordHistory(@PathVariable Long id) {
        log.debug("REST request to get PasswordHistory : {}", id);
        Optional<PasswordHistory> passwordHistory = passwordHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(passwordHistory);
    }

    /**
     * {@code DELETE  /password-histories/:id} : delete the "id" passwordHistory.
     *
     * @param id the id of the passwordHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/password-histories/{id}")
    public ResponseEntity<Void> deletePasswordHistory(@PathVariable Long id) {
        log.debug("REST request to delete PasswordHistory : {}", id);
        passwordHistoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
