package com.reservas.web.rest;

import static com.reservas.domain.TipoQuartoAsserts.*;
import static com.reservas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservas.IntegrationTest;
import com.reservas.domain.TipoQuarto;
import com.reservas.repository.TipoQuartoRepository;
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
 * Integration tests for the {@link TipoQuartoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoQuartoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-quartos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TipoQuartoRepository tipoQuartoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoQuartoMockMvc;

    private TipoQuarto tipoQuarto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoQuarto createEntity(EntityManager em) {
        TipoQuarto tipoQuarto = new TipoQuarto().nome(DEFAULT_NOME);
        return tipoQuarto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoQuarto createUpdatedEntity(EntityManager em) {
        TipoQuarto tipoQuarto = new TipoQuarto().nome(UPDATED_NOME);
        return tipoQuarto;
    }

    @BeforeEach
    public void initTest() {
        tipoQuarto = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoQuarto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TipoQuarto
        var returnedTipoQuarto = om.readValue(
            restTipoQuartoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoQuarto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TipoQuarto.class
        );

        // Validate the TipoQuarto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTipoQuartoUpdatableFieldsEquals(returnedTipoQuarto, getPersistedTipoQuarto(returnedTipoQuarto));
    }

    @Test
    @Transactional
    void createTipoQuartoWithExistingId() throws Exception {
        // Create the TipoQuarto with an existing ID
        tipoQuarto.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoQuartoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoQuarto)))
            .andExpect(status().isBadRequest());

        // Validate the TipoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTipoQuartos() throws Exception {
        // Initialize the database
        tipoQuartoRepository.saveAndFlush(tipoQuarto);

        // Get all the tipoQuartoList
        restTipoQuartoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoQuarto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getTipoQuarto() throws Exception {
        // Initialize the database
        tipoQuartoRepository.saveAndFlush(tipoQuarto);

        // Get the tipoQuarto
        restTipoQuartoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoQuarto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoQuarto.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getNonExistingTipoQuarto() throws Exception {
        // Get the tipoQuarto
        restTipoQuartoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipoQuarto() throws Exception {
        // Initialize the database
        tipoQuartoRepository.saveAndFlush(tipoQuarto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoQuarto
        TipoQuarto updatedTipoQuarto = tipoQuartoRepository.findById(tipoQuarto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipoQuarto are not directly saved in db
        em.detach(updatedTipoQuarto);
        updatedTipoQuarto.nome(UPDATED_NOME);

        restTipoQuartoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTipoQuarto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTipoQuarto))
            )
            .andExpect(status().isOk());

        // Validate the TipoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTipoQuartoToMatchAllProperties(updatedTipoQuarto);
    }

    @Test
    @Transactional
    void putNonExistingTipoQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoQuarto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoQuartoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoQuarto.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoQuarto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoQuarto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoQuartoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoQuarto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoQuarto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoQuartoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoQuarto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoQuartoWithPatch() throws Exception {
        // Initialize the database
        tipoQuartoRepository.saveAndFlush(tipoQuarto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoQuarto using partial update
        TipoQuarto partialUpdatedTipoQuarto = new TipoQuarto();
        partialUpdatedTipoQuarto.setId(tipoQuarto.getId());

        partialUpdatedTipoQuarto.nome(UPDATED_NOME);

        restTipoQuartoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoQuarto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoQuarto))
            )
            .andExpect(status().isOk());

        // Validate the TipoQuarto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoQuartoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTipoQuarto, tipoQuarto),
            getPersistedTipoQuarto(tipoQuarto)
        );
    }

    @Test
    @Transactional
    void fullUpdateTipoQuartoWithPatch() throws Exception {
        // Initialize the database
        tipoQuartoRepository.saveAndFlush(tipoQuarto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoQuarto using partial update
        TipoQuarto partialUpdatedTipoQuarto = new TipoQuarto();
        partialUpdatedTipoQuarto.setId(tipoQuarto.getId());

        partialUpdatedTipoQuarto.nome(UPDATED_NOME);

        restTipoQuartoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoQuarto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoQuarto))
            )
            .andExpect(status().isOk());

        // Validate the TipoQuarto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoQuartoUpdatableFieldsEquals(partialUpdatedTipoQuarto, getPersistedTipoQuarto(partialUpdatedTipoQuarto));
    }

    @Test
    @Transactional
    void patchNonExistingTipoQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoQuarto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoQuartoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoQuarto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoQuarto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoQuarto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoQuartoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoQuarto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoQuarto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoQuartoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tipoQuarto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoQuarto() throws Exception {
        // Initialize the database
        tipoQuartoRepository.saveAndFlush(tipoQuarto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tipoQuarto
        restTipoQuartoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoQuarto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tipoQuartoRepository.count();
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

    protected TipoQuarto getPersistedTipoQuarto(TipoQuarto tipoQuarto) {
        return tipoQuartoRepository.findById(tipoQuarto.getId()).orElseThrow();
    }

    protected void assertPersistedTipoQuartoToMatchAllProperties(TipoQuarto expectedTipoQuarto) {
        assertTipoQuartoAllPropertiesEquals(expectedTipoQuarto, getPersistedTipoQuarto(expectedTipoQuarto));
    }

    protected void assertPersistedTipoQuartoToMatchUpdatableProperties(TipoQuarto expectedTipoQuarto) {
        assertTipoQuartoAllUpdatablePropertiesEquals(expectedTipoQuarto, getPersistedTipoQuarto(expectedTipoQuarto));
    }
}
