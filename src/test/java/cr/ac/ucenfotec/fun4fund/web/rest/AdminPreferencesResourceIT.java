package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.AdminPreferences;
import cr.ac.ucenfotec.fun4fund.repository.AdminPreferencesRepository;
import cr.ac.ucenfotec.fun4fund.service.AdminPreferencesService;
import cr.ac.ucenfotec.fun4fund.service.dto.AdminPreferencesCriteria;
import cr.ac.ucenfotec.fun4fund.service.AdminPreferencesQueryService;

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
    private static final Integer SMALLER_INACTIVITY_TIME = 1 - 1;

    private static final Integer DEFAULT_NOTIFICATION_RECURRENCE = 1;
    private static final Integer UPDATED_NOTIFICATION_RECURRENCE = 2;
    private static final Integer SMALLER_NOTIFICATION_RECURRENCE = 1 - 1;

    @Autowired
    private AdminPreferencesRepository adminPreferencesRepository;

    @Autowired
    private AdminPreferencesService adminPreferencesService;

    @Autowired
    private AdminPreferencesQueryService adminPreferencesQueryService;

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
    public void getAdminPreferencesByIdFiltering() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        Long id = adminPreferences.getId();

        defaultAdminPreferencesShouldBeFound("id.equals=" + id);
        defaultAdminPreferencesShouldNotBeFound("id.notEquals=" + id);

        defaultAdminPreferencesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAdminPreferencesShouldNotBeFound("id.greaterThan=" + id);

        defaultAdminPreferencesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAdminPreferencesShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAdminPreferencesByInactivityTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where inactivityTime equals to DEFAULT_INACTIVITY_TIME
        defaultAdminPreferencesShouldBeFound("inactivityTime.equals=" + DEFAULT_INACTIVITY_TIME);

        // Get all the adminPreferencesList where inactivityTime equals to UPDATED_INACTIVITY_TIME
        defaultAdminPreferencesShouldNotBeFound("inactivityTime.equals=" + UPDATED_INACTIVITY_TIME);
    }

    @Test
    @Transactional
    public void getAllAdminPreferencesByInactivityTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where inactivityTime not equals to DEFAULT_INACTIVITY_TIME
        defaultAdminPreferencesShouldNotBeFound("inactivityTime.notEquals=" + DEFAULT_INACTIVITY_TIME);

        // Get all the adminPreferencesList where inactivityTime not equals to UPDATED_INACTIVITY_TIME
        defaultAdminPreferencesShouldBeFound("inactivityTime.notEquals=" + UPDATED_INACTIVITY_TIME);
    }

    @Test
    @Transactional
    public void getAllAdminPreferencesByInactivityTimeIsInShouldWork() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where inactivityTime in DEFAULT_INACTIVITY_TIME or UPDATED_INACTIVITY_TIME
        defaultAdminPreferencesShouldBeFound("inactivityTime.in=" + DEFAULT_INACTIVITY_TIME + "," + UPDATED_INACTIVITY_TIME);

        // Get all the adminPreferencesList where inactivityTime equals to UPDATED_INACTIVITY_TIME
        defaultAdminPreferencesShouldNotBeFound("inactivityTime.in=" + UPDATED_INACTIVITY_TIME);
    }

    @Test
    @Transactional
    public void getAllAdminPreferencesByInactivityTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where inactivityTime is not null
        defaultAdminPreferencesShouldBeFound("inactivityTime.specified=true");

        // Get all the adminPreferencesList where inactivityTime is null
        defaultAdminPreferencesShouldNotBeFound("inactivityTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllAdminPreferencesByInactivityTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where inactivityTime is greater than or equal to DEFAULT_INACTIVITY_TIME
        defaultAdminPreferencesShouldBeFound("inactivityTime.greaterThanOrEqual=" + DEFAULT_INACTIVITY_TIME);

        // Get all the adminPreferencesList where inactivityTime is greater than or equal to UPDATED_INACTIVITY_TIME
        defaultAdminPreferencesShouldNotBeFound("inactivityTime.greaterThanOrEqual=" + UPDATED_INACTIVITY_TIME);
    }

    @Test
    @Transactional
    public void getAllAdminPreferencesByInactivityTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where inactivityTime is less than or equal to DEFAULT_INACTIVITY_TIME
        defaultAdminPreferencesShouldBeFound("inactivityTime.lessThanOrEqual=" + DEFAULT_INACTIVITY_TIME);

        // Get all the adminPreferencesList where inactivityTime is less than or equal to SMALLER_INACTIVITY_TIME
        defaultAdminPreferencesShouldNotBeFound("inactivityTime.lessThanOrEqual=" + SMALLER_INACTIVITY_TIME);
    }

    @Test
    @Transactional
    public void getAllAdminPreferencesByInactivityTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where inactivityTime is less than DEFAULT_INACTIVITY_TIME
        defaultAdminPreferencesShouldNotBeFound("inactivityTime.lessThan=" + DEFAULT_INACTIVITY_TIME);

        // Get all the adminPreferencesList where inactivityTime is less than UPDATED_INACTIVITY_TIME
        defaultAdminPreferencesShouldBeFound("inactivityTime.lessThan=" + UPDATED_INACTIVITY_TIME);
    }

    @Test
    @Transactional
    public void getAllAdminPreferencesByInactivityTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where inactivityTime is greater than DEFAULT_INACTIVITY_TIME
        defaultAdminPreferencesShouldNotBeFound("inactivityTime.greaterThan=" + DEFAULT_INACTIVITY_TIME);

        // Get all the adminPreferencesList where inactivityTime is greater than SMALLER_INACTIVITY_TIME
        defaultAdminPreferencesShouldBeFound("inactivityTime.greaterThan=" + SMALLER_INACTIVITY_TIME);
    }


    @Test
    @Transactional
    public void getAllAdminPreferencesByNotificationRecurrenceIsEqualToSomething() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where notificationRecurrence equals to DEFAULT_NOTIFICATION_RECURRENCE
        defaultAdminPreferencesShouldBeFound("notificationRecurrence.equals=" + DEFAULT_NOTIFICATION_RECURRENCE);

        // Get all the adminPreferencesList where notificationRecurrence equals to UPDATED_NOTIFICATION_RECURRENCE
        defaultAdminPreferencesShouldNotBeFound("notificationRecurrence.equals=" + UPDATED_NOTIFICATION_RECURRENCE);
    }

    @Test
    @Transactional
    public void getAllAdminPreferencesByNotificationRecurrenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where notificationRecurrence not equals to DEFAULT_NOTIFICATION_RECURRENCE
        defaultAdminPreferencesShouldNotBeFound("notificationRecurrence.notEquals=" + DEFAULT_NOTIFICATION_RECURRENCE);

        // Get all the adminPreferencesList where notificationRecurrence not equals to UPDATED_NOTIFICATION_RECURRENCE
        defaultAdminPreferencesShouldBeFound("notificationRecurrence.notEquals=" + UPDATED_NOTIFICATION_RECURRENCE);
    }

    @Test
    @Transactional
    public void getAllAdminPreferencesByNotificationRecurrenceIsInShouldWork() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where notificationRecurrence in DEFAULT_NOTIFICATION_RECURRENCE or UPDATED_NOTIFICATION_RECURRENCE
        defaultAdminPreferencesShouldBeFound("notificationRecurrence.in=" + DEFAULT_NOTIFICATION_RECURRENCE + "," + UPDATED_NOTIFICATION_RECURRENCE);

        // Get all the adminPreferencesList where notificationRecurrence equals to UPDATED_NOTIFICATION_RECURRENCE
        defaultAdminPreferencesShouldNotBeFound("notificationRecurrence.in=" + UPDATED_NOTIFICATION_RECURRENCE);
    }

    @Test
    @Transactional
    public void getAllAdminPreferencesByNotificationRecurrenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where notificationRecurrence is not null
        defaultAdminPreferencesShouldBeFound("notificationRecurrence.specified=true");

        // Get all the adminPreferencesList where notificationRecurrence is null
        defaultAdminPreferencesShouldNotBeFound("notificationRecurrence.specified=false");
    }

    @Test
    @Transactional
    public void getAllAdminPreferencesByNotificationRecurrenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where notificationRecurrence is greater than or equal to DEFAULT_NOTIFICATION_RECURRENCE
        defaultAdminPreferencesShouldBeFound("notificationRecurrence.greaterThanOrEqual=" + DEFAULT_NOTIFICATION_RECURRENCE);

        // Get all the adminPreferencesList where notificationRecurrence is greater than or equal to UPDATED_NOTIFICATION_RECURRENCE
        defaultAdminPreferencesShouldNotBeFound("notificationRecurrence.greaterThanOrEqual=" + UPDATED_NOTIFICATION_RECURRENCE);
    }

    @Test
    @Transactional
    public void getAllAdminPreferencesByNotificationRecurrenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where notificationRecurrence is less than or equal to DEFAULT_NOTIFICATION_RECURRENCE
        defaultAdminPreferencesShouldBeFound("notificationRecurrence.lessThanOrEqual=" + DEFAULT_NOTIFICATION_RECURRENCE);

        // Get all the adminPreferencesList where notificationRecurrence is less than or equal to SMALLER_NOTIFICATION_RECURRENCE
        defaultAdminPreferencesShouldNotBeFound("notificationRecurrence.lessThanOrEqual=" + SMALLER_NOTIFICATION_RECURRENCE);
    }

    @Test
    @Transactional
    public void getAllAdminPreferencesByNotificationRecurrenceIsLessThanSomething() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where notificationRecurrence is less than DEFAULT_NOTIFICATION_RECURRENCE
        defaultAdminPreferencesShouldNotBeFound("notificationRecurrence.lessThan=" + DEFAULT_NOTIFICATION_RECURRENCE);

        // Get all the adminPreferencesList where notificationRecurrence is less than UPDATED_NOTIFICATION_RECURRENCE
        defaultAdminPreferencesShouldBeFound("notificationRecurrence.lessThan=" + UPDATED_NOTIFICATION_RECURRENCE);
    }

    @Test
    @Transactional
    public void getAllAdminPreferencesByNotificationRecurrenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        adminPreferencesRepository.saveAndFlush(adminPreferences);

        // Get all the adminPreferencesList where notificationRecurrence is greater than DEFAULT_NOTIFICATION_RECURRENCE
        defaultAdminPreferencesShouldNotBeFound("notificationRecurrence.greaterThan=" + DEFAULT_NOTIFICATION_RECURRENCE);

        // Get all the adminPreferencesList where notificationRecurrence is greater than SMALLER_NOTIFICATION_RECURRENCE
        defaultAdminPreferencesShouldBeFound("notificationRecurrence.greaterThan=" + SMALLER_NOTIFICATION_RECURRENCE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdminPreferencesShouldBeFound(String filter) throws Exception {
        restAdminPreferencesMockMvc.perform(get("/api/admin-preferences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminPreferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].inactivityTime").value(hasItem(DEFAULT_INACTIVITY_TIME)))
            .andExpect(jsonPath("$.[*].notificationRecurrence").value(hasItem(DEFAULT_NOTIFICATION_RECURRENCE)));

        // Check, that the count call also returns 1
        restAdminPreferencesMockMvc.perform(get("/api/admin-preferences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdminPreferencesShouldNotBeFound(String filter) throws Exception {
        restAdminPreferencesMockMvc.perform(get("/api/admin-preferences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdminPreferencesMockMvc.perform(get("/api/admin-preferences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        adminPreferencesService.save(adminPreferences);

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
        adminPreferencesService.save(adminPreferences);

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
