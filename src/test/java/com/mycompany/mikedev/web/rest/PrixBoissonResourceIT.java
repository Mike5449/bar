package com.mycompany.mikedev.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.mikedev.IntegrationTest;
import com.mycompany.mikedev.domain.PrixBoisson;
import com.mycompany.mikedev.repository.PrixBoissonRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PrixBoissonResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrixBoissonResourceIT {

    private static final Float DEFAULT_PRIX = 1F;
    private static final Float UPDATED_PRIX = 2F;

    private static final String ENTITY_API_URL = "/api/prix-boissons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrixBoissonRepository prixBoissonRepository;

    @Mock
    private PrixBoissonRepository prixBoissonRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrixBoissonMockMvc;

    private PrixBoisson prixBoisson;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrixBoisson createEntity(EntityManager em) {
        PrixBoisson prixBoisson = new PrixBoisson().prix(DEFAULT_PRIX);
        return prixBoisson;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrixBoisson createUpdatedEntity(EntityManager em) {
        PrixBoisson prixBoisson = new PrixBoisson().prix(UPDATED_PRIX);
        return prixBoisson;
    }

    @BeforeEach
    public void initTest() {
        prixBoisson = createEntity(em);
    }

    @Test
    @Transactional
    void createPrixBoisson() throws Exception {
        int databaseSizeBeforeCreate = prixBoissonRepository.findAll().size();
        // Create the PrixBoisson
        restPrixBoissonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prixBoisson)))
            .andExpect(status().isCreated());

        // Validate the PrixBoisson in the database
        List<PrixBoisson> prixBoissonList = prixBoissonRepository.findAll();
        assertThat(prixBoissonList).hasSize(databaseSizeBeforeCreate + 1);
        PrixBoisson testPrixBoisson = prixBoissonList.get(prixBoissonList.size() - 1);
        assertThat(testPrixBoisson.getPrix()).isEqualTo(DEFAULT_PRIX);
    }

    @Test
    @Transactional
    void createPrixBoissonWithExistingId() throws Exception {
        // Create the PrixBoisson with an existing ID
        prixBoisson.setId(1L);

        int databaseSizeBeforeCreate = prixBoissonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrixBoissonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prixBoisson)))
            .andExpect(status().isBadRequest());

        // Validate the PrixBoisson in the database
        List<PrixBoisson> prixBoissonList = prixBoissonRepository.findAll();
        assertThat(prixBoissonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPrixIsRequired() throws Exception {
        int databaseSizeBeforeTest = prixBoissonRepository.findAll().size();
        // set the field null
        prixBoisson.setPrix(null);

        // Create the PrixBoisson, which fails.

        restPrixBoissonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prixBoisson)))
            .andExpect(status().isBadRequest());

        List<PrixBoisson> prixBoissonList = prixBoissonRepository.findAll();
        assertThat(prixBoissonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrixBoissons() throws Exception {
        // Initialize the database
        prixBoissonRepository.saveAndFlush(prixBoisson);

        // Get all the prixBoissonList
        restPrixBoissonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prixBoisson.getId().intValue())))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrixBoissonsWithEagerRelationshipsIsEnabled() throws Exception {
        when(prixBoissonRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrixBoissonMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prixBoissonRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrixBoissonsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(prixBoissonRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrixBoissonMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(prixBoissonRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPrixBoisson() throws Exception {
        // Initialize the database
        prixBoissonRepository.saveAndFlush(prixBoisson);

        // Get the prixBoisson
        restPrixBoissonMockMvc
            .perform(get(ENTITY_API_URL_ID, prixBoisson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prixBoisson.getId().intValue()))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPrixBoisson() throws Exception {
        // Get the prixBoisson
        restPrixBoissonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrixBoisson() throws Exception {
        // Initialize the database
        prixBoissonRepository.saveAndFlush(prixBoisson);

        int databaseSizeBeforeUpdate = prixBoissonRepository.findAll().size();

        // Update the prixBoisson
        PrixBoisson updatedPrixBoisson = prixBoissonRepository.findById(prixBoisson.getId()).get();
        // Disconnect from session so that the updates on updatedPrixBoisson are not directly saved in db
        em.detach(updatedPrixBoisson);
        updatedPrixBoisson.prix(UPDATED_PRIX);

        restPrixBoissonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrixBoisson.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrixBoisson))
            )
            .andExpect(status().isOk());

        // Validate the PrixBoisson in the database
        List<PrixBoisson> prixBoissonList = prixBoissonRepository.findAll();
        assertThat(prixBoissonList).hasSize(databaseSizeBeforeUpdate);
        PrixBoisson testPrixBoisson = prixBoissonList.get(prixBoissonList.size() - 1);
        assertThat(testPrixBoisson.getPrix()).isEqualTo(UPDATED_PRIX);
    }

    @Test
    @Transactional
    void putNonExistingPrixBoisson() throws Exception {
        int databaseSizeBeforeUpdate = prixBoissonRepository.findAll().size();
        prixBoisson.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrixBoissonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prixBoisson.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prixBoisson))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrixBoisson in the database
        List<PrixBoisson> prixBoissonList = prixBoissonRepository.findAll();
        assertThat(prixBoissonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrixBoisson() throws Exception {
        int databaseSizeBeforeUpdate = prixBoissonRepository.findAll().size();
        prixBoisson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrixBoissonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prixBoisson))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrixBoisson in the database
        List<PrixBoisson> prixBoissonList = prixBoissonRepository.findAll();
        assertThat(prixBoissonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrixBoisson() throws Exception {
        int databaseSizeBeforeUpdate = prixBoissonRepository.findAll().size();
        prixBoisson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrixBoissonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prixBoisson)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrixBoisson in the database
        List<PrixBoisson> prixBoissonList = prixBoissonRepository.findAll();
        assertThat(prixBoissonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrixBoissonWithPatch() throws Exception {
        // Initialize the database
        prixBoissonRepository.saveAndFlush(prixBoisson);

        int databaseSizeBeforeUpdate = prixBoissonRepository.findAll().size();

        // Update the prixBoisson using partial update
        PrixBoisson partialUpdatedPrixBoisson = new PrixBoisson();
        partialUpdatedPrixBoisson.setId(prixBoisson.getId());

        partialUpdatedPrixBoisson.prix(UPDATED_PRIX);

        restPrixBoissonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrixBoisson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrixBoisson))
            )
            .andExpect(status().isOk());

        // Validate the PrixBoisson in the database
        List<PrixBoisson> prixBoissonList = prixBoissonRepository.findAll();
        assertThat(prixBoissonList).hasSize(databaseSizeBeforeUpdate);
        PrixBoisson testPrixBoisson = prixBoissonList.get(prixBoissonList.size() - 1);
        assertThat(testPrixBoisson.getPrix()).isEqualTo(UPDATED_PRIX);
    }

    @Test
    @Transactional
    void fullUpdatePrixBoissonWithPatch() throws Exception {
        // Initialize the database
        prixBoissonRepository.saveAndFlush(prixBoisson);

        int databaseSizeBeforeUpdate = prixBoissonRepository.findAll().size();

        // Update the prixBoisson using partial update
        PrixBoisson partialUpdatedPrixBoisson = new PrixBoisson();
        partialUpdatedPrixBoisson.setId(prixBoisson.getId());

        partialUpdatedPrixBoisson.prix(UPDATED_PRIX);

        restPrixBoissonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrixBoisson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrixBoisson))
            )
            .andExpect(status().isOk());

        // Validate the PrixBoisson in the database
        List<PrixBoisson> prixBoissonList = prixBoissonRepository.findAll();
        assertThat(prixBoissonList).hasSize(databaseSizeBeforeUpdate);
        PrixBoisson testPrixBoisson = prixBoissonList.get(prixBoissonList.size() - 1);
        assertThat(testPrixBoisson.getPrix()).isEqualTo(UPDATED_PRIX);
    }

    @Test
    @Transactional
    void patchNonExistingPrixBoisson() throws Exception {
        int databaseSizeBeforeUpdate = prixBoissonRepository.findAll().size();
        prixBoisson.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrixBoissonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prixBoisson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prixBoisson))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrixBoisson in the database
        List<PrixBoisson> prixBoissonList = prixBoissonRepository.findAll();
        assertThat(prixBoissonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrixBoisson() throws Exception {
        int databaseSizeBeforeUpdate = prixBoissonRepository.findAll().size();
        prixBoisson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrixBoissonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prixBoisson))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrixBoisson in the database
        List<PrixBoisson> prixBoissonList = prixBoissonRepository.findAll();
        assertThat(prixBoissonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrixBoisson() throws Exception {
        int databaseSizeBeforeUpdate = prixBoissonRepository.findAll().size();
        prixBoisson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrixBoissonMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(prixBoisson))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrixBoisson in the database
        List<PrixBoisson> prixBoissonList = prixBoissonRepository.findAll();
        assertThat(prixBoissonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrixBoisson() throws Exception {
        // Initialize the database
        prixBoissonRepository.saveAndFlush(prixBoisson);

        int databaseSizeBeforeDelete = prixBoissonRepository.findAll().size();

        // Delete the prixBoisson
        restPrixBoissonMockMvc
            .perform(delete(ENTITY_API_URL_ID, prixBoisson.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PrixBoisson> prixBoissonList = prixBoissonRepository.findAll();
        assertThat(prixBoissonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
