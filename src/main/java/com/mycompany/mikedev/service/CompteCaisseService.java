package com.mycompany.mikedev.service;

import com.mycompany.mikedev.domain.CompteCaisse;
import com.mycompany.mikedev.service.dto.CompteCaisseDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.mikedev.domain.CompteCaisse}.
 */
public interface CompteCaisseService {
    /**
     * Save a compteCaisse.
     *
     * @param compteCaisseDTO the entity to save.
     * @return the persisted entity.
     */
    CompteCaisseDTO save(CompteCaisseDTO compteCaisseDTO);

    /**
     * Updates a compteCaisse.
     *
     * @param compteCaisseDTO the entity to update.
     * @return the persisted entity.
     */
    CompteCaisseDTO update(CompteCaisseDTO compteCaisseDTO);
    

    /**
     * Partially updates a compteCaisse.
     *
     * @param compteCaisseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompteCaisseDTO> partialUpdate(CompteCaisseDTO compteCaisseDTO);

    /**
     * Get all the compteCaisses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompteCaisseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" compteCaisse.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompteCaisseDTO> findOne(Long id);

    /**
     * Delete the "id" compteCaisse.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<CompteCaisseDTO> findByEmployeeAndCompteActive();

    CompteCaisseDTO updateInjection(CompteCaisseDTO compteCaisseDTO , Long injection);
    CompteCaisseDTO updatePret(CompteCaisseDTO compteCaisseDTO , Long injection);
    CompteCaisseDTO createCaisseAfterControlByCaissier(CompteCaisseDTO compteCaisseDTO , Long versement);

}
