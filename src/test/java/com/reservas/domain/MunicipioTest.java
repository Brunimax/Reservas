package com.reservas.domain;

import static com.reservas.domain.EstadoTestSamples.*;
import static com.reservas.domain.HotelTestSamples.*;
import static com.reservas.domain.MunicipioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reservas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MunicipioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Municipio.class);
        Municipio municipio1 = getMunicipioSample1();
        Municipio municipio2 = new Municipio();
        assertThat(municipio1).isNotEqualTo(municipio2);

        municipio2.setId(municipio1.getId());
        assertThat(municipio1).isEqualTo(municipio2);

        municipio2 = getMunicipioSample2();
        assertThat(municipio1).isNotEqualTo(municipio2);
    }

    @Test
    void hotelTest() throws Exception {
        Municipio municipio = getMunicipioRandomSampleGenerator();
        Hotel hotelBack = getHotelRandomSampleGenerator();

        municipio.addHotel(hotelBack);
        assertThat(municipio.getHotels()).containsOnly(hotelBack);
        assertThat(hotelBack.getMunicipio()).isEqualTo(municipio);

        municipio.removeHotel(hotelBack);
        assertThat(municipio.getHotels()).doesNotContain(hotelBack);
        assertThat(hotelBack.getMunicipio()).isNull();

        municipio.hotels(new HashSet<>(Set.of(hotelBack)));
        assertThat(municipio.getHotels()).containsOnly(hotelBack);
        assertThat(hotelBack.getMunicipio()).isEqualTo(municipio);

        municipio.setHotels(new HashSet<>());
        assertThat(municipio.getHotels()).doesNotContain(hotelBack);
        assertThat(hotelBack.getMunicipio()).isNull();
    }

    @Test
    void estadoTest() throws Exception {
        Municipio municipio = getMunicipioRandomSampleGenerator();
        Estado estadoBack = getEstadoRandomSampleGenerator();

        municipio.setEstado(estadoBack);
        assertThat(municipio.getEstado()).isEqualTo(estadoBack);

        municipio.estado(null);
        assertThat(municipio.getEstado()).isNull();
    }
}
