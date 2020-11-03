package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.AppLog;
import cr.ac.ucenfotec.fun4fund.repository.AppLogRepository;

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

import cr.ac.ucenfotec.fun4fund.domain.enumeration.ActionType;
/**
 * Integration tests for the {@link AppLogResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AppLogResourceIT {

    private static final ZonedDateTime DEFAULT_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_STAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ActionType DEFAULT_ACTION = ActionType.CREATE;
    private static final ActionType UPDATED_ACTION = ActionType.UPDATE;

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private AppLogRepository appLogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppLogMockMvc;

    private AppLog appLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppLog createEntity(EntityManager em) {
        AppLog appLog = new AppLog()
            .timeStamp(DEFAULT_TIME_STAMP)
            .action(DEFAULT_ACTION)
            .user(DEFAULT_USER)
            .description(DEFAULT_DESCRIPTION);
        return appLog;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppLog createUpdatedEntity(EntityManager em) {
        AppLog appLog = new AppLog()
            .timeStamp(UPDATED_TIME_STAMP)
            .action(UPDATED_ACTION)
            .user(UPDATED_USER)
            .description(UPDATED_DESCRIPTION);
        return appLog;
    }

    @BeforeEach
    public void initTest() {
        appLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppLog() throws Exception {
        int databaseSizeBeforeCreate = appLogRepository.findAll().size();
        // Create the AppLog
        restAppLogMockMvc.perform(post("/api/app-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appLog)))
            .andExpect(status().isCreated());

        // Validate the AppLog in the database
        List<AppLog> appLogList = appLogRepository.findAll();
        assertThat(appLogList).hasSize(databaseSizeBeforeCreate + 1);
        AppLog testAppLog = appLogList.get(appLogList.size() - 1);
        assertThat(testAppLog.getTimeStamp()).isEqualTo(DEFAULT_TIME_STAMP);
        assertThat(testAppLog.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testAppLog.getUser()).isEqualTo(DEFAULT_USER);
        assertThat(testAppLog.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createAppLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appLogRepository.findAll().size();

        // Create the AppLog with an existing ID
        appLog.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppLogMockMvc.perform(post("/api/app-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appLog)))
            .andExpect(status().isBadRequest());

        // Validate the AppLog in the database
        List<AppLog> appLogList = appLogRepository.findAll();
        assertThat(appLogList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTimeStampIsRequired() throws Exception {
        int databaseSizeBeforeTest = appLogRepository.findAll().size();
        // set the field null
        appLog.setTimeStamp(null);

        // Create the AppLog, which fails.


        restAppLogMockMvc.perform(post("/api/app-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appLog)))
            .andExpect(status().isBadRequest());

        List<AppLog> appLogList = appLogRepository.findAll();
        assertThat(appLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActionIsRequired() throws Exception {
        int databaseSizeBeforeTest = appLogRepository.findAll().size();
        // set the field null
        appLog.setAction(null);

        // Create the AppLog, which fails.


        restAppLogMockMvc.perform(post("/api/app-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appLog)))
            .andExpect(status().isBadRequest());

        List<AppLog> appLogList = appLogRepository.findAll();
        assertThat(appLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIsRequired() throws Exception {
        int databaseSizeBeforeTest = appLogRepository.findAll().size();
        // set the field null
        appLog.setUser(null);

        // Create the AppLog, which fails.


        restAppLogMockMvc.perform(post("/api/app-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appLog)))
            .andExpect(status().isBadRequest());

        List<AppLog> appLogList = appLogRepository.findAll();
        assertThat(appLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = appLogRepository.findAll().size();
        // set the field null
        appLog.setDescription(null);

        // Create the AppLog, which fails.


        restAppLogMockMvc.perform(post("/api/app-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appLog)))
            .andExpect(status().isBadRequest());

        List<AppLog> appLogList = appLogRepository.findAll();
        assertThat(appLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAppLogs() throws Exception {
        // Initialize the database
        appLogRepository.saveAndFlush(appLog);

        // Get all the appLogList
        restAppLogMockMvc.perform(get("/api/app-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeStamp").value(hasItem(sameInstant(DEFAULT_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getAppLog() throws Exception {
        // Initialize the database
        appLogRepository.saveAndFlush(appLog);

        // Get the appLog
        restAppLogMockMvc.perform(get("/api/app-logs/{id}", appLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appLog.getId().intValue()))
            .andExpect(jsonPath("$.timeStamp").value(sameInstant(DEFAULT_TIME_STAMP)))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingAppLog() throws Exception {
        // Get the appLog
        restAppLogMockMvc.perform(get("/api/app-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppLog() throws Exception {
        // Initialize the database
        appLogRepository.saveAndFlush(appLog);

        int databaseSizeBeforeUpdate = appLogRepository.findAll().size();

        // Update the appLog
        AppLog updatedAppLog = appLogRepository.findById(appLog.getId()).get();
        // Disconnect from session so that the updates on updatedAppLog are not directly saved in db
        em.detach(updatedAppLog);
        updatedAppLog
            .timeStamp(UPDATED_TIME_STAMP)
            .action(UPDATED_ACTION)
            .user(UPDATED_USER)
            .description(UPDATED_DESCRIPTION);

        restAppLogMockMvc.perform(put("/api/app-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAppLog)))
            .andExpect(status().isOk());

        // Validate the AppLog in the database
        List<AppLog> appLogList = appLogRepository.findAll();
        assertThat(appLogList).hasSize(databaseSizeBeforeUpdate);
        AppLog testAppLog = appLogList.get(appLogList.size() - 1);
        assertThat(testAppLog.getTimeStamp()).isEqualTo(UPDATED_TIME_STAMP);
        assertThat(testAppLog.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testAppLog.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testAppLog.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingAppLog() throws Exception {
        int databaseSizeBeforeUpdate = appLogRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppLogMockMvc.perform(put("/api/app-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appLog)))
            .andExpect(status().isBadRequest());

        // Validate the AppLog in the database
        List<AppLog> appLogList = appLogRepository.findAll();
        assertThat(appLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAppLog() throws Exception {
        // Initialize the database
        appLogRepository.saveAndFlush(appLog);

        int databaseSizeBeforeDelete = appLogRepository.findAll().size();

        // Delete the appLog
        restAppLogMockMvc.perform(delete("/api/app-logs/{id}", appLog.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppLog> appLogList = appLogRepository.findAll();
        assertThat(appLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
