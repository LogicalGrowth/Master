package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.DonationHistory;
import cr.ac.ucenfotec.fun4fund.repository.DonationHistoryRepository;

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

/**
 * Integration tests for the {@link DonationHistoryResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class DonationHistoryResourceIT {

    private static final Double DEFAULT_AMOUNT = 0D;
    private static final Double UPDATED_AMOUNT = 1D;

    private static final ZonedDateTime DEFAULT_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_STAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private DonationHistoryRepository donationHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDonationHistoryMockMvc;

    private DonationHistory donationHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DonationHistory createEntity(EntityManager em) {
        DonationHistory donationHistory = new DonationHistory()
            .amount(DEFAULT_AMOUNT)
            .timeStamp(DEFAULT_TIME_STAMP);
        return donationHistory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DonationHistory createUpdatedEntity(EntityManager em) {
        DonationHistory donationHistory = new DonationHistory()
            .amount(UPDATED_AMOUNT)
            .timeStamp(UPDATED_TIME_STAMP);
        return donationHistory;
    }

    @BeforeEach
    public void initTest() {
        donationHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createDonationHistory() throws Exception {
        int databaseSizeBeforeCreate = donationHistoryRepository.findAll().size();
        // Create the DonationHistory
        restDonationHistoryMockMvc.perform(post("/api/donation-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(donationHistory)))
            .andExpect(status().isCreated());

        // Validate the DonationHistory in the database
        List<DonationHistory> donationHistoryList = donationHistoryRepository.findAll();
        assertThat(donationHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        DonationHistory testDonationHistory = donationHistoryList.get(donationHistoryList.size() - 1);
        assertThat(testDonationHistory.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testDonationHistory.getTimeStamp()).isEqualTo(DEFAULT_TIME_STAMP);
    }

    @Test
    @Transactional
    public void createDonationHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = donationHistoryRepository.findAll().size();

        // Create the DonationHistory with an existing ID
        donationHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDonationHistoryMockMvc.perform(post("/api/donation-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(donationHistory)))
            .andExpect(status().isBadRequest());

        // Validate the DonationHistory in the database
        List<DonationHistory> donationHistoryList = donationHistoryRepository.findAll();
        assertThat(donationHistoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = donationHistoryRepository.findAll().size();
        // set the field null
        donationHistory.setAmount(null);

        // Create the DonationHistory, which fails.


        restDonationHistoryMockMvc.perform(post("/api/donation-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(donationHistory)))
            .andExpect(status().isBadRequest());

        List<DonationHistory> donationHistoryList = donationHistoryRepository.findAll();
        assertThat(donationHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimeStampIsRequired() throws Exception {
        int databaseSizeBeforeTest = donationHistoryRepository.findAll().size();
        // set the field null
        donationHistory.setTimeStamp(null);

        // Create the DonationHistory, which fails.


        restDonationHistoryMockMvc.perform(post("/api/donation-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(donationHistory)))
            .andExpect(status().isBadRequest());

        List<DonationHistory> donationHistoryList = donationHistoryRepository.findAll();
        assertThat(donationHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDonationHistories() throws Exception {
        // Initialize the database
        donationHistoryRepository.saveAndFlush(donationHistory);

        // Get all the donationHistoryList
        restDonationHistoryMockMvc.perform(get("/api/donation-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donationHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].timeStamp").value(hasItem(sameInstant(DEFAULT_TIME_STAMP))));
    }
    
    @Test
    @Transactional
    public void getDonationHistory() throws Exception {
        // Initialize the database
        donationHistoryRepository.saveAndFlush(donationHistory);

        // Get the donationHistory
        restDonationHistoryMockMvc.perform(get("/api/donation-histories/{id}", donationHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(donationHistory.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.timeStamp").value(sameInstant(DEFAULT_TIME_STAMP)));
    }
    @Test
    @Transactional
    public void getNonExistingDonationHistory() throws Exception {
        // Get the donationHistory
        restDonationHistoryMockMvc.perform(get("/api/donation-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDonationHistory() throws Exception {
        // Initialize the database
        donationHistoryRepository.saveAndFlush(donationHistory);

        int databaseSizeBeforeUpdate = donationHistoryRepository.findAll().size();

        // Update the donationHistory
        DonationHistory updatedDonationHistory = donationHistoryRepository.findById(donationHistory.getId()).get();
        // Disconnect from session so that the updates on updatedDonationHistory are not directly saved in db
        em.detach(updatedDonationHistory);
        updatedDonationHistory
            .amount(UPDATED_AMOUNT)
            .timeStamp(UPDATED_TIME_STAMP);

        restDonationHistoryMockMvc.perform(put("/api/donation-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDonationHistory)))
            .andExpect(status().isOk());

        // Validate the DonationHistory in the database
        List<DonationHistory> donationHistoryList = donationHistoryRepository.findAll();
        assertThat(donationHistoryList).hasSize(databaseSizeBeforeUpdate);
        DonationHistory testDonationHistory = donationHistoryList.get(donationHistoryList.size() - 1);
        assertThat(testDonationHistory.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testDonationHistory.getTimeStamp()).isEqualTo(UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingDonationHistory() throws Exception {
        int databaseSizeBeforeUpdate = donationHistoryRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonationHistoryMockMvc.perform(put("/api/donation-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(donationHistory)))
            .andExpect(status().isBadRequest());

        // Validate the DonationHistory in the database
        List<DonationHistory> donationHistoryList = donationHistoryRepository.findAll();
        assertThat(donationHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDonationHistory() throws Exception {
        // Initialize the database
        donationHistoryRepository.saveAndFlush(donationHistory);

        int databaseSizeBeforeDelete = donationHistoryRepository.findAll().size();

        // Delete the donationHistory
        restDonationHistoryMockMvc.perform(delete("/api/donation-histories/{id}", donationHistory.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DonationHistory> donationHistoryList = donationHistoryRepository.findAll();
        assertThat(donationHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
