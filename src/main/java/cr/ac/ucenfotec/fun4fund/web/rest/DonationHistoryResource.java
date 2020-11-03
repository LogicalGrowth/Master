package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.DonationHistory;
import cr.ac.ucenfotec.fun4fund.repository.DonationHistoryRepository;
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
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.DonationHistory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DonationHistoryResource {

    private final Logger log = LoggerFactory.getLogger(DonationHistoryResource.class);

    private static final String ENTITY_NAME = "donationHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DonationHistoryRepository donationHistoryRepository;

    public DonationHistoryResource(DonationHistoryRepository donationHistoryRepository) {
        this.donationHistoryRepository = donationHistoryRepository;
    }

    /**
     * {@code POST  /donation-histories} : Create a new donationHistory.
     *
     * @param donationHistory the donationHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new donationHistory, or with status {@code 400 (Bad Request)} if the donationHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/donation-histories")
    public ResponseEntity<DonationHistory> createDonationHistory(@Valid @RequestBody DonationHistory donationHistory) throws URISyntaxException {
        log.debug("REST request to save DonationHistory : {}", donationHistory);
        if (donationHistory.getId() != null) {
            throw new BadRequestAlertException("A new donationHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DonationHistory result = donationHistoryRepository.save(donationHistory);
        return ResponseEntity.created(new URI("/api/donation-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /donation-histories} : Updates an existing donationHistory.
     *
     * @param donationHistory the donationHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donationHistory,
     * or with status {@code 400 (Bad Request)} if the donationHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the donationHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/donation-histories")
    public ResponseEntity<DonationHistory> updateDonationHistory(@Valid @RequestBody DonationHistory donationHistory) throws URISyntaxException {
        log.debug("REST request to update DonationHistory : {}", donationHistory);
        if (donationHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DonationHistory result = donationHistoryRepository.save(donationHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donationHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /donation-histories} : get all the donationHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of donationHistories in body.
     */
    @GetMapping("/donation-histories")
    public List<DonationHistory> getAllDonationHistories() {
        log.debug("REST request to get all DonationHistories");
        return donationHistoryRepository.findAll();
    }

    /**
     * {@code GET  /donation-histories/:id} : get the "id" donationHistory.
     *
     * @param id the id of the donationHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donationHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/donation-histories/{id}")
    public ResponseEntity<DonationHistory> getDonationHistory(@PathVariable Long id) {
        log.debug("REST request to get DonationHistory : {}", id);
        Optional<DonationHistory> donationHistory = donationHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(donationHistory);
    }

    /**
     * {@code DELETE  /donation-histories/:id} : delete the "id" donationHistory.
     *
     * @param id the id of the donationHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/donation-histories/{id}")
    public ResponseEntity<Void> deleteDonationHistory(@PathVariable Long id) {
        log.debug("REST request to delete DonationHistory : {}", id);
        donationHistoryRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
