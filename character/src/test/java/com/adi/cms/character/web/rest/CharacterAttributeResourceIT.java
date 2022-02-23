package com.adi.cms.character.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.character.IntegrationTest;
import com.adi.cms.character.domain.CharacterAttribute;
import com.adi.cms.character.domain.enumeration.AttributeName;
import com.adi.cms.character.repository.CharacterAttributeRepository;
import com.adi.cms.character.service.dto.CharacterAttributeDTO;
import com.adi.cms.character.service.mapper.CharacterAttributeMapper;
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
 * Integration tests for the {@link CharacterAttributeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restCharacterAttributeMockMvc;

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

    @BeforeEach
    public void initTest() {
        characterAttribute = createEntity(em);
    }

    @Test
    @Transactional
    void createCharacterAttribute() throws Exception {
        int databaseSizeBeforeCreate = characterAttributeRepository.findAll().size();
        // Create the CharacterAttribute
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);
        restCharacterAttributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeCreate + 1);
        CharacterAttribute testCharacterAttribute = characterAttributeList.get(characterAttributeList.size() - 1);
        assertThat(testCharacterAttribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCharacterAttribute.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testCharacterAttribute.getAttributeModifier()).isEqualTo(DEFAULT_ATTRIBUTE_MODIFIER);
    }

    @Test
    @Transactional
    void createCharacterAttributeWithExistingId() throws Exception {
        // Create the CharacterAttribute with an existing ID
        characterAttribute.setId(1L);
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        int databaseSizeBeforeCreate = characterAttributeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCharacterAttributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterAttributeRepository.findAll().size();
        // set the field null
        characterAttribute.setName(null);

        // Create the CharacterAttribute, which fails.
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        restCharacterAttributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterAttributeRepository.findAll().size();
        // set the field null
        characterAttribute.setPoints(null);

        // Create the CharacterAttribute, which fails.
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        restCharacterAttributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCharacterAttributes() throws Exception {
        // Initialize the database
        characterAttributeRepository.saveAndFlush(characterAttribute);

        // Get all the characterAttributeList
        restCharacterAttributeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(characterAttribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].attributeModifier").value(hasItem(DEFAULT_ATTRIBUTE_MODIFIER)));
    }

    @Test
    @Transactional
    void getCharacterAttribute() throws Exception {
        // Initialize the database
        characterAttributeRepository.saveAndFlush(characterAttribute);

        // Get the characterAttribute
        restCharacterAttributeMockMvc
            .perform(get(ENTITY_API_URL_ID, characterAttribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(characterAttribute.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.attributeModifier").value(DEFAULT_ATTRIBUTE_MODIFIER));
    }

    @Test
    @Transactional
    void getNonExistingCharacterAttribute() throws Exception {
        // Get the characterAttribute
        restCharacterAttributeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCharacterAttribute() throws Exception {
        // Initialize the database
        characterAttributeRepository.saveAndFlush(characterAttribute);

        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().size();

        // Update the characterAttribute
        CharacterAttribute updatedCharacterAttribute = characterAttributeRepository.findById(characterAttribute.getId()).get();
        // Disconnect from session so that the updates on updatedCharacterAttribute are not directly saved in db
        em.detach(updatedCharacterAttribute);
        updatedCharacterAttribute.name(UPDATED_NAME).points(UPDATED_POINTS).attributeModifier(UPDATED_ATTRIBUTE_MODIFIER);
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(updatedCharacterAttribute);

        restCharacterAttributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, characterAttributeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
        CharacterAttribute testCharacterAttribute = characterAttributeList.get(characterAttributeList.size() - 1);
        assertThat(testCharacterAttribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCharacterAttribute.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacterAttribute.getAttributeModifier()).isEqualTo(UPDATED_ATTRIBUTE_MODIFIER);
    }

    @Test
    @Transactional
    void putNonExistingCharacterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().size();
        characterAttribute.setId(count.incrementAndGet());

        // Create the CharacterAttribute
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacterAttributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, characterAttributeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCharacterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().size();
        characterAttribute.setId(count.incrementAndGet());

        // Create the CharacterAttribute
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterAttributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCharacterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().size();
        characterAttribute.setId(count.incrementAndGet());

        // Create the CharacterAttribute
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterAttributeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCharacterAttributeWithPatch() throws Exception {
        // Initialize the database
        characterAttributeRepository.saveAndFlush(characterAttribute);

        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().size();

        // Update the characterAttribute using partial update
        CharacterAttribute partialUpdatedCharacterAttribute = new CharacterAttribute();
        partialUpdatedCharacterAttribute.setId(characterAttribute.getId());

        partialUpdatedCharacterAttribute.points(UPDATED_POINTS).attributeModifier(UPDATED_ATTRIBUTE_MODIFIER);

        restCharacterAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacterAttribute.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterAttribute))
            )
            .andExpect(status().isOk());

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
        CharacterAttribute testCharacterAttribute = characterAttributeList.get(characterAttributeList.size() - 1);
        assertThat(testCharacterAttribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCharacterAttribute.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacterAttribute.getAttributeModifier()).isEqualTo(UPDATED_ATTRIBUTE_MODIFIER);
    }

    @Test
    @Transactional
    void fullUpdateCharacterAttributeWithPatch() throws Exception {
        // Initialize the database
        characterAttributeRepository.saveAndFlush(characterAttribute);

        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().size();

        // Update the characterAttribute using partial update
        CharacterAttribute partialUpdatedCharacterAttribute = new CharacterAttribute();
        partialUpdatedCharacterAttribute.setId(characterAttribute.getId());

        partialUpdatedCharacterAttribute.name(UPDATED_NAME).points(UPDATED_POINTS).attributeModifier(UPDATED_ATTRIBUTE_MODIFIER);

        restCharacterAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacterAttribute.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterAttribute))
            )
            .andExpect(status().isOk());

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
        CharacterAttribute testCharacterAttribute = characterAttributeList.get(characterAttributeList.size() - 1);
        assertThat(testCharacterAttribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCharacterAttribute.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacterAttribute.getAttributeModifier()).isEqualTo(UPDATED_ATTRIBUTE_MODIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingCharacterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().size();
        characterAttribute.setId(count.incrementAndGet());

        // Create the CharacterAttribute
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacterAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, characterAttributeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCharacterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().size();
        characterAttribute.setId(count.incrementAndGet());

        // Create the CharacterAttribute
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCharacterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = characterAttributeRepository.findAll().size();
        characterAttribute.setId(count.incrementAndGet());

        // Create the CharacterAttribute
        CharacterAttributeDTO characterAttributeDTO = characterAttributeMapper.toDto(characterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(characterAttributeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CharacterAttribute in the database
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCharacterAttribute() throws Exception {
        // Initialize the database
        characterAttributeRepository.saveAndFlush(characterAttribute);

        int databaseSizeBeforeDelete = characterAttributeRepository.findAll().size();

        // Delete the characterAttribute
        restCharacterAttributeMockMvc
            .perform(delete(ENTITY_API_URL_ID, characterAttribute.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CharacterAttribute> characterAttributeList = characterAttributeRepository.findAll();
        assertThat(characterAttributeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
