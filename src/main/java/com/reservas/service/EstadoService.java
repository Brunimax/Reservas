package com.reservas.service;

import com.reservas.domain.Estado;
import com.reservas.repository.EstadoRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.reservas.domain.Estado}.
 */
@Service
@Transactional
public class EstadoService {

    private final Logger log = LoggerFactory.getLogger(EstadoService.class);

    private final EstadoRepository estadoRepository;

    public EstadoService(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    /**
     * Save a estado.
     *
     * @param estado the entity to save.
     * @return the persisted entity.
     */
    public Estado save(Estado estado) {
        log.debug("Request to save Estado : {}", estado);
        return estadoRepository.save(estado);
    }

    /**
     * Update a estado.
     *
     * @param estado the entity to save.
     * @return the persisted entity.
     */
    public Estado update(Estado estado) {
        log.debug("Request to update Estado : {}", estado);
        return estadoRepository.save(estado);
    }

    /**
     * Partially update a estado.
     *
     * @param estado the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Estado> partialUpdate(Estado estado) {
        log.debug("Request to partially update Estado : {}", estado);

        return estadoRepository
            .findById(estado.getId())
            .map(existingEstado -> {
                if (estado.getNome() != null) {
                    existingEstado.setNome(estado.getNome());
                }
                if (estado.getSigla() != null) {
                    existingEstado.setSigla(estado.getSigla());
                }

                return existingEstado;
            })
            .map(estadoRepository::save);
    }

    /**
     * Get all the estados.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Estado> findAll() {
        log.debug("Request to get all Estados");
        return estadoRepository.findAll();
    }

    /**
     * Get one estado by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Estado> findOne(Long id) {
        log.debug("Request to get Estado : {}", id);
        return estadoRepository.findById(id);
    }

    /**
     * Delete the estado by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Estado : {}", id);
        estadoRepository.deleteById(id);
    }
}
