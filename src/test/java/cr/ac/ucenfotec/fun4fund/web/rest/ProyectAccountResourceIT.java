package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.ProyectAccount;
import cr.ac.ucenfotec.fun4fund.repository.ProyectAccountRepository;

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

import cr.ac.ucenfotec.fun4fund.domain.enumeration.Currency;
/**
 * Integration tests for the {@link ProyectAccountResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProyectAccountResourceIT {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final Currency DEFAULT_CURRENCY_TYPE = Currency.USD;
    private static final Currency UPDATED_CURRENCY_TYPE = Currency.CRC;

    @Autowired
    private ProyectAccountRepository proyectAccountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProyectAccountMockMvc;

    private ProyectAccount proyectAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProyectAccount createEntity(EntityManager em) {
        ProyectAccount proyectAccount = new ProyectAccount()
            .number(DEFAULT_NUMBER)
            .currencyType(DEFAULT_CURRENCY_TYPE);
        return proyectAccount;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProyectAccount createUpdatedEntity(EntityManager em) {
        ProyectAccount proyectAccount = new ProyectAccount()
            .number(UPDATED_NUMBER)
            .currencyType(UPDATED_CURRENCY_TYPE);
        return proyectAccount;
    }

    @BeforeEach
    public void initTest() {
        proyectAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createProyectAccount() throws Exception {
        int databaseSizeBeforeCreate = proyectAccountRepository.findAll().size();
        // Create the ProyectAccount
        restProyectAccountMockMvc.perform(post("/api/proyect-accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyectAccount)))
            .andExpect(status().isCreated());

        // Validate the ProyectAccount in the database
        List<ProyectAccount> proyectAccountList = proyectAccountRepository.findAll();
        assertThat(proyectAccountList).hasSize(databaseSizeBeforeCreate + 1);
        ProyectAccount testProyectAccount = proyectAccountList.get(proyectAccountList.size() - 1);
        assertThat(testProyectAccount.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testProyectAccount.getCurrencyType()).isEqualTo(DEFAULT_CURRENCY_TYPE);
    }

    @Test
    @Transactional
    public void createProyectAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = proyectAccountRepository.findAll().size();

        // Create the ProyectAccount with an existing ID
        proyectAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProyectAccountMockMvc.perform(post("/api/proyect-accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyectAccount)))
            .andExpect(status().isBadRequest());

        // Validate the ProyectAccount in the database
        List<ProyectAccount> proyectAccountList = proyectAccountRepository.findAll();
        assertThat(proyectAccountList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectAccountRepository.findAll().size();
        // set the field null
        proyectAccount.setNumber(null);

        // Create the ProyectAccount, which fails.


        restProyectAccountMockMvc.perform(post("/api/proyect-accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyectAccount)))
            .andExpect(status().isBadRequest());

        List<ProyectAccount> proyectAccountList = proyectAccountRepository.findAll();
        assertThat(proyectAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrencyTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectAccountRepository.findAll().size();
        // set the field null
        proyectAccount.setCurrencyType(null);

        // Create the ProyectAccount, which fails.


        restProyectAccountMockMvc.perform(post("/api/proyect-accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyectAccount)))
            .andExpect(status().isBadRequest());

        List<ProyectAccount> proyectAccountList = proyectAccountRepository.findAll();
        assertThat(proyectAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProyectAccounts() throws Exception {
        // Initialize the database
        proyectAccountRepository.saveAndFlush(proyectAccount);

        // Get all the proyectAccountList
        restProyectAccountMockMvc.perform(get("/api/proyect-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proyectAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].currencyType").value(hasItem(DEFAULT_CURRENCY_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getProyectAccount() throws Exception {
        // Initialize the database
        proyectAccountRepository.saveAndFlush(proyectAccount);

        // Get the proyectAccount
        restProyectAccountMockMvc.perform(get("/api/proyect-accounts/{id}", proyectAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proyectAccount.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.currencyType").value(DEFAULT_CURRENCY_TYPE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingProyectAccount() throws Exception {
        // Get the proyectAccount
        restProyectAccountMockMvc.perform(get("/api/proyect-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProyectAccount() throws Exception {
        // Initialize the database
        proyectAccountRepository.saveAndFlush(proyectAccount);

        int databaseSizeBeforeUpdate = proyectAccountRepository.findAll().size();

        // Update the proyectAccount
        ProyectAccount updatedProyectAccount = proyectAccountRepository.findById(proyectAccount.getId()).get();
        // Disconnect from session so that the updates on updatedProyectAccount are not directly saved in db
        em.detach(updatedProyectAccount);
        updatedProyectAccount
            .number(UPDATED_NUMBER)
            .currencyType(UPDATED_CURRENCY_TYPE);

        restProyectAccountMockMvc.perform(put("/api/proyect-accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProyectAccount)))
            .andExpect(status().isOk());

        // Validate the ProyectAccount in the database
        List<ProyectAccount> proyectAccountList = proyectAccountRepository.findAll();
        assertThat(proyectAccountList).hasSize(databaseSizeBeforeUpdate);
        ProyectAccount testProyectAccount = proyectAccountList.get(proyectAccountList.size() - 1);
        assertThat(testProyectAccount.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testProyectAccount.getCurrencyType()).isEqualTo(UPDATED_CURRENCY_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingProyectAccount() throws Exception {
        int databaseSizeBeforeUpdate = proyectAccountRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProyectAccountMockMvc.perform(put("/api/proyect-accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proyectAccount)))
            .andExpect(status().isBadRequest());

        // Validate the ProyectAccount in the database
        List<ProyectAccount> proyectAccountList = proyectAccountRepository.findAll();
        assertThat(proyectAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProyectAccount() throws Exception {
        // Initialize the database
        proyectAccountRepository.saveAndFlush(proyectAccount);

        int databaseSizeBeforeDelete = proyectAccountRepository.findAll().size();

        // Delete the proyectAccount
        restProyectAccountMockMvc.perform(delete("/api/proyect-accounts/{id}", proyectAccount.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProyectAccount> proyectAccountList = proyectAccountRepository.findAll();
        assertThat(proyectAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
