package com.reservas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HotelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Hotel getHotelSample1() {
        return new Hotel()
            .id(1L)
            .nome("nome1")
            .quantidadeQuartos(1L)
            .vagas(1L)
            .classificacao(1L)
            .cep("cep1")
            .bairro("bairro1")
            .endereco("endereco1");
    }

    public static Hotel getHotelSample2() {
        return new Hotel()
            .id(2L)
            .nome("nome2")
            .quantidadeQuartos(2L)
            .vagas(2L)
            .classificacao(2L)
            .cep("cep2")
            .bairro("bairro2")
            .endereco("endereco2");
    }

    public static Hotel getHotelRandomSampleGenerator() {
        return new Hotel()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .quantidadeQuartos(longCount.incrementAndGet())
            .vagas(longCount.incrementAndGet())
            .classificacao(longCount.incrementAndGet())
            .cep(UUID.randomUUID().toString())
            .bairro(UUID.randomUUID().toString())
            .endereco(UUID.randomUUID().toString());
    }
}
