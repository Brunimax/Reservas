package com.reservas.web.rest;

import static com.reservas.domain.AvaliacaoAsserts.*;
import static com.reservas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservas.IntegrationTest;
import com.reservas.domain.Avaliacao;
import com.reservas.repository.AvaliacaoRepository;
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
 * Integration tests for the {@link AvaliacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AvaliacaoResourceIT {

    private static final Long DEFAULT_PONTOS = 1L;
    private static final Long UPDATED_PONTOS = 2L;

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/avaliacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAvaliacaoMockMvc;

    private Avaliacao avaliacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Avaliacao createEntity(EntityManager em) {
        Avaliacao avaliacao = new Avaliacao().pontos(DEFAULT_PONTOS).comentario(DEFAULT_COMENTARIO);
        return avaliacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Avaliacao createUpdatedEntity(EntityManager em) {
        Avaliacao avaliacao = new Avaliacao().pontos(UPDATED_PONTOS).comentario(UPDATED_COMENTARIO);
        return avaliacao;
    }

    @BeforeEach
    public void initTest() {
        avaliacao = createEntity(em);
    }

    @Test
    @Transactional
    void createAvaliacao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Avaliacao
        var returnedAvaliacao = om.readValue(
            restAvaliacaoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avaliacao)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Avaliacao.class
        );

        // Validate the Avaliacao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAvaliacaoUpdatableFieldsEquals(returnedAvaliacao, getPersistedAvaliacao(returnedAvaliacao));
    }

    @Test
    @Transactional
    void createAvaliacaoWithExistingId() throws Exception {
        // Create the Avaliacao with an existing ID
        avaliacao.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvaliacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avaliacao)))
            .andExpect(status().isBadRequest());

        // Validate the Avaliacao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAvaliacaos() throws Exception {
        // Initialize the database
        avaliacaoRepository.saveAndFlush(avaliacao);

        // Get all the avaliacaoList
        restAvaliacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avaliacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].pontos").value(hasItem(DEFAULT_PONTOS.intValue())))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO)));
    }

    @Test
    @Transactional
    void getAvaliacao() throws Exception {
        // Initialize the database
        avaliacaoRepository.saveAndFlush(avaliacao);

        // Get the avaliacao
        restAvaliacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, avaliacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(avaliacao.getId().intValue()))
            .andExpect(jsonPath("$.pontos").value(DEFAULT_PONTOS.intValue()))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO));
    }

    @Test
    @Transactional
    void getNonExistingAvaliacao() throws Exception {
        // Get the avaliacao
        restAvaliacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAvaliacao() throws Exception {
        // Initialize the database
        avaliacaoRepository.saveAndFlush(avaliacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the avaliacao
        Avaliacao updatedAvaliacao = avaliacaoRepository.findById(avaliacao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAvaliacao are not directly saved in db
        em.detach(updatedAvaliacao);
        updatedAvaliacao.pontos(UPDATED_PONTOS).comentario(UPDATED_COMENTARIO);

        restAvaliacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAvaliacao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAvaliacao))
            )
            .andExpect(status().isOk());

        // Validate the Avaliacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAvaliacaoToMatchAllProperties(updatedAvaliacao);
    }

    @Test
    @Transactional
    void putNonExistingAvaliacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avaliacao.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvaliacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, avaliacao.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avaliacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avaliacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAvaliacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avaliacao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvaliacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(avaliacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avaliacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAvaliacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avaliacao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvaliacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avaliacao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Avaliacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAvaliacaoWithPatch() throws Exception {
        // Initialize the database
        avaliacaoRepository.saveAndFlush(avaliacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the avaliacao using partial update
        Avaliacao partialUpdatedAvaliacao = new Avaliacao();
        partialUpdatedAvaliacao.setId(avaliacao.getId());

        restAvaliacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvaliacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAvaliacao))
            )
            .andExpect(status().isOk());

        // Validate the Avaliacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAvaliacaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAvaliacao, avaliacao),
            getPersistedAvaliacao(avaliacao)
        );
    }

    @Test
    @Transactional
    void fullUpdateAvaliacaoWithPatch() throws Exception {
        // Initialize the database
        avaliacaoRepository.saveAndFlush(avaliacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the avaliacao using partial update
        Avaliacao partialUpdatedAvaliacao = new Avaliacao();
        partialUpdatedAvaliacao.setId(avaliacao.getId());

        partialUpdatedAvaliacao.pontos(UPDATED_PONTOS).comentario(UPDATED_COMENTARIO);

        restAvaliacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvaliacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAvaliacao))
            )
            .andExpect(status().isOk());

        // Validate the Avaliacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAvaliacaoUpdatableFieldsEquals(partialUpdatedAvaliacao, getPersistedAvaliacao(partialUpdatedAvaliacao));
    }

    @Test
    @Transactional
    void patchNonExistingAvaliacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avaliacao.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvaliacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, avaliacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(avaliacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avaliacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAvaliacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avaliacao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvaliacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(avaliacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avaliacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAvaliacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avaliacao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvaliacaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(avaliacao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Avaliacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAvaliacao() throws Exception {
        // Initialize the database
        avaliacaoRepository.saveAndFlush(avaliacao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the avaliacao
        restAvaliacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, avaliacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return avaliacaoRepository.count();
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

    protected Avaliacao getPersistedAvaliacao(Avaliacao avaliacao) {
        return avaliacaoRepository.findById(avaliacao.getId()).orElseThrow();
    }

    protected void assertPersistedAvaliacaoToMatchAllProperties(Avaliacao expectedAvaliacao) {
        assertAvaliacaoAllPropertiesEquals(expectedAvaliacao, getPersistedAvaliacao(expectedAvaliacao));
    }

    protected void assertPersistedAvaliacaoToMatchUpdatableProperties(Avaliacao expectedAvaliacao) {
        assertAvaliacaoAllUpdatablePropertiesEquals(expectedAvaliacao, getPersistedAvaliacao(expectedAvaliacao));
    }
}
