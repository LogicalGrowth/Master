package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.domain.Payment;
import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.ProductType;
import cr.ac.ucenfotec.fun4fund.service.*;
import cr.ac.ucenfotec.fun4fund.repository.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import cr.ac.ucenfotec.fun4fund.web.rest.errors.BadRequestAlertException;
import cr.ac.ucenfotec.fun4fund.service.dto.PaymentCriteria;

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
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.Payment}.
 */
@RestController
@RequestMapping("/api")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    private static final String ENTITY_NAME = "payment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentService paymentService;

    private final PaymentQueryService paymentQueryService;

    private final PaymentRepository paymentRepository;

    private final MailService mailService;

    private final ApplicationUserService applicationUserService;

    private final ProyectService proyectService;

    public PaymentResource(PaymentService paymentService, PaymentQueryService paymentQueryService,
                           PaymentRepository paymentRepository, MailService mailService, ApplicationUserService applicationUserService,
                           ProyectService proyectService) {
        this.paymentService = paymentService;
        this.paymentQueryService = paymentQueryService;
        this.paymentRepository = paymentRepository;
        this.mailService = mailService;
        this.applicationUserService = applicationUserService;
        this.proyectService = proyectService;
    }

    /**
     * {@code POST  /payments} : Create a new payment.
     *
     * @param payment the payment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payment, or with status {@code 400 (Bad Request)} if the payment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payments")
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) throws URISyntaxException {
        log.debug("REST request to save Payment : {}", payment);
        if (payment.getId() != null) {
            throw new BadRequestAlertException("A new payment cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Optional<ApplicationUser> applicationUser = applicationUserService.findOne(payment.getApplicationUser().getId());

        Proyect proyect = proyectService.findOne(payment.getProyect().getId()).get();
        Double fee = payment.getAmount() * proyect.getFee() / 100;
        proyect.setCollected(proyect.getCollected() + payment.getAmount() - fee);

        String type = "";

        if(payment.getType() == ProductType.AUCTION){
            type = "la puja de " + payment.getAmount() + "de la subasta en el proyecto " + payment.getProyect().getName();
        } else if(payment.getType() == ProductType.DONATION){
            type = "la donación de " + payment.getAmount() +" al proyecto " + payment.getProyect().getName();
        }else if(payment.getType() == ProductType.EXCLUSIVE_CONTENT){
            type = "la compra de contenido exlusivo en el proyecto " + payment.getProyect().getName() + "monto final: " + payment.getAmount();
        }else if(payment.getType() == ProductType.PARTNERSHIP){
            type = "";
        }else if(payment.getType() == ProductType.RAFFLE){
            type = "participar en la rifa del proyecto " + payment.getProyect().getName()+ "el monto final fue de: " + payment.getAmount();
        }
        String subject = "Recibido de pago";

        String content = "Muchas gracias por " + type;
        mailService.sendEmail(applicationUser.get().getInternalUser().getEmail(),subject,content,false,true);
        Payment result = paymentService.save(payment);

        Proyect proyectResult = proyectService.save(proyect);
        //Guardar aquí fee

        return ResponseEntity.created(new URI("/api/payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payments} : Updates an existing payment.
     *
     * @param payment the payment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payment,
     * or with status {@code 400 (Bad Request)} if the payment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payments")
    public ResponseEntity<Payment> updatePayment(@Valid @RequestBody Payment payment) throws URISyntaxException {
        log.debug("REST request to update Payment : {}", payment);
        if (payment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Payment result = paymentService.save(payment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payment.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payments} : get all the payments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payments in body.
     */
    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getAllPayments(PaymentCriteria criteria) {
        log.debug("REST request to get Payments by criteria: {}", criteria);
        List<Payment> entityList = paymentQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /payments/count} : count all the payments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/payments/count")
    public ResponseEntity<Long> countPayments(PaymentCriteria criteria) {
        log.debug("REST request to count Payments by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payments/:id} : get the "id" payment.
     *
     * @param id the id of the payment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payments/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        log.debug("REST request to get Payment : {}", id);
        Optional<Payment> payment = paymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payment);
    }

    /**
     * {@code DELETE  /payments/:id} : delete the "id" payment.
     *
     * @param id the id of the payment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payments/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        log.debug("REST request to delete Payment : {}", id);
        paymentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/payments/proyect/{id}")
    public List<?> getProyectDonations(@PathVariable Long id) {
        log.debug("REST request to get Payment : {}", id);
        Pageable paging = PageRequest.of(0, 5);
        return paymentRepository.findTop5ByProyectId(id, paging);
    }
}
