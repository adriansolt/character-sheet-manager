package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.WeaponManeuver;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.repository.WeaponManeuverRepository;
import com.adi.cms.gateway.service.dto.WeaponManeuverDTO;
import com.adi.cms.gateway.service.mapper.WeaponManeuverMapper;
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
 * Integration tests for the {@link WeaponManeuverResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class WeaponManeuverResourceIT {

    private static final String ENTITY_API_URL = "/api/weapon-maneuvers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WeaponManeuverRepository weaponManeuverRepository;

    @Autowired
    private WeaponManeuverMapper weaponManeuverMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private WeaponManeuver weaponManeuver;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeaponManeuver createEntity(EntityManager em) {
        WeaponManeuver weaponManeuver = new WeaponManeuver();
        return weaponManeuver;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeaponManeuver createUpdatedEntity(EntityManager em) {
        WeaponManeuver weaponManeuver = new WeaponManeuver();
        return weaponManeuver;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(WeaponManeuver.class).block();
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
        weaponManeuver = createEntity(em);
    }

    @Test
    void createWeaponManeuver() throws Exception {
        int databaseSizeBeforeCreate = weaponManeuverRepository.findAll().collectList().block().size();
        // Create the WeaponManeuver
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll().collectList().block();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeCreate + 1);
        WeaponManeuver testWeaponManeuver = weaponManeuverList.get(weaponManeuverList.size() - 1);
    }

    @Test
    void createWeaponManeuverWithExistingId() throws Exception {
        // Create the WeaponManeuver with an existing ID
        weaponManeuver.setId(1L);
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);

        int databaseSizeBeforeCreate = weaponManeuverRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll().collectList().block();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllWeaponManeuvers() {
        // Initialize the database
        weaponManeuverRepository.save(weaponManeuver).block();

        // Get all the weaponManeuverList
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
            .value(hasItem(weaponManeuver.getId().intValue()));
    }

    @Test
    void getWeaponManeuver() {
        // Initialize the database
        weaponManeuverRepository.save(weaponManeuver).block();

        // Get the weaponManeuver
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, weaponManeuver.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(weaponManeuver.getId().intValue()));
    }

    @Test
    void getNonExistingWeaponManeuver() {
        // Get the weaponManeuver
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewWeaponManeuver() throws Exception {
        // Initialize the database
        weaponManeuverRepository.save(weaponManeuver).block();

        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().collectList().block().size();

        // Update the weaponManeuver
        WeaponManeuver updatedWeaponManeuver = weaponManeuverRepository.findById(weaponManeuver.getId()).block();
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(updatedWeaponManeuver);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, weaponManeuverDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll().collectList().block();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
        WeaponManeuver testWeaponManeuver = weaponManeuverList.get(weaponManeuverList.size() - 1);
    }

    @Test
    void putNonExistingWeaponManeuver() throws Exception {
        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().collectList().block().size();
        weaponManeuver.setId(count.incrementAndGet());

        // Create the WeaponManeuver
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, weaponManeuverDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll().collectList().block();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchWeaponManeuver() throws Exception {
        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().collectList().block().size();
        weaponManeuver.setId(count.incrementAndGet());

        // Create the WeaponManeuver
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll().collectList().block();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamWeaponManeuver() throws Exception {
        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().collectList().block().size();
        weaponManeuver.setId(count.incrementAndGet());

        // Create the WeaponManeuver
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll().collectList().block();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateWeaponManeuverWithPatch() throws Exception {
        // Initialize the database
        weaponManeuverRepository.save(weaponManeuver).block();

        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().collectList().block().size();

        // Update the weaponManeuver using partial update
        WeaponManeuver partialUpdatedWeaponManeuver = new WeaponManeuver();
        partialUpdatedWeaponManeuver.setId(weaponManeuver.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWeaponManeuver.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWeaponManeuver))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll().collectList().block();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
        WeaponManeuver testWeaponManeuver = weaponManeuverList.get(weaponManeuverList.size() - 1);
    }

    @Test
    void fullUpdateWeaponManeuverWithPatch() throws Exception {
        // Initialize the database
        weaponManeuverRepository.save(weaponManeuver).block();

        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().collectList().block().size();

        // Update the weaponManeuver using partial update
        WeaponManeuver partialUpdatedWeaponManeuver = new WeaponManeuver();
        partialUpdatedWeaponManeuver.setId(weaponManeuver.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWeaponManeuver.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWeaponManeuver))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll().collectList().block();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
        WeaponManeuver testWeaponManeuver = weaponManeuverList.get(weaponManeuverList.size() - 1);
    }

    @Test
    void patchNonExistingWeaponManeuver() throws Exception {
        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().collectList().block().size();
        weaponManeuver.setId(count.incrementAndGet());

        // Create the WeaponManeuver
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, weaponManeuverDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll().collectList().block();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchWeaponManeuver() throws Exception {
        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().collectList().block().size();
        weaponManeuver.setId(count.incrementAndGet());

        // Create the WeaponManeuver
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll().collectList().block();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamWeaponManeuver() throws Exception {
        int databaseSizeBeforeUpdate = weaponManeuverRepository.findAll().collectList().block().size();
        weaponManeuver.setId(count.incrementAndGet());

        // Create the WeaponManeuver
        WeaponManeuverDTO weaponManeuverDTO = weaponManeuverMapper.toDto(weaponManeuver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponManeuverDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the WeaponManeuver in the database
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll().collectList().block();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteWeaponManeuver() {
        // Initialize the database
        weaponManeuverRepository.save(weaponManeuver).block();

        int databaseSizeBeforeDelete = weaponManeuverRepository.findAll().collectList().block().size();

        // Delete the weaponManeuver
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, weaponManeuver.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<WeaponManeuver> weaponManeuverList = weaponManeuverRepository.findAll().collectList().block();
        assertThat(weaponManeuverList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
