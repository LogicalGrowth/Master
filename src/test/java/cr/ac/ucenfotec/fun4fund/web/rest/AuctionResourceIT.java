package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Auction;
import cr.ac.ucenfotec.fun4fund.domain.Prize;
import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.repository.AuctionRepository;
import cr.ac.ucenfotec.fun4fund.service.AuctionService;
import cr.ac.ucenfotec.fun4fund.service.dto.AuctionCriteria;
import cr.ac.ucenfotec.fun4fund.service.AuctionQueryService;

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
 * Integration tests for the {@link AuctionResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AuctionResourceIT {

    private static final Double DEFAULT_INITIAL_BID = 0D;
    private static final Double UPDATED_INITIAL_BID = 1D;
    private static final Double SMALLER_INITIAL_BID = 0D - 1D;

    private static final Double DEFAULT_WINNING_BID = 0D;
    private static final Double UPDATED_WINNING_BID = 1D;
    private static final Double SMALLER_WINNING_BID = 0D - 1D;

    private static final ZonedDateTime DEFAULT_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ActivityStatus DEFAULT_STATE = ActivityStatus.ENABLED;
    private static final ActivityStatus UPDATED_STATE = ActivityStatus.DISABLED;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private AuctionQueryService auctionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuctionMockMvc;

    private Auction auction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Auction createEntity(EntityManager em) {
        Auction auction = new Auction()
            .initialBid(DEFAULT_INITIAL_BID)
            .winningBid(DEFAULT_WINNING_BID)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .state(DEFAULT_STATE);
        return auction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Auction createUpdatedEntity(EntityManager em) {
        Auction auction = new Auction()
            .initialBid(UPDATED_INITIAL_BID)
            .winningBid(UPDATED_WINNING_BID)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .state(UPDATED_STATE);
        return auction;
    }

    @BeforeEach
    public void initTest() {
        auction = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuction() throws Exception {
        int databaseSizeBeforeCreate = auctionRepository.findAll().size();
        // Create the Auction
        restAuctionMockMvc.perform(post("/api/auctions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auction)))
            .andExpect(status().isCreated());

        // Validate the Auction in the database
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeCreate + 1);
        Auction testAuction = auctionList.get(auctionList.size() - 1);
        assertThat(testAuction.getInitialBid()).isEqualTo(DEFAULT_INITIAL_BID);
        assertThat(testAuction.getWinningBid()).isEqualTo(DEFAULT_WINNING_BID);
        assertThat(testAuction.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testAuction.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createAuctionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = auctionRepository.findAll().size();

        // Create the Auction with an existing ID
        auction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuctionMockMvc.perform(post("/api/auctions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auction)))
            .andExpect(status().isBadRequest());

        // Validate the Auction in the database
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkInitialBidIsRequired() throws Exception {
        int databaseSizeBeforeTest = auctionRepository.findAll().size();
        // set the field null
        auction.setInitialBid(null);

        // Create the Auction, which fails.


        restAuctionMockMvc.perform(post("/api/auctions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auction)))
            .andExpect(status().isBadRequest());

        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpirationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = auctionRepository.findAll().size();
        // set the field null
        auction.setExpirationDate(null);

        // Create the Auction, which fails.


        restAuctionMockMvc.perform(post("/api/auctions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auction)))
            .andExpect(status().isBadRequest());

        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = auctionRepository.findAll().size();
        // set the field null
        auction.setState(null);

        // Create the Auction, which fails.


        restAuctionMockMvc.perform(post("/api/auctions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auction)))
            .andExpect(status().isBadRequest());

        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuctions() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList
        restAuctionMockMvc.perform(get("/api/auctions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auction.getId().intValue())))
            .andExpect(jsonPath("$.[*].initialBid").value(hasItem(DEFAULT_INITIAL_BID.doubleValue())))
            .andExpect(jsonPath("$.[*].winningBid").value(hasItem(DEFAULT_WINNING_BID.doubleValue())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(sameInstant(DEFAULT_EXPIRATION_DATE))))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
    
    @Test
    @Transactional
    public void getAuction() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get the auction
        restAuctionMockMvc.perform(get("/api/auctions/{id}", auction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auction.getId().intValue()))
            .andExpect(jsonPath("$.initialBid").value(DEFAULT_INITIAL_BID.doubleValue()))
            .andExpect(jsonPath("$.winningBid").value(DEFAULT_WINNING_BID.doubleValue()))
            .andExpect(jsonPath("$.expirationDate").value(sameInstant(DEFAULT_EXPIRATION_DATE)))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }


    @Test
    @Transactional
    public void getAuctionsByIdFiltering() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        Long id = auction.getId();

        defaultAuctionShouldBeFound("id.equals=" + id);
        defaultAuctionShouldNotBeFound("id.notEquals=" + id);

        defaultAuctionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAuctionShouldNotBeFound("id.greaterThan=" + id);

        defaultAuctionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAuctionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAuctionsByInitialBidIsEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where initialBid equals to DEFAULT_INITIAL_BID
        defaultAuctionShouldBeFound("initialBid.equals=" + DEFAULT_INITIAL_BID);

        // Get all the auctionList where initialBid equals to UPDATED_INITIAL_BID
        defaultAuctionShouldNotBeFound("initialBid.equals=" + UPDATED_INITIAL_BID);
    }

    @Test
    @Transactional
    public void getAllAuctionsByInitialBidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where initialBid not equals to DEFAULT_INITIAL_BID
        defaultAuctionShouldNotBeFound("initialBid.notEquals=" + DEFAULT_INITIAL_BID);

        // Get all the auctionList where initialBid not equals to UPDATED_INITIAL_BID
        defaultAuctionShouldBeFound("initialBid.notEquals=" + UPDATED_INITIAL_BID);
    }

    @Test
    @Transactional
    public void getAllAuctionsByInitialBidIsInShouldWork() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where initialBid in DEFAULT_INITIAL_BID or UPDATED_INITIAL_BID
        defaultAuctionShouldBeFound("initialBid.in=" + DEFAULT_INITIAL_BID + "," + UPDATED_INITIAL_BID);

        // Get all the auctionList where initialBid equals to UPDATED_INITIAL_BID
        defaultAuctionShouldNotBeFound("initialBid.in=" + UPDATED_INITIAL_BID);
    }

    @Test
    @Transactional
    public void getAllAuctionsByInitialBidIsNullOrNotNull() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where initialBid is not null
        defaultAuctionShouldBeFound("initialBid.specified=true");

        // Get all the auctionList where initialBid is null
        defaultAuctionShouldNotBeFound("initialBid.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuctionsByInitialBidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where initialBid is greater than or equal to DEFAULT_INITIAL_BID
        defaultAuctionShouldBeFound("initialBid.greaterThanOrEqual=" + DEFAULT_INITIAL_BID);

        // Get all the auctionList where initialBid is greater than or equal to UPDATED_INITIAL_BID
        defaultAuctionShouldNotBeFound("initialBid.greaterThanOrEqual=" + UPDATED_INITIAL_BID);
    }

    @Test
    @Transactional
    public void getAllAuctionsByInitialBidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where initialBid is less than or equal to DEFAULT_INITIAL_BID
        defaultAuctionShouldBeFound("initialBid.lessThanOrEqual=" + DEFAULT_INITIAL_BID);

        // Get all the auctionList where initialBid is less than or equal to SMALLER_INITIAL_BID
        defaultAuctionShouldNotBeFound("initialBid.lessThanOrEqual=" + SMALLER_INITIAL_BID);
    }

    @Test
    @Transactional
    public void getAllAuctionsByInitialBidIsLessThanSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where initialBid is less than DEFAULT_INITIAL_BID
        defaultAuctionShouldNotBeFound("initialBid.lessThan=" + DEFAULT_INITIAL_BID);

        // Get all the auctionList where initialBid is less than UPDATED_INITIAL_BID
        defaultAuctionShouldBeFound("initialBid.lessThan=" + UPDATED_INITIAL_BID);
    }

    @Test
    @Transactional
    public void getAllAuctionsByInitialBidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where initialBid is greater than DEFAULT_INITIAL_BID
        defaultAuctionShouldNotBeFound("initialBid.greaterThan=" + DEFAULT_INITIAL_BID);

        // Get all the auctionList where initialBid is greater than SMALLER_INITIAL_BID
        defaultAuctionShouldBeFound("initialBid.greaterThan=" + SMALLER_INITIAL_BID);
    }


    @Test
    @Transactional
    public void getAllAuctionsByWinningBidIsEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where winningBid equals to DEFAULT_WINNING_BID
        defaultAuctionShouldBeFound("winningBid.equals=" + DEFAULT_WINNING_BID);

        // Get all the auctionList where winningBid equals to UPDATED_WINNING_BID
        defaultAuctionShouldNotBeFound("winningBid.equals=" + UPDATED_WINNING_BID);
    }

    @Test
    @Transactional
    public void getAllAuctionsByWinningBidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where winningBid not equals to DEFAULT_WINNING_BID
        defaultAuctionShouldNotBeFound("winningBid.notEquals=" + DEFAULT_WINNING_BID);

        // Get all the auctionList where winningBid not equals to UPDATED_WINNING_BID
        defaultAuctionShouldBeFound("winningBid.notEquals=" + UPDATED_WINNING_BID);
    }

    @Test
    @Transactional
    public void getAllAuctionsByWinningBidIsInShouldWork() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where winningBid in DEFAULT_WINNING_BID or UPDATED_WINNING_BID
        defaultAuctionShouldBeFound("winningBid.in=" + DEFAULT_WINNING_BID + "," + UPDATED_WINNING_BID);

        // Get all the auctionList where winningBid equals to UPDATED_WINNING_BID
        defaultAuctionShouldNotBeFound("winningBid.in=" + UPDATED_WINNING_BID);
    }

    @Test
    @Transactional
    public void getAllAuctionsByWinningBidIsNullOrNotNull() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where winningBid is not null
        defaultAuctionShouldBeFound("winningBid.specified=true");

        // Get all the auctionList where winningBid is null
        defaultAuctionShouldNotBeFound("winningBid.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuctionsByWinningBidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where winningBid is greater than or equal to DEFAULT_WINNING_BID
        defaultAuctionShouldBeFound("winningBid.greaterThanOrEqual=" + DEFAULT_WINNING_BID);

        // Get all the auctionList where winningBid is greater than or equal to UPDATED_WINNING_BID
        defaultAuctionShouldNotBeFound("winningBid.greaterThanOrEqual=" + UPDATED_WINNING_BID);
    }

    @Test
    @Transactional
    public void getAllAuctionsByWinningBidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where winningBid is less than or equal to DEFAULT_WINNING_BID
        defaultAuctionShouldBeFound("winningBid.lessThanOrEqual=" + DEFAULT_WINNING_BID);

        // Get all the auctionList where winningBid is less than or equal to SMALLER_WINNING_BID
        defaultAuctionShouldNotBeFound("winningBid.lessThanOrEqual=" + SMALLER_WINNING_BID);
    }

    @Test
    @Transactional
    public void getAllAuctionsByWinningBidIsLessThanSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where winningBid is less than DEFAULT_WINNING_BID
        defaultAuctionShouldNotBeFound("winningBid.lessThan=" + DEFAULT_WINNING_BID);

        // Get all the auctionList where winningBid is less than UPDATED_WINNING_BID
        defaultAuctionShouldBeFound("winningBid.lessThan=" + UPDATED_WINNING_BID);
    }

    @Test
    @Transactional
    public void getAllAuctionsByWinningBidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where winningBid is greater than DEFAULT_WINNING_BID
        defaultAuctionShouldNotBeFound("winningBid.greaterThan=" + DEFAULT_WINNING_BID);

        // Get all the auctionList where winningBid is greater than SMALLER_WINNING_BID
        defaultAuctionShouldBeFound("winningBid.greaterThan=" + SMALLER_WINNING_BID);
    }


    @Test
    @Transactional
    public void getAllAuctionsByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where expirationDate equals to DEFAULT_EXPIRATION_DATE
        defaultAuctionShouldBeFound("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the auctionList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultAuctionShouldNotBeFound("expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllAuctionsByExpirationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where expirationDate not equals to DEFAULT_EXPIRATION_DATE
        defaultAuctionShouldNotBeFound("expirationDate.notEquals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the auctionList where expirationDate not equals to UPDATED_EXPIRATION_DATE
        defaultAuctionShouldBeFound("expirationDate.notEquals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllAuctionsByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where expirationDate in DEFAULT_EXPIRATION_DATE or UPDATED_EXPIRATION_DATE
        defaultAuctionShouldBeFound("expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE);

        // Get all the auctionList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultAuctionShouldNotBeFound("expirationDate.in=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllAuctionsByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where expirationDate is not null
        defaultAuctionShouldBeFound("expirationDate.specified=true");

        // Get all the auctionList where expirationDate is null
        defaultAuctionShouldNotBeFound("expirationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuctionsByExpirationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where expirationDate is greater than or equal to DEFAULT_EXPIRATION_DATE
        defaultAuctionShouldBeFound("expirationDate.greaterThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the auctionList where expirationDate is greater than or equal to UPDATED_EXPIRATION_DATE
        defaultAuctionShouldNotBeFound("expirationDate.greaterThanOrEqual=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllAuctionsByExpirationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where expirationDate is less than or equal to DEFAULT_EXPIRATION_DATE
        defaultAuctionShouldBeFound("expirationDate.lessThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the auctionList where expirationDate is less than or equal to SMALLER_EXPIRATION_DATE
        defaultAuctionShouldNotBeFound("expirationDate.lessThanOrEqual=" + SMALLER_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllAuctionsByExpirationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where expirationDate is less than DEFAULT_EXPIRATION_DATE
        defaultAuctionShouldNotBeFound("expirationDate.lessThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the auctionList where expirationDate is less than UPDATED_EXPIRATION_DATE
        defaultAuctionShouldBeFound("expirationDate.lessThan=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllAuctionsByExpirationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where expirationDate is greater than DEFAULT_EXPIRATION_DATE
        defaultAuctionShouldNotBeFound("expirationDate.greaterThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the auctionList where expirationDate is greater than SMALLER_EXPIRATION_DATE
        defaultAuctionShouldBeFound("expirationDate.greaterThan=" + SMALLER_EXPIRATION_DATE);
    }


    @Test
    @Transactional
    public void getAllAuctionsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where state equals to DEFAULT_STATE
        defaultAuctionShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the auctionList where state equals to UPDATED_STATE
        defaultAuctionShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllAuctionsByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where state not equals to DEFAULT_STATE
        defaultAuctionShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the auctionList where state not equals to UPDATED_STATE
        defaultAuctionShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllAuctionsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where state in DEFAULT_STATE or UPDATED_STATE
        defaultAuctionShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the auctionList where state equals to UPDATED_STATE
        defaultAuctionShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllAuctionsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList where state is not null
        defaultAuctionShouldBeFound("state.specified=true");

        // Get all the auctionList where state is null
        defaultAuctionShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuctionsByPrizeIsEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);
        Prize prize = PrizeResourceIT.createEntity(em);
        em.persist(prize);
        em.flush();
        auction.setPrize(prize);
        auctionRepository.saveAndFlush(auction);
        Long prizeId = prize.getId();

        // Get all the auctionList where prize equals to prizeId
        defaultAuctionShouldBeFound("prizeId.equals=" + prizeId);

        // Get all the auctionList where prize equals to prizeId + 1
        defaultAuctionShouldNotBeFound("prizeId.equals=" + (prizeId + 1));
    }


    @Test
    @Transactional
    public void getAllAuctionsByWinnerIsEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);
        ApplicationUser winner = ApplicationUserResourceIT.createEntity(em);
        em.persist(winner);
        em.flush();
        auction.setWinner(winner);
        auctionRepository.saveAndFlush(auction);
        Long winnerId = winner.getId();

        // Get all the auctionList where winner equals to winnerId
        defaultAuctionShouldBeFound("winnerId.equals=" + winnerId);

        // Get all the auctionList where winner equals to winnerId + 1
        defaultAuctionShouldNotBeFound("winnerId.equals=" + (winnerId + 1));
    }


    @Test
    @Transactional
    public void getAllAuctionsByProyectIsEqualToSomething() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);
        Proyect proyect = ProyectResourceIT.createEntity(em);
        em.persist(proyect);
        em.flush();
        auction.setProyect(proyect);
        auctionRepository.saveAndFlush(auction);
        Long proyectId = proyect.getId();

        // Get all the auctionList where proyect equals to proyectId
        defaultAuctionShouldBeFound("proyectId.equals=" + proyectId);

        // Get all the auctionList where proyect equals to proyectId + 1
        defaultAuctionShouldNotBeFound("proyectId.equals=" + (proyectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuctionShouldBeFound(String filter) throws Exception {
        restAuctionMockMvc.perform(get("/api/auctions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auction.getId().intValue())))
            .andExpect(jsonPath("$.[*].initialBid").value(hasItem(DEFAULT_INITIAL_BID.doubleValue())))
            .andExpect(jsonPath("$.[*].winningBid").value(hasItem(DEFAULT_WINNING_BID.doubleValue())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(sameInstant(DEFAULT_EXPIRATION_DATE))))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));

        // Check, that the count call also returns 1
        restAuctionMockMvc.perform(get("/api/auctions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuctionShouldNotBeFound(String filter) throws Exception {
        restAuctionMockMvc.perform(get("/api/auctions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuctionMockMvc.perform(get("/api/auctions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAuction() throws Exception {
        // Get the auction
        restAuctionMockMvc.perform(get("/api/auctions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuction() throws Exception {
        // Initialize the database
        auctionService.save(auction);

        int databaseSizeBeforeUpdate = auctionRepository.findAll().size();

        // Update the auction
        Auction updatedAuction = auctionRepository.findById(auction.getId()).get();
        // Disconnect from session so that the updates on updatedAuction are not directly saved in db
        em.detach(updatedAuction);
        updatedAuction
            .initialBid(UPDATED_INITIAL_BID)
            .winningBid(UPDATED_WINNING_BID)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .state(UPDATED_STATE);

        restAuctionMockMvc.perform(put("/api/auctions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuction)))
            .andExpect(status().isOk());

        // Validate the Auction in the database
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeUpdate);
        Auction testAuction = auctionList.get(auctionList.size() - 1);
        assertThat(testAuction.getInitialBid()).isEqualTo(UPDATED_INITIAL_BID);
        assertThat(testAuction.getWinningBid()).isEqualTo(UPDATED_WINNING_BID);
        assertThat(testAuction.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testAuction.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAuction() throws Exception {
        int databaseSizeBeforeUpdate = auctionRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuctionMockMvc.perform(put("/api/auctions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auction)))
            .andExpect(status().isBadRequest());

        // Validate the Auction in the database
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuction() throws Exception {
        // Initialize the database
        auctionService.save(auction);

        int databaseSizeBeforeDelete = auctionRepository.findAll().size();

        // Delete the auction
        restAuctionMockMvc.perform(delete("/api/auctions/{id}", auction.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
