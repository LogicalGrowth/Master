package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Resource;
import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.domain.Prize;
import cr.ac.ucenfotec.fun4fund.repository.ResourceRepository;
import cr.ac.ucenfotec.fun4fund.service.ResourceService;
import cr.ac.ucenfotec.fun4fund.service.dto.ResourceCriteria;
import cr.ac.ucenfotec.fun4fund.service.ResourceQueryService;

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
 * Integration tests for the {@link ResourceResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ResourceResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceQueryService resourceQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResourceMockMvc;

    private Resource resource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createEntity(EntityManager em) {
        Resource resource = new Resource()
            .url(DEFAULT_URL)
            .type(DEFAULT_TYPE);
        return resource;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createUpdatedEntity(EntityManager em) {
        Resource resource = new Resource()
            .url(UPDATED_URL)
            .type(UPDATED_TYPE);
        return resource;
    }

    @BeforeEach
    public void initTest() {
        resource = createEntity(em);
    }

    @Test
    @Transactional
    public void createResource() throws Exception {
        int databaseSizeBeforeCreate = resourceRepository.findAll().size();
        // Create the Resource
        restResourceMockMvc.perform(post("/api/resources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isCreated());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate + 1);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testResource.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createResourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resourceRepository.findAll().size();

        // Create the Resource with an existing ID
        resource.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceMockMvc.perform(post("/api/resources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceRepository.findAll().size();
        // set the field null
        resource.setUrl(null);

        // Create the Resource, which fails.


        restResourceMockMvc.perform(post("/api/resources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isBadRequest());

        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceRepository.findAll().size();
        // set the field null
        resource.setType(null);

        // Create the Resource, which fails.


        restResourceMockMvc.perform(post("/api/resources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isBadRequest());

        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResources() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList
        restResourceMockMvc.perform(get("/api/resources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get the resource
        restResourceMockMvc.perform(get("/api/resources/{id}", resource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resource.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }


    @Test
    @Transactional
    public void getResourcesByIdFiltering() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        Long id = resource.getId();

        defaultResourceShouldBeFound("id.equals=" + id);
        defaultResourceShouldNotBeFound("id.notEquals=" + id);

        defaultResourceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultResourceShouldNotBeFound("id.greaterThan=" + id);

        defaultResourceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultResourceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllResourcesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where url equals to DEFAULT_URL
        defaultResourceShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the resourceList where url equals to UPDATED_URL
        defaultResourceShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllResourcesByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where url not equals to DEFAULT_URL
        defaultResourceShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the resourceList where url not equals to UPDATED_URL
        defaultResourceShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllResourcesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where url in DEFAULT_URL or UPDATED_URL
        defaultResourceShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the resourceList where url equals to UPDATED_URL
        defaultResourceShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllResourcesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where url is not null
        defaultResourceShouldBeFound("url.specified=true");

        // Get all the resourceList where url is null
        defaultResourceShouldNotBeFound("url.specified=false");
    }
                @Test
    @Transactional
    public void getAllResourcesByUrlContainsSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where url contains DEFAULT_URL
        defaultResourceShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the resourceList where url contains UPDATED_URL
        defaultResourceShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllResourcesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where url does not contain DEFAULT_URL
        defaultResourceShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the resourceList where url does not contain UPDATED_URL
        defaultResourceShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }


    @Test
    @Transactional
    public void getAllResourcesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where type equals to DEFAULT_TYPE
        defaultResourceShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the resourceList where type equals to UPDATED_TYPE
        defaultResourceShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllResourcesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where type not equals to DEFAULT_TYPE
        defaultResourceShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the resourceList where type not equals to UPDATED_TYPE
        defaultResourceShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllResourcesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultResourceShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the resourceList where type equals to UPDATED_TYPE
        defaultResourceShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllResourcesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where type is not null
        defaultResourceShouldBeFound("type.specified=true");

        // Get all the resourceList where type is null
        defaultResourceShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllResourcesByTypeContainsSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where type contains DEFAULT_TYPE
        defaultResourceShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the resourceList where type contains UPDATED_TYPE
        defaultResourceShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllResourcesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where type does not contain DEFAULT_TYPE
        defaultResourceShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the resourceList where type does not contain UPDATED_TYPE
        defaultResourceShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllResourcesByProyectIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);
        Proyect proyect = ProyectResourceIT.createEntity(em);
        em.persist(proyect);
        em.flush();
        resource.setProyect(proyect);
        resourceRepository.saveAndFlush(resource);
        Long proyectId = proyect.getId();

        // Get all the resourceList where proyect equals to proyectId
        defaultResourceShouldBeFound("proyectId.equals=" + proyectId);

        // Get all the resourceList where proyect equals to proyectId + 1
        defaultResourceShouldNotBeFound("proyectId.equals=" + (proyectId + 1));
    }


    @Test
    @Transactional
    public void getAllResourcesByPrizeIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);
        Prize prize = PrizeResourceIT.createEntity(em);
        em.persist(prize);
        em.flush();
        resource.setPrize(prize);
        resourceRepository.saveAndFlush(resource);
        Long prizeId = prize.getId();

        // Get all the resourceList where prize equals to prizeId
        defaultResourceShouldBeFound("prizeId.equals=" + prizeId);

        // Get all the resourceList where prize equals to prizeId + 1
        defaultResourceShouldNotBeFound("prizeId.equals=" + (prizeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultResourceShouldBeFound(String filter) throws Exception {
        restResourceMockMvc.perform(get("/api/resources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restResourceMockMvc.perform(get("/api/resources/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultResourceShouldNotBeFound(String filter) throws Exception {
        restResourceMockMvc.perform(get("/api/resources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restResourceMockMvc.perform(get("/api/resources/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingResource() throws Exception {
        // Get the resource
        restResourceMockMvc.perform(get("/api/resources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResource() throws Exception {
        // Initialize the database
        resourceService.save(resource);

        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Update the resource
        Resource updatedResource = resourceRepository.findById(resource.getId()).get();
        // Disconnect from session so that the updates on updatedResource are not directly saved in db
        em.detach(updatedResource);
        updatedResource
            .url(UPDATED_URL)
            .type(UPDATED_TYPE);

        restResourceMockMvc.perform(put("/api/resources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedResource)))
            .andExpect(status().isOk());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testResource.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceMockMvc.perform(put("/api/resources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteResource() throws Exception {
        // Initialize the database
        resourceService.save(resource);

        int databaseSizeBeforeDelete = resourceRepository.findAll().size();

        // Delete the resource
        restResourceMockMvc.perform(delete("/api/resources/{id}", resource.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
