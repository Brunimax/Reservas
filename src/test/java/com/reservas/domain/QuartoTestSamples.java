package com.reservas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QuartoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Quarto getQuartoSample1() {
        return new Quarto().id(1L).nome("nome1").quantidadeHospedes(1L).classificacao(1L);
    }

    public static Quarto getQuartoSample2() {
        return new Quarto().id(2L).nome("nome2").quantidadeHospedes(2L).classificacao(2L);
    }

    public static Quarto getQuartoRandomSampleGenerator() {
        return new Quarto()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .quantidadeHospedes(longCount.incrementAndGet())
            .classificacao(longCount.incrementAndGet());
    }
}
