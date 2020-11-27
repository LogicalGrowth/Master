package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.ConfigSystem;
import cr.ac.ucenfotec.fun4fund.service.ConfigSystemService;
import cr.ac.ucenfotec.fun4fund.web.rest.errors.BadRequestAlertException;
import cr.ac.ucenfotec.fun4fund.service.dto.ConfigSystemCriteria;
import cr.ac.ucenfotec.fun4fund.service.ConfigSystemQueryService;

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
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.ConfigSystem}.
 */
@RestController
@RequestMapping("/api")
public class ConfigSystemResource {

    private final Logger log = LoggerFactory.getLogger(ConfigSystemResource.class);

    private static final String ENTITY_NAME = "configSystem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigSystemService configSystemService;

    private final ConfigSystemQueryService configSystemQueryService;

    public ConfigSystemResource(ConfigSystemService configSystemService, ConfigSystemQueryService configSystemQueryService) {
        this.configSystemService = configSystemService;
        this.configSystemQueryService = configSystemQueryService;
    }

    /**
     * {@code POST  /config-systems} : Create a new configSystem.
     *
     * @param configSystem the configSystem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configSystem, or with status {@code 400 (Bad Request)} if the configSystem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-systems")
    public ResponseEntity<ConfigSystem> createConfigSystem(@Valid @RequestBody ConfigSystem configSystem) throws URISyntaxException {
        log.debug("REST request to save ConfigSystem : {}", configSystem);
        if (configSystem.getId() != null) {
            throw new BadRequestAlertException("A new configSystem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigSystem result = configSystemService.save(configSystem);
        return ResponseEntity.created(new URI("/api/config-systems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-systems} : Updates an existing configSystem.
     *
     * @param configSystem the configSystem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configSystem,
     * or with status {@code 400 (Bad Request)} if the configSystem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configSystem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-systems")
    public ResponseEntity<ConfigSystem> updateConfigSystem(@Valid @RequestBody ConfigSystem configSystem) throws URISyntaxException {
        log.debug("REST request to update ConfigSystem : {}", configSystem);
        if (configSystem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigSystem result = configSystemService.save(configSystem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configSystem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-systems} : get all the configSystems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configSystems in body.
     */
    @GetMapping("/config-systems")
    public ResponseEntity<List<ConfigSystem>> getAllConfigSystems(ConfigSystemCriteria criteria) {
        log.debug("REST request to get ConfigSystems by criteria: {}", criteria);
        List<ConfigSystem> entityList = configSystemQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /config-systems/count} : count all the configSystems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/config-systems/count")
    public ResponseEntity<Long> countConfigSystems(ConfigSystemCriteria criteria) {
        log.debug("REST request to count ConfigSystems by criteria: {}", criteria);
        return ResponseEntity.ok().body(configSystemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /config-systems/:id} : get the "id" configSystem.
     *
     * @param id the id of the configSystem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configSystem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-systems/{id}")
    public ResponseEntity<ConfigSystem> getConfigSystem(@PathVariable Long id) {
        log.debug("REST request to get ConfigSystem : {}", id);
        Optional<ConfigSystem> configSystem = configSystemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configSystem);
    }

    /**
     * {@code DELETE  /config-systems/:id} : delete the "id" configSystem.
     *
     * @param id the id of the configSystem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-systems/{id}")
    public ResponseEntity<Void> deleteConfigSystem(@PathVariable Long id) {
        log.debug("REST request to delete ConfigSystem : {}", id);
        configSystemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
