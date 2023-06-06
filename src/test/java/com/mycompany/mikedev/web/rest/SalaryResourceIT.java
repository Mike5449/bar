package com.mycompany.mikedev.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.mikedev.IntegrationTest;
import com.mycompany.mikedev.domain.Salary;
import com.mycompany.mikedev.repository.SalaryRepository;
import com.mycompany.mikedev.service.SalaryService;
import com.mycompany.mikedev.service.dto.SalaryDTO;
import com.mycompany.mikedev.service.mapper.SalaryMapper;
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
 * Integration tests for the {@link SalaryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SalaryResourceIT {

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String ENTITY_API_URL = "/api/salaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SalaryRepository salaryRepository;

    @Mock
    private SalaryRepository salaryRepositoryMock;

    @Autowired
    private SalaryMapper salaryMapper;

    @Mock
    private SalaryService salaryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalaryMockMvc;

    private Salary salary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salary createEntity(EntityManager em) {
        Salary salary = new Salary().month(DEFAULT_MONTH).year(DEFAULT_YEAR).amount(DEFAULT_AMOUNT);
        return salary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salary createUpdatedEntity(EntityManager em) {
        Salary salary = new Salary().month(UPDATED_MONTH).year(UPDATED_YEAR).amount(UPDATED_AMOUNT);
        return salary;
    }

    @BeforeEach
    public void initTest() {
        salary = createEntity(em);
    }

    @Test
    @Transactional
    void createSalary() throws Exception {
        int databaseSizeBeforeCreate = salaryRepository.findAll().size();
        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);
        restSalaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salaryDTO)))
            .andExpect(status().isCreated());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeCreate + 1);
        Salary testSalary = salaryList.get(salaryList.size() - 1);
        assertThat(testSalary.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testSalary.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testSalary.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void createSalaryWithExistingId() throws Exception {
        // Create the Salary with an existing ID
        salary.setId(1L);
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        int databaseSizeBeforeCreate = salaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryRepository.findAll().size();
        // set the field null
        salary.setAmount(null);

        // Create the Salary, which fails.
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        restSalaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salaryDTO)))
            .andExpect(status().isBadRequest());

        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSalaries() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList
        restSalaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salary.getId().intValue())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSalariesWithEagerRelationshipsIsEnabled() throws Exception {
        when(salaryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSalaryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(salaryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSalariesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(salaryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSalaryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(salaryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSalary() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get the salary
        restSalaryMockMvc
            .perform(get(ENTITY_API_URL_ID, salary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salary.getId().intValue()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingSalary() throws Exception {
        // Get the salary
        restSalaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSalary() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();

        // Update the salary
        Salary updatedSalary = salaryRepository.findById(salary.getId()).get();
        // Disconnect from session so that the updates on updatedSalary are not directly saved in db
        em.detach(updatedSalary);
        updatedSalary.month(UPDATED_MONTH).year(UPDATED_YEAR).amount(UPDATED_AMOUNT);
        SalaryDTO salaryDTO = salaryMapper.toDto(updatedSalary);

        restSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
        Salary testSalary = salaryList.get(salaryList.size() - 1);
        assertThat(testSalary.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testSalary.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testSalary.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingSalary() throws Exception {
        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();
        salary.setId(count.incrementAndGet());

        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalary() throws Exception {
        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();
        salary.setId(count.incrementAndGet());

        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalary() throws Exception {
        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();
        salary.setId(count.incrementAndGet());

        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salaryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalaryWithPatch() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();

        // Update the salary using partial update
        Salary partialUpdatedSalary = new Salary();
        partialUpdatedSalary.setId(salary.getId());

        partialUpdatedSalary.year(UPDATED_YEAR);

        restSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalary))
            )
            .andExpect(status().isOk());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
        Salary testSalary = salaryList.get(salaryList.size() - 1);
        assertThat(testSalary.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testSalary.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testSalary.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateSalaryWithPatch() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();

        // Update the salary using partial update
        Salary partialUpdatedSalary = new Salary();
        partialUpdatedSalary.setId(salary.getId());

        partialUpdatedSalary.month(UPDATED_MONTH).year(UPDATED_YEAR).amount(UPDATED_AMOUNT);

        restSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalary))
            )
            .andExpect(status().isOk());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
        Salary testSalary = salaryList.get(salaryList.size() - 1);
        assertThat(testSalary.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testSalary.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testSalary.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingSalary() throws Exception {
        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();
        salary.setId(count.incrementAndGet());

        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalary() throws Exception {
        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();
        salary.setId(count.incrementAndGet());

        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalary() throws Exception {
        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();
        salary.setId(count.incrementAndGet());

        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(salaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalary() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        int databaseSizeBeforeDelete = salaryRepository.findAll().size();

        // Delete the salary
        restSalaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, salary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
