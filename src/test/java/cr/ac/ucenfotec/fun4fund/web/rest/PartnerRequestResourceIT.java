package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.PartnerRequest;
import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.repository.PartnerRequestRepository;
import cr.ac.ucenfotec.fun4fund.service.PartnerRequestService;
import cr.ac.ucenfotec.fun4fund.service.dto.PartnerRequestCriteria;
import cr.ac.ucenfotec.fun4fund.service.PartnerRequestQueryService;

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
    private static final Double SMALLER_AMOUNT = 0D - 1D;

    private static final RequestStatus DEFAULT_STATUS = RequestStatus.SEND;
    private static final RequestStatus UPDATED_STATUS = RequestStatus.RECEIVED;

    @Autowired
    private PartnerRequestRepository partnerRequestRepository;

    @Autowired
    private PartnerRequestService partnerRequestService;

    @Autowired
    private PartnerRequestQueryService partnerRequestQueryService;

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
    public void getPartnerRequestsByIdFiltering() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        Long id = partnerRequest.getId();

        defaultPartnerRequestShouldBeFound("id.equals=" + id);
        defaultPartnerRequestShouldNotBeFound("id.notEquals=" + id);

        defaultPartnerRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPartnerRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultPartnerRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPartnerRequestShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPartnerRequestsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        // Get all the partnerRequestList where amount equals to DEFAULT_AMOUNT
        defaultPartnerRequestShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the partnerRequestList where amount equals to UPDATED_AMOUNT
        defaultPartnerRequestShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPartnerRequestsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        // Get all the partnerRequestList where amount not equals to DEFAULT_AMOUNT
        defaultPartnerRequestShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the partnerRequestList where amount not equals to UPDATED_AMOUNT
        defaultPartnerRequestShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPartnerRequestsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        // Get all the partnerRequestList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultPartnerRequestShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the partnerRequestList where amount equals to UPDATED_AMOUNT
        defaultPartnerRequestShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPartnerRequestsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        // Get all the partnerRequestList where amount is not null
        defaultPartnerRequestShouldBeFound("amount.specified=true");

        // Get all the partnerRequestList where amount is null
        defaultPartnerRequestShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPartnerRequestsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        // Get all the partnerRequestList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultPartnerRequestShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the partnerRequestList where amount is greater than or equal to UPDATED_AMOUNT
        defaultPartnerRequestShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPartnerRequestsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        // Get all the partnerRequestList where amount is less than or equal to DEFAULT_AMOUNT
        defaultPartnerRequestShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the partnerRequestList where amount is less than or equal to SMALLER_AMOUNT
        defaultPartnerRequestShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPartnerRequestsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        // Get all the partnerRequestList where amount is less than DEFAULT_AMOUNT
        defaultPartnerRequestShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the partnerRequestList where amount is less than UPDATED_AMOUNT
        defaultPartnerRequestShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPartnerRequestsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        // Get all the partnerRequestList where amount is greater than DEFAULT_AMOUNT
        defaultPartnerRequestShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the partnerRequestList where amount is greater than SMALLER_AMOUNT
        defaultPartnerRequestShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllPartnerRequestsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        // Get all the partnerRequestList where status equals to DEFAULT_STATUS
        defaultPartnerRequestShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the partnerRequestList where status equals to UPDATED_STATUS
        defaultPartnerRequestShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllPartnerRequestsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        // Get all the partnerRequestList where status not equals to DEFAULT_STATUS
        defaultPartnerRequestShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the partnerRequestList where status not equals to UPDATED_STATUS
        defaultPartnerRequestShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllPartnerRequestsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        // Get all the partnerRequestList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultPartnerRequestShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the partnerRequestList where status equals to UPDATED_STATUS
        defaultPartnerRequestShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllPartnerRequestsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);

        // Get all the partnerRequestList where status is not null
        defaultPartnerRequestShouldBeFound("status.specified=true");

        // Get all the partnerRequestList where status is null
        defaultPartnerRequestShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllPartnerRequestsByApplicantIsEqualToSomething() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);
        ApplicationUser applicant = ApplicationUserResourceIT.createEntity(em);
        em.persist(applicant);
        em.flush();
        partnerRequest.setApplicant(applicant);
        partnerRequestRepository.saveAndFlush(partnerRequest);
        Long applicantId = applicant.getId();

        // Get all the partnerRequestList where applicant equals to applicantId
        defaultPartnerRequestShouldBeFound("applicantId.equals=" + applicantId);

        // Get all the partnerRequestList where applicant equals to applicantId + 1
        defaultPartnerRequestShouldNotBeFound("applicantId.equals=" + (applicantId + 1));
    }


    @Test
    @Transactional
    public void getAllPartnerRequestsByProyectIsEqualToSomething() throws Exception {
        // Initialize the database
        partnerRequestRepository.saveAndFlush(partnerRequest);
        Proyect proyect = ProyectResourceIT.createEntity(em);
        em.persist(proyect);
        em.flush();
        partnerRequest.setProyect(proyect);
        partnerRequestRepository.saveAndFlush(partnerRequest);
        Long proyectId = proyect.getId();

        // Get all the partnerRequestList where proyect equals to proyectId
        defaultPartnerRequestShouldBeFound("proyectId.equals=" + proyectId);

        // Get all the partnerRequestList where proyect equals to proyectId + 1
        defaultPartnerRequestShouldNotBeFound("proyectId.equals=" + (proyectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartnerRequestShouldBeFound(String filter) throws Exception {
        restPartnerRequestMockMvc.perform(get("/api/partner-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partnerRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restPartnerRequestMockMvc.perform(get("/api/partner-requests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartnerRequestShouldNotBeFound(String filter) throws Exception {
        restPartnerRequestMockMvc.perform(get("/api/partner-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartnerRequestMockMvc.perform(get("/api/partner-requests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        partnerRequestService.save(partnerRequest);

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
        partnerRequestService.save(partnerRequest);

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
