package ro.upb.elth.licenta.bogdan.web.rest;

import ro.upb.elth.licenta.bogdan.SparkMeApp;
import ro.upb.elth.licenta.bogdan.domain.Vehicul;
import ro.upb.elth.licenta.bogdan.repository.VehiculRepository;

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
/**
 * Integration tests for the {@link VehiculResource} REST controller.
 */
@SpringBootTest(classes = SparkMeApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class VehiculResourceIT {

    private static final String DEFAULT_MARCA = "AAAAAAAAAA";
    private static final String UPDATED_MARCA = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final Integer DEFAULT_AN_FABRICATIE = 1990;
    private static final Integer UPDATED_AN_FABRICATIE = 1991;

    private static final String DEFAULT_CULOARE = "AAAAAAAAAA";
    private static final String UPDATED_CULOARE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIERE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIERE = "BBBBBBBBBB";

    private static final Conector DEFAULT_CONECTOR = Conector.TYPE1;
    private static final Conector UPDATED_CONECTOR = Conector.TYPE2;

    @Autowired
    private VehiculRepository vehiculRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehiculMockMvc;

    private Vehicul vehicul;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicul createEntity(EntityManager em) {
        Vehicul vehicul = new Vehicul()
            .marca(DEFAULT_MARCA)
            .model(DEFAULT_MODEL)
            .anFabricatie(DEFAULT_AN_FABRICATIE)
            .culoare(DEFAULT_CULOARE)
            .descriere(DEFAULT_DESCRIERE)
            .conector(DEFAULT_CONECTOR);
        return vehicul;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicul createUpdatedEntity(EntityManager em) {
        Vehicul vehicul = new Vehicul()
            .marca(UPDATED_MARCA)
            .model(UPDATED_MODEL)
            .anFabricatie(UPDATED_AN_FABRICATIE)
            .culoare(UPDATED_CULOARE)
            .descriere(UPDATED_DESCRIERE)
            .conector(UPDATED_CONECTOR);
        return vehicul;
    }

    @BeforeEach
    public void initTest() {
        vehicul = createEntity(em);
    }

    @Test
    @Transactional
    public void createVehicul() throws Exception {
        int databaseSizeBeforeCreate = vehiculRepository.findAll().size();
        // Create the Vehicul
        restVehiculMockMvc.perform(post("/api/vehiculs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicul)))
            .andExpect(status().isCreated());

        // Validate the Vehicul in the database
        List<Vehicul> vehiculList = vehiculRepository.findAll();
        assertThat(vehiculList).hasSize(databaseSizeBeforeCreate + 1);
        Vehicul testVehicul = vehiculList.get(vehiculList.size() - 1);
        assertThat(testVehicul.getMarca()).isEqualTo(DEFAULT_MARCA);
        assertThat(testVehicul.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testVehicul.getAnFabricatie()).isEqualTo(DEFAULT_AN_FABRICATIE);
        assertThat(testVehicul.getCuloare()).isEqualTo(DEFAULT_CULOARE);
        assertThat(testVehicul.getDescriere()).isEqualTo(DEFAULT_DESCRIERE);
        assertThat(testVehicul.getConector()).isEqualTo(DEFAULT_CONECTOR);
    }

    @Test
    @Transactional
    public void createVehiculWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vehiculRepository.findAll().size();

        // Create the Vehicul with an existing ID
        vehicul.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehiculMockMvc.perform(post("/api/vehiculs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicul)))
            .andExpect(status().isBadRequest());

        // Validate the Vehicul in the database
        List<Vehicul> vehiculList = vehiculRepository.findAll();
        assertThat(vehiculList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkMarcaIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculRepository.findAll().size();
        // set the field null
        vehicul.setMarca(null);

        // Create the Vehicul, which fails.


        restVehiculMockMvc.perform(post("/api/vehiculs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicul)))
            .andExpect(status().isBadRequest());

        List<Vehicul> vehiculList = vehiculRepository.findAll();
        assertThat(vehiculList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModelIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculRepository.findAll().size();
        // set the field null
        vehicul.setModel(null);

        // Create the Vehicul, which fails.


        restVehiculMockMvc.perform(post("/api/vehiculs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicul)))
            .andExpect(status().isBadRequest());

        List<Vehicul> vehiculList = vehiculRepository.findAll();
        assertThat(vehiculList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnFabricatieIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculRepository.findAll().size();
        // set the field null
        vehicul.setAnFabricatie(null);

        // Create the Vehicul, which fails.


        restVehiculMockMvc.perform(post("/api/vehiculs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicul)))
            .andExpect(status().isBadRequest());

        List<Vehicul> vehiculList = vehiculRepository.findAll();
        assertThat(vehiculList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCuloareIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculRepository.findAll().size();
        // set the field null
        vehicul.setCuloare(null);

        // Create the Vehicul, which fails.


        restVehiculMockMvc.perform(post("/api/vehiculs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicul)))
            .andExpect(status().isBadRequest());

        List<Vehicul> vehiculList = vehiculRepository.findAll();
        assertThat(vehiculList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConectorIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculRepository.findAll().size();
        // set the field null
        vehicul.setConector(null);

        // Create the Vehicul, which fails.


        restVehiculMockMvc.perform(post("/api/vehiculs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicul)))
            .andExpect(status().isBadRequest());

        List<Vehicul> vehiculList = vehiculRepository.findAll();
        assertThat(vehiculList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVehiculs() throws Exception {
        // Initialize the database
        vehiculRepository.saveAndFlush(vehicul);

        // Get all the vehiculList
        restVehiculMockMvc.perform(get("/api/vehiculs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicul.getId().intValue())))
            .andExpect(jsonPath("$.[*].marca").value(hasItem(DEFAULT_MARCA)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].anFabricatie").value(hasItem(DEFAULT_AN_FABRICATIE)))
            .andExpect(jsonPath("$.[*].culoare").value(hasItem(DEFAULT_CULOARE)))
            .andExpect(jsonPath("$.[*].descriere").value(hasItem(DEFAULT_DESCRIERE)))
            .andExpect(jsonPath("$.[*].conector").value(hasItem(DEFAULT_CONECTOR.toString())));
    }
    
    @Test
    @Transactional
    public void getVehicul() throws Exception {
        // Initialize the database
        vehiculRepository.saveAndFlush(vehicul);

        // Get the vehicul
        restVehiculMockMvc.perform(get("/api/vehiculs/{id}", vehicul.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicul.getId().intValue()))
            .andExpect(jsonPath("$.marca").value(DEFAULT_MARCA))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.anFabricatie").value(DEFAULT_AN_FABRICATIE))
            .andExpect(jsonPath("$.culoare").value(DEFAULT_CULOARE))
            .andExpect(jsonPath("$.descriere").value(DEFAULT_DESCRIERE))
            .andExpect(jsonPath("$.conector").value(DEFAULT_CONECTOR.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingVehicul() throws Exception {
        // Get the vehicul
        restVehiculMockMvc.perform(get("/api/vehiculs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVehicul() throws Exception {
        // Initialize the database
        vehiculRepository.saveAndFlush(vehicul);

        int databaseSizeBeforeUpdate = vehiculRepository.findAll().size();

        // Update the vehicul
        Vehicul updatedVehicul = vehiculRepository.findById(vehicul.getId()).get();
        // Disconnect from session so that the updates on updatedVehicul are not directly saved in db
        em.detach(updatedVehicul);
        updatedVehicul
            .marca(UPDATED_MARCA)
            .model(UPDATED_MODEL)
            .anFabricatie(UPDATED_AN_FABRICATIE)
            .culoare(UPDATED_CULOARE)
            .descriere(UPDATED_DESCRIERE)
            .conector(UPDATED_CONECTOR);

        restVehiculMockMvc.perform(put("/api/vehiculs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVehicul)))
            .andExpect(status().isOk());

        // Validate the Vehicul in the database
        List<Vehicul> vehiculList = vehiculRepository.findAll();
        assertThat(vehiculList).hasSize(databaseSizeBeforeUpdate);
        Vehicul testVehicul = vehiculList.get(vehiculList.size() - 1);
        assertThat(testVehicul.getMarca()).isEqualTo(UPDATED_MARCA);
        assertThat(testVehicul.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testVehicul.getAnFabricatie()).isEqualTo(UPDATED_AN_FABRICATIE);
        assertThat(testVehicul.getCuloare()).isEqualTo(UPDATED_CULOARE);
        assertThat(testVehicul.getDescriere()).isEqualTo(UPDATED_DESCRIERE);
        assertThat(testVehicul.getConector()).isEqualTo(UPDATED_CONECTOR);
    }

    @Test
    @Transactional
    public void updateNonExistingVehicul() throws Exception {
        int databaseSizeBeforeUpdate = vehiculRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehiculMockMvc.perform(put("/api/vehiculs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicul)))
            .andExpect(status().isBadRequest());

        // Validate the Vehicul in the database
        List<Vehicul> vehiculList = vehiculRepository.findAll();
        assertThat(vehiculList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVehicul() throws Exception {
        // Initialize the database
        vehiculRepository.saveAndFlush(vehicul);

        int databaseSizeBeforeDelete = vehiculRepository.findAll().size();

        // Delete the vehicul
        restVehiculMockMvc.perform(delete("/api/vehiculs/{id}", vehicul.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vehicul> vehiculList = vehiculRepository.findAll();
        assertThat(vehiculList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
