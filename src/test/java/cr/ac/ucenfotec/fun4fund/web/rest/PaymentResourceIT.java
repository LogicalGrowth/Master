package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Payment;
import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.repository.PaymentRepository;
import cr.ac.ucenfotec.fun4fund.service.PaymentService;
import cr.ac.ucenfotec.fun4fund.service.dto.PaymentCriteria;
import cr.ac.ucenfotec.fun4fund.service.PaymentQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static cr.ac.ucenfotec.fun4fund.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.ucenfotec.fun4fund.domain.enumeration.ProductType;
/**
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PaymentResourceIT {

    private static final Double DEFAULT_AMOUNT = 0D;
    private static final Double UPDATED_AMOUNT = 1D;
    private static final Double SMALLER_AMOUNT = 0D - 1D;

    private static final ProductType DEFAULT_TYPE = ProductType.RAFFLE;
    private static final ProductType UPDATED_TYPE = ProductType.AUCTION;

    private static final ZonedDateTime DEFAULT_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_STAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentQueryService paymentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .amount(DEFAULT_AMOUNT)
            .type(DEFAULT_TYPE)
            .timeStamp(DEFAULT_TIME_STAMP);
        return payment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity(EntityManager em) {
        Payment payment = new Payment()
            .amount(UPDATED_AMOUNT)
            .type(UPDATED_TYPE)
            .timeStamp(UPDATED_TIME_STAMP);
        return payment;
    }

    @BeforeEach
    public void initTest() {
        payment = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();
        // Create the Payment
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPayment.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPayment.getTimeStamp()).isEqualTo(DEFAULT_TIME_STAMP);
    }

    @Test
    @Transactional
    public void createPaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment with an existing ID
        payment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setAmount(null);

        // Create the Payment, which fails.


        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setType(null);

        // Create the Payment, which fails.


        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimeStampIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setTimeStamp(null);

        // Create the Payment, which fails.


        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].timeStamp").value(hasItem(sameInstant(DEFAULT_TIME_STAMP))));
    }
    
    @Test
    @Transactional
    public void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.timeStamp").value(sameInstant(DEFAULT_TIME_STAMP)));
    }


    @Test
    @Transactional
    public void getPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        Long id = payment.getId();

        defaultPaymentShouldBeFound("id.equals=" + id);
        defaultPaymentShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPaymentsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount equals to DEFAULT_AMOUNT
        defaultPaymentShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount equals to UPDATED_AMOUNT
        defaultPaymentShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount not equals to DEFAULT_AMOUNT
        defaultPaymentShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount not equals to UPDATED_AMOUNT
        defaultPaymentShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultPaymentShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the paymentList where amount equals to UPDATED_AMOUNT
        defaultPaymentShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is not null
        defaultPaymentShouldBeFound("amount.specified=true");

        // Get all the paymentList where amount is null
        defaultPaymentShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultPaymentShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is greater than or equal to UPDATED_AMOUNT
        defaultPaymentShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is less than or equal to DEFAULT_AMOUNT
        defaultPaymentShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is less than or equal to SMALLER_AMOUNT
        defaultPaymentShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is less than DEFAULT_AMOUNT
        defaultPaymentShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is less than UPDATED_AMOUNT
        defaultPaymentShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is greater than DEFAULT_AMOUNT
        defaultPaymentShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is greater than SMALLER_AMOUNT
        defaultPaymentShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllPaymentsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where type equals to DEFAULT_TYPE
        defaultPaymentShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the paymentList where type equals to UPDATED_TYPE
        defaultPaymentShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where type not equals to DEFAULT_TYPE
        defaultPaymentShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the paymentList where type not equals to UPDATED_TYPE
        defaultPaymentShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultPaymentShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the paymentList where type equals to UPDATED_TYPE
        defaultPaymentShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where type is not null
        defaultPaymentShouldBeFound("type.specified=true");

        // Get all the paymentList where type is null
        defaultPaymentShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByTimeStampIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where timeStamp equals to DEFAULT_TIME_STAMP
        defaultPaymentShouldBeFound("timeStamp.equals=" + DEFAULT_TIME_STAMP);

        // Get all the paymentList where timeStamp equals to UPDATED_TIME_STAMP
        defaultPaymentShouldNotBeFound("timeStamp.equals=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllPaymentsByTimeStampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where timeStamp not equals to DEFAULT_TIME_STAMP
        defaultPaymentShouldNotBeFound("timeStamp.notEquals=" + DEFAULT_TIME_STAMP);

        // Get all the paymentList where timeStamp not equals to UPDATED_TIME_STAMP
        defaultPaymentShouldBeFound("timeStamp.notEquals=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllPaymentsByTimeStampIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where timeStamp in DEFAULT_TIME_STAMP or UPDATED_TIME_STAMP
        defaultPaymentShouldBeFound("timeStamp.in=" + DEFAULT_TIME_STAMP + "," + UPDATED_TIME_STAMP);

        // Get all the paymentList where timeStamp equals to UPDATED_TIME_STAMP
        defaultPaymentShouldNotBeFound("timeStamp.in=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllPaymentsByTimeStampIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where timeStamp is not null
        defaultPaymentShouldBeFound("timeStamp.specified=true");

        // Get all the paymentList where timeStamp is null
        defaultPaymentShouldNotBeFound("timeStamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByTimeStampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where timeStamp is greater than or equal to DEFAULT_TIME_STAMP
        defaultPaymentShouldBeFound("timeStamp.greaterThanOrEqual=" + DEFAULT_TIME_STAMP);

        // Get all the paymentList where timeStamp is greater than or equal to UPDATED_TIME_STAMP
        defaultPaymentShouldNotBeFound("timeStamp.greaterThanOrEqual=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllPaymentsByTimeStampIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where timeStamp is less than or equal to DEFAULT_TIME_STAMP
        defaultPaymentShouldBeFound("timeStamp.lessThanOrEqual=" + DEFAULT_TIME_STAMP);

        // Get all the paymentList where timeStamp is less than or equal to SMALLER_TIME_STAMP
        defaultPaymentShouldNotBeFound("timeStamp.lessThanOrEqual=" + SMALLER_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllPaymentsByTimeStampIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where timeStamp is less than DEFAULT_TIME_STAMP
        defaultPaymentShouldNotBeFound("timeStamp.lessThan=" + DEFAULT_TIME_STAMP);

        // Get all the paymentList where timeStamp is less than UPDATED_TIME_STAMP
        defaultPaymentShouldBeFound("timeStamp.lessThan=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllPaymentsByTimeStampIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where timeStamp is greater than DEFAULT_TIME_STAMP
        defaultPaymentShouldNotBeFound("timeStamp.greaterThan=" + DEFAULT_TIME_STAMP);

        // Get all the paymentList where timeStamp is greater than SMALLER_TIME_STAMP
        defaultPaymentShouldBeFound("timeStamp.greaterThan=" + SMALLER_TIME_STAMP);
    }


    @Test
    @Transactional
    public void getAllPaymentsByApplicationUserIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        ApplicationUser applicationUser = ApplicationUserResourceIT.createEntity(em);
        em.persist(applicationUser);
        em.flush();
        payment.setApplicationUser(applicationUser);
        paymentRepository.saveAndFlush(payment);
        Long applicationUserId = applicationUser.getId();

        // Get all the paymentList where applicationUser equals to applicationUserId
        defaultPaymentShouldBeFound("applicationUserId.equals=" + applicationUserId);

        // Get all the paymentList where applicationUser equals to applicationUserId + 1
        defaultPaymentShouldNotBeFound("applicationUserId.equals=" + (applicationUserId + 1));
    }


    @Test
    @Transactional
    public void getAllPaymentsByProyectIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        Proyect proyect = ProyectResourceIT.createEntity(em);
        em.persist(proyect);
        em.flush();
        payment.setProyect(proyect);
        paymentRepository.saveAndFlush(payment);
        Long proyectId = proyect.getId();

        // Get all the paymentList where proyect equals to proyectId
        defaultPaymentShouldBeFound("proyectId.equals=" + proyectId);

        // Get all the paymentList where proyect equals to proyectId + 1
        defaultPaymentShouldNotBeFound("proyectId.equals=" + (proyectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentShouldBeFound(String filter) throws Exception {
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].timeStamp").value(hasItem(sameInstant(DEFAULT_TIME_STAMP))));

        // Check, that the count call also returns 1
        restPaymentMockMvc.perform(get("/api/payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentShouldNotBeFound(String filter) throws Exception {
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMockMvc.perform(get("/api/payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayment() throws Exception {
        // Initialize the database
        paymentService.save(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).get();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .amount(UPDATED_AMOUNT)
            .type(UPDATED_TYPE)
            .timeStamp(UPDATED_TIME_STAMP);

        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPayment)))
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPayment.getTimeStamp()).isEqualTo(UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePayment() throws Exception {
        // Initialize the database
        paymentService.save(payment);

        int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Delete the payment
        restPaymentMockMvc.perform(delete("/api/payments/{id}", payment.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
