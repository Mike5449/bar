package com.mycompany.mikedev.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.mikedev.IntegrationTest;
import com.mycompany.mikedev.domain.Prix;
import com.mycompany.mikedev.repository.PrixRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PrixResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrixResourceIT {

    private static final Float DEFAULT_PRIX = 1F;
    private static final Float UPDATED_PRIX = 2F;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/prixes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrixRepository prixRepository;

    @Mock
    private PrixRepository prixRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrixMockMvc;

    private Prix prix;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prix createEntity(EntityManager em) {
        Prix prix = new Prix().prix(DEFAULT_PRIX).date(DEFAULT_DATE);
        return prix;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prix createUpdatedEntity(EntityManager em) {
        Prix prix = new Prix().prix(UPDATED_PRIX).date(UPDATED_DATE);
        return prix;
    }

    @BeforeEach
    public void initTest() {
        prix = createEntity(em);
    }

    @Test
    @Transactional
    void createPrix() throws Exception {
        int databaseSizeBeforeCreate = prixRepository.findAll().size();
        // Create the Prix
        restPrixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prix)))
            .andExpect(status().isCreated());

        // Validate the Prix in the database
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeCreate + 1);
        Prix testPrix = prixList.get(prixList.size() - 1);
        assertThat(testPrix.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testPrix.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createPrixWithExistingId() throws Exception {
        // Create the Prix with an existing ID
        prix.setId(1L);

        int databaseSizeBeforeCreate = prixRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prix)))
            .andExpect(status().isBadRequest());

        // Validate the Prix in the database
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPrixIsRequired() throws Exception {
        int databaseSizeBeforeTest = prixRepository.findAll().size();
        // set the field null
        prix.setPrix(null);

        // Create the Prix, which fails.

        restPrixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prix)))
            .andExpect(status().isBadRequest());

        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = prixRepository.findAll().size();
        // set the field null
        prix.setDate(null);

        // Create the Prix, which fails.

        restPrixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prix)))
            .andExpect(status().isBadRequest());

        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrixes() throws Exception {
        // Initialize the database
        prixRepository.saveAndFlush(prix);

        // Get all the prixList
        restPrixMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prix.getId().intValue())))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrixesWithEagerRelationshipsIsEnabled() throws Exception {
        when(prixRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrixMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prixRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrixesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(prixRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrixMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(prixRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPrix() throws Exception {
        // Initialize the database
        prixRepository.saveAndFlush(prix);

        // Get the prix
        restPrixMockMvc
            .perform(get(ENTITY_API_URL_ID, prix.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prix.getId().intValue()))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPrix() throws Exception {
        // Get the prix
        restPrixMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrix() throws Exception {
        // Initialize the database
        prixRepository.saveAndFlush(prix);

        int databaseSizeBeforeUpdate = prixRepository.findAll().size();

        // Update the prix
        Prix updatedPrix = prixRepository.findById(prix.getId()).get();
        // Disconnect from session so that the updates on updatedPrix are not directly saved in db
        em.detach(updatedPrix);
        updatedPrix.prix(UPDATED_PRIX).date(UPDATED_DATE);

        restPrixMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrix.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrix))
            )
            .andExpect(status().isOk());

        // Validate the Prix in the database
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeUpdate);
        Prix testPrix = prixList.get(prixList.size() - 1);
        assertThat(testPrix.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testPrix.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPrix() throws Exception {
        int databaseSizeBeforeUpdate = prixRepository.findAll().size();
        prix.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrixMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prix.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prix))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prix in the database
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrix() throws Exception {
        int databaseSizeBeforeUpdate = prixRepository.findAll().size();
        prix.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrixMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prix))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prix in the database
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrix() throws Exception {
        int databaseSizeBeforeUpdate = prixRepository.findAll().size();
        prix.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrixMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prix)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prix in the database
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrixWithPatch() throws Exception {
        // Initialize the database
        prixRepository.saveAndFlush(prix);

        int databaseSizeBeforeUpdate = prixRepository.findAll().size();

        // Update the prix using partial update
        Prix partialUpdatedPrix = new Prix();
        partialUpdatedPrix.setId(prix.getId());

        partialUpdatedPrix.prix(UPDATED_PRIX);

        restPrixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrix.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrix))
            )
            .andExpect(status().isOk());

        // Validate the Prix in the database
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeUpdate);
        Prix testPrix = prixList.get(prixList.size() - 1);
        assertThat(testPrix.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testPrix.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePrixWithPatch() throws Exception {
        // Initialize the database
        prixRepository.saveAndFlush(prix);

        int databaseSizeBeforeUpdate = prixRepository.findAll().size();

        // Update the prix using partial update
        Prix partialUpdatedPrix = new Prix();
        partialUpdatedPrix.setId(prix.getId());

        partialUpdatedPrix.prix(UPDATED_PRIX).date(UPDATED_DATE);

        restPrixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrix.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrix))
            )
            .andExpect(status().isOk());

        // Validate the Prix in the database
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeUpdate);
        Prix testPrix = prixList.get(prixList.size() - 1);
        assertThat(testPrix.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testPrix.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPrix() throws Exception {
        int databaseSizeBeforeUpdate = prixRepository.findAll().size();
        prix.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prix.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prix))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prix in the database
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrix() throws Exception {
        int databaseSizeBeforeUpdate = prixRepository.findAll().size();
        prix.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prix))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prix in the database
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrix() throws Exception {
        int databaseSizeBeforeUpdate = prixRepository.findAll().size();
        prix.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrixMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(prix)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prix in the database
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrix() throws Exception {
        // Initialize the database
        prixRepository.saveAndFlush(prix);

        int databaseSizeBeforeDelete = prixRepository.findAll().size();

        // Delete the prix
        restPrixMockMvc
            .perform(delete(ENTITY_API_URL_ID, prix.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
