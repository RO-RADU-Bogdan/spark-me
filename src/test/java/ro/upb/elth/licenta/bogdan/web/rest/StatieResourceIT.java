package ro.upb.elth.licenta.bogdan.web.rest;

import ro.upb.elth.licenta.bogdan.SparkMeApp;
import ro.upb.elth.licenta.bogdan.domain.Statie;
import ro.upb.elth.licenta.bogdan.repository.StatieRepository;

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

import ro.upb.elth.licenta.bogdan.domain.enumeration.StatutStatie;
import ro.upb.elth.licenta.bogdan.domain.enumeration.TipCost;
/**
 * Integration tests for the {@link StatieResource} REST controller.
 */
@SpringBootTest(classes = SparkMeApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class StatieResourceIT {

    private static final String DEFAULT_DENUMIRE = "AAAAAAAAAA";
    private static final String UPDATED_DENUMIRE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCATOR = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCATOR = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDINE = -90D;
    private static final Double UPDATED_LATITUDINE = -89D;

    private static final Double DEFAULT_LONGITUDINE = -180D;
    private static final Double UPDATED_LONGITUDINE = -179D;

    private static final StatutStatie DEFAULT_STATUT = StatutStatie.NECUNOSCUT;
    private static final StatutStatie UPDATED_STATUT = StatutStatie.DISPONIBIL;

    private static final TipCost DEFAULT_TIP_COST = TipCost.NECUNOSCUT;
    private static final TipCost UPDATED_TIP_COST = TipCost.GRATUIT;

    private static final String DEFAULT_DESCRIERE_COST = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIERE_COST = "BBBBBBBBBB";

    @Autowired
    private StatieRepository statieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatieMockMvc;

    private Statie statie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Statie createEntity(EntityManager em) {
        Statie statie = new Statie()
            .denumire(DEFAULT_DENUMIRE)
            .producator(DEFAULT_PRODUCATOR)
            .model(DEFAULT_MODEL)
            .latitudine(DEFAULT_LATITUDINE)
            .longitudine(DEFAULT_LONGITUDINE)
            .statut(DEFAULT_STATUT)
            .tipCost(DEFAULT_TIP_COST)
            .descriereCost(DEFAULT_DESCRIERE_COST);
        return statie;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Statie createUpdatedEntity(EntityManager em) {
        Statie statie = new Statie()
            .denumire(UPDATED_DENUMIRE)
            .producator(UPDATED_PRODUCATOR)
            .model(UPDATED_MODEL)
            .latitudine(UPDATED_LATITUDINE)
            .longitudine(UPDATED_LONGITUDINE)
            .statut(UPDATED_STATUT)
            .tipCost(UPDATED_TIP_COST)
            .descriereCost(UPDATED_DESCRIERE_COST);
        return statie;
    }

    @BeforeEach
    public void initTest() {
        statie = createEntity(em);
    }

    @Test
    @Transactional
    public void createStatie() throws Exception {
        int databaseSizeBeforeCreate = statieRepository.findAll().size();
        // Create the Statie
        restStatieMockMvc.perform(post("/api/staties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(statie)))
            .andExpect(status().isCreated());

        // Validate the Statie in the database
        List<Statie> statieList = statieRepository.findAll();
        assertThat(statieList).hasSize(databaseSizeBeforeCreate + 1);
        Statie testStatie = statieList.get(statieList.size() - 1);
        assertThat(testStatie.getDenumire()).isEqualTo(DEFAULT_DENUMIRE);
        assertThat(testStatie.getProducator()).isEqualTo(DEFAULT_PRODUCATOR);
        assertThat(testStatie.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testStatie.getLatitudine()).isEqualTo(DEFAULT_LATITUDINE);
        assertThat(testStatie.getLongitudine()).isEqualTo(DEFAULT_LONGITUDINE);
        assertThat(testStatie.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testStatie.getTipCost()).isEqualTo(DEFAULT_TIP_COST);
        assertThat(testStatie.getDescriereCost()).isEqualTo(DEFAULT_DESCRIERE_COST);
    }

    @Test
    @Transactional
    public void createStatieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = statieRepository.findAll().size();

        // Create the Statie with an existing ID
        statie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatieMockMvc.perform(post("/api/staties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(statie)))
            .andExpect(status().isBadRequest());

        // Validate the Statie in the database
        List<Statie> statieList = statieRepository.findAll();
        assertThat(statieList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDenumireIsRequired() throws Exception {
        int databaseSizeBeforeTest = statieRepository.findAll().size();
        // set the field null
        statie.setDenumire(null);

        // Create the Statie, which fails.


        restStatieMockMvc.perform(post("/api/staties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(statie)))
            .andExpect(status().isBadRequest());

        List<Statie> statieList = statieRepository.findAll();
        assertThat(statieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProducatorIsRequired() throws Exception {
        int databaseSizeBeforeTest = statieRepository.findAll().size();
        // set the field null
        statie.setProducator(null);

        // Create the Statie, which fails.


        restStatieMockMvc.perform(post("/api/staties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(statie)))
            .andExpect(status().isBadRequest());

        List<Statie> statieList = statieRepository.findAll();
        assertThat(statieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModelIsRequired() throws Exception {
        int databaseSizeBeforeTest = statieRepository.findAll().size();
        // set the field null
        statie.setModel(null);

        // Create the Statie, which fails.


        restStatieMockMvc.perform(post("/api/staties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(statie)))
            .andExpect(status().isBadRequest());

        List<Statie> statieList = statieRepository.findAll();
        assertThat(statieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatutIsRequired() throws Exception {
        int databaseSizeBeforeTest = statieRepository.findAll().size();
        // set the field null
        statie.setStatut(null);

        // Create the Statie, which fails.


        restStatieMockMvc.perform(post("/api/staties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(statie)))
            .andExpect(status().isBadRequest());

        List<Statie> statieList = statieRepository.findAll();
        assertThat(statieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = statieRepository.findAll().size();
        // set the field null
        statie.setTipCost(null);

        // Create the Statie, which fails.


        restStatieMockMvc.perform(post("/api/staties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(statie)))
            .andExpect(status().isBadRequest());

        List<Statie> statieList = statieRepository.findAll();
        assertThat(statieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStaties() throws Exception {
        // Initialize the database
        statieRepository.saveAndFlush(statie);

        // Get all the statieList
        restStatieMockMvc.perform(get("/api/staties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statie.getId().intValue())))
            .andExpect(jsonPath("$.[*].denumire").value(hasItem(DEFAULT_DENUMIRE)))
            .andExpect(jsonPath("$.[*].producator").value(hasItem(DEFAULT_PRODUCATOR)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].latitudine").value(hasItem(DEFAULT_LATITUDINE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitudine").value(hasItem(DEFAULT_LONGITUDINE.doubleValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].tipCost").value(hasItem(DEFAULT_TIP_COST.toString())))
            .andExpect(jsonPath("$.[*].descriereCost").value(hasItem(DEFAULT_DESCRIERE_COST)));
    }
    
    @Test
    @Transactional
    public void getStatie() throws Exception {
        // Initialize the database
        statieRepository.saveAndFlush(statie);

        // Get the statie
        restStatieMockMvc.perform(get("/api/staties/{id}", statie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(statie.getId().intValue()))
            .andExpect(jsonPath("$.denumire").value(DEFAULT_DENUMIRE))
            .andExpect(jsonPath("$.producator").value(DEFAULT_PRODUCATOR))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.latitudine").value(DEFAULT_LATITUDINE.doubleValue()))
            .andExpect(jsonPath("$.longitudine").value(DEFAULT_LONGITUDINE.doubleValue()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.tipCost").value(DEFAULT_TIP_COST.toString()))
            .andExpect(jsonPath("$.descriereCost").value(DEFAULT_DESCRIERE_COST));
    }
    @Test
    @Transactional
    public void getNonExistingStatie() throws Exception {
        // Get the statie
        restStatieMockMvc.perform(get("/api/staties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStatie() throws Exception {
        // Initialize the database
        statieRepository.saveAndFlush(statie);

        int databaseSizeBeforeUpdate = statieRepository.findAll().size();

        // Update the statie
        Statie updatedStatie = statieRepository.findById(statie.getId()).get();
        // Disconnect from session so that the updates on updatedStatie are not directly saved in db
        em.detach(updatedStatie);
        updatedStatie
            .denumire(UPDATED_DENUMIRE)
            .producator(UPDATED_PRODUCATOR)
            .model(UPDATED_MODEL)
            .latitudine(UPDATED_LATITUDINE)
            .longitudine(UPDATED_LONGITUDINE)
            .statut(UPDATED_STATUT)
            .tipCost(UPDATED_TIP_COST)
            .descriereCost(UPDATED_DESCRIERE_COST);

        restStatieMockMvc.perform(put("/api/staties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStatie)))
            .andExpect(status().isOk());

        // Validate the Statie in the database
        List<Statie> statieList = statieRepository.findAll();
        assertThat(statieList).hasSize(databaseSizeBeforeUpdate);
        Statie testStatie = statieList.get(statieList.size() - 1);
        assertThat(testStatie.getDenumire()).isEqualTo(UPDATED_DENUMIRE);
        assertThat(testStatie.getProducator()).isEqualTo(UPDATED_PRODUCATOR);
        assertThat(testStatie.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testStatie.getLatitudine()).isEqualTo(UPDATED_LATITUDINE);
        assertThat(testStatie.getLongitudine()).isEqualTo(UPDATED_LONGITUDINE);
        assertThat(testStatie.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testStatie.getTipCost()).isEqualTo(UPDATED_TIP_COST);
        assertThat(testStatie.getDescriereCost()).isEqualTo(UPDATED_DESCRIERE_COST);
    }

    @Test
    @Transactional
    public void updateNonExistingStatie() throws Exception {
        int databaseSizeBeforeUpdate = statieRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatieMockMvc.perform(put("/api/staties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(statie)))
            .andExpect(status().isBadRequest());

        // Validate the Statie in the database
        List<Statie> statieList = statieRepository.findAll();
        assertThat(statieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStatie() throws Exception {
        // Initialize the database
        statieRepository.saveAndFlush(statie);

        int databaseSizeBeforeDelete = statieRepository.findAll().size();

        // Delete the statie
        restStatieMockMvc.perform(delete("/api/staties/{id}", statie.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Statie> statieList = statieRepository.findAll();
        assertThat(statieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
