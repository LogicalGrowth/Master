package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.ExclusiveContent;
import cr.ac.ucenfotec.fun4fund.domain.Prize;
import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.repository.ExclusiveContentRepository;
import cr.ac.ucenfotec.fun4fund.service.dto.ExclusiveContentCriteria;
import cr.ac.ucenfotec.fun4fund.service.ExclusiveContentQueryService;

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
    private static final Double SMALLER_PRICE = 0D - 1D;

    private static final Integer DEFAULT_STOCK = 0;
    private static final Integer UPDATED_STOCK = 1;
    private static final Integer SMALLER_STOCK = 0 - 1;

    private static final ActivityStatus DEFAULT_STATE = ActivityStatus.ENABLED;
    private static final ActivityStatus UPDATED_STATE = ActivityStatus.DISABLED;

    @Autowired
    private ExclusiveContentRepository exclusiveContentRepository;

    @Autowired
    private ExclusiveContentQueryService exclusiveContentQueryService;

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
    public void getExclusiveContentsByIdFiltering() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        Long id = exclusiveContent.getId();

        defaultExclusiveContentShouldBeFound("id.equals=" + id);
        defaultExclusiveContentShouldNotBeFound("id.notEquals=" + id);

        defaultExclusiveContentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExclusiveContentShouldNotBeFound("id.greaterThan=" + id);

        defaultExclusiveContentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExclusiveContentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllExclusiveContentsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where price equals to DEFAULT_PRICE
        defaultExclusiveContentShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the exclusiveContentList where price equals to UPDATED_PRICE
        defaultExclusiveContentShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where price not equals to DEFAULT_PRICE
        defaultExclusiveContentShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the exclusiveContentList where price not equals to UPDATED_PRICE
        defaultExclusiveContentShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultExclusiveContentShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the exclusiveContentList where price equals to UPDATED_PRICE
        defaultExclusiveContentShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where price is not null
        defaultExclusiveContentShouldBeFound("price.specified=true");

        // Get all the exclusiveContentList where price is null
        defaultExclusiveContentShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where price is greater than or equal to DEFAULT_PRICE
        defaultExclusiveContentShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the exclusiveContentList where price is greater than or equal to UPDATED_PRICE
        defaultExclusiveContentShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where price is less than or equal to DEFAULT_PRICE
        defaultExclusiveContentShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the exclusiveContentList where price is less than or equal to SMALLER_PRICE
        defaultExclusiveContentShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where price is less than DEFAULT_PRICE
        defaultExclusiveContentShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the exclusiveContentList where price is less than UPDATED_PRICE
        defaultExclusiveContentShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where price is greater than DEFAULT_PRICE
        defaultExclusiveContentShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the exclusiveContentList where price is greater than SMALLER_PRICE
        defaultExclusiveContentShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }


    @Test
    @Transactional
    public void getAllExclusiveContentsByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where stock equals to DEFAULT_STOCK
        defaultExclusiveContentShouldBeFound("stock.equals=" + DEFAULT_STOCK);

        // Get all the exclusiveContentList where stock equals to UPDATED_STOCK
        defaultExclusiveContentShouldNotBeFound("stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByStockIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where stock not equals to DEFAULT_STOCK
        defaultExclusiveContentShouldNotBeFound("stock.notEquals=" + DEFAULT_STOCK);

        // Get all the exclusiveContentList where stock not equals to UPDATED_STOCK
        defaultExclusiveContentShouldBeFound("stock.notEquals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByStockIsInShouldWork() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where stock in DEFAULT_STOCK or UPDATED_STOCK
        defaultExclusiveContentShouldBeFound("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK);

        // Get all the exclusiveContentList where stock equals to UPDATED_STOCK
        defaultExclusiveContentShouldNotBeFound("stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where stock is not null
        defaultExclusiveContentShouldBeFound("stock.specified=true");

        // Get all the exclusiveContentList where stock is null
        defaultExclusiveContentShouldNotBeFound("stock.specified=false");
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where stock is greater than or equal to DEFAULT_STOCK
        defaultExclusiveContentShouldBeFound("stock.greaterThanOrEqual=" + DEFAULT_STOCK);

        // Get all the exclusiveContentList where stock is greater than or equal to UPDATED_STOCK
        defaultExclusiveContentShouldNotBeFound("stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where stock is less than or equal to DEFAULT_STOCK
        defaultExclusiveContentShouldBeFound("stock.lessThanOrEqual=" + DEFAULT_STOCK);

        // Get all the exclusiveContentList where stock is less than or equal to SMALLER_STOCK
        defaultExclusiveContentShouldNotBeFound("stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where stock is less than DEFAULT_STOCK
        defaultExclusiveContentShouldNotBeFound("stock.lessThan=" + DEFAULT_STOCK);

        // Get all the exclusiveContentList where stock is less than UPDATED_STOCK
        defaultExclusiveContentShouldBeFound("stock.lessThan=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where stock is greater than DEFAULT_STOCK
        defaultExclusiveContentShouldNotBeFound("stock.greaterThan=" + DEFAULT_STOCK);

        // Get all the exclusiveContentList where stock is greater than SMALLER_STOCK
        defaultExclusiveContentShouldBeFound("stock.greaterThan=" + SMALLER_STOCK);
    }


    @Test
    @Transactional
    public void getAllExclusiveContentsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where state equals to DEFAULT_STATE
        defaultExclusiveContentShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the exclusiveContentList where state equals to UPDATED_STATE
        defaultExclusiveContentShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where state not equals to DEFAULT_STATE
        defaultExclusiveContentShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the exclusiveContentList where state not equals to UPDATED_STATE
        defaultExclusiveContentShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where state in DEFAULT_STATE or UPDATED_STATE
        defaultExclusiveContentShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the exclusiveContentList where state equals to UPDATED_STATE
        defaultExclusiveContentShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);

        // Get all the exclusiveContentList where state is not null
        defaultExclusiveContentShouldBeFound("state.specified=true");

        // Get all the exclusiveContentList where state is null
        defaultExclusiveContentShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllExclusiveContentsByPrizeIsEqualToSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);
        Prize prize = PrizeResourceIT.createEntity(em);
        em.persist(prize);
        em.flush();
        exclusiveContent.setPrize(prize);
        exclusiveContentRepository.saveAndFlush(exclusiveContent);
        Long prizeId = prize.getId();

        // Get all the exclusiveContentList where prize equals to prizeId
        defaultExclusiveContentShouldBeFound("prizeId.equals=" + prizeId);

        // Get all the exclusiveContentList where prize equals to prizeId + 1
        defaultExclusiveContentShouldNotBeFound("prizeId.equals=" + (prizeId + 1));
    }


    @Test
    @Transactional
    public void getAllExclusiveContentsByProyectIsEqualToSomething() throws Exception {
        // Initialize the database
        exclusiveContentRepository.saveAndFlush(exclusiveContent);
        Proyect proyect = ProyectResourceIT.createEntity(em);
        em.persist(proyect);
        em.flush();
        exclusiveContent.setProyect(proyect);
        exclusiveContentRepository.saveAndFlush(exclusiveContent);
        Long proyectId = proyect.getId();

        // Get all the exclusiveContentList where proyect equals to proyectId
        defaultExclusiveContentShouldBeFound("proyectId.equals=" + proyectId);

        // Get all the exclusiveContentList where proyect equals to proyectId + 1
        defaultExclusiveContentShouldNotBeFound("proyectId.equals=" + (proyectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExclusiveContentShouldBeFound(String filter) throws Exception {
        restExclusiveContentMockMvc.perform(get("/api/exclusive-contents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exclusiveContent.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));

        // Check, that the count call also returns 1
        restExclusiveContentMockMvc.perform(get("/api/exclusive-contents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExclusiveContentShouldNotBeFound(String filter) throws Exception {
        restExclusiveContentMockMvc.perform(get("/api/exclusive-contents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExclusiveContentMockMvc.perform(get("/api/exclusive-contents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
