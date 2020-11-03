package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.ExclusiveContent;
import cr.ac.ucenfotec.fun4fund.repository.ExclusiveContentRepository;

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

import cr.ac.ucenfotec.fun4fund.domain.enumeration.ActivityStatus;
/**
 * Integration tests for the {@link ExclusiveContentResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ExclusiveContentResourceIT {

    private static final Double DEFAULT_PRICE = 0D;
    private static final Double UPDATED_PRICE = 1D;

    private static final Integer DEFAULT_STOCK = 1;
    private static final Integer UPDATED_STOCK = 2;

    private static final ActivityStatus DEFAULT_STATE = ActivityStatus.ENABLED;
    private static final ActivityStatus UPDATED_STATE = ActivityStatus.DISABLED;

    @Autowired
    private ExclusiveContentRepository exclusiveContentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExclusiveContentMockMvc;

    private ExclusiveContent exclusiveContent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExclusiveContent createEntity(EntityManager em) {
        ExclusiveContent exclusiveContent = new ExclusiveContent()
            .price(DEFAULT_PRICE)
            .stock(DEFAULT_STOCK)
            .state(DEFAULT_STATE);
        return exclusiveContent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExclusiveContent createUpdatedEntity(EntityManager em) {
        ExclusiveContent exclusiveContent = new ExclusiveContent()
            .price(UPDATED_PRICE)
            .stock(UPDATED_STOCK)
            .state(UPDATED_STATE);
        return exclusiveContent;
    }

    @BeforeEach
    public void initTest() {
        exclusiveContent = createEntity(em);
    }

    @Test
    @Transactional
    public void createExclusiveContent() throws Exception {
        int databaseSizeBeforeCreate = exclusiveContentRepository.findAll().size();
        // Create the ExclusiveContent
        restExclusiveContentMockMvc.perform(post("/api/exclusive-contents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(exclusiveContent)))
            .andExpect(status().isCreated());

        // Validate the ExclusiveContent in the database
        List<ExclusiveContent> exclusiveContentList = exclusiveContentRepository.findAll();
        assertThat(exclusiveContentList).hasSize(databaseSizeBeforeCreate + 1);
        ExclusiveContent testExclusiveContent = exclusiveContentList.get(exclusiveContentList.size() - 1);
        assertThat(testExclusiveContent.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testExclusiveContent.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testExclusiveContent.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createExclusiveContentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = exclusiveContentRepository.findAll().size();

        // Create the ExclusiveContent with an existing ID
        exclusiveContent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExclusiveContentMockMvc.perform(post("/api/exclusive-contents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(exclusiveContent)))
            .andExpect(status().isBadRequest());

        // Validate the ExclusiveContent in the database
        List<ExclusiveContent> exclusiveContentList = exclusiveContentRepository.findAll();
        assertThat(exclusiveContentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = exclusiveContentRepository.findAll().size();
        // set the field null
        exclusiveContent.setPrice(null);

        // Create the ExclusiveContent, which fails.


        restExclusiveContentMockMvc.perform(post("/api/exclusive-contents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(exclusiveContent)))
            .andExpect(status().isBadRequest());

        List<ExclusiveContent> exclusiveContentList = exclusiveContentRepository.findAll();
        assertThat(exclusiveContentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = exclusiveContentRepository.findAll().size();
        // set the field null
        exclusiveContent.setState(null);

        // Create the ExclusiveContent, which fails.


        restExclusiveContentMockMvc.perform(post("/api/exclusive-contents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(exclusiveContent)))
            .andExpect(status().isBadRequest());

        List<ExclusiveContent> exclusiveContentList = exclusiveContentRepository.findAll();
        assertThat(exclusiveContentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExclusiveContents() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList
        restExclusiveContentMockMvc.perform(get("/api/exclusive-contents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exclusiveContent.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
    
    @Test
    @Transactional
    public void getExclusiveContent() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get the exclusiveContent
        restExclusiveContentMockMvc.perform(get("/api/exclusive-contents/{id}", exclusiveContent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exclusiveContent.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingExclusiveContent() throws Exception {
        // Get the exclusiveContent
        restExclusiveContentMockMvc.perform(get("/api/exclusive-contents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExclusiveContent() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        int databaseSizeBeforeUpdate = exclusiveContentRepository.findAll().size();

        // Update the exclusiveContent
        ExclusiveContent updatedExclusiveContent = exclusiveContentRepository.findById(exclusiveContent.getId()).get();
        // Disconnect from session so that the updates on updatedExclusiveContent are not directly saved in db
        em.detach(updatedExclusiveContent);
        updatedExclusiveContent
            .price(UPDATED_PRICE)
            .stock(UPDATED_STOCK)
            .state(UPDATED_STATE);

        restExclusiveContentMockMvc.perform(put("/api/exclusive-contents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedExclusiveContent)))
            .andExpect(status().isOk());

        // Validate the ExclusiveContent in the database
        List<ExclusiveContent> exclusiveContentList = exclusiveContentRepository.findAll();
        assertThat(exclusiveContentList).hasSize(databaseSizeBeforeUpdate);
        ExclusiveContent testExclusiveContent = exclusiveContentList.get(exclusiveContentList.size() - 1);
        assertThat(testExclusiveContent.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testExclusiveContent.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testExclusiveContent.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    public void updateNonExistingExclusiveContent() throws Exception {
        int databaseSizeBeforeUpdate = exclusiveContentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExclusiveContentMockMvc.perform(put("/api/exclusive-contents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(exclusiveContent)))
            .andExpect(status().isBadRequest());

        // Validate the ExclusiveContent in the database
        List<ExclusiveContent> exclusiveContentList = exclusiveContentRepository.findAll();
        assertThat(exclusiveContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExclusiveContent() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        int databaseSizeBeforeDelete = exclusiveContentRepository.findAll().size();

        // Delete the exclusiveContent
        restExclusiveContentMockMvc.perform(delete("/api/exclusive-contents/{id}", exclusiveContent.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExclusiveContent> exclusiveContentList = exclusiveContentRepository.findAll();
        assertThat(exclusiveContentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
