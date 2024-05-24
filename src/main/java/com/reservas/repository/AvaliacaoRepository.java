package com.reservas.repository;

import com.reservas.domain.Avaliacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Avaliacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {}
