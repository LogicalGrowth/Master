package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.PasswordHistory;
import cr.ac.ucenfotec.fun4fund.repository.PasswordHistoryRepository;
import cr.ac.ucenfotec.fun4fund.service.PasswordHistoryService;
import cr.ac.ucenfotec.fun4fund.service.dto.PasswordHistoryCriteria;
import cr.ac.ucenfotec.fun4fund.service.PasswordHistoryQueryService;

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

import cr.ac.ucenfotec.fun4fund.domain.enumeration.PasswordStatus;
/**
 * Integration tests for the {@link PasswordHistoryResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PasswordHistoryResourceIT {

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final PasswordStatus DEFAULT_STATUS = PasswordStatus.ACTIVE;
    private static final PasswordStatus UPDATED_STATUS = PasswordStatus.EXPIRED;

    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;

    @Autowired
    private PasswordHistoryService passwordHistoryService;

    @Autowired
    private PasswordHistoryQueryService passwordHistoryQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPasswordHistoryMockMvc;

    private PasswordHistory passwordHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PasswordHistory createEntity(EntityManager em) {
        PasswordHistory passwordHistory = new PasswordHistory()
            .password(DEFAULT_PASSWORD)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS);
        return passwordHistory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PasswordHistory createUpdatedEntity(EntityManager em) {
        PasswordHistory passwordHistory = new PasswordHistory()
            .password(UPDATED_PASSWORD)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS);
        return passwordHistory;
    }

    @BeforeEach
    public void initTest() {
        passwordHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createPasswordHistory() throws Exception {
        int databaseSizeBeforeCreate = passwordHistoryRepository.findAll().size();
        // Create the PasswordHistory
        restPasswordHistoryMockMvc.perform(post("/api/password-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passwordHistory)))
            .andExpect(status().isCreated());

        // Validate the PasswordHistory in the database
        List<PasswordHistory> passwordHistoryList = passwordHistoryRepository.findAll();
        assertThat(passwordHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        PasswordHistory testPasswordHistory = passwordHistoryList.get(passwordHistoryList.size() - 1);
        assertThat(testPasswordHistory.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testPasswordHistory.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPasswordHistory.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPasswordHistory.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createPasswordHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = passwordHistoryRepository.findAll().size();

        // Create the PasswordHistory with an existing ID
        passwordHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPasswordHistoryMockMvc.perform(post("/api/password-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passwordHistory)))
            .andExpect(status().isBadRequest());

        // Validate the PasswordHistory in the database
        List<PasswordHistory> passwordHistoryList = passwordHistoryRepository.findAll();
        assertThat(passwordHistoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPasswordHistories() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList
        restPasswordHistoryMockMvc.perform(get("/api/password-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passwordHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getPasswordHistory() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get the passwordHistory
        restPasswordHistoryMockMvc.perform(get("/api/password-histories/{id}", passwordHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(passwordHistory.getId().intValue()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }


    @Test
    @Transactional
    public void getPasswordHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        Long id = passwordHistory.getId();

        defaultPasswordHistoryShouldBeFound("id.equals=" + id);
        defaultPasswordHistoryShouldNotBeFound("id.notEquals=" + id);

        defaultPasswordHistoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPasswordHistoryShouldNotBeFound("id.greaterThan=" + id);

        defaultPasswordHistoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPasswordHistoryShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPasswordHistoriesByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where password equals to DEFAULT_PASSWORD
        defaultPasswordHistoryShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the passwordHistoryList where password equals to UPDATED_PASSWORD
        defaultPasswordHistoryShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where password not equals to DEFAULT_PASSWORD
        defaultPasswordHistoryShouldNotBeFound("password.notEquals=" + DEFAULT_PASSWORD);

        // Get all the passwordHistoryList where password not equals to UPDATED_PASSWORD
        defaultPasswordHistoryShouldBeFound("password.notEquals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultPasswordHistoryShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the passwordHistoryList where password equals to UPDATED_PASSWORD
        defaultPasswordHistoryShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where password is not null
        defaultPasswordHistoryShouldBeFound("password.specified=true");

        // Get all the passwordHistoryList where password is null
        defaultPasswordHistoryShouldNotBeFound("password.specified=false");
    }
                @Test
    @Transactional
    public void getAllPasswordHistoriesByPasswordContainsSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where password contains DEFAULT_PASSWORD
        defaultPasswordHistoryShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the passwordHistoryList where password contains UPDATED_PASSWORD
        defaultPasswordHistoryShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where password does not contain DEFAULT_PASSWORD
        defaultPasswordHistoryShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the passwordHistoryList where password does not contain UPDATED_PASSWORD
        defaultPasswordHistoryShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }


    @Test
    @Transactional
    public void getAllPasswordHistoriesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where startDate equals to DEFAULT_START_DATE
        defaultPasswordHistoryShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the passwordHistoryList where startDate equals to UPDATED_START_DATE
        defaultPasswordHistoryShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where startDate not equals to DEFAULT_START_DATE
        defaultPasswordHistoryShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the passwordHistoryList where startDate not equals to UPDATED_START_DATE
        defaultPasswordHistoryShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultPasswordHistoryShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the passwordHistoryList where startDate equals to UPDATED_START_DATE
        defaultPasswordHistoryShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where startDate is not null
        defaultPasswordHistoryShouldBeFound("startDate.specified=true");

        // Get all the passwordHistoryList where startDate is null
        defaultPasswordHistoryShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultPasswordHistoryShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the passwordHistoryList where startDate is greater than or equal to UPDATED_START_DATE
        defaultPasswordHistoryShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where startDate is less than or equal to DEFAULT_START_DATE
        defaultPasswordHistoryShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the passwordHistoryList where startDate is less than or equal to SMALLER_START_DATE
        defaultPasswordHistoryShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where startDate is less than DEFAULT_START_DATE
        defaultPasswordHistoryShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the passwordHistoryList where startDate is less than UPDATED_START_DATE
        defaultPasswordHistoryShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where startDate is greater than DEFAULT_START_DATE
        defaultPasswordHistoryShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the passwordHistoryList where startDate is greater than SMALLER_START_DATE
        defaultPasswordHistoryShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }


    @Test
    @Transactional
    public void getAllPasswordHistoriesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where endDate equals to DEFAULT_END_DATE
        defaultPasswordHistoryShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the passwordHistoryList where endDate equals to UPDATED_END_DATE
        defaultPasswordHistoryShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where endDate not equals to DEFAULT_END_DATE
        defaultPasswordHistoryShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the passwordHistoryList where endDate not equals to UPDATED_END_DATE
        defaultPasswordHistoryShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultPasswordHistoryShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the passwordHistoryList where endDate equals to UPDATED_END_DATE
        defaultPasswordHistoryShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where endDate is not null
        defaultPasswordHistoryShouldBeFound("endDate.specified=true");

        // Get all the passwordHistoryList where endDate is null
        defaultPasswordHistoryShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultPasswordHistoryShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the passwordHistoryList where endDate is greater than or equal to UPDATED_END_DATE
        defaultPasswordHistoryShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where endDate is less than or equal to DEFAULT_END_DATE
        defaultPasswordHistoryShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the passwordHistoryList where endDate is less than or equal to SMALLER_END_DATE
        defaultPasswordHistoryShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where endDate is less than DEFAULT_END_DATE
        defaultPasswordHistoryShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the passwordHistoryList where endDate is less than UPDATED_END_DATE
        defaultPasswordHistoryShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where endDate is greater than DEFAULT_END_DATE
        defaultPasswordHistoryShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the passwordHistoryList where endDate is greater than SMALLER_END_DATE
        defaultPasswordHistoryShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }


    @Test
    @Transactional
    public void getAllPasswordHistoriesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where status equals to DEFAULT_STATUS
        defaultPasswordHistoryShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the passwordHistoryList where status equals to UPDATED_STATUS
        defaultPasswordHistoryShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where status not equals to DEFAULT_STATUS
        defaultPasswordHistoryShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the passwordHistoryList where status not equals to UPDATED_STATUS
        defaultPasswordHistoryShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultPasswordHistoryShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the passwordHistoryList where status equals to UPDATED_STATUS
        defaultPasswordHistoryShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllPasswordHistoriesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

        // Get all the passwordHistoryList where status is not null
        defaultPasswordHistoryShouldBeFound("status.specified=true");

        // Get all the passwordHistoryList where status is null
        defaultPasswordHistoryShouldNotBeFound("status.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPasswordHistoryShouldBeFound(String filter) throws Exception {
        restPasswordHistoryMockMvc.perform(get("/api/password-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passwordHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restPasswordHistoryMockMvc.perform(get("/api/password-histories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPasswordHistoryShouldNotBeFound(String filter) throws Exception {
        restPasswordHistoryMockMvc.perform(get("/api/password-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPasswordHistoryMockMvc.perform(get("/api/password-histories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPasswordHistory() throws Exception {
        // Get the passwordHistory
        restPasswordHistoryMockMvc.perform(get("/api/password-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePasswordHistory() throws Exception {
        // Initialize the database
        passwordHistoryService.save(passwordHistory);

        int databaseSizeBeforeUpdate = passwordHistoryRepository.findAll().size();

        // Update the passwordHistory
        PasswordHistory updatedPasswordHistory = passwordHistoryRepository.findById(passwordHistory.getId()).get();
        // Disconnect from session so that the updates on updatedPasswordHistory are not directly saved in db
        em.detach(updatedPasswordHistory);
        updatedPasswordHistory
            .password(UPDATED_PASSWORD)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS);

        restPasswordHistoryMockMvc.perform(put("/api/password-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPasswordHistory)))
            .andExpect(status().isOk());

        // Validate the PasswordHistory in the database
        List<PasswordHistory> passwordHistoryList = passwordHistoryRepository.findAll();
        assertThat(passwordHistoryList).hasSize(databaseSizeBeforeUpdate);
        PasswordHistory testPasswordHistory = passwordHistoryList.get(passwordHistoryList.size() - 1);
        assertThat(testPasswordHistory.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testPasswordHistory.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPasswordHistory.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPasswordHistory.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingPasswordHistory() throws Exception {
        int databaseSizeBeforeUpdate = passwordHistoryRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPasswordHistoryMockMvc.perform(put("/api/password-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passwordHistory)))
            .andExpect(status().isBadRequest());

        // Validate the PasswordHistory in the database
        List<PasswordHistory> passwordHistoryList = passwordHistoryRepository.findAll();
        assertThat(passwordHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePasswordHistory() throws Exception {
        // Initialize the database
        passwordHistoryService.save(passwordHistory);

        int databaseSizeBeforeDelete = passwordHistoryRepository.findAll().size();

        // Delete the passwordHistory
        restPasswordHistoryMockMvc.perform(delete("/api/password-histories/{id}", passwordHistory.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PasswordHistory> passwordHistoryList = passwordHistoryRepository.findAll();
        assertThat(passwordHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
