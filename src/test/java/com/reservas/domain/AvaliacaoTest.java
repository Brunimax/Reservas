package com.reservas.domain;

import static com.reservas.domain.AvaliacaoTestSamples.*;
import static com.reservas.domain.ReservaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reservas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AvaliacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Avaliacao.class);
        Avaliacao avaliacao1 = getAvaliacaoSample1();
        Avaliacao avaliacao2 = new Avaliacao();
        assertThat(avaliacao1).isNotEqualTo(avaliacao2);

        avaliacao2.setId(avaliacao1.getId());
        assertThat(avaliacao1).isEqualTo(avaliacao2);

        avaliacao2 = getAvaliacaoSample2();
        assertThat(avaliacao1).isNotEqualTo(avaliacao2);
    }

    @Test
    void reservaTest() throws Exception {
        Avaliacao avaliacao = getAvaliacaoRandomSampleGenerator();
        Reserva reservaBack = getReservaRandomSampleGenerator();

        avaliacao.setReserva(reservaBack);
        assertThat(avaliacao.getReserva()).isEqualTo(reservaBack);
        assertThat(reservaBack.getAvaliacao()).isEqualTo(avaliacao);

        avaliacao.reserva(null);
        assertThat(avaliacao.getReserva()).isNull();
        assertThat(reservaBack.getAvaliacao()).isNull();
    }
}
