package com.reservas.web.rest;

import static com.reservas.domain.EstadoAsserts.*;
import static com.reservas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservas.IntegrationTest;
import com.reservas.domain.Estado;
import com.reservas.repository.EstadoRepository;
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
 * Integration tests for the {@link EstadoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EstadoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLA = "AAAAAAAAAA";
    private static final String UPDATED_SIGLA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/estados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEstadoMockMvc;

    private Estado estado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estado createEntity(EntityManager em) {
        Estado estado = new Estado().nome(DEFAULT_NOME).sigla(DEFAULT_SIGLA);
        return estado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estado createUpdatedEntity(EntityManager em) {
        Estado estado = new Estado().nome(UPDATED_NOME).sigla(UPDATED_SIGLA);
        return estado;
    }

    @BeforeEach
    public void initTest() {
        estado = createEntity(em);
    }

    @Test
    @Transactional
    void createEstado() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Estado
        var returnedEstado = om.readValue(
            restEstadoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estado)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Estado.class
        );

        // Validate the Estado in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEstadoUpdatableFieldsEquals(returnedEstado, getPersistedEstado(returnedEstado));
    }

    @Test
    @Transactional
    void createEstadoWithExistingId() throws Exception {
        // Create the Estado with an existing ID
        estado.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estado)))
            .andExpect(status().isBadRequest());

        // Validate the Estado in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEstados() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        // Get all the estadoList
        restEstadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)));
    }

    @Test
    @Transactional
    void getEstado() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        // Get the estado
        restEstadoMockMvc
            .perform(get(ENTITY_API_URL_ID, estado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(estado.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.sigla").value(DEFAULT_SIGLA));
    }

    @Test
    @Transactional
    void getNonExistingEstado() throws Exception {
        // Get the estado
        restEstadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEstado() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estado
        Estado updatedEstado = estadoRepository.findById(estado.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEstado are not directly saved in db
        em.detach(updatedEstado);
        updatedEstado.nome(UPDATED_NOME).sigla(UPDATED_SIGLA);

        restEstadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEstado.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEstado))
            )
            .andExpect(status().isOk());

        // Validate the Estado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEstadoToMatchAllProperties(updatedEstado);
    }

    @Test
    @Transactional
    void putNonExistingEstado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estado.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstadoMockMvc
            .perform(put(ENTITY_API_URL_ID, estado.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estado)))
            .andExpect(status().isBadRequest());

        // Validate the Estado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEstado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estado.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEstado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estado.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estado)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Estado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEstadoWithPatch() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estado using partial update
        Estado partialUpdatedEstado = new Estado();
        partialUpdatedEstado.setId(estado.getId());

        restEstadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstado))
            )
            .andExpect(status().isOk());

        // Validate the Estado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEstadoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEstado, estado), getPersistedEstado(estado));
    }

    @Test
    @Transactional
    void fullUpdateEstadoWithPatch() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estado using partial update
        Estado partialUpdatedEstado = new Estado();
        partialUpdatedEstado.setId(estado.getId());

        partialUpdatedEstado.nome(UPDATED_NOME).sigla(UPDATED_SIGLA);

        restEstadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstado))
            )
            .andExpect(status().isOk());

        // Validate the Estado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEstadoUpdatableFieldsEquals(partialUpdatedEstado, getPersistedEstado(partialUpdatedEstado));
    }

    @Test
    @Transactional
    void patchNonExistingEstado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estado.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, estado.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(estado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEstado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estado.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(estado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEstado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estado.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(estado)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Estado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEstado() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the estado
        restEstadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, estado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return estadoRepository.count();
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

    protected Estado getPersistedEstado(Estado estado) {
        return estadoRepository.findById(estado.getId()).orElseThrow();
    }

    protected void assertPersistedEstadoToMatchAllProperties(Estado expectedEstado) {
        assertEstadoAllPropertiesEquals(expectedEstado, getPersistedEstado(expectedEstado));
    }

    protected void assertPersistedEstadoToMatchUpdatableProperties(Estado expectedEstado) {
        assertEstadoAllUpdatablePropertiesEquals(expectedEstado, getPersistedEstado(expectedEstado));
    }
}
