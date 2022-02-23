package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.CharacterAttribute;
import com.adi.cms.gateway.domain.enumeration.AttributeName;
import com.adi.cms.gateway.repository.CharacterAttributeRepository;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.service.dto.CharacterAttributeDTO;
import com.adi.cms.gateway.service.mapper.CharacterAttributeMapper;
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
 * Integration tests for the {@link CharacterAttributeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CharacterAttributeResourceIT {

    private static final AttributeName DEFAULT_NAME = AttributeName.ST;
    private static final AttributeName UPDATED_NAME = AttributeName.DX;

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final Integer DEFAULT_ATTRIBUTE_MODIFIER = 1;
    private static final Integer UPDATED_ATTRIBUTE_MODIFIER = 2;

    private static final String ENTITY_API_URL = "/api/character-attributes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CharacterAttributeRepository characterAttributeRepository;

    @Autowired
    private CharacterAttributeMapper characterAttributeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CharacterAttribute characterAttribute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharacterAttribute createEntity(EntityManager em) {
        CharacterAttribute characterAttribute = new CharacterAttribute()
            .name(DEFAULT_NAME)
            .points(DEFAULT_POINTS)
            .attributeModifier(DEFAULT_ATTRIBUTE_MODIFIER);
        return characterAttribute;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharacterAttribute createUpdatedEntity(EntityManager em) {
        CharacterAttribute characterAttribute = new CharacterAttribute()
            .name(UPDATED_NAME)
            .points(UPDATED_POINTS)
            .attributeModifier(UPDATED_ATTRIBUTE_MODIFIER);
        return characterAttribute;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CharacterAttribute.class).block();
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
        characterAttribute = createEntity(em);
    }

    @Test
    void createCharacterAttribute() throws Exception {
        int databaseSizeBeforeCreate = characterAttributeRepository.findAll().collectList().block().size();
        // Create the CharacterAttribute
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll().collectList().block();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeCreate + 1);
        CharacterAttribute testCharacterAttribute = characterAttributeList.get(characterAttributeList.size() - 1);
        assertThat(testCharacterAttribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCharacterAttribute.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testCharacterAttribute.getAttributeModifier()).isEqualTo(DEFAULT_ATTRIBUTE_MODIFIER);
    }

    @Test
    void createCharacterAttributeWithExistingId() throws Exception {
        // Create the CharacterAttribute with an existing ID
        characterAttribute.setId(1L);
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        int databaseSizeBeforeCreate = characterAttributeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll().collectList().block();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterAttributeRepository.findAll().collectList().block().size();
        // set the field null
        characterAttribute.setName(null);

        // Create the CharacterAttribute, which fails.
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll().collectList().block();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterAttributeRepository.findAll().collectList().block().size();
        // set the field null
        characterAttribute.setPoints(null);

        // Create the CharacterAttribute, which fails.
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll().collectList().block();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCharacterAttributes() {
        // Initialize the database
        characterAttributeRepository.save(characterAttribute).block();

        // Get all the characterAttributeList
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
            .value(hasItem(characterAttribute.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME.toString()))
            .jsonPath("$.[*].points")
            .value(hasItem(DEFAULT_POINTS))
            .jsonPath("$.[*].attributeModifier")
            .value(hasItem(DEFAULT_ATTRIBUTE_MODIFIER));
    }

    @Test
    void getCharacterAttribute() {
        // Initialize the database
        characterAttributeRepository.save(characterAttribute).block();

        // Get the characterAttribute
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, characterAttribute.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(characterAttribute.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME.toString()))
            .jsonPath("$.points")
            .value(is(DEFAULT_POINTS))
            .jsonPath("$.attributeModifier")
            .value(is(DEFAULT_ATTRIBUTE_MODIFIER));
    }

    @Test
    void getNonExistingCharacterAttribute() {
        // Get the characterAttribute
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCharacterAttribute() throws Exception {
        // Initialize the database
        characterAttributeRepository.save(characterAttribute).block();

        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().collectList().block().size();

        // Update the characterAttribute
        CharacterAttribute updatedCharacterAttribute = characterAttributeRepository.findById(characterAttribute.getId()).block();
        updatedCharacterAttribute.name(UPDATED_NAME).points(UPDATED_POINTS).attributeModifier(UPDATED_ATTRIBUTE_MODIFIER);
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(updatedCharacterAttribute);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, characterAttributeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll().collectList().block();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
        CharacterAttribute testCharacterAttribute = characterAttributeList.get(characterAttributeList.size() - 1);
        assertThat(testCharacterAttribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCharacterAttribute.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacterAttribute.getAttributeModifier()).isEqualTo(UPDATED_ATTRIBUTE_MODIFIER);
    }

    @Test
    void putNonExistingCharacterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().collectList().block().size();
        characterAttribute.setId(count.incrementAndGet());

        // Create the CharacterAttribute
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, characterAttributeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll().collectList().block();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCharacterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().collectList().block().size();
        characterAttribute.setId(count.incrementAndGet());

        // Create the CharacterAttribute
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll().collectList().block();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCharacterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().collectList().block().size();
        characterAttribute.setId(count.incrementAndGet());

        // Create the CharacterAttribute
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll().collectList().block();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCharacterAttributeWithPatch() throws Exception {
        // Initialize the database
        characterAttributeRepository.save(characterAttribute).block();

        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().collectList().block().size();

        // Update the characterAttribute using partial update
        CharacterAttribute partialUpdatedCharacterAttribute = new CharacterAttribute();
        partialUpdatedCharacterAttribute.setId(characterAttribute.getId());

        partialUpdatedCharacterAttribute.points(UPDATED_POINTS).attributeModifier(UPDATED_ATTRIBUTE_MODIFIER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCharacterAttribute.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterAttribute))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll().collectList().block();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
        CharacterAttribute testCharacterAttribute = characterAttributeList.get(characterAttributeList.size() - 1);
        assertThat(testCharacterAttribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCharacterAttribute.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacterAttribute.getAttributeModifier()).isEqualTo(UPDATED_ATTRIBUTE_MODIFIER);
    }

    @Test
    void fullUpdateCharacterAttributeWithPatch() throws Exception {
        // Initialize the database
        characterAttributeRepository.save(characterAttribute).block();

        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().collectList().block().size();

        // Update the characterAttribute using partial update
        CharacterAttribute partialUpdatedCharacterAttribute = new CharacterAttribute();
        partialUpdatedCharacterAttribute.setId(characterAttribute.getId());

        partialUpdatedCharacterAttribute.name(UPDATED_NAME).points(UPDATED_POINTS).attributeModifier(UPDATED_ATTRIBUTE_MODIFIER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCharacterAttribute.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterAttribute))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll().collectList().block();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
        CharacterAttribute testCharacterAttribute = characterAttributeList.get(characterAttributeList.size() - 1);
        assertThat(testCharacterAttribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCharacterAttribute.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacterAttribute.getAttributeModifier()).isEqualTo(UPDATED_ATTRIBUTE_MODIFIER);
    }

    @Test
    void patchNonExistingCharacterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().collectList().block().size();
        characterAttribute.setId(count.incrementAndGet());

        // Create the CharacterAttribute
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, characterAttributeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll().collectList().block();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCharacterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().collectList().block().size();
        characterAttribute.setId(count.incrementAndGet());

        // Create the CharacterAttribute
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll().collectList().block();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCharacterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().collectList().block().size();
        characterAttribute.setId(count.incrementAndGet());

        // Create the CharacterAttribute
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll().collectList().block();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCharacterAttribute() {
        // Initialize the database
        characterAttributeRepository.save(characterAttribute).block();

        int databaseSizeBeforeDelete = characterAttributeRepository.findAll().collectList().block().size();

        // Delete the characterAttribute
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, characterAttribute.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll().collectList().block();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
