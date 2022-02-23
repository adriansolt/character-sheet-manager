package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.CharacterSkill;
import com.adi.cms.gateway.repository.CharacterSkillRepository;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.service.dto.CharacterSkillDTO;
import com.adi.cms.gateway.service.mapper.CharacterSkillMapper;
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
 * Integration tests for the {@link CharacterSkillResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CharacterSkillResourceIT {

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final Integer DEFAULT_SKILL_MODIFIER = 1;
    private static final Integer UPDATED_SKILL_MODIFIER = 2;

    private static final String ENTITY_API_URL = "/api/character-skills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CharacterSkillRepository characterSkillRepository;

    @Autowired
    private CharacterSkillMapper characterSkillMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CharacterSkill characterSkill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharacterSkill createEntity(EntityManager em) {
        CharacterSkill characterSkill = new CharacterSkill().points(DEFAULT_POINTS).skillModifier(DEFAULT_SKILL_MODIFIER);
        return characterSkill;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharacterSkill createUpdatedEntity(EntityManager em) {
        CharacterSkill characterSkill = new CharacterSkill().points(UPDATED_POINTS).skillModifier(UPDATED_SKILL_MODIFIER);
        return characterSkill;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CharacterSkill.class).block();
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
        characterSkill = createEntity(em);
    }

    @Test
    void createCharacterSkill() throws Exception {
        int databaseSizeBeforeCreate = characterSkillRepository.findAll().collectList().block().size();
        // Create the CharacterSkill
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll().collectList().block();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeCreate + 1);
        CharacterSkill testCharacterSkill = characterSkillList.get(characterSkillList.size() - 1);
        assertThat(testCharacterSkill.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testCharacterSkill.getSkillModifier()).isEqualTo(DEFAULT_SKILL_MODIFIER);
    }

    @Test
    void createCharacterSkillWithExistingId() throws Exception {
        // Create the CharacterSkill with an existing ID
        characterSkill.setId(1L);
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        int databaseSizeBeforeCreate = characterSkillRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll().collectList().block();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterSkillRepository.findAll().collectList().block().size();
        // set the field null
        characterSkill.setPoints(null);

        // Create the CharacterSkill, which fails.
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll().collectList().block();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCharacterSkills() {
        // Initialize the database
        characterSkillRepository.save(characterSkill).block();

        // Get all the characterSkillList
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
            .value(hasItem(characterSkill.getId().intValue()))
            .jsonPath("$.[*].points")
            .value(hasItem(DEFAULT_POINTS))
            .jsonPath("$.[*].skillModifier")
            .value(hasItem(DEFAULT_SKILL_MODIFIER));
    }

    @Test
    void getCharacterSkill() {
        // Initialize the database
        characterSkillRepository.save(characterSkill).block();

        // Get the characterSkill
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, characterSkill.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(characterSkill.getId().intValue()))
            .jsonPath("$.points")
            .value(is(DEFAULT_POINTS))
            .jsonPath("$.skillModifier")
            .value(is(DEFAULT_SKILL_MODIFIER));
    }

    @Test
    void getNonExistingCharacterSkill() {
        // Get the characterSkill
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCharacterSkill() throws Exception {
        // Initialize the database
        characterSkillRepository.save(characterSkill).block();

        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().collectList().block().size();

        // Update the characterSkill
        CharacterSkill updatedCharacterSkill = characterSkillRepository.findById(characterSkill.getId()).block();
        updatedCharacterSkill.points(UPDATED_POINTS).skillModifier(UPDATED_SKILL_MODIFIER);
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(updatedCharacterSkill);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, characterSkillDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll().collectList().block();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
        CharacterSkill testCharacterSkill = characterSkillList.get(characterSkillList.size() - 1);
        assertThat(testCharacterSkill.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacterSkill.getSkillModifier()).isEqualTo(UPDATED_SKILL_MODIFIER);
    }

    @Test
    void putNonExistingCharacterSkill() throws Exception {
        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().collectList().block().size();
        characterSkill.setId(count.incrementAndGet());

        // Create the CharacterSkill
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, characterSkillDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll().collectList().block();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCharacterSkill() throws Exception {
        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().collectList().block().size();
        characterSkill.setId(count.incrementAndGet());

        // Create the CharacterSkill
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll().collectList().block();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCharacterSkill() throws Exception {
        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().collectList().block().size();
        characterSkill.setId(count.incrementAndGet());

        // Create the CharacterSkill
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll().collectList().block();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCharacterSkillWithPatch() throws Exception {
        // Initialize the database
        characterSkillRepository.save(characterSkill).block();

        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().collectList().block().size();

        // Update the characterSkill using partial update
        CharacterSkill partialUpdatedCharacterSkill = new CharacterSkill();
        partialUpdatedCharacterSkill.setId(characterSkill.getId());

        partialUpdatedCharacterSkill.points(UPDATED_POINTS).skillModifier(UPDATED_SKILL_MODIFIER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCharacterSkill.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterSkill))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll().collectList().block();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
        CharacterSkill testCharacterSkill = characterSkillList.get(characterSkillList.size() - 1);
        assertThat(testCharacterSkill.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacterSkill.getSkillModifier()).isEqualTo(UPDATED_SKILL_MODIFIER);
    }

    @Test
    void fullUpdateCharacterSkillWithPatch() throws Exception {
        // Initialize the database
        characterSkillRepository.save(characterSkill).block();

        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().collectList().block().size();

        // Update the characterSkill using partial update
        CharacterSkill partialUpdatedCharacterSkill = new CharacterSkill();
        partialUpdatedCharacterSkill.setId(characterSkill.getId());

        partialUpdatedCharacterSkill.points(UPDATED_POINTS).skillModifier(UPDATED_SKILL_MODIFIER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCharacterSkill.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterSkill))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll().collectList().block();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
        CharacterSkill testCharacterSkill = characterSkillList.get(characterSkillList.size() - 1);
        assertThat(testCharacterSkill.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacterSkill.getSkillModifier()).isEqualTo(UPDATED_SKILL_MODIFIER);
    }

    @Test
    void patchNonExistingCharacterSkill() throws Exception {
        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().collectList().block().size();
        characterSkill.setId(count.incrementAndGet());

        // Create the CharacterSkill
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, characterSkillDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll().collectList().block();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCharacterSkill() throws Exception {
        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().collectList().block().size();
        characterSkill.setId(count.incrementAndGet());

        // Create the CharacterSkill
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll().collectList().block();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCharacterSkill() throws Exception {
        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().collectList().block().size();
        characterSkill.setId(count.incrementAndGet());

        // Create the CharacterSkill
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll().collectList().block();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCharacterSkill() {
        // Initialize the database
        characterSkillRepository.save(characterSkill).block();

        int databaseSizeBeforeDelete = characterSkillRepository.findAll().collectList().block().size();

        // Delete the characterSkill
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, characterSkill.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll().collectList().block();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
