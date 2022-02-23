package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.CharacterEquippedArmor;
import com.adi.cms.gateway.repository.CharacterEquippedArmorRepository;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.service.dto.CharacterEquippedArmorDTO;
import com.adi.cms.gateway.service.mapper.CharacterEquippedArmorMapper;
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
 * Integration tests for the {@link CharacterEquippedArmorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CharacterEquippedArmorResourceIT {

    private static final Long DEFAULT_CHARACTER_ID = 1L;
    private static final Long UPDATED_CHARACTER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/character-equipped-armors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CharacterEquippedArmorRepository characterEquippedArmorRepository;

    @Autowired
    private CharacterEquippedArmorMapper characterEquippedArmorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CharacterEquippedArmor characterEquippedArmor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharacterEquippedArmor createEntity(EntityManager em) {
        CharacterEquippedArmor characterEquippedArmor = new CharacterEquippedArmor().characterId(DEFAULT_CHARACTER_ID);
        return characterEquippedArmor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharacterEquippedArmor createUpdatedEntity(EntityManager em) {
        CharacterEquippedArmor characterEquippedArmor = new CharacterEquippedArmor().characterId(UPDATED_CHARACTER_ID);
        return characterEquippedArmor;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CharacterEquippedArmor.class).block();
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
        characterEquippedArmor = createEntity(em);
    }

    @Test
    void createCharacterEquippedArmor() throws Exception {
        int databaseSizeBeforeCreate = characterEquippedArmorRepository.findAll().collectList().block().size();
        // Create the CharacterEquippedArmor
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll().collectList().block();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeCreate + 1);
        CharacterEquippedArmor testCharacterEquippedArmor = characterEquippedArmorList.get(characterEquippedArmorList.size() - 1);
        assertThat(testCharacterEquippedArmor.getCharacterId()).isEqualTo(DEFAULT_CHARACTER_ID);
    }

    @Test
    void createCharacterEquippedArmorWithExistingId() throws Exception {
        // Create the CharacterEquippedArmor with an existing ID
        characterEquippedArmor.setId(1L);
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);

        int databaseSizeBeforeCreate = characterEquippedArmorRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll().collectList().block();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCharacterEquippedArmors() {
        // Initialize the database
        characterEquippedArmorRepository.save(characterEquippedArmor).block();

        // Get all the characterEquippedArmorList
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
            .value(hasItem(characterEquippedArmor.getId().intValue()))
            .jsonPath("$.[*].characterId")
            .value(hasItem(DEFAULT_CHARACTER_ID.intValue()));
    }

    @Test
    void getCharacterEquippedArmor() {
        // Initialize the database
        characterEquippedArmorRepository.save(characterEquippedArmor).block();

        // Get the characterEquippedArmor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, characterEquippedArmor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(characterEquippedArmor.getId().intValue()))
            .jsonPath("$.characterId")
            .value(is(DEFAULT_CHARACTER_ID.intValue()));
    }

    @Test
    void getNonExistingCharacterEquippedArmor() {
        // Get the characterEquippedArmor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCharacterEquippedArmor() throws Exception {
        // Initialize the database
        characterEquippedArmorRepository.save(characterEquippedArmor).block();

        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().collectList().block().size();

        // Update the characterEquippedArmor
        CharacterEquippedArmor updatedCharacterEquippedArmor = characterEquippedArmorRepository
            .findById(characterEquippedArmor.getId())
            .block();
        updatedCharacterEquippedArmor.characterId(UPDATED_CHARACTER_ID);
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(updatedCharacterEquippedArmor);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, characterEquippedArmorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll().collectList().block();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
        CharacterEquippedArmor testCharacterEquippedArmor = characterEquippedArmorList.get(characterEquippedArmorList.size() - 1);
        assertThat(testCharacterEquippedArmor.getCharacterId()).isEqualTo(UPDATED_CHARACTER_ID);
    }

    @Test
    void putNonExistingCharacterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().collectList().block().size();
        characterEquippedArmor.setId(count.incrementAndGet());

        // Create the CharacterEquippedArmor
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, characterEquippedArmorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll().collectList().block();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCharacterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().collectList().block().size();
        characterEquippedArmor.setId(count.incrementAndGet());

        // Create the CharacterEquippedArmor
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll().collectList().block();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCharacterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().collectList().block().size();
        characterEquippedArmor.setId(count.incrementAndGet());

        // Create the CharacterEquippedArmor
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll().collectList().block();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCharacterEquippedArmorWithPatch() throws Exception {
        // Initialize the database
        characterEquippedArmorRepository.save(characterEquippedArmor).block();

        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().collectList().block().size();

        // Update the characterEquippedArmor using partial update
        CharacterEquippedArmor partialUpdatedCharacterEquippedArmor = new CharacterEquippedArmor();
        partialUpdatedCharacterEquippedArmor.setId(characterEquippedArmor.getId());

        partialUpdatedCharacterEquippedArmor.characterId(UPDATED_CHARACTER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCharacterEquippedArmor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterEquippedArmor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll().collectList().block();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
        CharacterEquippedArmor testCharacterEquippedArmor = characterEquippedArmorList.get(characterEquippedArmorList.size() - 1);
        assertThat(testCharacterEquippedArmor.getCharacterId()).isEqualTo(UPDATED_CHARACTER_ID);
    }

    @Test
    void fullUpdateCharacterEquippedArmorWithPatch() throws Exception {
        // Initialize the database
        characterEquippedArmorRepository.save(characterEquippedArmor).block();

        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().collectList().block().size();

        // Update the characterEquippedArmor using partial update
        CharacterEquippedArmor partialUpdatedCharacterEquippedArmor = new CharacterEquippedArmor();
        partialUpdatedCharacterEquippedArmor.setId(characterEquippedArmor.getId());

        partialUpdatedCharacterEquippedArmor.characterId(UPDATED_CHARACTER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCharacterEquippedArmor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterEquippedArmor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll().collectList().block();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
        CharacterEquippedArmor testCharacterEquippedArmor = characterEquippedArmorList.get(characterEquippedArmorList.size() - 1);
        assertThat(testCharacterEquippedArmor.getCharacterId()).isEqualTo(UPDATED_CHARACTER_ID);
    }

    @Test
    void patchNonExistingCharacterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().collectList().block().size();
        characterEquippedArmor.setId(count.incrementAndGet());

        // Create the CharacterEquippedArmor
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, characterEquippedArmorDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll().collectList().block();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCharacterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().collectList().block().size();
        characterEquippedArmor.setId(count.incrementAndGet());

        // Create the CharacterEquippedArmor
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll().collectList().block();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCharacterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().collectList().block().size();
        characterEquippedArmor.setId(count.incrementAndGet());

        // Create the CharacterEquippedArmor
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll().collectList().block();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCharacterEquippedArmor() {
        // Initialize the database
        characterEquippedArmorRepository.save(characterEquippedArmor).block();

        int databaseSizeBeforeDelete = characterEquippedArmorRepository.findAll().collectList().block().size();

        // Delete the characterEquippedArmor
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, characterEquippedArmor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll().collectList().block();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
