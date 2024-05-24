package com.reservas.web.rest;

import static com.reservas.domain.PessoaAsserts.*;
import static com.reservas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservas.IntegrationTest;
import com.reservas.domain.Pessoa;
import com.reservas.repository.PessoaRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PessoaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PessoaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SOBRENOME = "AAAAAAAAAA";
    private static final String UPDATED_SOBRENOME = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_NASCIMENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_NASCIMENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pessoas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPessoaMockMvc;

    private Pessoa pessoa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pessoa createEntity(EntityManager em) {
        Pessoa pessoa = new Pessoa()
            .nome(DEFAULT_NOME)
            .sobrenome(DEFAULT_SOBRENOME)
            .cpf(DEFAULT_CPF)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .telefone(DEFAULT_TELEFONE)
            .email(DEFAULT_EMAIL);
        return pessoa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pessoa createUpdatedEntity(EntityManager em) {
        Pessoa pessoa = new Pessoa()
            .nome(UPDATED_NOME)
            .sobrenome(UPDATED_SOBRENOME)
            .cpf(UPDATED_CPF)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL);
        return pessoa;
    }

    @BeforeEach
    public void initTest() {
        pessoa = createEntity(em);
    }

    @Test
    @Transactional
    void createPessoa() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pessoa
        var returnedPessoa = om.readValue(
            restPessoaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoa)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Pessoa.class
        );

        // Validate the Pessoa in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPessoaUpdatableFieldsEquals(returnedPessoa, getPersistedPessoa(returnedPessoa));
    }

    @Test
    @Transactional
    void createPessoaWithExistingId() throws Exception {
        // Create the Pessoa with an existing ID
        pessoa.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPessoaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoa)))
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPessoas() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList
        restPessoaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pessoa.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sobrenome").value(hasItem(DEFAULT_SOBRENOME)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getPessoa() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get the pessoa
        restPessoaMockMvc
            .perform(get(ENTITY_API_URL_ID, pessoa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pessoa.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.sobrenome").value(DEFAULT_SOBRENOME))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingPessoa() throws Exception {
        // Get the pessoa
        restPessoaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPessoa() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pessoa
        Pessoa updatedPessoa = pessoaRepository.findById(pessoa.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPessoa are not directly saved in db
        em.detach(updatedPessoa);
        updatedPessoa
            .nome(UPDATED_NOME)
            .sobrenome(UPDATED_SOBRENOME)
            .cpf(UPDATED_CPF)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL);

        restPessoaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPessoa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPessoa))
            )
            .andExpect(status().isOk());

        // Validate the Pessoa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPessoaToMatchAllProperties(updatedPessoa);
    }

    @Test
    @Transactional
    void putNonExistingPessoa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoa.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(put(ENTITY_API_URL_ID, pessoa.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoa)))
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPessoa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoa.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pessoa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPessoa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoa.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pessoa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePessoaWithPatch() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pessoa using partial update
        Pessoa partialUpdatedPessoa = new Pessoa();
        partialUpdatedPessoa.setId(pessoa.getId());

        partialUpdatedPessoa.dataNascimento(UPDATED_DATA_NASCIMENTO).email(UPDATED_EMAIL);

        restPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPessoa.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPessoa))
            )
            .andExpect(status().isOk());

        // Validate the Pessoa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPessoaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPessoa, pessoa), getPersistedPessoa(pessoa));
    }

    @Test
    @Transactional
    void fullUpdatePessoaWithPatch() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pessoa using partial update
        Pessoa partialUpdatedPessoa = new Pessoa();
        partialUpdatedPessoa.setId(pessoa.getId());

        partialUpdatedPessoa
            .nome(UPDATED_NOME)
            .sobrenome(UPDATED_SOBRENOME)
            .cpf(UPDATED_CPF)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL);

        restPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPessoa.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPessoa))
            )
            .andExpect(status().isOk());

        // Validate the Pessoa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPessoaUpdatableFieldsEquals(partialUpdatedPessoa, getPersistedPessoa(partialUpdatedPessoa));
    }

    @Test
    @Transactional
    void patchNonExistingPessoa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoa.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pessoa.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pessoa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPessoa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoa.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pessoa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPessoa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoa.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pessoa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pessoa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePessoa() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pessoa
        restPessoaMockMvc
            .perform(delete(ENTITY_API_URL_ID, pessoa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pessoaRepository.count();
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

    protected Pessoa getPersistedPessoa(Pessoa pessoa) {
        return pessoaRepository.findById(pessoa.getId()).orElseThrow();
    }

    protected void assertPersistedPessoaToMatchAllProperties(Pessoa expectedPessoa) {
        assertPessoaAllPropertiesEquals(expectedPessoa, getPersistedPessoa(expectedPessoa));
    }

    protected void assertPersistedPessoaToMatchUpdatableProperties(Pessoa expectedPessoa) {
        assertPessoaAllUpdatablePropertiesEquals(expectedPessoa, getPersistedPessoa(expectedPessoa));
    }
}
