package com.adi.cms.item.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.item.IntegrationTest;
import com.adi.cms.item.domain.Maneuver;
import com.adi.cms.item.repository.ManeuverRepository;
import com.adi.cms.item.service.dto.ManeuverDTO;
import com.adi.cms.item.service.mapper.ManeuverMapper;
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
 * Integration tests for the {@link ManeuverResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ManeuverResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_MODIFIER = 1;
    private static final Integer UPDATED_MODIFIER = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/maneuvers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ManeuverRepository maneuverRepository;

    @Autowired
    private ManeuverMapper maneuverMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManeuverMockMvc;

    private Maneuver maneuver;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maneuver createEntity(EntityManager em) {
        Maneuver maneuver = new Maneuver().name(DEFAULT_NAME).modifier(DEFAULT_MODIFIER).description(DEFAULT_DESCRIPTION);
        return maneuver;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maneuver createUpdatedEntity(EntityManager em) {
        Maneuver maneuver = new Maneuver().name(UPDATED_NAME).modifier(UPDATED_MODIFIER).description(UPDATED_DESCRIPTION);
        return maneuver;
    }

    @BeforeEach
    public void initTest() {
        maneuver = createEntity(em);
    }

    @Test
    @Transactional
    void createManeuver() throws Exception {
        int databaseSizeBeforeCreate = maneuverRepository.findAll().size();
        // Create the Maneuver
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);
        restManeuverMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll();
        assertThat(maneuverList).hasSize(databaseSizeBeforeCreate + 1);
        Maneuver testManeuver = maneuverList.get(maneuverList.size() - 1);
        assertThat(testManeuver.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testManeuver.getModifier()).isEqualTo(DEFAULT_MODIFIER);
        assertThat(testManeuver.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createManeuverWithExistingId() throws Exception {
        // Create the Maneuver with an existing ID
        maneuver.setId(1L);
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        int databaseSizeBeforeCreate = maneuverRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restManeuverMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll();
        assertThat(maneuverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = maneuverRepository.findAll().size();
        // set the field null
        maneuver.setName(null);

        // Create the Maneuver, which fails.
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        restManeuverMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            )
            .andExpect(status().isBadRequest());

        List<Maneuver> maneuverList = maneuverRepository.findAll();
        assertThat(maneuverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = maneuverRepository.findAll().size();
        // set the field null
        maneuver.setDescription(null);

        // Create the Maneuver, which fails.
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        restManeuverMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            )
            .andExpect(status().isBadRequest());

        List<Maneuver> maneuverList = maneuverRepository.findAll();
        assertThat(maneuverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllManeuvers() throws Exception {
        // Initialize the database
        maneuverRepository.saveAndFlush(maneuver);

        // Get all the maneuverList
        restManeuverMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maneuver.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].modifier").value(hasItem(DEFAULT_MODIFIER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getManeuver() throws Exception {
        // Initialize the database
        maneuverRepository.saveAndFlush(maneuver);

        // Get the maneuver
        restManeuverMockMvc
            .perform(get(ENTITY_API_URL_ID, maneuver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(maneuver.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.modifier").value(DEFAULT_MODIFIER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingManeuver() throws Exception {
        // Get the maneuver
        restManeuverMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewManeuver() throws Exception {
        // Initialize the database
        maneuverRepository.saveAndFlush(maneuver);

        int databaseSizeBeforeUpdate = maneuverRepository.findAll().size();

        // Update the maneuver
        Maneuver updatedManeuver = maneuverRepository.findById(maneuver.getId()).get();
        // Disconnect from session so that the updates on updatedManeuver are not directly saved in db
        em.detach(updatedManeuver);
        updatedManeuver.name(UPDATED_NAME).modifier(UPDATED_MODIFIER).description(UPDATED_DESCRIPTION);
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(updatedManeuver);

        restManeuverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maneuverDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            )
            .andExpect(status().isOk());

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
        Maneuver testManeuver = maneuverList.get(maneuverList.size() - 1);
        assertThat(testManeuver.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testManeuver.getModifier()).isEqualTo(UPDATED_MODIFIER);
        assertThat(testManeuver.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingManeuver() throws Exception {
        int databaseSizeBeforeUpdate = maneuverRepository.findAll().size();
        maneuver.setId(count.incrementAndGet());

        // Create the Maneuver
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManeuverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maneuverDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchManeuver() throws Exception {
        int databaseSizeBeforeUpdate = maneuverRepository.findAll().size();
        maneuver.setId(count.incrementAndGet());

        // Create the Maneuver
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManeuverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamManeuver() throws Exception {
        int databaseSizeBeforeUpdate = maneuverRepository.findAll().size();
        maneuver.setId(count.incrementAndGet());

        // Create the Maneuver
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManeuverMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateManeuverWithPatch() throws Exception {
        // Initialize the database
        maneuverRepository.saveAndFlush(maneuver);

        int databaseSizeBeforeUpdate = maneuverRepository.findAll().size();

        // Update the maneuver using partial update
        Maneuver partialUpdatedManeuver = new Maneuver();
        partialUpdatedManeuver.setId(maneuver.getId());

        restManeuverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManeuver.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManeuver))
            )
            .andExpect(status().isOk());

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
        Maneuver testManeuver = maneuverList.get(maneuverList.size() - 1);
        assertThat(testManeuver.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testManeuver.getModifier()).isEqualTo(DEFAULT_MODIFIER);
        assertThat(testManeuver.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateManeuverWithPatch() throws Exception {
        // Initialize the database
        maneuverRepository.saveAndFlush(maneuver);

        int databaseSizeBeforeUpdate = maneuverRepository.findAll().size();

        // Update the maneuver using partial update
        Maneuver partialUpdatedManeuver = new Maneuver();
        partialUpdatedManeuver.setId(maneuver.getId());

        partialUpdatedManeuver.name(UPDATED_NAME).modifier(UPDATED_MODIFIER).description(UPDATED_DESCRIPTION);

        restManeuverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManeuver.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManeuver))
            )
            .andExpect(status().isOk());

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
        Maneuver testManeuver = maneuverList.get(maneuverList.size() - 1);
        assertThat(testManeuver.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testManeuver.getModifier()).isEqualTo(UPDATED_MODIFIER);
        assertThat(testManeuver.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingManeuver() throws Exception {
        int databaseSizeBeforeUpdate = maneuverRepository.findAll().size();
        maneuver.setId(count.incrementAndGet());

        // Create the Maneuver
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManeuverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, maneuverDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchManeuver() throws Exception {
        int databaseSizeBeforeUpdate = maneuverRepository.findAll().size();
        maneuver.setId(count.incrementAndGet());

        // Create the Maneuver
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManeuverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamManeuver() throws Exception {
        int databaseSizeBeforeUpdate = maneuverRepository.findAll().size();
        maneuver.setId(count.incrementAndGet());

        // Create the Maneuver
        ManeuverDTO maneuverDTO = maneuverMapper.toDto(maneuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManeuverMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maneuverDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Maneuver in the database
        List<Maneuver> maneuverList = maneuverRepository.findAll();
        assertThat(maneuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteManeuver() throws Exception {
        // Initialize the database
        maneuverRepository.saveAndFlush(maneuver);

        int databaseSizeBeforeDelete = maneuverRepository.findAll().size();

        // Delete the maneuver
        restManeuverMockMvc
            .perform(delete(ENTITY_API_URL_ID, maneuver.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Maneuver> maneuverList = maneuverRepository.findAll();
        assertThat(maneuverList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
