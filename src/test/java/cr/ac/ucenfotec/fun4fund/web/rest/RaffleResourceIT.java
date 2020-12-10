package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Raffle;
import cr.ac.ucenfotec.fun4fund.domain.Prize;
import cr.ac.ucenfotec.fun4fund.domain.Ticket;
import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.repository.RaffleRepository;
import cr.ac.ucenfotec.fun4fund.service.RaffleService;
import cr.ac.ucenfotec.fun4fund.service.dto.RaffleCriteria;
import cr.ac.ucenfotec.fun4fund.service.RaffleQueryService;

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
    private static final Double SMALLER_PRICE = 0D - 1D;

    private static final Integer DEFAULT_TOTAL_TICKET = 1;
    private static final Integer UPDATED_TOTAL_TICKET = 2;
    private static final Integer SMALLER_TOTAL_TICKET = 1 - 1;

    private static final ZonedDateTime DEFAULT_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ActivityStatus DEFAULT_STATE = ActivityStatus.ENABLED;
    private static final ActivityStatus UPDATED_STATE = ActivityStatus.DISABLED;

    @Autowired
    private RaffleRepository raffleRepository;

    @Autowired
    private RaffleService raffleService;

    @Autowired
    private RaffleQueryService raffleQueryService;

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
    public void getRafflesByIdFiltering() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        Long id = raffle.getId();

        defaultRaffleShouldBeFound("id.equals=" + id);
        defaultRaffleShouldNotBeFound("id.notEquals=" + id);

        defaultRaffleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRaffleShouldNotBeFound("id.greaterThan=" + id);

        defaultRaffleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRaffleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRafflesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where price equals to DEFAULT_PRICE
        defaultRaffleShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the raffleList where price equals to UPDATED_PRICE
        defaultRaffleShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllRafflesByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where price not equals to DEFAULT_PRICE
        defaultRaffleShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the raffleList where price not equals to UPDATED_PRICE
        defaultRaffleShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllRafflesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultRaffleShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the raffleList where price equals to UPDATED_PRICE
        defaultRaffleShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllRafflesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where price is not null
        defaultRaffleShouldBeFound("price.specified=true");

        // Get all the raffleList where price is null
        defaultRaffleShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllRafflesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where price is greater than or equal to DEFAULT_PRICE
        defaultRaffleShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the raffleList where price is greater than or equal to UPDATED_PRICE
        defaultRaffleShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllRafflesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where price is less than or equal to DEFAULT_PRICE
        defaultRaffleShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the raffleList where price is less than or equal to SMALLER_PRICE
        defaultRaffleShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllRafflesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where price is less than DEFAULT_PRICE
        defaultRaffleShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the raffleList where price is less than UPDATED_PRICE
        defaultRaffleShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllRafflesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where price is greater than DEFAULT_PRICE
        defaultRaffleShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the raffleList where price is greater than SMALLER_PRICE
        defaultRaffleShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }


    @Test
    @Transactional
    public void getAllRafflesByTotalTicketIsEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where totalTicket equals to DEFAULT_TOTAL_TICKET
        defaultRaffleShouldBeFound("totalTicket.equals=" + DEFAULT_TOTAL_TICKET);

        // Get all the raffleList where totalTicket equals to UPDATED_TOTAL_TICKET
        defaultRaffleShouldNotBeFound("totalTicket.equals=" + UPDATED_TOTAL_TICKET);
    }

    @Test
    @Transactional
    public void getAllRafflesByTotalTicketIsNotEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where totalTicket not equals to DEFAULT_TOTAL_TICKET
        defaultRaffleShouldNotBeFound("totalTicket.notEquals=" + DEFAULT_TOTAL_TICKET);

        // Get all the raffleList where totalTicket not equals to UPDATED_TOTAL_TICKET
        defaultRaffleShouldBeFound("totalTicket.notEquals=" + UPDATED_TOTAL_TICKET);
    }

    @Test
    @Transactional
    public void getAllRafflesByTotalTicketIsInShouldWork() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where totalTicket in DEFAULT_TOTAL_TICKET or UPDATED_TOTAL_TICKET
        defaultRaffleShouldBeFound("totalTicket.in=" + DEFAULT_TOTAL_TICKET + "," + UPDATED_TOTAL_TICKET);

        // Get all the raffleList where totalTicket equals to UPDATED_TOTAL_TICKET
        defaultRaffleShouldNotBeFound("totalTicket.in=" + UPDATED_TOTAL_TICKET);
    }

    @Test
    @Transactional
    public void getAllRafflesByTotalTicketIsNullOrNotNull() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where totalTicket is not null
        defaultRaffleShouldBeFound("totalTicket.specified=true");

        // Get all the raffleList where totalTicket is null
        defaultRaffleShouldNotBeFound("totalTicket.specified=false");
    }

    @Test
    @Transactional
    public void getAllRafflesByTotalTicketIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where totalTicket is greater than or equal to DEFAULT_TOTAL_TICKET
        defaultRaffleShouldBeFound("totalTicket.greaterThanOrEqual=" + DEFAULT_TOTAL_TICKET);

        // Get all the raffleList where totalTicket is greater than or equal to UPDATED_TOTAL_TICKET
        defaultRaffleShouldNotBeFound("totalTicket.greaterThanOrEqual=" + UPDATED_TOTAL_TICKET);
    }

    @Test
    @Transactional
    public void getAllRafflesByTotalTicketIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where totalTicket is less than or equal to DEFAULT_TOTAL_TICKET
        defaultRaffleShouldBeFound("totalTicket.lessThanOrEqual=" + DEFAULT_TOTAL_TICKET);

        // Get all the raffleList where totalTicket is less than or equal to SMALLER_TOTAL_TICKET
        defaultRaffleShouldNotBeFound("totalTicket.lessThanOrEqual=" + SMALLER_TOTAL_TICKET);
    }

    @Test
    @Transactional
    public void getAllRafflesByTotalTicketIsLessThanSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where totalTicket is less than DEFAULT_TOTAL_TICKET
        defaultRaffleShouldNotBeFound("totalTicket.lessThan=" + DEFAULT_TOTAL_TICKET);

        // Get all the raffleList where totalTicket is less than UPDATED_TOTAL_TICKET
        defaultRaffleShouldBeFound("totalTicket.lessThan=" + UPDATED_TOTAL_TICKET);
    }

    @Test
    @Transactional
    public void getAllRafflesByTotalTicketIsGreaterThanSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where totalTicket is greater than DEFAULT_TOTAL_TICKET
        defaultRaffleShouldNotBeFound("totalTicket.greaterThan=" + DEFAULT_TOTAL_TICKET);

        // Get all the raffleList where totalTicket is greater than SMALLER_TOTAL_TICKET
        defaultRaffleShouldBeFound("totalTicket.greaterThan=" + SMALLER_TOTAL_TICKET);
    }


    @Test
    @Transactional
    public void getAllRafflesByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where expirationDate equals to DEFAULT_EXPIRATION_DATE
        defaultRaffleShouldBeFound("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the raffleList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultRaffleShouldNotBeFound("expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllRafflesByExpirationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where expirationDate not equals to DEFAULT_EXPIRATION_DATE
        defaultRaffleShouldNotBeFound("expirationDate.notEquals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the raffleList where expirationDate not equals to UPDATED_EXPIRATION_DATE
        defaultRaffleShouldBeFound("expirationDate.notEquals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllRafflesByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where expirationDate in DEFAULT_EXPIRATION_DATE or UPDATED_EXPIRATION_DATE
        defaultRaffleShouldBeFound("expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE);

        // Get all the raffleList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultRaffleShouldNotBeFound("expirationDate.in=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllRafflesByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where expirationDate is not null
        defaultRaffleShouldBeFound("expirationDate.specified=true");

        // Get all the raffleList where expirationDate is null
        defaultRaffleShouldNotBeFound("expirationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRafflesByExpirationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where expirationDate is greater than or equal to DEFAULT_EXPIRATION_DATE
        defaultRaffleShouldBeFound("expirationDate.greaterThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the raffleList where expirationDate is greater than or equal to UPDATED_EXPIRATION_DATE
        defaultRaffleShouldNotBeFound("expirationDate.greaterThanOrEqual=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllRafflesByExpirationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where expirationDate is less than or equal to DEFAULT_EXPIRATION_DATE
        defaultRaffleShouldBeFound("expirationDate.lessThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the raffleList where expirationDate is less than or equal to SMALLER_EXPIRATION_DATE
        defaultRaffleShouldNotBeFound("expirationDate.lessThanOrEqual=" + SMALLER_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllRafflesByExpirationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where expirationDate is less than DEFAULT_EXPIRATION_DATE
        defaultRaffleShouldNotBeFound("expirationDate.lessThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the raffleList where expirationDate is less than UPDATED_EXPIRATION_DATE
        defaultRaffleShouldBeFound("expirationDate.lessThan=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllRafflesByExpirationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where expirationDate is greater than DEFAULT_EXPIRATION_DATE
        defaultRaffleShouldNotBeFound("expirationDate.greaterThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the raffleList where expirationDate is greater than SMALLER_EXPIRATION_DATE
        defaultRaffleShouldBeFound("expirationDate.greaterThan=" + SMALLER_EXPIRATION_DATE);
    }


    @Test
    @Transactional
    public void getAllRafflesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where state equals to DEFAULT_STATE
        defaultRaffleShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the raffleList where state equals to UPDATED_STATE
        defaultRaffleShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllRafflesByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where state not equals to DEFAULT_STATE
        defaultRaffleShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the raffleList where state not equals to UPDATED_STATE
        defaultRaffleShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllRafflesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where state in DEFAULT_STATE or UPDATED_STATE
        defaultRaffleShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the raffleList where state equals to UPDATED_STATE
        defaultRaffleShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllRafflesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);

        // Get all the raffleList where state is not null
        defaultRaffleShouldBeFound("state.specified=true");

        // Get all the raffleList where state is null
        defaultRaffleShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllRafflesByPrizeIsEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);
        Prize prize = PrizeResourceIT.createEntity(em);
        em.persist(prize);
        em.flush();
        raffle.setPrize(prize);
        raffleRepository.saveAndFlush(raffle);
        Long prizeId = prize.getId();

        // Get all the raffleList where prize equals to prizeId
        defaultRaffleShouldBeFound("prizeId.equals=" + prizeId);

        // Get all the raffleList where prize equals to prizeId + 1
        defaultRaffleShouldNotBeFound("prizeId.equals=" + (prizeId + 1));
    }


    @Test
    @Transactional
    public void getAllRafflesByTicketIsEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);
        Ticket ticket = TicketResourceIT.createEntity(em);
        em.persist(ticket);
        em.flush();
        raffle.addTicket(ticket);
        raffleRepository.saveAndFlush(raffle);
        Long ticketId = ticket.getId();

        // Get all the raffleList where ticket equals to ticketId
        defaultRaffleShouldBeFound("ticketId.equals=" + ticketId);

        // Get all the raffleList where ticket equals to ticketId + 1
        defaultRaffleShouldNotBeFound("ticketId.equals=" + (ticketId + 1));
    }


    @Test
    @Transactional
    public void getAllRafflesByBuyerIsEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);
        ApplicationUser buyer = ApplicationUserResourceIT.createEntity(em);
        em.persist(buyer);
        em.flush();
        raffle.setBuyer(buyer);
        raffleRepository.saveAndFlush(raffle);
        Long buyerId = buyer.getId();

        // Get all the raffleList where buyer equals to buyerId
        defaultRaffleShouldBeFound("buyerId.equals=" + buyerId);

        // Get all the raffleList where buyer equals to buyerId + 1
        defaultRaffleShouldNotBeFound("buyerId.equals=" + (buyerId + 1));
    }


    @Test
    @Transactional
    public void getAllRafflesByProyectIsEqualToSomething() throws Exception {
        // Initialize the database
        raffleRepository.saveAndFlush(raffle);
        Proyect proyect = ProyectResourceIT.createEntity(em);
        em.persist(proyect);
        em.flush();
        raffle.setProyect(proyect);
        raffleRepository.saveAndFlush(raffle);
        Long proyectId = proyect.getId();

        // Get all the raffleList where proyect equals to proyectId
        defaultRaffleShouldBeFound("proyectId.equals=" + proyectId);

        // Get all the raffleList where proyect equals to proyectId + 1
        defaultRaffleShouldNotBeFound("proyectId.equals=" + (proyectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRaffleShouldBeFound(String filter) throws Exception {
        restRaffleMockMvc.perform(get("/api/raffles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raffle.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalTicket").value(hasItem(DEFAULT_TOTAL_TICKET)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(sameInstant(DEFAULT_EXPIRATION_DATE))))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));

        // Check, that the count call also returns 1
        restRaffleMockMvc.perform(get("/api/raffles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRaffleShouldNotBeFound(String filter) throws Exception {
        restRaffleMockMvc.perform(get("/api/raffles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRaffleMockMvc.perform(get("/api/raffles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        raffleService.save(raffle);

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
        raffleService.save(raffle);

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
