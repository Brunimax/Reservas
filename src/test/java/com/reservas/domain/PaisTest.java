package com.reservas.domain;

import static com.reservas.domain.EstadoTestSamples.*;
import static com.reservas.domain.PaisTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reservas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PaisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pais.class);
        Pais pais1 = getPaisSample1();
        Pais pais2 = new Pais();
        assertThat(pais1).isNotEqualTo(pais2);

        pais2.setId(pais1.getId());
        assertThat(pais1).isEqualTo(pais2);

        pais2 = getPaisSample2();
        assertThat(pais1).isNotEqualTo(pais2);
    }

    @Test
    void estadoTest() throws Exception {
        Pais pais = getPaisRandomSampleGenerator();
        Estado estadoBack = getEstadoRandomSampleGenerator();

        pais.addEstado(estadoBack);
        assertThat(pais.getEstados()).containsOnly(estadoBack);
        assertThat(estadoBack.getPais()).isEqualTo(pais);

        pais.removeEstado(estadoBack);
        assertThat(pais.getEstados()).doesNotContain(estadoBack);
        assertThat(estadoBack.getPais()).isNull();

        pais.estados(new HashSet<>(Set.of(estadoBack)));
        assertThat(pais.getEstados()).containsOnly(estadoBack);
        assertThat(estadoBack.getPais()).isEqualTo(pais);

        pais.setEstados(new HashSet<>());
        assertThat(pais.getEstados()).doesNotContain(estadoBack);
        assertThat(estadoBack.getPais()).isNull();
    }
}
