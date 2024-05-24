package com.reservas.web.rest;

import static com.reservas.domain.FotoQuartoAsserts.*;
import static com.reservas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservas.IntegrationTest;
import com.reservas.domain.FotoQuarto;
import com.reservas.repository.FotoQuartoRepository;
import jakarta.persistence.EntityManager;
import java.util.Base64;
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
 * Integration tests for the {@link FotoQuartoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FotoQuartoResourceIT {

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String ENTITY_API_URL = "/api/foto-quartos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FotoQuartoRepository fotoQuartoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFotoQuartoMockMvc;

    private FotoQuarto fotoQuarto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FotoQuarto createEntity(EntityManager em) {
        FotoQuarto fotoQuarto = new FotoQuarto().foto(DEFAULT_FOTO).fotoContentType(DEFAULT_FOTO_CONTENT_TYPE).status(DEFAULT_STATUS);
        return fotoQuarto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FotoQuarto createUpdatedEntity(EntityManager em) {
        FotoQuarto fotoQuarto = new FotoQuarto().foto(UPDATED_FOTO).fotoContentType(UPDATED_FOTO_CONTENT_TYPE).status(UPDATED_STATUS);
        return fotoQuarto;
    }

    @BeforeEach
    public void initTest() {
        fotoQuarto = createEntity(em);
    }

    @Test
    @Transactional
    void createFotoQuarto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FotoQuarto
        var returnedFotoQuarto = om.readValue(
            restFotoQuartoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fotoQuarto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FotoQuarto.class
        );

        // Validate the FotoQuarto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFotoQuartoUpdatableFieldsEquals(returnedFotoQuarto, getPersistedFotoQuarto(returnedFotoQuarto));
    }

    @Test
    @Transactional
    void createFotoQuartoWithExistingId() throws Exception {
        // Create the FotoQuarto with an existing ID
        fotoQuarto.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFotoQuartoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fotoQuarto)))
            .andExpect(status().isBadRequest());

        // Validate the FotoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFotoQuartos() throws Exception {
        // Initialize the database
        fotoQuartoRepository.saveAndFlush(fotoQuarto);

        // Get all the fotoQuartoList
        restFotoQuartoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fotoQuarto.getId().intValue())))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FOTO))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    void getFotoQuarto() throws Exception {
        // Initialize the database
        fotoQuartoRepository.saveAndFlush(fotoQuarto);

        // Get the fotoQuarto
        restFotoQuartoMockMvc
            .perform(get(ENTITY_API_URL_ID, fotoQuarto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fotoQuarto.getId().intValue()))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64.getEncoder().encodeToString(DEFAULT_FOTO)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFotoQuarto() throws Exception {
        // Get the fotoQuarto
        restFotoQuartoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFotoQuarto() throws Exception {
        // Initialize the database
        fotoQuartoRepository.saveAndFlush(fotoQuarto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fotoQuarto
        FotoQuarto updatedFotoQuarto = fotoQuartoRepository.findById(fotoQuarto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFotoQuarto are not directly saved in db
        em.detach(updatedFotoQuarto);
        updatedFotoQuarto.foto(UPDATED_FOTO).fotoContentType(UPDATED_FOTO_CONTENT_TYPE).status(UPDATED_STATUS);

        restFotoQuartoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFotoQuarto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFotoQuarto))
            )
            .andExpect(status().isOk());

        // Validate the FotoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFotoQuartoToMatchAllProperties(updatedFotoQuarto);
    }

    @Test
    @Transactional
    void putNonExistingFotoQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fotoQuarto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFotoQuartoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fotoQuarto.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fotoQuarto))
            )
            .andExpect(status().isBadRequest());

        // Validate the FotoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFotoQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fotoQuarto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFotoQuartoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fotoQuarto))
            )
            .andExpect(status().isBadRequest());

        // Validate the FotoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFotoQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fotoQuarto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFotoQuartoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fotoQuarto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FotoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFotoQuartoWithPatch() throws Exception {
        // Initialize the database
        fotoQuartoRepository.saveAndFlush(fotoQuarto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fotoQuarto using partial update
        FotoQuarto partialUpdatedFotoQuarto = new FotoQuarto();
        partialUpdatedFotoQuarto.setId(fotoQuarto.getId());

        restFotoQuartoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFotoQuarto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFotoQuarto))
            )
            .andExpect(status().isOk());

        // Validate the FotoQuarto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFotoQuartoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFotoQuarto, fotoQuarto),
            getPersistedFotoQuarto(fotoQuarto)
        );
    }

    @Test
    @Transactional
    void fullUpdateFotoQuartoWithPatch() throws Exception {
        // Initialize the database
        fotoQuartoRepository.saveAndFlush(fotoQuarto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fotoQuarto using partial update
        FotoQuarto partialUpdatedFotoQuarto = new FotoQuarto();
        partialUpdatedFotoQuarto.setId(fotoQuarto.getId());

        partialUpdatedFotoQuarto.foto(UPDATED_FOTO).fotoContentType(UPDATED_FOTO_CONTENT_TYPE).status(UPDATED_STATUS);

        restFotoQuartoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFotoQuarto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFotoQuarto))
            )
            .andExpect(status().isOk());

        // Validate the FotoQuarto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFotoQuartoUpdatableFieldsEquals(partialUpdatedFotoQuarto, getPersistedFotoQuarto(partialUpdatedFotoQuarto));
    }

    @Test
    @Transactional
    void patchNonExistingFotoQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fotoQuarto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFotoQuartoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fotoQuarto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fotoQuarto))
            )
            .andExpect(status().isBadRequest());

        // Validate the FotoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFotoQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fotoQuarto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFotoQuartoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fotoQuarto))
            )
            .andExpect(status().isBadRequest());

        // Validate the FotoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFotoQuarto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fotoQuarto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFotoQuartoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fotoQuarto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FotoQuarto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFotoQuarto() throws Exception {
        // Initialize the database
        fotoQuartoRepository.saveAndFlush(fotoQuarto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fotoQuarto
        restFotoQuartoMockMvc
            .perform(delete(ENTITY_API_URL_ID, fotoQuarto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fotoQuartoRepository.count();
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

    protected FotoQuarto getPersistedFotoQuarto(FotoQuarto fotoQuarto) {
        return fotoQuartoRepository.findById(fotoQuarto.getId()).orElseThrow();
    }

    protected void assertPersistedFotoQuartoToMatchAllProperties(FotoQuarto expectedFotoQuarto) {
        assertFotoQuartoAllPropertiesEquals(expectedFotoQuarto, getPersistedFotoQuarto(expectedFotoQuarto));
    }

    protected void assertPersistedFotoQuartoToMatchUpdatableProperties(FotoQuarto expectedFotoQuarto) {
        assertFotoQuartoAllUpdatablePropertiesEquals(expectedFotoQuarto, getPersistedFotoQuarto(expectedFotoQuarto));
    }
}
