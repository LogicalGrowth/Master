package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Fee;
import cr.ac.ucenfotec.fun4fund.repository.FeeRepository;
import cr.ac.ucenfotec.fun4fund.service.FeeService;
import cr.ac.ucenfotec.fun4fund.service.dto.FeeCriteria;
import cr.ac.ucenfotec.fun4fund.service.FeeQueryService;

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
    private static final Double SMALLER_PERCENTAGE = 0D - 1D;

    private static final Double DEFAULT_HIGHEST_AMOUNT = 0D;
    private static final Double UPDATED_HIGHEST_AMOUNT = 1D;
    private static final Double SMALLER_HIGHEST_AMOUNT = 0D - 1D;

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    private FeeService feeService;

    @Autowired
    private FeeQueryService feeQueryService;

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
    public void getFeesByIdFiltering() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        Long id = fee.getId();

        defaultFeeShouldBeFound("id.equals=" + id);
        defaultFeeShouldNotBeFound("id.notEquals=" + id);

        defaultFeeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFeeShouldNotBeFound("id.greaterThan=" + id);

        defaultFeeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFeeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFeesByPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where percentage equals to DEFAULT_PERCENTAGE
        defaultFeeShouldBeFound("percentage.equals=" + DEFAULT_PERCENTAGE);

        // Get all the feeList where percentage equals to UPDATED_PERCENTAGE
        defaultFeeShouldNotBeFound("percentage.equals=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllFeesByPercentageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where percentage not equals to DEFAULT_PERCENTAGE
        defaultFeeShouldNotBeFound("percentage.notEquals=" + DEFAULT_PERCENTAGE);

        // Get all the feeList where percentage not equals to UPDATED_PERCENTAGE
        defaultFeeShouldBeFound("percentage.notEquals=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllFeesByPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where percentage in DEFAULT_PERCENTAGE or UPDATED_PERCENTAGE
        defaultFeeShouldBeFound("percentage.in=" + DEFAULT_PERCENTAGE + "," + UPDATED_PERCENTAGE);

        // Get all the feeList where percentage equals to UPDATED_PERCENTAGE
        defaultFeeShouldNotBeFound("percentage.in=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllFeesByPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where percentage is not null
        defaultFeeShouldBeFound("percentage.specified=true");

        // Get all the feeList where percentage is null
        defaultFeeShouldNotBeFound("percentage.specified=false");
    }

    @Test
    @Transactional
    public void getAllFeesByPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where percentage is greater than or equal to DEFAULT_PERCENTAGE
        defaultFeeShouldBeFound("percentage.greaterThanOrEqual=" + DEFAULT_PERCENTAGE);

        // Get all the feeList where percentage is greater than or equal to UPDATED_PERCENTAGE
        defaultFeeShouldNotBeFound("percentage.greaterThanOrEqual=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllFeesByPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where percentage is less than or equal to DEFAULT_PERCENTAGE
        defaultFeeShouldBeFound("percentage.lessThanOrEqual=" + DEFAULT_PERCENTAGE);

        // Get all the feeList where percentage is less than or equal to SMALLER_PERCENTAGE
        defaultFeeShouldNotBeFound("percentage.lessThanOrEqual=" + SMALLER_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllFeesByPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where percentage is less than DEFAULT_PERCENTAGE
        defaultFeeShouldNotBeFound("percentage.lessThan=" + DEFAULT_PERCENTAGE);

        // Get all the feeList where percentage is less than UPDATED_PERCENTAGE
        defaultFeeShouldBeFound("percentage.lessThan=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllFeesByPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where percentage is greater than DEFAULT_PERCENTAGE
        defaultFeeShouldNotBeFound("percentage.greaterThan=" + DEFAULT_PERCENTAGE);

        // Get all the feeList where percentage is greater than SMALLER_PERCENTAGE
        defaultFeeShouldBeFound("percentage.greaterThan=" + SMALLER_PERCENTAGE);
    }


    @Test
    @Transactional
    public void getAllFeesByHighestAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where highestAmount equals to DEFAULT_HIGHEST_AMOUNT
        defaultFeeShouldBeFound("highestAmount.equals=" + DEFAULT_HIGHEST_AMOUNT);

        // Get all the feeList where highestAmount equals to UPDATED_HIGHEST_AMOUNT
        defaultFeeShouldNotBeFound("highestAmount.equals=" + UPDATED_HIGHEST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFeesByHighestAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where highestAmount not equals to DEFAULT_HIGHEST_AMOUNT
        defaultFeeShouldNotBeFound("highestAmount.notEquals=" + DEFAULT_HIGHEST_AMOUNT);

        // Get all the feeList where highestAmount not equals to UPDATED_HIGHEST_AMOUNT
        defaultFeeShouldBeFound("highestAmount.notEquals=" + UPDATED_HIGHEST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFeesByHighestAmountIsInShouldWork() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where highestAmount in DEFAULT_HIGHEST_AMOUNT or UPDATED_HIGHEST_AMOUNT
        defaultFeeShouldBeFound("highestAmount.in=" + DEFAULT_HIGHEST_AMOUNT + "," + UPDATED_HIGHEST_AMOUNT);

        // Get all the feeList where highestAmount equals to UPDATED_HIGHEST_AMOUNT
        defaultFeeShouldNotBeFound("highestAmount.in=" + UPDATED_HIGHEST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFeesByHighestAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where highestAmount is not null
        defaultFeeShouldBeFound("highestAmount.specified=true");

        // Get all the feeList where highestAmount is null
        defaultFeeShouldNotBeFound("highestAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllFeesByHighestAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where highestAmount is greater than or equal to DEFAULT_HIGHEST_AMOUNT
        defaultFeeShouldBeFound("highestAmount.greaterThanOrEqual=" + DEFAULT_HIGHEST_AMOUNT);

        // Get all the feeList where highestAmount is greater than or equal to UPDATED_HIGHEST_AMOUNT
        defaultFeeShouldNotBeFound("highestAmount.greaterThanOrEqual=" + UPDATED_HIGHEST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFeesByHighestAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where highestAmount is less than or equal to DEFAULT_HIGHEST_AMOUNT
        defaultFeeShouldBeFound("highestAmount.lessThanOrEqual=" + DEFAULT_HIGHEST_AMOUNT);

        // Get all the feeList where highestAmount is less than or equal to SMALLER_HIGHEST_AMOUNT
        defaultFeeShouldNotBeFound("highestAmount.lessThanOrEqual=" + SMALLER_HIGHEST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFeesByHighestAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where highestAmount is less than DEFAULT_HIGHEST_AMOUNT
        defaultFeeShouldNotBeFound("highestAmount.lessThan=" + DEFAULT_HIGHEST_AMOUNT);

        // Get all the feeList where highestAmount is less than UPDATED_HIGHEST_AMOUNT
        defaultFeeShouldBeFound("highestAmount.lessThan=" + UPDATED_HIGHEST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFeesByHighestAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        feeRepository.saveAndFlush(fee);

        // Get all the feeList where highestAmount is greater than DEFAULT_HIGHEST_AMOUNT
        defaultFeeShouldNotBeFound("highestAmount.greaterThan=" + DEFAULT_HIGHEST_AMOUNT);

        // Get all the feeList where highestAmount is greater than SMALLER_HIGHEST_AMOUNT
        defaultFeeShouldBeFound("highestAmount.greaterThan=" + SMALLER_HIGHEST_AMOUNT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFeeShouldBeFound(String filter) throws Exception {
        restFeeMockMvc.perform(get("/api/fees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fee.getId().intValue())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].highestAmount").value(hasItem(DEFAULT_HIGHEST_AMOUNT.doubleValue())));

        // Check, that the count call also returns 1
        restFeeMockMvc.perform(get("/api/fees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFeeShouldNotBeFound(String filter) throws Exception {
        restFeeMockMvc.perform(get("/api/fees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFeeMockMvc.perform(get("/api/fees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        feeService.save(fee);

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
        feeService.save(fee);

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
