package com.mycompany.mikedev.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.mikedev.IntegrationTest;
import com.mycompany.mikedev.domain.Boisson;
import com.mycompany.mikedev.domain.enumeration.Categorie;
import com.mycompany.mikedev.repository.BoissonRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BoissonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BoissonResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final Categorie DEFAULT_TYPE = Categorie.ALCOLISEE;
    private static final Categorie UPDATED_TYPE = Categorie.GASEUSE;

    private static final String ENTITY_API_URL = "/api/boissons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BoissonRepository boissonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBoissonMockMvc;

    private Boisson boisson;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Boisson createEntity(EntityManager em) {
        Boisson boisson = new Boisson().name(DEFAULT_NAME).image(DEFAULT_IMAGE).type(DEFAULT_TYPE);
        return boisson;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Boisson createUpdatedEntity(EntityManager em) {
        Boisson boisson = new Boisson().name(UPDATED_NAME).image(UPDATED_IMAGE).type(UPDATED_TYPE);
        return boisson;
    }

    @BeforeEach
    public void initTest() {
        boisson = createEntity(em);
    }

    @Test
    @Transactional
    void createBoisson() throws Exception {
        int databaseSizeBeforeCreate = boissonRepository.findAll().size();
        // Create the Boisson
        restBoissonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boisson)))
            .andExpect(status().isCreated());

        // Validate the Boisson in the database
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeCreate + 1);
        Boisson testBoisson = boissonList.get(boissonList.size() - 1);
        assertThat(testBoisson.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBoisson.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testBoisson.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createBoissonWithExistingId() throws Exception {
        // Create the Boisson with an existing ID
        boisson.setId(1L);

        int databaseSizeBeforeCreate = boissonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoissonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boisson)))
            .andExpect(status().isBadRequest());

        // Validate the Boisson in the database
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = boissonRepository.findAll().size();
        // set the field null
        boisson.setName(null);

        // Create the Boisson, which fails.

        restBoissonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boisson)))
            .andExpect(status().isBadRequest());

        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = boissonRepository.findAll().size();
        // set the field null
        boisson.setType(null);

        // Create the Boisson, which fails.

        restBoissonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boisson)))
            .andExpect(status().isBadRequest());

        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBoissons() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get all the boissonList
        restBoissonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boisson.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getBoisson() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get the boisson
        restBoissonMockMvc
            .perform(get(ENTITY_API_URL_ID, boisson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(boisson.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBoisson() throws Exception {
        // Get the boisson
        restBoissonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBoisson() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        int databaseSizeBeforeUpdate = boissonRepository.findAll().size();

        // Update the boisson
        Boisson updatedBoisson = boissonRepository.findById(boisson.getId()).get();
        // Disconnect from session so that the updates on updatedBoisson are not directly saved in db
        em.detach(updatedBoisson);
        updatedBoisson.name(UPDATED_NAME).image(UPDATED_IMAGE).type(UPDATED_TYPE);

        restBoissonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBoisson.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBoisson))
            )
            .andExpect(status().isOk());

        // Validate the Boisson in the database
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeUpdate);
        Boisson testBoisson = boissonList.get(boissonList.size() - 1);
        assertThat(testBoisson.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBoisson.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testBoisson.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingBoisson() throws Exception {
        int databaseSizeBeforeUpdate = boissonRepository.findAll().size();
        boisson.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoissonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, boisson.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boisson))
            )
            .andExpect(status().isBadRequest());

        // Validate the Boisson in the database
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBoisson() throws Exception {
        int databaseSizeBeforeUpdate = boissonRepository.findAll().size();
        boisson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoissonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boisson))
            )
            .andExpect(status().isBadRequest());

        // Validate the Boisson in the database
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBoisson() throws Exception {
        int databaseSizeBeforeUpdate = boissonRepository.findAll().size();
        boisson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoissonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boisson)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Boisson in the database
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBoissonWithPatch() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        int databaseSizeBeforeUpdate = boissonRepository.findAll().size();

        // Update the boisson using partial update
        Boisson partialUpdatedBoisson = new Boisson();
        partialUpdatedBoisson.setId(boisson.getId());

        partialUpdatedBoisson.type(UPDATED_TYPE);

        restBoissonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoisson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoisson))
            )
            .andExpect(status().isOk());

        // Validate the Boisson in the database
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeUpdate);
        Boisson testBoisson = boissonList.get(boissonList.size() - 1);
        assertThat(testBoisson.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBoisson.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testBoisson.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateBoissonWithPatch() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        int databaseSizeBeforeUpdate = boissonRepository.findAll().size();

        // Update the boisson using partial update
        Boisson partialUpdatedBoisson = new Boisson();
        partialUpdatedBoisson.setId(boisson.getId());

        partialUpdatedBoisson.name(UPDATED_NAME).image(UPDATED_IMAGE).type(UPDATED_TYPE);

        restBoissonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoisson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoisson))
            )
            .andExpect(status().isOk());

        // Validate the Boisson in the database
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeUpdate);
        Boisson testBoisson = boissonList.get(boissonList.size() - 1);
        assertThat(testBoisson.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBoisson.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testBoisson.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingBoisson() throws Exception {
        int databaseSizeBeforeUpdate = boissonRepository.findAll().size();
        boisson.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoissonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, boisson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boisson))
            )
            .andExpect(status().isBadRequest());

        // Validate the Boisson in the database
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBoisson() throws Exception {
        int databaseSizeBeforeUpdate = boissonRepository.findAll().size();
        boisson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoissonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boisson))
            )
            .andExpect(status().isBadRequest());

        // Validate the Boisson in the database
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBoisson() throws Exception {
        int databaseSizeBeforeUpdate = boissonRepository.findAll().size();
        boisson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoissonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(boisson)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Boisson in the database
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBoisson() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        int databaseSizeBeforeDelete = boissonRepository.findAll().size();

        // Delete the boisson
        restBoissonMockMvc
            .perform(delete(ENTITY_API_URL_ID, boisson.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
