package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.PasswordHistory;
import cr.ac.ucenfotec.fun4fund.repository.PasswordHistoryRepository;

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

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final PasswordStatus DEFAULT_STATUS = PasswordStatus.ACTIVE;
    private static final PasswordStatus UPDATED_STATUS = PasswordStatus.EXPIRED;

    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;

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
    public void getNonExistingPasswordHistory() throws Exception {
        // Get the passwordHistory
        restPasswordHistoryMockMvc.perform(get("/api/password-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePasswordHistory() throws Exception {
        // Initialize the database
        passwordHistoryRepository.saveAndFlush(passwordHistory);

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
        passwordHistoryRepository.saveAndFlush(passwordHistory);

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
