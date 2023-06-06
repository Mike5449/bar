package com.mycompany.mikedev.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.mikedev.IntegrationTest;
import com.mycompany.mikedev.domain.LeaveRequest;
import com.mycompany.mikedev.domain.enumeration.RequestStatus;
import com.mycompany.mikedev.repository.LeaveRequestRepository;
import com.mycompany.mikedev.service.LeaveRequestService;
import com.mycompany.mikedev.service.dto.LeaveRequestDTO;
import com.mycompany.mikedev.service.mapper.LeaveRequestMapper;
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
 * Integration tests for the {@link LeaveRequestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LeaveRequestResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final RequestStatus DEFAULT_REQUEST_STATUS = RequestStatus.PENDING;
    private static final RequestStatus UPDATED_REQUEST_STATUS = RequestStatus.APPROVED;

    private static final String ENTITY_API_URL = "/api/leave-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private LeaveRequestRepository leaveRequestRepositoryMock;

    @Autowired
    private LeaveRequestMapper leaveRequestMapper;

    @Mock
    private LeaveRequestService leaveRequestServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaveRequestMockMvc;

    private LeaveRequest leaveRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveRequest createEntity(EntityManager em) {
        LeaveRequest leaveRequest = new LeaveRequest()
            .startDate(DEFAULT_START_DATE)
            .enDate(DEFAULT_EN_DATE)
            .requestStatus(DEFAULT_REQUEST_STATUS);
        return leaveRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveRequest createUpdatedEntity(EntityManager em) {
        LeaveRequest leaveRequest = new LeaveRequest()
            .startDate(UPDATED_START_DATE)
            .enDate(UPDATED_EN_DATE)
            .requestStatus(UPDATED_REQUEST_STATUS);
        return leaveRequest;
    }

    @BeforeEach
    public void initTest() {
        leaveRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaveRequest() throws Exception {
        int databaseSizeBeforeCreate = leaveRequestRepository.findAll().size();
        // Create the LeaveRequest
        LeaveRequestDTO leaveRequestDTO = leaveRequestMapper.toDto(leaveRequest);
        restLeaveRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveRequestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaveRequest in the database
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeCreate + 1);
        LeaveRequest testLeaveRequest = leaveRequestList.get(leaveRequestList.size() - 1);
        assertThat(testLeaveRequest.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testLeaveRequest.getEnDate()).isEqualTo(DEFAULT_EN_DATE);
        assertThat(testLeaveRequest.getRequestStatus()).isEqualTo(DEFAULT_REQUEST_STATUS);
    }

    @Test
    @Transactional
    void createLeaveRequestWithExistingId() throws Exception {
        // Create the LeaveRequest with an existing ID
        leaveRequest.setId(1L);
        LeaveRequestDTO leaveRequestDTO = leaveRequestMapper.toDto(leaveRequest);

        int databaseSizeBeforeCreate = leaveRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaveRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveRequest in the database
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaveRequestRepository.findAll().size();
        // set the field null
        leaveRequest.setStartDate(null);

        // Create the LeaveRequest, which fails.
        LeaveRequestDTO leaveRequestDTO = leaveRequestMapper.toDto(leaveRequest);

        restLeaveRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaveRequestRepository.findAll().size();
        // set the field null
        leaveRequest.setEnDate(null);

        // Create the LeaveRequest, which fails.
        LeaveRequestDTO leaveRequestDTO = leaveRequestMapper.toDto(leaveRequest);

        restLeaveRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeaveRequests() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList
        restLeaveRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].enDate").value(hasItem(DEFAULT_EN_DATE.toString())))
            .andExpect(jsonPath("$.[*].requestStatus").value(hasItem(DEFAULT_REQUEST_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLeaveRequestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(leaveRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLeaveRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(leaveRequestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLeaveRequestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(leaveRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLeaveRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(leaveRequestRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLeaveRequest() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get the leaveRequest
        restLeaveRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, leaveRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaveRequest.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.enDate").value(DEFAULT_EN_DATE.toString()))
            .andExpect(jsonPath("$.requestStatus").value(DEFAULT_REQUEST_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLeaveRequest() throws Exception {
        // Get the leaveRequest
        restLeaveRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLeaveRequest() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        int databaseSizeBeforeUpdate = leaveRequestRepository.findAll().size();

        // Update the leaveRequest
        LeaveRequest updatedLeaveRequest = leaveRequestRepository.findById(leaveRequest.getId()).get();
        // Disconnect from session so that the updates on updatedLeaveRequest are not directly saved in db
        em.detach(updatedLeaveRequest);
        updatedLeaveRequest.startDate(UPDATED_START_DATE).enDate(UPDATED_EN_DATE).requestStatus(UPDATED_REQUEST_STATUS);
        LeaveRequestDTO leaveRequestDTO = leaveRequestMapper.toDto(updatedLeaveRequest);

        restLeaveRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaveRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaveRequest in the database
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeUpdate);
        LeaveRequest testLeaveRequest = leaveRequestList.get(leaveRequestList.size() - 1);
        assertThat(testLeaveRequest.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLeaveRequest.getEnDate()).isEqualTo(UPDATED_EN_DATE);
        assertThat(testLeaveRequest.getRequestStatus()).isEqualTo(UPDATED_REQUEST_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingLeaveRequest() throws Exception {
        int databaseSizeBeforeUpdate = leaveRequestRepository.findAll().size();
        leaveRequest.setId(count.incrementAndGet());

        // Create the LeaveRequest
        LeaveRequestDTO leaveRequestDTO = leaveRequestMapper.toDto(leaveRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaveRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveRequest in the database
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaveRequest() throws Exception {
        int databaseSizeBeforeUpdate = leaveRequestRepository.findAll().size();
        leaveRequest.setId(count.incrementAndGet());

        // Create the LeaveRequest
        LeaveRequestDTO leaveRequestDTO = leaveRequestMapper.toDto(leaveRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveRequest in the database
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaveRequest() throws Exception {
        int databaseSizeBeforeUpdate = leaveRequestRepository.findAll().size();
        leaveRequest.setId(count.incrementAndGet());

        // Create the LeaveRequest
        LeaveRequestDTO leaveRequestDTO = leaveRequestMapper.toDto(leaveRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveRequestMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaveRequest in the database
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLeaveRequestWithPatch() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        int databaseSizeBeforeUpdate = leaveRequestRepository.findAll().size();

        // Update the leaveRequest using partial update
        LeaveRequest partialUpdatedLeaveRequest = new LeaveRequest();
        partialUpdatedLeaveRequest.setId(leaveRequest.getId());

        restLeaveRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaveRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaveRequest))
            )
            .andExpect(status().isOk());

        // Validate the LeaveRequest in the database
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeUpdate);
        LeaveRequest testLeaveRequest = leaveRequestList.get(leaveRequestList.size() - 1);
        assertThat(testLeaveRequest.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testLeaveRequest.getEnDate()).isEqualTo(DEFAULT_EN_DATE);
        assertThat(testLeaveRequest.getRequestStatus()).isEqualTo(DEFAULT_REQUEST_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateLeaveRequestWithPatch() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        int databaseSizeBeforeUpdate = leaveRequestRepository.findAll().size();

        // Update the leaveRequest using partial update
        LeaveRequest partialUpdatedLeaveRequest = new LeaveRequest();
        partialUpdatedLeaveRequest.setId(leaveRequest.getId());

        partialUpdatedLeaveRequest.startDate(UPDATED_START_DATE).enDate(UPDATED_EN_DATE).requestStatus(UPDATED_REQUEST_STATUS);

        restLeaveRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaveRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaveRequest))
            )
            .andExpect(status().isOk());

        // Validate the LeaveRequest in the database
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeUpdate);
        LeaveRequest testLeaveRequest = leaveRequestList.get(leaveRequestList.size() - 1);
        assertThat(testLeaveRequest.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLeaveRequest.getEnDate()).isEqualTo(UPDATED_EN_DATE);
        assertThat(testLeaveRequest.getRequestStatus()).isEqualTo(UPDATED_REQUEST_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingLeaveRequest() throws Exception {
        int databaseSizeBeforeUpdate = leaveRequestRepository.findAll().size();
        leaveRequest.setId(count.incrementAndGet());

        // Create the LeaveRequest
        LeaveRequestDTO leaveRequestDTO = leaveRequestMapper.toDto(leaveRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaveRequestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveRequest in the database
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaveRequest() throws Exception {
        int databaseSizeBeforeUpdate = leaveRequestRepository.findAll().size();
        leaveRequest.setId(count.incrementAndGet());

        // Create the LeaveRequest
        LeaveRequestDTO leaveRequestDTO = leaveRequestMapper.toDto(leaveRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveRequest in the database
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaveRequest() throws Exception {
        int databaseSizeBeforeUpdate = leaveRequestRepository.findAll().size();
        leaveRequest.setId(count.incrementAndGet());

        // Create the LeaveRequest
        LeaveRequestDTO leaveRequestDTO = leaveRequestMapper.toDto(leaveRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaveRequest in the database
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLeaveRequest() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        int databaseSizeBeforeDelete = leaveRequestRepository.findAll().size();

        // Delete the leaveRequest
        restLeaveRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaveRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
