package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.ConfigSystem;
import cr.ac.ucenfotec.fun4fund.repository.ConfigSystemRepository;
import cr.ac.ucenfotec.fun4fund.service.ConfigSystemService;
import cr.ac.ucenfotec.fun4fund.service.dto.ConfigSystemCriteria;
import cr.ac.ucenfotec.fun4fund.service.ConfigSystemQueryService;

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
 * Integration tests for the {@link ConfigSystemResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ConfigSystemResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private ConfigSystemRepository configSystemRepository;

    @Autowired
    private ConfigSystemService configSystemService;

    @Autowired
    private ConfigSystemQueryService configSystemQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfigSystemMockMvc;

    private ConfigSystem configSystem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigSystem createEntity(EntityManager em) {
        ConfigSystem configSystem = new ConfigSystem()
            .type(DEFAULT_TYPE)
            .value(DEFAULT_VALUE);
        return configSystem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigSystem createUpdatedEntity(EntityManager em) {
        ConfigSystem configSystem = new ConfigSystem()
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE);
        return configSystem;
    }

    @BeforeEach
    public void initTest() {
        configSystem = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigSystem() throws Exception {
        int databaseSizeBeforeCreate = configSystemRepository.findAll().size();
        // Create the ConfigSystem
        restConfigSystemMockMvc.perform(post("/api/config-systems")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configSystem)))
            .andExpect(status().isCreated());

        // Validate the ConfigSystem in the database
        List<ConfigSystem> configSystemList = configSystemRepository.findAll();
        assertThat(configSystemList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigSystem testConfigSystem = configSystemList.get(configSystemList.size() - 1);
        assertThat(testConfigSystem.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testConfigSystem.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createConfigSystemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configSystemRepository.findAll().size();

        // Create the ConfigSystem with an existing ID
        configSystem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigSystemMockMvc.perform(post("/api/config-systems")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configSystem)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigSystem in the database
        List<ConfigSystem> configSystemList = configSystemRepository.findAll();
        assertThat(configSystemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSystemRepository.findAll().size();
        // set the field null
        configSystem.setType(null);

        // Create the ConfigSystem, which fails.


        restConfigSystemMockMvc.perform(post("/api/config-systems")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configSystem)))
            .andExpect(status().isBadRequest());

        List<ConfigSystem> configSystemList = configSystemRepository.findAll();
        assertThat(configSystemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSystemRepository.findAll().size();
        // set the field null
        configSystem.setValue(null);

        // Create the ConfigSystem, which fails.


        restConfigSystemMockMvc.perform(post("/api/config-systems")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configSystem)))
            .andExpect(status().isBadRequest());

        List<ConfigSystem> configSystemList = configSystemRepository.findAll();
        assertThat(configSystemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConfigSystems() throws Exception {
        // Initialize the database
        configSystemRepository.saveAndFlush(configSystem);

        // Get all the configSystemList
        restConfigSystemMockMvc.perform(get("/api/config-systems?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configSystem.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
    
    @Test
    @Transactional
    public void getConfigSystem() throws Exception {
        // Initialize the database
        configSystemRepository.saveAndFlush(configSystem);

        // Get the configSystem
        restConfigSystemMockMvc.perform(get("/api/config-systems/{id}", configSystem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configSystem.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }


    @Test
    @Transactional
    public void getConfigSystemsByIdFiltering() throws Exception {
        // Initialize the database
        configSystemRepository.saveAndFlush(configSystem);

        Long id = configSystem.getId();

        defaultConfigSystemShouldBeFound("id.equals=" + id);
        defaultConfigSystemShouldNotBeFound("id.notEquals=" + id);

        defaultConfigSystemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConfigSystemShouldNotBeFound("id.greaterThan=" + id);

        defaultConfigSystemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConfigSystemShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllConfigSystemsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        configSystemRepository.saveAndFlush(configSystem);

        // Get all the configSystemList where type equals to DEFAULT_TYPE
        defaultConfigSystemShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the configSystemList where type equals to UPDATED_TYPE
        defaultConfigSystemShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllConfigSystemsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        configSystemRepository.saveAndFlush(configSystem);

        // Get all the configSystemList where type not equals to DEFAULT_TYPE
        defaultConfigSystemShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the configSystemList where type not equals to UPDATED_TYPE
        defaultConfigSystemShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllConfigSystemsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        configSystemRepository.saveAndFlush(configSystem);

        // Get all the configSystemList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultConfigSystemShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the configSystemList where type equals to UPDATED_TYPE
        defaultConfigSystemShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllConfigSystemsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        configSystemRepository.saveAndFlush(configSystem);

        // Get all the configSystemList where type is not null
        defaultConfigSystemShouldBeFound("type.specified=true");

        // Get all the configSystemList where type is null
        defaultConfigSystemShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllConfigSystemsByTypeContainsSomething() throws Exception {
        // Initialize the database
        configSystemRepository.saveAndFlush(configSystem);

        // Get all the configSystemList where type contains DEFAULT_TYPE
        defaultConfigSystemShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the configSystemList where type contains UPDATED_TYPE
        defaultConfigSystemShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllConfigSystemsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        configSystemRepository.saveAndFlush(configSystem);

        // Get all the configSystemList where type does not contain DEFAULT_TYPE
        defaultConfigSystemShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the configSystemList where type does not contain UPDATED_TYPE
        defaultConfigSystemShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllConfigSystemsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        configSystemRepository.saveAndFlush(configSystem);

        // Get all the configSystemList where value equals to DEFAULT_VALUE
        defaultConfigSystemShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the configSystemList where value equals to UPDATED_VALUE
        defaultConfigSystemShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllConfigSystemsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        configSystemRepository.saveAndFlush(configSystem);

        // Get all the configSystemList where value not equals to DEFAULT_VALUE
        defaultConfigSystemShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the configSystemList where value not equals to UPDATED_VALUE
        defaultConfigSystemShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllConfigSystemsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        configSystemRepository.saveAndFlush(configSystem);

        // Get all the configSystemList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultConfigSystemShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the configSystemList where value equals to UPDATED_VALUE
        defaultConfigSystemShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllConfigSystemsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        configSystemRepository.saveAndFlush(configSystem);

        // Get all the configSystemList where value is not null
        defaultConfigSystemShouldBeFound("value.specified=true");

        // Get all the configSystemList where value is null
        defaultConfigSystemShouldNotBeFound("value.specified=false");
    }
                @Test
    @Transactional
    public void getAllConfigSystemsByValueContainsSomething() throws Exception {
        // Initialize the database
        configSystemRepository.saveAndFlush(configSystem);

        // Get all the configSystemList where value contains DEFAULT_VALUE
        defaultConfigSystemShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the configSystemList where value contains UPDATED_VALUE
        defaultConfigSystemShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllConfigSystemsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        configSystemRepository.saveAndFlush(configSystem);

        // Get all the configSystemList where value does not contain DEFAULT_VALUE
        defaultConfigSystemShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the configSystemList where value does not contain UPDATED_VALUE
        defaultConfigSystemShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConfigSystemShouldBeFound(String filter) throws Exception {
        restConfigSystemMockMvc.perform(get("/api/config-systems?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configSystem.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));

        // Check, that the count call also returns 1
        restConfigSystemMockMvc.perform(get("/api/config-systems/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConfigSystemShouldNotBeFound(String filter) throws Exception {
        restConfigSystemMockMvc.perform(get("/api/config-systems?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConfigSystemMockMvc.perform(get("/api/config-systems/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingConfigSystem() throws Exception {
        // Get the configSystem
        restConfigSystemMockMvc.perform(get("/api/config-systems/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigSystem() throws Exception {
        // Initialize the database
        configSystemService.save(configSystem);

        int databaseSizeBeforeUpdate = configSystemRepository.findAll().size();

        // Update the configSystem
        ConfigSystem updatedConfigSystem = configSystemRepository.findById(configSystem.getId()).get();
        // Disconnect from session so that the updates on updatedConfigSystem are not directly saved in db
        em.detach(updatedConfigSystem);
        updatedConfigSystem
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE);

        restConfigSystemMockMvc.perform(put("/api/config-systems")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfigSystem)))
            .andExpect(status().isOk());

        // Validate the ConfigSystem in the database
        List<ConfigSystem> configSystemList = configSystemRepository.findAll();
        assertThat(configSystemList).hasSize(databaseSizeBeforeUpdate);
        ConfigSystem testConfigSystem = configSystemList.get(configSystemList.size() - 1);
        assertThat(testConfigSystem.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testConfigSystem.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigSystem() throws Exception {
        int databaseSizeBeforeUpdate = configSystemRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigSystemMockMvc.perform(put("/api/config-systems")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configSystem)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigSystem in the database
        List<ConfigSystem> configSystemList = configSystemRepository.findAll();
        assertThat(configSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigSystem() throws Exception {
        // Initialize the database
        configSystemService.save(configSystem);

        int databaseSizeBeforeDelete = configSystemRepository.findAll().size();

        // Delete the configSystem
        restConfigSystemMockMvc.perform(delete("/api/config-systems/{id}", configSystem.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigSystem> configSystemList = configSystemRepository.findAll();
        assertThat(configSystemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
