package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.PartnerRequest;
import cr.ac.ucenfotec.fun4fund.repository.PartnerRequestRepository;
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
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.PartnerRequest}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PartnerRequestResource {

    private final Logger log = LoggerFactory.getLogger(PartnerRequestResource.class);

    private static final String ENTITY_NAME = "partnerRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartnerRequestRepository partnerRequestRepository;

    public PartnerRequestResource(PartnerRequestRepository partnerRequestRepository) {
        this.partnerRequestRepository = partnerRequestRepository;
    }

    /**
     * {@code POST  /partner-requests} : Create a new partnerRequest.
     *
     * @param partnerRequest the partnerRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partnerRequest, or with status {@code 400 (Bad Request)} if the partnerRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/partner-requests")
    public ResponseEntity<PartnerRequest> createPartnerRequest(@Valid @RequestBody PartnerRequest partnerRequest) throws URISyntaxException {
        log.debug("REST request to save PartnerRequest : {}", partnerRequest);
        if (partnerRequest.getId() != null) {
            throw new BadRequestAlertException("A new partnerRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PartnerRequest result = partnerRequestRepository.save(partnerRequest);
        return ResponseEntity.created(new URI("/api/partner-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /partner-requests} : Updates an existing partnerRequest.
     *
     * @param partnerRequest the partnerRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partnerRequest,
     * or with status {@code 400 (Bad Request)} if the partnerRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partnerRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/partner-requests")
    public ResponseEntity<PartnerRequest> updatePartnerRequest(@Valid @RequestBody PartnerRequest partnerRequest) throws URISyntaxException {
        log.debug("REST request to update PartnerRequest : {}", partnerRequest);
        if (partnerRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PartnerRequest result = partnerRequestRepository.save(partnerRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partnerRequest.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /partner-requests} : get all the partnerRequests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of partnerRequests in body.
     */
    @GetMapping("/partner-requests")
    public List<PartnerRequest> getAllPartnerRequests() {
        log.debug("REST request to get all PartnerRequests");
        return partnerRequestRepository.findAll();
    }

    /**
     * {@code GET  /partner-requests/:id} : get the "id" partnerRequest.
     *
     * @param id the id of the partnerRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partnerRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/partner-requests/{id}")
    public ResponseEntity<PartnerRequest> getPartnerRequest(@PathVariable Long id) {
        log.debug("REST request to get PartnerRequest : {}", id);
        Optional<PartnerRequest> partnerRequest = partnerRequestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(partnerRequest);
    }

    /**
     * {@code DELETE  /partner-requests/:id} : delete the "id" partnerRequest.
     *
     * @param id the id of the partnerRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/partner-requests/{id}")
    public ResponseEntity<Void> deletePartnerRequest(@PathVariable Long id) {
        log.debug("REST request to delete PartnerRequest : {}", id);
        partnerRequestRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
