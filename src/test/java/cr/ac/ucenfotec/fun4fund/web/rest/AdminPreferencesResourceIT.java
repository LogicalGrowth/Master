package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.AdminPreferences;
import cr.ac.ucenfotec.fun4fund.repository.AdminPreferencesRepository;

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
 * Integration tests for the {@link AdminPreferencesResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AdminPreferencesResourceIT {

    private static final Integer DEFAULT_INACTIVITY_TIME = 1;
    private static final Integer UPDATED_INACTIVITY_TIME = 2;

    private static final Integer DEFAULT_NOTIFICATION_RECURRENCE = 1;
    private static final Integer UPDATED_NOTIFICATION_RECURRENCE = 2;

    @Autowired
    private AdminPreferencesRepository adminPreferencesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdminPreferencesMockMvc;

    private AdminPreferences adminPreferences;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminPreferences createEntity(EntityManager em) {
        AdminPreferences adminPreferences = new AdminPreferences()
            .inactivityTime(DEFAULT_INACTIVITY_TIME)
            .notificationRecurrence(DEFAULT_NOTIFICATION_RECURRENCE);
        return adminPreferences;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminPreferences createUpdatedEntity(EntityManager em) {
        AdminPreferences adminPreferences = new AdminPreferences()
            .inactivityTime(UPDATED_INACTIVITY_TIME)
            .notificationRecurrence(UPDATED_NOTIFICATION_RECURRENCE);
        return adminPreferences;
    }

    @BeforeEach
    public void initTest() {
        adminPreferences = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdminPreferences() throws Exception {
        int databaseSizeBeforeCreate = adminPreferencesRepository.findAll().size();
        // Create the AdminPreferences
        restAdminPreferencesMockMvc.perform(post("/api/admin-preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adminPreferences)))
            .andExpect(status().isCreated());

        // Validate the AdminPreferences in the database
        List<AdminPreferences> adminPreferencesList = adminPreferencesRepository.findAll();
        assertThat(adminPreferencesList).hasSize(databaseSizeBeforeCreate + 1);
        AdminPreferences testAdminPreferences = adminPreferencesList.get(adminPreferencesList.size() - 1);
        assertThat(testAdminPreferences.getInactivityTime()).isEqualTo(DEFAULT_INACTIVITY_TIME);
        assertThat(testAdminPreferences.getNotificationRecurrence()).isEqualTo(DEFAULT_NOTIFICATION_RECURRENCE);
    }

    @Test
    @Transactional
    public void createAdminPreferencesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adminPreferencesRepository.findAll().size();

        // Create the AdminPreferences with an existing ID
        adminPreferences.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminPreferencesMockMvc.perform(post("/api/admin-preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adminPreferences)))
            .andExpect(status().isBadRequest());

        // Validate the AdminPreferences in the database
        List<AdminPreferences> adminPreferencesList = adminPreferencesRepository.findAll();
        assertThat(adminPreferencesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkInactivityTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminPreferencesRepository.findAll().size();
        // set the field null
        adminPreferences.setInactivityTime(null);

        // Create the AdminPreferences, which fails.


        restAdminPreferencesMockMvc.perform(post("/api/admin-preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adminPreferences)))
            .andExpect(status().isBadRequest());

        List<AdminPreferences> adminPreferencesList = adminPreferencesRepository.findAll();
        assertThat(adminPreferencesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNotificationRecurrenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminPreferencesRepository.findAll().size();
        // set the field null
        adminPreferences.setNotificationRecurrence(null);

        // Create the AdminPreferences, which fails.


        restAdminPreferencesMockMvc.perform(post("/api/admin-preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adminPreferences)))
            .andExpect(status().isBadRequest());

        List<AdminPreferences> adminPreferencesList = adminPreferencesRepository.findAll();
        assertThat(adminPreferencesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAdminPreferences() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList
        restAdminPreferencesMockMvc.perform(get("/api/admin-preferences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminPreferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].inactivityTime").value(hasItem(DEFAULT_INACTIVITY_TIME)))
            .andExpect(jsonPath("$.[*].notificationRecurrence").value(hasItem(DEFAULT_NOTIFICATION_RECURRENCE)));
    }
    
    @Test
    @Transactional
    public void getAdminPreferences() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get the adminPreferences
        restAdminPreferencesMockMvc.perform(get("/api/admin-preferences/{id}", adminPreferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adminPreferences.getId().intValue()))
            .andExpect(jsonPath("$.inactivityTime").value(DEFAULT_INACTIVITY_TIME))
            .andExpect(jsonPath("$.notificationRecurrence").value(DEFAULT_NOTIFICATION_RECURRENCE));
    }
    @Test
    @Transactional
    public void getNonExistingAdminPreferences() throws Exception {
        // Get the adminPreferences
        restAdminPreferencesMockMvc.perform(get("/api/admin-preferences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdminPreferences() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        int databaseSizeBeforeUpdate = adminPreferencesRepository.findAll().size();

        // Update the adminPreferences
        AdminPreferences updatedAdminPreferences = adminPreferencesRepository.findById(adminPreferences.getId()).get();
        // Disconnect from session so that the updates on updatedAdminPreferences are not directly saved in db
        em.detach(updatedAdminPreferences);
        updatedAdminPreferences
            .inactivityTime(UPDATED_INACTIVITY_TIME)
            .notificationRecurrence(UPDATED_NOTIFICATION_RECURRENCE);

        restAdminPreferencesMockMvc.perform(put("/api/admin-preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAdminPreferences)))
            .andExpect(status().isOk());

        // Validate the AdminPreferences in the database
        List<AdminPreferences> adminPreferencesList = adminPreferencesRepository.findAll();
        assertThat(adminPreferencesList).hasSize(databaseSizeBeforeUpdate);
        AdminPreferences testAdminPreferences = adminPreferencesList.get(adminPreferencesList.size() - 1);
        assertThat(testAdminPreferences.getInactivityTime()).isEqualTo(UPDATED_INACTIVITY_TIME);
        assertThat(testAdminPreferences.getNotificationRecurrence()).isEqualTo(UPDATED_NOTIFICATION_RECURRENCE);
    }

    @Test
    @Transactional
    public void updateNonExistingAdminPreferences() throws Exception {
        int databaseSizeBeforeUpdate = adminPreferencesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminPreferencesMockMvc.perform(put("/api/admin-preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adminPreferences)))
            .andExpect(status().isBadRequest());

        // Validate the AdminPreferences in the database
        List<AdminPreferences> adminPreferencesList = adminPreferencesRepository.findAll();
        assertThat(adminPreferencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAdminPreferences() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        int databaseSizeBeforeDelete = adminPreferencesRepository.findAll().size();

        // Delete the adminPreferences
        restAdminPreferencesMockMvc.perform(delete("/api/admin-preferences/{id}", adminPreferences.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdminPreferences> adminPreferencesList = adminPreferencesRepository.findAll();
        assertThat(adminPreferencesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
