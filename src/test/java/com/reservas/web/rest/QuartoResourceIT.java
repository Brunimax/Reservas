package com.reservas.web.rest;

import static com.reservas.domain.QuartoAsserts.*;
import static com.reservas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservas.IntegrationTest;
import com.reservas.domain.Quarto;
import com.reservas.repository.QuartoRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuartoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuartoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Long DEFAULT_QUANTIDADE_HOSPEDES = 1L;
    private static final Long UPDATED_QUANTIDADE_HOSPEDES = 2L;

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final Long DEFAULT_CLASSIFICACAO = 1L;
    private static final Long UPDATED_CLASSIFICACAO = 2L;

    private static final String ENTITY_API_URL = "/api/quartos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuartoRepository quartoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuartoMockMvc;

    private Quarto quarto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quarto createEntity(EntityManager em) {
        Quarto quarto = new Quarto()
            .nome(DEFAULT_NOME)
            .quantidadeHospedes(DEFAULT_QUANTIDADE_HOSPEDES)
            .status(DEFAULT_STATUS)
            .classificacao(DEFAULT_CLASSIFICACAO);
        return quarto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quarto createUpdatedEntity(EntityManager em) {
        Quarto quarto = new Quarto()
            .nome(UPDATED_NOME)
            .quantidadeHospedes(UPDATED_QUANTIDADE_HOSPEDES)
            .status(UPDATED_STATUS)
            .classificacao(UPDATED_CLASSIFICACAO);
        return quarto;
    }

    @BeforeEach
    public void initTest() {
        quarto = createEntity(em);
    }

    @Test
    @Transactional
    void createQuarto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Quarto
        var returnedQuarto = om.readValue(
            restQuartoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quarto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Quarto.class
        );

        // Validate the Quarto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertQuartoUpdatableFieldsEquals(returnedQuarto, getPersistedQuarto(returnedQuarto));
    }

    @Test
    @Transactional
    void createQuartoWithExistingId() throws Exception {
        // Create the Quarto with an existing ID
        quarto.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuartoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quarto)))
            .andExpect(status().isBadRequest());

        // Validate the Quarto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuartos() throws Exception {
        // Initialize the database
        quartoRepository.saveAndFlush(quarto);

        // Get all the quartoList
        restQuartoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quarto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].quantidadeHospedes").value(hasItem(DEFAULT_QUANTIDADE_HOSPEDES.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].classificacao").value(hasItem(DEFAULT_CLASSIFICACAO.intValue())));
    }

    @Test
    @Transactional
    void getQuarto() throws Exception {
        // Initialize the database
        quartoRepository.saveAndFlush(quarto);

        // Get the quarto
        restQuartoMockMvc
            .perform(get(ENTITY_API_URL_ID, quarto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quarto.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.quantidadeHospedes").value(DEFAULT_QUANTIDADE_HOSPEDES.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.classificacao").value(DEFAULT_CLASSIFICACAO.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingQuarto() throws Exception {
        // Get the quarto
        restQuartoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuarto() throws Exception {
        // Initialize the database
        quartoRepository.saveAndFlush(quarto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quarto
        Quarto updatedQuarto = quartoRepository.findById(quarto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuarto are not directly saved in db
        em.detach(updatedQuarto);
        updatedQuarto
            .nome(UPDATED_NOME)
            .quantidadeHospedes(UPDATED_QUANTIDADE_HOSPEDES)
            .status(UPDATED_STATUS)
            .classificacao(UPDATED_CLASSIFICACAO);

        restQuartoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuarto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedQuarto))
            )
            .andExpect(status().isOk());

        // Validate the Quarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuartoToMatchAllProperties(updatedQuarto);
    }

    @Test
    @Transactional
    void putNonExistingQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quarto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuartoMockMvc
            .perform(put(ENTITY_API_URL_ID, quarto.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quarto)))
            .andExpect(status().isBadRequest());

        // Validate the Quarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quarto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuartoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quarto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quarto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuartoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quarto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuartoWithPatch() throws Exception {
        // Initialize the database
        quartoRepository.saveAndFlush(quarto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quarto using partial update
        Quarto partialUpdatedQuarto = new Quarto();
        partialUpdatedQuarto.setId(quarto.getId());

        partialUpdatedQuarto.nome(UPDATED_NOME);

        restQuartoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuarto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuarto))
            )
            .andExpect(status().isOk());

        // Validate the Quarto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuartoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedQuarto, quarto), getPersistedQuarto(quarto));
    }

    @Test
    @Transactional
    void fullUpdateQuartoWithPatch() throws Exception {
        // Initialize the database
        quartoRepository.saveAndFlush(quarto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quarto using partial update
        Quarto partialUpdatedQuarto = new Quarto();
        partialUpdatedQuarto.setId(quarto.getId());

        partialUpdatedQuarto
            .nome(UPDATED_NOME)
            .quantidadeHospedes(UPDATED_QUANTIDADE_HOSPEDES)
            .status(UPDATED_STATUS)
            .classificacao(UPDATED_CLASSIFICACAO);

        restQuartoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuarto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuarto))
            )
            .andExpect(status().isOk());

        // Validate the Quarto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuartoUpdatableFieldsEquals(partialUpdatedQuarto, getPersistedQuarto(partialUpdatedQuarto));
    }

    @Test
    @Transactional
    void patchNonExistingQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quarto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuartoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quarto.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quarto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quarto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuartoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quarto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quarto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuartoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quarto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuarto() throws Exception {
        // Initialize the database
        quartoRepository.saveAndFlush(quarto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quarto
        restQuartoMockMvc
            .perform(delete(ENTITY_API_URL_ID, quarto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quartoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Quarto getPersistedQuarto(Quarto quarto) {
        return quartoRepository.findById(quarto.getId()).orElseThrow();
    }

    protected void assertPersistedQuartoToMatchAllProperties(Quarto expectedQuarto) {
        assertQuartoAllPropertiesEquals(expectedQuarto, getPersistedQuarto(expectedQuarto));
    }

    protected void assertPersistedQuartoToMatchUpdatableProperties(Quarto expectedQuarto) {
        assertQuartoAllUpdatablePropertiesEquals(expectedQuarto, getPersistedQuarto(expectedQuarto));
    }
}
