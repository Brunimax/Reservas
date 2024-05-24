package com.reservas.web.rest;

import static com.reservas.domain.MunicipioAsserts.*;
import static com.reservas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservas.IntegrationTest;
import com.reservas.domain.Municipio;
import com.reservas.repository.MunicipioRepository;
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
 * Integration tests for the {@link MunicipioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MunicipioResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/municipios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMunicipioMockMvc;

    private Municipio municipio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Municipio createEntity(EntityManager em) {
        Municipio municipio = new Municipio().nome(DEFAULT_NOME);
        return municipio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Municipio createUpdatedEntity(EntityManager em) {
        Municipio municipio = new Municipio().nome(UPDATED_NOME);
        return municipio;
    }

    @BeforeEach
    public void initTest() {
        municipio = createEntity(em);
    }

    @Test
    @Transactional
    void createMunicipio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Municipio
        var returnedMunicipio = om.readValue(
            restMunicipioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(municipio)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Municipio.class
        );

        // Validate the Municipio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMunicipioUpdatableFieldsEquals(returnedMunicipio, getPersistedMunicipio(returnedMunicipio));
    }

    @Test
    @Transactional
    void createMunicipioWithExistingId() throws Exception {
        // Create the Municipio with an existing ID
        municipio.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMunicipioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(municipio)))
            .andExpect(status().isBadRequest());

        // Validate the Municipio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMunicipios() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get all the municipioList
        restMunicipioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(municipio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getMunicipio() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get the municipio
        restMunicipioMockMvc
            .perform(get(ENTITY_API_URL_ID, municipio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(municipio.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getNonExistingMunicipio() throws Exception {
        // Get the municipio
        restMunicipioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMunicipio() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the municipio
        Municipio updatedMunicipio = municipioRepository.findById(municipio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMunicipio are not directly saved in db
        em.detach(updatedMunicipio);
        updatedMunicipio.nome(UPDATED_NOME);

        restMunicipioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMunicipio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMunicipio))
            )
            .andExpect(status().isOk());

        // Validate the Municipio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMunicipioToMatchAllProperties(updatedMunicipio);
    }

    @Test
    @Transactional
    void putNonExistingMunicipio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        municipio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, municipio.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(municipio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMunicipio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        municipio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(municipio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMunicipio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        municipio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(municipio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Municipio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMunicipioWithPatch() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the municipio using partial update
        Municipio partialUpdatedMunicipio = new Municipio();
        partialUpdatedMunicipio.setId(municipio.getId());

        partialUpdatedMunicipio.nome(UPDATED_NOME);

        restMunicipioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMunicipio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMunicipio))
            )
            .andExpect(status().isOk());

        // Validate the Municipio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMunicipioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMunicipio, municipio),
            getPersistedMunicipio(municipio)
        );
    }

    @Test
    @Transactional
    void fullUpdateMunicipioWithPatch() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the municipio using partial update
        Municipio partialUpdatedMunicipio = new Municipio();
        partialUpdatedMunicipio.setId(municipio.getId());

        partialUpdatedMunicipio.nome(UPDATED_NOME);

        restMunicipioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMunicipio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMunicipio))
            )
            .andExpect(status().isOk());

        // Validate the Municipio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMunicipioUpdatableFieldsEquals(partialUpdatedMunicipio, getPersistedMunicipio(partialUpdatedMunicipio));
    }

    @Test
    @Transactional
    void patchNonExistingMunicipio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        municipio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, municipio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(municipio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMunicipio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        municipio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(municipio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Municipio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMunicipio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        municipio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMunicipioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(municipio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Municipio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMunicipio() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the municipio
        restMunicipioMockMvc
            .perform(delete(ENTITY_API_URL_ID, municipio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return municipioRepository.count();
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

    protected Municipio getPersistedMunicipio(Municipio municipio) {
        return municipioRepository.findById(municipio.getId()).orElseThrow();
    }

    protected void assertPersistedMunicipioToMatchAllProperties(Municipio expectedMunicipio) {
        assertMunicipioAllPropertiesEquals(expectedMunicipio, getPersistedMunicipio(expectedMunicipio));
    }

    protected void assertPersistedMunicipioToMatchUpdatableProperties(Municipio expectedMunicipio) {
        assertMunicipioAllUpdatablePropertiesEquals(expectedMunicipio, getPersistedMunicipio(expectedMunicipio));
    }
}
