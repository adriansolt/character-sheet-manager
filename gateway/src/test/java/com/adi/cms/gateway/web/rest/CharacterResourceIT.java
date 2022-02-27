package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.Character;
import com.adi.cms.gateway.domain.User;
import com.adi.cms.gateway.domain.enumeration.Handedness;
import com.adi.cms.gateway.repository.CharacterRepository;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.service.dto.CharacterDTO;
import com.adi.cms.gateway.service.mapper.CharacterMapper;
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
 * Integration tests for the {@link CharacterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CharacterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Handedness DEFAULT_HANDEDNESS = Handedness.RIGHT;
    private static final Handedness UPDATED_HANDEDNESS = Handedness.LEFT;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/characters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private CharacterMapper characterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Character character;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Character createEntity(EntityManager em) {
        Character character = new Character()
            .name(DEFAULT_NAME)
            .weight(DEFAULT_WEIGHT)
            .height(DEFAULT_HEIGHT)
            .points(DEFAULT_POINTS)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .handedness(DEFAULT_HANDEDNESS)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity(em)).block();
        character.setUser(user);
        return character;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Character createUpdatedEntity(EntityManager em) {
        Character character = new Character()
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .points(UPDATED_POINTS)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .handedness(UPDATED_HANDEDNESS)
            .active(UPDATED_ACTIVE);
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity(em)).block();
        character.setUser(user);
        return character;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Character.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        UserResourceIT.deleteEntities(em);
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
        character = createEntity(em);
    }

    @Test
    void createCharacter() throws Exception {
        int databaseSizeBeforeCreate = characterRepository.findAll().collectList().block().size();
        // Create the Character
        CharacterDTO characterDTO = characterMapper.toDto(character);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeCreate + 1);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCharacter.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testCharacter.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testCharacter.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testCharacter.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testCharacter.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testCharacter.getHandedness()).isEqualTo(DEFAULT_HANDEDNESS);
        assertThat(testCharacter.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    void createCharacterWithExistingId() throws Exception {
        // Create the Character with an existing ID
        character.setId(1L);
        CharacterDTO characterDTO = characterMapper.toDto(character);

        int databaseSizeBeforeCreate = characterRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterRepository.findAll().collectList().block().size();
        // set the field null
        character.setName(null);

        // Create the Character, which fails.
        CharacterDTO characterDTO = characterMapper.toDto(character);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterRepository.findAll().collectList().block().size();
        // set the field null
        character.setWeight(null);

        // Create the Character, which fails.
        CharacterDTO characterDTO = characterMapper.toDto(character);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkHeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterRepository.findAll().collectList().block().size();
        // set the field null
        character.setHeight(null);

        // Create the Character, which fails.
        CharacterDTO characterDTO = characterMapper.toDto(character);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterRepository.findAll().collectList().block().size();
        // set the field null
        character.setPoints(null);

        // Create the Character, which fails.
        CharacterDTO characterDTO = characterMapper.toDto(character);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCharacters() {
        // Initialize the database
        characterRepository.save(character).block();

        // Get all the characterList
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
            .value(hasItem(character.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].weight")
            .value(hasItem(DEFAULT_WEIGHT))
            .jsonPath("$.[*].height")
            .value(hasItem(DEFAULT_HEIGHT))
            .jsonPath("$.[*].points")
            .value(hasItem(DEFAULT_POINTS))
            .jsonPath("$.[*].pictureContentType")
            .value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE))
            .jsonPath("$.[*].picture")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .jsonPath("$.[*].handedness")
            .value(hasItem(DEFAULT_HANDEDNESS.toString()))
            .jsonPath("$.[*].active")
            .value(hasItem(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getCharacter() {
        // Initialize the database
        characterRepository.save(character).block();

        // Get the character
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, character.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(character.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.weight")
            .value(is(DEFAULT_WEIGHT))
            .jsonPath("$.height")
            .value(is(DEFAULT_HEIGHT))
            .jsonPath("$.points")
            .value(is(DEFAULT_POINTS))
            .jsonPath("$.pictureContentType")
            .value(is(DEFAULT_PICTURE_CONTENT_TYPE))
            .jsonPath("$.picture")
            .value(is(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .jsonPath("$.handedness")
            .value(is(DEFAULT_HANDEDNESS.toString()))
            .jsonPath("$.active")
            .value(is(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getNonExistingCharacter() {
        // Get the character
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCharacter() throws Exception {
        // Initialize the database
        characterRepository.save(character).block();

        int databaseSizeBeforeUpdate = characterRepository.findAll().collectList().block().size();

        // Update the character
        Character updatedCharacter = characterRepository.findById(character.getId()).block();
        updatedCharacter
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .points(UPDATED_POINTS)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .handedness(UPDATED_HANDEDNESS)
            .active(UPDATED_ACTIVE);
        CharacterDTO characterDTO = characterMapper.toDto(updatedCharacter);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, characterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCharacter.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testCharacter.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testCharacter.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacter.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testCharacter.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testCharacter.getHandedness()).isEqualTo(UPDATED_HANDEDNESS);
        assertThat(testCharacter.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    void putNonExistingCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().collectList().block().size();
        character.setId(count.incrementAndGet());

        // Create the Character
        CharacterDTO characterDTO = characterMapper.toDto(character);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, characterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().collectList().block().size();
        character.setId(count.incrementAndGet());

        // Create the Character
        CharacterDTO characterDTO = characterMapper.toDto(character);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().collectList().block().size();
        character.setId(count.incrementAndGet());

        // Create the Character
        CharacterDTO characterDTO = characterMapper.toDto(character);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCharacterWithPatch() throws Exception {
        // Initialize the database
        characterRepository.save(character).block();

        int databaseSizeBeforeUpdate = characterRepository.findAll().collectList().block().size();

        // Update the character using partial update
        Character partialUpdatedCharacter = new Character();
        partialUpdatedCharacter.setId(character.getId());

        partialUpdatedCharacter.handedness(UPDATED_HANDEDNESS).active(UPDATED_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCharacter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCharacter.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testCharacter.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testCharacter.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testCharacter.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testCharacter.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testCharacter.getHandedness()).isEqualTo(UPDATED_HANDEDNESS);
        assertThat(testCharacter.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    void fullUpdateCharacterWithPatch() throws Exception {
        // Initialize the database
        characterRepository.save(character).block();

        int databaseSizeBeforeUpdate = characterRepository.findAll().collectList().block().size();

        // Update the character using partial update
        Character partialUpdatedCharacter = new Character();
        partialUpdatedCharacter.setId(character.getId());

        partialUpdatedCharacter
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .points(UPDATED_POINTS)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .handedness(UPDATED_HANDEDNESS)
            .active(UPDATED_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCharacter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCharacter.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testCharacter.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testCharacter.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacter.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testCharacter.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testCharacter.getHandedness()).isEqualTo(UPDATED_HANDEDNESS);
        assertThat(testCharacter.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    void patchNonExistingCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().collectList().block().size();
        character.setId(count.incrementAndGet());

        // Create the Character
        CharacterDTO characterDTO = characterMapper.toDto(character);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, characterDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().collectList().block().size();
        character.setId(count.incrementAndGet());

        // Create the Character
        CharacterDTO characterDTO = characterMapper.toDto(character);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().collectList().block().size();
        character.setId(count.incrementAndGet());

        // Create the Character
        CharacterDTO characterDTO = characterMapper.toDto(character);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCharacter() {
        // Initialize the database
        characterRepository.save(character).block();

        int databaseSizeBeforeDelete = characterRepository.findAll().collectList().block().size();

        // Delete the character
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, character.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Character> characterList = characterRepository.findAll().collectList().block();
        assertThat(characterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
