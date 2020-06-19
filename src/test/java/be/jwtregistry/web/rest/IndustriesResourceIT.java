package be.jwtregistry.web.rest;

import be.jwtregistry.BejwtregistryApp;
import be.jwtregistry.domain.Industries;
import be.jwtregistry.repository.IndustriesRepository;
import be.jwtregistry.service.IndustriesService;

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
 * Integration tests for the {@link IndustriesResource} REST controller.
 */
@SpringBootTest(classes = BejwtregistryApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class IndustriesResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private IndustriesRepository industriesRepository;

    @Autowired
    private IndustriesService industriesService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndustriesMockMvc;

    private Industries industries;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Industries createEntity(EntityManager em) {
        Industries industries = new Industries()
            .description(DEFAULT_DESCRIPTION)
            .name(DEFAULT_NAME);
        return industries;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Industries createUpdatedEntity(EntityManager em) {
        Industries industries = new Industries()
            .description(UPDATED_DESCRIPTION)
            .name(UPDATED_NAME);
        return industries;
    }

    @BeforeEach
    public void initTest() {
        industries = createEntity(em);
    }

    @Test
    @Transactional
    public void createIndustries() throws Exception {
        int databaseSizeBeforeCreate = industriesRepository.findAll().size();
        // Create the Industries
        restIndustriesMockMvc.perform(post("/api/industries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(industries)))
            .andExpect(status().isCreated());

        // Validate the Industries in the database
        List<Industries> industriesList = industriesRepository.findAll();
        assertThat(industriesList).hasSize(databaseSizeBeforeCreate + 1);
        Industries testIndustries = industriesList.get(industriesList.size() - 1);
        assertThat(testIndustries.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIndustries.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createIndustriesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = industriesRepository.findAll().size();

        // Create the Industries with an existing ID
        industries.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndustriesMockMvc.perform(post("/api/industries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(industries)))
            .andExpect(status().isBadRequest());

        // Validate the Industries in the database
        List<Industries> industriesList = industriesRepository.findAll();
        assertThat(industriesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllIndustries() throws Exception {
        // Initialize the database
        industriesRepository.saveAndFlush(industries);

        // Get all the industriesList
        restIndustriesMockMvc.perform(get("/api/industries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(industries.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getIndustries() throws Exception {
        // Initialize the database
        industriesRepository.saveAndFlush(industries);

        // Get the industries
        restIndustriesMockMvc.perform(get("/api/industries/{id}", industries.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(industries.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingIndustries() throws Exception {
        // Get the industries
        restIndustriesMockMvc.perform(get("/api/industries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIndustries() throws Exception {
        // Initialize the database
        industriesService.save(industries);

        int databaseSizeBeforeUpdate = industriesRepository.findAll().size();

        // Update the industries
        Industries updatedIndustries = industriesRepository.findById(industries.getId()).get();
        // Disconnect from session so that the updates on updatedIndustries are not directly saved in db
        em.detach(updatedIndustries);
        updatedIndustries
            .description(UPDATED_DESCRIPTION)
            .name(UPDATED_NAME);

        restIndustriesMockMvc.perform(put("/api/industries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedIndustries)))
            .andExpect(status().isOk());

        // Validate the Industries in the database
        List<Industries> industriesList = industriesRepository.findAll();
        assertThat(industriesList).hasSize(databaseSizeBeforeUpdate);
        Industries testIndustries = industriesList.get(industriesList.size() - 1);
        assertThat(testIndustries.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIndustries.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingIndustries() throws Exception {
        int databaseSizeBeforeUpdate = industriesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndustriesMockMvc.perform(put("/api/industries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(industries)))
            .andExpect(status().isBadRequest());

        // Validate the Industries in the database
        List<Industries> industriesList = industriesRepository.findAll();
        assertThat(industriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteIndustries() throws Exception {
        // Initialize the database
        industriesService.save(industries);

        int databaseSizeBeforeDelete = industriesRepository.findAll().size();

        // Delete the industries
        restIndustriesMockMvc.perform(delete("/api/industries/{id}", industries.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Industries> industriesList = industriesRepository.findAll();
        assertThat(industriesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
