package com.reservas.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class FotoQuartoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FotoQuarto getFotoQuartoSample1() {
        return new FotoQuarto().id(1L);
    }

    public static FotoQuarto getFotoQuartoSample2() {
        return new FotoQuarto().id(2L);
    }

    public static FotoQuarto getFotoQuartoRandomSampleGenerator() {
        return new FotoQuarto().id(longCount.incrementAndGet());
    }
}
