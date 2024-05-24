package com.reservas.domain;

import static com.reservas.domain.FotoQuartoTestSamples.*;
import static com.reservas.domain.HotelTestSamples.*;
import static com.reservas.domain.QuartoTestSamples.*;
import static com.reservas.domain.ReservaTestSamples.*;
import static com.reservas.domain.TipoQuartoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reservas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class QuartoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quarto.class);
        Quarto quarto1 = getQuartoSample1();
        Quarto quarto2 = new Quarto();
        assertThat(quarto1).isNotEqualTo(quarto2);

        quarto2.setId(quarto1.getId());
        assertThat(quarto1).isEqualTo(quarto2);

        quarto2 = getQuartoSample2();
        assertThat(quarto1).isNotEqualTo(quarto2);
    }

    @Test
    void fotoQuartoTest() throws Exception {
        Quarto quarto = getQuartoRandomSampleGenerator();
        FotoQuarto fotoQuartoBack = getFotoQuartoRandomSampleGenerator();

        quarto.addFotoQuarto(fotoQuartoBack);
        assertThat(quarto.getFotoQuartos()).containsOnly(fotoQuartoBack);
        assertThat(fotoQuartoBack.getQuarto()).isEqualTo(quarto);

        quarto.removeFotoQuarto(fotoQuartoBack);
        assertThat(quarto.getFotoQuartos()).doesNotContain(fotoQuartoBack);
        assertThat(fotoQuartoBack.getQuarto()).isNull();

        quarto.fotoQuartos(new HashSet<>(Set.of(fotoQuartoBack)));
        assertThat(quarto.getFotoQuartos()).containsOnly(fotoQuartoBack);
        assertThat(fotoQuartoBack.getQuarto()).isEqualTo(quarto);

        quarto.setFotoQuartos(new HashSet<>());
        assertThat(quarto.getFotoQuartos()).doesNotContain(fotoQuartoBack);
        assertThat(fotoQuartoBack.getQuarto()).isNull();
    }

    @Test
    void reservaTest() throws Exception {
        Quarto quarto = getQuartoRandomSampleGenerator();
        Reserva reservaBack = getReservaRandomSampleGenerator();

        quarto.addReserva(reservaBack);
        assertThat(quarto.getReservas()).containsOnly(reservaBack);
        assertThat(reservaBack.getQuarto()).isEqualTo(quarto);

        quarto.removeReserva(reservaBack);
        assertThat(quarto.getReservas()).doesNotContain(reservaBack);
        assertThat(reservaBack.getQuarto()).isNull();

        quarto.reservas(new HashSet<>(Set.of(reservaBack)));
        assertThat(quarto.getReservas()).containsOnly(reservaBack);
        assertThat(reservaBack.getQuarto()).isEqualTo(quarto);

        quarto.setReservas(new HashSet<>());
        assertThat(quarto.getReservas()).doesNotContain(reservaBack);
        assertThat(reservaBack.getQuarto()).isNull();
    }

    @Test
    void hotelTest() throws Exception {
        Quarto quarto = getQuartoRandomSampleGenerator();
        Hotel hotelBack = getHotelRandomSampleGenerator();

        quarto.setHotel(hotelBack);
        assertThat(quarto.getHotel()).isEqualTo(hotelBack);

        quarto.hotel(null);
        assertThat(quarto.getHotel()).isNull();
    }

    @Test
    void tipoQuartoTest() throws Exception {
        Quarto quarto = getQuartoRandomSampleGenerator();
        TipoQuarto tipoQuartoBack = getTipoQuartoRandomSampleGenerator();

        quarto.setTipoQuarto(tipoQuartoBack);
        assertThat(quarto.getTipoQuarto()).isEqualTo(tipoQuartoBack);

        quarto.tipoQuarto(null);
        assertThat(quarto.getTipoQuarto()).isNull();
    }
}
