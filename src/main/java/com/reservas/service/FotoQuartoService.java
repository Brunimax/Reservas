package com.reservas.service;

import com.reservas.domain.FotoQuarto;
import com.reservas.repository.FotoQuartoRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.reservas.domain.FotoQuarto}.
 */
@Service
@Transactional
public class FotoQuartoService {

    private final Logger log = LoggerFactory.getLogger(FotoQuartoService.class);

    private final FotoQuartoRepository fotoQuartoRepository;

    public FotoQuartoService(FotoQuartoRepository fotoQuartoRepository) {
        this.fotoQuartoRepository = fotoQuartoRepository;
    }

    /**
     * Save a fotoQuarto.
     *
     * @param fotoQuarto the entity to save.
     * @return the persisted entity.
     */
    public FotoQuarto save(FotoQuarto fotoQuarto) {
        log.debug("Request to save FotoQuarto : {}", fotoQuarto);
        return fotoQuartoRepository.save(fotoQuarto);
    }

    /**
     * Update a fotoQuarto.
     *
     * @param fotoQuarto the entity to save.
     * @return the persisted entity.
     */
    public FotoQuarto update(FotoQuarto fotoQuarto) {
        log.debug("Request to update FotoQuarto : {}", fotoQuarto);
        return fotoQuartoRepository.save(fotoQuarto);
    }

    /**
     * Partially update a fotoQuarto.
     *
     * @param fotoQuarto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FotoQuarto> partialUpdate(FotoQuarto fotoQuarto) {
        log.debug("Request to partially update FotoQuarto : {}", fotoQuarto);

        return fotoQuartoRepository
            .findById(fotoQuarto.getId())
            .map(existingFotoQuarto -> {
                if (fotoQuarto.getFoto() != null) {
                    existingFotoQuarto.setFoto(fotoQuarto.getFoto());
                }
                if (fotoQuarto.getFotoContentType() != null) {
                    existingFotoQuarto.setFotoContentType(fotoQuarto.getFotoContentType());
                }
                if (fotoQuarto.getStatus() != null) {
                    existingFotoQuarto.setStatus(fotoQuarto.getStatus());
                }

                return existingFotoQuarto;
            })
            .map(fotoQuartoRepository::save);
    }

    /**
     * Get all the fotoQuartos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FotoQuarto> findAll() {
        log.debug("Request to get all FotoQuartos");
        return fotoQuartoRepository.findAll();
    }

    /**
     * Get one fotoQuarto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FotoQuarto> findOne(Long id) {
        log.debug("Request to get FotoQuarto : {}", id);
        return fotoQuartoRepository.findById(id);
    }

    /**
     * Delete the fotoQuarto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FotoQuarto : {}", id);
        fotoQuartoRepository.deleteById(id);
    }
}
