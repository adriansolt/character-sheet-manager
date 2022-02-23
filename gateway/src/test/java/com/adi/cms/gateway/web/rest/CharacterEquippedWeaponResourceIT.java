package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.CharacterEquippedWeapon;
import com.adi.cms.gateway.domain.enumeration.Handedness;
import com.adi.cms.gateway.repository.CharacterEquippedWeaponRepository;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.service.dto.CharacterEquippedWeaponDTO;
import com.adi.cms.gateway.service.mapper.CharacterEquippedWeaponMapper;
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
 * Integration tests for the {@link CharacterEquippedWeaponResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CharacterEquippedWeaponResourceIT {

    private static final Long DEFAULT_CHARACTER_ID = 1L;
    private static final Long UPDATED_CHARACTER_ID = 2L;

    private static final Handedness DEFAULT_HAND = Handedness.RIGHT;
    private static final Handedness UPDATED_HAND = Handedness.LEFT;

    private static final String ENTITY_API_URL = "/api/character-equipped-weapons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CharacterEquippedWeaponRepository characterEquippedWeaponRepository;

    @Autowired
    private CharacterEquippedWeaponMapper characterEquippedWeaponMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CharacterEquippedWeapon characterEquippedWeapon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharacterEquippedWeapon createEntity(EntityManager em) {
        CharacterEquippedWeapon characterEquippedWeapon = new CharacterEquippedWeapon()
            .characterId(DEFAULT_CHARACTER_ID)
            .hand(DEFAULT_HAND);
        return characterEquippedWeapon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharacterEquippedWeapon createUpdatedEntity(EntityManager em) {
        CharacterEquippedWeapon characterEquippedWeapon = new CharacterEquippedWeapon()
            .characterId(UPDATED_CHARACTER_ID)
            .hand(UPDATED_HAND);
        return characterEquippedWeapon;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CharacterEquippedWeapon.class).block();
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
        characterEquippedWeapon = createEntity(em);
    }

    @Test
    void createCharacterEquippedWeapon() throws Exception {
        int databaseSizeBeforeCreate = characterEquippedWeaponRepository.findAll().collectList().block().size();
        // Create the CharacterEquippedWeapon
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeCreate + 1);
        CharacterEquippedWeapon testCharacterEquippedWeapon = characterEquippedWeaponList.get(characterEquippedWeaponList.size() - 1);
        assertThat(testCharacterEquippedWeapon.getCharacterId()).isEqualTo(DEFAULT_CHARACTER_ID);
        assertThat(testCharacterEquippedWeapon.getHand()).isEqualTo(DEFAULT_HAND);
    }

    @Test
    void createCharacterEquippedWeaponWithExistingId() throws Exception {
        // Create the CharacterEquippedWeapon with an existing ID
        characterEquippedWeapon.setId(1L);
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);

        int databaseSizeBeforeCreate = characterEquippedWeaponRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCharacterEquippedWeapons() {
        // Initialize the database
        characterEquippedWeaponRepository.save(characterEquippedWeapon).block();

        // Get all the characterEquippedWeaponList
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
            .value(hasItem(characterEquippedWeapon.getId().intValue()))
            .jsonPath("$.[*].characterId")
            .value(hasItem(DEFAULT_CHARACTER_ID.intValue()))
            .jsonPath("$.[*].hand")
            .value(hasItem(DEFAULT_HAND.toString()));
    }

    @Test
    void getCharacterEquippedWeapon() {
        // Initialize the database
        characterEquippedWeaponRepository.save(characterEquippedWeapon).block();

        // Get the characterEquippedWeapon
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, characterEquippedWeapon.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(characterEquippedWeapon.getId().intValue()))
            .jsonPath("$.characterId")
            .value(is(DEFAULT_CHARACTER_ID.intValue()))
            .jsonPath("$.hand")
            .value(is(DEFAULT_HAND.toString()));
    }

    @Test
    void getNonExistingCharacterEquippedWeapon() {
        // Get the characterEquippedWeapon
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCharacterEquippedWeapon() throws Exception {
        // Initialize the database
        characterEquippedWeaponRepository.save(characterEquippedWeapon).block();

        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().collectList().block().size();

        // Update the characterEquippedWeapon
        CharacterEquippedWeapon updatedCharacterEquippedWeapon = characterEquippedWeaponRepository
            .findById(characterEquippedWeapon.getId())
            .block();
        updatedCharacterEquippedWeapon.characterId(UPDATED_CHARACTER_ID).hand(UPDATED_HAND);
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(updatedCharacterEquippedWeapon);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, characterEquippedWeaponDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
        CharacterEquippedWeapon testCharacterEquippedWeapon = characterEquippedWeaponList.get(characterEquippedWeaponList.size() - 1);
        assertThat(testCharacterEquippedWeapon.getCharacterId()).isEqualTo(UPDATED_CHARACTER_ID);
        assertThat(testCharacterEquippedWeapon.getHand()).isEqualTo(UPDATED_HAND);
    }

    @Test
    void putNonExistingCharacterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().collectList().block().size();
        characterEquippedWeapon.setId(count.incrementAndGet());

        // Create the CharacterEquippedWeapon
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, characterEquippedWeaponDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCharacterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().collectList().block().size();
        characterEquippedWeapon.setId(count.incrementAndGet());

        // Create the CharacterEquippedWeapon
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCharacterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().collectList().block().size();
        characterEquippedWeapon.setId(count.incrementAndGet());

        // Create the CharacterEquippedWeapon
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCharacterEquippedWeaponWithPatch() throws Exception {
        // Initialize the database
        characterEquippedWeaponRepository.save(characterEquippedWeapon).block();

        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().collectList().block().size();

        // Update the characterEquippedWeapon using partial update
        CharacterEquippedWeapon partialUpdatedCharacterEquippedWeapon = new CharacterEquippedWeapon();
        partialUpdatedCharacterEquippedWeapon.setId(characterEquippedWeapon.getId());

        partialUpdatedCharacterEquippedWeapon.characterId(UPDATED_CHARACTER_ID).hand(UPDATED_HAND);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCharacterEquippedWeapon.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterEquippedWeapon))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
        CharacterEquippedWeapon testCharacterEquippedWeapon = characterEquippedWeaponList.get(characterEquippedWeaponList.size() - 1);
        assertThat(testCharacterEquippedWeapon.getCharacterId()).isEqualTo(UPDATED_CHARACTER_ID);
        assertThat(testCharacterEquippedWeapon.getHand()).isEqualTo(UPDATED_HAND);
    }

    @Test
    void fullUpdateCharacterEquippedWeaponWithPatch() throws Exception {
        // Initialize the database
        characterEquippedWeaponRepository.save(characterEquippedWeapon).block();

        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().collectList().block().size();

        // Update the characterEquippedWeapon using partial update
        CharacterEquippedWeapon partialUpdatedCharacterEquippedWeapon = new CharacterEquippedWeapon();
        partialUpdatedCharacterEquippedWeapon.setId(characterEquippedWeapon.getId());

        partialUpdatedCharacterEquippedWeapon.characterId(UPDATED_CHARACTER_ID).hand(UPDATED_HAND);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCharacterEquippedWeapon.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterEquippedWeapon))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
        CharacterEquippedWeapon testCharacterEquippedWeapon = characterEquippedWeaponList.get(characterEquippedWeaponList.size() - 1);
        assertThat(testCharacterEquippedWeapon.getCharacterId()).isEqualTo(UPDATED_CHARACTER_ID);
        assertThat(testCharacterEquippedWeapon.getHand()).isEqualTo(UPDATED_HAND);
    }

    @Test
    void patchNonExistingCharacterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().collectList().block().size();
        characterEquippedWeapon.setId(count.incrementAndGet());

        // Create the CharacterEquippedWeapon
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, characterEquippedWeaponDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCharacterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().collectList().block().size();
        characterEquippedWeapon.setId(count.incrementAndGet());

        // Create the CharacterEquippedWeapon
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCharacterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().collectList().block().size();
        characterEquippedWeapon.setId(count.incrementAndGet());

        // Create the CharacterEquippedWeapon
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCharacterEquippedWeapon() {
        // Initialize the database
        characterEquippedWeaponRepository.save(characterEquippedWeapon).block();

        int databaseSizeBeforeDelete = characterEquippedWeaponRepository.findAll().collectList().block().size();

        // Delete the characterEquippedWeapon
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, characterEquippedWeapon.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
