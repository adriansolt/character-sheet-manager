package com.adi.cms.item.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.item.IntegrationTest;
import com.adi.cms.item.domain.CharacterEquippedWeapon;
import com.adi.cms.item.domain.enumeration.Handedness;
import com.adi.cms.item.repository.CharacterEquippedWeaponRepository;
import com.adi.cms.item.service.dto.CharacterEquippedWeaponDTO;
import com.adi.cms.item.service.mapper.CharacterEquippedWeaponMapper;
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
 * Integration tests for the {@link CharacterEquippedWeaponResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CharacterEquippedWeaponResourceIT {

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
    private MockMvc restCharacterEquippedWeaponMockMvc;

    private CharacterEquippedWeapon characterEquippedWeapon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharacterEquippedWeapon createEntity(EntityManager em) {
        CharacterEquippedWeapon characterEquippedWeapon = new CharacterEquippedWeapon().hand(DEFAULT_HAND);
        return characterEquippedWeapon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharacterEquippedWeapon createUpdatedEntity(EntityManager em) {
        CharacterEquippedWeapon characterEquippedWeapon = new CharacterEquippedWeapon().hand(UPDATED_HAND);
        return characterEquippedWeapon;
    }

    @BeforeEach
    public void initTest() {
        characterEquippedWeapon = createEntity(em);
    }

    @Test
    @Transactional
    void createCharacterEquippedWeapon() throws Exception {
        int databaseSizeBeforeCreate = characterEquippedWeaponRepository.findAll().size();
        // Create the CharacterEquippedWeapon
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);
        restCharacterEquippedWeaponMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeCreate + 1);
        CharacterEquippedWeapon testCharacterEquippedWeapon = characterEquippedWeaponList.get(characterEquippedWeaponList.size() - 1);
        assertThat(testCharacterEquippedWeapon.getHand()).isEqualTo(DEFAULT_HAND);
    }

    @Test
    @Transactional
    void createCharacterEquippedWeaponWithExistingId() throws Exception {
        // Create the CharacterEquippedWeapon with an existing ID
        characterEquippedWeapon.setId(1L);
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);

        int databaseSizeBeforeCreate = characterEquippedWeaponRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCharacterEquippedWeaponMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCharacterEquippedWeapons() throws Exception {
        // Initialize the database
        characterEquippedWeaponRepository.saveAndFlush(characterEquippedWeapon);

        // Get all the characterEquippedWeaponList
        restCharacterEquippedWeaponMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(characterEquippedWeapon.getId().intValue())))
            .andExpect(jsonPath("$.[*].hand").value(hasItem(DEFAULT_HAND.toString())));
    }

    @Test
    @Transactional
    void getCharacterEquippedWeapon() throws Exception {
        // Initialize the database
        characterEquippedWeaponRepository.saveAndFlush(characterEquippedWeapon);

        // Get the characterEquippedWeapon
        restCharacterEquippedWeaponMockMvc
            .perform(get(ENTITY_API_URL_ID, characterEquippedWeapon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(characterEquippedWeapon.getId().intValue()))
            .andExpect(jsonPath("$.hand").value(DEFAULT_HAND.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCharacterEquippedWeapon() throws Exception {
        // Get the characterEquippedWeapon
        restCharacterEquippedWeaponMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCharacterEquippedWeapon() throws Exception {
        // Initialize the database
        characterEquippedWeaponRepository.saveAndFlush(characterEquippedWeapon);

        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().size();

        // Update the characterEquippedWeapon
        CharacterEquippedWeapon updatedCharacterEquippedWeapon = characterEquippedWeaponRepository
            .findById(characterEquippedWeapon.getId())
            .get();
        // Disconnect from session so that the updates on updatedCharacterEquippedWeapon are not directly saved in db
        em.detach(updatedCharacterEquippedWeapon);
        updatedCharacterEquippedWeapon.hand(UPDATED_HAND);
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(updatedCharacterEquippedWeapon);

        restCharacterEquippedWeaponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, characterEquippedWeaponDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            )
            .andExpect(status().isOk());

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
        CharacterEquippedWeapon testCharacterEquippedWeapon = characterEquippedWeaponList.get(characterEquippedWeaponList.size() - 1);
        assertThat(testCharacterEquippedWeapon.getHand()).isEqualTo(UPDATED_HAND);
    }

    @Test
    @Transactional
    void putNonExistingCharacterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().size();
        characterEquippedWeapon.setId(count.incrementAndGet());

        // Create the CharacterEquippedWeapon
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacterEquippedWeaponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, characterEquippedWeaponDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCharacterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().size();
        characterEquippedWeapon.setId(count.incrementAndGet());

        // Create the CharacterEquippedWeapon
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterEquippedWeaponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCharacterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().size();
        characterEquippedWeapon.setId(count.incrementAndGet());

        // Create the CharacterEquippedWeapon
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterEquippedWeaponMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCharacterEquippedWeaponWithPatch() throws Exception {
        // Initialize the database
        characterEquippedWeaponRepository.saveAndFlush(characterEquippedWeapon);

        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().size();

        // Update the characterEquippedWeapon using partial update
        CharacterEquippedWeapon partialUpdatedCharacterEquippedWeapon = new CharacterEquippedWeapon();
        partialUpdatedCharacterEquippedWeapon.setId(characterEquippedWeapon.getId());

        partialUpdatedCharacterEquippedWeapon.hand(UPDATED_HAND);

        restCharacterEquippedWeaponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacterEquippedWeapon.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterEquippedWeapon))
            )
            .andExpect(status().isOk());

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
        CharacterEquippedWeapon testCharacterEquippedWeapon = characterEquippedWeaponList.get(characterEquippedWeaponList.size() - 1);
        assertThat(testCharacterEquippedWeapon.getHand()).isEqualTo(UPDATED_HAND);
    }

    @Test
    @Transactional
    void fullUpdateCharacterEquippedWeaponWithPatch() throws Exception {
        // Initialize the database
        characterEquippedWeaponRepository.saveAndFlush(characterEquippedWeapon);

        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().size();

        // Update the characterEquippedWeapon using partial update
        CharacterEquippedWeapon partialUpdatedCharacterEquippedWeapon = new CharacterEquippedWeapon();
        partialUpdatedCharacterEquippedWeapon.setId(characterEquippedWeapon.getId());

        partialUpdatedCharacterEquippedWeapon.hand(UPDATED_HAND);

        restCharacterEquippedWeaponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacterEquippedWeapon.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacterEquippedWeapon))
            )
            .andExpect(status().isOk());

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
        CharacterEquippedWeapon testCharacterEquippedWeapon = characterEquippedWeaponList.get(characterEquippedWeaponList.size() - 1);
        assertThat(testCharacterEquippedWeapon.getHand()).isEqualTo(UPDATED_HAND);
    }

    @Test
    @Transactional
    void patchNonExistingCharacterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().size();
        characterEquippedWeapon.setId(count.incrementAndGet());

        // Create the CharacterEquippedWeapon
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacterEquippedWeaponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, characterEquippedWeaponDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCharacterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().size();
        characterEquippedWeapon.setId(count.incrementAndGet());

        // Create the CharacterEquippedWeapon
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterEquippedWeaponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCharacterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = characterEquippedWeaponRepository.findAll().size();
        characterEquippedWeapon.setId(count.incrementAndGet());

        // Create the CharacterEquippedWeapon
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = characterEquippedWeaponMapper.toDto(characterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterEquippedWeaponMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(characterEquippedWeaponDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CharacterEquippedWeapon in the database
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCharacterEquippedWeapon() throws Exception {
        // Initialize the database
        characterEquippedWeaponRepository.saveAndFlush(characterEquippedWeapon);

        int databaseSizeBeforeDelete = characterEquippedWeaponRepository.findAll().size();

        // Delete the characterEquippedWeapon
        restCharacterEquippedWeaponMockMvc
            .perform(delete(ENTITY_API_URL_ID, characterEquippedWeapon.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CharacterEquippedWeapon> characterEquippedWeaponList = characterEquippedWeaponRepository.findAll();
        assertThat(characterEquippedWeaponList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
