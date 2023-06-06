package com.mycompany.mikedev.service;

import com.mycompany.mikedev.service.dto.DepotDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.mikedev.domain.Depot}.
 */
public interface DepotService {
    /**
     * Save a depot.
     *
     * @param depotDTO the entity to save.
     * @return the persisted entity.
     */
    DepotDTO save(DepotDTO depotDTO);

    /**
     * Updates a depot.
     *
     * @param depotDTO the entity to update.
     * @return the persisted entity.
     */
    DepotDTO update(DepotDTO depotDTO);

    /**
     * Partially updates a depot.
     *
     * @param depotDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DepotDTO> partialUpdate(DepotDTO depotDTO);

    /**
     * Get all the depots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepotDTO> findAll(Pageable pageable);

    /**
     * Get all the depots with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepotDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" depot.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DepotDTO> findOne(Long id);

    /**
     * Delete the "id" depot.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
