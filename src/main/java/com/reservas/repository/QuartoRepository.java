package com.reservas.repository;

import com.reservas.domain.Quarto;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Quarto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuartoRepository extends JpaRepository<Quarto, Long> {
    @Query(
        value = "SELECT qua.* \n" +
        "FROM public.quarto AS qua \n" +
        "WHERE (CASE WHEN ?1 != '' THEN POSITION(upper(unaccent(?1)) IN upper(unaccent(qua.nome))) > 0 ELSE true END) \n" +
        "AND qua.status = true \n" +
        "ORDER BY qua.classificacao DESC \n" +
        "LIMIT 10 OFFSET ?2 * 10",
        nativeQuery = true
    )
    List<Quarto> pagQuartos(String nomeQuarto, int page);

    @Query(
        value = "SELECT COUNT(qua.*) \n" +
        "FROM public.quarto AS qua \n" +
        "WHERE (CASE WHEN ?1 != '' THEN POSITION(upper(unaccent(?1)) IN upper(unaccent(qua.nome))) > 0 ELSE true END) \n" +
        "AND qua.status = true",
        nativeQuery = true
    )
    Long countPagQuartos(String nomeQuarto);
}
