package com.adi.cms.character.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.character.IntegrationTest;
import com.adi.cms.character.domain.CharacterSkill;
import com.adi.cms.character.repository.CharacterSkillRepository;
import com.adi.cms.character.service.dto.CharacterSkillDTO;
import com.adi.cms.character.service.mapper.CharacterSkillMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CharacterSkillResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restCharacterSkillMockMvc;

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

    @BeforeEach
    public void initTest() {
        characterSkill = createEntity(em);
    }

    @Test
    @Transactional
    void createCharacterSkill() throws Exception {
        int databaseSizeBeforeCreate = characterSkillRepository.findAll().size();
        // Create the CharacterSkill
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);
        restCharacterSkillMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeCreate + 1);
        CharacterSkill testCharacterSkill = characterSkillList.get(characterSkillList.size() - 1);
        assertThat(testCharacterSkill.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testCharacterSkill.getSkillModifier()).isEqualTo(DEFAULT_SKILL_MODIFIER);
    }

    @Test
    @Transactional
    void createCharacterSkillWithExistingId() throws Exception {
        // Create the CharacterSkill with an existing ID
        characterSkill.setId(1L);
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        int databaseSizeBeforeCreate = characterSkillRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCharacterSkillMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterSkillRepository.findAll().size();
        // set the field null
        characterSkill.setPoints(null);

        // Create the CharacterSkill, which fails.
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        restCharacterSkillMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            )
            .andExpect(status().isBadRequest());

        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCharacterSkills() throws Exception {
        // Initialize the database
        characterSkillRepository.saveAndFlush(characterSkill);

        // Get all the characterSkillList
        restCharacterSkillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(characterSkill.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].skillModifier").value(hasItem(DEFAULT_SKILL_MODIFIER)));
    }

    @Test
    @Transactional
    void getCharacterSkill() throws Exception {
        // Initialize the database
        characterSkillRepository.saveAndFlush(characterSkill);

        // Get the characterSkill
        restCharacterSkillMockMvc
            .perform(get(ENTITY_API_URL_ID, characterSkill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(characterSkill.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.skillModifier").value(DEFAULT_SKILL_MODIFIER));
    }

    @Test
    @Transactional
    void getNonExistingCharacterSkill() throws Exception {
        // Get the characterSkill
        restCharacterSkillMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCharacterSkill() throws Exception {
        // Initialize the database
        characterSkillRepository.saveAndFlush(characterSkill);

        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().size();

        // Update the characterSkill
        CharacterSkill updatedCharacterSkill = characterSkillRepository.findById(characterSkill.getId()).get();
        // Disconnect from session so that the updates on updatedCharacterSkill are not directly saved in db
        em.detach(updatedCharacterSkill);
        updatedCharacterSkill.points(UPDATED_POINTS).skillModifier(UPDATED_SKILL_MODIFIER);
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(updatedCharacterSkill);

        restCharacterSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, characterSkillDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            )
            .andExpect(status().isOk());

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
        CharacterSkill testCharacterSkill = characterSkillList.get(characterSkillList.size() - 1);
        assertThat(testCharacterSkill.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacterSkill.getSkillModifier()).isEqualTo(UPDATED_SKILL_MODIFIER);
    }

    @Test
    @Transactional
    void putNonExistingCharacterSkill() throws Exception {
        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().size();
        characterSkill.setId(count.incrementAndGet());

        // Create the CharacterSkill
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacterSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, characterSkillDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCharacterSkill() throws Exception {
        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().size();
        characterSkill.setId(count.incrementAndGet());

        // Create the CharacterSkill
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCharacterSkill() throws Exception {
        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().size();
        characterSkill.setId(count.incrementAndGet());

        // Create the CharacterSkill
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterSkillMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCharacterSkillWithPatch() throws Exception {
        // Initialize the database
        characterSkillRepository.saveAndFlush(characterSkill);

        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().size();

        // Update the characterSkill using partial update
        CharacterSkill partialUpdatedCharacterSkill = new CharacterSkill();
        partialUpdatedCharacterSkill.setId(characterSkill.getId());

        partialUpdatedCharacterSkill.points(UPDATED_POINTS).skillModifier(UPDATED_SKILL_MODIFIER);

        restCharacterSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacterSkill.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterSkill))
            )
            .andExpect(status().isOk());

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
        CharacterSkill testCharacterSkill = characterSkillList.get(characterSkillList.size() - 1);
        assertThat(testCharacterSkill.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacterSkill.getSkillModifier()).isEqualTo(UPDATED_SKILL_MODIFIER);
    }

    @Test
    @Transactional
    void fullUpdateCharacterSkillWithPatch() throws Exception {
        // Initialize the database
        characterSkillRepository.saveAndFlush(characterSkill);

        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().size();

        // Update the characterSkill using partial update
        CharacterSkill partialUpdatedCharacterSkill = new CharacterSkill();
        partialUpdatedCharacterSkill.setId(characterSkill.getId());

        partialUpdatedCharacterSkill.points(UPDATED_POINTS).skillModifier(UPDATED_SKILL_MODIFIER);

        restCharacterSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacterSkill.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterSkill))
            )
            .andExpect(status().isOk());

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
        CharacterSkill testCharacterSkill = characterSkillList.get(characterSkillList.size() - 1);
        assertThat(testCharacterSkill.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacterSkill.getSkillModifier()).isEqualTo(UPDATED_SKILL_MODIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingCharacterSkill() throws Exception {
        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().size();
        characterSkill.setId(count.incrementAndGet());

        // Create the CharacterSkill
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacterSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, characterSkillDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCharacterSkill() throws Exception {
        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().size();
        characterSkill.setId(count.incrementAndGet());

        // Create the CharacterSkill
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCharacterSkill() throws Exception {
        int databaseSizeBeforeUpdate = characterSkillRepository.findAll().size();
        characterSkill.setId(count.incrementAndGet());

        // Create the CharacterSkill
        CharacterSkillDTO characterSkillDTO = characterSkillMapper.toDto(characterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterSkillMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(characterSkillDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CharacterSkill in the database
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCharacterSkill() throws Exception {
        // Initialize the database
        characterSkillRepository.saveAndFlush(characterSkill);

        int databaseSizeBeforeDelete = characterSkillRepository.findAll().size();

        // Delete the characterSkill
        restCharacterSkillMockMvc
            .perform(delete(ENTITY_API_URL_ID, characterSkill.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CharacterSkill> characterSkillList = characterSkillRepository.findAll();
        assertThat(characterSkillList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
