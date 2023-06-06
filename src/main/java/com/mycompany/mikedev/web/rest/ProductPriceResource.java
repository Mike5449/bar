package com.mycompany.mikedev.web.rest;

import com.mycompany.mikedev.repository.ProductPriceRepository;
import com.mycompany.mikedev.service.ProductPriceService;
import com.mycompany.mikedev.service.dto.ProductPriceDTO;
import com.mycompany.mikedev.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.mikedev.domain.ProductPrice}.
 */
@RestController
@RequestMapping("/api")
public class ProductPriceResource {

    private final Logger log = LoggerFactory.getLogger(ProductPriceResource.class);

    private static final String ENTITY_NAME = "productPrice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductPriceService productPriceService;

    private final ProductPriceRepository productPriceRepository;

    public ProductPriceResource(ProductPriceService productPriceService, ProductPriceRepository productPriceRepository) {
        this.productPriceService = productPriceService;
        this.productPriceRepository = productPriceRepository;
    }

    /**
     * {@code POST  /product-prices} : Create a new productPrice.
     *
     * @param productPriceDTO the productPriceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productPriceDTO, or with status {@code 400 (Bad Request)} if the productPrice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-prices")
    public ResponseEntity<ProductPriceDTO> createProductPrice(@Valid @RequestBody ProductPriceDTO productPriceDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProductPrice : {}", productPriceDTO);
        if (productPriceDTO.getId() != null) {
            throw new BadRequestAlertException("A new productPrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductPriceDTO result = productPriceService.save(productPriceDTO);
        return ResponseEntity
            .created(new URI("/api/product-prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-prices/:id} : Updates an existing productPrice.
     *
     * @param id the id of the productPriceDTO to save.
     * @param productPriceDTO the productPriceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productPriceDTO,
     * or with status {@code 400 (Bad Request)} if the productPriceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productPriceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-prices/{id}")
    public ResponseEntity<ProductPriceDTO> updateProductPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductPriceDTO productPriceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductPrice : {}, {}", id, productPriceDTO);
        if (productPriceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productPriceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductPriceDTO result = productPriceService.update(productPriceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productPriceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-prices/:id} : Partial updates given fields of an existing productPrice, field will ignore if it is null
     *
     * @param id the id of the productPriceDTO to save.
     * @param productPriceDTO the productPriceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productPriceDTO,
     * or with status {@code 400 (Bad Request)} if the productPriceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productPriceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productPriceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-prices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductPriceDTO> partialUpdateProductPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductPriceDTO productPriceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductPrice partially : {}, {}", id, productPriceDTO);
        if (productPriceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productPriceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductPriceDTO> result = productPriceService.partialUpdate(productPriceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productPriceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-prices} : get all the productPrices.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productPrices in body.
     */
    @GetMapping("/product-prices")
    public ResponseEntity<List<ProductPriceDTO>> getAllProductPrices(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of ProductPrices");
        Page<ProductPriceDTO> page;
        if (eagerload) {
            page = productPriceService.findAllWithEagerRelationships(pageable);
        } else {
            page = productPriceService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-prices/:id} : get the "id" productPrice.
     *
     * @param id the id of the productPriceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productPriceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-prices/{id}")
    public ResponseEntity<ProductPriceDTO> getProductPrice(@PathVariable Long id) {
        log.debug("REST request to get ProductPrice : {}", id);
        Optional<ProductPriceDTO> productPriceDTO = productPriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productPriceDTO);
    }

    /**
     * {@code DELETE  /product-prices/:id} : delete the "id" productPrice.
     *
     * @param id the id of the productPriceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-prices/{id}")
    public ResponseEntity<Void> deleteProductPrice(@PathVariable Long id) {
        log.debug("REST request to delete ProductPrice : {}", id);
        productPriceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
