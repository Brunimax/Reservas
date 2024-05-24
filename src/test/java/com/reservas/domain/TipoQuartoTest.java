package com.reservas.domain;

import static com.reservas.domain.QuartoTestSamples.*;
import static com.reservas.domain.TipoQuartoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reservas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TipoQuartoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoQuarto.class);
        TipoQuarto tipoQuarto1 = getTipoQuartoSample1();
        TipoQuarto tipoQuarto2 = new TipoQuarto();
        assertThat(tipoQuarto1).isNotEqualTo(tipoQuarto2);

        tipoQuarto2.setId(tipoQuarto1.getId());
        assertThat(tipoQuarto1).isEqualTo(tipoQuarto2);

        tipoQuarto2 = getTipoQuartoSample2();
        assertThat(tipoQuarto1).isNotEqualTo(tipoQuarto2);
    }

    @Test
    void quartoTest() throws Exception {
        TipoQuarto tipoQuarto = getTipoQuartoRandomSampleGenerator();
        Quarto quartoBack = getQuartoRandomSampleGenerator();

        tipoQuarto.addQuarto(quartoBack);
        assertThat(tipoQuarto.getQuartos()).containsOnly(quartoBack);
        assertThat(quartoBack.getTipoQuarto()).isEqualTo(tipoQuarto);

        tipoQuarto.removeQuarto(quartoBack);
        assertThat(tipoQuarto.getQuartos()).doesNotContain(quartoBack);
        assertThat(quartoBack.getTipoQuarto()).isNull();

        tipoQuarto.quartos(new HashSet<>(Set.of(quartoBack)));
        assertThat(tipoQuarto.getQuartos()).containsOnly(quartoBack);
        assertThat(quartoBack.getTipoQuarto()).isEqualTo(tipoQuarto);

        tipoQuarto.setQuartos(new HashSet<>());
        assertThat(tipoQuarto.getQuartos()).doesNotContain(quartoBack);
        assertThat(quartoBack.getTipoQuarto()).isNull();
    }
}
