package com.mycompany.mikedev.web.rest;

import com.mycompany.mikedev.domain.PrixBoisson;
import com.mycompany.mikedev.repository.PrixBoissonRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.mikedev.domain.PrixBoisson}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PrixBoissonResource {

    private final Logger log = LoggerFactory.getLogger(PrixBoissonResource.class);

    private static final String ENTITY_NAME = "prixBoisson";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrixBoissonRepository prixBoissonRepository;

    public PrixBoissonResource(PrixBoissonRepository prixBoissonRepository) {
        this.prixBoissonRepository = prixBoissonRepository;
    }

    /**
     * {@code POST  /prix-boissons} : Create a new prixBoisson.
     *
     * @param prixBoisson the prixBoisson to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prixBoisson, or with status {@code 400 (Bad Request)} if the prixBoisson has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prix-boissons")
    public ResponseEntity<PrixBoisson> createPrixBoisson(@Valid @RequestBody PrixBoisson prixBoisson) throws URISyntaxException {
        log.debug("REST request to save PrixBoisson : {}", prixBoisson);
        if (prixBoisson.getId() != null) {
            throw new BadRequestAlertException("A new prixBoisson cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrixBoisson result = prixBoissonRepository.save(prixBoisson);
        return ResponseEntity
            .created(new URI("/api/prix-boissons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prix-boissons/:id} : Updates an existing prixBoisson.
     *
     * @param id the id of the prixBoisson to save.
     * @param prixBoisson the prixBoisson to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prixBoisson,
     * or with status {@code 400 (Bad Request)} if the prixBoisson is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prixBoisson couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prix-boissons/{id}")
    public ResponseEntity<PrixBoisson> updatePrixBoisson(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrixBoisson prixBoisson
    ) throws URISyntaxException {
        log.debug("REST request to update PrixBoisson : {}, {}", id, prixBoisson);
        if (prixBoisson.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prixBoisson.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prixBoissonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PrixBoisson result = prixBoissonRepository.save(prixBoisson);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prixBoisson.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prix-boissons/:id} : Partial updates given fields of an existing prixBoisson, field will ignore if it is null
     *
     * @param id the id of the prixBoisson to save.
     * @param prixBoisson the prixBoisson to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prixBoisson,
     * or with status {@code 400 (Bad Request)} if the prixBoisson is not valid,
     * or with status {@code 404 (Not Found)} if the prixBoisson is not found,
     * or with status {@code 500 (Internal Server Error)} if the prixBoisson couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prix-boissons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrixBoisson> partialUpdatePrixBoisson(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrixBoisson prixBoisson
    ) throws URISyntaxException {
        log.debug("REST request to partial update PrixBoisson partially : {}, {}", id, prixBoisson);
        if (prixBoisson.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prixBoisson.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prixBoissonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrixBoisson> result = prixBoissonRepository
            .findById(prixBoisson.getId())
            .map(existingPrixBoisson -> {
                if (prixBoisson.getPrix() != null) {
                    existingPrixBoisson.setPrix(prixBoisson.getPrix());
                }

                return existingPrixBoisson;
            })
            .map(prixBoissonRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prixBoisson.getId().toString())
        );
    }

    /**
     * {@code GET  /prix-boissons} : get all the prixBoissons.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prixBoissons in body.
     */
    @GetMapping("/prix-boissons")
    public List<PrixBoisson> getAllPrixBoissons(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all PrixBoissons");
        if (eagerload) {
            return prixBoissonRepository.findAllWithEagerRelationships();
        } else {
            return prixBoissonRepository.findAll();
        }
    }

    /**
     * {@code GET  /prix-boissons/:id} : get the "id" prixBoisson.
     *
     * @param id the id of the prixBoisson to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prixBoisson, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prix-boissons/{id}")
    public ResponseEntity<PrixBoisson> getPrixBoisson(@PathVariable Long id) {
        log.debug("REST request to get PrixBoisson : {}", id);
        Optional<PrixBoisson> prixBoisson = prixBoissonRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(prixBoisson);
    }

    /**
     * {@code DELETE  /prix-boissons/:id} : delete the "id" prixBoisson.
     *
     * @param id the id of the prixBoisson to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prix-boissons/{id}")
    public ResponseEntity<Void> deletePrixBoisson(@PathVariable Long id) {
        log.debug("REST request to delete PrixBoisson : {}", id);
        prixBoissonRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
