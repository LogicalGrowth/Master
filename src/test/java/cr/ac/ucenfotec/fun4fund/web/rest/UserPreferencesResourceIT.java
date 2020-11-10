package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.UserPreferences;
import cr.ac.ucenfotec.fun4fund.repository.UserPreferencesRepository;
import cr.ac.ucenfotec.fun4fund.service.UserPreferencesService;
import cr.ac.ucenfotec.fun4fund.service.dto.UserPreferencesCriteria;
import cr.ac.ucenfotec.fun4fund.service.UserPreferencesQueryService;

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
 * Integration tests for the {@link UserPreferencesResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class UserPreferencesResourceIT {

    private static final Boolean DEFAULT_NOTIFICATIONS = false;
    private static final Boolean UPDATED_NOTIFICATIONS = true;

    @Autowired
    private UserPreferencesRepository userPreferencesRepository;

    @Autowired
    private UserPreferencesService userPreferencesService;

    @Autowired
    private UserPreferencesQueryService userPreferencesQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserPreferencesMockMvc;

    private UserPreferences userPreferences;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPreferences createEntity(EntityManager em) {
        UserPreferences userPreferences = new UserPreferences()
            .notifications(DEFAULT_NOTIFICATIONS);
        return userPreferences;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPreferences createUpdatedEntity(EntityManager em) {
        UserPreferences userPreferences = new UserPreferences()
            .notifications(UPDATED_NOTIFICATIONS);
        return userPreferences;
    }

    @BeforeEach
    public void initTest() {
        userPreferences = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserPreferences() throws Exception {
        int databaseSizeBeforeCreate = userPreferencesRepository.findAll().size();
        // Create the UserPreferences
        restUserPreferencesMockMvc.perform(post("/api/user-preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userPreferences)))
            .andExpect(status().isCreated());

        // Validate the UserPreferences in the database
        List<UserPreferences> userPreferencesList = userPreferencesRepository.findAll();
        assertThat(userPreferencesList).hasSize(databaseSizeBeforeCreate + 1);
        UserPreferences testUserPreferences = userPreferencesList.get(userPreferencesList.size() - 1);
        assertThat(testUserPreferences.isNotifications()).isEqualTo(DEFAULT_NOTIFICATIONS);
    }

    @Test
    @Transactional
    public void createUserPreferencesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userPreferencesRepository.findAll().size();

        // Create the UserPreferences with an existing ID
        userPreferences.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPreferencesMockMvc.perform(post("/api/user-preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userPreferences)))
            .andExpect(status().isBadRequest());

        // Validate the UserPreferences in the database
        List<UserPreferences> userPreferencesList = userPreferencesRepository.findAll();
        assertThat(userPreferencesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNotificationsIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPreferencesRepository.findAll().size();
        // set the field null
        userPreferences.setNotifications(null);

        // Create the UserPreferences, which fails.


        restUserPreferencesMockMvc.perform(post("/api/user-preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userPreferences)))
            .andExpect(status().isBadRequest());

        List<UserPreferences> userPreferencesList = userPreferencesRepository.findAll();
        assertThat(userPreferencesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserPreferences() throws Exception {
        // Initialize the database
        userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList
        restUserPreferencesMockMvc.perform(get("/api/user-preferences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPreferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].notifications").value(hasItem(DEFAULT_NOTIFICATIONS.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getUserPreferences() throws Exception {
        // Initialize the database
        userPreferencesRepository.saveAndFlush(userPreferences);

        // Get the userPreferences
        restUserPreferencesMockMvc.perform(get("/api/user-preferences/{id}", userPreferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userPreferences.getId().intValue()))
            .andExpect(jsonPath("$.notifications").value(DEFAULT_NOTIFICATIONS.booleanValue()));
    }


    @Test
    @Transactional
    public void getUserPreferencesByIdFiltering() throws Exception {
        // Initialize the database
        userPreferencesRepository.saveAndFlush(userPreferences);

        Long id = userPreferences.getId();

        defaultUserPreferencesShouldBeFound("id.equals=" + id);
        defaultUserPreferencesShouldNotBeFound("id.notEquals=" + id);

        defaultUserPreferencesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserPreferencesShouldNotBeFound("id.greaterThan=" + id);

        defaultUserPreferencesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserPreferencesShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllUserPreferencesByNotificationsIsEqualToSomething() throws Exception {
        // Initialize the database
        userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where notifications equals to DEFAULT_NOTIFICATIONS
        defaultUserPreferencesShouldBeFound("notifications.equals=" + DEFAULT_NOTIFICATIONS);

        // Get all the userPreferencesList where notifications equals to UPDATED_NOTIFICATIONS
        defaultUserPreferencesShouldNotBeFound("notifications.equals=" + UPDATED_NOTIFICATIONS);
    }

    @Test
    @Transactional
    public void getAllUserPreferencesByNotificationsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where notifications not equals to DEFAULT_NOTIFICATIONS
        defaultUserPreferencesShouldNotBeFound("notifications.notEquals=" + DEFAULT_NOTIFICATIONS);

        // Get all the userPreferencesList where notifications not equals to UPDATED_NOTIFICATIONS
        defaultUserPreferencesShouldBeFound("notifications.notEquals=" + UPDATED_NOTIFICATIONS);
    }

    @Test
    @Transactional
    public void getAllUserPreferencesByNotificationsIsInShouldWork() throws Exception {
        // Initialize the database
        userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where notifications in DEFAULT_NOTIFICATIONS or UPDATED_NOTIFICATIONS
        defaultUserPreferencesShouldBeFound("notifications.in=" + DEFAULT_NOTIFICATIONS + "," + UPDATED_NOTIFICATIONS);

        // Get all the userPreferencesList where notifications equals to UPDATED_NOTIFICATIONS
        defaultUserPreferencesShouldNotBeFound("notifications.in=" + UPDATED_NOTIFICATIONS);
    }

    @Test
    @Transactional
    public void getAllUserPreferencesByNotificationsIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where notifications is not null
        defaultUserPreferencesShouldBeFound("notifications.specified=true");

        // Get all the userPreferencesList where notifications is null
        defaultUserPreferencesShouldNotBeFound("notifications.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserPreferencesShouldBeFound(String filter) throws Exception {
        restUserPreferencesMockMvc.perform(get("/api/user-preferences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPreferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].notifications").value(hasItem(DEFAULT_NOTIFICATIONS.booleanValue())));

        // Check, that the count call also returns 1
        restUserPreferencesMockMvc.perform(get("/api/user-preferences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserPreferencesShouldNotBeFound(String filter) throws Exception {
        restUserPreferencesMockMvc.perform(get("/api/user-preferences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserPreferencesMockMvc.perform(get("/api/user-preferences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingUserPreferences() throws Exception {
        // Get the userPreferences
        restUserPreferencesMockMvc.perform(get("/api/user-preferences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserPreferences() throws Exception {
        // Initialize the database
        userPreferencesService.save(userPreferences);

        int databaseSizeBeforeUpdate = userPreferencesRepository.findAll().size();

        // Update the userPreferences
        UserPreferences updatedUserPreferences = userPreferencesRepository.findById(userPreferences.getId()).get();
        // Disconnect from session so that the updates on updatedUserPreferences are not directly saved in db
        em.detach(updatedUserPreferences);
        updatedUserPreferences
            .notifications(UPDATED_NOTIFICATIONS);

        restUserPreferencesMockMvc.perform(put("/api/user-preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserPreferences)))
            .andExpect(status().isOk());

        // Validate the UserPreferences in the database
        List<UserPreferences> userPreferencesList = userPreferencesRepository.findAll();
        assertThat(userPreferencesList).hasSize(databaseSizeBeforeUpdate);
        UserPreferences testUserPreferences = userPreferencesList.get(userPreferencesList.size() - 1);
        assertThat(testUserPreferences.isNotifications()).isEqualTo(UPDATED_NOTIFICATIONS);
    }

    @Test
    @Transactional
    public void updateNonExistingUserPreferences() throws Exception {
        int databaseSizeBeforeUpdate = userPreferencesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPreferencesMockMvc.perform(put("/api/user-preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userPreferences)))
            .andExpect(status().isBadRequest());

        // Validate the UserPreferences in the database
        List<UserPreferences> userPreferencesList = userPreferencesRepository.findAll();
        assertThat(userPreferencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserPreferences() throws Exception {
        // Initialize the database
        userPreferencesService.save(userPreferences);

        int databaseSizeBeforeDelete = userPreferencesRepository.findAll().size();

        // Delete the userPreferences
        restUserPreferencesMockMvc.perform(delete("/api/user-preferences/{id}", userPreferences.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserPreferences> userPreferencesList = userPreferencesRepository.findAll();
        assertThat(userPreferencesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
