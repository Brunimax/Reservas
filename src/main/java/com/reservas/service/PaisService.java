package com.reservas.service;

import com.reservas.domain.Pais;
import com.reservas.repository.PaisRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.reservas.domain.Pais}.
 */
@Service
@Transactional
public class PaisService {

    private final Logger log = LoggerFactory.getLogger(PaisService.class);

    private final PaisRepository paisRepository;

    public PaisService(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    /**
     * Save a pais.
     *
     * @param pais the entity to save.
     * @return the persisted entity.
     */
    public Pais save(Pais pais) {
        log.debug("Request to save Pais : {}", pais);
        return paisRepository.save(pais);
    }

    /**
     * Update a pais.
     *
     * @param pais the entity to save.
     * @return the persisted entity.
     */
    public Pais update(Pais pais) {
        log.debug("Request to update Pais : {}", pais);
        return paisRepository.save(pais);
    }

    /**
     * Partially update a pais.
     *
     * @param pais the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Pais> partialUpdate(Pais pais) {
        log.debug("Request to partially update Pais : {}", pais);

        return paisRepository
            .findById(pais.getId())
            .map(existingPais -> {
                if (pais.getNome() != null) {
                    existingPais.setNome(pais.getNome());
                }
                if (pais.getSigla() != null) {
                    existingPais.setSigla(pais.getSigla());
                }

                return existingPais;
            })
            .map(paisRepository::save);
    }

    /**
     * Get all the pais.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Pais> findAll() {
        log.debug("Request to get all Pais");
        return paisRepository.findAll();
    }

    /**
     * Get one pais by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Pais> findOne(Long id) {
        log.debug("Request to get Pais : {}", id);
        return paisRepository.findById(id);
    }

    /**
     * Delete the pais by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pais : {}", id);
        paisRepository.deleteById(id);
    }
}
