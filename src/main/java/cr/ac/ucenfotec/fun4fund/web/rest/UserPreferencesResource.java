package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.UserPreferences;
import cr.ac.ucenfotec.fun4fund.service.UserPreferencesService;
import cr.ac.ucenfotec.fun4fund.web.rest.errors.BadRequestAlertException;
import cr.ac.ucenfotec.fun4fund.service.dto.UserPreferencesCriteria;
import cr.ac.ucenfotec.fun4fund.service.UserPreferencesQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.UserPreferences}.
 */
@RestController
@RequestMapping("/api")
public class UserPreferencesResource {

    private final Logger log = LoggerFactory.getLogger(UserPreferencesResource.class);

    private static final String ENTITY_NAME = "userPreferences";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserPreferencesService userPreferencesService;

    private final UserPreferencesQueryService userPreferencesQueryService;

    public UserPreferencesResource(UserPreferencesService userPreferencesService, UserPreferencesQueryService userPreferencesQueryService) {
        this.userPreferencesService = userPreferencesService;
        this.userPreferencesQueryService = userPreferencesQueryService;
    }

    /**
     * {@code POST  /user-preferences} : Create a new userPreferences.
     *
     * @param userPreferences the userPreferences to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userPreferences, or with status {@code 400 (Bad Request)} if the userPreferences has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-preferences")
    public ResponseEntity<UserPreferences> createUserPreferences(@Valid @RequestBody UserPreferences userPreferences) throws URISyntaxException {
        log.debug("REST request to save UserPreferences : {}", userPreferences);
        if (userPreferences.getId() != null) {
            throw new BadRequestAlertException("A new userPreferences cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserPreferences result = userPreferencesService.save(userPreferences);
        return ResponseEntity.created(new URI("/api/user-preferences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-preferences} : Updates an existing userPreferences.
     *
     * @param userPreferences the userPreferences to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPreferences,
     * or with status {@code 400 (Bad Request)} if the userPreferences is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userPreferences couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-preferences")
    public ResponseEntity<UserPreferences> updateUserPreferences(@Valid @RequestBody UserPreferences userPreferences) throws URISyntaxException {
        log.debug("REST request to update UserPreferences : {}", userPreferences);
        if (userPreferences.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserPreferences result = userPreferencesService.save(userPreferences);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPreferences.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-preferences} : get all the userPreferences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userPreferences in body.
     */
    @GetMapping("/user-preferences")
    public ResponseEntity<List<UserPreferences>> getAllUserPreferences(UserPreferencesCriteria criteria) {
        log.debug("REST request to get UserPreferences by criteria: {}", criteria);
        List<UserPreferences> entityList = userPreferencesQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /user-preferences/count} : count all the userPreferences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/user-preferences/count")
    public ResponseEntity<Long> countUserPreferences(UserPreferencesCriteria criteria) {
        log.debug("REST request to count UserPreferences by criteria: {}", criteria);
        return ResponseEntity.ok().body(userPreferencesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-preferences/:id} : get the "id" userPreferences.
     *
     * @param id the id of the userPreferences to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userPreferences, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-preferences/{id}")
    public ResponseEntity<UserPreferences> getUserPreferences(@PathVariable Long id) {
        log.debug("REST request to get UserPreferences : {}", id);
        Optional<UserPreferences> userPreferences = userPreferencesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userPreferences);
    }

    /**
     * {@code DELETE  /user-preferences/:id} : delete the "id" userPreferences.
     *
     * @param id the id of the userPreferences to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-preferences/{id}")
    public ResponseEntity<Void> deleteUserPreferences(@PathVariable Long id) {
        log.debug("REST request to delete UserPreferences : {}", id);
        userPreferencesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
