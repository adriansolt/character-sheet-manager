package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.DefaultSkillOrAtribute;
import com.adi.cms.gateway.repository.DefaultSkillOrAtributeRepository;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.service.dto.DefaultSkillOrAtributeDTO;
import com.adi.cms.gateway.service.mapper.DefaultSkillOrAtributeMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link DefaultSkillOrAtributeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DefaultSkillOrAtributeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_MODIFIER = 1;
    private static final Integer UPDATED_MODIFIER = 2;

    private static final String ENTITY_API_URL = "/api/default-skill-or-atributes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DefaultSkillOrAtributeRepository defaultSkillOrAtributeRepository;

    @Autowired
    private DefaultSkillOrAtributeMapper defaultSkillOrAtributeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private DefaultSkillOrAtribute defaultSkillOrAtribute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DefaultSkillOrAtribute createEntity(EntityManager em) {
        DefaultSkillOrAtribute defaultSkillOrAtribute = new DefaultSkillOrAtribute().name(DEFAULT_NAME).modifier(DEFAULT_MODIFIER);
        return defaultSkillOrAtribute;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DefaultSkillOrAtribute createUpdatedEntity(EntityManager em) {
        DefaultSkillOrAtribute defaultSkillOrAtribute = new DefaultSkillOrAtribute().name(UPDATED_NAME).modifier(UPDATED_MODIFIER);
        return defaultSkillOrAtribute;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(DefaultSkillOrAtribute.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        defaultSkillOrAtribute = createEntity(em);
    }

    @Test
    void createDefaultSkillOrAtribute() throws Exception {
        int databaseSizeBeforeCreate = defaultSkillOrAtributeRepository.findAll().collectList().block().size();
        // Create the DefaultSkillOrAtribute
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeCreate + 1);
        DefaultSkillOrAtribute testDefaultSkillOrAtribute = defaultSkillOrAtributeList.get(defaultSkillOrAtributeList.size() - 1);
        assertThat(testDefaultSkillOrAtribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDefaultSkillOrAtribute.getModifier()).isEqualTo(DEFAULT_MODIFIER);
    }

    @Test
    void createDefaultSkillOrAtributeWithExistingId() throws Exception {
        // Create the DefaultSkillOrAtribute with an existing ID
        defaultSkillOrAtribute.setId(1L);
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        int databaseSizeBeforeCreate = defaultSkillOrAtributeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = defaultSkillOrAtributeRepository.findAll().collectList().block().size();
        // set the field null
        defaultSkillOrAtribute.setName(null);

        // Create the DefaultSkillOrAtribute, which fails.
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkModifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = defaultSkillOrAtributeRepository.findAll().collectList().block().size();
        // set the field null
        defaultSkillOrAtribute.setModifier(null);

        // Create the DefaultSkillOrAtribute, which fails.
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllDefaultSkillOrAtributes() {
        // Initialize the database
        defaultSkillOrAtributeRepository.save(defaultSkillOrAtribute).block();

        // Get all the defaultSkillOrAtributeList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(defaultSkillOrAtribute.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].modifier")
            .value(hasItem(DEFAULT_MODIFIER));
    }

    @Test
    void getDefaultSkillOrAtribute() {
        // Initialize the database
        defaultSkillOrAtributeRepository.save(defaultSkillOrAtribute).block();

        // Get the defaultSkillOrAtribute
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, defaultSkillOrAtribute.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(defaultSkillOrAtribute.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.modifier")
            .value(is(DEFAULT_MODIFIER));
    }

    @Test
    void getNonExistingDefaultSkillOrAtribute() {
        // Get the defaultSkillOrAtribute
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewDefaultSkillOrAtribute() throws Exception {
        // Initialize the database
        defaultSkillOrAtributeRepository.save(defaultSkillOrAtribute).block();

        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().collectList().block().size();

        // Update the defaultSkillOrAtribute
        DefaultSkillOrAtribute updatedDefaultSkillOrAtribute = defaultSkillOrAtributeRepository
            .findById(defaultSkillOrAtribute.getId())
            .block();
        updatedDefaultSkillOrAtribute.name(UPDATED_NAME).modifier(UPDATED_MODIFIER);
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(updatedDefaultSkillOrAtribute);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, defaultSkillOrAtributeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
        DefaultSkillOrAtribute testDefaultSkillOrAtribute = defaultSkillOrAtributeList.get(defaultSkillOrAtributeList.size() - 1);
        assertThat(testDefaultSkillOrAtribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDefaultSkillOrAtribute.getModifier()).isEqualTo(UPDATED_MODIFIER);
    }

    @Test
    void putNonExistingDefaultSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().collectList().block().size();
        defaultSkillOrAtribute.setId(count.incrementAndGet());

        // Create the DefaultSkillOrAtribute
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, defaultSkillOrAtributeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDefaultSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().collectList().block().size();
        defaultSkillOrAtribute.setId(count.incrementAndGet());

        // Create the DefaultSkillOrAtribute
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDefaultSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().collectList().block().size();
        defaultSkillOrAtribute.setId(count.incrementAndGet());

        // Create the DefaultSkillOrAtribute
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDefaultSkillOrAtributeWithPatch() throws Exception {
        // Initialize the database
        defaultSkillOrAtributeRepository.save(defaultSkillOrAtribute).block();

        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().collectList().block().size();

        // Update the defaultSkillOrAtribute using partial update
        DefaultSkillOrAtribute partialUpdatedDefaultSkillOrAtribute = new DefaultSkillOrAtribute();
        partialUpdatedDefaultSkillOrAtribute.setId(defaultSkillOrAtribute.getId());

        partialUpdatedDefaultSkillOrAtribute.name(UPDATED_NAME).modifier(UPDATED_MODIFIER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDefaultSkillOrAtribute.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDefaultSkillOrAtribute))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
        DefaultSkillOrAtribute testDefaultSkillOrAtribute = defaultSkillOrAtributeList.get(defaultSkillOrAtributeList.size() - 1);
        assertThat(testDefaultSkillOrAtribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDefaultSkillOrAtribute.getModifier()).isEqualTo(UPDATED_MODIFIER);
    }

    @Test
    void fullUpdateDefaultSkillOrAtributeWithPatch() throws Exception {
        // Initialize the database
        defaultSkillOrAtributeRepository.save(defaultSkillOrAtribute).block();

        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().collectList().block().size();

        // Update the defaultSkillOrAtribute using partial update
        DefaultSkillOrAtribute partialUpdatedDefaultSkillOrAtribute = new DefaultSkillOrAtribute();
        partialUpdatedDefaultSkillOrAtribute.setId(defaultSkillOrAtribute.getId());

        partialUpdatedDefaultSkillOrAtribute.name(UPDATED_NAME).modifier(UPDATED_MODIFIER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDefaultSkillOrAtribute.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDefaultSkillOrAtribute))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
        DefaultSkillOrAtribute testDefaultSkillOrAtribute = defaultSkillOrAtributeList.get(defaultSkillOrAtributeList.size() - 1);
        assertThat(testDefaultSkillOrAtribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDefaultSkillOrAtribute.getModifier()).isEqualTo(UPDATED_MODIFIER);
    }

    @Test
    void patchNonExistingDefaultSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().collectList().block().size();
        defaultSkillOrAtribute.setId(count.incrementAndGet());

        // Create the DefaultSkillOrAtribute
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, defaultSkillOrAtributeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDefaultSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().collectList().block().size();
        defaultSkillOrAtribute.setId(count.incrementAndGet());

        // Create the DefaultSkillOrAtribute
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDefaultSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().collectList().block().size();
        defaultSkillOrAtribute.setId(count.incrementAndGet());

        // Create the DefaultSkillOrAtribute
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDefaultSkillOrAtribute() {
        // Initialize the database
        defaultSkillOrAtributeRepository.save(defaultSkillOrAtribute).block();

        int databaseSizeBeforeDelete = defaultSkillOrAtributeRepository.findAll().collectList().block().size();

        // Delete the defaultSkillOrAtribute
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, defaultSkillOrAtribute.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
