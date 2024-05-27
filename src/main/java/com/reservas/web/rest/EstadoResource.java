package com.reservas.web.rest;

import com.reservas.domain.Estado;
import com.reservas.repository.EstadoRepository;
import com.reservas.service.EstadoService;
import com.reservas.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
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
 * REST controller for managing {@link com.reservas.domain.Estado}.
 */
@RestController
@RequestMapping("/api/estados")
public class EstadoResource {

    private final Logger log = LoggerFactory.getLogger(EstadoResource.class);

    private static final String ENTITY_NAME = "estado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EstadoService estadoService;

    private final EstadoRepository estadoRepository;

    public EstadoResource(EstadoService estadoService, EstadoRepository estadoRepository) {
        this.estadoService = estadoService;
        this.estadoRepository = estadoRepository;
    }

    /**
     * {@code POST  /estados} : Create a new estado.
     *
     * @param estado the estado to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new estado, or with status {@code 400 (Bad Request)} if the estado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Estado> createEstado(@RequestBody Estado estado) throws URISyntaxException {
        log.debug("REST request to save Estado : {}", estado);
        if (estado.getId() != null) {
            throw new BadRequestAlertException("A new estado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        estado = estadoService.save(estado);
        return ResponseEntity.created(new URI("/api/estados/" + estado.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, estado.getId().toString()))
            .body(estado);
    }

    /**
     * {@code PUT  /estados/:id} : Updates an existing estado.
     *
     * @param id the id of the estado to save.
     * @param estado the estado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estado,
     * or with status {@code 400 (Bad Request)} if the estado is not valid,
     * or with status {@code 500 (Internal Server Error)} if the estado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Estado> updateEstado(@PathVariable(value = "id", required = false) final Long id, @RequestBody Estado estado)
        throws URISyntaxException {
        log.debug("REST request to update Estado : {}, {}", id, estado);
        if (estado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        estado = estadoService.update(estado);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, estado.getId().toString()))
            .body(estado);
    }

    /**
     * {@code PATCH  /estados/:id} : Partial updates given fields of an existing estado, field will ignore if it is null
     *
     * @param id the id of the estado to save.
     * @param estado the estado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estado,
     * or with status {@code 400 (Bad Request)} if the estado is not valid,
     * or with status {@code 404 (Not Found)} if the estado is not found,
     * or with status {@code 500 (Internal Server Error)} if the estado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Estado> partialUpdateEstado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Estado estado
    ) throws URISyntaxException {
        log.debug("REST request to partial update Estado partially : {}, {}", id, estado);
        if (estado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Estado> result = estadoService.partialUpdate(estado);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, estado.getId().toString())
        );
    }

    /**
     * {@code GET  /estados} : get all the estados.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of estados in body.
     */
    @GetMapping("")
    public List<Estado> getAllEstados() {
        log.debug("REST request to get all Estados");
        return estadoService.findAll();
    }

    /**
     * {@code GET  /estados/:id} : get the "id" estado.
     *
     * @param id the id of the estado to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the estado, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Estado> getEstado(@PathVariable("id") Long id) {
        log.debug("REST request to get Estado : {}", id);
        Optional<Estado> estado = estadoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(estado);
    }

    @GetMapping("/listaPage")
    public ResponseEntity<List<Estado>> getEstadoPage(Pageable pageable, @RequestParam Map<String, String> params) {
        log.debug("REST request to get estado page with pageable: {}", pageable);
        log.debug("Request parameters: {}", params);

        final Page<Estado> page = estadoService.listaEstadoPaginada(pageable, params);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code DELETE  /estados/:id} : delete the "id" estado.
     *
     * @param id the id of the estado to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstado(@PathVariable("id") Long id) {
        log.debug("REST request to delete Estado : {}", id);
        estadoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
