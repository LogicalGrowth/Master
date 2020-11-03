package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Raffle;
import cr.ac.ucenfotec.fun4fund.repository.RaffleRepository;

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

import cr.ac.ucenfotec.fun4fund.domain.enumeration.ActivityStatus;
/**
 * Integration tests for the {@link RaffleResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RaffleResourceIT {

    private static final Double DEFAULT_PRICE = 0D;
    private static final Double UPDATED_PRICE = 1D;

    private static final Integer DEFAULT_TOTAL_TICKET = 1;
    private static final Integer UPDATED_TOTAL_TICKET = 2;

    private static final ZonedDateTime DEFAULT_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ActivityStatus DEFAULT_STATE = ActivityStatus.ENABLED;
    private static final ActivityStatus UPDATED_STATE = ActivityStatus.DISABLED;

    @Autowired
    private RaffleRepository raffleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRaffleMockMvc;

    private Raffle raffle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Raffle createEntity(EntityManager em) {
        Raffle raffle = new Raffle()
            .price(DEFAULT_PRICE)
            .totalTicket(DEFAULT_TOTAL_TICKET)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .state(DEFAULT_STATE);
        return raffle;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Raffle createUpdatedEntity(EntityManager em) {
        Raffle raffle = new Raffle()
            .price(UPDATED_PRICE)
            .totalTicket(UPDATED_TOTAL_TICKET)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .state(UPDATED_STATE);
        return raffle;
    }

    @BeforeEach
    public void initTest() {
        raffle = createEntity(em);
    }

    @Test
    @Transactional
    public void createRaffle() throws Exception {
        int databaseSizeBeforeCreate = raffleRepository.findAll().size();
        // Create the Raffle
        restRaffleMockMvc.perform(post("/api/raffles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(raffle)))
            .andExpect(status().isCreated());

        // Validate the Raffle in the database
        List<Raffle> raffleList = raffleRepository.findAll();
        assertThat(raffleList).hasSize(databaseSizeBeforeCreate + 1);
        Raffle testRaffle = raffleList.get(raffleList.size() - 1);
        assertThat(testRaffle.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testRaffle.getTotalTicket()).isEqualTo(DEFAULT_TOTAL_TICKET);
        assertThat(testRaffle.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testRaffle.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createRaffleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = raffleRepository.findAll().size();

        // Create the Raffle with an existing ID
        raffle.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRaffleMockMvc.perform(post("/api/raffles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(raffle)))
            .andExpect(status().isBadRequest());

        // Validate the Raffle in the database
        List<Raffle> raffleList = raffleRepository.findAll();
        assertThat(raffleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = raffleRepository.findAll().size();
        // set the field null
        raffle.setPrice(null);

        // Create the Raffle, which fails.


        restRaffleMockMvc.perform(post("/api/raffles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(raffle)))
            .andExpect(status().isBadRequest());

        List<Raffle> raffleList = raffleRepository.findAll();
        assertThat(raffleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalTicketIsRequired() throws Exception {
        int databaseSizeBeforeTest = raffleRepository.findAll().size();
        // set the field null
        raffle.setTotalTicket(null);

        // Create the Raffle, which fails.


        restRaffleMockMvc.perform(post("/api/raffles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(raffle)))
            .andExpect(status().isBadRequest());

        List<Raffle> raffleList = raffleRepository.findAll();
        assertThat(raffleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpirationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = raffleRepository.findAll().size();
        // set the field null
        raffle.setExpirationDate(null);

        // Create the Raffle, which fails.


        restRaffleMockMvc.perform(post("/api/raffles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(raffle)))
            .andExpect(status().isBadRequest());

        List<Raffle> raffleList = raffleRepository.findAll();
        assertThat(raffleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = raffleRepository.findAll().size();
        // set the field null
        raffle.setState(null);

        // Create the Raffle, which fails.


        restRaffleMockMvc.perform(post("/api/raffles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(raffle)))
            .andExpect(status().isBadRequest());

        List<Raffle> raffleList = raffleRepository.findAll();
        assertThat(raffleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRaffles() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList
        restRaffleMockMvc.perform(get("/api/raffles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raffle.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalTicket").value(hasItem(DEFAULT_TOTAL_TICKET)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(sameInstant(DEFAULT_EXPIRATION_DATE))))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
    
    @Test
    @Transactional
    public void getRaffle() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get the raffle
        restRaffleMockMvc.perform(get("/api/raffles/{id}", raffle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(raffle.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.totalTicket").value(DEFAULT_TOTAL_TICKET))
            .andExpect(jsonPath("$.expirationDate").value(sameInstant(DEFAULT_EXPIRATION_DATE)))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingRaffle() throws Exception {
        // Get the raffle
        restRaffleMockMvc.perform(get("/api/raffles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRaffle() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        int databaseSizeBeforeUpdate = raffleRepository.findAll().size();

        // Update the raffle
        Raffle updatedRaffle = raffleRepository.findById(raffle.getId()).get();
        // Disconnect from session so that the updates on updatedRaffle are not directly saved in db
        em.detach(updatedRaffle);
        updatedRaffle
            .price(UPDATED_PRICE)
            .totalTicket(UPDATED_TOTAL_TICKET)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .state(UPDATED_STATE);

        restRaffleMockMvc.perform(put("/api/raffles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRaffle)))
            .andExpect(status().isOk());

        // Validate the Raffle in the database
        List<Raffle> raffleList = raffleRepository.findAll();
        assertThat(raffleList).hasSize(databaseSizeBeforeUpdate);
        Raffle testRaffle = raffleList.get(raffleList.size() - 1);
        assertThat(testRaffle.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testRaffle.getTotalTicket()).isEqualTo(UPDATED_TOTAL_TICKET);
        assertThat(testRaffle.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testRaffle.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    public void updateNonExistingRaffle() throws Exception {
        int databaseSizeBeforeUpdate = raffleRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRaffleMockMvc.perform(put("/api/raffles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(raffle)))
            .andExpect(status().isBadRequest());

        // Validate the Raffle in the database
        List<Raffle> raffleList = raffleRepository.findAll();
        assertThat(raffleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRaffle() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        int databaseSizeBeforeDelete = raffleRepository.findAll().size();

        // Delete the raffle
        restRaffleMockMvc.perform(delete("/api/raffles/{id}", raffle.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Raffle> raffleList = raffleRepository.findAll();
        assertThat(raffleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
