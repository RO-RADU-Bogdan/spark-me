package ro.upb.elth.licenta.bogdan.web.rest;

import ro.upb.elth.licenta.bogdan.SparkMeApp;
import ro.upb.elth.licenta.bogdan.domain.Locatie;
import ro.upb.elth.licenta.bogdan.repository.LocatieRepository;

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

import ro.upb.elth.licenta.bogdan.domain.enumeration.TipAcces;
/**
 * Integration tests for the {@link LocatieResource} REST controller.
 */
@SpringBootTest(classes = SparkMeApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LocatieResourceIT {

    private static final String DEFAULT_DENUMIRE = "AAAAAAAAAA";
    private static final String UPDATED_DENUMIRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIERE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIERE = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDINE = -90D;
    private static final Double UPDATED_LATITUDINE = -89D;

    private static final Double DEFAULT_LONGITUDINE = -180D;
    private static final Double UPDATED_LONGITUDINE = -179D;

    private static final TipAcces DEFAULT_TIP_ACCES = TipAcces.PUBLIC;
    private static final TipAcces UPDATED_TIP_ACCES = TipAcces.PRIVAT;

    @Autowired
    private LocatieRepository locatieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocatieMockMvc;

    private Locatie locatie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Locatie createEntity(EntityManager em) {
        Locatie locatie = new Locatie()
            .denumire(DEFAULT_DENUMIRE)
            .descriere(DEFAULT_DESCRIERE)
            .latitudine(DEFAULT_LATITUDINE)
            .longitudine(DEFAULT_LONGITUDINE)
            .tipAcces(DEFAULT_TIP_ACCES);
        return locatie;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Locatie createUpdatedEntity(EntityManager em) {
        Locatie locatie = new Locatie()
            .denumire(UPDATED_DENUMIRE)
            .descriere(UPDATED_DESCRIERE)
            .latitudine(UPDATED_LATITUDINE)
            .longitudine(UPDATED_LONGITUDINE)
            .tipAcces(UPDATED_TIP_ACCES);
        return locatie;
    }

    @BeforeEach
    public void initTest() {
        locatie = createEntity(em);
    }

    @Test
    @Transactional
    public void createLocatie() throws Exception {
        int databaseSizeBeforeCreate = locatieRepository.findAll().size();
        // Create the Locatie
        restLocatieMockMvc.perform(post("/api/locaties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(locatie)))
            .andExpect(status().isCreated());

        // Validate the Locatie in the database
        List<Locatie> locatieList = locatieRepository.findAll();
        assertThat(locatieList).hasSize(databaseSizeBeforeCreate + 1);
        Locatie testLocatie = locatieList.get(locatieList.size() - 1);
        assertThat(testLocatie.getDenumire()).isEqualTo(DEFAULT_DENUMIRE);
        assertThat(testLocatie.getDescriere()).isEqualTo(DEFAULT_DESCRIERE);
        assertThat(testLocatie.getLatitudine()).isEqualTo(DEFAULT_LATITUDINE);
        assertThat(testLocatie.getLongitudine()).isEqualTo(DEFAULT_LONGITUDINE);
        assertThat(testLocatie.getTipAcces()).isEqualTo(DEFAULT_TIP_ACCES);
    }

    @Test
    @Transactional
    public void createLocatieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = locatieRepository.findAll().size();

        // Create the Locatie with an existing ID
        locatie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocatieMockMvc.perform(post("/api/locaties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(locatie)))
            .andExpect(status().isBadRequest());

        // Validate the Locatie in the database
        List<Locatie> locatieList = locatieRepository.findAll();
        assertThat(locatieList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDenumireIsRequired() throws Exception {
        int databaseSizeBeforeTest = locatieRepository.findAll().size();
        // set the field null
        locatie.setDenumire(null);

        // Create the Locatie, which fails.


        restLocatieMockMvc.perform(post("/api/locaties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(locatie)))
            .andExpect(status().isBadRequest());

        List<Locatie> locatieList = locatieRepository.findAll();
        assertThat(locatieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriereIsRequired() throws Exception {
        int databaseSizeBeforeTest = locatieRepository.findAll().size();
        // set the field null
        locatie.setDescriere(null);

        // Create the Locatie, which fails.


        restLocatieMockMvc.perform(post("/api/locaties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(locatie)))
            .andExpect(status().isBadRequest());

        List<Locatie> locatieList = locatieRepository.findAll();
        assertThat(locatieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipAccesIsRequired() throws Exception {
        int databaseSizeBeforeTest = locatieRepository.findAll().size();
        // set the field null
        locatie.setTipAcces(null);

        // Create the Locatie, which fails.


        restLocatieMockMvc.perform(post("/api/locaties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(locatie)))
            .andExpect(status().isBadRequest());

        List<Locatie> locatieList = locatieRepository.findAll();
        assertThat(locatieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocaties() throws Exception {
        // Initialize the database
        locatieRepository.saveAndFlush(locatie);

        // Get all the locatieList
        restLocatieMockMvc.perform(get("/api/locaties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locatie.getId().intValue())))
            .andExpect(jsonPath("$.[*].denumire").value(hasItem(DEFAULT_DENUMIRE)))
            .andExpect(jsonPath("$.[*].descriere").value(hasItem(DEFAULT_DESCRIERE)))
            .andExpect(jsonPath("$.[*].latitudine").value(hasItem(DEFAULT_LATITUDINE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitudine").value(hasItem(DEFAULT_LONGITUDINE.doubleValue())))
            .andExpect(jsonPath("$.[*].tipAcces").value(hasItem(DEFAULT_TIP_ACCES.toString())));
    }
    
    @Test
    @Transactional
    public void getLocatie() throws Exception {
        // Initialize the database
        locatieRepository.saveAndFlush(locatie);

        // Get the locatie
        restLocatieMockMvc.perform(get("/api/locaties/{id}", locatie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locatie.getId().intValue()))
            .andExpect(jsonPath("$.denumire").value(DEFAULT_DENUMIRE))
            .andExpect(jsonPath("$.descriere").value(DEFAULT_DESCRIERE))
            .andExpect(jsonPath("$.latitudine").value(DEFAULT_LATITUDINE.doubleValue()))
            .andExpect(jsonPath("$.longitudine").value(DEFAULT_LONGITUDINE.doubleValue()))
            .andExpect(jsonPath("$.tipAcces").value(DEFAULT_TIP_ACCES.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingLocatie() throws Exception {
        // Get the locatie
        restLocatieMockMvc.perform(get("/api/locaties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocatie() throws Exception {
        // Initialize the database
        locatieRepository.saveAndFlush(locatie);

        int databaseSizeBeforeUpdate = locatieRepository.findAll().size();

        // Update the locatie
        Locatie updatedLocatie = locatieRepository.findById(locatie.getId()).get();
        // Disconnect from session so that the updates on updatedLocatie are not directly saved in db
        em.detach(updatedLocatie);
        updatedLocatie
            .denumire(UPDATED_DENUMIRE)
            .descriere(UPDATED_DESCRIERE)
            .latitudine(UPDATED_LATITUDINE)
            .longitudine(UPDATED_LONGITUDINE)
            .tipAcces(UPDATED_TIP_ACCES);

        restLocatieMockMvc.perform(put("/api/locaties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLocatie)))
            .andExpect(status().isOk());

        // Validate the Locatie in the database
        List<Locatie> locatieList = locatieRepository.findAll();
        assertThat(locatieList).hasSize(databaseSizeBeforeUpdate);
        Locatie testLocatie = locatieList.get(locatieList.size() - 1);
        assertThat(testLocatie.getDenumire()).isEqualTo(UPDATED_DENUMIRE);
        assertThat(testLocatie.getDescriere()).isEqualTo(UPDATED_DESCRIERE);
        assertThat(testLocatie.getLatitudine()).isEqualTo(UPDATED_LATITUDINE);
        assertThat(testLocatie.getLongitudine()).isEqualTo(UPDATED_LONGITUDINE);
        assertThat(testLocatie.getTipAcces()).isEqualTo(UPDATED_TIP_ACCES);
    }

    @Test
    @Transactional
    public void updateNonExistingLocatie() throws Exception {
        int databaseSizeBeforeUpdate = locatieRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocatieMockMvc.perform(put("/api/locaties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(locatie)))
            .andExpect(status().isBadRequest());

        // Validate the Locatie in the database
        List<Locatie> locatieList = locatieRepository.findAll();
        assertThat(locatieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLocatie() throws Exception {
        // Initialize the database
        locatieRepository.saveAndFlush(locatie);

        int databaseSizeBeforeDelete = locatieRepository.findAll().size();

        // Delete the locatie
        restLocatieMockMvc.perform(delete("/api/locaties/{id}", locatie.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Locatie> locatieList = locatieRepository.findAll();
        assertThat(locatieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
