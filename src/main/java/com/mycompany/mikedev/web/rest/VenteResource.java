package com.mycompany.mikedev.web.rest;

import com.mycompany.mikedev.domain.Vente;
import com.mycompany.mikedev.repository.VenteRepository;
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
 * REST controller for managing {@link com.mycompany.mikedev.domain.Vente}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VenteResource {

    private final Logger log = LoggerFactory.getLogger(VenteResource.class);

    private static final String ENTITY_NAME = "vente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VenteRepository venteRepository;

    public VenteResource(VenteRepository venteRepository) {
        this.venteRepository = venteRepository;
    }

    /**
     * {@code POST  /ventes} : Create a new vente.
     *
     * @param vente the vente to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vente, or with status {@code 400 (Bad Request)} if the vente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ventes")
    public ResponseEntity<Vente> createVente(@Valid @RequestBody Vente vente) throws URISyntaxException {
        log.debug("REST request to save Vente : {}", vente);
        if (vente.getId() != null) {
            throw new BadRequestAlertException("A new vente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vente result = venteRepository.save(vente);
        return ResponseEntity
            .created(new URI("/api/ventes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ventes/:id} : Updates an existing vente.
     *
     * @param id the id of the vente to save.
     * @param vente the vente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vente,
     * or with status {@code 400 (Bad Request)} if the vente is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ventes/{id}")
    public ResponseEntity<Vente> updateVente(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Vente vente)
        throws URISyntaxException {
        log.debug("REST request to update Vente : {}, {}", id, vente);
        if (vente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!venteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Vente result = venteRepository.save(vente);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vente.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ventes/:id} : Partial updates given fields of an existing vente, field will ignore if it is null
     *
     * @param id the id of the vente to save.
     * @param vente the vente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vente,
     * or with status {@code 400 (Bad Request)} if the vente is not valid,
     * or with status {@code 404 (Not Found)} if the vente is not found,
     * or with status {@code 500 (Internal Server Error)} if the vente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ventes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vente> partialUpdateVente(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Vente vente
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vente partially : {}, {}", id, vente);
        if (vente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!venteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vente> result = venteRepository
            .findById(vente.getId())
            .map(existingVente -> {
                if (vente.getQuantite() != null) {
                    existingVente.setQuantite(vente.getQuantite());
                }
                if (vente.getStatus() != null) {
                    existingVente.setStatus(vente.getStatus());
                }

                return existingVente;
            })
            .map(venteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vente.getId().toString())
        );
    }

    /**
     * {@code GET  /ventes} : get all the ventes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ventes in body.
     */
    @GetMapping("/ventes")
    public List<Vente> getAllVentes(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Ventes");
        if (eagerload) {
            return venteRepository.findAllWithEagerRelationships();
        } else {
            return venteRepository.findAll();
        }
    }

    /**
     * {@code GET  /ventes/:id} : get the "id" vente.
     *
     * @param id the id of the vente to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vente, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ventes/{id}")
    public ResponseEntity<Vente> getVente(@PathVariable Long id) {
        log.debug("REST request to get Vente : {}", id);
        Optional<Vente> vente = venteRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(vente);
    }

    /**
     * {@code DELETE  /ventes/:id} : delete the "id" vente.
     *
     * @param id the id of the vente to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ventes/{id}")
    public ResponseEntity<Void> deleteVente(@PathVariable Long id) {
        log.debug("REST request to delete Vente : {}", id);
        venteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
