package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.XaracterEquippedWeapon;
import com.adi.cms.gateway.domain.enumeration.Handedness;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.repository.XaracterEquippedWeaponRepository;
import com.adi.cms.gateway.service.dto.XaracterEquippedWeaponDTO;
import com.adi.cms.gateway.service.mapper.XaracterEquippedWeaponMapper;
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
 * Integration tests for the {@link XaracterEquippedWeaponResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class XaracterEquippedWeaponResourceIT {

    private static final Long DEFAULT_XARACTER_ID = 1L;
    private static final Long UPDATED_XARACTER_ID = 2L;

    private static final Handedness DEFAULT_HAND = Handedness.RIGHT;
    private static final Handedness UPDATED_HAND = Handedness.LEFT;

    private static final String ENTITY_API_URL = "/api/xaracter-equipped-weapons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private XaracterEquippedWeaponRepository xaracterEquippedWeaponRepository;

    @Autowired
    private XaracterEquippedWeaponMapper xaracterEquippedWeaponMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private XaracterEquippedWeapon xaracterEquippedWeapon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static XaracterEquippedWeapon createEntity(EntityManager em) {
        XaracterEquippedWeapon xaracterEquippedWeapon = new XaracterEquippedWeapon().xaracterId(DEFAULT_XARACTER_ID).hand(DEFAULT_HAND);
        return xaracterEquippedWeapon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static XaracterEquippedWeapon createUpdatedEntity(EntityManager em) {
        XaracterEquippedWeapon xaracterEquippedWeapon = new XaracterEquippedWeapon().xaracterId(UPDATED_XARACTER_ID).hand(UPDATED_HAND);
        return xaracterEquippedWeapon;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(XaracterEquippedWeapon.class).block();
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
        xaracterEquippedWeapon = createEntity(em);
    }

    @Test
    void createXaracterEquippedWeapon() throws Exception {
        int databaseSizeBeforeCreate = xaracterEquippedWeaponRepository.findAll().collectList().block().size();
        // Create the XaracterEquippedWeapon
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeCreate + 1);
        XaracterEquippedWeapon testXaracterEquippedWeapon = xaracterEquippedWeaponList.get(xaracterEquippedWeaponList.size() - 1);
        assertThat(testXaracterEquippedWeapon.getXaracterId()).isEqualTo(DEFAULT_XARACTER_ID);
        assertThat(testXaracterEquippedWeapon.getHand()).isEqualTo(DEFAULT_HAND);
    }

    @Test
    void createXaracterEquippedWeaponWithExistingId() throws Exception {
        // Create the XaracterEquippedWeapon with an existing ID
        xaracterEquippedWeapon.setId(1L);
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);

        int databaseSizeBeforeCreate = xaracterEquippedWeaponRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllXaracterEquippedWeapons() {
        // Initialize the database
        xaracterEquippedWeaponRepository.save(xaracterEquippedWeapon).block();

        // Get all the xaracterEquippedWeaponList
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
            .value(hasItem(xaracterEquippedWeapon.getId().intValue()))
            .jsonPath("$.[*].xaracterId")
            .value(hasItem(DEFAULT_XARACTER_ID.intValue()))
            .jsonPath("$.[*].hand")
            .value(hasItem(DEFAULT_HAND.toString()));
    }

    @Test
    void getXaracterEquippedWeapon() {
        // Initialize the database
        xaracterEquippedWeaponRepository.save(xaracterEquippedWeapon).block();

        // Get the xaracterEquippedWeapon
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, xaracterEquippedWeapon.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(xaracterEquippedWeapon.getId().intValue()))
            .jsonPath("$.xaracterId")
            .value(is(DEFAULT_XARACTER_ID.intValue()))
            .jsonPath("$.hand")
            .value(is(DEFAULT_HAND.toString()));
    }

    @Test
    void getNonExistingXaracterEquippedWeapon() {
        // Get the xaracterEquippedWeapon
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewXaracterEquippedWeapon() throws Exception {
        // Initialize the database
        xaracterEquippedWeaponRepository.save(xaracterEquippedWeapon).block();

        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().collectList().block().size();

        // Update the xaracterEquippedWeapon
        XaracterEquippedWeapon updatedXaracterEquippedWeapon = xaracterEquippedWeaponRepository
            .findById(xaracterEquippedWeapon.getId())
            .block();
        updatedXaracterEquippedWeapon.xaracterId(UPDATED_XARACTER_ID).hand(UPDATED_HAND);
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(updatedXaracterEquippedWeapon);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, xaracterEquippedWeaponDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
        XaracterEquippedWeapon testXaracterEquippedWeapon = xaracterEquippedWeaponList.get(xaracterEquippedWeaponList.size() - 1);
        assertThat(testXaracterEquippedWeapon.getXaracterId()).isEqualTo(UPDATED_XARACTER_ID);
        assertThat(testXaracterEquippedWeapon.getHand()).isEqualTo(UPDATED_HAND);
    }

    @Test
    void putNonExistingXaracterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().collectList().block().size();
        xaracterEquippedWeapon.setId(count.incrementAndGet());

        // Create the XaracterEquippedWeapon
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, xaracterEquippedWeaponDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchXaracterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().collectList().block().size();
        xaracterEquippedWeapon.setId(count.incrementAndGet());

        // Create the XaracterEquippedWeapon
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamXaracterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().collectList().block().size();
        xaracterEquippedWeapon.setId(count.incrementAndGet());

        // Create the XaracterEquippedWeapon
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateXaracterEquippedWeaponWithPatch() throws Exception {
        // Initialize the database
        xaracterEquippedWeaponRepository.save(xaracterEquippedWeapon).block();

        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().collectList().block().size();

        // Update the xaracterEquippedWeapon using partial update
        XaracterEquippedWeapon partialUpdatedXaracterEquippedWeapon = new XaracterEquippedWeapon();
        partialUpdatedXaracterEquippedWeapon.setId(xaracterEquippedWeapon.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedXaracterEquippedWeapon.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterEquippedWeapon))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
        XaracterEquippedWeapon testXaracterEquippedWeapon = xaracterEquippedWeaponList.get(xaracterEquippedWeaponList.size() - 1);
        assertThat(testXaracterEquippedWeapon.getXaracterId()).isEqualTo(DEFAULT_XARACTER_ID);
        assertThat(testXaracterEquippedWeapon.getHand()).isEqualTo(DEFAULT_HAND);
    }

    @Test
    void fullUpdateXaracterEquippedWeaponWithPatch() throws Exception {
        // Initialize the database
        xaracterEquippedWeaponRepository.save(xaracterEquippedWeapon).block();

        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().collectList().block().size();

        // Update the xaracterEquippedWeapon using partial update
        XaracterEquippedWeapon partialUpdatedXaracterEquippedWeapon = new XaracterEquippedWeapon();
        partialUpdatedXaracterEquippedWeapon.setId(xaracterEquippedWeapon.getId());

        partialUpdatedXaracterEquippedWeapon.xaracterId(UPDATED_XARACTER_ID).hand(UPDATED_HAND);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedXaracterEquippedWeapon.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterEquippedWeapon))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
        XaracterEquippedWeapon testXaracterEquippedWeapon = xaracterEquippedWeaponList.get(xaracterEquippedWeaponList.size() - 1);
        assertThat(testXaracterEquippedWeapon.getXaracterId()).isEqualTo(UPDATED_XARACTER_ID);
        assertThat(testXaracterEquippedWeapon.getHand()).isEqualTo(UPDATED_HAND);
    }

    @Test
    void patchNonExistingXaracterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().collectList().block().size();
        xaracterEquippedWeapon.setId(count.incrementAndGet());

        // Create the XaracterEquippedWeapon
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, xaracterEquippedWeaponDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchXaracterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().collectList().block().size();
        xaracterEquippedWeapon.setId(count.incrementAndGet());

        // Create the XaracterEquippedWeapon
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamXaracterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().collectList().block().size();
        xaracterEquippedWeapon.setId(count.incrementAndGet());

        // Create the XaracterEquippedWeapon
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteXaracterEquippedWeapon() {
        // Initialize the database
        xaracterEquippedWeaponRepository.save(xaracterEquippedWeapon).block();

        int databaseSizeBeforeDelete = xaracterEquippedWeaponRepository.findAll().collectList().block().size();

        // Delete the xaracterEquippedWeapon
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, xaracterEquippedWeapon.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll().collectList().block();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
