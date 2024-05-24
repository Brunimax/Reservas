package com.reservas.repository;

import com.reservas.domain.TipoQuarto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TipoQuarto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoQuartoRepository extends JpaRepository<TipoQuarto, Long> {}
