package com.reservas.web.rest;

import com.reservas.domain.Avaliacao;
import com.reservas.repository.AvaliacaoRepository;
import com.reservas.service.AvaliacaoService;
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
 * REST controller for managing {@link com.reservas.domain.Avaliacao}.
 */
@RestController
@RequestMapping("/api/avaliacaos")
public class AvaliacaoResource {

    private final Logger log = LoggerFactory.getLogger(AvaliacaoResource.class);

    private static final String ENTITY_NAME = "avaliacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AvaliacaoService avaliacaoService;

    private final AvaliacaoRepository avaliacaoRepository;

    public AvaliacaoResource(AvaliacaoService avaliacaoService, AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoService = avaliacaoService;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    /**
     * {@code POST  /avaliacaos} : Create a new avaliacao.
     *
     * @param avaliacao the avaliacao to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new avaliacao, or with status {@code 400 (Bad Request)} if the avaliacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Avaliacao> createAvaliacao(@RequestBody Avaliacao avaliacao) throws URISyntaxException {
        log.debug("REST request to save Avaliacao : {}", avaliacao);
        if (avaliacao.getId() != null) {
            throw new BadRequestAlertException("A new avaliacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        avaliacao = avaliacaoService.save(avaliacao);
        return ResponseEntity.created(new URI("/api/avaliacaos/" + avaliacao.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, avaliacao.getId().toString()))
            .body(avaliacao);
    }

    /**
     * {@code PUT  /avaliacaos/:id} : Updates an existing avaliacao.
     *
     * @param id the id of the avaliacao to save.
     * @param avaliacao the avaliacao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avaliacao,
     * or with status {@code 400 (Bad Request)} if the avaliacao is not valid,
     * or with status {@code 500 (Internal Server Error)} if the avaliacao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Avaliacao> updateAvaliacao(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Avaliacao avaliacao
    ) throws URISyntaxException {
        log.debug("REST request to update Avaliacao : {}, {}", id, avaliacao);
        if (avaliacao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, avaliacao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!avaliacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        avaliacao = avaliacaoService.update(avaliacao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, avaliacao.getId().toString()))
            .body(avaliacao);
    }

    /**
     * {@code PATCH  /avaliacaos/:id} : Partial updates given fields of an existing avaliacao, field will ignore if it is null
     *
     * @param id the id of the avaliacao to save.
     * @param avaliacao the avaliacao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avaliacao,
     * or with status {@code 400 (Bad Request)} if the avaliacao is not valid,
     * or with status {@code 404 (Not Found)} if the avaliacao is not found,
     * or with status {@code 500 (Internal Server Error)} if the avaliacao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Avaliacao> partialUpdateAvaliacao(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Avaliacao avaliacao
    ) throws URISyntaxException {
        log.debug("REST request to partial update Avaliacao partially : {}, {}", id, avaliacao);
        if (avaliacao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, avaliacao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!avaliacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Avaliacao> result = avaliacaoService.partialUpdate(avaliacao);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, avaliacao.getId().toString())
        );
    }

    /**
     * {@code GET  /avaliacaos} : get all the avaliacaos.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of avaliacaos in body.
     */
    @GetMapping("")
    public List<Avaliacao> getAllAvaliacaos(@RequestParam(name = "filter", required = false) String filter) {
        if ("reserva-is-null".equals(filter)) {
            log.debug("REST request to get all Avaliacaos where reserva is null");
            return avaliacaoService.findAllWhereReservaIsNull();
        }
        log.debug("REST request to get all Avaliacaos");
        return avaliacaoService.findAll();
    }

    /**
     * {@code GET  /avaliacaos/:id} : get the "id" avaliacao.
     *
     * @param id the id of the avaliacao to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the avaliacao, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> getAvaliacao(@PathVariable("id") Long id) {
        log.debug("REST request to get Avaliacao : {}", id);
        Optional<Avaliacao> avaliacao = avaliacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(avaliacao);
    }

    /**
     * {@code DELETE  /avaliacaos/:id} : delete the "id" avaliacao.
     *
     * @param id the id of the avaliacao to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvaliacao(@PathVariable("id") Long id) {
        log.debug("REST request to delete Avaliacao : {}", id);
        avaliacaoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
