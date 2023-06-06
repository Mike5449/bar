package com.mycompany.mikedev.service;

import com.mycompany.mikedev.service.dto.SalaryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.mikedev.domain.Salary}.
 */
public interface SalaryService {
    /**
     * Save a salary.
     *
     * @param salaryDTO the entity to save.
     * @return the persisted entity.
     */
    SalaryDTO save(SalaryDTO salaryDTO);

    /**
     * Updates a salary.
     *
     * @param salaryDTO the entity to update.
     * @return the persisted entity.
     */
    SalaryDTO update(SalaryDTO salaryDTO);

    /**
     * Partially updates a salary.
     *
     * @param salaryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SalaryDTO> partialUpdate(SalaryDTO salaryDTO);

    /**
     * Get all the salaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SalaryDTO> findAll(Pageable pageable);

    /**
     * Get all the salaries with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SalaryDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" salary.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SalaryDTO> findOne(Long id);

    /**
     * Delete the "id" salary.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
