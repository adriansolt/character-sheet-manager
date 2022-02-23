package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.PrereqSkillOrAtribute;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.repository.PrereqSkillOrAtributeRepository;
import com.adi.cms.gateway.service.dto.PrereqSkillOrAtributeDTO;
import com.adi.cms.gateway.service.mapper.PrereqSkillOrAtributeMapper;
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
 * Integration tests for the {@link PrereqSkillOrAtributeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PrereqSkillOrAtributeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final String ENTITY_API_URL = "/api/prereq-skill-or-atributes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrereqSkillOrAtributeRepository prereqSkillOrAtributeRepository;

    @Autowired
    private PrereqSkillOrAtributeMapper prereqSkillOrAtributeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PrereqSkillOrAtribute prereqSkillOrAtribute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrereqSkillOrAtribute createEntity(EntityManager em) {
        PrereqSkillOrAtribute prereqSkillOrAtribute = new PrereqSkillOrAtribute().name(DEFAULT_NAME).level(DEFAULT_LEVEL);
        return prereqSkillOrAtribute;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrereqSkillOrAtribute createUpdatedEntity(EntityManager em) {
        PrereqSkillOrAtribute prereqSkillOrAtribute = new PrereqSkillOrAtribute().name(UPDATED_NAME).level(UPDATED_LEVEL);
        return prereqSkillOrAtribute;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PrereqSkillOrAtribute.class).block();
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
        prereqSkillOrAtribute = createEntity(em);
    }

    @Test
    void createPrereqSkillOrAtribute() throws Exception {
        int databaseSizeBeforeCreate = prereqSkillOrAtributeRepository.findAll().collectList().block().size();
        // Create the PrereqSkillOrAtribute
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeCreate + 1);
        PrereqSkillOrAtribute testPrereqSkillOrAtribute = prereqSkillOrAtributeList.get(prereqSkillOrAtributeList.size() - 1);
        assertThat(testPrereqSkillOrAtribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrereqSkillOrAtribute.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    void createPrereqSkillOrAtributeWithExistingId() throws Exception {
        // Create the PrereqSkillOrAtribute with an existing ID
        prereqSkillOrAtribute.setId(1L);
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        int databaseSizeBeforeCreate = prereqSkillOrAtributeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = prereqSkillOrAtributeRepository.findAll().collectList().block().size();
        // set the field null
        prereqSkillOrAtribute.setName(null);

        // Create the PrereqSkillOrAtribute, which fails.
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = prereqSkillOrAtributeRepository.findAll().collectList().block().size();
        // set the field null
        prereqSkillOrAtribute.setLevel(null);

        // Create the PrereqSkillOrAtribute, which fails.
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPrereqSkillOrAtributes() {
        // Initialize the database
        prereqSkillOrAtributeRepository.save(prereqSkillOrAtribute).block();

        // Get all the prereqSkillOrAtributeList
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
            .value(hasItem(prereqSkillOrAtribute.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].level")
            .value(hasItem(DEFAULT_LEVEL));
    }

    @Test
    void getPrereqSkillOrAtribute() {
        // Initialize the database
        prereqSkillOrAtributeRepository.save(prereqSkillOrAtribute).block();

        // Get the prereqSkillOrAtribute
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, prereqSkillOrAtribute.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(prereqSkillOrAtribute.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.level")
            .value(is(DEFAULT_LEVEL));
    }

    @Test
    void getNonExistingPrereqSkillOrAtribute() {
        // Get the prereqSkillOrAtribute
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPrereqSkillOrAtribute() throws Exception {
        // Initialize the database
        prereqSkillOrAtributeRepository.save(prereqSkillOrAtribute).block();

        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().collectList().block().size();

        // Update the prereqSkillOrAtribute
        PrereqSkillOrAtribute updatedPrereqSkillOrAtribute = prereqSkillOrAtributeRepository
            .findById(prereqSkillOrAtribute.getId())
            .block();
        updatedPrereqSkillOrAtribute.name(UPDATED_NAME).level(UPDATED_LEVEL);
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(updatedPrereqSkillOrAtribute);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, prereqSkillOrAtributeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
        PrereqSkillOrAtribute testPrereqSkillOrAtribute = prereqSkillOrAtributeList.get(prereqSkillOrAtributeList.size() - 1);
        assertThat(testPrereqSkillOrAtribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrereqSkillOrAtribute.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    void putNonExistingPrereqSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().collectList().block().size();
        prereqSkillOrAtribute.setId(count.incrementAndGet());

        // Create the PrereqSkillOrAtribute
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, prereqSkillOrAtributeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPrereqSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().collectList().block().size();
        prereqSkillOrAtribute.setId(count.incrementAndGet());

        // Create the PrereqSkillOrAtribute
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPrereqSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().collectList().block().size();
        prereqSkillOrAtribute.setId(count.incrementAndGet());

        // Create the PrereqSkillOrAtribute
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePrereqSkillOrAtributeWithPatch() throws Exception {
        // Initialize the database
        prereqSkillOrAtributeRepository.save(prereqSkillOrAtribute).block();

        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().collectList().block().size();

        // Update the prereqSkillOrAtribute using partial update
        PrereqSkillOrAtribute partialUpdatedPrereqSkillOrAtribute = new PrereqSkillOrAtribute();
        partialUpdatedPrereqSkillOrAtribute.setId(prereqSkillOrAtribute.getId());

        partialUpdatedPrereqSkillOrAtribute.level(UPDATED_LEVEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPrereqSkillOrAtribute.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPrereqSkillOrAtribute))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
        PrereqSkillOrAtribute testPrereqSkillOrAtribute = prereqSkillOrAtributeList.get(prereqSkillOrAtributeList.size() - 1);
        assertThat(testPrereqSkillOrAtribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrereqSkillOrAtribute.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    void fullUpdatePrereqSkillOrAtributeWithPatch() throws Exception {
        // Initialize the database
        prereqSkillOrAtributeRepository.save(prereqSkillOrAtribute).block();

        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().collectList().block().size();

        // Update the prereqSkillOrAtribute using partial update
        PrereqSkillOrAtribute partialUpdatedPrereqSkillOrAtribute = new PrereqSkillOrAtribute();
        partialUpdatedPrereqSkillOrAtribute.setId(prereqSkillOrAtribute.getId());

        partialUpdatedPrereqSkillOrAtribute.name(UPDATED_NAME).level(UPDATED_LEVEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPrereqSkillOrAtribute.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPrereqSkillOrAtribute))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
        PrereqSkillOrAtribute testPrereqSkillOrAtribute = prereqSkillOrAtributeList.get(prereqSkillOrAtributeList.size() - 1);
        assertThat(testPrereqSkillOrAtribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrereqSkillOrAtribute.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    void patchNonExistingPrereqSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().collectList().block().size();
        prereqSkillOrAtribute.setId(count.incrementAndGet());

        // Create the PrereqSkillOrAtribute
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, prereqSkillOrAtributeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPrereqSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().collectList().block().size();
        prereqSkillOrAtribute.setId(count.incrementAndGet());

        // Create the PrereqSkillOrAtribute
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPrereqSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().collectList().block().size();
        prereqSkillOrAtribute.setId(count.incrementAndGet());

        // Create the PrereqSkillOrAtribute
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePrereqSkillOrAtribute() {
        // Initialize the database
        prereqSkillOrAtributeRepository.save(prereqSkillOrAtribute).block();

        int databaseSizeBeforeDelete = prereqSkillOrAtributeRepository.findAll().collectList().block().size();

        // Delete the prereqSkillOrAtribute
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, prereqSkillOrAtribute.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll().collectList().block();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
