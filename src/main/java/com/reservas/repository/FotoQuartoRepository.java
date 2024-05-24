package com.reservas.repository;

import com.reservas.domain.FotoQuarto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FotoQuarto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FotoQuartoRepository extends JpaRepository<FotoQuarto, Long> {}
