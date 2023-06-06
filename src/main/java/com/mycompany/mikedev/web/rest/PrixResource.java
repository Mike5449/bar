package com.mycompany.mikedev.web.rest;

import com.mycompany.mikedev.domain.Prix;
import com.mycompany.mikedev.repository.PrixRepository;
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
 * REST controller for managing {@link com.mycompany.mikedev.domain.Prix}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PrixResource {

    private final Logger log = LoggerFactory.getLogger(PrixResource.class);

    private static final String ENTITY_NAME = "prix";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrixRepository prixRepository;

    public PrixResource(PrixRepository prixRepository) {
        this.prixRepository = prixRepository;
    }

    /**
     * {@code POST  /prixes} : Create a new prix.
     *
     * @param prix the prix to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prix, or with status {@code 400 (Bad Request)} if the prix has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prixes")
    public ResponseEntity<Prix> createPrix(@Valid @RequestBody Prix prix) throws URISyntaxException {
        log.debug("REST request to save Prix : {}", prix);
        if (prix.getId() != null) {
            throw new BadRequestAlertException("A new prix cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prix result = prixRepository.save(prix);
        return ResponseEntity
            .created(new URI("/api/prixes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prixes/:id} : Updates an existing prix.
     *
     * @param id the id of the prix to save.
     * @param prix the prix to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prix,
     * or with status {@code 400 (Bad Request)} if the prix is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prix couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prixes/{id}")
    public ResponseEntity<Prix> updatePrix(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Prix prix)
        throws URISyntaxException {
        log.debug("REST request to update Prix : {}, {}", id, prix);
        if (prix.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prix.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prixRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Prix result = prixRepository.save(prix);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prix.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prixes/:id} : Partial updates given fields of an existing prix, field will ignore if it is null
     *
     * @param id the id of the prix to save.
     * @param prix the prix to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prix,
     * or with status {@code 400 (Bad Request)} if the prix is not valid,
     * or with status {@code 404 (Not Found)} if the prix is not found,
     * or with status {@code 500 (Internal Server Error)} if the prix couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prixes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Prix> partialUpdatePrix(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Prix prix
    ) throws URISyntaxException {
        log.debug("REST request to partial update Prix partially : {}, {}", id, prix);
        if (prix.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prix.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prixRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Prix> result = prixRepository
            .findById(prix.getId())
            .map(existingPrix -> {
                if (prix.getPrix() != null) {
                    existingPrix.setPrix(prix.getPrix());
                }
                if (prix.getDate() != null) {
                    existingPrix.setDate(prix.getDate());
                }

                return existingPrix;
            })
            .map(prixRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prix.getId().toString())
        );
    }

    /**
     * {@code GET  /prixes} : get all the prixes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prixes in body.
     */
    @GetMapping("/prixes")
    public List<Prix> getAllPrixes(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Prixes");
        if (eagerload) {
            return prixRepository.findAllWithEagerRelationships();
        } else {
            return prixRepository.findAll();
        }
    }

    /**
     * {@code GET  /prixes/:id} : get the "id" prix.
     *
     * @param id the id of the prix to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prix, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prixes/{id}")
    public ResponseEntity<Prix> getPrix(@PathVariable Long id) {
        log.debug("REST request to get Prix : {}", id);
        Optional<Prix> prix = prixRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(prix);
    }

    /**
     * {@code DELETE  /prixes/:id} : delete the "id" prix.
     *
     * @param id the id of the prix to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prixes/{id}")
    public ResponseEntity<Void> deletePrix(@PathVariable Long id) {
        log.debug("REST request to delete Prix : {}", id);
        prixRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
