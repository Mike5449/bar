package com.mycompany.mikedev.service;

import com.mycompany.mikedev.service.dto.ProductPriceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.mikedev.domain.ProductPrice}.
 */
public interface ProductPriceService {
    /**
     * Save a productPrice.
     *
     * @param productPriceDTO the entity to save.
     * @return the persisted entity.
     */
    ProductPriceDTO save(ProductPriceDTO productPriceDTO);

    /**
     * Updates a productPrice.
     *
     * @param productPriceDTO the entity to update.
     * @return the persisted entity.
     */
    ProductPriceDTO update(ProductPriceDTO productPriceDTO);

    /**
     * Partially updates a productPrice.
     *
     * @param productPriceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductPriceDTO> partialUpdate(ProductPriceDTO productPriceDTO);

    /**
     * Get all the productPrices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductPriceDTO> findAll(Pageable pageable);

    /**
     * Get all the productPrices with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductPriceDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" productPrice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductPriceDTO> findOne(Long id);

    /**
     * Delete the "id" productPrice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
