package com.reservas.domain;

import static com.reservas.domain.HotelTestSamples.*;
import static com.reservas.domain.MunicipioTestSamples.*;
import static com.reservas.domain.PessoaTestSamples.*;
import static com.reservas.domain.QuartoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reservas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HotelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hotel.class);
        Hotel hotel1 = getHotelSample1();
        Hotel hotel2 = new Hotel();
        assertThat(hotel1).isNotEqualTo(hotel2);

        hotel2.setId(hotel1.getId());
        assertThat(hotel1).isEqualTo(hotel2);

        hotel2 = getHotelSample2();
        assertThat(hotel1).isNotEqualTo(hotel2);
    }

    @Test
    void quartoTest() throws Exception {
        Hotel hotel = getHotelRandomSampleGenerator();
        Quarto quartoBack = getQuartoRandomSampleGenerator();

        hotel.addQuarto(quartoBack);
        assertThat(hotel.getQuartos()).containsOnly(quartoBack);
        assertThat(quartoBack.getHotel()).isEqualTo(hotel);

        hotel.removeQuarto(quartoBack);
        assertThat(hotel.getQuartos()).doesNotContain(quartoBack);
        assertThat(quartoBack.getHotel()).isNull();

        hotel.quartos(new HashSet<>(Set.of(quartoBack)));
        assertThat(hotel.getQuartos()).containsOnly(quartoBack);
        assertThat(quartoBack.getHotel()).isEqualTo(hotel);

        hotel.setQuartos(new HashSet<>());
        assertThat(hotel.getQuartos()).doesNotContain(quartoBack);
        assertThat(quartoBack.getHotel()).isNull();
    }

    @Test
    void municipioTest() throws Exception {
        Hotel hotel = getHotelRandomSampleGenerator();
        Municipio municipioBack = getMunicipioRandomSampleGenerator();

        hotel.setMunicipio(municipioBack);
        assertThat(hotel.getMunicipio()).isEqualTo(municipioBack);

        hotel.municipio(null);
        assertThat(hotel.getMunicipio()).isNull();
    }

    @Test
    void pessoaTest() throws Exception {
        Hotel hotel = getHotelRandomSampleGenerator();
        Pessoa pessoaBack = getPessoaRandomSampleGenerator();

        hotel.setPessoa(pessoaBack);
        assertThat(hotel.getPessoa()).isEqualTo(pessoaBack);

        hotel.pessoa(null);
        assertThat(hotel.getPessoa()).isNull();
    }
}
