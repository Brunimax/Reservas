package com.reservas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MunicipioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Municipio getMunicipioSample1() {
        return new Municipio().id(1L).nome("nome1");
    }

    public static Municipio getMunicipioSample2() {
        return new Municipio().id(2L).nome("nome2");
    }

    public static Municipio getMunicipioRandomSampleGenerator() {
        return new Municipio().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString());
    }
}
