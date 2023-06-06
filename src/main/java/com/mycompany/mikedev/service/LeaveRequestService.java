package com.mycompany.mikedev.service;

import com.mycompany.mikedev.service.dto.LeaveRequestDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.mikedev.domain.LeaveRequest}.
 */
public interface LeaveRequestService {
    /**
     * Save a leaveRequest.
     *
     * @param leaveRequestDTO the entity to save.
     * @return the persisted entity.
     */
    LeaveRequestDTO save(LeaveRequestDTO leaveRequestDTO);

    /**
     * Updates a leaveRequest.
     *
     * @param leaveRequestDTO the entity to update.
     * @return the persisted entity.
     */
    LeaveRequestDTO update(LeaveRequestDTO leaveRequestDTO);

    /**
     * Partially updates a leaveRequest.
     *
     * @param leaveRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeaveRequestDTO> partialUpdate(LeaveRequestDTO leaveRequestDTO);

    /**
     * Get all the leaveRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaveRequestDTO> findAll(Pageable pageable);

    /**
     * Get all the leaveRequests with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaveRequestDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" leaveRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaveRequestDTO> findOne(Long id);

    /**
     * Delete the "id" leaveRequest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
