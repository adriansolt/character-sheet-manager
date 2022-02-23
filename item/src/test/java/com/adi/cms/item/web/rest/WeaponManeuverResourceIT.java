package com.adi.cms.item.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.item.IntegrationTest;
import com.adi.cms.item.domain.WeaponManeuver;
import com.adi.cms.item.repository.WeaponManeuverRepository;
import com.adi.cms.item.service.dto.WeaponManeuverDTO;
import com.adi.cms.item.service.mapper.WeaponManeuverMapper;
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
 * Integration tests for the {@link WeaponManeuverResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WeaponManeuverResourceIT {

    private static final String ENTITY_API_URL = "/api/weapon-maneuvers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WeaponManeuverRepository weaponManeuverRepository;

    @Autowired
    private WeaponManeuverMapper weaponManeuverMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWeaponManeuverMockMvc;

    private WeaponManeuver weaponManeuver;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeaponManeuver createEntity(EntityManager em) {
        WeaponManeuver weaponManeuver = new WeaponManeuver();
        return weaponManeuver;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeaponManeuver createUpdatedEntity(EntityManager em) {
        WeaponManeuver weaponManeuver = new WeaponManeuver();
        return weaponManeuver;
    }

    @BeforeEach
    public void initTest() {
        weaponManeuver = createEntity(em);
    }

    @Test
    @Transactional
    void createWeaponManeuver() throws Exception {
        int databaseSizeBeforeCreate = weaponManeuverRepository.findAll().size();
        // Create the WeaponManeuver
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);
        restWeaponManeuverMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeCreate + 1);
        WeaponManeuver testWeaponManeuver = weaponManeuverList.get(weaponManeuverList.size() - 1);
    }

    @Test
    @Transactional
    void createWeaponManeuverWithExistingId() throws Exception {
        // Create the WeaponManeuver with an existing ID
        weaponManeuver.setId(1L);
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);

        int databaseSizeBeforeCreate = weaponManeuverRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeaponManeuverMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWeaponManeuvers() throws Exception {
        // Initialize the database
        weaponManeuverRepository.saveAndFlush(weaponManeuver);

        // Get all the weaponManeuverList
        restWeaponManeuverMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weaponManeuver.getId().intValue())));
    }

    @Test
    @Transactional
    void getWeaponManeuver() throws Exception {
        // Initialize the database
        weaponManeuverRepository.saveAndFlush(weaponManeuver);

        // Get the weaponManeuver
        restWeaponManeuverMockMvc
            .perform(get(ENTITY_API_URL_ID, weaponManeuver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(weaponManeuver.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingWeaponManeuver() throws Exception {
        // Get the weaponManeuver
        restWeaponManeuverMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWeaponManeuver() throws Exception {
        // Initialize the database
        weaponManeuverRepository.saveAndFlush(weaponManeuver);

        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().size();

        // Update the weaponManeuver
        WeaponManeuver updatedWeaponManeuver = weaponManeuverRepository.findById(weaponManeuver.getId()).get();
        // Disconnect from session so that the updates on updatedWeaponManeuver are not directly saved in db
        em.detach(updatedWeaponManeuver);
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(updatedWeaponManeuver);

        restWeaponManeuverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weaponManeuverDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            )
            .andExpect(status().isOk());

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
        WeaponManeuver testWeaponManeuver = weaponManeuverList.get(weaponManeuverList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingWeaponManeuver() throws Exception {
        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().size();
        weaponManeuver.setId(count.incrementAndGet());

        // Create the WeaponManeuver
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeaponManeuverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weaponManeuverDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWeaponManeuver() throws Exception {
        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().size();
        weaponManeuver.setId(count.incrementAndGet());

        // Create the WeaponManeuver
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeaponManeuverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWeaponManeuver() throws Exception {
        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().size();
        weaponManeuver.setId(count.incrementAndGet());

        // Create the WeaponManeuver
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeaponManeuverMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWeaponManeuverWithPatch() throws Exception {
        // Initialize the database
        weaponManeuverRepository.saveAndFlush(weaponManeuver);

        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().size();

        // Update the weaponManeuver using partial update
        WeaponManeuver partialUpdatedWeaponManeuver = new WeaponManeuver();
        partialUpdatedWeaponManeuver.setId(weaponManeuver.getId());

        restWeaponManeuverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeaponManeuver.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeaponManeuver))
            )
            .andExpect(status().isOk());

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
        WeaponManeuver testWeaponManeuver = weaponManeuverList.get(weaponManeuverList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateWeaponManeuverWithPatch() throws Exception {
        // Initialize the database
        weaponManeuverRepository.saveAndFlush(weaponManeuver);

        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().size();

        // Update the weaponManeuver using partial update
        WeaponManeuver partialUpdatedWeaponManeuver = new WeaponManeuver();
        partialUpdatedWeaponManeuver.setId(weaponManeuver.getId());

        restWeaponManeuverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeaponManeuver.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeaponManeuver))
            )
            .andExpect(status().isOk());

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
        WeaponManeuver testWeaponManeuver = weaponManeuverList.get(weaponManeuverList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingWeaponManeuver() throws Exception {
        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().size();
        weaponManeuver.setId(count.incrementAndGet());

        // Create the WeaponManeuver
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeaponManeuverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, weaponManeuverDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWeaponManeuver() throws Exception {
        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().size();
        weaponManeuver.setId(count.incrementAndGet());

        // Create the WeaponManeuver
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeaponManeuverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWeaponManeuver() throws Exception {
        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().size();
        weaponManeuver.setId(count.incrementAndGet());

        // Create the WeaponManeuver
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeaponManeuverMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWeaponManeuver() throws Exception {
        // Initialize the database
        weaponManeuverRepository.saveAndFlush(weaponManeuver);

        int databaseSizeBeforeDelete = weaponManeuverRepository.findAll().size();

        // Delete the weaponManeuver
        restWeaponManeuverMockMvc
            .perform(delete(ENTITY_API_URL_ID, weaponManeuver.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
