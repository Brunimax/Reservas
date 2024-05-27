package com.reservas.repository;

import com.reservas.domain.Municipio;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Municipio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Long> {
    @Query(
        value = "SELECT mu.* \n" +
        "FROM public.municipio as mu \n" +
        "WHERE (CASE WHEN ?1 != '' THEN POSITION(upper(unaccent(?1)) IN upper(unaccent(mu.nome))) > 0 ELSE true END) \n" +
        "ORDER BY mu.nome ASC \n" +
        "LIMIT 10 OFFSET ?2 * 10",
        nativeQuery = true
    )
    List<Municipio> pagMunicipio(String nomeMunicipio, int page);

    @Query(
        value = "SELECT COUNT(mu.*) \n" +
        "FROM public.municipio as mu \n" +
        "WHERE (CASE WHEN ?1 != '' THEN POSITION(upper(unaccent(?1)) IN upper(unaccent(mu.nome))) > 0 ELSE true END) \n",
        nativeQuery = true
    )
    Long countPagMunicipio(String nomeMunicipio);
}
