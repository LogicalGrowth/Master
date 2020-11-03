package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.repository.ProyectRepository;

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

    private static final Double DEFAULT_COLLECTED = 0D;
    private static final Double UPDATED_COLLECTED = 1D;

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_COORD_X = 1L;
    private static final Long UPDATED_COORD_X = 2L;

    private static final Long DEFAULT_COORD_Y = 1L;
    private static final Long UPDATED_COORD_Y = 2L;

    private static final Double DEFAULT_FEE = 1D;
    private static final Double UPDATED_FEE = 2D;

    @Autowired
    private ProyectRepository proyectRepository;

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
            .fee(DEFAULT_FEE);
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
            .fee(UPDATED_FEE);
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
            .andExpect(jsonPath("$.[*].coordX").value(hasItem(DEFAULT_COORD_X.intValue())))
            .andExpect(jsonPath("$.[*].coordY").value(hasItem(DEFAULT_COORD_Y.intValue())))
            .andExpect(jsonPath("$.[*].fee").value(hasItem(DEFAULT_FEE.doubleValue())));
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
            .andExpect(jsonPath("$.coordX").value(DEFAULT_COORD_X.intValue()))
            .andExpect(jsonPath("$.coordY").value(DEFAULT_COORD_Y.intValue()))
            .andExpect(jsonPath("$.fee").value(DEFAULT_FEE.doubleValue()));
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
        proyectRepository.saveAndFlush(proyect);

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
            .fee(UPDATED_FEE);

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
        proyectRepository.saveAndFlush(proyect);

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
