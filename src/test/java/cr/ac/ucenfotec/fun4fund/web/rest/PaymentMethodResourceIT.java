package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.PaymentMethod;
import cr.ac.ucenfotec.fun4fund.repository.PaymentMethodRepository;

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

import cr.ac.ucenfotec.fun4fund.domain.enumeration.CardType;
/**
 * Integration tests for the {@link PaymentMethodResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PaymentMethodResourceIT {

    private static final String DEFAULT_CARD_NUMBER = "AAAAAAAAAAAAAA";
    private static final String UPDATED_CARD_NUMBER = "BBBBBBBBBBBBBB";

    private static final String DEFAULT_CARD_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_CARD_OWNER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final CardType DEFAULT_TYPE = CardType.VISA;
    private static final CardType UPDATED_TYPE = CardType.MASTERCARD;

    private static final String DEFAULT_CVC = "AAAAAAAAAA";
    private static final String UPDATED_CVC = "BBBBBBBBBB";

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMethodMockMvc;

    private PaymentMethod paymentMethod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMethod createEntity(EntityManager em) {
        PaymentMethod paymentMethod = new PaymentMethod()
            .cardNumber(DEFAULT_CARD_NUMBER)
            .cardOwner(DEFAULT_CARD_OWNER)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .type(DEFAULT_TYPE)
            .cvc(DEFAULT_CVC);
        return paymentMethod;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMethod createUpdatedEntity(EntityManager em) {
        PaymentMethod paymentMethod = new PaymentMethod()
            .cardNumber(UPDATED_CARD_NUMBER)
            .cardOwner(UPDATED_CARD_OWNER)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .type(UPDATED_TYPE)
            .cvc(UPDATED_CVC);
        return paymentMethod;
    }

    @BeforeEach
    public void initTest() {
        paymentMethod = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentMethod() throws Exception {
        int databaseSizeBeforeCreate = paymentMethodRepository.findAll().size();
        // Create the PaymentMethod
        restPaymentMethodMockMvc.perform(post("/api/payment-methods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethod)))
            .andExpect(status().isCreated());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentMethod testPaymentMethod = paymentMethodList.get(paymentMethodList.size() - 1);
        assertThat(testPaymentMethod.getCardNumber()).isEqualTo(DEFAULT_CARD_NUMBER);
        assertThat(testPaymentMethod.getCardOwner()).isEqualTo(DEFAULT_CARD_OWNER);
        assertThat(testPaymentMethod.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testPaymentMethod.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPaymentMethod.getCvc()).isEqualTo(DEFAULT_CVC);
    }

    @Test
    @Transactional
    public void createPaymentMethodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentMethodRepository.findAll().size();

        // Create the PaymentMethod with an existing ID
        paymentMethod.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMethodMockMvc.perform(post("/api/payment-methods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethod)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCardNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().size();
        // set the field null
        paymentMethod.setCardNumber(null);

        // Create the PaymentMethod, which fails.


        restPaymentMethodMockMvc.perform(post("/api/payment-methods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethod)))
            .andExpect(status().isBadRequest());

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCardOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().size();
        // set the field null
        paymentMethod.setCardOwner(null);

        // Create the PaymentMethod, which fails.


        restPaymentMethodMockMvc.perform(post("/api/payment-methods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethod)))
            .andExpect(status().isBadRequest());

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpirationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().size();
        // set the field null
        paymentMethod.setExpirationDate(null);

        // Create the PaymentMethod, which fails.


        restPaymentMethodMockMvc.perform(post("/api/payment-methods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethod)))
            .andExpect(status().isBadRequest());

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().size();
        // set the field null
        paymentMethod.setType(null);

        // Create the PaymentMethod, which fails.


        restPaymentMethodMockMvc.perform(post("/api/payment-methods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethod)))
            .andExpect(status().isBadRequest());

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCvcIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().size();
        // set the field null
        paymentMethod.setCvc(null);

        // Create the PaymentMethod, which fails.


        restPaymentMethodMockMvc.perform(post("/api/payment-methods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethod)))
            .andExpect(status().isBadRequest());

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaymentMethods() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList
        restPaymentMethodMockMvc.perform(get("/api/payment-methods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER)))
            .andExpect(jsonPath("$.[*].cardOwner").value(hasItem(DEFAULT_CARD_OWNER)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(sameInstant(DEFAULT_EXPIRATION_DATE))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].cvc").value(hasItem(DEFAULT_CVC)));
    }
    
    @Test
    @Transactional
    public void getPaymentMethod() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get the paymentMethod
        restPaymentMethodMockMvc.perform(get("/api/payment-methods/{id}", paymentMethod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentMethod.getId().intValue()))
            .andExpect(jsonPath("$.cardNumber").value(DEFAULT_CARD_NUMBER))
            .andExpect(jsonPath("$.cardOwner").value(DEFAULT_CARD_OWNER))
            .andExpect(jsonPath("$.expirationDate").value(sameInstant(DEFAULT_EXPIRATION_DATE)))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.cvc").value(DEFAULT_CVC));
    }
    @Test
    @Transactional
    public void getNonExistingPaymentMethod() throws Exception {
        // Get the paymentMethod
        restPaymentMethodMockMvc.perform(get("/api/payment-methods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentMethod() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().size();

        // Update the paymentMethod
        PaymentMethod updatedPaymentMethod = paymentMethodRepository.findById(paymentMethod.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentMethod are not directly saved in db
        em.detach(updatedPaymentMethod);
        updatedPaymentMethod
            .cardNumber(UPDATED_CARD_NUMBER)
            .cardOwner(UPDATED_CARD_OWNER)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .type(UPDATED_TYPE)
            .cvc(UPDATED_CVC);

        restPaymentMethodMockMvc.perform(put("/api/payment-methods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaymentMethod)))
            .andExpect(status().isOk());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
        PaymentMethod testPaymentMethod = paymentMethodList.get(paymentMethodList.size() - 1);
        assertThat(testPaymentMethod.getCardNumber()).isEqualTo(UPDATED_CARD_NUMBER);
        assertThat(testPaymentMethod.getCardOwner()).isEqualTo(UPDATED_CARD_OWNER);
        assertThat(testPaymentMethod.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPaymentMethod.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPaymentMethod.getCvc()).isEqualTo(UPDATED_CVC);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentMethod() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMethodMockMvc.perform(put("/api/payment-methods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethod)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePaymentMethod() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        int databaseSizeBeforeDelete = paymentMethodRepository.findAll().size();

        // Delete the paymentMethod
        restPaymentMethodMockMvc.perform(delete("/api/payment-methods/{id}", paymentMethod.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
