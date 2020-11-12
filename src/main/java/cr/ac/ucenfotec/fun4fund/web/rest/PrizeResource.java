package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.Prize;
import cr.ac.ucenfotec.fun4fund.service.PrizeService;
import cr.ac.ucenfotec.fun4fund.web.rest.errors.BadRequestAlertException;
import cr.ac.ucenfotec.fun4fund.service.dto.PrizeCriteria;
import cr.ac.ucenfotec.fun4fund.service.PrizeQueryService;

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
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.Prize}.
 */
@RestController
@RequestMapping("/api")
public class PrizeResource {

    private final Logger log = LoggerFactory.getLogger(PrizeResource.class);

    private static final String ENTITY_NAME = "prize";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrizeService prizeService;

    private final PrizeQueryService prizeQueryService;

    public PrizeResource(PrizeService prizeService, PrizeQueryService prizeQueryService) {
        this.prizeService = prizeService;
        this.prizeQueryService = prizeQueryService;
    }

    /**
     * {@code POST  /prizes} : Create a new prize.
     *
     * @param prize the prize to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prize, or with status {@code 400 (Bad Request)} if the prize has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prizes")
    public ResponseEntity<Prize> createPrize(@Valid @RequestBody Prize prize) throws URISyntaxException {
        log.debug("REST request to save Prize : {}", prize);
        if (prize.getId() != null) {
            throw new BadRequestAlertException("A new prize cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prize result = prizeService.save(prize);
        return ResponseEntity.created(new URI("/api/prizes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prizes} : Updates an existing prize.
     *
     * @param prize the prize to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prize,
     * or with status {@code 400 (Bad Request)} if the prize is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prize couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prizes")
    public ResponseEntity<Prize> updatePrize(@Valid @RequestBody Prize prize) throws URISyntaxException {
        log.debug("REST request to update Prize : {}", prize);
        if (prize.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Prize result = prizeService.save(prize);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prize.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /prizes} : get all the prizes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prizes in body.
     */
    @GetMapping("/prizes")
    public ResponseEntity<List<Prize>> getAllPrizes(PrizeCriteria criteria) {
        log.debug("REST request to get Prizes by criteria: {}", criteria);
        List<Prize> entityList = prizeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /prizes/count} : count all the prizes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prizes/count")
    public ResponseEntity<Long> countPrizes(PrizeCriteria criteria) {
        log.debug("REST request to count Prizes by criteria: {}", criteria);
        return ResponseEntity.ok().body(prizeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prizes/:id} : get the "id" prize.
     *
     * @param id the id of the prize to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prize, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prizes/{id}")
    public ResponseEntity<Prize> getPrize(@PathVariable Long id) {
        log.debug("REST request to get Prize : {}", id);
        Optional<Prize> prize = prizeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prize);
    }

    /**
     * {@code DELETE  /prizes/:id} : delete the "id" prize.
     *
     * @param id the id of the prize to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prizes/{id}")
    public ResponseEntity<Void> deletePrize(@PathVariable Long id) {
        log.debug("REST request to delete Prize : {}", id);
        prizeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
