package com.adi.cms.xaracter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.xaracter.IntegrationTest;
import com.adi.cms.xaracter.domain.XaracterSkill;
import com.adi.cms.xaracter.repository.XaracterSkillRepository;
import com.adi.cms.xaracter.service.dto.XaracterSkillDTO;
import com.adi.cms.xaracter.service.mapper.XaracterSkillMapper;
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
 * Integration tests for the {@link XaracterSkillResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class XaracterSkillResourceIT {

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final Integer DEFAULT_SKILL_MODIFIER = 1;
    private static final Integer UPDATED_SKILL_MODIFIER = 2;

    private static final String ENTITY_API_URL = "/api/xaracter-skills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private XaracterSkillRepository xaracterSkillRepository;

    @Autowired
    private XaracterSkillMapper xaracterSkillMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restXaracterSkillMockMvc;

    private XaracterSkill xaracterSkill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static XaracterSkill createEntity(EntityManager em) {
        XaracterSkill xaracterSkill = new XaracterSkill().points(DEFAULT_POINTS).skillModifier(DEFAULT_SKILL_MODIFIER);
        return xaracterSkill;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static XaracterSkill createUpdatedEntity(EntityManager em) {
        XaracterSkill xaracterSkill = new XaracterSkill().points(UPDATED_POINTS).skillModifier(UPDATED_SKILL_MODIFIER);
        return xaracterSkill;
    }

    @BeforeEach
    public void initTest() {
        xaracterSkill = createEntity(em);
    }

    @Test
    @Transactional
    void createXaracterSkill() throws Exception {
        int databaseSizeBeforeCreate = xaracterSkillRepository.findAll().size();
        // Create the XaracterSkill
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);
        restXaracterSkillMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            )
            .andExpect(status().isCreated());

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeCreate + 1);
        XaracterSkill testXaracterSkill = xaracterSkillList.get(xaracterSkillList.size() - 1);
        assertThat(testXaracterSkill.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testXaracterSkill.getSkillModifier()).isEqualTo(DEFAULT_SKILL_MODIFIER);
    }

    @Test
    @Transactional
    void createXaracterSkillWithExistingId() throws Exception {
        // Create the XaracterSkill with an existing ID
        xaracterSkill.setId(1L);
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        int databaseSizeBeforeCreate = xaracterSkillRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restXaracterSkillMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = xaracterSkillRepository.findAll().size();
        // set the field null
        xaracterSkill.setPoints(null);

        // Create the XaracterSkill, which fails.
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        restXaracterSkillMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            )
            .andExpect(status().isBadRequest());

        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllXaracterSkills() throws Exception {
        // Initialize the database
        xaracterSkillRepository.saveAndFlush(xaracterSkill);

        // Get all the xaracterSkillList
        restXaracterSkillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(xaracterSkill.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].skillModifier").value(hasItem(DEFAULT_SKILL_MODIFIER)));
    }

    @Test
    @Transactional
    void getXaracterSkill() throws Exception {
        // Initialize the database
        xaracterSkillRepository.saveAndFlush(xaracterSkill);

        // Get the xaracterSkill
        restXaracterSkillMockMvc
            .perform(get(ENTITY_API_URL_ID, xaracterSkill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(xaracterSkill.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.skillModifier").value(DEFAULT_SKILL_MODIFIER));
    }

    @Test
    @Transactional
    void getNonExistingXaracterSkill() throws Exception {
        // Get the xaracterSkill
        restXaracterSkillMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewXaracterSkill() throws Exception {
        // Initialize the database
        xaracterSkillRepository.saveAndFlush(xaracterSkill);

        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().size();

        // Update the xaracterSkill
        XaracterSkill updatedXaracterSkill = xaracterSkillRepository.findById(xaracterSkill.getId()).get();
        // Disconnect from session so that the updates on updatedXaracterSkill are not directly saved in db
        em.detach(updatedXaracterSkill);
        updatedXaracterSkill.points(UPDATED_POINTS).skillModifier(UPDATED_SKILL_MODIFIER);
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(updatedXaracterSkill);

        restXaracterSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, xaracterSkillDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            )
            .andExpect(status().isOk());

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
        XaracterSkill testXaracterSkill = xaracterSkillList.get(xaracterSkillList.size() - 1);
        assertThat(testXaracterSkill.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testXaracterSkill.getSkillModifier()).isEqualTo(UPDATED_SKILL_MODIFIER);
    }

    @Test
    @Transactional
    void putNonExistingXaracterSkill() throws Exception {
        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().size();
        xaracterSkill.setId(count.incrementAndGet());

        // Create the XaracterSkill
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXaracterSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, xaracterSkillDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchXaracterSkill() throws Exception {
        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().size();
        xaracterSkill.setId(count.incrementAndGet());

        // Create the XaracterSkill
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamXaracterSkill() throws Exception {
        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().size();
        xaracterSkill.setId(count.incrementAndGet());

        // Create the XaracterSkill
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterSkillMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateXaracterSkillWithPatch() throws Exception {
        // Initialize the database
        xaracterSkillRepository.saveAndFlush(xaracterSkill);

        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().size();

        // Update the xaracterSkill using partial update
        XaracterSkill partialUpdatedXaracterSkill = new XaracterSkill();
        partialUpdatedXaracterSkill.setId(xaracterSkill.getId());

        partialUpdatedXaracterSkill.points(UPDATED_POINTS);

        restXaracterSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXaracterSkill.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterSkill))
            )
            .andExpect(status().isOk());

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
        XaracterSkill testXaracterSkill = xaracterSkillList.get(xaracterSkillList.size() - 1);
        assertThat(testXaracterSkill.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testXaracterSkill.getSkillModifier()).isEqualTo(DEFAULT_SKILL_MODIFIER);
    }

    @Test
    @Transactional
    void fullUpdateXaracterSkillWithPatch() throws Exception {
        // Initialize the database
        xaracterSkillRepository.saveAndFlush(xaracterSkill);

        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().size();

        // Update the xaracterSkill using partial update
        XaracterSkill partialUpdatedXaracterSkill = new XaracterSkill();
        partialUpdatedXaracterSkill.setId(xaracterSkill.getId());

        partialUpdatedXaracterSkill.points(UPDATED_POINTS).skillModifier(UPDATED_SKILL_MODIFIER);

        restXaracterSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXaracterSkill.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterSkill))
            )
            .andExpect(status().isOk());

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
        XaracterSkill testXaracterSkill = xaracterSkillList.get(xaracterSkillList.size() - 1);
        assertThat(testXaracterSkill.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testXaracterSkill.getSkillModifier()).isEqualTo(UPDATED_SKILL_MODIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingXaracterSkill() throws Exception {
        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().size();
        xaracterSkill.setId(count.incrementAndGet());

        // Create the XaracterSkill
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXaracterSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, xaracterSkillDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchXaracterSkill() throws Exception {
        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().size();
        xaracterSkill.setId(count.incrementAndGet());

        // Create the XaracterSkill
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamXaracterSkill() throws Exception {
        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().size();
        xaracterSkill.setId(count.incrementAndGet());

        // Create the XaracterSkill
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterSkillMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteXaracterSkill() throws Exception {
        // Initialize the database
        xaracterSkillRepository.saveAndFlush(xaracterSkill);

        int databaseSizeBeforeDelete = xaracterSkillRepository.findAll().size();

        // Delete the xaracterSkill
        restXaracterSkillMockMvc
            .perform(delete(ENTITY_API_URL_ID, xaracterSkill.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
