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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ArmorPieceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ArmorPieceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    private static final Integer DEFAULT_QUALITY = 1;
    private static final Integer UPDATED_QUALITY = 2;

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Long DEFAULT_CHARACTER_ID = 1L;
    private static final Long UPDATED_CHARACTER_ID = 2L;

    private static final Long DEFAULT_CAMPAIGN_ID = 1L;
    private static final Long UPDATED_CAMPAIGN_ID = 2L;

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
        ArmorPiece armorPiece = new ArmorPiece()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .weight(DEFAULT_WEIGHT)
            .quality(DEFAULT_QUALITY)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .characterId(DEFAULT_CHARACTER_ID)
            .campaignId(DEFAULT_CAMPAIGN_ID)
            .location(DEFAULT_LOCATION)
            .defenseModifier(DEFAULT_DEFENSE_MODIFIER);
        return armorPiece;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArmorPiece createUpdatedEntity(EntityManager em) {
        ArmorPiece armorPiece = new ArmorPiece()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .quality(UPDATED_QUALITY)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .characterId(UPDATED_CHARACTER_ID)
            .campaignId(UPDATED_CAMPAIGN_ID)
            .location(UPDATED_LOCATION)
            .defenseModifier(UPDATED_DEFENSE_MODIFIER);
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
        assertThat(testArmorPiece.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testArmorPiece.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testArmorPiece.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testArmorPiece.getQuality()).isEqualTo(DEFAULT_QUALITY);
        assertThat(testArmorPiece.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testArmorPiece.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testArmorPiece.getCharacterId()).isEqualTo(DEFAULT_CHARACTER_ID);
        assertThat(testArmorPiece.getCampaignId()).isEqualTo(DEFAULT_CAMPAIGN_ID);
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
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = armorPieceRepository.findAll().collectList().block().size();
        // set the field null
        armorPiece.setName(null);

        // Create the ArmorPiece, which fails.
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll().collectList().block();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = armorPieceRepository.findAll().collectList().block().size();
        // set the field null
        armorPiece.setWeight(null);

        // Create the ArmorPiece, which fails.
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll().collectList().block();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkQualityIsRequired() throws Exception {
        int databaseSizeBeforeTest = armorPieceRepository.findAll().collectList().block().size();
        // set the field null
        armorPiece.setQuality(null);

        // Create the ArmorPiece, which fails.
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll().collectList().block();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeTest);
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
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].weight")
            .value(hasItem(DEFAULT_WEIGHT))
            .jsonPath("$.[*].quality")
            .value(hasItem(DEFAULT_QUALITY))
            .jsonPath("$.[*].pictureContentType")
            .value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE))
            .jsonPath("$.[*].picture")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .jsonPath("$.[*].characterId")
            .value(hasItem(DEFAULT_CHARACTER_ID.intValue()))
            .jsonPath("$.[*].campaignId")
            .value(hasItem(DEFAULT_CAMPAIGN_ID.intValue()))
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
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.weight")
            .value(is(DEFAULT_WEIGHT))
            .jsonPath("$.quality")
            .value(is(DEFAULT_QUALITY))
            .jsonPath("$.pictureContentType")
            .value(is(DEFAULT_PICTURE_CONTENT_TYPE))
            .jsonPath("$.picture")
            .value(is(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .jsonPath("$.characterId")
            .value(is(DEFAULT_CHARACTER_ID.intValue()))
            .jsonPath("$.campaignId")
            .value(is(DEFAULT_CAMPAIGN_ID.intValue()))
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
        updatedArmorPiece
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .quality(UPDATED_QUALITY)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .characterId(UPDATED_CHARACTER_ID)
            .campaignId(UPDATED_CAMPAIGN_ID)
            .location(UPDATED_LOCATION)
            .defenseModifier(UPDATED_DEFENSE_MODIFIER);
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
        assertThat(testArmorPiece.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testArmorPiece.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArmorPiece.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testArmorPiece.getQuality()).isEqualTo(UPDATED_QUALITY);
        assertThat(testArmorPiece.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testArmorPiece.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testArmorPiece.getCharacterId()).isEqualTo(UPDATED_CHARACTER_ID);
        assertThat(testArmorPiece.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
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

        partialUpdatedArmorPiece
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .location(UPDATED_LOCATION)
            .defenseModifier(UPDATED_DEFENSE_MODIFIER);

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
        assertThat(testArmorPiece.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testArmorPiece.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArmorPiece.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testArmorPiece.getQuality()).isEqualTo(DEFAULT_QUALITY);
        assertThat(testArmorPiece.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testArmorPiece.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testArmorPiece.getCharacterId()).isEqualTo(DEFAULT_CHARACTER_ID);
        assertThat(testArmorPiece.getCampaignId()).isEqualTo(DEFAULT_CAMPAIGN_ID);
        assertThat(testArmorPiece.getLocation()).isEqualTo(UPDATED_LOCATION);
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

        partialUpdatedArmorPiece
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .quality(UPDATED_QUALITY)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .characterId(UPDATED_CHARACTER_ID)
            .campaignId(UPDATED_CAMPAIGN_ID)
            .location(UPDATED_LOCATION)
            .defenseModifier(UPDATED_DEFENSE_MODIFIER);

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
        assertThat(testArmorPiece.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testArmorPiece.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArmorPiece.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testArmorPiece.getQuality()).isEqualTo(UPDATED_QUALITY);
        assertThat(testArmorPiece.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testArmorPiece.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testArmorPiece.getCharacterId()).isEqualTo(UPDATED_CHARACTER_ID);
        assertThat(testArmorPiece.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
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
