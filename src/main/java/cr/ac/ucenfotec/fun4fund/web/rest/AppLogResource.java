package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.AppLog;
import cr.ac.ucenfotec.fun4fund.service.AppLogService;
import cr.ac.ucenfotec.fun4fund.web.rest.errors.BadRequestAlertException;
import cr.ac.ucenfotec.fun4fund.service.dto.AppLogCriteria;
import cr.ac.ucenfotec.fun4fund.service.AppLogQueryService;

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
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.AppLog}.
 */
@RestController
@RequestMapping("/api")
public class AppLogResource {

    private final Logger log = LoggerFactory.getLogger(AppLogResource.class);

    private static final String ENTITY_NAME = "appLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppLogService appLogService;

    private final AppLogQueryService appLogQueryService;

    public AppLogResource(AppLogService appLogService, AppLogQueryService appLogQueryService) {
        this.appLogService = appLogService;
        this.appLogQueryService = appLogQueryService;
    }

    /**
     * {@code POST  /app-logs} : Create a new appLog.
     *
     * @param appLog the appLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appLog, or with status {@code 400 (Bad Request)} if the appLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/app-logs")
    public ResponseEntity<AppLog> createAppLog(@Valid @RequestBody AppLog appLog) throws URISyntaxException {
        log.debug("REST request to save AppLog : {}", appLog);
        if (appLog.getId() != null) {
            throw new BadRequestAlertException("A new appLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppLog result = appLogService.save(appLog);
        return ResponseEntity.created(new URI("/api/app-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /app-logs} : Updates an existing appLog.
     *
     * @param appLog the appLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appLog,
     * or with status {@code 400 (Bad Request)} if the appLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/app-logs")
    public ResponseEntity<AppLog> updateAppLog(@Valid @RequestBody AppLog appLog) throws URISyntaxException {
        log.debug("REST request to update AppLog : {}", appLog);
        if (appLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AppLog result = appLogService.save(appLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appLog.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /app-logs} : get all the appLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appLogs in body.
     */
    @GetMapping("/app-logs")
    public ResponseEntity<List<AppLog>> getAllAppLogs(AppLogCriteria criteria) {
        log.debug("REST request to get AppLogs by criteria: {}", criteria);
        List<AppLog> entityList = appLogQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /app-logs/count} : count all the appLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/app-logs/count")
    public ResponseEntity<Long> countAppLogs(AppLogCriteria criteria) {
        log.debug("REST request to count AppLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(appLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /app-logs/:id} : get the "id" appLog.
     *
     * @param id the id of the appLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/app-logs/{id}")
    public ResponseEntity<AppLog> getAppLog(@PathVariable Long id) {
        log.debug("REST request to get AppLog : {}", id);
        Optional<AppLog> appLog = appLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appLog);
    }

    /**
     * {@code DELETE  /app-logs/:id} : delete the "id" appLog.
     *
     * @param id the id of the appLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/app-logs/{id}")
    public ResponseEntity<Void> deleteAppLog(@PathVariable Long id) {
        log.debug("REST request to delete AppLog : {}", id);
        appLogService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
