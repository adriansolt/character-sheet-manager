package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.Skill;
import com.adi.cms.gateway.domain.enumeration.Difficulty;
import com.adi.cms.gateway.domain.enumeration.SkillName;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.repository.SkillRepository;
import com.adi.cms.gateway.service.dto.SkillDTO;
import com.adi.cms.gateway.service.mapper.SkillMapper;
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
 * Integration tests for the {@link SkillResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SkillResourceIT {

    private static final SkillName DEFAULT_NAME = SkillName.COOKING;
    private static final SkillName UPDATED_NAME = SkillName.FARMING;

    private static final Difficulty DEFAULT_DIFFICULTY = Difficulty.EASY;
    private static final Difficulty UPDATED_DIFFICULTY = Difficulty.AVERAGE;

    private static final String ENTITY_API_URL = "/api/skills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillMapper skillMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Skill skill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Skill createEntity(EntityManager em) {
        Skill skill = new Skill().name(DEFAULT_NAME).difficulty(DEFAULT_DIFFICULTY);
        return skill;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Skill createUpdatedEntity(EntityManager em) {
        Skill skill = new Skill().name(UPDATED_NAME).difficulty(UPDATED_DIFFICULTY);
        return skill;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Skill.class).block();
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
        skill = createEntity(em);
    }

    @Test
    void createSkill() throws Exception {
        int databaseSizeBeforeCreate = skillRepository.findAll().collectList().block().size();
        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll().collectList().block();
        assertThat(skillList).hasSize(databaseSizeBeforeCreate + 1);
        Skill testSkill = skillList.get(skillList.size() - 1);
        assertThat(testSkill.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSkill.getDifficulty()).isEqualTo(DEFAULT_DIFFICULTY);
    }

    @Test
    void createSkillWithExistingId() throws Exception {
        // Create the Skill with an existing ID
        skill.setId(1L);
        SkillDTO skillDTO = skillMapper.toDto(skill);

        int databaseSizeBeforeCreate = skillRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll().collectList().block();
        assertThat(skillList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = skillRepository.findAll().collectList().block().size();
        // set the field null
        skill.setName(null);

        // Create the Skill, which fails.
        SkillDTO skillDTO = skillMapper.toDto(skill);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Skill> skillList = skillRepository.findAll().collectList().block();
        assertThat(skillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDifficultyIsRequired() throws Exception {
        int databaseSizeBeforeTest = skillRepository.findAll().collectList().block().size();
        // set the field null
        skill.setDifficulty(null);

        // Create the Skill, which fails.
        SkillDTO skillDTO = skillMapper.toDto(skill);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Skill> skillList = skillRepository.findAll().collectList().block();
        assertThat(skillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllSkills() {
        // Initialize the database
        skillRepository.save(skill).block();

        // Get all the skillList
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
            .value(hasItem(skill.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME.toString()))
            .jsonPath("$.[*].difficulty")
            .value(hasItem(DEFAULT_DIFFICULTY.toString()));
    }

    @Test
    void getSkill() {
        // Initialize the database
        skillRepository.save(skill).block();

        // Get the skill
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, skill.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(skill.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME.toString()))
            .jsonPath("$.difficulty")
            .value(is(DEFAULT_DIFFICULTY.toString()));
    }

    @Test
    void getNonExistingSkill() {
        // Get the skill
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewSkill() throws Exception {
        // Initialize the database
        skillRepository.save(skill).block();

        int databaseSizeBeforeUpdate = skillRepository.findAll().collectList().block().size();

        // Update the skill
        Skill updatedSkill = skillRepository.findById(skill.getId()).block();
        updatedSkill.name(UPDATED_NAME).difficulty(UPDATED_DIFFICULTY);
        SkillDTO skillDTO = skillMapper.toDto(updatedSkill);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, skillDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll().collectList().block();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
        Skill testSkill = skillList.get(skillList.size() - 1);
        assertThat(testSkill.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSkill.getDifficulty()).isEqualTo(UPDATED_DIFFICULTY);
    }

    @Test
    void putNonExistingSkill() throws Exception {
        int databaseSizeBeforeUpdate = skillRepository.findAll().collectList().block().size();
        skill.setId(count.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, skillDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll().collectList().block();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSkill() throws Exception {
        int databaseSizeBeforeUpdate = skillRepository.findAll().collectList().block().size();
        skill.setId(count.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll().collectList().block();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSkill() throws Exception {
        int databaseSizeBeforeUpdate = skillRepository.findAll().collectList().block().size();
        skill.setId(count.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll().collectList().block();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSkillWithPatch() throws Exception {
        // Initialize the database
        skillRepository.save(skill).block();

        int databaseSizeBeforeUpdate = skillRepository.findAll().collectList().block().size();

        // Update the skill using partial update
        Skill partialUpdatedSkill = new Skill();
        partialUpdatedSkill.setId(skill.getId());

        partialUpdatedSkill.difficulty(UPDATED_DIFFICULTY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSkill.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSkill))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll().collectList().block();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
        Skill testSkill = skillList.get(skillList.size() - 1);
        assertThat(testSkill.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSkill.getDifficulty()).isEqualTo(UPDATED_DIFFICULTY);
    }

    @Test
    void fullUpdateSkillWithPatch() throws Exception {
        // Initialize the database
        skillRepository.save(skill).block();

        int databaseSizeBeforeUpdate = skillRepository.findAll().collectList().block().size();

        // Update the skill using partial update
        Skill partialUpdatedSkill = new Skill();
        partialUpdatedSkill.setId(skill.getId());

        partialUpdatedSkill.name(UPDATED_NAME).difficulty(UPDATED_DIFFICULTY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSkill.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSkill))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll().collectList().block();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
        Skill testSkill = skillList.get(skillList.size() - 1);
        assertThat(testSkill.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSkill.getDifficulty()).isEqualTo(UPDATED_DIFFICULTY);
    }

    @Test
    void patchNonExistingSkill() throws Exception {
        int databaseSizeBeforeUpdate = skillRepository.findAll().collectList().block().size();
        skill.setId(count.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, skillDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll().collectList().block();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSkill() throws Exception {
        int databaseSizeBeforeUpdate = skillRepository.findAll().collectList().block().size();
        skill.setId(count.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll().collectList().block();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSkill() throws Exception {
        int databaseSizeBeforeUpdate = skillRepository.findAll().collectList().block().size();
        skill.setId(count.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(skillDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll().collectList().block();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSkill() {
        // Initialize the database
        skillRepository.save(skill).block();

        int databaseSizeBeforeDelete = skillRepository.findAll().collectList().block().size();

        // Delete the skill
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, skill.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Skill> skillList = skillRepository.findAll().collectList().block();
        assertThat(skillList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
