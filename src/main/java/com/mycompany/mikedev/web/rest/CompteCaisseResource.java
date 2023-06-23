package com.mycompany.mikedev.web.rest;

import com.mycompany.mikedev.domain.CompteCaisse;
import com.mycompany.mikedev.repository.CompteCaisseRepository;
import com.mycompany.mikedev.service.CompteCaisseService;
import com.mycompany.mikedev.service.dto.CompteCaisseDTO;
import com.mycompany.mikedev.service.mapper.CompteCaisseMapper;
import com.mycompany.mikedev.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.mycompany.mikedev.domain.CompteCaisse}.
 */
@RestController
@RequestMapping("/api")
public class CompteCaisseResource {

    private final Logger log = LoggerFactory.getLogger(CompteCaisseResource.class);

    private static final String ENTITY_NAME = "compteCaisse";

    private final CompteCaisseMapper compteCaisseMapper;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompteCaisseService compteCaisseService;

    private final CompteCaisseRepository compteCaisseRepository;

    public CompteCaisseResource(CompteCaisseService compteCaisseService, CompteCaisseRepository compteCaisseRepository ,CompteCaisseMapper compteCaisseMapper) {
        this.compteCaisseService = compteCaisseService;
        this.compteCaisseRepository = compteCaisseRepository;
        this.compteCaisseMapper=compteCaisseMapper;
    }

    /**
     * {@code POST  /compte-caisses} : Create a new compteCaisse.
     *
     * @param compteCaisseDTO the compteCaisseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compteCaisseDTO, or with status {@code 400 (Bad Request)} if the compteCaisse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/compte-caisses")
    public ResponseEntity<CompteCaisseDTO> createCompteCaisse(@RequestBody CompteCaisseDTO compteCaisseDTO) throws URISyntaxException {
        log.debug("REST request to save CompteCaisse : {}", compteCaisseDTO);
        if (compteCaisseDTO.getId() != null) {
            throw new BadRequestAlertException("A new compteCaisse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompteCaisseDTO result = compteCaisseService.save(compteCaisseDTO);
        return ResponseEntity
            .created(new URI("/api/compte-caisses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /compte-caisses/:id} : Updates an existing compteCaisse.
     *
     * @param id the id of the compteCaisseDTO to save.
     * @param compteCaisseDTO the compteCaisseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compteCaisseDTO,
     * or with status {@code 400 (Bad Request)} if the compteCaisseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compteCaisseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/compte-caisses/{id}")
    public ResponseEntity<CompteCaisseDTO> updateCompteCaisse(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompteCaisseDTO compteCaisseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompteCaisse : {}, {}", id, compteCaisseDTO);
        if (compteCaisseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compteCaisseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteCaisseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompteCaisseDTO result = compteCaisseService.update(compteCaisseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compteCaisseDTO.getId().toString()))
            .body(result);
    }

    
    @PutMapping("/compte-caisses/control/{id}/{versement}")
    public ResponseEntity<CompteCaisseDTO> createCaisseAfterControlByCaissier(
        @PathVariable(value = "id", required = false) final Long id,
        @PathVariable(value = "versement", required = false) final Long versement,
        @RequestBody CompteCaisseDTO compteCaisseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompteCaisse : {}, {}", id, compteCaisseDTO);
        if (compteCaisseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compteCaisseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteCaisseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompteCaisseDTO result = compteCaisseService.createCaisseAfterControlByCaissier(compteCaisseDTO,versement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compteCaisseDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/compte-caisses/injection/{id}/{injection}")
    public ResponseEntity<CompteCaisseDTO> updateInjection(
        @PathVariable(value = "id", required = false) final Long id,
        @PathVariable(value = "injection", required = false) final Long injection,
        @RequestBody CompteCaisseDTO compteCaisseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompteCaisse : {}, {}", id, compteCaisseDTO);
        if (compteCaisseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compteCaisseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteCaisseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompteCaisseDTO result = compteCaisseService.updateInjection(compteCaisseDTO,injection);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compteCaisseDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/compte-caisses/pret/{id}/{pret}")
    public ResponseEntity<CompteCaisseDTO> updatePret(
        @PathVariable(value = "id", required = false) final Long id,
        @PathVariable(value = "pret", required = false) final Long pret,
        @RequestBody CompteCaisseDTO compteCaisseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompteCaisse : {}, {}", id, compteCaisseDTO);
        if (compteCaisseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compteCaisseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteCaisseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompteCaisseDTO result = compteCaisseService.updatePret(compteCaisseDTO,pret);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compteCaisseDTO.getId().toString()))
            .body(result);
    }

    

    /**
     * {@code PATCH  /compte-caisses/:id} : Partial updates given fields of an existing compteCaisse, field will ignore if it is null
     *
     * @param id the id of the compteCaisseDTO to save.
     * @param compteCaisseDTO the compteCaisseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compteCaisseDTO,
     * or with status {@code 400 (Bad Request)} if the compteCaisseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the compteCaisseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the compteCaisseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/compte-caisses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompteCaisseDTO> partialUpdateCompteCaisse(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompteCaisseDTO compteCaisseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompteCaisse partially : {}, {}", id, compteCaisseDTO);
        if (compteCaisseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compteCaisseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteCaisseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompteCaisseDTO> result = compteCaisseService.partialUpdate(compteCaisseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compteCaisseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /compte-caisses} : get all the compteCaisses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compteCaisses in body.
     */
    @GetMapping("/compte-caisses")
    public ResponseEntity<List<CompteCaisseDTO>> getAllCompteCaisses(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CompteCaisses");
        Page<CompteCaisseDTO> page = compteCaisseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/compte-caisses/active")
    public ResponseEntity<List<CompteCaisseDTO>> findByCompteCaisseActive(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CompteCaisses");
        List<CompteCaisseDTO> page = compteCaisseService.findByEmployeeAndCompteActive();
        // HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code GET  /compte-caisses/:id} : get the "id" compteCaisse.
     *
     * @param id the id of the compteCaisseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compteCaisseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/compte-caisses/{id}")
    public ResponseEntity<CompteCaisseDTO> getCompteCaisse(@PathVariable Long id) {
        log.debug("REST request to get CompteCaisse : {}", id);
        Optional<CompteCaisseDTO> compteCaisseDTO = compteCaisseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compteCaisseDTO);
    }

    /**
     * {@code DELETE  /compte-caisses/:id} : delete the "id" compteCaisse.
     *
     * @param id the id of the compteCaisseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/compte-caisses/{id}")
    public ResponseEntity<Void> deleteCompteCaisse(@PathVariable Long id) {
        log.debug("REST request to delete CompteCaisse : {}", id);
        compteCaisseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
