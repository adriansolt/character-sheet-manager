package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.Campaign;
import com.adi.cms.gateway.repository.CampaignRepository;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.service.dto.CampaignDTO;
import com.adi.cms.gateway.service.mapper.CampaignMapper;
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
 * Integration tests for the {@link CampaignResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CampaignResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_MAP = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_MAP = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_MAP_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_MAP_CONTENT_TYPE = "image/png";

    private static final Long DEFAULT_MASTER_ID = 1L;
    private static final Long UPDATED_MASTER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/campaigns";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignMapper campaignMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Campaign campaign;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campaign createEntity(EntityManager em) {
        Campaign campaign = new Campaign()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .map(DEFAULT_MAP)
            .mapContentType(DEFAULT_MAP_CONTENT_TYPE)
            .masterId(DEFAULT_MASTER_ID);
        return campaign;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campaign createUpdatedEntity(EntityManager em) {
        Campaign campaign = new Campaign()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .map(UPDATED_MAP)
            .mapContentType(UPDATED_MAP_CONTENT_TYPE)
            .masterId(UPDATED_MASTER_ID);
        return campaign;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Campaign.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
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
        campaign = createEntity(em);
    }

    @Test
    void createCampaign() throws Exception {
        int databaseSizeBeforeCreate = campaignRepository.findAll().collectList().block().size();
        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeCreate + 1);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCampaign.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCampaign.getMap()).isEqualTo(DEFAULT_MAP);
        assertThat(testCampaign.getMapContentType()).isEqualTo(DEFAULT_MAP_CONTENT_TYPE);
        assertThat(testCampaign.getMasterId()).isEqualTo(DEFAULT_MASTER_ID);
    }

    @Test
    void createCampaignWithExistingId() throws Exception {
        // Create the Campaign with an existing ID
        campaign.setId(1L);
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        int databaseSizeBeforeCreate = campaignRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCampaigns() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList
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
            .value(hasItem(campaign.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].mapContentType")
            .value(hasItem(DEFAULT_MAP_CONTENT_TYPE))
            .jsonPath("$.[*].map")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_MAP)))
            .jsonPath("$.[*].masterId")
            .value(hasItem(DEFAULT_MASTER_ID.intValue()));
    }

    @Test
    void getCampaign() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get the campaign
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, campaign.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(campaign.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.mapContentType")
            .value(is(DEFAULT_MAP_CONTENT_TYPE))
            .jsonPath("$.map")
            .value(is(Base64Utils.encodeToString(DEFAULT_MAP)))
            .jsonPath("$.masterId")
            .value(is(DEFAULT_MASTER_ID.intValue()));
    }

    @Test
    void getNonExistingCampaign() {
        // Get the campaign
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCampaign() throws Exception {
        // Initialize the database
        campaignRepository.save(campaign).block();

        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();

        // Update the campaign
        Campaign updatedCampaign = campaignRepository.findById(campaign.getId()).block();
        updatedCampaign
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .map(UPDATED_MAP)
            .mapContentType(UPDATED_MAP_CONTENT_TYPE)
            .masterId(UPDATED_MASTER_ID);
        CampaignDTO campaignDTO = campaignMapper.toDto(updatedCampaign);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, campaignDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCampaign.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCampaign.getMap()).isEqualTo(UPDATED_MAP);
        assertThat(testCampaign.getMapContentType()).isEqualTo(UPDATED_MAP_CONTENT_TYPE);
        assertThat(testCampaign.getMasterId()).isEqualTo(UPDATED_MASTER_ID);
    }

    @Test
    void putNonExistingCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();
        campaign.setId(count.incrementAndGet());

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, campaignDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();
        campaign.setId(count.incrementAndGet());

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();
        campaign.setId(count.incrementAndGet());

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCampaignWithPatch() throws Exception {
        // Initialize the database
        campaignRepository.save(campaign).block();

        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();

        // Update the campaign using partial update
        Campaign partialUpdatedCampaign = new Campaign();
        partialUpdatedCampaign.setId(campaign.getId());

        partialUpdatedCampaign.description(UPDATED_DESCRIPTION).masterId(UPDATED_MASTER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCampaign.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCampaign))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCampaign.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCampaign.getMap()).isEqualTo(DEFAULT_MAP);
        assertThat(testCampaign.getMapContentType()).isEqualTo(DEFAULT_MAP_CONTENT_TYPE);
        assertThat(testCampaign.getMasterId()).isEqualTo(UPDATED_MASTER_ID);
    }

    @Test
    void fullUpdateCampaignWithPatch() throws Exception {
        // Initialize the database
        campaignRepository.save(campaign).block();

        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();

        // Update the campaign using partial update
        Campaign partialUpdatedCampaign = new Campaign();
        partialUpdatedCampaign.setId(campaign.getId());

        partialUpdatedCampaign
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .map(UPDATED_MAP)
            .mapContentType(UPDATED_MAP_CONTENT_TYPE)
            .masterId(UPDATED_MASTER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCampaign.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCampaign))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCampaign.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCampaign.getMap()).isEqualTo(UPDATED_MAP);
        assertThat(testCampaign.getMapContentType()).isEqualTo(UPDATED_MAP_CONTENT_TYPE);
        assertThat(testCampaign.getMasterId()).isEqualTo(UPDATED_MASTER_ID);
    }

    @Test
    void patchNonExistingCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();
        campaign.setId(count.incrementAndGet());

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, campaignDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();
        campaign.setId(count.incrementAndGet());

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();
        campaign.setId(count.incrementAndGet());

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCampaign() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        int databaseSizeBeforeDelete = campaignRepository.findAll().collectList().block().size();

        // Delete the campaign
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, campaign.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
