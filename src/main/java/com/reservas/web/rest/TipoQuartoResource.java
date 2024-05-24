package com.reservas.web.rest;

import com.reservas.domain.TipoQuarto;
import com.reservas.repository.TipoQuartoRepository;
import com.reservas.service.TipoQuartoService;
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
 * REST controller for managing {@link com.reservas.domain.TipoQuarto}.
 */
@RestController
@RequestMapping("/api/tipo-quartos")
public class TipoQuartoResource {

    private final Logger log = LoggerFactory.getLogger(TipoQuartoResource.class);

    private static final String ENTITY_NAME = "tipoQuarto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoQuartoService tipoQuartoService;

    private final TipoQuartoRepository tipoQuartoRepository;

    public TipoQuartoResource(TipoQuartoService tipoQuartoService, TipoQuartoRepository tipoQuartoRepository) {
        this.tipoQuartoService = tipoQuartoService;
        this.tipoQuartoRepository = tipoQuartoRepository;
    }

    /**
     * {@code POST  /tipo-quartos} : Create a new tipoQuarto.
     *
     * @param tipoQuarto the tipoQuarto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoQuarto, or with status {@code 400 (Bad Request)} if the tipoQuarto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TipoQuarto> createTipoQuarto(@RequestBody TipoQuarto tipoQuarto) throws URISyntaxException {
        log.debug("REST request to save TipoQuarto : {}", tipoQuarto);
        if (tipoQuarto.getId() != null) {
            throw new BadRequestAlertException("A new tipoQuarto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tipoQuarto = tipoQuartoService.save(tipoQuarto);
        return ResponseEntity.created(new URI("/api/tipo-quartos/" + tipoQuarto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tipoQuarto.getId().toString()))
            .body(tipoQuarto);
    }

    /**
     * {@code PUT  /tipo-quartos/:id} : Updates an existing tipoQuarto.
     *
     * @param id the id of the tipoQuarto to save.
     * @param tipoQuarto the tipoQuarto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoQuarto,
     * or with status {@code 400 (Bad Request)} if the tipoQuarto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoQuarto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoQuarto> updateTipoQuarto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoQuarto tipoQuarto
    ) throws URISyntaxException {
        log.debug("REST request to update TipoQuarto : {}, {}", id, tipoQuarto);
        if (tipoQuarto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoQuarto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoQuartoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tipoQuarto = tipoQuartoService.update(tipoQuarto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoQuarto.getId().toString()))
            .body(tipoQuarto);
    }

    /**
     * {@code PATCH  /tipo-quartos/:id} : Partial updates given fields of an existing tipoQuarto, field will ignore if it is null
     *
     * @param id the id of the tipoQuarto to save.
     * @param tipoQuarto the tipoQuarto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoQuarto,
     * or with status {@code 400 (Bad Request)} if the tipoQuarto is not valid,
     * or with status {@code 404 (Not Found)} if the tipoQuarto is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoQuarto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoQuarto> partialUpdateTipoQuarto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoQuarto tipoQuarto
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoQuarto partially : {}, {}", id, tipoQuarto);
        if (tipoQuarto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoQuarto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoQuartoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoQuarto> result = tipoQuartoService.partialUpdate(tipoQuarto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoQuarto.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-quartos} : get all the tipoQuartos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoQuartos in body.
     */
    @GetMapping("")
    public List<TipoQuarto> getAllTipoQuartos() {
        log.debug("REST request to get all TipoQuartos");
        return tipoQuartoService.findAll();
    }

    /**
     * {@code GET  /tipo-quartos/:id} : get the "id" tipoQuarto.
     *
     * @param id the id of the tipoQuarto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoQuarto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoQuarto> getTipoQuarto(@PathVariable("id") Long id) {
        log.debug("REST request to get TipoQuarto : {}", id);
        Optional<TipoQuarto> tipoQuarto = tipoQuartoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoQuarto);
    }

    /**
     * {@code DELETE  /tipo-quartos/:id} : delete the "id" tipoQuarto.
     *
     * @param id the id of the tipoQuarto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoQuarto(@PathVariable("id") Long id) {
        log.debug("REST request to delete TipoQuarto : {}", id);
        tipoQuartoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
