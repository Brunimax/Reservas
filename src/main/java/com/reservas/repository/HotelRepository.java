package com.reservas.repository;

import com.reservas.domain.Hotel;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Hotel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query(
        value = "SELECT hot.* \n" +
        "FROM public.hotel AS hot \n" +
        "WHERE (CASE WHEN ?1 != '' THEN POSITION(upper(unaccent(?1)) IN upper(unaccent(hot.nome))) > 0 ELSE true END) \n" +
        "AND hot.status = true \n" +
        "ORDER BY hot.classificacao DESC \n" +
        "LIMIT ?2 OFFSET ?3",
        nativeQuery = true
    )
    List<Hotel> pagHotel(String nomeHotel, int limit, int offset);

    @Query(
        value = "SELECT count(*) \n" +
        "FROM public.hotel AS hot \n" +
        "WHERE (CASE WHEN ?1 != '' THEN POSITION(upper(unaccent(?1)) IN upper(unaccent(hot.nome))) > 0 ELSE true END) \n" +
        "AND hot.status = true ",
        nativeQuery = true
    )
    Long countPagHotel(String nomeHotel);
}
