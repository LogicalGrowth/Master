package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.domain.Resource;
import cr.ac.ucenfotec.fun4fund.domain.Checkpoint;
import cr.ac.ucenfotec.fun4fund.domain.Review;
import cr.ac.ucenfotec.fun4fund.domain.PartnerRequest;
import cr.ac.ucenfotec.fun4fund.domain.Raffle;
import cr.ac.ucenfotec.fun4fund.domain.Auction;
import cr.ac.ucenfotec.fun4fund.domain.ExclusiveContent;
import cr.ac.ucenfotec.fun4fund.domain.Payment;
import cr.ac.ucenfotec.fun4fund.domain.Favorite;
import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.domain.Category;
import cr.ac.ucenfotec.fun4fund.repository.ProyectRepository;
import cr.ac.ucenfotec.fun4fund.service.ProyectService;
import cr.ac.ucenfotec.fun4fund.service.dto.ProyectCriteria;
import cr.ac.ucenfotec.fun4fund.service.ProyectQueryService;

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

import cr.ac.ucenfotec.fun4fund.domain.enumeration.ProyectType;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.Currency;
/**
 * Integration tests for the {@link ProyectResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProyectResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ProyectType DEFAULT_ID_TYPE = ProyectType.PROFITABLE;
    private static final ProyectType UPDATED_ID_TYPE = ProyectType.NONPROFIT;

    private static final Double DEFAULT_GOAL_AMOUNT = 1D;
    private static final Double UPDATED_GOAL_AMOUNT = 2D;
    private static final Double SMALLER_GOAL_AMOUNT = 1D - 1D;

    private static final Double DEFAULT_COLLECTED = 0D;
    private static final Double UPDATED_COLLECTED = 1D;
    private static final Double SMALLER_COLLECTED = 0D - 1D;

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;
    private static final Double SMALLER_RATING = 1D - 1D;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Double DEFAULT_COORD_X = 1D;
    private static final Double UPDATED_COORD_X = 2D;
    private static final Double SMALLER_COORD_X = 1D - 1D;

    private static final Double DEFAULT_COORD_Y = 1D;
    private static final Double UPDATED_COORD_Y = 2D;
    private static final Double SMALLER_COORD_Y = 1D - 1D;

    private static final Double DEFAULT_FEE = 1D;
    private static final Double UPDATED_FEE = 2D;
    private static final Double SMALLER_FEE = 1D - 1D;

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final Currency DEFAULT_CURRENCY_TYPE = Currency.USD;
    private static final Currency UPDATED_CURRENCY_TYPE = Currency.CRC;

    @Autowired
    private ProyectRepository proyectRepository;

    @Autowired
    private ProyectService proyectService;

    @Autowired
    private ProyectQueryService proyectQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProyectMockMvc;

    private Proyect proyect;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proyect createEntity(EntityManager em) {
        Proyect proyect = new Proyect()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .idType(DEFAULT_ID_TYPE)
            .goalAmount(DEFAULT_GOAL_AMOUNT)
            .collected(DEFAULT_COLLECTED)
            .rating(DEFAULT_RATING)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .coordX(DEFAULT_COORD_X)
            .coordY(DEFAULT_COORD_Y)
            .fee(DEFAULT_FEE)
            .number(DEFAULT_NUMBER)
            .currencyType(DEFAULT_CURRENCY_TYPE);
        return proyect;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proyect createUpdatedEntity(EntityManager em) {
        Proyect proyect = new Proyect()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .idType(UPDATED_ID_TYPE)
            .goalAmount(UPDATED_GOAL_AMOUNT)
            .collected(UPDATED_COLLECTED)
            .rating(UPDATED_RATING)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .coordX(UPDATED_COORD_X)
            .coordY(UPDATED_COORD_Y)
            .fee(UPDATED_FEE)
            .number(UPDATED_NUMBER)
            .currencyType(UPDATED_CURRENCY_TYPE);
        return proyect;
    }

    @BeforeEach
    public void initTest() {
        proyect = createEntity(em);
    }

    @Test
    @Transactional
    public void createProyect() throws Exception {
        int databaseSizeBeforeCreate = proyectRepository.findAll().size();
        // Create the Proyect
        restProyectMockMvc.perform(post("/api/proyects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyect)))
            .andExpect(status().isCreated());

        // Validate the Proyect in the database
        List<Proyect> proyectList = proyectRepository.findAll();
        assertThat(proyectList).hasSize(databaseSizeBeforeCreate + 1);
        Proyect testProyect = proyectList.get(proyectList.size() - 1);
        assertThat(testProyect.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProyect.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProyect.getIdType()).isEqualTo(DEFAULT_ID_TYPE);
        assertThat(testProyect.getGoalAmount()).isEqualTo(DEFAULT_GOAL_AMOUNT);
        assertThat(testProyect.getCollected()).isEqualTo(DEFAULT_COLLECTED);
        assertThat(testProyect.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testProyect.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testProyect.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testProyect.getCoordX()).isEqualTo(DEFAULT_COORD_X);
        assertThat(testProyect.getCoordY()).isEqualTo(DEFAULT_COORD_Y);
        assertThat(testProyect.getFee()).isEqualTo(DEFAULT_FEE);
        assertThat(testProyect.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testProyect.getCurrencyType()).isEqualTo(DEFAULT_CURRENCY_TYPE);
    }

    @Test
    @Transactional
    public void createProyectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = proyectRepository.findAll().size();

        // Create the Proyect with an existing ID
        proyect.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProyectMockMvc.perform(post("/api/proyects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyect)))
            .andExpect(status().isBadRequest());

        // Validate the Proyect in the database
        List<Proyect> proyectList = proyectRepository.findAll();
        assertThat(proyectList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectRepository.findAll().size();
        // set the field null
        proyect.setName(null);

        // Create the Proyect, which fails.


        restProyectMockMvc.perform(post("/api/proyects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyect)))
            .andExpect(status().isBadRequest());

        List<Proyect> proyectList = proyectRepository.findAll();
        assertThat(proyectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectRepository.findAll().size();
        // set the field null
        proyect.setDescription(null);

        // Create the Proyect, which fails.


        restProyectMockMvc.perform(post("/api/proyects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyect)))
            .andExpect(status().isBadRequest());

        List<Proyect> proyectList = proyectRepository.findAll();
        assertThat(proyectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectRepository.findAll().size();
        // set the field null
        proyect.setIdType(null);

        // Create the Proyect, which fails.


        restProyectMockMvc.perform(post("/api/proyects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyect)))
            .andExpect(status().isBadRequest());

        List<Proyect> proyectList = proyectRepository.findAll();
        assertThat(proyectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGoalAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectRepository.findAll().size();
        // set the field null
        proyect.setGoalAmount(null);

        // Create the Proyect, which fails.


        restProyectMockMvc.perform(post("/api/proyects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyect)))
            .andExpect(status().isBadRequest());

        List<Proyect> proyectList = proyectRepository.findAll();
        assertThat(proyectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectRepository.findAll().size();
        // set the field null
        proyect.setCreationDate(null);

        // Create the Proyect, which fails.


        restProyectMockMvc.perform(post("/api/proyects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyect)))
            .andExpect(status().isBadRequest());

        List<Proyect> proyectList = proyectRepository.findAll();
        assertThat(proyectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCoordXIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectRepository.findAll().size();
        // set the field null
        proyect.setCoordX(null);

        // Create the Proyect, which fails.


        restProyectMockMvc.perform(post("/api/proyects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyect)))
            .andExpect(status().isBadRequest());

        List<Proyect> proyectList = proyectRepository.findAll();
        assertThat(proyectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCoordYIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectRepository.findAll().size();
        // set the field null
        proyect.setCoordY(null);

        // Create the Proyect, which fails.


        restProyectMockMvc.perform(post("/api/proyects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyect)))
            .andExpect(status().isBadRequest());

        List<Proyect> proyectList = proyectRepository.findAll();
        assertThat(proyectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFeeIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectRepository.findAll().size();
        // set the field null
        proyect.setFee(null);

        // Create the Proyect, which fails.


        restProyectMockMvc.perform(post("/api/proyects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyect)))
            .andExpect(status().isBadRequest());

        List<Proyect> proyectList = proyectRepository.findAll();
        assertThat(proyectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectRepository.findAll().size();
        // set the field null
        proyect.setNumber(null);

        // Create the Proyect, which fails.


        restProyectMockMvc.perform(post("/api/proyects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyect)))
            .andExpect(status().isBadRequest());

        List<Proyect> proyectList = proyectRepository.findAll();
        assertThat(proyectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrencyTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectRepository.findAll().size();
        // set the field null
        proyect.setCurrencyType(null);

        // Create the Proyect, which fails.


        restProyectMockMvc.perform(post("/api/proyects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyect)))
            .andExpect(status().isBadRequest());

        List<Proyect> proyectList = proyectRepository.findAll();
        assertThat(proyectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProyects() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList
        restProyectMockMvc.perform(get("/api/proyects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proyect.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].idType").value(hasItem(DEFAULT_ID_TYPE.toString())))
            .andExpect(jsonPath("$.[*].goalAmount").value(hasItem(DEFAULT_GOAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].collected").value(hasItem(DEFAULT_COLLECTED.doubleValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED))))
            .andExpect(jsonPath("$.[*].coordX").value(hasItem(DEFAULT_COORD_X.doubleValue())))
            .andExpect(jsonPath("$.[*].coordY").value(hasItem(DEFAULT_COORD_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].fee").value(hasItem(DEFAULT_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].currencyType").value(hasItem(DEFAULT_CURRENCY_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getProyect() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get the proyect
        restProyectMockMvc.perform(get("/api/proyects/{id}", proyect.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proyect.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.idType").value(DEFAULT_ID_TYPE.toString()))
            .andExpect(jsonPath("$.goalAmount").value(DEFAULT_GOAL_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.collected").value(DEFAULT_COLLECTED.doubleValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastUpdated").value(sameInstant(DEFAULT_LAST_UPDATED)))
            .andExpect(jsonPath("$.coordX").value(DEFAULT_COORD_X.doubleValue()))
            .andExpect(jsonPath("$.coordY").value(DEFAULT_COORD_Y.doubleValue()))
            .andExpect(jsonPath("$.fee").value(DEFAULT_FEE.doubleValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.currencyType").value(DEFAULT_CURRENCY_TYPE.toString()));
    }


    @Test
    @Transactional
    public void getProyectsByIdFiltering() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        Long id = proyect.getId();

        defaultProyectShouldBeFound("id.equals=" + id);
        defaultProyectShouldNotBeFound("id.notEquals=" + id);

        defaultProyectShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProyectShouldNotBeFound("id.greaterThan=" + id);

        defaultProyectShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProyectShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProyectsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where name equals to DEFAULT_NAME
        defaultProyectShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the proyectList where name equals to UPDATED_NAME
        defaultProyectShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProyectsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where name not equals to DEFAULT_NAME
        defaultProyectShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the proyectList where name not equals to UPDATED_NAME
        defaultProyectShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProyectsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProyectShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the proyectList where name equals to UPDATED_NAME
        defaultProyectShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProyectsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where name is not null
        defaultProyectShouldBeFound("name.specified=true");

        // Get all the proyectList where name is null
        defaultProyectShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllProyectsByNameContainsSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where name contains DEFAULT_NAME
        defaultProyectShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the proyectList where name contains UPDATED_NAME
        defaultProyectShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProyectsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where name does not contain DEFAULT_NAME
        defaultProyectShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the proyectList where name does not contain UPDATED_NAME
        defaultProyectShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllProyectsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where description equals to DEFAULT_DESCRIPTION
        defaultProyectShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the proyectList where description equals to UPDATED_DESCRIPTION
        defaultProyectShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProyectsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where description not equals to DEFAULT_DESCRIPTION
        defaultProyectShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the proyectList where description not equals to UPDATED_DESCRIPTION
        defaultProyectShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProyectsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProyectShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the proyectList where description equals to UPDATED_DESCRIPTION
        defaultProyectShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProyectsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where description is not null
        defaultProyectShouldBeFound("description.specified=true");

        // Get all the proyectList where description is null
        defaultProyectShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllProyectsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where description contains DEFAULT_DESCRIPTION
        defaultProyectShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the proyectList where description contains UPDATED_DESCRIPTION
        defaultProyectShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProyectsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where description does not contain DEFAULT_DESCRIPTION
        defaultProyectShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the proyectList where description does not contain UPDATED_DESCRIPTION
        defaultProyectShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllProyectsByIdTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where idType equals to DEFAULT_ID_TYPE
        defaultProyectShouldBeFound("idType.equals=" + DEFAULT_ID_TYPE);

        // Get all the proyectList where idType equals to UPDATED_ID_TYPE
        defaultProyectShouldNotBeFound("idType.equals=" + UPDATED_ID_TYPE);
    }

    @Test
    @Transactional
    public void getAllProyectsByIdTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where idType not equals to DEFAULT_ID_TYPE
        defaultProyectShouldNotBeFound("idType.notEquals=" + DEFAULT_ID_TYPE);

        // Get all the proyectList where idType not equals to UPDATED_ID_TYPE
        defaultProyectShouldBeFound("idType.notEquals=" + UPDATED_ID_TYPE);
    }

    @Test
    @Transactional
    public void getAllProyectsByIdTypeIsInShouldWork() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where idType in DEFAULT_ID_TYPE or UPDATED_ID_TYPE
        defaultProyectShouldBeFound("idType.in=" + DEFAULT_ID_TYPE + "," + UPDATED_ID_TYPE);

        // Get all the proyectList where idType equals to UPDATED_ID_TYPE
        defaultProyectShouldNotBeFound("idType.in=" + UPDATED_ID_TYPE);
    }

    @Test
    @Transactional
    public void getAllProyectsByIdTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where idType is not null
        defaultProyectShouldBeFound("idType.specified=true");

        // Get all the proyectList where idType is null
        defaultProyectShouldNotBeFound("idType.specified=false");
    }

    @Test
    @Transactional
    public void getAllProyectsByGoalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where goalAmount equals to DEFAULT_GOAL_AMOUNT
        defaultProyectShouldBeFound("goalAmount.equals=" + DEFAULT_GOAL_AMOUNT);

        // Get all the proyectList where goalAmount equals to UPDATED_GOAL_AMOUNT
        defaultProyectShouldNotBeFound("goalAmount.equals=" + UPDATED_GOAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllProyectsByGoalAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where goalAmount not equals to DEFAULT_GOAL_AMOUNT
        defaultProyectShouldNotBeFound("goalAmount.notEquals=" + DEFAULT_GOAL_AMOUNT);

        // Get all the proyectList where goalAmount not equals to UPDATED_GOAL_AMOUNT
        defaultProyectShouldBeFound("goalAmount.notEquals=" + UPDATED_GOAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllProyectsByGoalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where goalAmount in DEFAULT_GOAL_AMOUNT or UPDATED_GOAL_AMOUNT
        defaultProyectShouldBeFound("goalAmount.in=" + DEFAULT_GOAL_AMOUNT + "," + UPDATED_GOAL_AMOUNT);

        // Get all the proyectList where goalAmount equals to UPDATED_GOAL_AMOUNT
        defaultProyectShouldNotBeFound("goalAmount.in=" + UPDATED_GOAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllProyectsByGoalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where goalAmount is not null
        defaultProyectShouldBeFound("goalAmount.specified=true");

        // Get all the proyectList where goalAmount is null
        defaultProyectShouldNotBeFound("goalAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllProyectsByGoalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where goalAmount is greater than or equal to DEFAULT_GOAL_AMOUNT
        defaultProyectShouldBeFound("goalAmount.greaterThanOrEqual=" + DEFAULT_GOAL_AMOUNT);

        // Get all the proyectList where goalAmount is greater than or equal to UPDATED_GOAL_AMOUNT
        defaultProyectShouldNotBeFound("goalAmount.greaterThanOrEqual=" + UPDATED_GOAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllProyectsByGoalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where goalAmount is less than or equal to DEFAULT_GOAL_AMOUNT
        defaultProyectShouldBeFound("goalAmount.lessThanOrEqual=" + DEFAULT_GOAL_AMOUNT);

        // Get all the proyectList where goalAmount is less than or equal to SMALLER_GOAL_AMOUNT
        defaultProyectShouldNotBeFound("goalAmount.lessThanOrEqual=" + SMALLER_GOAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllProyectsByGoalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where goalAmount is less than DEFAULT_GOAL_AMOUNT
        defaultProyectShouldNotBeFound("goalAmount.lessThan=" + DEFAULT_GOAL_AMOUNT);

        // Get all the proyectList where goalAmount is less than UPDATED_GOAL_AMOUNT
        defaultProyectShouldBeFound("goalAmount.lessThan=" + UPDATED_GOAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllProyectsByGoalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where goalAmount is greater than DEFAULT_GOAL_AMOUNT
        defaultProyectShouldNotBeFound("goalAmount.greaterThan=" + DEFAULT_GOAL_AMOUNT);

        // Get all the proyectList where goalAmount is greater than SMALLER_GOAL_AMOUNT
        defaultProyectShouldBeFound("goalAmount.greaterThan=" + SMALLER_GOAL_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllProyectsByCollectedIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where collected equals to DEFAULT_COLLECTED
        defaultProyectShouldBeFound("collected.equals=" + DEFAULT_COLLECTED);

        // Get all the proyectList where collected equals to UPDATED_COLLECTED
        defaultProyectShouldNotBeFound("collected.equals=" + UPDATED_COLLECTED);
    }

    @Test
    @Transactional
    public void getAllProyectsByCollectedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where collected not equals to DEFAULT_COLLECTED
        defaultProyectShouldNotBeFound("collected.notEquals=" + DEFAULT_COLLECTED);

        // Get all the proyectList where collected not equals to UPDATED_COLLECTED
        defaultProyectShouldBeFound("collected.notEquals=" + UPDATED_COLLECTED);
    }

    @Test
    @Transactional
    public void getAllProyectsByCollectedIsInShouldWork() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where collected in DEFAULT_COLLECTED or UPDATED_COLLECTED
        defaultProyectShouldBeFound("collected.in=" + DEFAULT_COLLECTED + "," + UPDATED_COLLECTED);

        // Get all the proyectList where collected equals to UPDATED_COLLECTED
        defaultProyectShouldNotBeFound("collected.in=" + UPDATED_COLLECTED);
    }

    @Test
    @Transactional
    public void getAllProyectsByCollectedIsNullOrNotNull() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where collected is not null
        defaultProyectShouldBeFound("collected.specified=true");

        // Get all the proyectList where collected is null
        defaultProyectShouldNotBeFound("collected.specified=false");
    }

    @Test
    @Transactional
    public void getAllProyectsByCollectedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where collected is greater than or equal to DEFAULT_COLLECTED
        defaultProyectShouldBeFound("collected.greaterThanOrEqual=" + DEFAULT_COLLECTED);

        // Get all the proyectList where collected is greater than or equal to UPDATED_COLLECTED
        defaultProyectShouldNotBeFound("collected.greaterThanOrEqual=" + UPDATED_COLLECTED);
    }

    @Test
    @Transactional
    public void getAllProyectsByCollectedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where collected is less than or equal to DEFAULT_COLLECTED
        defaultProyectShouldBeFound("collected.lessThanOrEqual=" + DEFAULT_COLLECTED);

        // Get all the proyectList where collected is less than or equal to SMALLER_COLLECTED
        defaultProyectShouldNotBeFound("collected.lessThanOrEqual=" + SMALLER_COLLECTED);
    }

    @Test
    @Transactional
    public void getAllProyectsByCollectedIsLessThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where collected is less than DEFAULT_COLLECTED
        defaultProyectShouldNotBeFound("collected.lessThan=" + DEFAULT_COLLECTED);

        // Get all the proyectList where collected is less than UPDATED_COLLECTED
        defaultProyectShouldBeFound("collected.lessThan=" + UPDATED_COLLECTED);
    }

    @Test
    @Transactional
    public void getAllProyectsByCollectedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where collected is greater than DEFAULT_COLLECTED
        defaultProyectShouldNotBeFound("collected.greaterThan=" + DEFAULT_COLLECTED);

        // Get all the proyectList where collected is greater than SMALLER_COLLECTED
        defaultProyectShouldBeFound("collected.greaterThan=" + SMALLER_COLLECTED);
    }


    @Test
    @Transactional
    public void getAllProyectsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where rating equals to DEFAULT_RATING
        defaultProyectShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the proyectList where rating equals to UPDATED_RATING
        defaultProyectShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllProyectsByRatingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where rating not equals to DEFAULT_RATING
        defaultProyectShouldNotBeFound("rating.notEquals=" + DEFAULT_RATING);

        // Get all the proyectList where rating not equals to UPDATED_RATING
        defaultProyectShouldBeFound("rating.notEquals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllProyectsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultProyectShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the proyectList where rating equals to UPDATED_RATING
        defaultProyectShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllProyectsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where rating is not null
        defaultProyectShouldBeFound("rating.specified=true");

        // Get all the proyectList where rating is null
        defaultProyectShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    public void getAllProyectsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where rating is greater than or equal to DEFAULT_RATING
        defaultProyectShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the proyectList where rating is greater than or equal to UPDATED_RATING
        defaultProyectShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllProyectsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where rating is less than or equal to DEFAULT_RATING
        defaultProyectShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the proyectList where rating is less than or equal to SMALLER_RATING
        defaultProyectShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    public void getAllProyectsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where rating is less than DEFAULT_RATING
        defaultProyectShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the proyectList where rating is less than UPDATED_RATING
        defaultProyectShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllProyectsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where rating is greater than DEFAULT_RATING
        defaultProyectShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the proyectList where rating is greater than SMALLER_RATING
        defaultProyectShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }


    @Test
    @Transactional
    public void getAllProyectsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where creationDate equals to DEFAULT_CREATION_DATE
        defaultProyectShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the proyectList where creationDate equals to UPDATED_CREATION_DATE
        defaultProyectShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProyectsByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultProyectShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the proyectList where creationDate not equals to UPDATED_CREATION_DATE
        defaultProyectShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProyectsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultProyectShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the proyectList where creationDate equals to UPDATED_CREATION_DATE
        defaultProyectShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProyectsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where creationDate is not null
        defaultProyectShouldBeFound("creationDate.specified=true");

        // Get all the proyectList where creationDate is null
        defaultProyectShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProyectsByCreationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where creationDate is greater than or equal to DEFAULT_CREATION_DATE
        defaultProyectShouldBeFound("creationDate.greaterThanOrEqual=" + DEFAULT_CREATION_DATE);

        // Get all the proyectList where creationDate is greater than or equal to UPDATED_CREATION_DATE
        defaultProyectShouldNotBeFound("creationDate.greaterThanOrEqual=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProyectsByCreationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where creationDate is less than or equal to DEFAULT_CREATION_DATE
        defaultProyectShouldBeFound("creationDate.lessThanOrEqual=" + DEFAULT_CREATION_DATE);

        // Get all the proyectList where creationDate is less than or equal to SMALLER_CREATION_DATE
        defaultProyectShouldNotBeFound("creationDate.lessThanOrEqual=" + SMALLER_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProyectsByCreationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where creationDate is less than DEFAULT_CREATION_DATE
        defaultProyectShouldNotBeFound("creationDate.lessThan=" + DEFAULT_CREATION_DATE);

        // Get all the proyectList where creationDate is less than UPDATED_CREATION_DATE
        defaultProyectShouldBeFound("creationDate.lessThan=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProyectsByCreationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where creationDate is greater than DEFAULT_CREATION_DATE
        defaultProyectShouldNotBeFound("creationDate.greaterThan=" + DEFAULT_CREATION_DATE);

        // Get all the proyectList where creationDate is greater than SMALLER_CREATION_DATE
        defaultProyectShouldBeFound("creationDate.greaterThan=" + SMALLER_CREATION_DATE);
    }


    @Test
    @Transactional
    public void getAllProyectsByLastUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where lastUpdated equals to DEFAULT_LAST_UPDATED
        defaultProyectShouldBeFound("lastUpdated.equals=" + DEFAULT_LAST_UPDATED);

        // Get all the proyectList where lastUpdated equals to UPDATED_LAST_UPDATED
        defaultProyectShouldNotBeFound("lastUpdated.equals=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void getAllProyectsByLastUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where lastUpdated not equals to DEFAULT_LAST_UPDATED
        defaultProyectShouldNotBeFound("lastUpdated.notEquals=" + DEFAULT_LAST_UPDATED);

        // Get all the proyectList where lastUpdated not equals to UPDATED_LAST_UPDATED
        defaultProyectShouldBeFound("lastUpdated.notEquals=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void getAllProyectsByLastUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where lastUpdated in DEFAULT_LAST_UPDATED or UPDATED_LAST_UPDATED
        defaultProyectShouldBeFound("lastUpdated.in=" + DEFAULT_LAST_UPDATED + "," + UPDATED_LAST_UPDATED);

        // Get all the proyectList where lastUpdated equals to UPDATED_LAST_UPDATED
        defaultProyectShouldNotBeFound("lastUpdated.in=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void getAllProyectsByLastUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where lastUpdated is not null
        defaultProyectShouldBeFound("lastUpdated.specified=true");

        // Get all the proyectList where lastUpdated is null
        defaultProyectShouldNotBeFound("lastUpdated.specified=false");
    }

    @Test
    @Transactional
    public void getAllProyectsByLastUpdatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where lastUpdated is greater than or equal to DEFAULT_LAST_UPDATED
        defaultProyectShouldBeFound("lastUpdated.greaterThanOrEqual=" + DEFAULT_LAST_UPDATED);

        // Get all the proyectList where lastUpdated is greater than or equal to UPDATED_LAST_UPDATED
        defaultProyectShouldNotBeFound("lastUpdated.greaterThanOrEqual=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void getAllProyectsByLastUpdatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where lastUpdated is less than or equal to DEFAULT_LAST_UPDATED
        defaultProyectShouldBeFound("lastUpdated.lessThanOrEqual=" + DEFAULT_LAST_UPDATED);

        // Get all the proyectList where lastUpdated is less than or equal to SMALLER_LAST_UPDATED
        defaultProyectShouldNotBeFound("lastUpdated.lessThanOrEqual=" + SMALLER_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void getAllProyectsByLastUpdatedIsLessThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where lastUpdated is less than DEFAULT_LAST_UPDATED
        defaultProyectShouldNotBeFound("lastUpdated.lessThan=" + DEFAULT_LAST_UPDATED);

        // Get all the proyectList where lastUpdated is less than UPDATED_LAST_UPDATED
        defaultProyectShouldBeFound("lastUpdated.lessThan=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void getAllProyectsByLastUpdatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where lastUpdated is greater than DEFAULT_LAST_UPDATED
        defaultProyectShouldNotBeFound("lastUpdated.greaterThan=" + DEFAULT_LAST_UPDATED);

        // Get all the proyectList where lastUpdated is greater than SMALLER_LAST_UPDATED
        defaultProyectShouldBeFound("lastUpdated.greaterThan=" + SMALLER_LAST_UPDATED);
    }


    @Test
    @Transactional
    public void getAllProyectsByCoordXIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordX equals to DEFAULT_COORD_X
        defaultProyectShouldBeFound("coordX.equals=" + DEFAULT_COORD_X);

        // Get all the proyectList where coordX equals to UPDATED_COORD_X
        defaultProyectShouldNotBeFound("coordX.equals=" + UPDATED_COORD_X);
    }

    @Test
    @Transactional
    public void getAllProyectsByCoordXIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordX not equals to DEFAULT_COORD_X
        defaultProyectShouldNotBeFound("coordX.notEquals=" + DEFAULT_COORD_X);

        // Get all the proyectList where coordX not equals to UPDATED_COORD_X
        defaultProyectShouldBeFound("coordX.notEquals=" + UPDATED_COORD_X);
    }

    @Test
    @Transactional
    public void getAllProyectsByCoordXIsInShouldWork() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordX in DEFAULT_COORD_X or UPDATED_COORD_X
        defaultProyectShouldBeFound("coordX.in=" + DEFAULT_COORD_X + "," + UPDATED_COORD_X);

        // Get all the proyectList where coordX equals to UPDATED_COORD_X
        defaultProyectShouldNotBeFound("coordX.in=" + UPDATED_COORD_X);
    }

    @Test
    @Transactional
    public void getAllProyectsByCoordXIsNullOrNotNull() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordX is not null
        defaultProyectShouldBeFound("coordX.specified=true");

        // Get all the proyectList where coordX is null
        defaultProyectShouldNotBeFound("coordX.specified=false");
    }

    @Test
    @Transactional
    public void getAllProyectsByCoordXIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordX is greater than or equal to DEFAULT_COORD_X
        defaultProyectShouldBeFound("coordX.greaterThanOrEqual=" + DEFAULT_COORD_X);

        // Get all the proyectList where coordX is greater than or equal to UPDATED_COORD_X
        defaultProyectShouldNotBeFound("coordX.greaterThanOrEqual=" + UPDATED_COORD_X);
    }

    @Test
    @Transactional
    public void getAllProyectsByCoordXIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordX is less than or equal to DEFAULT_COORD_X
        defaultProyectShouldBeFound("coordX.lessThanOrEqual=" + DEFAULT_COORD_X);

        // Get all the proyectList where coordX is less than or equal to SMALLER_COORD_X
        defaultProyectShouldNotBeFound("coordX.lessThanOrEqual=" + SMALLER_COORD_X);
    }

    @Test
    @Transactional
    public void getAllProyectsByCoordXIsLessThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordX is less than DEFAULT_COORD_X
        defaultProyectShouldNotBeFound("coordX.lessThan=" + DEFAULT_COORD_X);

        // Get all the proyectList where coordX is less than UPDATED_COORD_X
        defaultProyectShouldBeFound("coordX.lessThan=" + UPDATED_COORD_X);
    }

    @Test
    @Transactional
    public void getAllProyectsByCoordXIsGreaterThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordX is greater than DEFAULT_COORD_X
        defaultProyectShouldNotBeFound("coordX.greaterThan=" + DEFAULT_COORD_X);

        // Get all the proyectList where coordX is greater than SMALLER_COORD_X
        defaultProyectShouldBeFound("coordX.greaterThan=" + SMALLER_COORD_X);
    }


    @Test
    @Transactional
    public void getAllProyectsByCoordYIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordY equals to DEFAULT_COORD_Y
        defaultProyectShouldBeFound("coordY.equals=" + DEFAULT_COORD_Y);

        // Get all the proyectList where coordY equals to UPDATED_COORD_Y
        defaultProyectShouldNotBeFound("coordY.equals=" + UPDATED_COORD_Y);
    }

    @Test
    @Transactional
    public void getAllProyectsByCoordYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordY not equals to DEFAULT_COORD_Y
        defaultProyectShouldNotBeFound("coordY.notEquals=" + DEFAULT_COORD_Y);

        // Get all the proyectList where coordY not equals to UPDATED_COORD_Y
        defaultProyectShouldBeFound("coordY.notEquals=" + UPDATED_COORD_Y);
    }

    @Test
    @Transactional
    public void getAllProyectsByCoordYIsInShouldWork() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordY in DEFAULT_COORD_Y or UPDATED_COORD_Y
        defaultProyectShouldBeFound("coordY.in=" + DEFAULT_COORD_Y + "," + UPDATED_COORD_Y);

        // Get all the proyectList where coordY equals to UPDATED_COORD_Y
        defaultProyectShouldNotBeFound("coordY.in=" + UPDATED_COORD_Y);
    }

    @Test
    @Transactional
    public void getAllProyectsByCoordYIsNullOrNotNull() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordY is not null
        defaultProyectShouldBeFound("coordY.specified=true");

        // Get all the proyectList where coordY is null
        defaultProyectShouldNotBeFound("coordY.specified=false");
    }

    @Test
    @Transactional
    public void getAllProyectsByCoordYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordY is greater than or equal to DEFAULT_COORD_Y
        defaultProyectShouldBeFound("coordY.greaterThanOrEqual=" + DEFAULT_COORD_Y);

        // Get all the proyectList where coordY is greater than or equal to UPDATED_COORD_Y
        defaultProyectShouldNotBeFound("coordY.greaterThanOrEqual=" + UPDATED_COORD_Y);
    }

    @Test
    @Transactional
    public void getAllProyectsByCoordYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordY is less than or equal to DEFAULT_COORD_Y
        defaultProyectShouldBeFound("coordY.lessThanOrEqual=" + DEFAULT_COORD_Y);

        // Get all the proyectList where coordY is less than or equal to SMALLER_COORD_Y
        defaultProyectShouldNotBeFound("coordY.lessThanOrEqual=" + SMALLER_COORD_Y);
    }

    @Test
    @Transactional
    public void getAllProyectsByCoordYIsLessThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordY is less than DEFAULT_COORD_Y
        defaultProyectShouldNotBeFound("coordY.lessThan=" + DEFAULT_COORD_Y);

        // Get all the proyectList where coordY is less than UPDATED_COORD_Y
        defaultProyectShouldBeFound("coordY.lessThan=" + UPDATED_COORD_Y);
    }

    @Test
    @Transactional
    public void getAllProyectsByCoordYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where coordY is greater than DEFAULT_COORD_Y
        defaultProyectShouldNotBeFound("coordY.greaterThan=" + DEFAULT_COORD_Y);

        // Get all the proyectList where coordY is greater than SMALLER_COORD_Y
        defaultProyectShouldBeFound("coordY.greaterThan=" + SMALLER_COORD_Y);
    }


    @Test
    @Transactional
    public void getAllProyectsByFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where fee equals to DEFAULT_FEE
        defaultProyectShouldBeFound("fee.equals=" + DEFAULT_FEE);

        // Get all the proyectList where fee equals to UPDATED_FEE
        defaultProyectShouldNotBeFound("fee.equals=" + UPDATED_FEE);
    }

    @Test
    @Transactional
    public void getAllProyectsByFeeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where fee not equals to DEFAULT_FEE
        defaultProyectShouldNotBeFound("fee.notEquals=" + DEFAULT_FEE);

        // Get all the proyectList where fee not equals to UPDATED_FEE
        defaultProyectShouldBeFound("fee.notEquals=" + UPDATED_FEE);
    }

    @Test
    @Transactional
    public void getAllProyectsByFeeIsInShouldWork() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where fee in DEFAULT_FEE or UPDATED_FEE
        defaultProyectShouldBeFound("fee.in=" + DEFAULT_FEE + "," + UPDATED_FEE);

        // Get all the proyectList where fee equals to UPDATED_FEE
        defaultProyectShouldNotBeFound("fee.in=" + UPDATED_FEE);
    }

    @Test
    @Transactional
    public void getAllProyectsByFeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where fee is not null
        defaultProyectShouldBeFound("fee.specified=true");

        // Get all the proyectList where fee is null
        defaultProyectShouldNotBeFound("fee.specified=false");
    }

    @Test
    @Transactional
    public void getAllProyectsByFeeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where fee is greater than or equal to DEFAULT_FEE
        defaultProyectShouldBeFound("fee.greaterThanOrEqual=" + DEFAULT_FEE);

        // Get all the proyectList where fee is greater than or equal to UPDATED_FEE
        defaultProyectShouldNotBeFound("fee.greaterThanOrEqual=" + UPDATED_FEE);
    }

    @Test
    @Transactional
    public void getAllProyectsByFeeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where fee is less than or equal to DEFAULT_FEE
        defaultProyectShouldBeFound("fee.lessThanOrEqual=" + DEFAULT_FEE);

        // Get all the proyectList where fee is less than or equal to SMALLER_FEE
        defaultProyectShouldNotBeFound("fee.lessThanOrEqual=" + SMALLER_FEE);
    }

    @Test
    @Transactional
    public void getAllProyectsByFeeIsLessThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where fee is less than DEFAULT_FEE
        defaultProyectShouldNotBeFound("fee.lessThan=" + DEFAULT_FEE);

        // Get all the proyectList where fee is less than UPDATED_FEE
        defaultProyectShouldBeFound("fee.lessThan=" + UPDATED_FEE);
    }

    @Test
    @Transactional
    public void getAllProyectsByFeeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where fee is greater than DEFAULT_FEE
        defaultProyectShouldNotBeFound("fee.greaterThan=" + DEFAULT_FEE);

        // Get all the proyectList where fee is greater than SMALLER_FEE
        defaultProyectShouldBeFound("fee.greaterThan=" + SMALLER_FEE);
    }


    @Test
    @Transactional
    public void getAllProyectsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where number equals to DEFAULT_NUMBER
        defaultProyectShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the proyectList where number equals to UPDATED_NUMBER
        defaultProyectShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProyectsByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where number not equals to DEFAULT_NUMBER
        defaultProyectShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the proyectList where number not equals to UPDATED_NUMBER
        defaultProyectShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProyectsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultProyectShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the proyectList where number equals to UPDATED_NUMBER
        defaultProyectShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProyectsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where number is not null
        defaultProyectShouldBeFound("number.specified=true");

        // Get all the proyectList where number is null
        defaultProyectShouldNotBeFound("number.specified=false");
    }
                @Test
    @Transactional
    public void getAllProyectsByNumberContainsSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where number contains DEFAULT_NUMBER
        defaultProyectShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the proyectList where number contains UPDATED_NUMBER
        defaultProyectShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProyectsByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where number does not contain DEFAULT_NUMBER
        defaultProyectShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the proyectList where number does not contain UPDATED_NUMBER
        defaultProyectShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }


    @Test
    @Transactional
    public void getAllProyectsByCurrencyTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where currencyType equals to DEFAULT_CURRENCY_TYPE
        defaultProyectShouldBeFound("currencyType.equals=" + DEFAULT_CURRENCY_TYPE);

        // Get all the proyectList where currencyType equals to UPDATED_CURRENCY_TYPE
        defaultProyectShouldNotBeFound("currencyType.equals=" + UPDATED_CURRENCY_TYPE);
    }

    @Test
    @Transactional
    public void getAllProyectsByCurrencyTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where currencyType not equals to DEFAULT_CURRENCY_TYPE
        defaultProyectShouldNotBeFound("currencyType.notEquals=" + DEFAULT_CURRENCY_TYPE);

        // Get all the proyectList where currencyType not equals to UPDATED_CURRENCY_TYPE
        defaultProyectShouldBeFound("currencyType.notEquals=" + UPDATED_CURRENCY_TYPE);
    }

    @Test
    @Transactional
    public void getAllProyectsByCurrencyTypeIsInShouldWork() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where currencyType in DEFAULT_CURRENCY_TYPE or UPDATED_CURRENCY_TYPE
        defaultProyectShouldBeFound("currencyType.in=" + DEFAULT_CURRENCY_TYPE + "," + UPDATED_CURRENCY_TYPE);

        // Get all the proyectList where currencyType equals to UPDATED_CURRENCY_TYPE
        defaultProyectShouldNotBeFound("currencyType.in=" + UPDATED_CURRENCY_TYPE);
    }

    @Test
    @Transactional
    public void getAllProyectsByCurrencyTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);

        // Get all the proyectList where currencyType is not null
        defaultProyectShouldBeFound("currencyType.specified=true");

        // Get all the proyectList where currencyType is null
        defaultProyectShouldNotBeFound("currencyType.specified=false");
    }

    @Test
    @Transactional
    public void getAllProyectsByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);
        Resource image = ResourceResourceIT.createEntity(em);
        em.persist(image);
        em.flush();
        proyect.addImage(image);
        proyectRepository.saveAndFlush(proyect);
        Long imageId = image.getId();

        // Get all the proyectList where image equals to imageId
        defaultProyectShouldBeFound("imageId.equals=" + imageId);

        // Get all the proyectList where image equals to imageId + 1
        defaultProyectShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }


    @Test
    @Transactional
    public void getAllProyectsByCheckpointIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);
        Checkpoint checkpoint = CheckpointResourceIT.createEntity(em);
        em.persist(checkpoint);
        em.flush();
        proyect.addCheckpoint(checkpoint);
        proyectRepository.saveAndFlush(proyect);
        Long checkpointId = checkpoint.getId();

        // Get all the proyectList where checkpoint equals to checkpointId
        defaultProyectShouldBeFound("checkpointId.equals=" + checkpointId);

        // Get all the proyectList where checkpoint equals to checkpointId + 1
        defaultProyectShouldNotBeFound("checkpointId.equals=" + (checkpointId + 1));
    }


    @Test
    @Transactional
    public void getAllProyectsByReviewIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);
        Review review = ReviewResourceIT.createEntity(em);
        em.persist(review);
        em.flush();
        proyect.addReview(review);
        proyectRepository.saveAndFlush(proyect);
        Long reviewId = review.getId();

        // Get all the proyectList where review equals to reviewId
        defaultProyectShouldBeFound("reviewId.equals=" + reviewId);

        // Get all the proyectList where review equals to reviewId + 1
        defaultProyectShouldNotBeFound("reviewId.equals=" + (reviewId + 1));
    }


    @Test
    @Transactional
    public void getAllProyectsByPartnerIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);
        PartnerRequest partner = PartnerRequestResourceIT.createEntity(em);
        em.persist(partner);
        em.flush();
        proyect.addPartner(partner);
        proyectRepository.saveAndFlush(proyect);
        Long partnerId = partner.getId();

        // Get all the proyectList where partner equals to partnerId
        defaultProyectShouldBeFound("partnerId.equals=" + partnerId);

        // Get all the proyectList where partner equals to partnerId + 1
        defaultProyectShouldNotBeFound("partnerId.equals=" + (partnerId + 1));
    }


    @Test
    @Transactional
    public void getAllProyectsByRaffleIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);
        Raffle raffle = RaffleResourceIT.createEntity(em);
        em.persist(raffle);
        em.flush();
        proyect.addRaffle(raffle);
        proyectRepository.saveAndFlush(proyect);
        Long raffleId = raffle.getId();

        // Get all the proyectList where raffle equals to raffleId
        defaultProyectShouldBeFound("raffleId.equals=" + raffleId);

        // Get all the proyectList where raffle equals to raffleId + 1
        defaultProyectShouldNotBeFound("raffleId.equals=" + (raffleId + 1));
    }


    @Test
    @Transactional
    public void getAllProyectsByAuctionIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);
        Auction auction = AuctionResourceIT.createEntity(em);
        em.persist(auction);
        em.flush();
        proyect.addAuction(auction);
        proyectRepository.saveAndFlush(proyect);
        Long auctionId = auction.getId();

        // Get all the proyectList where auction equals to auctionId
        defaultProyectShouldBeFound("auctionId.equals=" + auctionId);

        // Get all the proyectList where auction equals to auctionId + 1
        defaultProyectShouldNotBeFound("auctionId.equals=" + (auctionId + 1));
    }


    @Test
    @Transactional
    public void getAllProyectsByExclusiveContentIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);
        ExclusiveContent exclusiveContent = ExclusiveContentResourceIT.createEntity(em);
        em.persist(exclusiveContent);
        em.flush();
        proyect.addExclusiveContent(exclusiveContent);
        proyectRepository.saveAndFlush(proyect);
        Long exclusiveContentId = exclusiveContent.getId();

        // Get all the proyectList where exclusiveContent equals to exclusiveContentId
        defaultProyectShouldBeFound("exclusiveContentId.equals=" + exclusiveContentId);

        // Get all the proyectList where exclusiveContent equals to exclusiveContentId + 1
        defaultProyectShouldNotBeFound("exclusiveContentId.equals=" + (exclusiveContentId + 1));
    }


    @Test
    @Transactional
    public void getAllProyectsByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);
        Payment payment = PaymentResourceIT.createEntity(em);
        em.persist(payment);
        em.flush();
        proyect.addPayment(payment);
        proyectRepository.saveAndFlush(proyect);
        Long paymentId = payment.getId();

        // Get all the proyectList where payment equals to paymentId
        defaultProyectShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the proyectList where payment equals to paymentId + 1
        defaultProyectShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }


    @Test
    @Transactional
    public void getAllProyectsByFavoriteIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);
        Favorite favorite = FavoriteResourceIT.createEntity(em);
        em.persist(favorite);
        em.flush();
        proyect.addFavorite(favorite);
        proyectRepository.saveAndFlush(proyect);
        Long favoriteId = favorite.getId();

        // Get all the proyectList where favorite equals to favoriteId
        defaultProyectShouldBeFound("favoriteId.equals=" + favoriteId);

        // Get all the proyectList where favorite equals to favoriteId + 1
        defaultProyectShouldNotBeFound("favoriteId.equals=" + (favoriteId + 1));
    }


    @Test
    @Transactional
    public void getAllProyectsByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);
        ApplicationUser owner = ApplicationUserResourceIT.createEntity(em);
        em.persist(owner);
        em.flush();
        proyect.setOwner(owner);
        proyectRepository.saveAndFlush(proyect);
        Long ownerId = owner.getId();

        // Get all the proyectList where owner equals to ownerId
        defaultProyectShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the proyectList where owner equals to ownerId + 1
        defaultProyectShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }


    @Test
    @Transactional
    public void getAllProyectsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        proyectRepository.saveAndFlush(proyect);
        Category category = CategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        proyect.setCategory(category);
        proyectRepository.saveAndFlush(proyect);
        Long categoryId = category.getId();

        // Get all the proyectList where category equals to categoryId
        defaultProyectShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the proyectList where category equals to categoryId + 1
        defaultProyectShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProyectShouldBeFound(String filter) throws Exception {
        restProyectMockMvc.perform(get("/api/proyects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proyect.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].idType").value(hasItem(DEFAULT_ID_TYPE.toString())))
            .andExpect(jsonPath("$.[*].goalAmount").value(hasItem(DEFAULT_GOAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].collected").value(hasItem(DEFAULT_COLLECTED.doubleValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED))))
            .andExpect(jsonPath("$.[*].coordX").value(hasItem(DEFAULT_COORD_X.doubleValue())))
            .andExpect(jsonPath("$.[*].coordY").value(hasItem(DEFAULT_COORD_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].fee").value(hasItem(DEFAULT_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].currencyType").value(hasItem(DEFAULT_CURRENCY_TYPE.toString())));

        // Check, that the count call also returns 1
        restProyectMockMvc.perform(get("/api/proyects/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProyectShouldNotBeFound(String filter) throws Exception {
        restProyectMockMvc.perform(get("/api/proyects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProyectMockMvc.perform(get("/api/proyects/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingProyect() throws Exception {
        // Get the proyect
        restProyectMockMvc.perform(get("/api/proyects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProyect() throws Exception {
        // Initialize the database
        proyectService.save(proyect);

        int databaseSizeBeforeUpdate = proyectRepository.findAll().size();

        // Update the proyect
        Proyect updatedProyect = proyectRepository.findById(proyect.getId()).get();
        // Disconnect from session so that the updates on updatedProyect are not directly saved in db
        em.detach(updatedProyect);
        updatedProyect
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .idType(UPDATED_ID_TYPE)
            .goalAmount(UPDATED_GOAL_AMOUNT)
            .collected(UPDATED_COLLECTED)
            .rating(UPDATED_RATING)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .coordX(UPDATED_COORD_X)
            .coordY(UPDATED_COORD_Y)
            .fee(UPDATED_FEE)
            .number(UPDATED_NUMBER)
            .currencyType(UPDATED_CURRENCY_TYPE);

        restProyectMockMvc.perform(put("/api/proyects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProyect)))
            .andExpect(status().isOk());

        // Validate the Proyect in the database
        List<Proyect> proyectList = proyectRepository.findAll();
        assertThat(proyectList).hasSize(databaseSizeBeforeUpdate);
        Proyect testProyect = proyectList.get(proyectList.size() - 1);
        assertThat(testProyect.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProyect.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProyect.getIdType()).isEqualTo(UPDATED_ID_TYPE);
        assertThat(testProyect.getGoalAmount()).isEqualTo(UPDATED_GOAL_AMOUNT);
        assertThat(testProyect.getCollected()).isEqualTo(UPDATED_COLLECTED);
        assertThat(testProyect.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testProyect.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProyect.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testProyect.getCoordX()).isEqualTo(UPDATED_COORD_X);
        assertThat(testProyect.getCoordY()).isEqualTo(UPDATED_COORD_Y);
        assertThat(testProyect.getFee()).isEqualTo(UPDATED_FEE);
        assertThat(testProyect.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testProyect.getCurrencyType()).isEqualTo(UPDATED_CURRENCY_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingProyect() throws Exception {
        int databaseSizeBeforeUpdate = proyectRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProyectMockMvc.perform(put("/api/proyects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyect)))
            .andExpect(status().isBadRequest());

        // Validate the Proyect in the database
        List<Proyect> proyectList = proyectRepository.findAll();
        assertThat(proyectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProyect() throws Exception {
        // Initialize the database
        proyectService.save(proyect);

        int databaseSizeBeforeDelete = proyectRepository.findAll().size();

        // Delete the proyect
        restProyectMockMvc.perform(delete("/api/proyects/{id}", proyect.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Proyect> proyectList = proyectRepository.findAll();
        assertThat(proyectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
