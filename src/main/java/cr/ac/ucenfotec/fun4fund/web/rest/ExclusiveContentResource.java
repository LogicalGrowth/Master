package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.ExclusiveContent;
import cr.ac.ucenfotec.fun4fund.domain.Prize;
import cr.ac.ucenfotec.fun4fund.domain.Resource;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.ActivityStatus;
import cr.ac.ucenfotec.fun4fund.service.ExclusiveContentService;
import cr.ac.ucenfotec.fun4fund.service.PrizeService;
import cr.ac.ucenfotec.fun4fund.service.ResourceService;
import cr.ac.ucenfotec.fun4fund.web.rest.errors.BadRequestAlertException;
import cr.ac.ucenfotec.fun4fund.service.dto.ExclusiveContentCriteria;
import cr.ac.ucenfotec.fun4fund.service.ExclusiveContentQueryService;

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
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.ExclusiveContent}.
 */
@RestController
@RequestMapping("/api")
public class ExclusiveContentResource {

    private final Logger log = LoggerFactory.getLogger(ExclusiveContentResource.class);

    private static final String ENTITY_NAME = "exclusiveContent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExclusiveContentService exclusiveContentService;

    private final ExclusiveContentQueryService exclusiveContentQueryService;

    private final ResourceService resourceService;

    private final PrizeService prizeService;

    public ExclusiveContentResource(ExclusiveContentService exclusiveContentService, ExclusiveContentQueryService exclusiveContentQueryService, ResourceService resourceService, PrizeService prizeService) {
        this.exclusiveContentService = exclusiveContentService;
        this.exclusiveContentQueryService = exclusiveContentQueryService;
        this.resourceService = resourceService;
        this.prizeService = prizeService;
    }

    /**
     * {@code POST  /exclusive-contents} : Create a new exclusiveContent.
     *
     * @param exclusiveContent the exclusiveContent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exclusiveContent, or with status {@code 400 (Bad Request)} if the exclusiveContent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exclusive-contents")
    public ResponseEntity<ExclusiveContent> createExclusiveContent(@Valid @RequestBody ExclusiveContent exclusiveContent) throws URISyntaxException {
        log.debug("REST request to save ExclusiveContent : {}", exclusiveContent);
        if (exclusiveContent.getId() != null) {
            throw new BadRequestAlertException("A new exclusiveContent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prize prize = exclusiveContent.getPrize();
        Set<Resource> images = prize.getImages();

        Prize temp = prizeService.save(prize);

        for (Object image:images.toArray()) {
            Resource img = (Resource)image;
            img.setPrize(temp);
            resourceService.save((Resource) image);
        };

        ExclusiveContent result = exclusiveContentService.save(exclusiveContent);
        return ResponseEntity.created(new URI("/api/exclusive-contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exclusive-contents} : Updates an existing exclusiveContent.
     *
     * @param exclusiveContent the exclusiveContent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exclusiveContent,
     * or with status {@code 400 (Bad Request)} if the exclusiveContent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exclusiveContent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exclusive-contents")
    public ResponseEntity<ExclusiveContent> updateExclusiveContent(@Valid @RequestBody ExclusiveContent exclusiveContent) throws URISyntaxException {
        log.debug("REST request to update ExclusiveContent : {}", exclusiveContent);
        if (exclusiveContent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if(exclusiveContent.getStock() == 0) {
            exclusiveContent.setState(ActivityStatus.DISABLED);
        }
        ExclusiveContent result = exclusiveContentService.save(exclusiveContent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exclusiveContent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /exclusive-contents} : get all the exclusiveContents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exclusiveContents in body.
     */
    @GetMapping("/exclusive-contents")
    public ResponseEntity<List<ExclusiveContent>> getAllExclusiveContents(ExclusiveContentCriteria criteria) {
        log.debug("REST request to get ExclusiveContents by criteria: {}", criteria);
        List<ExclusiveContent> entityList = exclusiveContentQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /exclusive-contents/count} : count all the exclusiveContents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/exclusive-contents/count")
    public ResponseEntity<Long> countExclusiveContents(ExclusiveContentCriteria criteria) {
        log.debug("REST request to count ExclusiveContents by criteria: {}", criteria);
        return ResponseEntity.ok().body(exclusiveContentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /exclusive-contents/:id} : get the "id" exclusiveContent.
     *
     * @param id the id of the exclusiveContent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exclusiveContent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exclusive-contents/{id}")
    public ResponseEntity<ExclusiveContent> getExclusiveContent(@PathVariable Long id) {
        log.debug("REST request to get ExclusiveContent : {}", id);
        Optional<ExclusiveContent> exclusiveContent = exclusiveContentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exclusiveContent);
    }

    /**
     * {@code DELETE  /exclusive-contents/:id} : delete the "id" exclusiveContent.
     *
     * @param id the id of the exclusiveContent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exclusive-contents/{id}")
    public ResponseEntity<Void> deleteExclusiveContent(@PathVariable Long id) {
        log.debug("REST request to delete ExclusiveContent : {}", id);
        exclusiveContentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
