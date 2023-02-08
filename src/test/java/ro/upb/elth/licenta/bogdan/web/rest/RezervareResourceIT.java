package ro.upb.elth.licenta.bogdan.web.rest;

import ro.upb.elth.licenta.bogdan.SparkMeApp;
import ro.upb.elth.licenta.bogdan.domain.Rezervare;
import ro.upb.elth.licenta.bogdan.repository.RezervareRepository;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ro.upb.elth.licenta.bogdan.domain.enumeration.StatutRezervare;
/**
 * Integration tests for the {@link RezervareResource} REST controller.
 */
@SpringBootTest(classes = SparkMeApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RezervareResourceIT {

    private static final Instant DEFAULT_DATA_CREARE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_CREARE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_EXPIRARE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_EXPIRARE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_FINAL = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_FINAL = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final StatutRezervare DEFAULT_STATUT = StatutRezervare.NECUNOSCUT;
    private static final StatutRezervare UPDATED_STATUT = StatutRezervare.CONFIRMAT;

    @Autowired
    private RezervareRepository rezervareRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRezervareMockMvc;

    private Rezervare rezervare;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rezervare createEntity(EntityManager em) {
        Rezervare rezervare = new Rezervare()
            .dataCreare(DEFAULT_DATA_CREARE)
            .dataExpirare(DEFAULT_DATA_EXPIRARE)
            .dataStart(DEFAULT_DATA_START)
            .dataFinal(DEFAULT_DATA_FINAL)
            .statut(DEFAULT_STATUT);
        return rezervare;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rezervare createUpdatedEntity(EntityManager em) {
        Rezervare rezervare = new Rezervare()
            .dataCreare(UPDATED_DATA_CREARE)
            .dataExpirare(UPDATED_DATA_EXPIRARE)
            .dataStart(UPDATED_DATA_START)
            .dataFinal(UPDATED_DATA_FINAL)
            .statut(UPDATED_STATUT);
        return rezervare;
    }

    @BeforeEach
    public void initTest() {
        rezervare = createEntity(em);
    }

    @Test
    @Transactional
    public void createRezervare() throws Exception {
        int databaseSizeBeforeCreate = rezervareRepository.findAll().size();
        // Create the Rezervare
        restRezervareMockMvc.perform(post("/api/rezervares")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rezervare)))
            .andExpect(status().isCreated());

        // Validate the Rezervare in the database
        List<Rezervare> rezervareList = rezervareRepository.findAll();
        assertThat(rezervareList).hasSize(databaseSizeBeforeCreate + 1);
        Rezervare testRezervare = rezervareList.get(rezervareList.size() - 1);
        assertThat(testRezervare.getDataCreare()).isEqualTo(DEFAULT_DATA_CREARE);
        assertThat(testRezervare.getDataExpirare()).isEqualTo(DEFAULT_DATA_EXPIRARE);
        assertThat(testRezervare.getDataStart()).isEqualTo(DEFAULT_DATA_START);
        assertThat(testRezervare.getDataFinal()).isEqualTo(DEFAULT_DATA_FINAL);
        assertThat(testRezervare.getStatut()).isEqualTo(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    public void createRezervareWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rezervareRepository.findAll().size();

        // Create the Rezervare with an existing ID
        rezervare.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRezervareMockMvc.perform(post("/api/rezervares")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rezervare)))
            .andExpect(status().isBadRequest());

        // Validate the Rezervare in the database
        List<Rezervare> rezervareList = rezervareRepository.findAll();
        assertThat(rezervareList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDataCreareIsRequired() throws Exception {
        int databaseSizeBeforeTest = rezervareRepository.findAll().size();
        // set the field null
        rezervare.setDataCreare(null);

        // Create the Rezervare, which fails.


        restRezervareMockMvc.perform(post("/api/rezervares")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rezervare)))
            .andExpect(status().isBadRequest());

        List<Rezervare> rezervareList = rezervareRepository.findAll();
        assertThat(rezervareList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataExpirareIsRequired() throws Exception {
        int databaseSizeBeforeTest = rezervareRepository.findAll().size();
        // set the field null
        rezervare.setDataExpirare(null);

        // Create the Rezervare, which fails.


        restRezervareMockMvc.perform(post("/api/rezervares")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rezervare)))
            .andExpect(status().isBadRequest());

        List<Rezervare> rezervareList = rezervareRepository.findAll();
        assertThat(rezervareList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = rezervareRepository.findAll().size();
        // set the field null
        rezervare.setDataStart(null);

        // Create the Rezervare, which fails.


        restRezervareMockMvc.perform(post("/api/rezervares")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rezervare)))
            .andExpect(status().isBadRequest());

        List<Rezervare> rezervareList = rezervareRepository.findAll();
        assertThat(rezervareList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataFinalIsRequired() throws Exception {
        int databaseSizeBeforeTest = rezervareRepository.findAll().size();
        // set the field null
        rezervare.setDataFinal(null);

        // Create the Rezervare, which fails.


        restRezervareMockMvc.perform(post("/api/rezervares")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rezervare)))
            .andExpect(status().isBadRequest());

        List<Rezervare> rezervareList = rezervareRepository.findAll();
        assertThat(rezervareList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatutIsRequired() throws Exception {
        int databaseSizeBeforeTest = rezervareRepository.findAll().size();
        // set the field null
        rezervare.setStatut(null);

        // Create the Rezervare, which fails.


        restRezervareMockMvc.perform(post("/api/rezervares")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rezervare)))
            .andExpect(status().isBadRequest());

        List<Rezervare> rezervareList = rezervareRepository.findAll();
        assertThat(rezervareList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRezervares() throws Exception {
        // Initialize the database
        rezervareRepository.saveAndFlush(rezervare);

        // Get all the rezervareList
        restRezervareMockMvc.perform(get("/api/rezervares?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rezervare.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataCreare").value(hasItem(DEFAULT_DATA_CREARE.toString())))
            .andExpect(jsonPath("$.[*].dataExpirare").value(hasItem(DEFAULT_DATA_EXPIRARE.toString())))
            .andExpect(jsonPath("$.[*].dataStart").value(hasItem(DEFAULT_DATA_START.toString())))
            .andExpect(jsonPath("$.[*].dataFinal").value(hasItem(DEFAULT_DATA_FINAL.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }
    
    @Test
    @Transactional
    public void getRezervare() throws Exception {
        // Initialize the database
        rezervareRepository.saveAndFlush(rezervare);

        // Get the rezervare
        restRezervareMockMvc.perform(get("/api/rezervares/{id}", rezervare.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rezervare.getId().intValue()))
            .andExpect(jsonPath("$.dataCreare").value(DEFAULT_DATA_CREARE.toString()))
            .andExpect(jsonPath("$.dataExpirare").value(DEFAULT_DATA_EXPIRARE.toString()))
            .andExpect(jsonPath("$.dataStart").value(DEFAULT_DATA_START.toString()))
            .andExpect(jsonPath("$.dataFinal").value(DEFAULT_DATA_FINAL.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingRezervare() throws Exception {
        // Get the rezervare
        restRezervareMockMvc.perform(get("/api/rezervares/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRezervare() throws Exception {
        // Initialize the database
        rezervareRepository.saveAndFlush(rezervare);

        int databaseSizeBeforeUpdate = rezervareRepository.findAll().size();

        // Update the rezervare
        Rezervare updatedRezervare = rezervareRepository.findById(rezervare.getId()).get();
        // Disconnect from session so that the updates on updatedRezervare are not directly saved in db
        em.detach(updatedRezervare);
        updatedRezervare
            .dataCreare(UPDATED_DATA_CREARE)
            .dataExpirare(UPDATED_DATA_EXPIRARE)
            .dataStart(UPDATED_DATA_START)
            .dataFinal(UPDATED_DATA_FINAL)
            .statut(UPDATED_STATUT);

        restRezervareMockMvc.perform(put("/api/rezervares")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRezervare)))
            .andExpect(status().isOk());

        // Validate the Rezervare in the database
        List<Rezervare> rezervareList = rezervareRepository.findAll();
        assertThat(rezervareList).hasSize(databaseSizeBeforeUpdate);
        Rezervare testRezervare = rezervareList.get(rezervareList.size() - 1);
        assertThat(testRezervare.getDataCreare()).isEqualTo(UPDATED_DATA_CREARE);
        assertThat(testRezervare.getDataExpirare()).isEqualTo(UPDATED_DATA_EXPIRARE);
        assertThat(testRezervare.getDataStart()).isEqualTo(UPDATED_DATA_START);
        assertThat(testRezervare.getDataFinal()).isEqualTo(UPDATED_DATA_FINAL);
        assertThat(testRezervare.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    public void updateNonExistingRezervare() throws Exception {
        int databaseSizeBeforeUpdate = rezervareRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRezervareMockMvc.perform(put("/api/rezervares")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rezervare)))
            .andExpect(status().isBadRequest());

        // Validate the Rezervare in the database
        List<Rezervare> rezervareList = rezervareRepository.findAll();
        assertThat(rezervareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRezervare() throws Exception {
        // Initialize the database
        rezervareRepository.saveAndFlush(rezervare);

        int databaseSizeBeforeDelete = rezervareRepository.findAll().size();

        // Delete the rezervare
        restRezervareMockMvc.perform(delete("/api/rezervares/{id}", rezervare.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rezervare> rezervareList = rezervareRepository.findAll();
        assertThat(rezervareList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
