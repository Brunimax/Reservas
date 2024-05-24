package com.reservas.repository;

import com.reservas.domain.Hotel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Hotel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {}
