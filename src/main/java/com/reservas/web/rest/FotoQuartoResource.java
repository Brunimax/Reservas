package com.reservas.web.rest;

import com.reservas.domain.FotoQuarto;
import com.reservas.repository.FotoQuartoRepository;
import com.reservas.service.FotoQuartoService;
import com.reservas.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.reservas.domain.FotoQuarto}.
 */
@RestController
@RequestMapping("/api/foto-quartos")
public class FotoQuartoResource {

    private final Logger log = LoggerFactory.getLogger(FotoQuartoResource.class);

    private static final String ENTITY_NAME = "fotoQuarto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FotoQuartoService fotoQuartoService;

    private final FotoQuartoRepository fotoQuartoRepository;

    public FotoQuartoResource(FotoQuartoService fotoQuartoService, FotoQuartoRepository fotoQuartoRepository) {
        this.fotoQuartoService = fotoQuartoService;
        this.fotoQuartoRepository = fotoQuartoRepository;
    }

    /**
     * {@code POST  /foto-quartos} : Create a new fotoQuarto.
     *
     * @param fotoQuarto the fotoQuarto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fotoQuarto, or with status {@code 400 (Bad Request)} if the fotoQuarto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FotoQuarto> createFotoQuarto(@RequestBody FotoQuarto fotoQuarto) throws URISyntaxException {
        log.debug("REST request to save FotoQuarto : {}", fotoQuarto);
        if (fotoQuarto.getId() != null) {
            throw new BadRequestAlertException("A new fotoQuarto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fotoQuarto = fotoQuartoService.save(fotoQuarto);
        return ResponseEntity.created(new URI("/api/foto-quartos/" + fotoQuarto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, fotoQuarto.getId().toString()))
            .body(fotoQuarto);
    }

    /**
     * {@code PUT  /foto-quartos/:id} : Updates an existing fotoQuarto.
     *
     * @param id the id of the fotoQuarto to save.
     * @param fotoQuarto the fotoQuarto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fotoQuarto,
     * or with status {@code 400 (Bad Request)} if the fotoQuarto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fotoQuarto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FotoQuarto> updateFotoQuarto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FotoQuarto fotoQuarto
    ) throws URISyntaxException {
        log.debug("REST request to update FotoQuarto : {}, {}", id, fotoQuarto);
        if (fotoQuarto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fotoQuarto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fotoQuartoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fotoQuarto = fotoQuartoService.update(fotoQuarto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fotoQuarto.getId().toString()))
            .body(fotoQuarto);
    }

    /**
     * {@code PATCH  /foto-quartos/:id} : Partial updates given fields of an existing fotoQuarto, field will ignore if it is null
     *
     * @param id the id of the fotoQuarto to save.
     * @param fotoQuarto the fotoQuarto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fotoQuarto,
     * or with status {@code 400 (Bad Request)} if the fotoQuarto is not valid,
     * or with status {@code 404 (Not Found)} if the fotoQuarto is not found,
     * or with status {@code 500 (Internal Server Error)} if the fotoQuarto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FotoQuarto> partialUpdateFotoQuarto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FotoQuarto fotoQuarto
    ) throws URISyntaxException {
        log.debug("REST request to partial update FotoQuarto partially : {}, {}", id, fotoQuarto);
        if (fotoQuarto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fotoQuarto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fotoQuartoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FotoQuarto> result = fotoQuartoService.partialUpdate(fotoQuarto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fotoQuarto.getId().toString())
        );
    }

    /**
     * {@code GET  /foto-quartos} : get all the fotoQuartos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fotoQuartos in body.
     */
    @GetMapping("")
    public List<FotoQuarto> getAllFotoQuartos() {
        log.debug("REST request to get all FotoQuartos");
        return fotoQuartoService.findAll();
    }

    /**
     * {@code GET  /foto-quartos/:id} : get the "id" fotoQuarto.
     *
     * @param id the id of the fotoQuarto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fotoQuarto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FotoQuarto> getFotoQuarto(@PathVariable("id") Long id) {
        log.debug("REST request to get FotoQuarto : {}", id);
        Optional<FotoQuarto> fotoQuarto = fotoQuartoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fotoQuarto);
    }

    /**
     * {@code DELETE  /foto-quartos/:id} : delete the "id" fotoQuarto.
     *
     * @param id the id of the fotoQuarto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFotoQuarto(@PathVariable("id") Long id) {
        log.debug("REST request to delete FotoQuarto : {}", id);
        fotoQuartoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
