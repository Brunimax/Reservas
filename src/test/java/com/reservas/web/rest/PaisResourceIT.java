package com.reservas.web.rest;

import static com.reservas.domain.PaisAsserts.*;
import static com.reservas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservas.IntegrationTest;
import com.reservas.domain.Pais;
import com.reservas.repository.PaisRepository;
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
 * Integration tests for the {@link PaisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaisResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLA = "AAAAAAAAAA";
    private static final String UPDATED_SIGLA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pais";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaisMockMvc;

    private Pais pais;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pais createEntity(EntityManager em) {
        Pais pais = new Pais().nome(DEFAULT_NOME).sigla(DEFAULT_SIGLA);
        return pais;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pais createUpdatedEntity(EntityManager em) {
        Pais pais = new Pais().nome(UPDATED_NOME).sigla(UPDATED_SIGLA);
        return pais;
    }

    @BeforeEach
    public void initTest() {
        pais = createEntity(em);
    }

    @Test
    @Transactional
    void createPais() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pais
        var returnedPais = om.readValue(
            restPaisMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pais)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Pais.class
        );

        // Validate the Pais in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaisUpdatableFieldsEquals(returnedPais, getPersistedPais(returnedPais));
    }

    @Test
    @Transactional
    void createPaisWithExistingId() throws Exception {
        // Create the Pais with an existing ID
        pais.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pais)))
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPais() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get all the paisList
        restPaisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pais.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)));
    }

    @Test
    @Transactional
    void getPais() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get the pais
        restPaisMockMvc
            .perform(get(ENTITY_API_URL_ID, pais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pais.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.sigla").value(DEFAULT_SIGLA));
    }

    @Test
    @Transactional
    void getNonExistingPais() throws Exception {
        // Get the pais
        restPaisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPais() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pais
        Pais updatedPais = paisRepository.findById(pais.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPais are not directly saved in db
        em.detach(updatedPais);
        updatedPais.nome(UPDATED_NOME).sigla(UPDATED_SIGLA);

        restPaisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPais.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPais))
            )
            .andExpect(status().isOk());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaisToMatchAllProperties(updatedPais);
    }

    @Test
    @Transactional
    void putNonExistingPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(put(ENTITY_API_URL_ID, pais.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pais)))
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pais))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pais)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaisWithPatch() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pais using partial update
        Pais partialUpdatedPais = new Pais();
        partialUpdatedPais.setId(pais.getId());

        partialUpdatedPais.sigla(UPDATED_SIGLA);

        restPaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPais.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPais))
            )
            .andExpect(status().isOk());

        // Validate the Pais in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaisUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPais, pais), getPersistedPais(pais));
    }

    @Test
    @Transactional
    void fullUpdatePaisWithPatch() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pais using partial update
        Pais partialUpdatedPais = new Pais();
        partialUpdatedPais.setId(pais.getId());

        partialUpdatedPais.nome(UPDATED_NOME).sigla(UPDATED_SIGLA);

        restPaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPais.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPais))
            )
            .andExpect(status().isOk());

        // Validate the Pais in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaisUpdatableFieldsEquals(partialUpdatedPais, getPersistedPais(partialUpdatedPais));
    }

    @Test
    @Transactional
    void patchNonExistingPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(patch(ENTITY_API_URL_ID, pais.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pais)))
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pais))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pais)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePais() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pais
        restPaisMockMvc
            .perform(delete(ENTITY_API_URL_ID, pais.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paisRepository.count();
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

    protected Pais getPersistedPais(Pais pais) {
        return paisRepository.findById(pais.getId()).orElseThrow();
    }

    protected void assertPersistedPaisToMatchAllProperties(Pais expectedPais) {
        assertPaisAllPropertiesEquals(expectedPais, getPersistedPais(expectedPais));
    }

    protected void assertPersistedPaisToMatchUpdatableProperties(Pais expectedPais) {
        assertPaisAllUpdatablePropertiesEquals(expectedPais, getPersistedPais(expectedPais));
    }
}
