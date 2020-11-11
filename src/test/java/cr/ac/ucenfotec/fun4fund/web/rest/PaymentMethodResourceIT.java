package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.PaymentMethod;
import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.repository.PaymentMethodRepository;
import cr.ac.ucenfotec.fun4fund.service.PaymentMethodService;
import cr.ac.ucenfotec.fun4fund.service.dto.PaymentMethodCriteria;
import cr.ac.ucenfotec.fun4fund.service.PaymentMethodQueryService;

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
    private static final ZonedDateTime SMALLER_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final CardType DEFAULT_TYPE = CardType.VISA;
    private static final CardType UPDATED_TYPE = CardType.MASTERCARD;

    private static final String DEFAULT_CVC = "AAAAAAAAAA";
    private static final String UPDATED_CVC = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FAVORITE = false;
    private static final Boolean UPDATED_FAVORITE = true;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private PaymentMethodQueryService paymentMethodQueryService;

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
            .cvc(DEFAULT_CVC)
            .favorite(DEFAULT_FAVORITE);
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
            .cvc(UPDATED_CVC)
            .favorite(UPDATED_FAVORITE);
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
        assertThat(testPaymentMethod.isFavorite()).isEqualTo(DEFAULT_FAVORITE);
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
    public void checkFavoriteIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().size();
        // set the field null
        paymentMethod.setFavorite(null);

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
            .andExpect(jsonPath("$.[*].cvc").value(hasItem(DEFAULT_CVC)))
            .andExpect(jsonPath("$.[*].favorite").value(hasItem(DEFAULT_FAVORITE.booleanValue())));
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
            .andExpect(jsonPath("$.cvc").value(DEFAULT_CVC))
            .andExpect(jsonPath("$.favorite").value(DEFAULT_FAVORITE.booleanValue()));
    }


    @Test
    @Transactional
    public void getPaymentMethodsByIdFiltering() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        Long id = paymentMethod.getId();

        defaultPaymentMethodShouldBeFound("id.equals=" + id);
        defaultPaymentMethodShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentMethodShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentMethodShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentMethodShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentMethodShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPaymentMethodsByCardNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cardNumber equals to DEFAULT_CARD_NUMBER
        defaultPaymentMethodShouldBeFound("cardNumber.equals=" + DEFAULT_CARD_NUMBER);

        // Get all the paymentMethodList where cardNumber equals to UPDATED_CARD_NUMBER
        defaultPaymentMethodShouldNotBeFound("cardNumber.equals=" + UPDATED_CARD_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByCardNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cardNumber not equals to DEFAULT_CARD_NUMBER
        defaultPaymentMethodShouldNotBeFound("cardNumber.notEquals=" + DEFAULT_CARD_NUMBER);

        // Get all the paymentMethodList where cardNumber not equals to UPDATED_CARD_NUMBER
        defaultPaymentMethodShouldBeFound("cardNumber.notEquals=" + UPDATED_CARD_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByCardNumberIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cardNumber in DEFAULT_CARD_NUMBER or UPDATED_CARD_NUMBER
        defaultPaymentMethodShouldBeFound("cardNumber.in=" + DEFAULT_CARD_NUMBER + "," + UPDATED_CARD_NUMBER);

        // Get all the paymentMethodList where cardNumber equals to UPDATED_CARD_NUMBER
        defaultPaymentMethodShouldNotBeFound("cardNumber.in=" + UPDATED_CARD_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByCardNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cardNumber is not null
        defaultPaymentMethodShouldBeFound("cardNumber.specified=true");

        // Get all the paymentMethodList where cardNumber is null
        defaultPaymentMethodShouldNotBeFound("cardNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentMethodsByCardNumberContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cardNumber contains DEFAULT_CARD_NUMBER
        defaultPaymentMethodShouldBeFound("cardNumber.contains=" + DEFAULT_CARD_NUMBER);

        // Get all the paymentMethodList where cardNumber contains UPDATED_CARD_NUMBER
        defaultPaymentMethodShouldNotBeFound("cardNumber.contains=" + UPDATED_CARD_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByCardNumberNotContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cardNumber does not contain DEFAULT_CARD_NUMBER
        defaultPaymentMethodShouldNotBeFound("cardNumber.doesNotContain=" + DEFAULT_CARD_NUMBER);

        // Get all the paymentMethodList where cardNumber does not contain UPDATED_CARD_NUMBER
        defaultPaymentMethodShouldBeFound("cardNumber.doesNotContain=" + UPDATED_CARD_NUMBER);
    }


    @Test
    @Transactional
    public void getAllPaymentMethodsByCardOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cardOwner equals to DEFAULT_CARD_OWNER
        defaultPaymentMethodShouldBeFound("cardOwner.equals=" + DEFAULT_CARD_OWNER);

        // Get all the paymentMethodList where cardOwner equals to UPDATED_CARD_OWNER
        defaultPaymentMethodShouldNotBeFound("cardOwner.equals=" + UPDATED_CARD_OWNER);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByCardOwnerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cardOwner not equals to DEFAULT_CARD_OWNER
        defaultPaymentMethodShouldNotBeFound("cardOwner.notEquals=" + DEFAULT_CARD_OWNER);

        // Get all the paymentMethodList where cardOwner not equals to UPDATED_CARD_OWNER
        defaultPaymentMethodShouldBeFound("cardOwner.notEquals=" + UPDATED_CARD_OWNER);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByCardOwnerIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cardOwner in DEFAULT_CARD_OWNER or UPDATED_CARD_OWNER
        defaultPaymentMethodShouldBeFound("cardOwner.in=" + DEFAULT_CARD_OWNER + "," + UPDATED_CARD_OWNER);

        // Get all the paymentMethodList where cardOwner equals to UPDATED_CARD_OWNER
        defaultPaymentMethodShouldNotBeFound("cardOwner.in=" + UPDATED_CARD_OWNER);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByCardOwnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cardOwner is not null
        defaultPaymentMethodShouldBeFound("cardOwner.specified=true");

        // Get all the paymentMethodList where cardOwner is null
        defaultPaymentMethodShouldNotBeFound("cardOwner.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentMethodsByCardOwnerContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cardOwner contains DEFAULT_CARD_OWNER
        defaultPaymentMethodShouldBeFound("cardOwner.contains=" + DEFAULT_CARD_OWNER);

        // Get all the paymentMethodList where cardOwner contains UPDATED_CARD_OWNER
        defaultPaymentMethodShouldNotBeFound("cardOwner.contains=" + UPDATED_CARD_OWNER);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByCardOwnerNotContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cardOwner does not contain DEFAULT_CARD_OWNER
        defaultPaymentMethodShouldNotBeFound("cardOwner.doesNotContain=" + DEFAULT_CARD_OWNER);

        // Get all the paymentMethodList where cardOwner does not contain UPDATED_CARD_OWNER
        defaultPaymentMethodShouldBeFound("cardOwner.doesNotContain=" + UPDATED_CARD_OWNER);
    }


    @Test
    @Transactional
    public void getAllPaymentMethodsByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where expirationDate equals to DEFAULT_EXPIRATION_DATE
        defaultPaymentMethodShouldBeFound("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the paymentMethodList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultPaymentMethodShouldNotBeFound("expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByExpirationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where expirationDate not equals to DEFAULT_EXPIRATION_DATE
        defaultPaymentMethodShouldNotBeFound("expirationDate.notEquals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the paymentMethodList where expirationDate not equals to UPDATED_EXPIRATION_DATE
        defaultPaymentMethodShouldBeFound("expirationDate.notEquals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where expirationDate in DEFAULT_EXPIRATION_DATE or UPDATED_EXPIRATION_DATE
        defaultPaymentMethodShouldBeFound("expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE);

        // Get all the paymentMethodList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultPaymentMethodShouldNotBeFound("expirationDate.in=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where expirationDate is not null
        defaultPaymentMethodShouldBeFound("expirationDate.specified=true");

        // Get all the paymentMethodList where expirationDate is null
        defaultPaymentMethodShouldNotBeFound("expirationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByExpirationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where expirationDate is greater than or equal to DEFAULT_EXPIRATION_DATE
        defaultPaymentMethodShouldBeFound("expirationDate.greaterThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the paymentMethodList where expirationDate is greater than or equal to UPDATED_EXPIRATION_DATE
        defaultPaymentMethodShouldNotBeFound("expirationDate.greaterThanOrEqual=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByExpirationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where expirationDate is less than or equal to DEFAULT_EXPIRATION_DATE
        defaultPaymentMethodShouldBeFound("expirationDate.lessThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the paymentMethodList where expirationDate is less than or equal to SMALLER_EXPIRATION_DATE
        defaultPaymentMethodShouldNotBeFound("expirationDate.lessThanOrEqual=" + SMALLER_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByExpirationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where expirationDate is less than DEFAULT_EXPIRATION_DATE
        defaultPaymentMethodShouldNotBeFound("expirationDate.lessThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the paymentMethodList where expirationDate is less than UPDATED_EXPIRATION_DATE
        defaultPaymentMethodShouldBeFound("expirationDate.lessThan=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByExpirationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where expirationDate is greater than DEFAULT_EXPIRATION_DATE
        defaultPaymentMethodShouldNotBeFound("expirationDate.greaterThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the paymentMethodList where expirationDate is greater than SMALLER_EXPIRATION_DATE
        defaultPaymentMethodShouldBeFound("expirationDate.greaterThan=" + SMALLER_EXPIRATION_DATE);
    }


    @Test
    @Transactional
    public void getAllPaymentMethodsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where type equals to DEFAULT_TYPE
        defaultPaymentMethodShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the paymentMethodList where type equals to UPDATED_TYPE
        defaultPaymentMethodShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where type not equals to DEFAULT_TYPE
        defaultPaymentMethodShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the paymentMethodList where type not equals to UPDATED_TYPE
        defaultPaymentMethodShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultPaymentMethodShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the paymentMethodList where type equals to UPDATED_TYPE
        defaultPaymentMethodShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where type is not null
        defaultPaymentMethodShouldBeFound("type.specified=true");

        // Get all the paymentMethodList where type is null
        defaultPaymentMethodShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByCvcIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cvc equals to DEFAULT_CVC
        defaultPaymentMethodShouldBeFound("cvc.equals=" + DEFAULT_CVC);

        // Get all the paymentMethodList where cvc equals to UPDATED_CVC
        defaultPaymentMethodShouldNotBeFound("cvc.equals=" + UPDATED_CVC);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByCvcIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cvc not equals to DEFAULT_CVC
        defaultPaymentMethodShouldNotBeFound("cvc.notEquals=" + DEFAULT_CVC);

        // Get all the paymentMethodList where cvc not equals to UPDATED_CVC
        defaultPaymentMethodShouldBeFound("cvc.notEquals=" + UPDATED_CVC);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByCvcIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cvc in DEFAULT_CVC or UPDATED_CVC
        defaultPaymentMethodShouldBeFound("cvc.in=" + DEFAULT_CVC + "," + UPDATED_CVC);

        // Get all the paymentMethodList where cvc equals to UPDATED_CVC
        defaultPaymentMethodShouldNotBeFound("cvc.in=" + UPDATED_CVC);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByCvcIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cvc is not null
        defaultPaymentMethodShouldBeFound("cvc.specified=true");

        // Get all the paymentMethodList where cvc is null
        defaultPaymentMethodShouldNotBeFound("cvc.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentMethodsByCvcContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cvc contains DEFAULT_CVC
        defaultPaymentMethodShouldBeFound("cvc.contains=" + DEFAULT_CVC);

        // Get all the paymentMethodList where cvc contains UPDATED_CVC
        defaultPaymentMethodShouldNotBeFound("cvc.contains=" + UPDATED_CVC);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByCvcNotContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where cvc does not contain DEFAULT_CVC
        defaultPaymentMethodShouldNotBeFound("cvc.doesNotContain=" + DEFAULT_CVC);

        // Get all the paymentMethodList where cvc does not contain UPDATED_CVC
        defaultPaymentMethodShouldBeFound("cvc.doesNotContain=" + UPDATED_CVC);
    }


    @Test
    @Transactional
    public void getAllPaymentMethodsByFavoriteIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where favorite equals to DEFAULT_FAVORITE
        defaultPaymentMethodShouldBeFound("favorite.equals=" + DEFAULT_FAVORITE);

        // Get all the paymentMethodList where favorite equals to UPDATED_FAVORITE
        defaultPaymentMethodShouldNotBeFound("favorite.equals=" + UPDATED_FAVORITE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByFavoriteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where favorite not equals to DEFAULT_FAVORITE
        defaultPaymentMethodShouldNotBeFound("favorite.notEquals=" + DEFAULT_FAVORITE);

        // Get all the paymentMethodList where favorite not equals to UPDATED_FAVORITE
        defaultPaymentMethodShouldBeFound("favorite.notEquals=" + UPDATED_FAVORITE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByFavoriteIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where favorite in DEFAULT_FAVORITE or UPDATED_FAVORITE
        defaultPaymentMethodShouldBeFound("favorite.in=" + DEFAULT_FAVORITE + "," + UPDATED_FAVORITE);

        // Get all the paymentMethodList where favorite equals to UPDATED_FAVORITE
        defaultPaymentMethodShouldNotBeFound("favorite.in=" + UPDATED_FAVORITE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByFavoriteIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where favorite is not null
        defaultPaymentMethodShouldBeFound("favorite.specified=true");

        // Get all the paymentMethodList where favorite is null
        defaultPaymentMethodShouldNotBeFound("favorite.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);
        ApplicationUser owner = ApplicationUserResourceIT.createEntity(em);
        em.persist(owner);
        em.flush();
        paymentMethod.setOwner(owner);
        paymentMethodRepository.saveAndFlush(paymentMethod);
        Long ownerId = owner.getId();

        // Get all the paymentMethodList where owner equals to ownerId
        defaultPaymentMethodShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the paymentMethodList where owner equals to ownerId + 1
        defaultPaymentMethodShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentMethodShouldBeFound(String filter) throws Exception {
        restPaymentMethodMockMvc.perform(get("/api/payment-methods?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER)))
            .andExpect(jsonPath("$.[*].cardOwner").value(hasItem(DEFAULT_CARD_OWNER)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(sameInstant(DEFAULT_EXPIRATION_DATE))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].cvc").value(hasItem(DEFAULT_CVC)))
            .andExpect(jsonPath("$.[*].favorite").value(hasItem(DEFAULT_FAVORITE.booleanValue())));

        // Check, that the count call also returns 1
        restPaymentMethodMockMvc.perform(get("/api/payment-methods/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentMethodShouldNotBeFound(String filter) throws Exception {
        restPaymentMethodMockMvc.perform(get("/api/payment-methods?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMethodMockMvc.perform(get("/api/payment-methods/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        paymentMethodService.save(paymentMethod);

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
            .cvc(UPDATED_CVC)
            .favorite(UPDATED_FAVORITE);

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
        assertThat(testPaymentMethod.isFavorite()).isEqualTo(UPDATED_FAVORITE);
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
        paymentMethodService.save(paymentMethod);

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
