package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.CampaignUser;
import com.adi.cms.gateway.domain.User;
import com.adi.cms.gateway.repository.CampaignUserRepository;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.service.dto.CampaignUserDTO;
import com.adi.cms.gateway.service.mapper.CampaignUserMapper;
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

/**
 * Integration tests for the {@link CampaignUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CampaignUserResourceIT {

    private static final String ENTITY_API_URL = "/api/campaign-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CampaignUserRepository campaignUserRepository;

    @Autowired
    private CampaignUserMapper campaignUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CampaignUser campaignUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CampaignUser createEntity(EntityManager em) {
        CampaignUser campaignUser = new CampaignUser();
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity(em)).block();
        campaignUser.setUser(user);
        return campaignUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CampaignUser createUpdatedEntity(EntityManager em) {
        CampaignUser campaignUser = new CampaignUser();
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity(em)).block();
        campaignUser.setUser(user);
        return campaignUser;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CampaignUser.class).block();
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
        campaignUser = createEntity(em);
    }

    @Test
    void createCampaignUser() throws Exception {
        int databaseSizeBeforeCreate = campaignUserRepository.findAll().collectList().block().size();
        // Create the CampaignUser
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll().collectList().block();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeCreate + 1);
        CampaignUser testCampaignUser = campaignUserList.get(campaignUserList.size() - 1);
    }

    @Test
    void createCampaignUserWithExistingId() throws Exception {
        // Create the CampaignUser with an existing ID
        campaignUser.setId(1L);
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);

        int databaseSizeBeforeCreate = campaignUserRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll().collectList().block();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCampaignUsers() {
        // Initialize the database
        campaignUserRepository.save(campaignUser).block();

        // Get all the campaignUserList
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
            .value(hasItem(campaignUser.getId().intValue()));
    }

    @Test
    void getCampaignUser() {
        // Initialize the database
        campaignUserRepository.save(campaignUser).block();

        // Get the campaignUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, campaignUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(campaignUser.getId().intValue()));
    }

    @Test
    void getNonExistingCampaignUser() {
        // Get the campaignUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCampaignUser() throws Exception {
        // Initialize the database
        campaignUserRepository.save(campaignUser).block();

        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().collectList().block().size();

        // Update the campaignUser
        CampaignUser updatedCampaignUser = campaignUserRepository.findById(campaignUser.getId()).block();
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(updatedCampaignUser);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, campaignUserDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll().collectList().block();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
        CampaignUser testCampaignUser = campaignUserList.get(campaignUserList.size() - 1);
    }

    @Test
    void putNonExistingCampaignUser() throws Exception {
        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().collectList().block().size();
        campaignUser.setId(count.incrementAndGet());

        // Create the CampaignUser
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, campaignUserDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll().collectList().block();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCampaignUser() throws Exception {
        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().collectList().block().size();
        campaignUser.setId(count.incrementAndGet());

        // Create the CampaignUser
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll().collectList().block();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCampaignUser() throws Exception {
        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().collectList().block().size();
        campaignUser.setId(count.incrementAndGet());

        // Create the CampaignUser
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll().collectList().block();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCampaignUserWithPatch() throws Exception {
        // Initialize the database
        campaignUserRepository.save(campaignUser).block();

        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().collectList().block().size();

        // Update the campaignUser using partial update
        CampaignUser partialUpdatedCampaignUser = new CampaignUser();
        partialUpdatedCampaignUser.setId(campaignUser.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCampaignUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCampaignUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll().collectList().block();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
        CampaignUser testCampaignUser = campaignUserList.get(campaignUserList.size() - 1);
    }

    @Test
    void fullUpdateCampaignUserWithPatch() throws Exception {
        // Initialize the database
        campaignUserRepository.save(campaignUser).block();

        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().collectList().block().size();

        // Update the campaignUser using partial update
        CampaignUser partialUpdatedCampaignUser = new CampaignUser();
        partialUpdatedCampaignUser.setId(campaignUser.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCampaignUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCampaignUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll().collectList().block();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
        CampaignUser testCampaignUser = campaignUserList.get(campaignUserList.size() - 1);
    }

    @Test
    void patchNonExistingCampaignUser() throws Exception {
        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().collectList().block().size();
        campaignUser.setId(count.incrementAndGet());

        // Create the CampaignUser
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, campaignUserDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll().collectList().block();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCampaignUser() throws Exception {
        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().collectList().block().size();
        campaignUser.setId(count.incrementAndGet());

        // Create the CampaignUser
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll().collectList().block();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCampaignUser() throws Exception {
        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().collectList().block().size();
        campaignUser.setId(count.incrementAndGet());

        // Create the CampaignUser
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll().collectList().block();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCampaignUser() {
        // Initialize the database
        campaignUserRepository.save(campaignUser).block();

        int databaseSizeBeforeDelete = campaignUserRepository.findAll().collectList().block().size();

        // Delete the campaignUser
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, campaignUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll().collectList().block();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
