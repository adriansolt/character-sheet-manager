package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.ArmorPiece;
import com.adi.cms.gateway.domain.enumeration.ArmorLocation;
import com.adi.cms.gateway.repository.ArmorPieceRepository;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.service.dto.ArmorPieceDTO;
import com.adi.cms.gateway.service.mapper.ArmorPieceMapper;
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
 * Integration tests for the {@link ArmorPieceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ArmorPieceResourceIT {

    private static final ArmorLocation DEFAULT_LOCATION = ArmorLocation.HEAD;
    private static final ArmorLocation UPDATED_LOCATION = ArmorLocation.RIGHT_SHOULDER;

    private static final Integer DEFAULT_DEFENSE_MODIFIER = 1;
    private static final Integer UPDATED_DEFENSE_MODIFIER = 2;

    private static final String ENTITY_API_URL = "/api/armor-pieces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArmorPieceRepository armorPieceRepository;

    @Autowired
    private ArmorPieceMapper armorPieceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ArmorPiece armorPiece;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArmorPiece createEntity(EntityManager em) {
        ArmorPiece armorPiece = new ArmorPiece().location(DEFAULT_LOCATION).defenseModifier(DEFAULT_DEFENSE_MODIFIER);
        return armorPiece;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArmorPiece createUpdatedEntity(EntityManager em) {
        ArmorPiece armorPiece = new ArmorPiece().location(UPDATED_LOCATION).defenseModifier(UPDATED_DEFENSE_MODIFIER);
        return armorPiece;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ArmorPiece.class).block();
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
        armorPiece = createEntity(em);
    }

    @Test
    void createArmorPiece() throws Exception {
        int databaseSizeBeforeCreate = armorPieceRepository.findAll().collectList().block().size();
        // Create the ArmorPiece
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll().collectList().block();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeCreate + 1);
        ArmorPiece testArmorPiece = armorPieceList.get(armorPieceList.size() - 1);
        assertThat(testArmorPiece.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testArmorPiece.getDefenseModifier()).isEqualTo(DEFAULT_DEFENSE_MODIFIER);
    }

    @Test
    void createArmorPieceWithExistingId() throws Exception {
        // Create the ArmorPiece with an existing ID
        armorPiece.setId(1L);
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        int databaseSizeBeforeCreate = armorPieceRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll().collectList().block();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllArmorPieces() {
        // Initialize the database
        armorPieceRepository.save(armorPiece).block();

        // Get all the armorPieceList
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
            .value(hasItem(armorPiece.getId().intValue()))
            .jsonPath("$.[*].location")
            .value(hasItem(DEFAULT_LOCATION.toString()))
            .jsonPath("$.[*].defenseModifier")
            .value(hasItem(DEFAULT_DEFENSE_MODIFIER));
    }

    @Test
    void getArmorPiece() {
        // Initialize the database
        armorPieceRepository.save(armorPiece).block();

        // Get the armorPiece
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, armorPiece.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(armorPiece.getId().intValue()))
            .jsonPath("$.location")
            .value(is(DEFAULT_LOCATION.toString()))
            .jsonPath("$.defenseModifier")
            .value(is(DEFAULT_DEFENSE_MODIFIER));
    }

    @Test
    void getNonExistingArmorPiece() {
        // Get the armorPiece
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewArmorPiece() throws Exception {
        // Initialize the database
        armorPieceRepository.save(armorPiece).block();

        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().collectList().block().size();

        // Update the armorPiece
        ArmorPiece updatedArmorPiece = armorPieceRepository.findById(armorPiece.getId()).block();
        updatedArmorPiece.location(UPDATED_LOCATION).defenseModifier(UPDATED_DEFENSE_MODIFIER);
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(updatedArmorPiece);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, armorPieceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll().collectList().block();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
        ArmorPiece testArmorPiece = armorPieceList.get(armorPieceList.size() - 1);
        assertThat(testArmorPiece.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testArmorPiece.getDefenseModifier()).isEqualTo(UPDATED_DEFENSE_MODIFIER);
    }

    @Test
    void putNonExistingArmorPiece() throws Exception {
        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().collectList().block().size();
        armorPiece.setId(count.incrementAndGet());

        // Create the ArmorPiece
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, armorPieceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll().collectList().block();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchArmorPiece() throws Exception {
        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().collectList().block().size();
        armorPiece.setId(count.incrementAndGet());

        // Create the ArmorPiece
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll().collectList().block();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamArmorPiece() throws Exception {
        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().collectList().block().size();
        armorPiece.setId(count.incrementAndGet());

        // Create the ArmorPiece
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll().collectList().block();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateArmorPieceWithPatch() throws Exception {
        // Initialize the database
        armorPieceRepository.save(armorPiece).block();

        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().collectList().block().size();

        // Update the armorPiece using partial update
        ArmorPiece partialUpdatedArmorPiece = new ArmorPiece();
        partialUpdatedArmorPiece.setId(armorPiece.getId());

        partialUpdatedArmorPiece.defenseModifier(UPDATED_DEFENSE_MODIFIER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedArmorPiece.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedArmorPiece))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll().collectList().block();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
        ArmorPiece testArmorPiece = armorPieceList.get(armorPieceList.size() - 1);
        assertThat(testArmorPiece.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testArmorPiece.getDefenseModifier()).isEqualTo(UPDATED_DEFENSE_MODIFIER);
    }

    @Test
    void fullUpdateArmorPieceWithPatch() throws Exception {
        // Initialize the database
        armorPieceRepository.save(armorPiece).block();

        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().collectList().block().size();

        // Update the armorPiece using partial update
        ArmorPiece partialUpdatedArmorPiece = new ArmorPiece();
        partialUpdatedArmorPiece.setId(armorPiece.getId());

        partialUpdatedArmorPiece.location(UPDATED_LOCATION).defenseModifier(UPDATED_DEFENSE_MODIFIER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedArmorPiece.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedArmorPiece))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll().collectList().block();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
        ArmorPiece testArmorPiece = armorPieceList.get(armorPieceList.size() - 1);
        assertThat(testArmorPiece.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testArmorPiece.getDefenseModifier()).isEqualTo(UPDATED_DEFENSE_MODIFIER);
    }

    @Test
    void patchNonExistingArmorPiece() throws Exception {
        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().collectList().block().size();
        armorPiece.setId(count.incrementAndGet());

        // Create the ArmorPiece
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, armorPieceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll().collectList().block();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchArmorPiece() throws Exception {
        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().collectList().block().size();
        armorPiece.setId(count.incrementAndGet());

        // Create the ArmorPiece
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll().collectList().block();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamArmorPiece() throws Exception {
        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().collectList().block().size();
        armorPiece.setId(count.incrementAndGet());

        // Create the ArmorPiece
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll().collectList().block();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteArmorPiece() {
        // Initialize the database
        armorPieceRepository.save(armorPiece).block();

        int databaseSizeBeforeDelete = armorPieceRepository.findAll().collectList().block().size();

        // Delete the armorPiece
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, armorPiece.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll().collectList().block();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
