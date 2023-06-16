package com.mycompany.mikedev.web.rest;

import com.mycompany.mikedev.repository.DepotRepository;
import com.mycompany.mikedev.service.DepotService;
import com.mycompany.mikedev.service.dto.DepotDTO;
import com.mycompany.mikedev.service.mapper.DepotMapper;
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
 * REST controller for managing {@link com.mycompany.mikedev.domain.Depot}.
 */
@RestController
@RequestMapping("/api")
public class DepotResource {

    private final Logger log = LoggerFactory.getLogger(DepotResource.class);

    private static final String ENTITY_NAME = "depot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepotService depotService;

    private final DepotRepository depotRepository;

    private static DepotMapper depotMapper;

    public DepotResource(DepotService depotService, DepotRepository depotRepository,DepotMapper depotMapper) {
        this.depotService = depotService;
        this.depotRepository = depotRepository;
    }

    /**
     * {@code POST  /depots} : Create a new depot.
     *
     * @param depotDTO the depotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depotDTO, or with status {@code 400 (Bad Request)} if the depot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/depots")
    public ResponseEntity<DepotDTO> createDepot(@Valid @RequestBody DepotDTO depotDTO) throws URISyntaxException {
        log.debug("REST request to save Depot : {}", depotDTO);
        if (depotDTO.getId() != null) {
            throw new BadRequestAlertException("A new depot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepotDTO result = depotService.save(depotDTO);
        return ResponseEntity
            .created(new URI("/api/depots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depots/:id} : Updates an existing depot.
     *
     * @param id the id of the depotDTO to save.
     * @param depotDTO the depotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depotDTO,
     * or with status {@code 400 (Bad Request)} if the depotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/depots/{id}")
    public ResponseEntity<DepotDTO> updateDepot(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DepotDTO depotDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Depot : {}, {}", id, depotDTO);
        if (depotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DepotDTO result = depotService.update(depotDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depotDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /depots/:id} : Partial updates given fields of an existing depot, field will ignore if it is null
     *
     * @param id the id of the depotDTO to save.
     * @param depotDTO the depotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depotDTO,
     * or with status {@code 400 (Bad Request)} if the depotDTO is not valid,
     * or with status {@code 404 (Not Found)} if the depotDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the depotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/depots/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepotDTO> partialUpdateDepot(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DepotDTO depotDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Depot partially : {}, {}", id, depotDTO);
        if (depotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepotDTO> result = depotService.partialUpdate(depotDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depotDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /depots} : get all the depots.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depots in body.
     */
    @GetMapping("/depots")
    public ResponseEntity<List<DepotDTO>> getAllDepots(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Depots");
        Page<DepotDTO> page;
        if (eagerload) {
            page = depotService.findAllWithEagerRelationships(pageable);
        } else {
            page = depotService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /depots/:id} : get the "id" depot.
     *
     * @param id the id of the depotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depots/{id}")
    public ResponseEntity<DepotDTO> getDepot(@PathVariable Long id) {
        log.debug("REST request to get Depot : {}", id);
        Optional<DepotDTO> depotDTO = depotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depotDTO);
    }
    @GetMapping("/depots/byClient/{id}")
    public ResponseEntity<List<DepotDTO>> getDepotsByClientId(@PathVariable Long id) {
        log.debug("REST request to get Depot : {}", id);
        List<DepotDTO>depotDTO = depotService.getDepotsByClientId(id);
        
        return ResponseEntity.ok().body(depotDTO);
    }

    @GetMapping("/depots/sum/{id}")
    public Double getTotalDepotsClientByClientId(@PathVariable Long clientId){
        return depotService.getTotalDepotsClientByClientId(clientId);
    }

    /**
     * {@code DELETE  /depots/:id} : delete the "id" depot.
     *
     * @param id the id of the depotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/depots/{id}")
    public ResponseEntity<Void> deleteDepot(@PathVariable Long id) {
        log.debug("REST request to delete Depot : {}", id);
        depotService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }


}

