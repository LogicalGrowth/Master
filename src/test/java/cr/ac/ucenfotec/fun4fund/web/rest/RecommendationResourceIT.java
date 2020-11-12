package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Recommendation;
import cr.ac.ucenfotec.fun4fund.repository.RecommendationRepository;
import cr.ac.ucenfotec.fun4fund.service.RecommendationService;
import cr.ac.ucenfotec.fun4fund.service.dto.RecommendationCriteria;
import cr.ac.ucenfotec.fun4fund.service.RecommendationQueryService;

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
 * Integration tests for the {@link RecommendationResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RecommendationResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private RecommendationQueryService recommendationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecommendationMockMvc;

    private Recommendation recommendation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recommendation createEntity(EntityManager em) {
        Recommendation recommendation = new Recommendation()
            .description(DEFAULT_DESCRIPTION);
        return recommendation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recommendation createUpdatedEntity(EntityManager em) {
        Recommendation recommendation = new Recommendation()
            .description(UPDATED_DESCRIPTION);
        return recommendation;
    }

    @BeforeEach
    public void initTest() {
        recommendation = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecommendation() throws Exception {
        int databaseSizeBeforeCreate = recommendationRepository.findAll().size();
        // Create the Recommendation
        restRecommendationMockMvc.perform(post("/api/recommendations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(recommendation)))
            .andExpect(status().isCreated());

        // Validate the Recommendation in the database
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeCreate + 1);
        Recommendation testRecommendation = recommendationList.get(recommendationList.size() - 1);
        assertThat(testRecommendation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createRecommendationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recommendationRepository.findAll().size();

        // Create the Recommendation with an existing ID
        recommendation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecommendationMockMvc.perform(post("/api/recommendations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(recommendation)))
            .andExpect(status().isBadRequest());

        // Validate the Recommendation in the database
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = recommendationRepository.findAll().size();
        // set the field null
        recommendation.setDescription(null);

        // Create the Recommendation, which fails.


        restRecommendationMockMvc.perform(post("/api/recommendations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(recommendation)))
            .andExpect(status().isBadRequest());

        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecommendations() throws Exception {
        // Initialize the database
        recommendationRepository.saveAndFlush(recommendation);

        // Get all the recommendationList
        restRecommendationMockMvc.perform(get("/api/recommendations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recommendation.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getRecommendation() throws Exception {
        // Initialize the database
        recommendationRepository.saveAndFlush(recommendation);

        // Get the recommendation
        restRecommendationMockMvc.perform(get("/api/recommendations/{id}", recommendation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recommendation.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getRecommendationsByIdFiltering() throws Exception {
        // Initialize the database
        recommendationRepository.saveAndFlush(recommendation);

        Long id = recommendation.getId();

        defaultRecommendationShouldBeFound("id.equals=" + id);
        defaultRecommendationShouldNotBeFound("id.notEquals=" + id);

        defaultRecommendationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRecommendationShouldNotBeFound("id.greaterThan=" + id);

        defaultRecommendationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRecommendationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRecommendationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        recommendationRepository.saveAndFlush(recommendation);

        // Get all the recommendationList where description equals to DEFAULT_DESCRIPTION
        defaultRecommendationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the recommendationList where description equals to UPDATED_DESCRIPTION
        defaultRecommendationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRecommendationsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        recommendationRepository.saveAndFlush(recommendation);

        // Get all the recommendationList where description not equals to DEFAULT_DESCRIPTION
        defaultRecommendationShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the recommendationList where description not equals to UPDATED_DESCRIPTION
        defaultRecommendationShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRecommendationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        recommendationRepository.saveAndFlush(recommendation);

        // Get all the recommendationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRecommendationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the recommendationList where description equals to UPDATED_DESCRIPTION
        defaultRecommendationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRecommendationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        recommendationRepository.saveAndFlush(recommendation);

        // Get all the recommendationList where description is not null
        defaultRecommendationShouldBeFound("description.specified=true");

        // Get all the recommendationList where description is null
        defaultRecommendationShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllRecommendationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        recommendationRepository.saveAndFlush(recommendation);

        // Get all the recommendationList where description contains DEFAULT_DESCRIPTION
        defaultRecommendationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the recommendationList where description contains UPDATED_DESCRIPTION
        defaultRecommendationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRecommendationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        recommendationRepository.saveAndFlush(recommendation);

        // Get all the recommendationList where description does not contain DEFAULT_DESCRIPTION
        defaultRecommendationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the recommendationList where description does not contain UPDATED_DESCRIPTION
        defaultRecommendationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRecommendationShouldBeFound(String filter) throws Exception {
        restRecommendationMockMvc.perform(get("/api/recommendations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recommendation.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restRecommendationMockMvc.perform(get("/api/recommendations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRecommendationShouldNotBeFound(String filter) throws Exception {
        restRecommendationMockMvc.perform(get("/api/recommendations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecommendationMockMvc.perform(get("/api/recommendations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRecommendation() throws Exception {
        // Get the recommendation
        restRecommendationMockMvc.perform(get("/api/recommendations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecommendation() throws Exception {
        // Initialize the database
        recommendationService.save(recommendation);

        int databaseSizeBeforeUpdate = recommendationRepository.findAll().size();

        // Update the recommendation
        Recommendation updatedRecommendation = recommendationRepository.findById(recommendation.getId()).get();
        // Disconnect from session so that the updates on updatedRecommendation are not directly saved in db
        em.detach(updatedRecommendation);
        updatedRecommendation
            .description(UPDATED_DESCRIPTION);

        restRecommendationMockMvc.perform(put("/api/recommendations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecommendation)))
            .andExpect(status().isOk());

        // Validate the Recommendation in the database
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeUpdate);
        Recommendation testRecommendation = recommendationList.get(recommendationList.size() - 1);
        assertThat(testRecommendation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingRecommendation() throws Exception {
        int databaseSizeBeforeUpdate = recommendationRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecommendationMockMvc.perform(put("/api/recommendations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(recommendation)))
            .andExpect(status().isBadRequest());

        // Validate the Recommendation in the database
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRecommendation() throws Exception {
        // Initialize the database
        recommendationService.save(recommendation);

        int databaseSizeBeforeDelete = recommendationRepository.findAll().size();

        // Delete the recommendation
        restRecommendationMockMvc.perform(delete("/api/recommendations/{id}", recommendation.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Recommendation> recommendationList = recommendationRepository.findAll();
        assertThat(recommendationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
