package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.*;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.ActivityStatus;
import cr.ac.ucenfotec.fun4fund.repository.TicketRepository;
import cr.ac.ucenfotec.fun4fund.service.MailService;
import cr.ac.ucenfotec.fun4fund.service.PrizeService;
import cr.ac.ucenfotec.fun4fund.service.RaffleService;
import cr.ac.ucenfotec.fun4fund.web.rest.errors.BadRequestAlertException;
import cr.ac.ucenfotec.fun4fund.service.dto.RaffleCriteria;
import cr.ac.ucenfotec.fun4fund.service.RaffleQueryService;

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
import java.util.Random;

/**
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.Raffle}.
 */
@RestController
@RequestMapping("/api")
public class RaffleResource {

    private final Logger log = LoggerFactory.getLogger(RaffleResource.class);

    private static final String ENTITY_NAME = "raffle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RaffleService raffleService;
    private final RaffleQueryService raffleQueryService;
    private final MailService mailService;
    private final PrizeService prizeService;
    private final TicketRepository ticketRepository;

    public RaffleResource(RaffleService raffleService, RaffleQueryService raffleQueryService, MailService mailService, PrizeService prizeService, TicketRepository ticketRepository) {
        this.raffleService = raffleService;
        this.raffleQueryService = raffleQueryService;
        this.mailService = mailService;
        this.prizeService = prizeService;
        this.ticketRepository = ticketRepository;
    }

    /**
     * {@code POST  /raffles} : Create a new raffle.
     *
     * @param raffle the raffle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new raffle, or with status {@code 400 (Bad Request)} if the raffle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/raffles")
    public ResponseEntity<Raffle> createRaffle(@Valid @RequestBody Raffle raffle) throws URISyntaxException {
        log.debug("REST request to save Raffle : {}", raffle);
        if (raffle.getId() != null) {
            throw new BadRequestAlertException("A new raffle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prize prizeResult = raffle.getPrize();
        Prize newPrize = prizeService.save(prizeResult);
        raffle.setPrize(newPrize);
        Raffle result = raffleService.save(raffle);
        return ResponseEntity.created(new URI("/api/raffles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /raffles} : Updates an existing raffle.
     *
     * @param raffle the raffle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated raffle,
     * or with status {@code 400 (Bad Request)} if the raffle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the raffle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/raffles")
    public ResponseEntity<Raffle> updateRaffle(@Valid @RequestBody Raffle raffle) throws URISyntaxException {
        log.debug("REST request to update Raffle : {}", raffle);
        String msgSubject = "Rifa terminada.";
        String msgUser = "Ha ganado la rifa del proyecto "+raffle.getProyect().getName()+ " su premio es " + raffle.getPrize().getName() + ".";

        if (raffle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (raffle.getState() == ActivityStatus.FINISHED){
            Random random = new Random();
            List<Ticket> ticketList = ticketRepository.findByRaffle(raffle);
            int ticketCount = ticketList.size()-1;
            int randomPosition = random.nextInt(ticketCount - 0 + 1) + 0;
            Ticket ticketWinner = ticketList.get(randomPosition);
            ApplicationUser user = ticketWinner.getBuyer();
            raffle.setBuyer(user);
            String msgAdmin = "El ganador de la rifa fue " + raffle.getBuyer().getInternalUser().getFirstName() + " " + raffle.getBuyer().getInternalUser().getLastName() +
                "<br> Pongase en contacto con el ganador por el correo " + raffle.getBuyer().getInternalUser().getEmail() +
                "<br> El premio fue " + raffle.getPrize().getName() + ".";
            mailService.sendEmail(user.getInternalUser().getEmail(),msgSubject,msgUser,false,true);
            mailService.sendEmail(raffle.getProyect().getOwner().getInternalUser().getEmail(),msgSubject,msgAdmin,false,true);
        }else{
            Prize prizeResult = raffle.getPrize();
            Prize newPrize = prizeService.save(prizeResult);
            raffle.setPrize(newPrize);
        }

        Raffle result = raffleService.save(raffle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, raffle.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /raffles} : get all the raffles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of raffles in body.
     */
    @GetMapping("/raffles")
    public ResponseEntity<List<Raffle>> getAllRaffles(RaffleCriteria criteria) {
        log.debug("REST request to get Raffles by criteria: {}", criteria);
        List<Raffle> entityList = raffleQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /raffles/count} : count all the raffles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/raffles/count")
    public ResponseEntity<Long> countRaffles(RaffleCriteria criteria) {
        log.debug("REST request to count Raffles by criteria: {}", criteria);
        return ResponseEntity.ok().body(raffleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /raffles/:id} : get the "id" raffle.
     *
     * @param id the id of the raffle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the raffle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/raffles/{id}")
    public ResponseEntity<Raffle> getRaffle(@PathVariable Long id) {
        log.debug("REST request to get Raffle : {}", id);
        Optional<Raffle> raffle = raffleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(raffle);
    }

    /**
     * {@code DELETE  /raffles/:id} : delete the "id" raffle.
     *
     * @param id the id of the raffle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/raffles/{id}")
    public ResponseEntity<Void> deleteRaffle(@PathVariable Long id) {
        log.debug("REST request to delete Raffle : {}", id);
        raffleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/raffles/getMyRaffleReport")
    public List<List<AuctionAnswerStatistics>> getRaffle(
        @RequestParam(name = "numberMonths", required = false, defaultValue = "6") int numberMonths
    ) {
        if(numberMonths == 0){
            numberMonths = 6;
        }
        return raffleService.getDataRaffle(numberMonths);
    }
}
