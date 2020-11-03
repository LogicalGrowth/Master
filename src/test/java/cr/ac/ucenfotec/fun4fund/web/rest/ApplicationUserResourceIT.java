package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.repository.ApplicationUserRepository;

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

import cr.ac.ucenfotec.fun4fund.domain.enumeration.IdType;
/**
 * Integration tests for the {@link ApplicationUserResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ApplicationUserResourceIT {

    private static final String DEFAULT_IDENTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATION = "BBBBBBBBBB";

    private static final IdType DEFAULT_ID_TYPE = IdType.PASSPORT;
    private static final IdType UPDATED_ID_TYPE = IdType.IDENTIFICATION;

    private static final ZonedDateTime DEFAULT_BIRTH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_BIRTH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ADMIN = false;
    private static final Boolean UPDATED_ADMIN = true;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationUserMockMvc;

    private ApplicationUser applicationUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser()
            .identification(DEFAULT_IDENTIFICATION)
            .idType(DEFAULT_ID_TYPE)
            .birthDate(DEFAULT_BIRTH_DATE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .admin(DEFAULT_ADMIN);
        return applicationUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createUpdatedEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser()
            .identification(UPDATED_IDENTIFICATION)
            .idType(UPDATED_ID_TYPE)
            .birthDate(UPDATED_BIRTH_DATE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .admin(UPDATED_ADMIN);
        return applicationUser;
    }

    @BeforeEach
    public void initTest() {
        applicationUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplicationUser() throws Exception {
        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();
        // Create the ApplicationUser
        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isCreated());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getIdentification()).isEqualTo(DEFAULT_IDENTIFICATION);
        assertThat(testApplicationUser.getIdType()).isEqualTo(DEFAULT_ID_TYPE);
        assertThat(testApplicationUser.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testApplicationUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testApplicationUser.isAdmin()).isEqualTo(DEFAULT_ADMIN);
    }

    @Test
    @Transactional
    public void createApplicationUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();

        // Create the ApplicationUser with an existing ID
        applicationUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkIdentificationIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationUserRepository.findAll().size();
        // set the field null
        applicationUser.setIdentification(null);

        // Create the ApplicationUser, which fails.


        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationUserRepository.findAll().size();
        // set the field null
        applicationUser.setIdType(null);

        // Create the ApplicationUser, which fails.


        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationUserRepository.findAll().size();
        // set the field null
        applicationUser.setBirthDate(null);

        // Create the ApplicationUser, which fails.


        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationUserRepository.findAll().size();
        // set the field null
        applicationUser.setPhoneNumber(null);

        // Create the ApplicationUser, which fails.


        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdminIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationUserRepository.findAll().size();
        // set the field null
        applicationUser.setAdmin(null);

        // Create the ApplicationUser, which fails.


        restApplicationUserMockMvc.perform(post("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplicationUsers() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList
        restApplicationUserMockMvc.perform(get("/api/application-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].identification").value(hasItem(DEFAULT_IDENTIFICATION)))
            .andExpect(jsonPath("$.[*].idType").value(hasItem(DEFAULT_ID_TYPE.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(sameInstant(DEFAULT_BIRTH_DATE))))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].admin").value(hasItem(DEFAULT_ADMIN.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get the applicationUser
        restApplicationUserMockMvc.perform(get("/api/application-users/{id}", applicationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationUser.getId().intValue()))
            .andExpect(jsonPath("$.identification").value(DEFAULT_IDENTIFICATION))
            .andExpect(jsonPath("$.idType").value(DEFAULT_ID_TYPE.toString()))
            .andExpect(jsonPath("$.birthDate").value(sameInstant(DEFAULT_BIRTH_DATE)))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.admin").value(DEFAULT_ADMIN.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingApplicationUser() throws Exception {
        // Get the applicationUser
        restApplicationUserMockMvc.perform(get("/api/application-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser
        ApplicationUser updatedApplicationUser = applicationUserRepository.findById(applicationUser.getId()).get();
        // Disconnect from session so that the updates on updatedApplicationUser are not directly saved in db
        em.detach(updatedApplicationUser);
        updatedApplicationUser
            .identification(UPDATED_IDENTIFICATION)
            .idType(UPDATED_ID_TYPE)
            .birthDate(UPDATED_BIRTH_DATE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .admin(UPDATED_ADMIN);

        restApplicationUserMockMvc.perform(put("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedApplicationUser)))
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getIdentification()).isEqualTo(UPDATED_IDENTIFICATION);
        assertThat(testApplicationUser.getIdType()).isEqualTo(UPDATED_ID_TYPE);
        assertThat(testApplicationUser.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testApplicationUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testApplicationUser.isAdmin()).isEqualTo(UPDATED_ADMIN);
    }

    @Test
    @Transactional
    public void updateNonExistingApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc.perform(put("/api/application-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeDelete = applicationUserRepository.findAll().size();

        // Delete the applicationUser
        restApplicationUserMockMvc.perform(delete("/api/application-users/{id}", applicationUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
