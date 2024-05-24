package com.reservas.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class FotoQuartoAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFotoQuartoAllPropertiesEquals(FotoQuarto expected, FotoQuarto actual) {
        assertFotoQuartoAutoGeneratedPropertiesEquals(expected, actual);
        assertFotoQuartoAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFotoQuartoAllUpdatablePropertiesEquals(FotoQuarto expected, FotoQuarto actual) {
        assertFotoQuartoUpdatableFieldsEquals(expected, actual);
        assertFotoQuartoUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFotoQuartoAutoGeneratedPropertiesEquals(FotoQuarto expected, FotoQuarto actual) {
        assertThat(expected)
            .as("Verify FotoQuarto auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFotoQuartoUpdatableFieldsEquals(FotoQuarto expected, FotoQuarto actual) {
        assertThat(expected)
            .as("Verify FotoQuarto relevant properties")
            .satisfies(e -> assertThat(e.getFoto()).as("check foto").isEqualTo(actual.getFoto()))
            .satisfies(e -> assertThat(e.getFotoContentType()).as("check foto contenty type").isEqualTo(actual.getFotoContentType()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFotoQuartoUpdatableRelationshipsEquals(FotoQuarto expected, FotoQuarto actual) {
        assertThat(expected)
            .as("Verify FotoQuarto relationships")
            .satisfies(e -> assertThat(e.getQuarto()).as("check quarto").isEqualTo(actual.getQuarto()));
    }
}