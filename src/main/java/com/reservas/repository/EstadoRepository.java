package com.reservas.repository;

import com.reservas.domain.Estado;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Estado entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
    @Query(
        value = "SELECT est.* \n" +
        "FROM public.estado AS est \n" +
        "WHERE (CASE WHEN ?1 != '' THEN POSITION(upper(unaccent(?1)) IN upper(unaccent(est.nome))) > 0 ELSE true END) \n" +
        "ORDER BY est.nome ASC \n" +
        "LIMIT 10 OFFSET ?2 * 10",
        nativeQuery = true
    )
    List<Estado> pagEstados(String nomeEstado, int page);

    @Query(
        value = "SELECT COUNT(est.*) \n" +
        "FROM public.estado AS est \n" +
        "WHERE (CASE WHEN ?1 != '' THEN POSITION(upper(unaccent(?1)) IN upper(unaccent(est.nome))) > 0 ELSE true END) \n",
        nativeQuery = true
    )
    Long countPagMunicipio(String nomeEstado);
}
