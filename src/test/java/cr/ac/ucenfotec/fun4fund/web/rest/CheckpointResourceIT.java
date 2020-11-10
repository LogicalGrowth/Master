package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Checkpoint;
import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.repository.CheckpointRepository;
import cr.ac.ucenfotec.fun4fund.service.CheckpointService;
import cr.ac.ucenfotec.fun4fund.service.dto.CheckpointCriteria;
import cr.ac.ucenfotec.fun4fund.service.CheckpointQueryService;

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
 * Integration tests for the {@link CheckpointResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CheckpointResourceIT {

    private static final Double DEFAULT_COMPLETITION_PERCENTAGE = 1D;
    private static final Double UPDATED_COMPLETITION_PERCENTAGE = 2D;
    private static final Double SMALLER_COMPLETITION_PERCENTAGE = 1D - 1D;

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    @Autowired
    private CheckpointRepository checkpointRepository;

    @Autowired
    private CheckpointService checkpointService;

    @Autowired
    private CheckpointQueryService checkpointQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCheckpointMockMvc;

    private Checkpoint checkpoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Checkpoint createEntity(EntityManager em) {
        Checkpoint checkpoint = new Checkpoint()
            .completitionPercentage(DEFAULT_COMPLETITION_PERCENTAGE)
            .message(DEFAULT_MESSAGE)
            .completed(DEFAULT_COMPLETED);
        return checkpoint;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Checkpoint createUpdatedEntity(EntityManager em) {
        Checkpoint checkpoint = new Checkpoint()
            .completitionPercentage(UPDATED_COMPLETITION_PERCENTAGE)
            .message(UPDATED_MESSAGE)
            .completed(UPDATED_COMPLETED);
        return checkpoint;
    }

    @BeforeEach
    public void initTest() {
        checkpoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createCheckpoint() throws Exception {
        int databaseSizeBeforeCreate = checkpointRepository.findAll().size();
        // Create the Checkpoint
        restCheckpointMockMvc.perform(post("/api/checkpoints")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkpoint)))
            .andExpect(status().isCreated());

        // Validate the Checkpoint in the database
        List<Checkpoint> checkpointList = checkpointRepository.findAll();
        assertThat(checkpointList).hasSize(databaseSizeBeforeCreate + 1);
        Checkpoint testCheckpoint = checkpointList.get(checkpointList.size() - 1);
        assertThat(testCheckpoint.getCompletitionPercentage()).isEqualTo(DEFAULT_COMPLETITION_PERCENTAGE);
        assertThat(testCheckpoint.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testCheckpoint.isCompleted()).isEqualTo(DEFAULT_COMPLETED);
    }

    @Test
    @Transactional
    public void createCheckpointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = checkpointRepository.findAll().size();

        // Create the Checkpoint with an existing ID
        checkpoint.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCheckpointMockMvc.perform(post("/api/checkpoints")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkpoint)))
            .andExpect(status().isBadRequest());

        // Validate the Checkpoint in the database
        List<Checkpoint> checkpointList = checkpointRepository.findAll();
        assertThat(checkpointList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCompletitionPercentageIsRequired() throws Exception {
        int databaseSizeBeforeTest = checkpointRepository.findAll().size();
        // set the field null
        checkpoint.setCompletitionPercentage(null);

        // Create the Checkpoint, which fails.


        restCheckpointMockMvc.perform(post("/api/checkpoints")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkpoint)))
            .andExpect(status().isBadRequest());

        List<Checkpoint> checkpointList = checkpointRepository.findAll();
        assertThat(checkpointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = checkpointRepository.findAll().size();
        // set the field null
        checkpoint.setMessage(null);

        // Create the Checkpoint, which fails.


        restCheckpointMockMvc.perform(post("/api/checkpoints")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkpoint)))
            .andExpect(status().isBadRequest());

        List<Checkpoint> checkpointList = checkpointRepository.findAll();
        assertThat(checkpointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = checkpointRepository.findAll().size();
        // set the field null
        checkpoint.setCompleted(null);

        // Create the Checkpoint, which fails.


        restCheckpointMockMvc.perform(post("/api/checkpoints")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkpoint)))
            .andExpect(status().isBadRequest());

        List<Checkpoint> checkpointList = checkpointRepository.findAll();
        assertThat(checkpointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCheckpoints() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList
        restCheckpointMockMvc.perform(get("/api/checkpoints?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checkpoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].completitionPercentage").value(hasItem(DEFAULT_COMPLETITION_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCheckpoint() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get the checkpoint
        restCheckpointMockMvc.perform(get("/api/checkpoints/{id}", checkpoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(checkpoint.getId().intValue()))
            .andExpect(jsonPath("$.completitionPercentage").value(DEFAULT_COMPLETITION_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()));
    }


    @Test
    @Transactional
    public void getCheckpointsByIdFiltering() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        Long id = checkpoint.getId();

        defaultCheckpointShouldBeFound("id.equals=" + id);
        defaultCheckpointShouldNotBeFound("id.notEquals=" + id);

        defaultCheckpointShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCheckpointShouldNotBeFound("id.greaterThan=" + id);

        defaultCheckpointShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCheckpointShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCheckpointsByCompletitionPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where completitionPercentage equals to DEFAULT_COMPLETITION_PERCENTAGE
        defaultCheckpointShouldBeFound("completitionPercentage.equals=" + DEFAULT_COMPLETITION_PERCENTAGE);

        // Get all the checkpointList where completitionPercentage equals to UPDATED_COMPLETITION_PERCENTAGE
        defaultCheckpointShouldNotBeFound("completitionPercentage.equals=" + UPDATED_COMPLETITION_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllCheckpointsByCompletitionPercentageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where completitionPercentage not equals to DEFAULT_COMPLETITION_PERCENTAGE
        defaultCheckpointShouldNotBeFound("completitionPercentage.notEquals=" + DEFAULT_COMPLETITION_PERCENTAGE);

        // Get all the checkpointList where completitionPercentage not equals to UPDATED_COMPLETITION_PERCENTAGE
        defaultCheckpointShouldBeFound("completitionPercentage.notEquals=" + UPDATED_COMPLETITION_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllCheckpointsByCompletitionPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where completitionPercentage in DEFAULT_COMPLETITION_PERCENTAGE or UPDATED_COMPLETITION_PERCENTAGE
        defaultCheckpointShouldBeFound("completitionPercentage.in=" + DEFAULT_COMPLETITION_PERCENTAGE + "," + UPDATED_COMPLETITION_PERCENTAGE);

        // Get all the checkpointList where completitionPercentage equals to UPDATED_COMPLETITION_PERCENTAGE
        defaultCheckpointShouldNotBeFound("completitionPercentage.in=" + UPDATED_COMPLETITION_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllCheckpointsByCompletitionPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where completitionPercentage is not null
        defaultCheckpointShouldBeFound("completitionPercentage.specified=true");

        // Get all the checkpointList where completitionPercentage is null
        defaultCheckpointShouldNotBeFound("completitionPercentage.specified=false");
    }

    @Test
    @Transactional
    public void getAllCheckpointsByCompletitionPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where completitionPercentage is greater than or equal to DEFAULT_COMPLETITION_PERCENTAGE
        defaultCheckpointShouldBeFound("completitionPercentage.greaterThanOrEqual=" + DEFAULT_COMPLETITION_PERCENTAGE);

        // Get all the checkpointList where completitionPercentage is greater than or equal to UPDATED_COMPLETITION_PERCENTAGE
        defaultCheckpointShouldNotBeFound("completitionPercentage.greaterThanOrEqual=" + UPDATED_COMPLETITION_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllCheckpointsByCompletitionPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where completitionPercentage is less than or equal to DEFAULT_COMPLETITION_PERCENTAGE
        defaultCheckpointShouldBeFound("completitionPercentage.lessThanOrEqual=" + DEFAULT_COMPLETITION_PERCENTAGE);

        // Get all the checkpointList where completitionPercentage is less than or equal to SMALLER_COMPLETITION_PERCENTAGE
        defaultCheckpointShouldNotBeFound("completitionPercentage.lessThanOrEqual=" + SMALLER_COMPLETITION_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllCheckpointsByCompletitionPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where completitionPercentage is less than DEFAULT_COMPLETITION_PERCENTAGE
        defaultCheckpointShouldNotBeFound("completitionPercentage.lessThan=" + DEFAULT_COMPLETITION_PERCENTAGE);

        // Get all the checkpointList where completitionPercentage is less than UPDATED_COMPLETITION_PERCENTAGE
        defaultCheckpointShouldBeFound("completitionPercentage.lessThan=" + UPDATED_COMPLETITION_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllCheckpointsByCompletitionPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where completitionPercentage is greater than DEFAULT_COMPLETITION_PERCENTAGE
        defaultCheckpointShouldNotBeFound("completitionPercentage.greaterThan=" + DEFAULT_COMPLETITION_PERCENTAGE);

        // Get all the checkpointList where completitionPercentage is greater than SMALLER_COMPLETITION_PERCENTAGE
        defaultCheckpointShouldBeFound("completitionPercentage.greaterThan=" + SMALLER_COMPLETITION_PERCENTAGE);
    }


    @Test
    @Transactional
    public void getAllCheckpointsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where message equals to DEFAULT_MESSAGE
        defaultCheckpointShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the checkpointList where message equals to UPDATED_MESSAGE
        defaultCheckpointShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllCheckpointsByMessageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where message not equals to DEFAULT_MESSAGE
        defaultCheckpointShouldNotBeFound("message.notEquals=" + DEFAULT_MESSAGE);

        // Get all the checkpointList where message not equals to UPDATED_MESSAGE
        defaultCheckpointShouldBeFound("message.notEquals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllCheckpointsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultCheckpointShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the checkpointList where message equals to UPDATED_MESSAGE
        defaultCheckpointShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllCheckpointsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where message is not null
        defaultCheckpointShouldBeFound("message.specified=true");

        // Get all the checkpointList where message is null
        defaultCheckpointShouldNotBeFound("message.specified=false");
    }
                @Test
    @Transactional
    public void getAllCheckpointsByMessageContainsSomething() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where message contains DEFAULT_MESSAGE
        defaultCheckpointShouldBeFound("message.contains=" + DEFAULT_MESSAGE);

        // Get all the checkpointList where message contains UPDATED_MESSAGE
        defaultCheckpointShouldNotBeFound("message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllCheckpointsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where message does not contain DEFAULT_MESSAGE
        defaultCheckpointShouldNotBeFound("message.doesNotContain=" + DEFAULT_MESSAGE);

        // Get all the checkpointList where message does not contain UPDATED_MESSAGE
        defaultCheckpointShouldBeFound("message.doesNotContain=" + UPDATED_MESSAGE);
    }


    @Test
    @Transactional
    public void getAllCheckpointsByCompletedIsEqualToSomething() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where completed equals to DEFAULT_COMPLETED
        defaultCheckpointShouldBeFound("completed.equals=" + DEFAULT_COMPLETED);

        // Get all the checkpointList where completed equals to UPDATED_COMPLETED
        defaultCheckpointShouldNotBeFound("completed.equals=" + UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    public void getAllCheckpointsByCompletedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where completed not equals to DEFAULT_COMPLETED
        defaultCheckpointShouldNotBeFound("completed.notEquals=" + DEFAULT_COMPLETED);

        // Get all the checkpointList where completed not equals to UPDATED_COMPLETED
        defaultCheckpointShouldBeFound("completed.notEquals=" + UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    public void getAllCheckpointsByCompletedIsInShouldWork() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where completed in DEFAULT_COMPLETED or UPDATED_COMPLETED
        defaultCheckpointShouldBeFound("completed.in=" + DEFAULT_COMPLETED + "," + UPDATED_COMPLETED);

        // Get all the checkpointList where completed equals to UPDATED_COMPLETED
        defaultCheckpointShouldNotBeFound("completed.in=" + UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    public void getAllCheckpointsByCompletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

        // Get all the checkpointList where completed is not null
        defaultCheckpointShouldBeFound("completed.specified=true");

        // Get all the checkpointList where completed is null
        defaultCheckpointShouldNotBeFound("completed.specified=false");
    }

    @Test
    @Transactional
    public void getAllCheckpointsByProyectIsEqualToSomething() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);
        Proyect proyect = ProyectResourceIT.createEntity(em);
        em.persist(proyect);
        em.flush();
        checkpoint.setProyect(proyect);
        checkpointRepository.saveAndFlush(checkpoint);
        Long proyectId = proyect.getId();

        // Get all the checkpointList where proyect equals to proyectId
        defaultCheckpointShouldBeFound("proyectId.equals=" + proyectId);

        // Get all the checkpointList where proyect equals to proyectId + 1
        defaultCheckpointShouldNotBeFound("proyectId.equals=" + (proyectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCheckpointShouldBeFound(String filter) throws Exception {
        restCheckpointMockMvc.perform(get("/api/checkpoints?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checkpoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].completitionPercentage").value(hasItem(DEFAULT_COMPLETITION_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));

        // Check, that the count call also returns 1
        restCheckpointMockMvc.perform(get("/api/checkpoints/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCheckpointShouldNotBeFound(String filter) throws Exception {
        restCheckpointMockMvc.perform(get("/api/checkpoints?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCheckpointMockMvc.perform(get("/api/checkpoints/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCheckpoint() throws Exception {
        // Get the checkpoint
        restCheckpointMockMvc.perform(get("/api/checkpoints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCheckpoint() throws Exception {
        // Initialize the database
        checkpointService.save(checkpoint);

        int databaseSizeBeforeUpdate = checkpointRepository.findAll().size();

        // Update the checkpoint
        Checkpoint updatedCheckpoint = checkpointRepository.findById(checkpoint.getId()).get();
        // Disconnect from session so that the updates on updatedCheckpoint are not directly saved in db
        em.detach(updatedCheckpoint);
        updatedCheckpoint
            .completitionPercentage(UPDATED_COMPLETITION_PERCENTAGE)
            .message(UPDATED_MESSAGE)
            .completed(UPDATED_COMPLETED);

        restCheckpointMockMvc.perform(put("/api/checkpoints")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCheckpoint)))
            .andExpect(status().isOk());

        // Validate the Checkpoint in the database
        List<Checkpoint> checkpointList = checkpointRepository.findAll();
        assertThat(checkpointList).hasSize(databaseSizeBeforeUpdate);
        Checkpoint testCheckpoint = checkpointList.get(checkpointList.size() - 1);
        assertThat(testCheckpoint.getCompletitionPercentage()).isEqualTo(UPDATED_COMPLETITION_PERCENTAGE);
        assertThat(testCheckpoint.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testCheckpoint.isCompleted()).isEqualTo(UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    public void updateNonExistingCheckpoint() throws Exception {
        int databaseSizeBeforeUpdate = checkpointRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCheckpointMockMvc.perform(put("/api/checkpoints")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkpoint)))
            .andExpect(status().isBadRequest());

        // Validate the Checkpoint in the database
        List<Checkpoint> checkpointList = checkpointRepository.findAll();
        assertThat(checkpointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCheckpoint() throws Exception {
        // Initialize the database
        checkpointService.save(checkpoint);

        int databaseSizeBeforeDelete = checkpointRepository.findAll().size();

        // Delete the checkpoint
        restCheckpointMockMvc.perform(delete("/api/checkpoints/{id}", checkpoint.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Checkpoint> checkpointList = checkpointRepository.findAll();
        assertThat(checkpointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
