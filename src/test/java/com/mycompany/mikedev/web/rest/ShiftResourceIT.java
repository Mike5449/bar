package com.mycompany.mikedev.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.mikedev.IntegrationTest;
import com.mycompany.mikedev.domain.Shift;
import com.mycompany.mikedev.domain.enumeration.RequestStatus;
import com.mycompany.mikedev.repository.ShiftRepository;
import com.mycompany.mikedev.service.ShiftService;
import com.mycompany.mikedev.service.dto.ShiftDTO;
import com.mycompany.mikedev.service.mapper.ShiftMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ShiftResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShiftResourceIT {

    private static final Instant DEFAULT_SHIFT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIFT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final RequestStatus DEFAULT_SHIFT_TYPE = RequestStatus.PENDING;
    private static final RequestStatus UPDATED_SHIFT_TYPE = RequestStatus.APPROVED;

    private static final String ENTITY_API_URL = "/api/shifts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShiftRepository shiftRepository;

    @Mock
    private ShiftRepository shiftRepositoryMock;

    @Autowired
    private ShiftMapper shiftMapper;

    @Mock
    private ShiftService shiftServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShiftMockMvc;

    private Shift shift;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shift createEntity(EntityManager em) {
        Shift shift = new Shift().shiftDate(DEFAULT_SHIFT_DATE).shiftType(DEFAULT_SHIFT_TYPE);
        return shift;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shift createUpdatedEntity(EntityManager em) {
        Shift shift = new Shift().shiftDate(UPDATED_SHIFT_DATE).shiftType(UPDATED_SHIFT_TYPE);
        return shift;
    }

    @BeforeEach
    public void initTest() {
        shift = createEntity(em);
    }

    @Test
    @Transactional
    void createShift() throws Exception {
        int databaseSizeBeforeCreate = shiftRepository.findAll().size();
        // Create the Shift
        ShiftDTO shiftDTO = shiftMapper.toDto(shift);
        restShiftMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shiftDTO)))
            .andExpect(status().isCreated());

        // Validate the Shift in the database
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeCreate + 1);
        Shift testShift = shiftList.get(shiftList.size() - 1);
        assertThat(testShift.getShiftDate()).isEqualTo(DEFAULT_SHIFT_DATE);
        assertThat(testShift.getShiftType()).isEqualTo(DEFAULT_SHIFT_TYPE);
    }

    @Test
    @Transactional
    void createShiftWithExistingId() throws Exception {
        // Create the Shift with an existing ID
        shift.setId(1L);
        ShiftDTO shiftDTO = shiftMapper.toDto(shift);

        int databaseSizeBeforeCreate = shiftRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShiftMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shiftDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Shift in the database
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShifts() throws Exception {
        // Initialize the database
        shiftRepository.saveAndFlush(shift);

        // Get all the shiftList
        restShiftMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shift.getId().intValue())))
            .andExpect(jsonPath("$.[*].shiftDate").value(hasItem(DEFAULT_SHIFT_DATE.toString())))
            .andExpect(jsonPath("$.[*].shiftType").value(hasItem(DEFAULT_SHIFT_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShiftsWithEagerRelationshipsIsEnabled() throws Exception {
        when(shiftServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShiftMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(shiftServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShiftsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(shiftServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShiftMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(shiftRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getShift() throws Exception {
        // Initialize the database
        shiftRepository.saveAndFlush(shift);

        // Get the shift
        restShiftMockMvc
            .perform(get(ENTITY_API_URL_ID, shift.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shift.getId().intValue()))
            .andExpect(jsonPath("$.shiftDate").value(DEFAULT_SHIFT_DATE.toString()))
            .andExpect(jsonPath("$.shiftType").value(DEFAULT_SHIFT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingShift() throws Exception {
        // Get the shift
        restShiftMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShift() throws Exception {
        // Initialize the database
        shiftRepository.saveAndFlush(shift);

        int databaseSizeBeforeUpdate = shiftRepository.findAll().size();

        // Update the shift
        Shift updatedShift = shiftRepository.findById(shift.getId()).get();
        // Disconnect from session so that the updates on updatedShift are not directly saved in db
        em.detach(updatedShift);
        updatedShift.shiftDate(UPDATED_SHIFT_DATE).shiftType(UPDATED_SHIFT_TYPE);
        ShiftDTO shiftDTO = shiftMapper.toDto(updatedShift);

        restShiftMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shiftDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shiftDTO))
            )
            .andExpect(status().isOk());

        // Validate the Shift in the database
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeUpdate);
        Shift testShift = shiftList.get(shiftList.size() - 1);
        assertThat(testShift.getShiftDate()).isEqualTo(UPDATED_SHIFT_DATE);
        assertThat(testShift.getShiftType()).isEqualTo(UPDATED_SHIFT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingShift() throws Exception {
        int databaseSizeBeforeUpdate = shiftRepository.findAll().size();
        shift.setId(count.incrementAndGet());

        // Create the Shift
        ShiftDTO shiftDTO = shiftMapper.toDto(shift);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShiftMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shiftDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shiftDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shift in the database
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShift() throws Exception {
        int databaseSizeBeforeUpdate = shiftRepository.findAll().size();
        shift.setId(count.incrementAndGet());

        // Create the Shift
        ShiftDTO shiftDTO = shiftMapper.toDto(shift);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShiftMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shiftDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shift in the database
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShift() throws Exception {
        int databaseSizeBeforeUpdate = shiftRepository.findAll().size();
        shift.setId(count.incrementAndGet());

        // Create the Shift
        ShiftDTO shiftDTO = shiftMapper.toDto(shift);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShiftMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shiftDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shift in the database
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShiftWithPatch() throws Exception {
        // Initialize the database
        shiftRepository.saveAndFlush(shift);

        int databaseSizeBeforeUpdate = shiftRepository.findAll().size();

        // Update the shift using partial update
        Shift partialUpdatedShift = new Shift();
        partialUpdatedShift.setId(shift.getId());

        partialUpdatedShift.shiftDate(UPDATED_SHIFT_DATE).shiftType(UPDATED_SHIFT_TYPE);

        restShiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShift.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShift))
            )
            .andExpect(status().isOk());

        // Validate the Shift in the database
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeUpdate);
        Shift testShift = shiftList.get(shiftList.size() - 1);
        assertThat(testShift.getShiftDate()).isEqualTo(UPDATED_SHIFT_DATE);
        assertThat(testShift.getShiftType()).isEqualTo(UPDATED_SHIFT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateShiftWithPatch() throws Exception {
        // Initialize the database
        shiftRepository.saveAndFlush(shift);

        int databaseSizeBeforeUpdate = shiftRepository.findAll().size();

        // Update the shift using partial update
        Shift partialUpdatedShift = new Shift();
        partialUpdatedShift.setId(shift.getId());

        partialUpdatedShift.shiftDate(UPDATED_SHIFT_DATE).shiftType(UPDATED_SHIFT_TYPE);

        restShiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShift.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShift))
            )
            .andExpect(status().isOk());

        // Validate the Shift in the database
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeUpdate);
        Shift testShift = shiftList.get(shiftList.size() - 1);
        assertThat(testShift.getShiftDate()).isEqualTo(UPDATED_SHIFT_DATE);
        assertThat(testShift.getShiftType()).isEqualTo(UPDATED_SHIFT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingShift() throws Exception {
        int databaseSizeBeforeUpdate = shiftRepository.findAll().size();
        shift.setId(count.incrementAndGet());

        // Create the Shift
        ShiftDTO shiftDTO = shiftMapper.toDto(shift);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shiftDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shiftDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shift in the database
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShift() throws Exception {
        int databaseSizeBeforeUpdate = shiftRepository.findAll().size();
        shift.setId(count.incrementAndGet());

        // Create the Shift
        ShiftDTO shiftDTO = shiftMapper.toDto(shift);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shiftDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shift in the database
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShift() throws Exception {
        int databaseSizeBeforeUpdate = shiftRepository.findAll().size();
        shift.setId(count.incrementAndGet());

        // Create the Shift
        ShiftDTO shiftDTO = shiftMapper.toDto(shift);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShiftMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(shiftDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shift in the database
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShift() throws Exception {
        // Initialize the database
        shiftRepository.saveAndFlush(shift);

        int databaseSizeBeforeDelete = shiftRepository.findAll().size();

        // Delete the shift
        restShiftMockMvc
            .perform(delete(ENTITY_API_URL_ID, shift.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
