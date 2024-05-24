package com.reservas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TipoQuartoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TipoQuarto getTipoQuartoSample1() {
        return new TipoQuarto().id(1L).nome("nome1");
    }

    public static TipoQuarto getTipoQuartoSample2() {
        return new TipoQuarto().id(2L).nome("nome2");
    }

    public static TipoQuarto getTipoQuartoRandomSampleGenerator() {
        return new TipoQuarto().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString());
    }
}
