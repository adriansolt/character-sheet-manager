package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.XaracterEquippedArmor;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.repository.XaracterEquippedArmorRepository;
import com.adi.cms.gateway.service.dto.XaracterEquippedArmorDTO;
import com.adi.cms.gateway.service.mapper.XaracterEquippedArmorMapper;
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
 * Integration tests for the {@link XaracterEquippedArmorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class XaracterEquippedArmorResourceIT {

    private static final Long DEFAULT_XARACTER_ID = 1L;
    private static final Long UPDATED_XARACTER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/xaracter-equipped-armors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private XaracterEquippedArmorRepository xaracterEquippedArmorRepository;

    @Autowired
    private XaracterEquippedArmorMapper xaracterEquippedArmorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private XaracterEquippedArmor xaracterEquippedArmor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static XaracterEquippedArmor createEntity(EntityManager em) {
        XaracterEquippedArmor xaracterEquippedArmor = new XaracterEquippedArmor().xaracterId(DEFAULT_XARACTER_ID);
        return xaracterEquippedArmor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static XaracterEquippedArmor createUpdatedEntity(EntityManager em) {
        XaracterEquippedArmor xaracterEquippedArmor = new XaracterEquippedArmor().xaracterId(UPDATED_XARACTER_ID);
        return xaracterEquippedArmor;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(XaracterEquippedArmor.class).block();
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
        xaracterEquippedArmor = createEntity(em);
    }

    @Test
    void createXaracterEquippedArmor() throws Exception {
        int databaseSizeBeforeCreate = xaracterEquippedArmorRepository.findAll().collectList().block().size();
        // Create the XaracterEquippedArmor
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll().collectList().block();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeCreate + 1);
        XaracterEquippedArmor testXaracterEquippedArmor = xaracterEquippedArmorList.get(xaracterEquippedArmorList.size() - 1);
        assertThat(testXaracterEquippedArmor.getXaracterId()).isEqualTo(DEFAULT_XARACTER_ID);
    }

    @Test
    void createXaracterEquippedArmorWithExistingId() throws Exception {
        // Create the XaracterEquippedArmor with an existing ID
        xaracterEquippedArmor.setId(1L);
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);

        int databaseSizeBeforeCreate = xaracterEquippedArmorRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll().collectList().block();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllXaracterEquippedArmors() {
        // Initialize the database
        xaracterEquippedArmorRepository.save(xaracterEquippedArmor).block();

        // Get all the xaracterEquippedArmorList
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
            .value(hasItem(xaracterEquippedArmor.getId().intValue()))
            .jsonPath("$.[*].xaracterId")
            .value(hasItem(DEFAULT_XARACTER_ID.intValue()));
    }

    @Test
    void getXaracterEquippedArmor() {
        // Initialize the database
        xaracterEquippedArmorRepository.save(xaracterEquippedArmor).block();

        // Get the xaracterEquippedArmor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, xaracterEquippedArmor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(xaracterEquippedArmor.getId().intValue()))
            .jsonPath("$.xaracterId")
            .value(is(DEFAULT_XARACTER_ID.intValue()));
    }

    @Test
    void getNonExistingXaracterEquippedArmor() {
        // Get the xaracterEquippedArmor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewXaracterEquippedArmor() throws Exception {
        // Initialize the database
        xaracterEquippedArmorRepository.save(xaracterEquippedArmor).block();

        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().collectList().block().size();

        // Update the xaracterEquippedArmor
        XaracterEquippedArmor updatedXaracterEquippedArmor = xaracterEquippedArmorRepository
            .findById(xaracterEquippedArmor.getId())
            .block();
        updatedXaracterEquippedArmor.xaracterId(UPDATED_XARACTER_ID);
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(updatedXaracterEquippedArmor);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, xaracterEquippedArmorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll().collectList().block();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
        XaracterEquippedArmor testXaracterEquippedArmor = xaracterEquippedArmorList.get(xaracterEquippedArmorList.size() - 1);
        assertThat(testXaracterEquippedArmor.getXaracterId()).isEqualTo(UPDATED_XARACTER_ID);
    }

    @Test
    void putNonExistingXaracterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().collectList().block().size();
        xaracterEquippedArmor.setId(count.incrementAndGet());

        // Create the XaracterEquippedArmor
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, xaracterEquippedArmorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll().collectList().block();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchXaracterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().collectList().block().size();
        xaracterEquippedArmor.setId(count.incrementAndGet());

        // Create the XaracterEquippedArmor
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll().collectList().block();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamXaracterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().collectList().block().size();
        xaracterEquippedArmor.setId(count.incrementAndGet());

        // Create the XaracterEquippedArmor
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll().collectList().block();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateXaracterEquippedArmorWithPatch() throws Exception {
        // Initialize the database
        xaracterEquippedArmorRepository.save(xaracterEquippedArmor).block();

        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().collectList().block().size();

        // Update the xaracterEquippedArmor using partial update
        XaracterEquippedArmor partialUpdatedXaracterEquippedArmor = new XaracterEquippedArmor();
        partialUpdatedXaracterEquippedArmor.setId(xaracterEquippedArmor.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedXaracterEquippedArmor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterEquippedArmor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll().collectList().block();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
        XaracterEquippedArmor testXaracterEquippedArmor = xaracterEquippedArmorList.get(xaracterEquippedArmorList.size() - 1);
        assertThat(testXaracterEquippedArmor.getXaracterId()).isEqualTo(DEFAULT_XARACTER_ID);
    }

    @Test
    void fullUpdateXaracterEquippedArmorWithPatch() throws Exception {
        // Initialize the database
        xaracterEquippedArmorRepository.save(xaracterEquippedArmor).block();

        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().collectList().block().size();

        // Update the xaracterEquippedArmor using partial update
        XaracterEquippedArmor partialUpdatedXaracterEquippedArmor = new XaracterEquippedArmor();
        partialUpdatedXaracterEquippedArmor.setId(xaracterEquippedArmor.getId());

        partialUpdatedXaracterEquippedArmor.xaracterId(UPDATED_XARACTER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedXaracterEquippedArmor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterEquippedArmor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll().collectList().block();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
        XaracterEquippedArmor testXaracterEquippedArmor = xaracterEquippedArmorList.get(xaracterEquippedArmorList.size() - 1);
        assertThat(testXaracterEquippedArmor.getXaracterId()).isEqualTo(UPDATED_XARACTER_ID);
    }

    @Test
    void patchNonExistingXaracterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().collectList().block().size();
        xaracterEquippedArmor.setId(count.incrementAndGet());

        // Create the XaracterEquippedArmor
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, xaracterEquippedArmorDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll().collectList().block();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchXaracterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().collectList().block().size();
        xaracterEquippedArmor.setId(count.incrementAndGet());

        // Create the XaracterEquippedArmor
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll().collectList().block();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamXaracterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().collectList().block().size();
        xaracterEquippedArmor.setId(count.incrementAndGet());

        // Create the XaracterEquippedArmor
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll().collectList().block();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteXaracterEquippedArmor() {
        // Initialize the database
        xaracterEquippedArmorRepository.save(xaracterEquippedArmor).block();

        int databaseSizeBeforeDelete = xaracterEquippedArmorRepository.findAll().collectList().block().size();

        // Delete the xaracterEquippedArmor
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, xaracterEquippedArmor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll().collectList().block();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
