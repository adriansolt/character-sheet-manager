package com.adi.cms.character.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.character.IntegrationTest;
import com.adi.cms.character.domain.PrereqSkillOrAtribute;
import com.adi.cms.character.repository.PrereqSkillOrAtributeRepository;
import com.adi.cms.character.service.dto.PrereqSkillOrAtributeDTO;
import com.adi.cms.character.service.mapper.PrereqSkillOrAtributeMapper;
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
 * Integration tests for the {@link PrereqSkillOrAtributeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrereqSkillOrAtributeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final String ENTITY_API_URL = "/api/prereq-skill-or-atributes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrereqSkillOrAtributeRepository prereqSkillOrAtributeRepository;

    @Autowired
    private PrereqSkillOrAtributeMapper prereqSkillOrAtributeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrereqSkillOrAtributeMockMvc;

    private PrereqSkillOrAtribute prereqSkillOrAtribute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrereqSkillOrAtribute createEntity(EntityManager em) {
        PrereqSkillOrAtribute prereqSkillOrAtribute = new PrereqSkillOrAtribute().name(DEFAULT_NAME).level(DEFAULT_LEVEL);
        return prereqSkillOrAtribute;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrereqSkillOrAtribute createUpdatedEntity(EntityManager em) {
        PrereqSkillOrAtribute prereqSkillOrAtribute = new PrereqSkillOrAtribute().name(UPDATED_NAME).level(UPDATED_LEVEL);
        return prereqSkillOrAtribute;
    }

    @BeforeEach
    public void initTest() {
        prereqSkillOrAtribute = createEntity(em);
    }

    @Test
    @Transactional
    void createPrereqSkillOrAtribute() throws Exception {
        int databaseSizeBeforeCreate = prereqSkillOrAtributeRepository.findAll().size();
        // Create the PrereqSkillOrAtribute
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);
        restPrereqSkillOrAtributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeCreate + 1);
        PrereqSkillOrAtribute testPrereqSkillOrAtribute = prereqSkillOrAtributeList.get(prereqSkillOrAtributeList.size() - 1);
        assertThat(testPrereqSkillOrAtribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrereqSkillOrAtribute.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void createPrereqSkillOrAtributeWithExistingId() throws Exception {
        // Create the PrereqSkillOrAtribute with an existing ID
        prereqSkillOrAtribute.setId(1L);
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        int databaseSizeBeforeCreate = prereqSkillOrAtributeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrereqSkillOrAtributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = prereqSkillOrAtributeRepository.findAll().size();
        // set the field null
        prereqSkillOrAtribute.setName(null);

        // Create the PrereqSkillOrAtribute, which fails.
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        restPrereqSkillOrAtributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = prereqSkillOrAtributeRepository.findAll().size();
        // set the field null
        prereqSkillOrAtribute.setLevel(null);

        // Create the PrereqSkillOrAtribute, which fails.
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        restPrereqSkillOrAtributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrereqSkillOrAtributes() throws Exception {
        // Initialize the database
        prereqSkillOrAtributeRepository.saveAndFlush(prereqSkillOrAtribute);

        // Get all the prereqSkillOrAtributeList
        restPrereqSkillOrAtributeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prereqSkillOrAtribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }

    @Test
    @Transactional
    void getPrereqSkillOrAtribute() throws Exception {
        // Initialize the database
        prereqSkillOrAtributeRepository.saveAndFlush(prereqSkillOrAtribute);

        // Get the prereqSkillOrAtribute
        restPrereqSkillOrAtributeMockMvc
            .perform(get(ENTITY_API_URL_ID, prereqSkillOrAtribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prereqSkillOrAtribute.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL));
    }

    @Test
    @Transactional
    void getNonExistingPrereqSkillOrAtribute() throws Exception {
        // Get the prereqSkillOrAtribute
        restPrereqSkillOrAtributeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrereqSkillOrAtribute() throws Exception {
        // Initialize the database
        prereqSkillOrAtributeRepository.saveAndFlush(prereqSkillOrAtribute);

        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().size();

        // Update the prereqSkillOrAtribute
        PrereqSkillOrAtribute updatedPrereqSkillOrAtribute = prereqSkillOrAtributeRepository.findById(prereqSkillOrAtribute.getId()).get();
        // Disconnect from session so that the updates on updatedPrereqSkillOrAtribute are not directly saved in db
        em.detach(updatedPrereqSkillOrAtribute);
        updatedPrereqSkillOrAtribute.name(UPDATED_NAME).level(UPDATED_LEVEL);
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(updatedPrereqSkillOrAtribute);

        restPrereqSkillOrAtributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prereqSkillOrAtributeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            )
            .andExpect(status().isOk());

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
        PrereqSkillOrAtribute testPrereqSkillOrAtribute = prereqSkillOrAtributeList.get(prereqSkillOrAtributeList.size() - 1);
        assertThat(testPrereqSkillOrAtribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrereqSkillOrAtribute.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void putNonExistingPrereqSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().size();
        prereqSkillOrAtribute.setId(count.incrementAndGet());

        // Create the PrereqSkillOrAtribute
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrereqSkillOrAtributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prereqSkillOrAtributeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrereqSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().size();
        prereqSkillOrAtribute.setId(count.incrementAndGet());

        // Create the PrereqSkillOrAtribute
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrereqSkillOrAtributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrereqSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().size();
        prereqSkillOrAtribute.setId(count.incrementAndGet());

        // Create the PrereqSkillOrAtribute
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrereqSkillOrAtributeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrereqSkillOrAtributeWithPatch() throws Exception {
        // Initialize the database
        prereqSkillOrAtributeRepository.saveAndFlush(prereqSkillOrAtribute);

        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().size();

        // Update the prereqSkillOrAtribute using partial update
        PrereqSkillOrAtribute partialUpdatedPrereqSkillOrAtribute = new PrereqSkillOrAtribute();
        partialUpdatedPrereqSkillOrAtribute.setId(prereqSkillOrAtribute.getId());

        partialUpdatedPrereqSkillOrAtribute.level(UPDATED_LEVEL);

        restPrereqSkillOrAtributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrereqSkillOrAtribute.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrereqSkillOrAtribute))
            )
            .andExpect(status().isOk());

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
        PrereqSkillOrAtribute testPrereqSkillOrAtribute = prereqSkillOrAtributeList.get(prereqSkillOrAtributeList.size() - 1);
        assertThat(testPrereqSkillOrAtribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrereqSkillOrAtribute.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void fullUpdatePrereqSkillOrAtributeWithPatch() throws Exception {
        // Initialize the database
        prereqSkillOrAtributeRepository.saveAndFlush(prereqSkillOrAtribute);

        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().size();

        // Update the prereqSkillOrAtribute using partial update
        PrereqSkillOrAtribute partialUpdatedPrereqSkillOrAtribute = new PrereqSkillOrAtribute();
        partialUpdatedPrereqSkillOrAtribute.setId(prereqSkillOrAtribute.getId());

        partialUpdatedPrereqSkillOrAtribute.name(UPDATED_NAME).level(UPDATED_LEVEL);

        restPrereqSkillOrAtributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrereqSkillOrAtribute.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrereqSkillOrAtribute))
            )
            .andExpect(status().isOk());

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
        PrereqSkillOrAtribute testPrereqSkillOrAtribute = prereqSkillOrAtributeList.get(prereqSkillOrAtributeList.size() - 1);
        assertThat(testPrereqSkillOrAtribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrereqSkillOrAtribute.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void patchNonExistingPrereqSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().size();
        prereqSkillOrAtribute.setId(count.incrementAndGet());

        // Create the PrereqSkillOrAtribute
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrereqSkillOrAtributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prereqSkillOrAtributeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrereqSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().size();
        prereqSkillOrAtribute.setId(count.incrementAndGet());

        // Create the PrereqSkillOrAtribute
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrereqSkillOrAtributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrereqSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = prereqSkillOrAtributeRepository.findAll().size();
        prereqSkillOrAtribute.setId(count.incrementAndGet());

        // Create the PrereqSkillOrAtribute
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrereqSkillOrAtributeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prereqSkillOrAtributeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrereqSkillOrAtribute in the database
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrereqSkillOrAtribute() throws Exception {
        // Initialize the database
        prereqSkillOrAtributeRepository.saveAndFlush(prereqSkillOrAtribute);

        int databaseSizeBeforeDelete = prereqSkillOrAtributeRepository.findAll().size();

        // Delete the prereqSkillOrAtribute
        restPrereqSkillOrAtributeMockMvc
            .perform(delete(ENTITY_API_URL_ID, prereqSkillOrAtribute.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PrereqSkillOrAtribute> prereqSkillOrAtributeList = prereqSkillOrAtributeRepository.findAll();
        assertThat(prereqSkillOrAtributeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
