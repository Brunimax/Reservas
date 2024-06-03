package com.reservas.web.rest;

import com.reservas.domain.Pessoa;
import com.reservas.repository.PessoaRepository;
import com.reservas.service.PessoaService;
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
 * REST controller for managing {@link com.reservas.domain.Pessoa}.
 */
@RestController
@RequestMapping("/api/pessoas")
public class PessoaResource {

    private final Logger log = LoggerFactory.getLogger(PessoaResource.class);

    private static final String ENTITY_NAME = "pessoa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PessoaService pessoaService;

    private final PessoaRepository pessoaRepository;

    public PessoaResource(PessoaService pessoaService, PessoaRepository pessoaRepository) {
        this.pessoaService = pessoaService;
        this.pessoaRepository = pessoaRepository;
    }

    /**
     * {@code POST  /pessoas} : Create a new pessoa.
     *
     * @param pessoa the pessoa to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pessoa, or with status {@code 400 (Bad Request)} if the pessoa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Pessoa> createPessoa(@RequestBody Pessoa pessoa) throws URISyntaxException {
        log.debug("REST request to save Pessoa : {}", pessoa);
        if (pessoa.getId() != null) {
            throw new BadRequestAlertException("A new pessoa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pessoa = pessoaService.save(pessoa);
        return ResponseEntity.created(new URI("/api/pessoas/" + pessoa.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pessoa.getId().toString()))
            .body(pessoa);
    }

    /**
     * {@code PUT  /pessoas/:id} : Updates an existing pessoa.
     *
     * @param id the id of the pessoa to save.
     * @param pessoa the pessoa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pessoa,
     * or with status {@code 400 (Bad Request)} if the pessoa is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pessoa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> updatePessoa(@PathVariable(value = "id", required = false) final Long id, @RequestBody Pessoa pessoa)
        throws URISyntaxException {
        log.debug("REST request to update Pessoa : {}, {}", id, pessoa);
        if (pessoa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pessoa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pessoaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pessoa = pessoaService.update(pessoa);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pessoa.getId().toString()))
            .body(pessoa);
    }

    /**
     * {@code PATCH  /pessoas/:id} : Partial updates given fields of an existing pessoa, field will ignore if it is null
     *
     * @param id the id of the pessoa to save.
     * @param pessoa the pessoa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pessoa,
     * or with status {@code 400 (Bad Request)} if the pessoa is not valid,
     * or with status {@code 404 (Not Found)} if the pessoa is not found,
     * or with status {@code 500 (Internal Server Error)} if the pessoa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pessoa> partialUpdatePessoa(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Pessoa pessoa
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pessoa partially : {}, {}", id, pessoa);
        if (pessoa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pessoa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pessoaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pessoa> result = pessoaService.partialUpdate(pessoa);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pessoa.getId().toString())
        );
    }

    /**
     * {@code GET  /pessoas} : get all the pessoas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pessoas in body.
     */
    @GetMapping("")
    public List<Pessoa> getAllPessoas() {
        log.debug("REST request to get all Pessoas");
        return pessoaService.findAll();
    }

    /**
     * {@code GET  /pessoas/:id} : get the "id" pessoa.
     *
     * @param id the id of the pessoa to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pessoa, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> getPessoa(@PathVariable("id") Long id) {
        log.debug("REST request to get Pessoa : {}", id);
        Optional<Pessoa> pessoa = pessoaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pessoa);
    }

    @GetMapping("/listaPage")
    public ResponseEntity<List<Pessoa>> getPessoaPage(Pageable pageable, @RequestParam Map<String, String> params) {
        log.debug("REST request to get pessoa page with pageable: {}", pageable);
        log.debug("Request parameters: {}", params);

        final Page<Pessoa> page = pessoaService.listaPessoaPaginada(pageable, params);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code DELETE  /pessoas/:id} : delete the "id" pessoa.
     *
     * @param id the id of the pessoa to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePessoa(@PathVariable("id") Long id) {
        log.debug("REST request to delete Pessoa : {}", id);
        pessoaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
