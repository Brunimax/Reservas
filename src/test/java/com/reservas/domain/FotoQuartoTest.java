package com.reservas.domain;

import static com.reservas.domain.FotoQuartoTestSamples.*;
import static com.reservas.domain.QuartoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.reservas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FotoQuartoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FotoQuarto.class);
        FotoQuarto fotoQuarto1 = getFotoQuartoSample1();
        FotoQuarto fotoQuarto2 = new FotoQuarto();
        assertThat(fotoQuarto1).isNotEqualTo(fotoQuarto2);

        fotoQuarto2.setId(fotoQuarto1.getId());
        assertThat(fotoQuarto1).isEqualTo(fotoQuarto2);

        fotoQuarto2 = getFotoQuartoSample2();
        assertThat(fotoQuarto1).isNotEqualTo(fotoQuarto2);
    }

    @Test
    void quartoTest() throws Exception {
        FotoQuarto fotoQuarto = getFotoQuartoRandomSampleGenerator();
        Quarto quartoBack = getQuartoRandomSampleGenerator();

        fotoQuarto.setQuarto(quartoBack);
        assertThat(fotoQuarto.getQuarto()).isEqualTo(quartoBack);

        fotoQuarto.quarto(null);
        assertThat(fotoQuarto.getQuarto()).isNull();
    }
}
