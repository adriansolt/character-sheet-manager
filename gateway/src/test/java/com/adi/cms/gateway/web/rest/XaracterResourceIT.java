package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.User;
import com.adi.cms.gateway.domain.Xaracter;
import com.adi.cms.gateway.domain.enumeration.Handedness;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.repository.XaracterRepository;
import com.adi.cms.gateway.service.dto.XaracterDTO;
import com.adi.cms.gateway.service.mapper.XaracterMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link XaracterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private WebTestClient webTestClient;

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
        User user = em.insert(UserResourceIT.createEntity(em)).block();
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
        User user = em.insert(UserResourceIT.createEntity(em)).block();
        xaracter.setUser(user);
        return xaracter;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Xaracter.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        UserResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        xaracter = createEntity(em);
    }

    @Test
    void createXaracter() throws Exception {
        int databaseSizeBeforeCreate = xaracterRepository.findAll().collectList().block().size();
        // Create the Xaracter
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
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
    void createXaracterWithExistingId() throws Exception {
        // Create the Xaracter with an existing ID
        xaracter.setId(1L);
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        int databaseSizeBeforeCreate = xaracterRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
        assertThat(xaracterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = xaracterRepository.findAll().collectList().block().size();
        // set the field null
        xaracter.setName(null);

        // Create the Xaracter, which fails.
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
        assertThat(xaracterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = xaracterRepository.findAll().collectList().block().size();
        // set the field null
        xaracter.setWeight(null);

        // Create the Xaracter, which fails.
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
        assertThat(xaracterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkHeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = xaracterRepository.findAll().collectList().block().size();
        // set the field null
        xaracter.setHeight(null);

        // Create the Xaracter, which fails.
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
        assertThat(xaracterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = xaracterRepository.findAll().collectList().block().size();
        // set the field null
        xaracter.setPoints(null);

        // Create the Xaracter, which fails.
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
        assertThat(xaracterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllXaracters() {
        // Initialize the database
        xaracterRepository.save(xaracter).block();

        // Get all the xaracterList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(xaracter.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].weight")
            .value(hasItem(DEFAULT_WEIGHT))
            .jsonPath("$.[*].height")
            .value(hasItem(DEFAULT_HEIGHT))
            .jsonPath("$.[*].points")
            .value(hasItem(DEFAULT_POINTS))
            .jsonPath("$.[*].pictureContentType")
            .value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE))
            .jsonPath("$.[*].picture")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .jsonPath("$.[*].handedness")
            .value(hasItem(DEFAULT_HANDEDNESS.toString()))
            .jsonPath("$.[*].campaignId")
            .value(hasItem(DEFAULT_CAMPAIGN_ID.intValue()))
            .jsonPath("$.[*].active")
            .value(hasItem(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getXaracter() {
        // Initialize the database
        xaracterRepository.save(xaracter).block();

        // Get the xaracter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, xaracter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(xaracter.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.weight")
            .value(is(DEFAULT_WEIGHT))
            .jsonPath("$.height")
            .value(is(DEFAULT_HEIGHT))
            .jsonPath("$.points")
            .value(is(DEFAULT_POINTS))
            .jsonPath("$.pictureContentType")
            .value(is(DEFAULT_PICTURE_CONTENT_TYPE))
            .jsonPath("$.picture")
            .value(is(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .jsonPath("$.handedness")
            .value(is(DEFAULT_HANDEDNESS.toString()))
            .jsonPath("$.campaignId")
            .value(is(DEFAULT_CAMPAIGN_ID.intValue()))
            .jsonPath("$.active")
            .value(is(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getNonExistingXaracter() {
        // Get the xaracter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewXaracter() throws Exception {
        // Initialize the database
        xaracterRepository.save(xaracter).block();

        int databaseSizeBeforeUpdate = xaracterRepository.findAll().collectList().block().size();

        // Update the xaracter
        Xaracter updatedXaracter = xaracterRepository.findById(xaracter.getId()).block();
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

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, xaracterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
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
    void putNonExistingXaracter() throws Exception {
        int databaseSizeBeforeUpdate = xaracterRepository.findAll().collectList().block().size();
        xaracter.setId(count.incrementAndGet());

        // Create the Xaracter
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, xaracterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
        assertThat(xaracterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchXaracter() throws Exception {
        int databaseSizeBeforeUpdate = xaracterRepository.findAll().collectList().block().size();
        xaracter.setId(count.incrementAndGet());

        // Create the Xaracter
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
        assertThat(xaracterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamXaracter() throws Exception {
        int databaseSizeBeforeUpdate = xaracterRepository.findAll().collectList().block().size();
        xaracter.setId(count.incrementAndGet());

        // Create the Xaracter
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
        assertThat(xaracterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateXaracterWithPatch() throws Exception {
        // Initialize the database
        xaracterRepository.save(xaracter).block();

        int databaseSizeBeforeUpdate = xaracterRepository.findAll().collectList().block().size();

        // Update the xaracter using partial update
        Xaracter partialUpdatedXaracter = new Xaracter();
        partialUpdatedXaracter.setId(xaracter.getId());

        partialUpdatedXaracter.name(UPDATED_NAME).weight(UPDATED_WEIGHT).points(UPDATED_POINTS).campaignId(UPDATED_CAMPAIGN_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedXaracter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
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
    void fullUpdateXaracterWithPatch() throws Exception {
        // Initialize the database
        xaracterRepository.save(xaracter).block();

        int databaseSizeBeforeUpdate = xaracterRepository.findAll().collectList().block().size();

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

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedXaracter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
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
    void patchNonExistingXaracter() throws Exception {
        int databaseSizeBeforeUpdate = xaracterRepository.findAll().collectList().block().size();
        xaracter.setId(count.incrementAndGet());

        // Create the Xaracter
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, xaracterDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
        assertThat(xaracterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchXaracter() throws Exception {
        int databaseSizeBeforeUpdate = xaracterRepository.findAll().collectList().block().size();
        xaracter.setId(count.incrementAndGet());

        // Create the Xaracter
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
        assertThat(xaracterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamXaracter() throws Exception {
        int databaseSizeBeforeUpdate = xaracterRepository.findAll().collectList().block().size();
        xaracter.setId(count.incrementAndGet());

        // Create the Xaracter
        XaracterDTO xaracterDTO = xaracterMapper.toDto(xaracter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Xaracter in the database
        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
        assertThat(xaracterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteXaracter() {
        // Initialize the database
        xaracterRepository.save(xaracter).block();

        int databaseSizeBeforeDelete = xaracterRepository.findAll().collectList().block().size();

        // Delete the xaracter
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, xaracter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Xaracter> xaracterList = xaracterRepository.findAll().collectList().block();
        assertThat(xaracterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
