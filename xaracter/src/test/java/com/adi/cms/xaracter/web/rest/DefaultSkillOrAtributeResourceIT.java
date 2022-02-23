package com.adi.cms.xaracter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.xaracter.IntegrationTest;
import com.adi.cms.xaracter.domain.DefaultSkillOrAtribute;
import com.adi.cms.xaracter.repository.DefaultSkillOrAtributeRepository;
import com.adi.cms.xaracter.service.dto.DefaultSkillOrAtributeDTO;
import com.adi.cms.xaracter.service.mapper.DefaultSkillOrAtributeMapper;
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
 * Integration tests for the {@link DefaultSkillOrAtributeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DefaultSkillOrAtributeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_MODIFIER = 1;
    private static final Integer UPDATED_MODIFIER = 2;

    private static final String ENTITY_API_URL = "/api/default-skill-or-atributes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DefaultSkillOrAtributeRepository defaultSkillOrAtributeRepository;

    @Autowired
    private DefaultSkillOrAtributeMapper defaultSkillOrAtributeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDefaultSkillOrAtributeMockMvc;

    private DefaultSkillOrAtribute defaultSkillOrAtribute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DefaultSkillOrAtribute createEntity(EntityManager em) {
        DefaultSkillOrAtribute defaultSkillOrAtribute = new DefaultSkillOrAtribute().name(DEFAULT_NAME).modifier(DEFAULT_MODIFIER);
        return defaultSkillOrAtribute;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DefaultSkillOrAtribute createUpdatedEntity(EntityManager em) {
        DefaultSkillOrAtribute defaultSkillOrAtribute = new DefaultSkillOrAtribute().name(UPDATED_NAME).modifier(UPDATED_MODIFIER);
        return defaultSkillOrAtribute;
    }

    @BeforeEach
    public void initTest() {
        defaultSkillOrAtribute = createEntity(em);
    }

    @Test
    @Transactional
    void createDefaultSkillOrAtribute() throws Exception {
        int databaseSizeBeforeCreate = defaultSkillOrAtributeRepository.findAll().size();
        // Create the DefaultSkillOrAtribute
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);
        restDefaultSkillOrAtributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeCreate + 1);
        DefaultSkillOrAtribute testDefaultSkillOrAtribute = defaultSkillOrAtributeList.get(defaultSkillOrAtributeList.size() - 1);
        assertThat(testDefaultSkillOrAtribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDefaultSkillOrAtribute.getModifier()).isEqualTo(DEFAULT_MODIFIER);
    }

    @Test
    @Transactional
    void createDefaultSkillOrAtributeWithExistingId() throws Exception {
        // Create the DefaultSkillOrAtribute with an existing ID
        defaultSkillOrAtribute.setId(1L);
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        int databaseSizeBeforeCreate = defaultSkillOrAtributeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDefaultSkillOrAtributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = defaultSkillOrAtributeRepository.findAll().size();
        // set the field null
        defaultSkillOrAtribute.setName(null);

        // Create the DefaultSkillOrAtribute, which fails.
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        restDefaultSkillOrAtributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = defaultSkillOrAtributeRepository.findAll().size();
        // set the field null
        defaultSkillOrAtribute.setModifier(null);

        // Create the DefaultSkillOrAtribute, which fails.
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        restDefaultSkillOrAtributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDefaultSkillOrAtributes() throws Exception {
        // Initialize the database
        defaultSkillOrAtributeRepository.saveAndFlush(defaultSkillOrAtribute);

        // Get all the defaultSkillOrAtributeList
        restDefaultSkillOrAtributeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(defaultSkillOrAtribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].modifier").value(hasItem(DEFAULT_MODIFIER)));
    }

    @Test
    @Transactional
    void getDefaultSkillOrAtribute() throws Exception {
        // Initialize the database
        defaultSkillOrAtributeRepository.saveAndFlush(defaultSkillOrAtribute);

        // Get the defaultSkillOrAtribute
        restDefaultSkillOrAtributeMockMvc
            .perform(get(ENTITY_API_URL_ID, defaultSkillOrAtribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(defaultSkillOrAtribute.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.modifier").value(DEFAULT_MODIFIER));
    }

    @Test
    @Transactional
    void getNonExistingDefaultSkillOrAtribute() throws Exception {
        // Get the defaultSkillOrAtribute
        restDefaultSkillOrAtributeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDefaultSkillOrAtribute() throws Exception {
        // Initialize the database
        defaultSkillOrAtributeRepository.saveAndFlush(defaultSkillOrAtribute);

        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().size();

        // Update the defaultSkillOrAtribute
        DefaultSkillOrAtribute updatedDefaultSkillOrAtribute = defaultSkillOrAtributeRepository
            .findById(defaultSkillOrAtribute.getId())
            .get();
        // Disconnect from session so that the updates on updatedDefaultSkillOrAtribute are not directly saved in db
        em.detach(updatedDefaultSkillOrAtribute);
        updatedDefaultSkillOrAtribute.name(UPDATED_NAME).modifier(UPDATED_MODIFIER);
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(updatedDefaultSkillOrAtribute);

        restDefaultSkillOrAtributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, defaultSkillOrAtributeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            )
            .andExpect(status().isOk());

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
        DefaultSkillOrAtribute testDefaultSkillOrAtribute = defaultSkillOrAtributeList.get(defaultSkillOrAtributeList.size() - 1);
        assertThat(testDefaultSkillOrAtribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDefaultSkillOrAtribute.getModifier()).isEqualTo(UPDATED_MODIFIER);
    }

    @Test
    @Transactional
    void putNonExistingDefaultSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().size();
        defaultSkillOrAtribute.setId(count.incrementAndGet());

        // Create the DefaultSkillOrAtribute
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDefaultSkillOrAtributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, defaultSkillOrAtributeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDefaultSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().size();
        defaultSkillOrAtribute.setId(count.incrementAndGet());

        // Create the DefaultSkillOrAtribute
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefaultSkillOrAtributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDefaultSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().size();
        defaultSkillOrAtribute.setId(count.incrementAndGet());

        // Create the DefaultSkillOrAtribute
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefaultSkillOrAtributeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDefaultSkillOrAtributeWithPatch() throws Exception {
        // Initialize the database
        defaultSkillOrAtributeRepository.saveAndFlush(defaultSkillOrAtribute);

        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().size();

        // Update the defaultSkillOrAtribute using partial update
        DefaultSkillOrAtribute partialUpdatedDefaultSkillOrAtribute = new DefaultSkillOrAtribute();
        partialUpdatedDefaultSkillOrAtribute.setId(defaultSkillOrAtribute.getId());

        partialUpdatedDefaultSkillOrAtribute.name(UPDATED_NAME).modifier(UPDATED_MODIFIER);

        restDefaultSkillOrAtributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDefaultSkillOrAtribute.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDefaultSkillOrAtribute))
            )
            .andExpect(status().isOk());

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
        DefaultSkillOrAtribute testDefaultSkillOrAtribute = defaultSkillOrAtributeList.get(defaultSkillOrAtributeList.size() - 1);
        assertThat(testDefaultSkillOrAtribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDefaultSkillOrAtribute.getModifier()).isEqualTo(UPDATED_MODIFIER);
    }

    @Test
    @Transactional
    void fullUpdateDefaultSkillOrAtributeWithPatch() throws Exception {
        // Initialize the database
        defaultSkillOrAtributeRepository.saveAndFlush(defaultSkillOrAtribute);

        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().size();

        // Update the defaultSkillOrAtribute using partial update
        DefaultSkillOrAtribute partialUpdatedDefaultSkillOrAtribute = new DefaultSkillOrAtribute();
        partialUpdatedDefaultSkillOrAtribute.setId(defaultSkillOrAtribute.getId());

        partialUpdatedDefaultSkillOrAtribute.name(UPDATED_NAME).modifier(UPDATED_MODIFIER);

        restDefaultSkillOrAtributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDefaultSkillOrAtribute.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDefaultSkillOrAtribute))
            )
            .andExpect(status().isOk());

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
        DefaultSkillOrAtribute testDefaultSkillOrAtribute = defaultSkillOrAtributeList.get(defaultSkillOrAtributeList.size() - 1);
        assertThat(testDefaultSkillOrAtribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDefaultSkillOrAtribute.getModifier()).isEqualTo(UPDATED_MODIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingDefaultSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().size();
        defaultSkillOrAtribute.setId(count.incrementAndGet());

        // Create the DefaultSkillOrAtribute
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDefaultSkillOrAtributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, defaultSkillOrAtributeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDefaultSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().size();
        defaultSkillOrAtribute.setId(count.incrementAndGet());

        // Create the DefaultSkillOrAtribute
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefaultSkillOrAtributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDefaultSkillOrAtribute() throws Exception {
        int databaseSizeBeforeUpdate = defaultSkillOrAtributeRepository.findAll().size();
        defaultSkillOrAtribute.setId(count.incrementAndGet());

        // Create the DefaultSkillOrAtribute
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefaultSkillOrAtributeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(defaultSkillOrAtributeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DefaultSkillOrAtribute in the database
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDefaultSkillOrAtribute() throws Exception {
        // Initialize the database
        defaultSkillOrAtributeRepository.saveAndFlush(defaultSkillOrAtribute);

        int databaseSizeBeforeDelete = defaultSkillOrAtributeRepository.findAll().size();

        // Delete the defaultSkillOrAtribute
        restDefaultSkillOrAtributeMockMvc
            .perform(delete(ENTITY_API_URL_ID, defaultSkillOrAtribute.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DefaultSkillOrAtribute> defaultSkillOrAtributeList = defaultSkillOrAtributeRepository.findAll();
        assertThat(defaultSkillOrAtributeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
