package com.adi.cms.item.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.item.IntegrationTest;
import com.adi.cms.item.domain.CharacterEquippedArmor;
import com.adi.cms.item.repository.CharacterEquippedArmorRepository;
import com.adi.cms.item.service.dto.CharacterEquippedArmorDTO;
import com.adi.cms.item.service.mapper.CharacterEquippedArmorMapper;
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
 * Integration tests for the {@link CharacterEquippedArmorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CharacterEquippedArmorResourceIT {

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
    private MockMvc restCharacterEquippedArmorMockMvc;

    private CharacterEquippedArmor characterEquippedArmor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharacterEquippedArmor createEntity(EntityManager em) {
        CharacterEquippedArmor characterEquippedArmor = new CharacterEquippedArmor();
        return characterEquippedArmor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharacterEquippedArmor createUpdatedEntity(EntityManager em) {
        CharacterEquippedArmor characterEquippedArmor = new CharacterEquippedArmor();
        return characterEquippedArmor;
    }

    @BeforeEach
    public void initTest() {
        characterEquippedArmor = createEntity(em);
    }

    @Test
    @Transactional
    void createCharacterEquippedArmor() throws Exception {
        int databaseSizeBeforeCreate = characterEquippedArmorRepository.findAll().size();
        // Create the CharacterEquippedArmor
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);
        restCharacterEquippedArmorMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeCreate + 1);
        CharacterEquippedArmor testCharacterEquippedArmor = characterEquippedArmorList.get(characterEquippedArmorList.size() - 1);
    }

    @Test
    @Transactional
    void createCharacterEquippedArmorWithExistingId() throws Exception {
        // Create the CharacterEquippedArmor with an existing ID
        characterEquippedArmor.setId(1L);
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);

        int databaseSizeBeforeCreate = characterEquippedArmorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCharacterEquippedArmorMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCharacterEquippedArmors() throws Exception {
        // Initialize the database
        characterEquippedArmorRepository.saveAndFlush(characterEquippedArmor);

        // Get all the characterEquippedArmorList
        restCharacterEquippedArmorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(characterEquippedArmor.getId().intValue())));
    }

    @Test
    @Transactional
    void getCharacterEquippedArmor() throws Exception {
        // Initialize the database
        characterEquippedArmorRepository.saveAndFlush(characterEquippedArmor);

        // Get the characterEquippedArmor
        restCharacterEquippedArmorMockMvc
            .perform(get(ENTITY_API_URL_ID, characterEquippedArmor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(characterEquippedArmor.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingCharacterEquippedArmor() throws Exception {
        // Get the characterEquippedArmor
        restCharacterEquippedArmorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCharacterEquippedArmor() throws Exception {
        // Initialize the database
        characterEquippedArmorRepository.saveAndFlush(characterEquippedArmor);

        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().size();

        // Update the characterEquippedArmor
        CharacterEquippedArmor updatedCharacterEquippedArmor = characterEquippedArmorRepository
            .findById(characterEquippedArmor.getId())
            .get();
        // Disconnect from session so that the updates on updatedCharacterEquippedArmor are not directly saved in db
        em.detach(updatedCharacterEquippedArmor);
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(updatedCharacterEquippedArmor);

        restCharacterEquippedArmorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, characterEquippedArmorDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            )
            .andExpect(status().isOk());

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
        CharacterEquippedArmor testCharacterEquippedArmor = characterEquippedArmorList.get(characterEquippedArmorList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingCharacterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().size();
        characterEquippedArmor.setId(count.incrementAndGet());

        // Create the CharacterEquippedArmor
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacterEquippedArmorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, characterEquippedArmorDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCharacterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().size();
        characterEquippedArmor.setId(count.incrementAndGet());

        // Create the CharacterEquippedArmor
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterEquippedArmorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCharacterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().size();
        characterEquippedArmor.setId(count.incrementAndGet());

        // Create the CharacterEquippedArmor
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterEquippedArmorMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCharacterEquippedArmorWithPatch() throws Exception {
        // Initialize the database
        characterEquippedArmorRepository.saveAndFlush(characterEquippedArmor);

        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().size();

        // Update the characterEquippedArmor using partial update
        CharacterEquippedArmor partialUpdatedCharacterEquippedArmor = new CharacterEquippedArmor();
        partialUpdatedCharacterEquippedArmor.setId(characterEquippedArmor.getId());

        restCharacterEquippedArmorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacterEquippedArmor.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterEquippedArmor))
            )
            .andExpect(status().isOk());

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
        CharacterEquippedArmor testCharacterEquippedArmor = characterEquippedArmorList.get(characterEquippedArmorList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateCharacterEquippedArmorWithPatch() throws Exception {
        // Initialize the database
        characterEquippedArmorRepository.saveAndFlush(characterEquippedArmor);

        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().size();

        // Update the characterEquippedArmor using partial update
        CharacterEquippedArmor partialUpdatedCharacterEquippedArmor = new CharacterEquippedArmor();
        partialUpdatedCharacterEquippedArmor.setId(characterEquippedArmor.getId());

        restCharacterEquippedArmorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacterEquippedArmor.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterEquippedArmor))
            )
            .andExpect(status().isOk());

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
        CharacterEquippedArmor testCharacterEquippedArmor = characterEquippedArmorList.get(characterEquippedArmorList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingCharacterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().size();
        characterEquippedArmor.setId(count.incrementAndGet());

        // Create the CharacterEquippedArmor
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacterEquippedArmorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, characterEquippedArmorDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCharacterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().size();
        characterEquippedArmor.setId(count.incrementAndGet());

        // Create the CharacterEquippedArmor
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterEquippedArmorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCharacterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedArmorRepository.findAll().size();
        characterEquippedArmor.setId(count.incrementAndGet());

        // Create the CharacterEquippedArmor
        CharacterEquippedArmorDTO characterEquippedArmorDTO = characterEquippedArmorMapper.toDto(characterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterEquippedArmorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedArmorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CharacterEquippedArmor in the database
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCharacterEquippedArmor() throws Exception {
        // Initialize the database
        characterEquippedArmorRepository.saveAndFlush(characterEquippedArmor);

        int databaseSizeBeforeDelete = characterEquippedArmorRepository.findAll().size();

        // Delete the characterEquippedArmor
        restCharacterEquippedArmorMockMvc
            .perform(delete(ENTITY_API_URL_ID, characterEquippedArmor.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CharacterEquippedArmor> characterEquippedArmorList = characterEquippedArmorRepository.findAll();
        assertThat(characterEquippedArmorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
