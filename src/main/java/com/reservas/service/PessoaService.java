package com.reservas.service;

import com.reservas.domain.Pessoa;
import com.reservas.repository.PessoaRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.reservas.domain.Pessoa}.
 */
@Service
@Transactional
public class PessoaService {

    private final Logger log = LoggerFactory.getLogger(PessoaService.class);

    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    /**
     * Save a pessoa.
     *
     * @param pessoa the entity to save.
     * @return the persisted entity.
     */
    public Pessoa save(Pessoa pessoa) {
        log.debug("Request to save Pessoa : {}", pessoa);
        return pessoaRepository.save(pessoa);
    }

    /**
     * Update a pessoa.
     *
     * @param pessoa the entity to save.
     * @return the persisted entity.
     */
    public Pessoa update(Pessoa pessoa) {
        log.debug("Request to update Pessoa : {}", pessoa);
        return pessoaRepository.save(pessoa);
    }

    /**
     * Partially update a pessoa.
     *
     * @param pessoa the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Pessoa> partialUpdate(Pessoa pessoa) {
        log.debug("Request to partially update Pessoa : {}", pessoa);

        return pessoaRepository
            .findById(pessoa.getId())
            .map(existingPessoa -> {
                if (pessoa.getNome() != null) {
                    existingPessoa.setNome(pessoa.getNome());
                }
                if (pessoa.getSobrenome() != null) {
                    existingPessoa.setSobrenome(pessoa.getSobrenome());
                }
                if (pessoa.getCpf() != null) {
                    existingPessoa.setCpf(pessoa.getCpf());
                }
                if (pessoa.getDataNascimento() != null) {
                    existingPessoa.setDataNascimento(pessoa.getDataNascimento());
                }
                if (pessoa.getTelefone() != null) {
                    existingPessoa.setTelefone(pessoa.getTelefone());
                }
                if (pessoa.getEmail() != null) {
                    existingPessoa.setEmail(pessoa.getEmail());
                }

                return existingPessoa;
            })
            .map(pessoaRepository::save);
    }

    /**
     * Get all the pessoas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Pessoa> findAll() {
        log.debug("Request to get all Pessoas");
        return pessoaRepository.findAll();
    }

    /**
     * Get one pessoa by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Pessoa> findOne(Long id) {
        log.debug("Request to get Pessoa : {}", id);
        return pessoaRepository.findById(id);
    }

    /**
     * Delete the pessoa by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pessoa : {}", id);
        pessoaRepository.deleteById(id);
    }
}
