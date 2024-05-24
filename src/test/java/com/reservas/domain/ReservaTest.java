package com.reservas.domain;

import static com.reservas.domain.AvaliacaoTestSamples.*;
import static com.reservas.domain.PessoaTestSamples.*;
import static com.reservas.domain.QuartoTestSamples.*;
import static com.reservas.domain.ReservaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reservas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReservaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reserva.class);
        Reserva reserva1 = getReservaSample1();
        Reserva reserva2 = new Reserva();
        assertThat(reserva1).isNotEqualTo(reserva2);

        reserva2.setId(reserva1.getId());
        assertThat(reserva1).isEqualTo(reserva2);

        reserva2 = getReservaSample2();
        assertThat(reserva1).isNotEqualTo(reserva2);
    }

    @Test
    void avaliacaoTest() throws Exception {
        Reserva reserva = getReservaRandomSampleGenerator();
        Avaliacao avaliacaoBack = getAvaliacaoRandomSampleGenerator();

        reserva.setAvaliacao(avaliacaoBack);
        assertThat(reserva.getAvaliacao()).isEqualTo(avaliacaoBack);

        reserva.avaliacao(null);
        assertThat(reserva.getAvaliacao()).isNull();
    }

    @Test
    void quartoTest() throws Exception {
        Reserva reserva = getReservaRandomSampleGenerator();
        Quarto quartoBack = getQuartoRandomSampleGenerator();

        reserva.setQuarto(quartoBack);
        assertThat(reserva.getQuarto()).isEqualTo(quartoBack);

        reserva.quarto(null);
        assertThat(reserva.getQuarto()).isNull();
    }

    @Test
    void pessoaTest() throws Exception {
        Reserva reserva = getReservaRandomSampleGenerator();
        Pessoa pessoaBack = getPessoaRandomSampleGenerator();

        reserva.setPessoa(pessoaBack);
        assertThat(reserva.getPessoa()).isEqualTo(pessoaBack);

        reserva.pessoa(null);
        assertThat(reserva.getPessoa()).isNull();
    }
}
