package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Fee;
import cr.ac.ucenfotec.fun4fund.repository.FeeRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FeeResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FeeResourceIT {

    private static final Double DEFAULT_PERCENTAGE = 0D;
    private static final Double UPDATED_PERCENTAGE = 1D;

    private static final Double DEFAULT_HIGHEST_AMOUNT = 0D;
    private static final Double UPDATED_HIGHEST_AMOUNT = 1D;

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeeMockMvc;

    private Fee fee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fee createEntity(EntityManager em) {
        Fee fee = new Fee()
            .percentage(DEFAULT_PERCENTAGE)
            .highestAmount(DEFAULT_HIGHEST_AMOUNT);
        return fee;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fee createUpdatedEntity(EntityManager em) {
        Fee fee = new Fee()
            .percentage(UPDATED_PERCENTAGE)
            .highestAmount(UPDATED_HIGHEST_AMOUNT);
        return fee;
    }

    @BeforeEach
    public void initTest() {
        fee = createEntity(em);
    }

    @Test
    @Transactional
    public void createFee() throws Exception {
        int databaseSizeBeforeCreate = feeRepository.findAll().size();
        // Create the Fee
        restFeeMockMvc.perform(post("/api/fees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fee)))
            .andExpect(status().isCreated());

        // Validate the Fee in the database
        List<Fee> feeList = feeRepository.findAll();
        assertThat(feeList).hasSize(databaseSizeBeforeCreate + 1);
        Fee testFee = feeList.get(feeList.size() - 1);
        assertThat(testFee.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
        assertThat(testFee.getHighestAmount()).isEqualTo(DEFAULT_HIGHEST_AMOUNT);
    }

    @Test
    @Transactional
    public void createFeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = feeRepository.findAll().size();

        // Create the Fee with an existing ID
        fee.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeeMockMvc.perform(post("/api/fees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fee)))
            .andExpect(status().isBadRequest());

        // Validate the Fee in the database
        List<Fee> feeList = feeRepository.findAll();
        assertThat(feeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPercentageIsRequired() throws Exception {
        int databaseSizeBeforeTest = feeRepository.findAll().size();
        // set the field null
        fee.setPercentage(null);

        // Create the Fee, which fails.


        restFeeMockMvc.perform(post("/api/fees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fee)))
            .andExpect(status().isBadRequest());

        List<Fee> feeList = feeRepository.findAll();
        assertThat(feeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHighestAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = feeRepository.findAll().size();
        // set the field null
        fee.setHighestAmount(null);

        // Create the Fee, which fails.


        restFeeMockMvc.perform(post("/api/fees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fee)))
            .andExpect(status().isBadRequest());

        List<Fee> feeList = feeRepository.findAll();
        assertThat(feeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFees() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList
        restFeeMockMvc.perform(get("/api/fees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fee.getId().intValue())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].highestAmount").value(hasItem(DEFAULT_HIGHEST_AMOUNT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getFee() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get the fee
        restFeeMockMvc.perform(get("/api/fees/{id}", fee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fee.getId().intValue()))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.highestAmount").value(DEFAULT_HIGHEST_AMOUNT.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingFee() throws Exception {
        // Get the fee
        restFeeMockMvc.perform(get("/api/fees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFee() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        int databaseSizeBeforeUpdate = feeRepository.findAll().size();

        // Update the fee
        Fee updatedFee = feeRepository.findById(fee.getId()).get();
        // Disconnect from session so that the updates on updatedFee are not directly saved in db
        em.detach(updatedFee);
        updatedFee
            .percentage(UPDATED_PERCENTAGE)
            .highestAmount(UPDATED_HIGHEST_AMOUNT);

        restFeeMockMvc.perform(put("/api/fees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFee)))
            .andExpect(status().isOk());

        // Validate the Fee in the database
        List<Fee> feeList = feeRepository.findAll();
        assertThat(feeList).hasSize(databaseSizeBeforeUpdate);
        Fee testFee = feeList.get(feeList.size() - 1);
        assertThat(testFee.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
        assertThat(testFee.getHighestAmount()).isEqualTo(UPDATED_HIGHEST_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingFee() throws Exception {
        int databaseSizeBeforeUpdate = feeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeeMockMvc.perform(put("/api/fees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fee)))
            .andExpect(status().isBadRequest());

        // Validate the Fee in the database
        List<Fee> feeList = feeRepository.findAll();
        assertThat(feeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFee() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        int databaseSizeBeforeDelete = feeRepository.findAll().size();

        // Delete the fee
        restFeeMockMvc.perform(delete("/api/fees/{id}", fee.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fee> feeList = feeRepository.findAll();
        assertThat(feeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
