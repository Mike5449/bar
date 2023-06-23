package com.mycompany.mikedev.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.mikedev.IntegrationTest;
import com.mycompany.mikedev.domain.CompteCaisse;
import com.mycompany.mikedev.domain.enumeration.StatusCaisse;
import com.mycompany.mikedev.repository.CompteCaisseRepository;
import com.mycompany.mikedev.service.dto.CompteCaisseDTO;
import com.mycompany.mikedev.service.mapper.CompteCaisseMapper;
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
 * Integration tests for the {@link CompteCaisseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompteCaisseResourceIT {

    private static final Long DEFAULT_INJECTION = 1L;
    private static final Long UPDATED_INJECTION = 2L;

    private static final Long DEFAULT_SALE = 1L;
    private static final Long UPDATED_SALE = 2L;

    private static final Long DEFAULT_CANCEL = 1L;
    private static final Long UPDATED_CANCEL = 2L;

    private static final Long DEFAULT_CASH = 1L;
    private static final Long UPDATED_CASH = 2L;

    private static final Long DEFAULT_PRET = 1L;
    private static final Long UPDATED_PRET = 2L;

    private static final Long DEFAULT_BALANCE = 1L;
    private static final Long UPDATED_BALANCE = 2L;

    private static final Long DEFAULT_A_VERSER = 1L;
    private static final Long UPDATED_A_VERSER = 2L;

    private static final StatusCaisse DEFAULT_STATUS = StatusCaisse.ACTIVE;
    private static final StatusCaisse UPDATED_STATUS = StatusCaisse.INACTIVE;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/compte-caisses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompteCaisseRepository compteCaisseRepository;

    @Autowired
    private CompteCaisseMapper compteCaisseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompteCaisseMockMvc;

    private CompteCaisse compteCaisse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompteCaisse createEntity(EntityManager em) {
        CompteCaisse compteCaisse = new CompteCaisse()
            .injection(DEFAULT_INJECTION)
            .sale(DEFAULT_SALE)
            .cancel(DEFAULT_CANCEL)
            .cash(DEFAULT_CASH)
            .pret(DEFAULT_PRET)
            .balance(DEFAULT_BALANCE)
            .aVerser(DEFAULT_A_VERSER)
            .status(DEFAULT_STATUS)
            .name(DEFAULT_NAME);
        return compteCaisse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompteCaisse createUpdatedEntity(EntityManager em) {
        CompteCaisse compteCaisse = new CompteCaisse()
            .injection(UPDATED_INJECTION)
            .sale(UPDATED_SALE)
            .cancel(UPDATED_CANCEL)
            .cash(UPDATED_CASH)
            .pret(UPDATED_PRET)
            .balance(UPDATED_BALANCE)
            .aVerser(UPDATED_A_VERSER)
            .status(UPDATED_STATUS)
            .name(UPDATED_NAME);
        return compteCaisse;
    }

    @BeforeEach
    public void initTest() {
        compteCaisse = createEntity(em);
    }

    @Test
    @Transactional
    void createCompteCaisse() throws Exception {
        int databaseSizeBeforeCreate = compteCaisseRepository.findAll().size();
        // Create the CompteCaisse
        CompteCaisseDTO compteCaisseDTO = compteCaisseMapper.toDto(compteCaisse);
        restCompteCaisseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteCaisseDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CompteCaisse in the database
        List<CompteCaisse> compteCaisseList = compteCaisseRepository.findAll();
        assertThat(compteCaisseList).hasSize(databaseSizeBeforeCreate + 1);
        CompteCaisse testCompteCaisse = compteCaisseList.get(compteCaisseList.size() - 1);
        assertThat(testCompteCaisse.getInjection()).isEqualTo(DEFAULT_INJECTION);
        assertThat(testCompteCaisse.getSale()).isEqualTo(DEFAULT_SALE);
        assertThat(testCompteCaisse.getCancel()).isEqualTo(DEFAULT_CANCEL);
        assertThat(testCompteCaisse.getCash()).isEqualTo(DEFAULT_CASH);
        assertThat(testCompteCaisse.getPret()).isEqualTo(DEFAULT_PRET);
        assertThat(testCompteCaisse.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testCompteCaisse.getaVerser()).isEqualTo(DEFAULT_A_VERSER);
        assertThat(testCompteCaisse.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCompteCaisse.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createCompteCaisseWithExistingId() throws Exception {
        // Create the CompteCaisse with an existing ID
        compteCaisse.setId(1L);
        CompteCaisseDTO compteCaisseDTO = compteCaisseMapper.toDto(compteCaisse);

        int databaseSizeBeforeCreate = compteCaisseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompteCaisseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteCaisseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteCaisse in the database
        List<CompteCaisse> compteCaisseList = compteCaisseRepository.findAll();
        assertThat(compteCaisseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompteCaisses() throws Exception {
        // Initialize the database
        compteCaisseRepository.saveAndFlush(compteCaisse);

        // Get all the compteCaisseList
        restCompteCaisseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compteCaisse.getId().intValue())))
            .andExpect(jsonPath("$.[*].injection").value(hasItem(DEFAULT_INJECTION.intValue())))
            .andExpect(jsonPath("$.[*].sale").value(hasItem(DEFAULT_SALE.intValue())))
            .andExpect(jsonPath("$.[*].cancel").value(hasItem(DEFAULT_CANCEL.intValue())))
            .andExpect(jsonPath("$.[*].cash").value(hasItem(DEFAULT_CASH.intValue())))
            .andExpect(jsonPath("$.[*].pret").value(hasItem(DEFAULT_PRET.intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].aVerser").value(hasItem(DEFAULT_A_VERSER.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCompteCaisse() throws Exception {
        // Initialize the database
        compteCaisseRepository.saveAndFlush(compteCaisse);

        // Get the compteCaisse
        restCompteCaisseMockMvc
            .perform(get(ENTITY_API_URL_ID, compteCaisse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compteCaisse.getId().intValue()))
            .andExpect(jsonPath("$.injection").value(DEFAULT_INJECTION.intValue()))
            .andExpect(jsonPath("$.sale").value(DEFAULT_SALE.intValue()))
            .andExpect(jsonPath("$.cancel").value(DEFAULT_CANCEL.intValue()))
            .andExpect(jsonPath("$.cash").value(DEFAULT_CASH.intValue()))
            .andExpect(jsonPath("$.pret").value(DEFAULT_PRET.intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()))
            .andExpect(jsonPath("$.aVerser").value(DEFAULT_A_VERSER.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCompteCaisse() throws Exception {
        // Get the compteCaisse
        restCompteCaisseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompteCaisse() throws Exception {
        // Initialize the database
        compteCaisseRepository.saveAndFlush(compteCaisse);

        int databaseSizeBeforeUpdate = compteCaisseRepository.findAll().size();

        // Update the compteCaisse
        CompteCaisse updatedCompteCaisse = compteCaisseRepository.findById(compteCaisse.getId()).get();
        // Disconnect from session so that the updates on updatedCompteCaisse are not directly saved in db
        em.detach(updatedCompteCaisse);
        updatedCompteCaisse
            .injection(UPDATED_INJECTION)
            .sale(UPDATED_SALE)
            .cancel(UPDATED_CANCEL)
            .cash(UPDATED_CASH)
            .pret(UPDATED_PRET)
            .balance(UPDATED_BALANCE)
            .aVerser(UPDATED_A_VERSER)
            .status(UPDATED_STATUS)
            .name(UPDATED_NAME);
        CompteCaisseDTO compteCaisseDTO = compteCaisseMapper.toDto(updatedCompteCaisse);

        restCompteCaisseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteCaisseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteCaisseDTO))
            )
            .andExpect(status().isOk());

        // Validate the CompteCaisse in the database
        List<CompteCaisse> compteCaisseList = compteCaisseRepository.findAll();
        assertThat(compteCaisseList).hasSize(databaseSizeBeforeUpdate);
        CompteCaisse testCompteCaisse = compteCaisseList.get(compteCaisseList.size() - 1);
        assertThat(testCompteCaisse.getInjection()).isEqualTo(UPDATED_INJECTION);
        assertThat(testCompteCaisse.getSale()).isEqualTo(UPDATED_SALE);
        assertThat(testCompteCaisse.getCancel()).isEqualTo(UPDATED_CANCEL);
        assertThat(testCompteCaisse.getCash()).isEqualTo(UPDATED_CASH);
        assertThat(testCompteCaisse.getPret()).isEqualTo(UPDATED_PRET);
        assertThat(testCompteCaisse.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testCompteCaisse.getaVerser()).isEqualTo(UPDATED_A_VERSER);
        assertThat(testCompteCaisse.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCompteCaisse.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCompteCaisse() throws Exception {
        int databaseSizeBeforeUpdate = compteCaisseRepository.findAll().size();
        compteCaisse.setId(count.incrementAndGet());

        // Create the CompteCaisse
        CompteCaisseDTO compteCaisseDTO = compteCaisseMapper.toDto(compteCaisse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteCaisseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteCaisseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteCaisseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteCaisse in the database
        List<CompteCaisse> compteCaisseList = compteCaisseRepository.findAll();
        assertThat(compteCaisseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompteCaisse() throws Exception {
        int databaseSizeBeforeUpdate = compteCaisseRepository.findAll().size();
        compteCaisse.setId(count.incrementAndGet());

        // Create the CompteCaisse
        CompteCaisseDTO compteCaisseDTO = compteCaisseMapper.toDto(compteCaisse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteCaisseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteCaisseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteCaisse in the database
        List<CompteCaisse> compteCaisseList = compteCaisseRepository.findAll();
        assertThat(compteCaisseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompteCaisse() throws Exception {
        int databaseSizeBeforeUpdate = compteCaisseRepository.findAll().size();
        compteCaisse.setId(count.incrementAndGet());

        // Create the CompteCaisse
        CompteCaisseDTO compteCaisseDTO = compteCaisseMapper.toDto(compteCaisse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteCaisseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteCaisseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompteCaisse in the database
        List<CompteCaisse> compteCaisseList = compteCaisseRepository.findAll();
        assertThat(compteCaisseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompteCaisseWithPatch() throws Exception {
        // Initialize the database
        compteCaisseRepository.saveAndFlush(compteCaisse);

        int databaseSizeBeforeUpdate = compteCaisseRepository.findAll().size();

        // Update the compteCaisse using partial update
        CompteCaisse partialUpdatedCompteCaisse = new CompteCaisse();
        partialUpdatedCompteCaisse.setId(compteCaisse.getId());

        partialUpdatedCompteCaisse.injection(UPDATED_INJECTION).balance(UPDATED_BALANCE).status(UPDATED_STATUS).name(UPDATED_NAME);

        restCompteCaisseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompteCaisse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompteCaisse))
            )
            .andExpect(status().isOk());

        // Validate the CompteCaisse in the database
        List<CompteCaisse> compteCaisseList = compteCaisseRepository.findAll();
        assertThat(compteCaisseList).hasSize(databaseSizeBeforeUpdate);
        CompteCaisse testCompteCaisse = compteCaisseList.get(compteCaisseList.size() - 1);
        assertThat(testCompteCaisse.getInjection()).isEqualTo(UPDATED_INJECTION);
        assertThat(testCompteCaisse.getSale()).isEqualTo(DEFAULT_SALE);
        assertThat(testCompteCaisse.getCancel()).isEqualTo(DEFAULT_CANCEL);
        assertThat(testCompteCaisse.getCash()).isEqualTo(DEFAULT_CASH);
        assertThat(testCompteCaisse.getPret()).isEqualTo(DEFAULT_PRET);
        assertThat(testCompteCaisse.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testCompteCaisse.getaVerser()).isEqualTo(DEFAULT_A_VERSER);
        assertThat(testCompteCaisse.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCompteCaisse.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCompteCaisseWithPatch() throws Exception {
        // Initialize the database
        compteCaisseRepository.saveAndFlush(compteCaisse);

        int databaseSizeBeforeUpdate = compteCaisseRepository.findAll().size();

        // Update the compteCaisse using partial update
        CompteCaisse partialUpdatedCompteCaisse = new CompteCaisse();
        partialUpdatedCompteCaisse.setId(compteCaisse.getId());

        partialUpdatedCompteCaisse
            .injection(UPDATED_INJECTION)
            .sale(UPDATED_SALE)
            .cancel(UPDATED_CANCEL)
            .cash(UPDATED_CASH)
            .pret(UPDATED_PRET)
            .balance(UPDATED_BALANCE)
            .aVerser(UPDATED_A_VERSER)
            .status(UPDATED_STATUS)
            .name(UPDATED_NAME);

        restCompteCaisseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompteCaisse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompteCaisse))
            )
            .andExpect(status().isOk());

        // Validate the CompteCaisse in the database
        List<CompteCaisse> compteCaisseList = compteCaisseRepository.findAll();
        assertThat(compteCaisseList).hasSize(databaseSizeBeforeUpdate);
        CompteCaisse testCompteCaisse = compteCaisseList.get(compteCaisseList.size() - 1);
        assertThat(testCompteCaisse.getInjection()).isEqualTo(UPDATED_INJECTION);
        assertThat(testCompteCaisse.getSale()).isEqualTo(UPDATED_SALE);
        assertThat(testCompteCaisse.getCancel()).isEqualTo(UPDATED_CANCEL);
        assertThat(testCompteCaisse.getCash()).isEqualTo(UPDATED_CASH);
        assertThat(testCompteCaisse.getPret()).isEqualTo(UPDATED_PRET);
        assertThat(testCompteCaisse.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testCompteCaisse.getaVerser()).isEqualTo(UPDATED_A_VERSER);
        assertThat(testCompteCaisse.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCompteCaisse.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCompteCaisse() throws Exception {
        int databaseSizeBeforeUpdate = compteCaisseRepository.findAll().size();
        compteCaisse.setId(count.incrementAndGet());

        // Create the CompteCaisse
        CompteCaisseDTO compteCaisseDTO = compteCaisseMapper.toDto(compteCaisse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteCaisseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compteCaisseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compteCaisseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteCaisse in the database
        List<CompteCaisse> compteCaisseList = compteCaisseRepository.findAll();
        assertThat(compteCaisseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompteCaisse() throws Exception {
        int databaseSizeBeforeUpdate = compteCaisseRepository.findAll().size();
        compteCaisse.setId(count.incrementAndGet());

        // Create the CompteCaisse
        CompteCaisseDTO compteCaisseDTO = compteCaisseMapper.toDto(compteCaisse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteCaisseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compteCaisseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteCaisse in the database
        List<CompteCaisse> compteCaisseList = compteCaisseRepository.findAll();
        assertThat(compteCaisseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompteCaisse() throws Exception {
        int databaseSizeBeforeUpdate = compteCaisseRepository.findAll().size();
        compteCaisse.setId(count.incrementAndGet());

        // Create the CompteCaisse
        CompteCaisseDTO compteCaisseDTO = compteCaisseMapper.toDto(compteCaisse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteCaisseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compteCaisseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompteCaisse in the database
        List<CompteCaisse> compteCaisseList = compteCaisseRepository.findAll();
        assertThat(compteCaisseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompteCaisse() throws Exception {
        // Initialize the database
        compteCaisseRepository.saveAndFlush(compteCaisse);

        int databaseSizeBeforeDelete = compteCaisseRepository.findAll().size();

        // Delete the compteCaisse
        restCompteCaisseMockMvc
            .perform(delete(ENTITY_API_URL_ID, compteCaisse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompteCaisse> compteCaisseList = compteCaisseRepository.findAll();
        assertThat(compteCaisseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
