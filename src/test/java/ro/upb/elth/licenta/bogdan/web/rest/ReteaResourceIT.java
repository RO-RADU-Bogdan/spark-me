package ro.upb.elth.licenta.bogdan.web.rest;

import ro.upb.elth.licenta.bogdan.SparkMeApp;
import ro.upb.elth.licenta.bogdan.domain.Retea;
import ro.upb.elth.licenta.bogdan.repository.ReteaRepository;

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
 * Integration tests for the {@link ReteaResource} REST controller.
 */
@SpringBootTest(classes = SparkMeApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ReteaResourceIT {

    private static final String DEFAULT_DENUMIRE = "AAAAAAAAAA";
    private static final String UPDATED_DENUMIRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIERE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIERE = "BBBBBBBBBB";

    @Autowired
    private ReteaRepository reteaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReteaMockMvc;

    private Retea retea;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Retea createEntity(EntityManager em) {
        Retea retea = new Retea()
            .denumire(DEFAULT_DENUMIRE)
            .descriere(DEFAULT_DESCRIERE);
        return retea;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Retea createUpdatedEntity(EntityManager em) {
        Retea retea = new Retea()
            .denumire(UPDATED_DENUMIRE)
            .descriere(UPDATED_DESCRIERE);
        return retea;
    }

    @BeforeEach
    public void initTest() {
        retea = createEntity(em);
    }

    @Test
    @Transactional
    public void createRetea() throws Exception {
        int databaseSizeBeforeCreate = reteaRepository.findAll().size();
        // Create the Retea
        restReteaMockMvc.perform(post("/api/reteas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(retea)))
            .andExpect(status().isCreated());

        // Validate the Retea in the database
        List<Retea> reteaList = reteaRepository.findAll();
        assertThat(reteaList).hasSize(databaseSizeBeforeCreate + 1);
        Retea testRetea = reteaList.get(reteaList.size() - 1);
        assertThat(testRetea.getDenumire()).isEqualTo(DEFAULT_DENUMIRE);
        assertThat(testRetea.getDescriere()).isEqualTo(DEFAULT_DESCRIERE);
    }

    @Test
    @Transactional
    public void createReteaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reteaRepository.findAll().size();

        // Create the Retea with an existing ID
        retea.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReteaMockMvc.perform(post("/api/reteas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(retea)))
            .andExpect(status().isBadRequest());

        // Validate the Retea in the database
        List<Retea> reteaList = reteaRepository.findAll();
        assertThat(reteaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDenumireIsRequired() throws Exception {
        int databaseSizeBeforeTest = reteaRepository.findAll().size();
        // set the field null
        retea.setDenumire(null);

        // Create the Retea, which fails.


        restReteaMockMvc.perform(post("/api/reteas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(retea)))
            .andExpect(status().isBadRequest());

        List<Retea> reteaList = reteaRepository.findAll();
        assertThat(reteaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReteas() throws Exception {
        // Initialize the database
        reteaRepository.saveAndFlush(retea);

        // Get all the reteaList
        restReteaMockMvc.perform(get("/api/reteas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(retea.getId().intValue())))
            .andExpect(jsonPath("$.[*].denumire").value(hasItem(DEFAULT_DENUMIRE)))
            .andExpect(jsonPath("$.[*].descriere").value(hasItem(DEFAULT_DESCRIERE)));
    }
    
    @Test
    @Transactional
    public void getRetea() throws Exception {
        // Initialize the database
        reteaRepository.saveAndFlush(retea);

        // Get the retea
        restReteaMockMvc.perform(get("/api/reteas/{id}", retea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(retea.getId().intValue()))
            .andExpect(jsonPath("$.denumire").value(DEFAULT_DENUMIRE))
            .andExpect(jsonPath("$.descriere").value(DEFAULT_DESCRIERE));
    }
    @Test
    @Transactional
    public void getNonExistingRetea() throws Exception {
        // Get the retea
        restReteaMockMvc.perform(get("/api/reteas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRetea() throws Exception {
        // Initialize the database
        reteaRepository.saveAndFlush(retea);

        int databaseSizeBeforeUpdate = reteaRepository.findAll().size();

        // Update the retea
        Retea updatedRetea = reteaRepository.findById(retea.getId()).get();
        // Disconnect from session so that the updates on updatedRetea are not directly saved in db
        em.detach(updatedRetea);
        updatedRetea
            .denumire(UPDATED_DENUMIRE)
            .descriere(UPDATED_DESCRIERE);

        restReteaMockMvc.perform(put("/api/reteas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRetea)))
            .andExpect(status().isOk());

        // Validate the Retea in the database
        List<Retea> reteaList = reteaRepository.findAll();
        assertThat(reteaList).hasSize(databaseSizeBeforeUpdate);
        Retea testRetea = reteaList.get(reteaList.size() - 1);
        assertThat(testRetea.getDenumire()).isEqualTo(UPDATED_DENUMIRE);
        assertThat(testRetea.getDescriere()).isEqualTo(UPDATED_DESCRIERE);
    }

    @Test
    @Transactional
    public void updateNonExistingRetea() throws Exception {
        int databaseSizeBeforeUpdate = reteaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReteaMockMvc.perform(put("/api/reteas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(retea)))
            .andExpect(status().isBadRequest());

        // Validate the Retea in the database
        List<Retea> reteaList = reteaRepository.findAll();
        assertThat(reteaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRetea() throws Exception {
        // Initialize the database
        reteaRepository.saveAndFlush(retea);

        int databaseSizeBeforeDelete = reteaRepository.findAll().size();

        // Delete the retea
        restReteaMockMvc.perform(delete("/api/reteas/{id}", retea.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Retea> reteaList = reteaRepository.findAll();
        assertThat(reteaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
