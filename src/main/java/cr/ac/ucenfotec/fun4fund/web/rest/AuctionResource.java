package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.Auction;
import cr.ac.ucenfotec.fun4fund.domain.Prize;
import cr.ac.ucenfotec.fun4fund.domain.Resource;
import cr.ac.ucenfotec.fun4fund.service.AuctionService;
import cr.ac.ucenfotec.fun4fund.service.PrizeService;
import cr.ac.ucenfotec.fun4fund.service.ResourceService;
import cr.ac.ucenfotec.fun4fund.web.rest.errors.BadRequestAlertException;
import cr.ac.ucenfotec.fun4fund.service.dto.AuctionCriteria;
import cr.ac.ucenfotec.fun4fund.service.AuctionQueryService;

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
import java.util.Set;

/**
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.Auction}.
 */
@RestController
@RequestMapping("/api")
public class AuctionResource {

    private final Logger log = LoggerFactory.getLogger(AuctionResource.class);

    private static final String ENTITY_NAME = "auction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuctionService auctionService;
    private final AuctionQueryService auctionQueryService;
    private final ResourceService resourceService;
    private final PrizeService prizeService;

    public AuctionResource(AuctionService auctionService, AuctionQueryService auctionQueryService, ResourceService resourceService, PrizeService prizeService) {
        this.auctionService = auctionService;
        this.auctionQueryService = auctionQueryService;
        this.resourceService =  resourceService;
        this.prizeService = prizeService;
    }

    /**
     * {@code POST  /auctions} : Create a new auction.
     *
     * @param auction the auction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auction, or with status {@code 400 (Bad Request)} if the auction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/auctions")
    public ResponseEntity<Auction> createAuction(@Valid @RequestBody Auction auction) throws URISyntaxException {
        log.debug("REST request to save Auction : {}", auction);
        if (auction.getId() != null) {
            throw new BadRequestAlertException("A new auction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prize prize = auction.getPrize();
        Set<Resource> images = prize.getImages();

        Prize temp = prizeService.save(prize);

        for (Object image:images.toArray()) {
            Resource img = (Resource)image;
            img.setPrize(temp);
            img.setType("image");
            resourceService.save((Resource) image);
        };

        Auction result = auctionService.save(auction);
        return ResponseEntity.created(new URI("/api/auctions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /auctions} : Updates an existing auction.
     *
     * @param auction the auction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auction,
     * or with status {@code 400 (Bad Request)} if the auction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/auctions")
    public ResponseEntity<Auction> updateAuction(@Valid @RequestBody Auction auction) throws URISyntaxException {
        log.debug("REST request to update Auction : {}", auction);
        if (auction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Auction result = auctionService.save(auction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auction.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /auctions} : get all the auctions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auctions in body.
     */
    @GetMapping("/auctions")
    public ResponseEntity<List<Auction>> getAllAuctions(AuctionCriteria criteria) {
        log.debug("REST request to get Auctions by criteria: {}", criteria);
        List<Auction> entityList = auctionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /auctions/count} : count all the auctions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/auctions/count")
    public ResponseEntity<Long> countAuctions(AuctionCriteria criteria) {
        log.debug("REST request to count Auctions by criteria: {}", criteria);
        return ResponseEntity.ok().body(auctionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /auctions/:id} : get the "id" auction.
     *
     * @param id the id of the auction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/auctions/{id}")
    public ResponseEntity<Auction> getAuction(@PathVariable Long id) {
        log.debug("REST request to get Auction : {}", id);
        Optional<Auction> auction = auctionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auction);
    }

    /**
     * {@code DELETE  /auctions/:id} : delete the "id" auction.
     *
     * @param id the id of the auction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/auctions/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable Long id) {
        log.debug("REST request to delete Auction : {}", id);
        auctionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
