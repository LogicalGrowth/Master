package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Auction;
import cr.ac.ucenfotec.fun4fund.repository.AuctionRepository;

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

    private static final Double DEFAULT_WINNING_BID = 0D;
    private static final Double UPDATED_WINNING_BID = 1D;

    private static final ZonedDateTime DEFAULT_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ActivityStatus DEFAULT_STATE = ActivityStatus.ENABLED;
    private static final ActivityStatus UPDATED_STATE = ActivityStatus.DISABLED;

    @Autowired
    private AuctionRepository auctionRepository;

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
    public void getNonExistingAuction() throws Exception {
        // Get the auction
        restAuctionMockMvc.perform(get("/api/auctions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuction() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

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
        auctionRepository.saveAndFlush(auction);

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
