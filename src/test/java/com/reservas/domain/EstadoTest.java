package com.reservas.domain;

import static com.reservas.domain.EstadoTestSamples.*;
import static com.reservas.domain.MunicipioTestSamples.*;
import static com.reservas.domain.PaisTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reservas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EstadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Estado.class);
        Estado estado1 = getEstadoSample1();
        Estado estado2 = new Estado();
        assertThat(estado1).isNotEqualTo(estado2);

        estado2.setId(estado1.getId());
        assertThat(estado1).isEqualTo(estado2);

        estado2 = getEstadoSample2();
        assertThat(estado1).isNotEqualTo(estado2);
    }

    @Test
    void municipioTest() throws Exception {
        Estado estado = getEstadoRandomSampleGenerator();
        Municipio municipioBack = getMunicipioRandomSampleGenerator();

        estado.addMunicipio(municipioBack);
        assertThat(estado.getMunicipios()).containsOnly(municipioBack);
        assertThat(municipioBack.getEstado()).isEqualTo(estado);

        estado.removeMunicipio(municipioBack);
        assertThat(estado.getMunicipios()).doesNotContain(municipioBack);
        assertThat(municipioBack.getEstado()).isNull();

        estado.municipios(new HashSet<>(Set.of(municipioBack)));
        assertThat(estado.getMunicipios()).containsOnly(municipioBack);
        assertThat(municipioBack.getEstado()).isEqualTo(estado);

        estado.setMunicipios(new HashSet<>());
        assertThat(estado.getMunicipios()).doesNotContain(municipioBack);
        assertThat(municipioBack.getEstado()).isNull();
    }

    @Test
    void paisTest() throws Exception {
        Estado estado = getEstadoRandomSampleGenerator();
        Pais paisBack = getPaisRandomSampleGenerator();

        estado.setPais(paisBack);
        assertThat(estado.getPais()).isEqualTo(paisBack);

        estado.pais(null);
        assertThat(estado.getPais()).isNull();
    }
}
