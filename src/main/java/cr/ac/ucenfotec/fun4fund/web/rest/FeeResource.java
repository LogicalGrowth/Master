package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.Fee;
import cr.ac.ucenfotec.fun4fund.service.FeeService;
import cr.ac.ucenfotec.fun4fund.web.rest.errors.BadRequestAlertException;
import cr.ac.ucenfotec.fun4fund.service.dto.FeeCriteria;
import cr.ac.ucenfotec.fun4fund.service.FeeQueryService;

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
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.Fee}.
 */
@RestController
@RequestMapping("/api")
public class FeeResource {

    private final Logger log = LoggerFactory.getLogger(FeeResource.class);

    private static final String ENTITY_NAME = "fee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeeService feeService;

    private final FeeQueryService feeQueryService;

    public FeeResource(FeeService feeService, FeeQueryService feeQueryService) {
        this.feeService = feeService;
        this.feeQueryService = feeQueryService;
    }

    /**
     * {@code POST  /fees} : Create a new fee.
     *
     * @param fee the fee to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fee, or with status {@code 400 (Bad Request)} if the fee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fees")
    public ResponseEntity<Fee> createFee(@Valid @RequestBody Fee fee) throws URISyntaxException {
        log.debug("REST request to save Fee : {}", fee);
        if (fee.getId() != null) {
            throw new BadRequestAlertException("A new fee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fee result = feeService.save(fee);
        return ResponseEntity.created(new URI("/api/fees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fees} : Updates an existing fee.
     *
     * @param fee the fee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fee,
     * or with status {@code 400 (Bad Request)} if the fee is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fees")
    public ResponseEntity<Fee> updateFee(@Valid @RequestBody Fee fee) throws URISyntaxException {
        log.debug("REST request to update Fee : {}", fee);
        if (fee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Fee result = feeService.save(fee);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fee.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fees} : get all the fees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fees in body.
     */
    @GetMapping("/fees")
    public ResponseEntity<List<Fee>> getAllFees(FeeCriteria criteria) {
        log.debug("REST request to get Fees by criteria: {}", criteria);
        List<Fee> entityList = feeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /fees/count} : count all the fees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fees/count")
    public ResponseEntity<Long> countFees(FeeCriteria criteria) {
        log.debug("REST request to count Fees by criteria: {}", criteria);
        return ResponseEntity.ok().body(feeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fees/:id} : get the "id" fee.
     *
     * @param id the id of the fee to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fee, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fees/{id}")
    public ResponseEntity<Fee> getFee(@PathVariable Long id) {
        log.debug("REST request to get Fee : {}", id);
        Optional<Fee> fee = feeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fee);
    }

    /**
     * {@code DELETE  /fees/:id} : delete the "id" fee.
     *
     * @param id the id of the fee to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fees/{id}")
    public ResponseEntity<Void> deleteFee(@PathVariable Long id) {
        log.debug("REST request to delete Fee : {}", id);
        feeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
