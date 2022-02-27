package com.adi.cms.item.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.item.IntegrationTest;
import com.adi.cms.item.domain.ArmorPiece;
import com.adi.cms.item.domain.enumeration.ArmorLocation;
import com.adi.cms.item.repository.ArmorPieceRepository;
import com.adi.cms.item.service.dto.ArmorPieceDTO;
import com.adi.cms.item.service.mapper.ArmorPieceMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ArmorPieceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArmorPieceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    private static final Integer DEFAULT_QUALITY = 1;
    private static final Integer UPDATED_QUALITY = 2;

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final ArmorLocation DEFAULT_LOCATION = ArmorLocation.HEAD;
    private static final ArmorLocation UPDATED_LOCATION = ArmorLocation.RIGHT_SHOULDER;

    private static final Integer DEFAULT_DEFENSE_MODIFIER = 1;
    private static final Integer UPDATED_DEFENSE_MODIFIER = 2;

    private static final String ENTITY_API_URL = "/api/armor-pieces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArmorPieceRepository armorPieceRepository;

    @Autowired
    private ArmorPieceMapper armorPieceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArmorPieceMockMvc;

    private ArmorPiece armorPiece;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArmorPiece createEntity(EntityManager em) {
        ArmorPiece armorPiece = new ArmorPiece()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .weight(DEFAULT_WEIGHT)
            .quality(DEFAULT_QUALITY)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .location(DEFAULT_LOCATION)
            .defenseModifier(DEFAULT_DEFENSE_MODIFIER);
        return armorPiece;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArmorPiece createUpdatedEntity(EntityManager em) {
        ArmorPiece armorPiece = new ArmorPiece()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .quality(UPDATED_QUALITY)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .location(UPDATED_LOCATION)
            .defenseModifier(UPDATED_DEFENSE_MODIFIER);
        return armorPiece;
    }

    @BeforeEach
    public void initTest() {
        armorPiece = createEntity(em);
    }

    @Test
    @Transactional
    void createArmorPiece() throws Exception {
        int databaseSizeBeforeCreate = armorPieceRepository.findAll().size();
        // Create the ArmorPiece
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);
        restArmorPieceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeCreate + 1);
        ArmorPiece testArmorPiece = armorPieceList.get(armorPieceList.size() - 1);
        assertThat(testArmorPiece.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testArmorPiece.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testArmorPiece.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testArmorPiece.getQuality()).isEqualTo(DEFAULT_QUALITY);
        assertThat(testArmorPiece.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testArmorPiece.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testArmorPiece.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testArmorPiece.getDefenseModifier()).isEqualTo(DEFAULT_DEFENSE_MODIFIER);
    }

    @Test
    @Transactional
    void createArmorPieceWithExistingId() throws Exception {
        // Create the ArmorPiece with an existing ID
        armorPiece.setId(1L);
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        int databaseSizeBeforeCreate = armorPieceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArmorPieceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = armorPieceRepository.findAll().size();
        // set the field null
        armorPiece.setName(null);

        // Create the ArmorPiece, which fails.
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        restArmorPieceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = armorPieceRepository.findAll().size();
        // set the field null
        armorPiece.setWeight(null);

        // Create the ArmorPiece, which fails.
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        restArmorPieceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQualityIsRequired() throws Exception {
        int databaseSizeBeforeTest = armorPieceRepository.findAll().size();
        // set the field null
        armorPiece.setQuality(null);

        // Create the ArmorPiece, which fails.
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        restArmorPieceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArmorPieces() throws Exception {
        // Initialize the database
        armorPieceRepository.saveAndFlush(armorPiece);

        // Get all the armorPieceList
        restArmorPieceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(armorPiece.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].quality").value(hasItem(DEFAULT_QUALITY)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].defenseModifier").value(hasItem(DEFAULT_DEFENSE_MODIFIER)));
    }

    @Test
    @Transactional
    void getArmorPiece() throws Exception {
        // Initialize the database
        armorPieceRepository.saveAndFlush(armorPiece);

        // Get the armorPiece
        restArmorPieceMockMvc
            .perform(get(ENTITY_API_URL_ID, armorPiece.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(armorPiece.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.quality").value(DEFAULT_QUALITY))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.defenseModifier").value(DEFAULT_DEFENSE_MODIFIER));
    }

    @Test
    @Transactional
    void getNonExistingArmorPiece() throws Exception {
        // Get the armorPiece
        restArmorPieceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewArmorPiece() throws Exception {
        // Initialize the database
        armorPieceRepository.saveAndFlush(armorPiece);

        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().size();

        // Update the armorPiece
        ArmorPiece updatedArmorPiece = armorPieceRepository.findById(armorPiece.getId()).get();
        // Disconnect from session so that the updates on updatedArmorPiece are not directly saved in db
        em.detach(updatedArmorPiece);
        updatedArmorPiece
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .quality(UPDATED_QUALITY)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .location(UPDATED_LOCATION)
            .defenseModifier(UPDATED_DEFENSE_MODIFIER);
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(updatedArmorPiece);

        restArmorPieceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, armorPieceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
        ArmorPiece testArmorPiece = armorPieceList.get(armorPieceList.size() - 1);
        assertThat(testArmorPiece.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testArmorPiece.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArmorPiece.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testArmorPiece.getQuality()).isEqualTo(UPDATED_QUALITY);
        assertThat(testArmorPiece.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testArmorPiece.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testArmorPiece.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testArmorPiece.getDefenseModifier()).isEqualTo(UPDATED_DEFENSE_MODIFIER);
    }

    @Test
    @Transactional
    void putNonExistingArmorPiece() throws Exception {
        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().size();
        armorPiece.setId(count.incrementAndGet());

        // Create the ArmorPiece
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArmorPieceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, armorPieceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArmorPiece() throws Exception {
        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().size();
        armorPiece.setId(count.incrementAndGet());

        // Create the ArmorPiece
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmorPieceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArmorPiece() throws Exception {
        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().size();
        armorPiece.setId(count.incrementAndGet());

        // Create the ArmorPiece
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmorPieceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArmorPieceWithPatch() throws Exception {
        // Initialize the database
        armorPieceRepository.saveAndFlush(armorPiece);

        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().size();

        // Update the armorPiece using partial update
        ArmorPiece partialUpdatedArmorPiece = new ArmorPiece();
        partialUpdatedArmorPiece.setId(armorPiece.getId());

        partialUpdatedArmorPiece.description(UPDATED_DESCRIPTION).weight(UPDATED_WEIGHT);

        restArmorPieceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArmorPiece.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArmorPiece))
            )
            .andExpect(status().isOk());

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
        ArmorPiece testArmorPiece = armorPieceList.get(armorPieceList.size() - 1);
        assertThat(testArmorPiece.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testArmorPiece.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArmorPiece.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testArmorPiece.getQuality()).isEqualTo(DEFAULT_QUALITY);
        assertThat(testArmorPiece.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testArmorPiece.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testArmorPiece.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testArmorPiece.getDefenseModifier()).isEqualTo(DEFAULT_DEFENSE_MODIFIER);
    }

    @Test
    @Transactional
    void fullUpdateArmorPieceWithPatch() throws Exception {
        // Initialize the database
        armorPieceRepository.saveAndFlush(armorPiece);

        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().size();

        // Update the armorPiece using partial update
        ArmorPiece partialUpdatedArmorPiece = new ArmorPiece();
        partialUpdatedArmorPiece.setId(armorPiece.getId());

        partialUpdatedArmorPiece
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .quality(UPDATED_QUALITY)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .location(UPDATED_LOCATION)
            .defenseModifier(UPDATED_DEFENSE_MODIFIER);

        restArmorPieceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArmorPiece.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArmorPiece))
            )
            .andExpect(status().isOk());

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
        ArmorPiece testArmorPiece = armorPieceList.get(armorPieceList.size() - 1);
        assertThat(testArmorPiece.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testArmorPiece.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArmorPiece.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testArmorPiece.getQuality()).isEqualTo(UPDATED_QUALITY);
        assertThat(testArmorPiece.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testArmorPiece.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testArmorPiece.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testArmorPiece.getDefenseModifier()).isEqualTo(UPDATED_DEFENSE_MODIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingArmorPiece() throws Exception {
        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().size();
        armorPiece.setId(count.incrementAndGet());

        // Create the ArmorPiece
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArmorPieceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, armorPieceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArmorPiece() throws Exception {
        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().size();
        armorPiece.setId(count.incrementAndGet());

        // Create the ArmorPiece
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmorPieceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArmorPiece() throws Exception {
        int databaseSizeBeforeUpdate = armorPieceRepository.findAll().size();
        armorPiece.setId(count.incrementAndGet());

        // Create the ArmorPiece
        ArmorPieceDTO armorPieceDTO = armorPieceMapper.toDto(armorPiece);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArmorPieceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(armorPieceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArmorPiece in the database
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArmorPiece() throws Exception {
        // Initialize the database
        armorPieceRepository.saveAndFlush(armorPiece);

        int databaseSizeBeforeDelete = armorPieceRepository.findAll().size();

        // Delete the armorPiece
        restArmorPieceMockMvc
            .perform(delete(ENTITY_API_URL_ID, armorPiece.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArmorPiece> armorPieceList = armorPieceRepository.findAll();
        assertThat(armorPieceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
