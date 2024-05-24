package com.reservas.service;

import com.reservas.domain.TipoQuarto;
import com.reservas.repository.TipoQuartoRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.reservas.domain.TipoQuarto}.
 */
@Service
@Transactional
public class TipoQuartoService {

    private final Logger log = LoggerFactory.getLogger(TipoQuartoService.class);

    private final TipoQuartoRepository tipoQuartoRepository;

    public TipoQuartoService(TipoQuartoRepository tipoQuartoRepository) {
        this.tipoQuartoRepository = tipoQuartoRepository;
    }

    /**
     * Save a tipoQuarto.
     *
     * @param tipoQuarto the entity to save.
     * @return the persisted entity.
     */
    public TipoQuarto save(TipoQuarto tipoQuarto) {
        log.debug("Request to save TipoQuarto : {}", tipoQuarto);
        return tipoQuartoRepository.save(tipoQuarto);
    }

    /**
     * Update a tipoQuarto.
     *
     * @param tipoQuarto the entity to save.
     * @return the persisted entity.
     */
    public TipoQuarto update(TipoQuarto tipoQuarto) {
        log.debug("Request to update TipoQuarto : {}", tipoQuarto);
        return tipoQuartoRepository.save(tipoQuarto);
    }

    /**
     * Partially update a tipoQuarto.
     *
     * @param tipoQuarto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TipoQuarto> partialUpdate(TipoQuarto tipoQuarto) {
        log.debug("Request to partially update TipoQuarto : {}", tipoQuarto);

        return tipoQuartoRepository
            .findById(tipoQuarto.getId())
            .map(existingTipoQuarto -> {
                if (tipoQuarto.getNome() != null) {
                    existingTipoQuarto.setNome(tipoQuarto.getNome());
                }

                return existingTipoQuarto;
            })
            .map(tipoQuartoRepository::save);
    }

    /**
     * Get all the tipoQuartos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TipoQuarto> findAll() {
        log.debug("Request to get all TipoQuartos");
        return tipoQuartoRepository.findAll();
    }

    /**
     * Get one tipoQuarto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoQuarto> findOne(Long id) {
        log.debug("Request to get TipoQuarto : {}", id);
        return tipoQuartoRepository.findById(id);
    }

    /**
     * Delete the tipoQuarto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TipoQuarto : {}", id);
        tipoQuartoRepository.deleteById(id);
    }
}
