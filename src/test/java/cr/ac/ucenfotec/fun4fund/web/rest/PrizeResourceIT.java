package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Prize;
import cr.ac.ucenfotec.fun4fund.repository.PrizeRepository;

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
    public void getNonExistingPrize() throws Exception {
        // Get the prize
        restPrizeMockMvc.perform(get("/api/prizes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrize() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

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
        prizeRepository.saveAndFlush(prize);

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
