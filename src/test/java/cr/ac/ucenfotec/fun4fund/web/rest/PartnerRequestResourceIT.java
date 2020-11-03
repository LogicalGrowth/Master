package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.PartnerRequest;
import cr.ac.ucenfotec.fun4fund.repository.PartnerRequestRepository;

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

import cr.ac.ucenfotec.fun4fund.domain.enumeration.RequestStatus;
/**
 * Integration tests for the {@link PartnerRequestResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PartnerRequestResourceIT {

    private static final Double DEFAULT_AMOUNT = 0D;
    private static final Double UPDATED_AMOUNT = 1D;

    private static final RequestStatus DEFAULT_STATUS = RequestStatus.SEND;
    private static final RequestStatus UPDATED_STATUS = RequestStatus.RECEIVED;

    @Autowired
    private PartnerRequestRepository partnerRequestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartnerRequestMockMvc;

    private PartnerRequest partnerRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartnerRequest createEntity(EntityManager em) {
        PartnerRequest partnerRequest = new PartnerRequest()
            .amount(DEFAULT_AMOUNT)
            .status(DEFAULT_STATUS);
        return partnerRequest;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartnerRequest createUpdatedEntity(EntityManager em) {
        PartnerRequest partnerRequest = new PartnerRequest()
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS);
        return partnerRequest;
    }

    @BeforeEach
    public void initTest() {
        partnerRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createPartnerRequest() throws Exception {
        int databaseSizeBeforeCreate = partnerRequestRepository.findAll().size();
        // Create the PartnerRequest
        restPartnerRequestMockMvc.perform(post("/api/partner-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(partnerRequest)))
            .andExpect(status().isCreated());

        // Validate the PartnerRequest in the database
        List<PartnerRequest> partnerRequestList = partnerRequestRepository.findAll();
        assertThat(partnerRequestList).hasSize(databaseSizeBeforeCreate + 1);
        PartnerRequest testPartnerRequest = partnerRequestList.get(partnerRequestList.size() - 1);
        assertThat(testPartnerRequest.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPartnerRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createPartnerRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = partnerRequestRepository.findAll().size();

        // Create the PartnerRequest with an existing ID
        partnerRequest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartnerRequestMockMvc.perform(post("/api/partner-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(partnerRequest)))
            .andExpect(status().isBadRequest());

        // Validate the PartnerRequest in the database
        List<PartnerRequest> partnerRequestList = partnerRequestRepository.findAll();
        assertThat(partnerRequestList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = partnerRequestRepository.findAll().size();
        // set the field null
        partnerRequest.setAmount(null);

        // Create the PartnerRequest, which fails.


        restPartnerRequestMockMvc.perform(post("/api/partner-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(partnerRequest)))
            .andExpect(status().isBadRequest());

        List<PartnerRequest> partnerRequestList = partnerRequestRepository.findAll();
        assertThat(partnerRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = partnerRequestRepository.findAll().size();
        // set the field null
        partnerRequest.setStatus(null);

        // Create the PartnerRequest, which fails.


        restPartnerRequestMockMvc.perform(post("/api/partner-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(partnerRequest)))
            .andExpect(status().isBadRequest());

        List<PartnerRequest> partnerRequestList = partnerRequestRepository.findAll();
        assertThat(partnerRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPartnerRequests() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        // Get all the partnerRequestList
        restPartnerRequestMockMvc.perform(get("/api/partner-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partnerRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getPartnerRequest() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        // Get the partnerRequest
        restPartnerRequestMockMvc.perform(get("/api/partner-requests/{id}", partnerRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partnerRequest.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingPartnerRequest() throws Exception {
        // Get the partnerRequest
        restPartnerRequestMockMvc.perform(get("/api/partner-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePartnerRequest() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        int databaseSizeBeforeUpdate = partnerRequestRepository.findAll().size();

        // Update the partnerRequest
        PartnerRequest updatedPartnerRequest = partnerRequestRepository.findById(partnerRequest.getId()).get();
        // Disconnect from session so that the updates on updatedPartnerRequest are not directly saved in db
        em.detach(updatedPartnerRequest);
        updatedPartnerRequest
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS);

        restPartnerRequestMockMvc.perform(put("/api/partner-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPartnerRequest)))
            .andExpect(status().isOk());

        // Validate the PartnerRequest in the database
        List<PartnerRequest> partnerRequestList = partnerRequestRepository.findAll();
        assertThat(partnerRequestList).hasSize(databaseSizeBeforeUpdate);
        PartnerRequest testPartnerRequest = partnerRequestList.get(partnerRequestList.size() - 1);
        assertThat(testPartnerRequest.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPartnerRequest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingPartnerRequest() throws Exception {
        int databaseSizeBeforeUpdate = partnerRequestRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartnerRequestMockMvc.perform(put("/api/partner-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(partnerRequest)))
            .andExpect(status().isBadRequest());

        // Validate the PartnerRequest in the database
        List<PartnerRequest> partnerRequestList = partnerRequestRepository.findAll();
        assertThat(partnerRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePartnerRequest() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        int databaseSizeBeforeDelete = partnerRequestRepository.findAll().size();

        // Delete the partnerRequest
        restPartnerRequestMockMvc.perform(delete("/api/partner-requests/{id}", partnerRequest.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PartnerRequest> partnerRequestList = partnerRequestRepository.findAll();
        assertThat(partnerRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
