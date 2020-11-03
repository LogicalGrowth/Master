package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Checkpoint;
import cr.ac.ucenfotec.fun4fund.repository.CheckpointRepository;

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

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    @Autowired
    private CheckpointRepository checkpointRepository;

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
    public void getNonExistingCheckpoint() throws Exception {
        // Get the checkpoint
        restCheckpointMockMvc.perform(get("/api/checkpoints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCheckpoint() throws Exception {
        // Initialize the database
        checkpointRepository.saveAndFlush(checkpoint);

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
        checkpointRepository.saveAndFlush(checkpoint);

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
