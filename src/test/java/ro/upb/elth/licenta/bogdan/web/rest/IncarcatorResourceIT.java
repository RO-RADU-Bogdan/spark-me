package ro.upb.elth.licenta.bogdan.web.rest;

import ro.upb.elth.licenta.bogdan.SparkMeApp;
import ro.upb.elth.licenta.bogdan.domain.Incarcator;
import ro.upb.elth.licenta.bogdan.repository.IncarcatorRepository;

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

import ro.upb.elth.licenta.bogdan.domain.enumeration.Conector;
import ro.upb.elth.licenta.bogdan.domain.enumeration.Disponibilitate;
/**
 * Integration tests for the {@link IncarcatorResource} REST controller.
 */
@SpringBootTest(classes = SparkMeApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class IncarcatorResourceIT {

    private static final String DEFAULT_DENUMIRE_CONECTOR = "AAAAAAAAAA";
    private static final String UPDATED_DENUMIRE_CONECTOR = "BBBBBBBBBB";

    private static final Conector DEFAULT_CONECTOR = Conector.TYPE1;
    private static final Conector UPDATED_CONECTOR = Conector.TYPE2;

    private static final String DEFAULT_DESCRIERE_CONECTOR = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIERE_CONECTOR = "BBBBBBBBBB";

    private static final Disponibilitate DEFAULT_DISPONIBILITATE = Disponibilitate.NECUNOSCUT;
    private static final Disponibilitate UPDATED_DISPONIBILITATE = Disponibilitate.DISPONIBIL;

    private static final Integer DEFAULT_PUTERE = 0;
    private static final Integer UPDATED_PUTERE = 1;

    @Autowired
    private IncarcatorRepository incarcatorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIncarcatorMockMvc;

    private Incarcator incarcator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Incarcator createEntity(EntityManager em) {
        Incarcator incarcator = new Incarcator()
            .denumireConector(DEFAULT_DENUMIRE_CONECTOR)
            .conector(DEFAULT_CONECTOR)
            .descriereConector(DEFAULT_DESCRIERE_CONECTOR)
            .disponibilitate(DEFAULT_DISPONIBILITATE)
            .putere(DEFAULT_PUTERE);
        return incarcator;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Incarcator createUpdatedEntity(EntityManager em) {
        Incarcator incarcator = new Incarcator()
            .denumireConector(UPDATED_DENUMIRE_CONECTOR)
            .conector(UPDATED_CONECTOR)
            .descriereConector(UPDATED_DESCRIERE_CONECTOR)
            .disponibilitate(UPDATED_DISPONIBILITATE)
            .putere(UPDATED_PUTERE);
        return incarcator;
    }

    @BeforeEach
    public void initTest() {
        incarcator = createEntity(em);
    }

    @Test
    @Transactional
    public void createIncarcator() throws Exception {
        int databaseSizeBeforeCreate = incarcatorRepository.findAll().size();
        // Create the Incarcator
        restIncarcatorMockMvc.perform(post("/api/incarcators")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incarcator)))
            .andExpect(status().isCreated());

        // Validate the Incarcator in the database
        List<Incarcator> incarcatorList = incarcatorRepository.findAll();
        assertThat(incarcatorList).hasSize(databaseSizeBeforeCreate + 1);
        Incarcator testIncarcator = incarcatorList.get(incarcatorList.size() - 1);
        assertThat(testIncarcator.getDenumireConector()).isEqualTo(DEFAULT_DENUMIRE_CONECTOR);
        assertThat(testIncarcator.getConector()).isEqualTo(DEFAULT_CONECTOR);
        assertThat(testIncarcator.getDescriereConector()).isEqualTo(DEFAULT_DESCRIERE_CONECTOR);
        assertThat(testIncarcator.getDisponibilitate()).isEqualTo(DEFAULT_DISPONIBILITATE);
        assertThat(testIncarcator.getPutere()).isEqualTo(DEFAULT_PUTERE);
    }

    @Test
    @Transactional
    public void createIncarcatorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = incarcatorRepository.findAll().size();

        // Create the Incarcator with an existing ID
        incarcator.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncarcatorMockMvc.perform(post("/api/incarcators")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incarcator)))
            .andExpect(status().isBadRequest());

        // Validate the Incarcator in the database
        List<Incarcator> incarcatorList = incarcatorRepository.findAll();
        assertThat(incarcatorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDenumireConectorIsRequired() throws Exception {
        int databaseSizeBeforeTest = incarcatorRepository.findAll().size();
        // set the field null
        incarcator.setDenumireConector(null);

        // Create the Incarcator, which fails.


        restIncarcatorMockMvc.perform(post("/api/incarcators")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incarcator)))
            .andExpect(status().isBadRequest());

        List<Incarcator> incarcatorList = incarcatorRepository.findAll();
        assertThat(incarcatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConectorIsRequired() throws Exception {
        int databaseSizeBeforeTest = incarcatorRepository.findAll().size();
        // set the field null
        incarcator.setConector(null);

        // Create the Incarcator, which fails.


        restIncarcatorMockMvc.perform(post("/api/incarcators")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incarcator)))
            .andExpect(status().isBadRequest());

        List<Incarcator> incarcatorList = incarcatorRepository.findAll();
        assertThat(incarcatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDisponibilitateIsRequired() throws Exception {
        int databaseSizeBeforeTest = incarcatorRepository.findAll().size();
        // set the field null
        incarcator.setDisponibilitate(null);

        // Create the Incarcator, which fails.


        restIncarcatorMockMvc.perform(post("/api/incarcators")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incarcator)))
            .andExpect(status().isBadRequest());

        List<Incarcator> incarcatorList = incarcatorRepository.findAll();
        assertThat(incarcatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPutereIsRequired() throws Exception {
        int databaseSizeBeforeTest = incarcatorRepository.findAll().size();
        // set the field null
        incarcator.setPutere(null);

        // Create the Incarcator, which fails.


        restIncarcatorMockMvc.perform(post("/api/incarcators")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incarcator)))
            .andExpect(status().isBadRequest());

        List<Incarcator> incarcatorList = incarcatorRepository.findAll();
        assertThat(incarcatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIncarcators() throws Exception {
        // Initialize the database
        incarcatorRepository.saveAndFlush(incarcator);

        // Get all the incarcatorList
        restIncarcatorMockMvc.perform(get("/api/incarcators?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incarcator.getId().intValue())))
            .andExpect(jsonPath("$.[*].denumireConector").value(hasItem(DEFAULT_DENUMIRE_CONECTOR)))
            .andExpect(jsonPath("$.[*].conector").value(hasItem(DEFAULT_CONECTOR.toString())))
            .andExpect(jsonPath("$.[*].descriereConector").value(hasItem(DEFAULT_DESCRIERE_CONECTOR)))
            .andExpect(jsonPath("$.[*].disponibilitate").value(hasItem(DEFAULT_DISPONIBILITATE.toString())))
            .andExpect(jsonPath("$.[*].putere").value(hasItem(DEFAULT_PUTERE)));
    }
    
    @Test
    @Transactional
    public void getIncarcator() throws Exception {
        // Initialize the database
        incarcatorRepository.saveAndFlush(incarcator);

        // Get the incarcator
        restIncarcatorMockMvc.perform(get("/api/incarcators/{id}", incarcator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(incarcator.getId().intValue()))
            .andExpect(jsonPath("$.denumireConector").value(DEFAULT_DENUMIRE_CONECTOR))
            .andExpect(jsonPath("$.conector").value(DEFAULT_CONECTOR.toString()))
            .andExpect(jsonPath("$.descriereConector").value(DEFAULT_DESCRIERE_CONECTOR))
            .andExpect(jsonPath("$.disponibilitate").value(DEFAULT_DISPONIBILITATE.toString()))
            .andExpect(jsonPath("$.putere").value(DEFAULT_PUTERE));
    }
    @Test
    @Transactional
    public void getNonExistingIncarcator() throws Exception {
        // Get the incarcator
        restIncarcatorMockMvc.perform(get("/api/incarcators/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIncarcator() throws Exception {
        // Initialize the database
        incarcatorRepository.saveAndFlush(incarcator);

        int databaseSizeBeforeUpdate = incarcatorRepository.findAll().size();

        // Update the incarcator
        Incarcator updatedIncarcator = incarcatorRepository.findById(incarcator.getId()).get();
        // Disconnect from session so that the updates on updatedIncarcator are not directly saved in db
        em.detach(updatedIncarcator);
        updatedIncarcator
            .denumireConector(UPDATED_DENUMIRE_CONECTOR)
            .conector(UPDATED_CONECTOR)
            .descriereConector(UPDATED_DESCRIERE_CONECTOR)
            .disponibilitate(UPDATED_DISPONIBILITATE)
            .putere(UPDATED_PUTERE);

        restIncarcatorMockMvc.perform(put("/api/incarcators")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedIncarcator)))
            .andExpect(status().isOk());

        // Validate the Incarcator in the database
        List<Incarcator> incarcatorList = incarcatorRepository.findAll();
        assertThat(incarcatorList).hasSize(databaseSizeBeforeUpdate);
        Incarcator testIncarcator = incarcatorList.get(incarcatorList.size() - 1);
        assertThat(testIncarcator.getDenumireConector()).isEqualTo(UPDATED_DENUMIRE_CONECTOR);
        assertThat(testIncarcator.getConector()).isEqualTo(UPDATED_CONECTOR);
        assertThat(testIncarcator.getDescriereConector()).isEqualTo(UPDATED_DESCRIERE_CONECTOR);
        assertThat(testIncarcator.getDisponibilitate()).isEqualTo(UPDATED_DISPONIBILITATE);
        assertThat(testIncarcator.getPutere()).isEqualTo(UPDATED_PUTERE);
    }

    @Test
    @Transactional
    public void updateNonExistingIncarcator() throws Exception {
        int databaseSizeBeforeUpdate = incarcatorRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncarcatorMockMvc.perform(put("/api/incarcators")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(incarcator)))
            .andExpect(status().isBadRequest());

        // Validate the Incarcator in the database
        List<Incarcator> incarcatorList = incarcatorRepository.findAll();
        assertThat(incarcatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteIncarcator() throws Exception {
        // Initialize the database
        incarcatorRepository.saveAndFlush(incarcator);

        int databaseSizeBeforeDelete = incarcatorRepository.findAll().size();

        // Delete the incarcator
        restIncarcatorMockMvc.perform(delete("/api/incarcators/{id}", incarcator.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Incarcator> incarcatorList = incarcatorRepository.findAll();
        assertThat(incarcatorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
