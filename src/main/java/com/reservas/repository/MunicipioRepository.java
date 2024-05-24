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
        value = "SELECT mun.* \n" +
        "FROM public.municipio \n" +
        "WHERE (?1 = '' OR upper(unaccent(nome)) LIKE upper(unaccent('%' || ?1 || '%'))) \n" +
        "ORDER BY nome ASC \n" +
        "LIMIT 10 OFFSET ?2 * 10",
        nativeQuery = true
    )
    List<Municipio> pagMunicipio(String nomeMunicipio, int page);

    @Query(
        value = "SELECT COUNT(mun.*) \n" +
        "FROM public.municipio \n" +
        "WHERE (?1 = '' OR upper(unaccent(nome)) LIKE upper(unaccent('%' || ?1 || '%'))) \n",
        nativeQuery = true
    )
    Long countPagMunicipio(String nomeMunicipio);
}
