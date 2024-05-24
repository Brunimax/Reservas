package com.reservas.repository;

import com.reservas.domain.Quarto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Quarto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuartoRepository extends JpaRepository<Quarto, Long> {}
