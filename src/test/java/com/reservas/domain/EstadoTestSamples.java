package com.reservas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EstadoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Estado getEstadoSample1() {
        return new Estado().id(1L).nome("nome1").sigla("sigla1");
    }

    public static Estado getEstadoSample2() {
        return new Estado().id(2L).nome("nome2").sigla("sigla2");
    }

    public static Estado getEstadoRandomSampleGenerator() {
        return new Estado().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString()).sigla(UUID.randomUUID().toString());
    }
}
