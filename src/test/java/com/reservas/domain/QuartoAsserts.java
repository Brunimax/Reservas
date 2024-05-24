package com.reservas.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class QuartoAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertQuartoAllPropertiesEquals(Quarto expected, Quarto actual) {
        assertQuartoAutoGeneratedPropertiesEquals(expected, actual);
        assertQuartoAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertQuartoAllUpdatablePropertiesEquals(Quarto expected, Quarto actual) {
        assertQuartoUpdatableFieldsEquals(expected, actual);
        assertQuartoUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertQuartoAutoGeneratedPropertiesEquals(Quarto expected, Quarto actual) {
        assertThat(expected)
            .as("Verify Quarto auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertQuartoUpdatableFieldsEquals(Quarto expected, Quarto actual) {
        assertThat(expected)
            .as("Verify Quarto relevant properties")
            .satisfies(e -> assertThat(e.getNome()).as("check nome").isEqualTo(actual.getNome()))
            .satisfies(e -> assertThat(e.getQuantidadeHospedes()).as("check quantidadeHospedes").isEqualTo(actual.getQuantidadeHospedes()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getClassificacao()).as("check classificacao").isEqualTo(actual.getClassificacao()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertQuartoUpdatableRelationshipsEquals(Quarto expected, Quarto actual) {
        assertThat(expected)
            .as("Verify Quarto relationships")
            .satisfies(e -> assertThat(e.getHotel()).as("check hotel").isEqualTo(actual.getHotel()))
            .satisfies(e -> assertThat(e.getTipoQuarto()).as("check tipoQuarto").isEqualTo(actual.getTipoQuarto()));
    }
}