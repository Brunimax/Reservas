package com.reservas.web.rest;

import com.reservas.domain.Quarto;
import com.reservas.repository.QuartoRepository;
import com.reservas.service.QuartoService;
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
 * REST controller for managing {@link com.reservas.domain.Quarto}.
 */
@RestController
@RequestMapping("/api/quartos")
public class QuartoResource {

    private final Logger log = LoggerFactory.getLogger(QuartoResource.class);

    private static final String ENTITY_NAME = "quarto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuartoService quartoService;

    private final QuartoRepository quartoRepository;

    public QuartoResource(QuartoService quartoService, QuartoRepository quartoRepository) {
        this.quartoService = quartoService;
        this.quartoRepository = quartoRepository;
    }

    /**
     * {@code POST  /quartos} : Create a new quarto.
     *
     * @param quarto the quarto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quarto, or with status {@code 400 (Bad Request)} if the quarto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Quarto> createQuarto(@RequestBody Quarto quarto) throws URISyntaxException {
        log.debug("REST request to save Quarto : {}", quarto);
        if (quarto.getId() != null) {
            throw new BadRequestAlertException("A new quarto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quarto = quartoService.save(quarto);
        return ResponseEntity.created(new URI("/api/quartos/" + quarto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, quarto.getId().toString()))
            .body(quarto);
    }

    /**
     * {@code PUT  /quartos/:id} : Updates an existing quarto.
     *
     * @param id the id of the quarto to save.
     * @param quarto the quarto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quarto,
     * or with status {@code 400 (Bad Request)} if the quarto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quarto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Quarto> updateQuarto(@PathVariable(value = "id", required = false) final Long id, @RequestBody Quarto quarto)
        throws URISyntaxException {
        log.debug("REST request to update Quarto : {}, {}", id, quarto);
        if (quarto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quarto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quartoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quarto = quartoService.update(quarto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quarto.getId().toString()))
            .body(quarto);
    }

    /**
     * {@code PATCH  /quartos/:id} : Partial updates given fields of an existing quarto, field will ignore if it is null
     *
     * @param id the id of the quarto to save.
     * @param quarto the quarto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quarto,
     * or with status {@code 400 (Bad Request)} if the quarto is not valid,
     * or with status {@code 404 (Not Found)} if the quarto is not found,
     * or with status {@code 500 (Internal Server Error)} if the quarto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Quarto> partialUpdateQuarto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Quarto quarto
    ) throws URISyntaxException {
        log.debug("REST request to partial update Quarto partially : {}, {}", id, quarto);
        if (quarto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quarto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quartoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Quarto> result = quartoService.partialUpdate(quarto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quarto.getId().toString())
        );
    }

    /**
     * {@code GET  /quartos} : get all the quartos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quartos in body.
     */
    @GetMapping("")
    public List<Quarto> getAllQuartos() {
        log.debug("REST request to get all Quartos");
        return quartoService.findAll();
    }

    /**
     * {@code GET  /quartos/:id} : get the "id" quarto.
     *
     * @param id the id of the quarto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quarto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Quarto> getQuarto(@PathVariable("id") Long id) {
        log.debug("REST request to get Quarto : {}", id);
        Optional<Quarto> quarto = quartoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quarto);
    }

    @GetMapping("/listaPage")
    public ResponseEntity<List<Quarto>> getQuartoPage(Pageable pageable, @RequestParam Map<String, String> params) {
        log.debug("REST request to get estado page with pageable: {}", pageable);
        log.debug("Request parameters: {}", params);

        final Page<Quarto> page = quartoService.listaQuartoPaginada(pageable, params);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code DELETE  /quartos/:id} : delete the "id" quarto.
     *
     * @param id the id of the quarto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuarto(@PathVariable("id") Long id) {
        log.debug("REST request to delete Quarto : {}", id);
        quartoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
