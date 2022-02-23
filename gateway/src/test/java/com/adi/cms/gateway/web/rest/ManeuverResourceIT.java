package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.Maneuver;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.repository.ManeuverRepository;
import com.adi.cms.gateway.service.dto.ManeuverDTO;
import com.adi.cms.gateway.service.mapper.ManeuverMapper;
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
 * Integration tests for the {@link ManeuverResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ManeuverResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_MODIFIER = 1;
    private static final Integer UPDATED_MODIFIER = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/maneuvers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ManeuverRepository maneuverRepository;

    @Autowired
    private ManeuverMapper maneuverMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Maneuver maneuver;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maneuver createEntity(EntityManager em) {
        Maneuver maneuver = new Maneuver().name(DEFAULT_NAME).modifier(DEFAULT_MODIFIER).description(DEFAULT_DESCRIPTION);
        return maneuver;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maneuver createUpdatedEntity(EntityManager em) {
        Maneuver maneuver = new Maneuver().name(UPDATED_NAME).modifier(UPDATED_MODIFIER).description(UPDATED_DESCRIPTION);
        return maneuver;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Maneuver.class).block();
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
        maneuver = createEntity(em);
    }

    @Test
    void createManeuver() throws Exception {
        int databaseSizeBeforeCreate = maneuverRepository.findAll().collectList().block().size();
        // Create the Maneuver
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll().collectList().block();
        assertThat(maneuverList).hasSize(databaseSizeBeforeCreate + 1);
        Maneuver testManeuver = maneuverList.get(maneuverList.size() - 1);
        assertThat(testManeuver.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testManeuver.getModifier()).isEqualTo(DEFAULT_MODIFIER);
        assertThat(testManeuver.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createManeuverWithExistingId() throws Exception {
        // Create the Maneuver with an existing ID
        maneuver.setId(1L);
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        int databaseSizeBeforeCreate = maneuverRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll().collectList().block();
        assertThat(maneuverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = maneuverRepository.findAll().collectList().block().size();
        // set the field null
        maneuver.setName(null);

        // Create the Maneuver, which fails.
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Maneuver> maneuverList = maneuverRepository.findAll().collectList().block();
        assertThat(maneuverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = maneuverRepository.findAll().collectList().block().size();
        // set the field null
        maneuver.setDescription(null);

        // Create the Maneuver, which fails.
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Maneuver> maneuverList = maneuverRepository.findAll().collectList().block();
        assertThat(maneuverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllManeuvers() {
        // Initialize the database
        maneuverRepository.save(maneuver).block();

        // Get all the maneuverList
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
            .value(hasItem(maneuver.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].modifier")
            .value(hasItem(DEFAULT_MODIFIER))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getManeuver() {
        // Initialize the database
        maneuverRepository.save(maneuver).block();

        // Get the maneuver
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, maneuver.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(maneuver.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.modifier")
            .value(is(DEFAULT_MODIFIER))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingManeuver() {
        // Get the maneuver
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewManeuver() throws Exception {
        // Initialize the database
        maneuverRepository.save(maneuver).block();

        int databaseSizeBeforeUpdate = maneuverRepository.findAll().collectList().block().size();

        // Update the maneuver
        Maneuver updatedManeuver = maneuverRepository.findById(maneuver.getId()).block();
        updatedManeuver.name(UPDATED_NAME).modifier(UPDATED_MODIFIER).description(UPDATED_DESCRIPTION);
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(updatedManeuver);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, maneuverDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll().collectList().block();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
        Maneuver testManeuver = maneuverList.get(maneuverList.size() - 1);
        assertThat(testManeuver.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testManeuver.getModifier()).isEqualTo(UPDATED_MODIFIER);
        assertThat(testManeuver.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingManeuver() throws Exception {
        int databaseSizeBeforeUpdate = maneuverRepository.findAll().collectList().block().size();
        maneuver.setId(count.incrementAndGet());

        // Create the Maneuver
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, maneuverDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll().collectList().block();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchManeuver() throws Exception {
        int databaseSizeBeforeUpdate = maneuverRepository.findAll().collectList().block().size();
        maneuver.setId(count.incrementAndGet());

        // Create the Maneuver
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll().collectList().block();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamManeuver() throws Exception {
        int databaseSizeBeforeUpdate = maneuverRepository.findAll().collectList().block().size();
        maneuver.setId(count.incrementAndGet());

        // Create the Maneuver
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll().collectList().block();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateManeuverWithPatch() throws Exception {
        // Initialize the database
        maneuverRepository.save(maneuver).block();

        int databaseSizeBeforeUpdate = maneuverRepository.findAll().collectList().block().size();

        // Update the maneuver using partial update
        Maneuver partialUpdatedManeuver = new Maneuver();
        partialUpdatedManeuver.setId(maneuver.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedManeuver.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedManeuver))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll().collectList().block();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
        Maneuver testManeuver = maneuverList.get(maneuverList.size() - 1);
        assertThat(testManeuver.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testManeuver.getModifier()).isEqualTo(DEFAULT_MODIFIER);
        assertThat(testManeuver.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateManeuverWithPatch() throws Exception {
        // Initialize the database
        maneuverRepository.save(maneuver).block();

        int databaseSizeBeforeUpdate = maneuverRepository.findAll().collectList().block().size();

        // Update the maneuver using partial update
        Maneuver partialUpdatedManeuver = new Maneuver();
        partialUpdatedManeuver.setId(maneuver.getId());

        partialUpdatedManeuver.name(UPDATED_NAME).modifier(UPDATED_MODIFIER).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedManeuver.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedManeuver))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll().collectList().block();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
        Maneuver testManeuver = maneuverList.get(maneuverList.size() - 1);
        assertThat(testManeuver.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testManeuver.getModifier()).isEqualTo(UPDATED_MODIFIER);
        assertThat(testManeuver.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingManeuver() throws Exception {
        int databaseSizeBeforeUpdate = maneuverRepository.findAll().collectList().block().size();
        maneuver.setId(count.incrementAndGet());

        // Create the Maneuver
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, maneuverDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll().collectList().block();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchManeuver() throws Exception {
        int databaseSizeBeforeUpdate = maneuverRepository.findAll().collectList().block().size();
        maneuver.setId(count.incrementAndGet());

        // Create the Maneuver
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll().collectList().block();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamManeuver() throws Exception {
        int databaseSizeBeforeUpdate = maneuverRepository.findAll().collectList().block().size();
        maneuver.setId(count.incrementAndGet());

        // Create the Maneuver
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll().collectList().block();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteManeuver() {
        // Initialize the database
        maneuverRepository.save(maneuver).block();

        int databaseSizeBeforeDelete = maneuverRepository.findAll().collectList().block().size();

        // Delete the maneuver
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, maneuver.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Maneuver> maneuverList = maneuverRepository.findAll().collectList().block();
        assertThat(maneuverList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
