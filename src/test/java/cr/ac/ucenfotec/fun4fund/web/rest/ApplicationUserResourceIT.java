package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.domain.User;
import cr.ac.ucenfotec.fun4fund.domain.PaymentMethod;
import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.domain.Notification;
import cr.ac.ucenfotec.fun4fund.domain.Payment;
import cr.ac.ucenfotec.fun4fund.repository.ApplicationUserRepository;
import cr.ac.ucenfotec.fun4fund.service.ApplicationUserService;
import cr.ac.ucenfotec.fun4fund.service.dto.ApplicationUserCriteria;
import cr.ac.ucenfotec.fun4fund.service.ApplicationUserQueryService;

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

import cr.ac.ucenfotec.fun4fund.domain.enumeration.IdType;
/**
 * Integration tests for the {@link ApplicationUserResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ApplicationUserResourceIT {

    private static final String DEFAULT_IDENTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATION = "BBBBBBBBBB";

    private static final IdType DEFAULT_ID_TYPE = IdType.PASSPORT;
    private static final IdType UPDATED_ID_TYPE = IdType.IDENTIFICATION;

    private static final ZonedDateTime DEFAULT_BIRTH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_BIRTH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_BIRTH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ADMIN = false;
    private static final Boolean UPDATED_ADMIN = true;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private ApplicationUserQueryService applicationUserQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationUserMockMvc;

    private ApplicationUser applicationUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser()
            .identification(DEFAULT_IDENTIFICATION)
            .idType(DEFAULT_ID_TYPE)
            .birthDate(DEFAULT_BIRTH_DATE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .admin(DEFAULT_ADMIN);
        return applicationUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createUpdatedEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser()
            .identification(UPDATED_IDENTIFICATION)
            .idType(UPDATED_ID_TYPE)
            .birthDate(UPDATED_BIRTH_DATE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .admin(UPDATED_ADMIN);
        return applicationUser;
    }

    @BeforeEach
    public void initTest() {
        applicationUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplicationUser() throws Exception {
        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();
        // Create the ApplicationUser
        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isCreated());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getIdentification()).isEqualTo(DEFAULT_IDENTIFICATION);
        assertThat(testApplicationUser.getIdType()).isEqualTo(DEFAULT_ID_TYPE);
        assertThat(testApplicationUser.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testApplicationUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testApplicationUser.isAdmin()).isEqualTo(DEFAULT_ADMIN);
    }

    @Test
    @Transactional
    public void createApplicationUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();

        // Create the ApplicationUser with an existing ID
        applicationUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkIdentificationIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationUserRepository.findAll().size();
        // set the field null
        applicationUser.setIdentification(null);

        // Create the ApplicationUser, which fails.


        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationUserRepository.findAll().size();
        // set the field null
        applicationUser.setIdType(null);

        // Create the ApplicationUser, which fails.


        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationUserRepository.findAll().size();
        // set the field null
        applicationUser.setBirthDate(null);

        // Create the ApplicationUser, which fails.


        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationUserRepository.findAll().size();
        // set the field null
        applicationUser.setPhoneNumber(null);

        // Create the ApplicationUser, which fails.


        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdminIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationUserRepository.findAll().size();
        // set the field null
        applicationUser.setAdmin(null);

        // Create the ApplicationUser, which fails.


        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplicationUsers() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList
        restApplicationUserMockMvc.perform(get("/api/application-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].identification").value(hasItem(DEFAULT_IDENTIFICATION)))
            .andExpect(jsonPath("$.[*].idType").value(hasItem(DEFAULT_ID_TYPE.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(sameInstant(DEFAULT_BIRTH_DATE))))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].admin").value(hasItem(DEFAULT_ADMIN.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get the applicationUser
        restApplicationUserMockMvc.perform(get("/api/application-users/{id}", applicationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationUser.getId().intValue()))
            .andExpect(jsonPath("$.identification").value(DEFAULT_IDENTIFICATION))
            .andExpect(jsonPath("$.idType").value(DEFAULT_ID_TYPE.toString()))
            .andExpect(jsonPath("$.birthDate").value(sameInstant(DEFAULT_BIRTH_DATE)))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.admin").value(DEFAULT_ADMIN.booleanValue()));
    }


    @Test
    @Transactional
    public void getApplicationUsersByIdFiltering() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        Long id = applicationUser.getId();

        defaultApplicationUserShouldBeFound("id.equals=" + id);
        defaultApplicationUserShouldNotBeFound("id.notEquals=" + id);

        defaultApplicationUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultApplicationUserShouldNotBeFound("id.greaterThan=" + id);

        defaultApplicationUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultApplicationUserShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllApplicationUsersByIdentificationIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where identification equals to DEFAULT_IDENTIFICATION
        defaultApplicationUserShouldBeFound("identification.equals=" + DEFAULT_IDENTIFICATION);

        // Get all the applicationUserList where identification equals to UPDATED_IDENTIFICATION
        defaultApplicationUserShouldNotBeFound("identification.equals=" + UPDATED_IDENTIFICATION);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByIdentificationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where identification not equals to DEFAULT_IDENTIFICATION
        defaultApplicationUserShouldNotBeFound("identification.notEquals=" + DEFAULT_IDENTIFICATION);

        // Get all the applicationUserList where identification not equals to UPDATED_IDENTIFICATION
        defaultApplicationUserShouldBeFound("identification.notEquals=" + UPDATED_IDENTIFICATION);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByIdentificationIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where identification in DEFAULT_IDENTIFICATION or UPDATED_IDENTIFICATION
        defaultApplicationUserShouldBeFound("identification.in=" + DEFAULT_IDENTIFICATION + "," + UPDATED_IDENTIFICATION);

        // Get all the applicationUserList where identification equals to UPDATED_IDENTIFICATION
        defaultApplicationUserShouldNotBeFound("identification.in=" + UPDATED_IDENTIFICATION);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByIdentificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where identification is not null
        defaultApplicationUserShouldBeFound("identification.specified=true");

        // Get all the applicationUserList where identification is null
        defaultApplicationUserShouldNotBeFound("identification.specified=false");
    }
                @Test
    @Transactional
    public void getAllApplicationUsersByIdentificationContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where identification contains DEFAULT_IDENTIFICATION
        defaultApplicationUserShouldBeFound("identification.contains=" + DEFAULT_IDENTIFICATION);

        // Get all the applicationUserList where identification contains UPDATED_IDENTIFICATION
        defaultApplicationUserShouldNotBeFound("identification.contains=" + UPDATED_IDENTIFICATION);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByIdentificationNotContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where identification does not contain DEFAULT_IDENTIFICATION
        defaultApplicationUserShouldNotBeFound("identification.doesNotContain=" + DEFAULT_IDENTIFICATION);

        // Get all the applicationUserList where identification does not contain UPDATED_IDENTIFICATION
        defaultApplicationUserShouldBeFound("identification.doesNotContain=" + UPDATED_IDENTIFICATION);
    }


    @Test
    @Transactional
    public void getAllApplicationUsersByIdTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where idType equals to DEFAULT_ID_TYPE
        defaultApplicationUserShouldBeFound("idType.equals=" + DEFAULT_ID_TYPE);

        // Get all the applicationUserList where idType equals to UPDATED_ID_TYPE
        defaultApplicationUserShouldNotBeFound("idType.equals=" + UPDATED_ID_TYPE);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByIdTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where idType not equals to DEFAULT_ID_TYPE
        defaultApplicationUserShouldNotBeFound("idType.notEquals=" + DEFAULT_ID_TYPE);

        // Get all the applicationUserList where idType not equals to UPDATED_ID_TYPE
        defaultApplicationUserShouldBeFound("idType.notEquals=" + UPDATED_ID_TYPE);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByIdTypeIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where idType in DEFAULT_ID_TYPE or UPDATED_ID_TYPE
        defaultApplicationUserShouldBeFound("idType.in=" + DEFAULT_ID_TYPE + "," + UPDATED_ID_TYPE);

        // Get all the applicationUserList where idType equals to UPDATED_ID_TYPE
        defaultApplicationUserShouldNotBeFound("idType.in=" + UPDATED_ID_TYPE);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByIdTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where idType is not null
        defaultApplicationUserShouldBeFound("idType.specified=true");

        // Get all the applicationUserList where idType is null
        defaultApplicationUserShouldNotBeFound("idType.specified=false");
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultApplicationUserShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the applicationUserList where birthDate equals to UPDATED_BIRTH_DATE
        defaultApplicationUserShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByBirthDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where birthDate not equals to DEFAULT_BIRTH_DATE
        defaultApplicationUserShouldNotBeFound("birthDate.notEquals=" + DEFAULT_BIRTH_DATE);

        // Get all the applicationUserList where birthDate not equals to UPDATED_BIRTH_DATE
        defaultApplicationUserShouldBeFound("birthDate.notEquals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultApplicationUserShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the applicationUserList where birthDate equals to UPDATED_BIRTH_DATE
        defaultApplicationUserShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where birthDate is not null
        defaultApplicationUserShouldBeFound("birthDate.specified=true");

        // Get all the applicationUserList where birthDate is null
        defaultApplicationUserShouldNotBeFound("birthDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where birthDate is greater than or equal to DEFAULT_BIRTH_DATE
        defaultApplicationUserShouldBeFound("birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the applicationUserList where birthDate is greater than or equal to UPDATED_BIRTH_DATE
        defaultApplicationUserShouldNotBeFound("birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where birthDate is less than or equal to DEFAULT_BIRTH_DATE
        defaultApplicationUserShouldBeFound("birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the applicationUserList where birthDate is less than or equal to SMALLER_BIRTH_DATE
        defaultApplicationUserShouldNotBeFound("birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where birthDate is less than DEFAULT_BIRTH_DATE
        defaultApplicationUserShouldNotBeFound("birthDate.lessThan=" + DEFAULT_BIRTH_DATE);

        // Get all the applicationUserList where birthDate is less than UPDATED_BIRTH_DATE
        defaultApplicationUserShouldBeFound("birthDate.lessThan=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where birthDate is greater than DEFAULT_BIRTH_DATE
        defaultApplicationUserShouldNotBeFound("birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);

        // Get all the applicationUserList where birthDate is greater than SMALLER_BIRTH_DATE
        defaultApplicationUserShouldBeFound("birthDate.greaterThan=" + SMALLER_BIRTH_DATE);
    }


    @Test
    @Transactional
    public void getAllApplicationUsersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultApplicationUserShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the applicationUserList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultApplicationUserShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultApplicationUserShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the applicationUserList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultApplicationUserShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultApplicationUserShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the applicationUserList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultApplicationUserShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where phoneNumber is not null
        defaultApplicationUserShouldBeFound("phoneNumber.specified=true");

        // Get all the applicationUserList where phoneNumber is null
        defaultApplicationUserShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllApplicationUsersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultApplicationUserShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the applicationUserList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultApplicationUserShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultApplicationUserShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the applicationUserList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultApplicationUserShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllApplicationUsersByAdminIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where admin equals to DEFAULT_ADMIN
        defaultApplicationUserShouldBeFound("admin.equals=" + DEFAULT_ADMIN);

        // Get all the applicationUserList where admin equals to UPDATED_ADMIN
        defaultApplicationUserShouldNotBeFound("admin.equals=" + UPDATED_ADMIN);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByAdminIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where admin not equals to DEFAULT_ADMIN
        defaultApplicationUserShouldNotBeFound("admin.notEquals=" + DEFAULT_ADMIN);

        // Get all the applicationUserList where admin not equals to UPDATED_ADMIN
        defaultApplicationUserShouldBeFound("admin.notEquals=" + UPDATED_ADMIN);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByAdminIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where admin in DEFAULT_ADMIN or UPDATED_ADMIN
        defaultApplicationUserShouldBeFound("admin.in=" + DEFAULT_ADMIN + "," + UPDATED_ADMIN);

        // Get all the applicationUserList where admin equals to UPDATED_ADMIN
        defaultApplicationUserShouldNotBeFound("admin.in=" + UPDATED_ADMIN);
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByAdminIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where admin is not null
        defaultApplicationUserShouldBeFound("admin.specified=true");

        // Get all the applicationUserList where admin is null
        defaultApplicationUserShouldNotBeFound("admin.specified=false");
    }

    @Test
    @Transactional
    public void getAllApplicationUsersByInternalUserIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);
        User internalUser = UserResourceIT.createEntity(em);
        em.persist(internalUser);
        em.flush();
        applicationUser.setInternalUser(internalUser);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long internalUserId = internalUser.getId();

        // Get all the applicationUserList where internalUser equals to internalUserId
        defaultApplicationUserShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the applicationUserList where internalUser equals to internalUserId + 1
        defaultApplicationUserShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }


    @Test
    @Transactional
    public void getAllApplicationUsersByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);
        PaymentMethod paymentMethod = PaymentMethodResourceIT.createEntity(em);
        em.persist(paymentMethod);
        em.flush();
        applicationUser.addPaymentMethod(paymentMethod);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long paymentMethodId = paymentMethod.getId();

        // Get all the applicationUserList where paymentMethod equals to paymentMethodId
        defaultApplicationUserShouldBeFound("paymentMethodId.equals=" + paymentMethodId);

        // Get all the applicationUserList where paymentMethod equals to paymentMethodId + 1
        defaultApplicationUserShouldNotBeFound("paymentMethodId.equals=" + (paymentMethodId + 1));
    }


    @Test
    @Transactional
    public void getAllApplicationUsersByProyectIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);
        Proyect proyect = ProyectResourceIT.createEntity(em);
        em.persist(proyect);
        em.flush();
        applicationUser.addProyect(proyect);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long proyectId = proyect.getId();

        // Get all the applicationUserList where proyect equals to proyectId
        defaultApplicationUserShouldBeFound("proyectId.equals=" + proyectId);

        // Get all the applicationUserList where proyect equals to proyectId + 1
        defaultApplicationUserShouldNotBeFound("proyectId.equals=" + (proyectId + 1));
    }


    @Test
    @Transactional
    public void getAllApplicationUsersByNotificationIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);
        Notification notification = NotificationResourceIT.createEntity(em);
        em.persist(notification);
        em.flush();
        applicationUser.addNotification(notification);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long notificationId = notification.getId();

        // Get all the applicationUserList where notification equals to notificationId
        defaultApplicationUserShouldBeFound("notificationId.equals=" + notificationId);

        // Get all the applicationUserList where notification equals to notificationId + 1
        defaultApplicationUserShouldNotBeFound("notificationId.equals=" + (notificationId + 1));
    }


    @Test
    @Transactional
    public void getAllApplicationUsersByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);
        Payment payment = PaymentResourceIT.createEntity(em);
        em.persist(payment);
        em.flush();
        applicationUser.addPayment(payment);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long paymentId = payment.getId();

        // Get all the applicationUserList where payment equals to paymentId
        defaultApplicationUserShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the applicationUserList where payment equals to paymentId + 1
        defaultApplicationUserShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }


    @Test
    @Transactional
    public void getAllApplicationUsersByFavoriteIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);
        Proyect favorite = ProyectResourceIT.createEntity(em);
        em.persist(favorite);
        em.flush();
        applicationUser.addFavorite(favorite);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long favoriteId = favorite.getId();

        // Get all the applicationUserList where favorite equals to favoriteId
        defaultApplicationUserShouldBeFound("favoriteId.equals=" + favoriteId);

        // Get all the applicationUserList where favorite equals to favoriteId + 1
        defaultApplicationUserShouldNotBeFound("favoriteId.equals=" + (favoriteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultApplicationUserShouldBeFound(String filter) throws Exception {
        restApplicationUserMockMvc.perform(get("/api/application-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].identification").value(hasItem(DEFAULT_IDENTIFICATION)))
            .andExpect(jsonPath("$.[*].idType").value(hasItem(DEFAULT_ID_TYPE.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(sameInstant(DEFAULT_BIRTH_DATE))))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].admin").value(hasItem(DEFAULT_ADMIN.booleanValue())));

        // Check, that the count call also returns 1
        restApplicationUserMockMvc.perform(get("/api/application-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultApplicationUserShouldNotBeFound(String filter) throws Exception {
        restApplicationUserMockMvc.perform(get("/api/application-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApplicationUserMockMvc.perform(get("/api/application-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingApplicationUser() throws Exception {
        // Get the applicationUser
        restApplicationUserMockMvc.perform(get("/api/application-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicationUser() throws Exception {
        // Initialize the database
        applicationUserService.save(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser
        ApplicationUser updatedApplicationUser = applicationUserRepository.findById(applicationUser.getId()).get();
        // Disconnect from session so that the updates on updatedApplicationUser are not directly saved in db
        em.detach(updatedApplicationUser);
        updatedApplicationUser
            .identification(UPDATED_IDENTIFICATION)
            .idType(UPDATED_ID_TYPE)
            .birthDate(UPDATED_BIRTH_DATE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .admin(UPDATED_ADMIN);

        restApplicationUserMockMvc.perform(put("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedApplicationUser)))
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getIdentification()).isEqualTo(UPDATED_IDENTIFICATION);
        assertThat(testApplicationUser.getIdType()).isEqualTo(UPDATED_ID_TYPE);
        assertThat(testApplicationUser.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testApplicationUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testApplicationUser.isAdmin()).isEqualTo(UPDATED_ADMIN);
    }

    @Test
    @Transactional
    public void updateNonExistingApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc.perform(put("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApplicationUser() throws Exception {
        // Initialize the database
        applicationUserService.save(applicationUser);

        int databaseSizeBeforeDelete = applicationUserRepository.findAll().size();

        // Delete the applicationUser
        restApplicationUserMockMvc.perform(delete("/api/application-users/{id}", applicationUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
