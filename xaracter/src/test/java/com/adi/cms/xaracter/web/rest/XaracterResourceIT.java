package com.adi.cms.xaracter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.xaracter.IntegrationTest;
import com.adi.cms.xaracter.domain.User;
import com.adi.cms.xaracter.domain.Xaracter;
import com.adi.cms.xaracter.domain.enumeration.Handedness;
import com.adi.cms.xaracter.repository.XaracterRepository;
import com.adi.cms.xaracter.service.dto.XaracterDTO;
import com.adi.cms.xaracter.service.mapper.XaracterMapper;
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
 * Integration tests for the {@link XaracterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class XaracterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Handedness DEFAULT_HANDEDNESS = Handedness.RIGHT;
    private static final Handedness UPDATED_HANDEDNESS = Handedness.LEFT;

    private static final Long DEFAULT_CAMPAIGN_ID = 1L;
    private static final Long UPDATED_CAMPAIGN_ID = 2L;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/xaracters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private XaracterRepository xaracterRepository;

    @Autowired
    private XaracterMapper xaracterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restXaracterMockMvc;

    private Xaracter xaracter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Xaracter createEntity(EntityManager em) {
        Xaracter xaracter = new Xaracter()
            .name(DEFAULT_NAME)
            .weight(DEFAULT_WEIGHT)
            .height(DEFAULT_HEIGHT)
            .points(DEFAULT_POINTS)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .handedness(DEFAULT_HANDEDNESS)
            .campaignId(DEFAULT_CAMPAIGN_ID)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        xaracter.setUser(user);
        return xaracter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Xaracter createUpdatedEntity(EntityManager em) {
        Xaracter xaracter = new Xaracter()
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .points(UPDATED_POINTS)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .handedness(UPDATED_HANDEDNESS)
            .campaignId(UPDATED_CAMPAIGN_ID)
            .active(UPDATED_ACTIVE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        xaracter.setUser(user);
        return xaracter;
    }

    @BeforeEach
    public void initTest() {
        xaracter = createEntity(em);
    }

    @Test
    @Transactional
    void createXaracter() throws Exception {
        int databaseSizeBeforeCreate = xaracterRepository.findAll().size();
        // Create the Xaracter
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);
        restXaracterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeCreate + 1);
        Xaracter testXaracter = xaracterList.get(xaracterList.size() - 1);
        assertThat(testXaracter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testXaracter.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testXaracter.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testXaracter.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testXaracter.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testXaracter.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testXaracter.getHandedness()).isEqualTo(DEFAULT_HANDEDNESS);
        assertThat(testXaracter.getCampaignId()).isEqualTo(DEFAULT_CAMPAIGN_ID);
        assertThat(testXaracter.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createXaracterWithExistingId() throws Exception {
        // Create the Xaracter with an existing ID
        xaracter.setId(1L);
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        int databaseSizeBeforeCreate = xaracterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restXaracterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = xaracterRepository.findAll().size();
        // set the field null
        xaracter.setName(null);

        // Create the Xaracter, which fails.
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        restXaracterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            )
            .andExpect(status().isBadRequest());

        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = xaracterRepository.findAll().size();
        // set the field null
        xaracter.setWeight(null);

        // Create the Xaracter, which fails.
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        restXaracterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            )
            .andExpect(status().isBadRequest());

        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = xaracterRepository.findAll().size();
        // set the field null
        xaracter.setHeight(null);

        // Create the Xaracter, which fails.
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        restXaracterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            )
            .andExpect(status().isBadRequest());

        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = xaracterRepository.findAll().size();
        // set the field null
        xaracter.setPoints(null);

        // Create the Xaracter, which fails.
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        restXaracterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            )
            .andExpect(status().isBadRequest());

        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllXaracters() throws Exception {
        // Initialize the database
        xaracterRepository.saveAndFlush(xaracter);

        // Get all the xaracterList
        restXaracterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(xaracter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].handedness").value(hasItem(DEFAULT_HANDEDNESS.toString())))
            .andExpect(jsonPath("$.[*].campaignId").value(hasItem(DEFAULT_CAMPAIGN_ID.intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getXaracter() throws Exception {
        // Initialize the database
        xaracterRepository.saveAndFlush(xaracter);

        // Get the xaracter
        restXaracterMockMvc
            .perform(get(ENTITY_API_URL_ID, xaracter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(xaracter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.handedness").value(DEFAULT_HANDEDNESS.toString()))
            .andExpect(jsonPath("$.campaignId").value(DEFAULT_CAMPAIGN_ID.intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingXaracter() throws Exception {
        // Get the xaracter
        restXaracterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewXaracter() throws Exception {
        // Initialize the database
        xaracterRepository.saveAndFlush(xaracter);

        int databaseSizeBeforeUpdate = xaracterRepository.findAll().size();

        // Update the xaracter
        Xaracter updatedXaracter = xaracterRepository.findById(xaracter.getId()).get();
        // Disconnect from session so that the updates on updatedXaracter are not directly saved in db
        em.detach(updatedXaracter);
        updatedXaracter
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .points(UPDATED_POINTS)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .handedness(UPDATED_HANDEDNESS)
            .campaignId(UPDATED_CAMPAIGN_ID)
            .active(UPDATED_ACTIVE);
        XaracterDTO xaracterDTO = xaracterMapper.toDto(updatedXaracter);

        restXaracterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, xaracterDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            )
            .andExpect(status().isOk());

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeUpdate);
        Xaracter testXaracter = xaracterList.get(xaracterList.size() - 1);
        assertThat(testXaracter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testXaracter.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testXaracter.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testXaracter.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testXaracter.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testXaracter.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testXaracter.getHandedness()).isEqualTo(UPDATED_HANDEDNESS);
        assertThat(testXaracter.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
        assertThat(testXaracter.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingXaracter() throws Exception {
        int databaseSizeBeforeUpdate = xaracterRepository.findAll().size();
        xaracter.setId(count.incrementAndGet());

        // Create the Xaracter
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXaracterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, xaracterDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchXaracter() throws Exception {
        int databaseSizeBeforeUpdate = xaracterRepository.findAll().size();
        xaracter.setId(count.incrementAndGet());

        // Create the Xaracter
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamXaracter() throws Exception {
        int databaseSizeBeforeUpdate = xaracterRepository.findAll().size();
        xaracter.setId(count.incrementAndGet());

        // Create the Xaracter
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateXaracterWithPatch() throws Exception {
        // Initialize the database
        xaracterRepository.saveAndFlush(xaracter);

        int databaseSizeBeforeUpdate = xaracterRepository.findAll().size();

        // Update the xaracter using partial update
        Xaracter partialUpdatedXaracter = new Xaracter();
        partialUpdatedXaracter.setId(xaracter.getId());

        partialUpdatedXaracter.name(UPDATED_NAME).weight(UPDATED_WEIGHT).points(UPDATED_POINTS).campaignId(UPDATED_CAMPAIGN_ID);

        restXaracterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXaracter.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracter))
            )
            .andExpect(status().isOk());

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeUpdate);
        Xaracter testXaracter = xaracterList.get(xaracterList.size() - 1);
        assertThat(testXaracter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testXaracter.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testXaracter.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testXaracter.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testXaracter.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testXaracter.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testXaracter.getHandedness()).isEqualTo(DEFAULT_HANDEDNESS);
        assertThat(testXaracter.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
        assertThat(testXaracter.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateXaracterWithPatch() throws Exception {
        // Initialize the database
        xaracterRepository.saveAndFlush(xaracter);

        int databaseSizeBeforeUpdate = xaracterRepository.findAll().size();

        // Update the xaracter using partial update
        Xaracter partialUpdatedXaracter = new Xaracter();
        partialUpdatedXaracter.setId(xaracter.getId());

        partialUpdatedXaracter
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .points(UPDATED_POINTS)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .handedness(UPDATED_HANDEDNESS)
            .campaignId(UPDATED_CAMPAIGN_ID)
            .active(UPDATED_ACTIVE);

        restXaracterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXaracter.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracter))
            )
            .andExpect(status().isOk());

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeUpdate);
        Xaracter testXaracter = xaracterList.get(xaracterList.size() - 1);
        assertThat(testXaracter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testXaracter.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testXaracter.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testXaracter.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testXaracter.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testXaracter.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testXaracter.getHandedness()).isEqualTo(UPDATED_HANDEDNESS);
        assertThat(testXaracter.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
        assertThat(testXaracter.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingXaracter() throws Exception {
        int databaseSizeBeforeUpdate = xaracterRepository.findAll().size();
        xaracter.setId(count.incrementAndGet());

        // Create the Xaracter
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXaracterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, xaracterDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchXaracter() throws Exception {
        int databaseSizeBeforeUpdate = xaracterRepository.findAll().size();
        xaracter.setId(count.incrementAndGet());

        // Create the Xaracter
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamXaracter() throws Exception {
        int databaseSizeBeforeUpdate = xaracterRepository.findAll().size();
        xaracter.setId(count.incrementAndGet());

        // Create the Xaracter
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteXaracter() throws Exception {
        // Initialize the database
        xaracterRepository.saveAndFlush(xaracter);

        int databaseSizeBeforeDelete = xaracterRepository.findAll().size();

        // Delete the xaracter
        restXaracterMockMvc
            .perform(delete(ENTITY_API_URL_ID, xaracter.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Xaracter> xaracterList = xaracterRepository.findAll();
        assertThat(xaracterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
