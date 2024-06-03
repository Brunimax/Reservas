package com.reservas.repository;

import com.reservas.domain.Pessoa;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pessoa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    @Query(
        value = "SELECT pes.*  \n" +
        "FROM public.pessoa AS pes \n" +
        "WHERE (CASE WHEN ?1 != '' THEN POSITION( upper(unaccent(?1)) IN upper(unaccent(pes.nome))) > 0 ELSE true END \n" +
        "OR CASE WHEN ?1 != '' THEN POSITION( upper(unaccent(?1)) IN upper(unaccent(pes.cpf))) > 0 ELSE true END) \n" +
        "ORDER BY pes.nome ASC",
        nativeQuery = true
    )
    List<Pessoa> pagPessoas(String search, int page);

    @Query(
        value = "SELECT COUNT(pes.*)  \n" +
        "FROM public.pessoa AS pes \n" +
        "WHERE (CASE WHEN ?1 != '' THEN POSITION( upper(unaccent(?1)) IN upper(unaccent(pes.nome))) > 0 ELSE true END \n" +
        "OR CASE WHEN ?1 != '' THEN POSITION( upper(unaccent(?1)) IN upper(unaccent(pes.cpf))) > 0 ELSE true END) \n",
        nativeQuery = true
    )
    Long countPagPessoas(String search);
}
