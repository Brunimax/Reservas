package com.reservas.domain;

import static com.reservas.domain.HotelTestSamples.*;
import static com.reservas.domain.PessoaTestSamples.*;
import static com.reservas.domain.ReservaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reservas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PessoaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pessoa.class);
        Pessoa pessoa1 = getPessoaSample1();
        Pessoa pessoa2 = new Pessoa();
        assertThat(pessoa1).isNotEqualTo(pessoa2);

        pessoa2.setId(pessoa1.getId());
        assertThat(pessoa1).isEqualTo(pessoa2);

        pessoa2 = getPessoaSample2();
        assertThat(pessoa1).isNotEqualTo(pessoa2);
    }

    @Test
    void reservaTest() throws Exception {
        Pessoa pessoa = getPessoaRandomSampleGenerator();
        Reserva reservaBack = getReservaRandomSampleGenerator();

        pessoa.addReserva(reservaBack);
        assertThat(pessoa.getReservas()).containsOnly(reservaBack);
        assertThat(reservaBack.getPessoa()).isEqualTo(pessoa);

        pessoa.removeReserva(reservaBack);
        assertThat(pessoa.getReservas()).doesNotContain(reservaBack);
        assertThat(reservaBack.getPessoa()).isNull();

        pessoa.reservas(new HashSet<>(Set.of(reservaBack)));
        assertThat(pessoa.getReservas()).containsOnly(reservaBack);
        assertThat(reservaBack.getPessoa()).isEqualTo(pessoa);

        pessoa.setReservas(new HashSet<>());
        assertThat(pessoa.getReservas()).doesNotContain(reservaBack);
        assertThat(reservaBack.getPessoa()).isNull();
    }

    @Test
    void hotelTest() throws Exception {
        Pessoa pessoa = getPessoaRandomSampleGenerator();
        Hotel hotelBack = getHotelRandomSampleGenerator();

        pessoa.addHotel(hotelBack);
        assertThat(pessoa.getHotels()).containsOnly(hotelBack);
        assertThat(hotelBack.getPessoa()).isEqualTo(pessoa);

        pessoa.removeHotel(hotelBack);
        assertThat(pessoa.getHotels()).doesNotContain(hotelBack);
        assertThat(hotelBack.getPessoa()).isNull();

        pessoa.hotels(new HashSet<>(Set.of(hotelBack)));
        assertThat(pessoa.getHotels()).containsOnly(hotelBack);
        assertThat(hotelBack.getPessoa()).isEqualTo(pessoa);

        pessoa.setHotels(new HashSet<>());
        assertThat(pessoa.getHotels()).doesNotContain(hotelBack);
        assertThat(hotelBack.getPessoa()).isNull();
    }
}
