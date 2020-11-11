package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Prize;
import cr.ac.ucenfotec.fun4fund.domain.Resource;
import cr.ac.ucenfotec.fun4fund.repository.PrizeRepository;
import cr.ac.ucenfotec.fun4fund.service.PrizeService;
import cr.ac.ucenfotec.fun4fund.service.dto.PrizeCriteria;
import cr.ac.ucenfotec.fun4fund.service.PrizeQueryService;

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
 * Integration tests for the {@link PrizeResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PrizeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PrizeRepository prizeRepository;

    @Autowired
    private PrizeService prizeService;

    @Autowired
    private PrizeQueryService prizeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrizeMockMvc;

    private Prize prize;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prize createEntity(EntityManager em) {
        Prize prize = new Prize()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return prize;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prize createUpdatedEntity(EntityManager em) {
        Prize prize = new Prize()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return prize;
    }

    @BeforeEach
    public void initTest() {
        prize = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrize() throws Exception {
        int databaseSizeBeforeCreate = prizeRepository.findAll().size();
        // Create the Prize
        restPrizeMockMvc.perform(post("/api/prizes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(prize)))
            .andExpect(status().isCreated());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeCreate + 1);
        Prize testPrize = prizeList.get(prizeList.size() - 1);
        assertThat(testPrize.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrize.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createPrizeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = prizeRepository.findAll().size();

        // Create the Prize with an existing ID
        prize.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrizeMockMvc.perform(post("/api/prizes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(prize)))
            .andExpect(status().isBadRequest());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = prizeRepository.findAll().size();
        // set the field null
        prize.setName(null);

        // Create the Prize, which fails.


        restPrizeMockMvc.perform(post("/api/prizes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(prize)))
            .andExpect(status().isBadRequest());

        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = prizeRepository.findAll().size();
        // set the field null
        prize.setDescription(null);

        // Create the Prize, which fails.


        restPrizeMockMvc.perform(post("/api/prizes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(prize)))
            .andExpect(status().isBadRequest());

        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrizes() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList
        restPrizeMockMvc.perform(get("/api/prizes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prize.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getPrize() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get the prize
        restPrizeMockMvc.perform(get("/api/prizes/{id}", prize.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prize.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getPrizesByIdFiltering() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        Long id = prize.getId();

        defaultPrizeShouldBeFound("id.equals=" + id);
        defaultPrizeShouldNotBeFound("id.notEquals=" + id);

        defaultPrizeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrizeShouldNotBeFound("id.greaterThan=" + id);

        defaultPrizeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrizeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPrizesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where name equals to DEFAULT_NAME
        defaultPrizeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the prizeList where name equals to UPDATED_NAME
        defaultPrizeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPrizesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where name not equals to DEFAULT_NAME
        defaultPrizeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the prizeList where name not equals to UPDATED_NAME
        defaultPrizeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPrizesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPrizeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the prizeList where name equals to UPDATED_NAME
        defaultPrizeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPrizesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where name is not null
        defaultPrizeShouldBeFound("name.specified=true");

        // Get all the prizeList where name is null
        defaultPrizeShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPrizesByNameContainsSomething() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where name contains DEFAULT_NAME
        defaultPrizeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the prizeList where name contains UPDATED_NAME
        defaultPrizeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPrizesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where name does not contain DEFAULT_NAME
        defaultPrizeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the prizeList where name does not contain UPDATED_NAME
        defaultPrizeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllPrizesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where description equals to DEFAULT_DESCRIPTION
        defaultPrizeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the prizeList where description equals to UPDATED_DESCRIPTION
        defaultPrizeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPrizesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where description not equals to DEFAULT_DESCRIPTION
        defaultPrizeShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the prizeList where description not equals to UPDATED_DESCRIPTION
        defaultPrizeShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPrizesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPrizeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the prizeList where description equals to UPDATED_DESCRIPTION
        defaultPrizeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPrizesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where description is not null
        defaultPrizeShouldBeFound("description.specified=true");

        // Get all the prizeList where description is null
        defaultPrizeShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllPrizesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where description contains DEFAULT_DESCRIPTION
        defaultPrizeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the prizeList where description contains UPDATED_DESCRIPTION
        defaultPrizeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPrizesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where description does not contain DEFAULT_DESCRIPTION
        defaultPrizeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the prizeList where description does not contain UPDATED_DESCRIPTION
        defaultPrizeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllPrizesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);
        Resource image = ResourceResourceIT.createEntity(em);
        em.persist(image);
        em.flush();
        prize.addImage(image);
        prizeRepository.saveAndFlush(prize);
        Long imageId = image.getId();

        // Get all the prizeList where image equals to imageId
        defaultPrizeShouldBeFound("imageId.equals=" + imageId);

        // Get all the prizeList where image equals to imageId + 1
        defaultPrizeShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrizeShouldBeFound(String filter) throws Exception {
        restPrizeMockMvc.perform(get("/api/prizes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prize.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restPrizeMockMvc.perform(get("/api/prizes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrizeShouldNotBeFound(String filter) throws Exception {
        restPrizeMockMvc.perform(get("/api/prizes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrizeMockMvc.perform(get("/api/prizes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPrize() throws Exception {
        // Get the prize
        restPrizeMockMvc.perform(get("/api/prizes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrize() throws Exception {
        // Initialize the database
        prizeService.save(prize);

        int databaseSizeBeforeUpdate = prizeRepository.findAll().size();

        // Update the prize
        Prize updatedPrize = prizeRepository.findById(prize.getId()).get();
        // Disconnect from session so that the updates on updatedPrize are not directly saved in db
        em.detach(updatedPrize);
        updatedPrize
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restPrizeMockMvc.perform(put("/api/prizes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrize)))
            .andExpect(status().isOk());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeUpdate);
        Prize testPrize = prizeList.get(prizeList.size() - 1);
        assertThat(testPrize.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrize.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingPrize() throws Exception {
        int databaseSizeBeforeUpdate = prizeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrizeMockMvc.perform(put("/api/prizes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(prize)))
            .andExpect(status().isBadRequest());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePrize() throws Exception {
        // Initialize the database
        prizeService.save(prize);

        int databaseSizeBeforeDelete = prizeRepository.findAll().size();

        // Delete the prize
        restPrizeMockMvc.perform(delete("/api/prizes/{id}", prize.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
