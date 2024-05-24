package com.reservas.service;

import com.reservas.domain.Quarto;
import com.reservas.repository.QuartoRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.reservas.domain.Quarto}.
 */
@Service
@Transactional
public class QuartoService {

    private final Logger log = LoggerFactory.getLogger(QuartoService.class);

    private final QuartoRepository quartoRepository;

    public QuartoService(QuartoRepository quartoRepository) {
        this.quartoRepository = quartoRepository;
    }

    /**
     * Save a quarto.
     *
     * @param quarto the entity to save.
     * @return the persisted entity.
     */
    public Quarto save(Quarto quarto) {
        log.debug("Request to save Quarto : {}", quarto);
        return quartoRepository.save(quarto);
    }

    /**
     * Update a quarto.
     *
     * @param quarto the entity to save.
     * @return the persisted entity.
     */
    public Quarto update(Quarto quarto) {
        log.debug("Request to update Quarto : {}", quarto);
        return quartoRepository.save(quarto);
    }

    /**
     * Partially update a quarto.
     *
     * @param quarto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Quarto> partialUpdate(Quarto quarto) {
        log.debug("Request to partially update Quarto : {}", quarto);

        return quartoRepository
            .findById(quarto.getId())
            .map(existingQuarto -> {
                if (quarto.getNome() != null) {
                    existingQuarto.setNome(quarto.getNome());
                }
                if (quarto.getQuantidadeHospedes() != null) {
                    existingQuarto.setQuantidadeHospedes(quarto.getQuantidadeHospedes());
                }
                if (quarto.getStatus() != null) {
                    existingQuarto.setStatus(quarto.getStatus());
                }
                if (quarto.getClassificacao() != null) {
                    existingQuarto.setClassificacao(quarto.getClassificacao());
                }

                return existingQuarto;
            })
            .map(quartoRepository::save);
    }

    /**
     * Get all the quartos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Quarto> findAll() {
        log.debug("Request to get all Quartos");
        return quartoRepository.findAll();
    }

    /**
     * Get one quarto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Quarto> findOne(Long id) {
        log.debug("Request to get Quarto : {}", id);
        return quartoRepository.findById(id);
    }

    /**
     * Delete the quarto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Quarto : {}", id);
        quartoRepository.deleteById(id);
    }
}
