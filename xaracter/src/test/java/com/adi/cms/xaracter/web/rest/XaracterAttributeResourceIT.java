package com.adi.cms.xaracter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.xaracter.IntegrationTest;
import com.adi.cms.xaracter.domain.XaracterAttribute;
import com.adi.cms.xaracter.domain.enumeration.AttributeName;
import com.adi.cms.xaracter.repository.XaracterAttributeRepository;
import com.adi.cms.xaracter.service.dto.XaracterAttributeDTO;
import com.adi.cms.xaracter.service.mapper.XaracterAttributeMapper;
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
 * Integration tests for the {@link XaracterAttributeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class XaracterAttributeResourceIT {

    private static final AttributeName DEFAULT_NAME = AttributeName.ST;
    private static final AttributeName UPDATED_NAME = AttributeName.DX;

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final Integer DEFAULT_ATTRIBUTE_MODIFIER = 1;
    private static final Integer UPDATED_ATTRIBUTE_MODIFIER = 2;

    private static final String ENTITY_API_URL = "/api/xaracter-attributes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private XaracterAttributeRepository xaracterAttributeRepository;

    @Autowired
    private XaracterAttributeMapper xaracterAttributeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restXaracterAttributeMockMvc;

    private XaracterAttribute xaracterAttribute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static XaracterAttribute createEntity(EntityManager em) {
        XaracterAttribute xaracterAttribute = new XaracterAttribute()
            .name(DEFAULT_NAME)
            .points(DEFAULT_POINTS)
            .attributeModifier(DEFAULT_ATTRIBUTE_MODIFIER);
        return xaracterAttribute;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static XaracterAttribute createUpdatedEntity(EntityManager em) {
        XaracterAttribute xaracterAttribute = new XaracterAttribute()
            .name(UPDATED_NAME)
            .points(UPDATED_POINTS)
            .attributeModifier(UPDATED_ATTRIBUTE_MODIFIER);
        return xaracterAttribute;
    }

    @BeforeEach
    public void initTest() {
        xaracterAttribute = createEntity(em);
    }

    @Test
    @Transactional
    void createXaracterAttribute() throws Exception {
        int databaseSizeBeforeCreate = xaracterAttributeRepository.findAll().size();
        // Create the XaracterAttribute
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);
        restXaracterAttributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeCreate + 1);
        XaracterAttribute testXaracterAttribute = xaracterAttributeList.get(xaracterAttributeList.size() - 1);
        assertThat(testXaracterAttribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testXaracterAttribute.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testXaracterAttribute.getAttributeModifier()).isEqualTo(DEFAULT_ATTRIBUTE_MODIFIER);
    }

    @Test
    @Transactional
    void createXaracterAttributeWithExistingId() throws Exception {
        // Create the XaracterAttribute with an existing ID
        xaracterAttribute.setId(1L);
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        int databaseSizeBeforeCreate = xaracterAttributeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restXaracterAttributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = xaracterAttributeRepository.findAll().size();
        // set the field null
        xaracterAttribute.setName(null);

        // Create the XaracterAttribute, which fails.
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        restXaracterAttributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = xaracterAttributeRepository.findAll().size();
        // set the field null
        xaracterAttribute.setPoints(null);

        // Create the XaracterAttribute, which fails.
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        restXaracterAttributeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllXaracterAttributes() throws Exception {
        // Initialize the database
        xaracterAttributeRepository.saveAndFlush(xaracterAttribute);

        // Get all the xaracterAttributeList
        restXaracterAttributeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(xaracterAttribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].attributeModifier").value(hasItem(DEFAULT_ATTRIBUTE_MODIFIER)));
    }

    @Test
    @Transactional
    void getXaracterAttribute() throws Exception {
        // Initialize the database
        xaracterAttributeRepository.saveAndFlush(xaracterAttribute);

        // Get the xaracterAttribute
        restXaracterAttributeMockMvc
            .perform(get(ENTITY_API_URL_ID, xaracterAttribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(xaracterAttribute.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.attributeModifier").value(DEFAULT_ATTRIBUTE_MODIFIER));
    }

    @Test
    @Transactional
    void getNonExistingXaracterAttribute() throws Exception {
        // Get the xaracterAttribute
        restXaracterAttributeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewXaracterAttribute() throws Exception {
        // Initialize the database
        xaracterAttributeRepository.saveAndFlush(xaracterAttribute);

        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().size();

        // Update the xaracterAttribute
        XaracterAttribute updatedXaracterAttribute = xaracterAttributeRepository.findById(xaracterAttribute.getId()).get();
        // Disconnect from session so that the updates on updatedXaracterAttribute are not directly saved in db
        em.detach(updatedXaracterAttribute);
        updatedXaracterAttribute.name(UPDATED_NAME).points(UPDATED_POINTS).attributeModifier(UPDATED_ATTRIBUTE_MODIFIER);
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(updatedXaracterAttribute);

        restXaracterAttributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, xaracterAttributeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            )
            .andExpect(status().isOk());

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
        XaracterAttribute testXaracterAttribute = xaracterAttributeList.get(xaracterAttributeList.size() - 1);
        assertThat(testXaracterAttribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testXaracterAttribute.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testXaracterAttribute.getAttributeModifier()).isEqualTo(UPDATED_ATTRIBUTE_MODIFIER);
    }

    @Test
    @Transactional
    void putNonExistingXaracterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().size();
        xaracterAttribute.setId(count.incrementAndGet());

        // Create the XaracterAttribute
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXaracterAttributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, xaracterAttributeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchXaracterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().size();
        xaracterAttribute.setId(count.incrementAndGet());

        // Create the XaracterAttribute
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterAttributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamXaracterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().size();
        xaracterAttribute.setId(count.incrementAndGet());

        // Create the XaracterAttribute
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterAttributeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateXaracterAttributeWithPatch() throws Exception {
        // Initialize the database
        xaracterAttributeRepository.saveAndFlush(xaracterAttribute);

        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().size();

        // Update the xaracterAttribute using partial update
        XaracterAttribute partialUpdatedXaracterAttribute = new XaracterAttribute();
        partialUpdatedXaracterAttribute.setId(xaracterAttribute.getId());

        partialUpdatedXaracterAttribute.name(UPDATED_NAME);

        restXaracterAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXaracterAttribute.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterAttribute))
            )
            .andExpect(status().isOk());

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
        XaracterAttribute testXaracterAttribute = xaracterAttributeList.get(xaracterAttributeList.size() - 1);
        assertThat(testXaracterAttribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testXaracterAttribute.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testXaracterAttribute.getAttributeModifier()).isEqualTo(DEFAULT_ATTRIBUTE_MODIFIER);
    }

    @Test
    @Transactional
    void fullUpdateXaracterAttributeWithPatch() throws Exception {
        // Initialize the database
        xaracterAttributeRepository.saveAndFlush(xaracterAttribute);

        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().size();

        // Update the xaracterAttribute using partial update
        XaracterAttribute partialUpdatedXaracterAttribute = new XaracterAttribute();
        partialUpdatedXaracterAttribute.setId(xaracterAttribute.getId());

        partialUpdatedXaracterAttribute.name(UPDATED_NAME).points(UPDATED_POINTS).attributeModifier(UPDATED_ATTRIBUTE_MODIFIER);

        restXaracterAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXaracterAttribute.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterAttribute))
            )
            .andExpect(status().isOk());

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
        XaracterAttribute testXaracterAttribute = xaracterAttributeList.get(xaracterAttributeList.size() - 1);
        assertThat(testXaracterAttribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testXaracterAttribute.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testXaracterAttribute.getAttributeModifier()).isEqualTo(UPDATED_ATTRIBUTE_MODIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingXaracterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().size();
        xaracterAttribute.setId(count.incrementAndGet());

        // Create the XaracterAttribute
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXaracterAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, xaracterAttributeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchXaracterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().size();
        xaracterAttribute.setId(count.incrementAndGet());

        // Create the XaracterAttribute
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamXaracterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().size();
        xaracterAttribute.setId(count.incrementAndGet());

        // Create the XaracterAttribute
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteXaracterAttribute() throws Exception {
        // Initialize the database
        xaracterAttributeRepository.saveAndFlush(xaracterAttribute);

        int databaseSizeBeforeDelete = xaracterAttributeRepository.findAll().size();

        // Delete the xaracterAttribute
        restXaracterAttributeMockMvc
            .perform(delete(ENTITY_API_URL_ID, xaracterAttribute.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
