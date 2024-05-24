package com.reservas.service;

import com.reservas.domain.Municipio;
import com.reservas.repository.MunicipioRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.reservas.domain.Municipio}.
 */
@Service
@Transactional
public class MunicipioService {

    private final Logger log = LoggerFactory.getLogger(MunicipioService.class);

    private final MunicipioRepository municipioRepository;

    public MunicipioService(MunicipioRepository municipioRepository) {
        this.municipioRepository = municipioRepository;
    }

    /**
     * Save a municipio.
     *
     * @param municipio the entity to save.
     * @return the persisted entity.
     */
    public Municipio save(Municipio municipio) {
        log.debug("Request to save Municipio : {}", municipio);
        return municipioRepository.save(municipio);
    }

    /**
     * Update a municipio.
     *
     * @param municipio the entity to save.
     * @return the persisted entity.
     */
    public Municipio update(Municipio municipio) {
        log.debug("Request to update Municipio : {}", municipio);
        return municipioRepository.save(municipio);
    }

    /**
     * Partially update a municipio.
     *
     * @param municipio the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Municipio> partialUpdate(Municipio municipio) {
        log.debug("Request to partially update Municipio : {}", municipio);

        return municipioRepository
            .findById(municipio.getId())
            .map(existingMunicipio -> {
                if (municipio.getNome() != null) {
                    existingMunicipio.setNome(municipio.getNome());
                }

                return existingMunicipio;
            })
            .map(municipioRepository::save);
    }

    /**
     * Get all the municipios.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Municipio> findAll() {
        log.debug("Request to get all Municipios");
        return municipioRepository.findAll();
    }

    /**
     * Get one municipio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Municipio> findOne(Long id) {
        log.debug("Request to get Municipio : {}", id);
        return municipioRepository.findById(id);
    }

    /**
     * Delete the municipio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Municipio : {}", id);
        municipioRepository.deleteById(id);
    }

    public Page<Municipio> listaParaPage(Pageable pageable, List<Municipio> lista, Long totalItens) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        Page<Municipio> page = new PageImpl<Municipio>(lista, PageRequest.of(currentPage, pageSize), totalItens);
        return page;
    }

    public Page<Municipio> listaMunicipioPaginado(Pageable pageable, Map<String, String> params) {
        Long countTotalMunicipios = municipioRepository.countPagMunicipio(params.get("search"));
        List<Municipio> listaMunicipio = municipioRepository.pagMunicipio(params.get("search"), Integer.parseInt(params.get("page")));
        Page<Municipio> municipioPage = listaParaPage(pageable, listaMunicipio, countTotalMunicipios);
        return municipioPage;
    }
}
