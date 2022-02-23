package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.XaracterSkill;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.repository.XaracterSkillRepository;
import com.adi.cms.gateway.service.dto.XaracterSkillDTO;
import com.adi.cms.gateway.service.mapper.XaracterSkillMapper;
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
 * Integration tests for the {@link XaracterSkillResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(XaracterSkill.class).block();
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
        xaracterSkill = createEntity(em);
    }

    @Test
    void createXaracterSkill() throws Exception {
        int databaseSizeBeforeCreate = xaracterSkillRepository.findAll().collectList().block().size();
        // Create the XaracterSkill
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll().collectList().block();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeCreate + 1);
        XaracterSkill testXaracterSkill = xaracterSkillList.get(xaracterSkillList.size() - 1);
        assertThat(testXaracterSkill.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testXaracterSkill.getSkillModifier()).isEqualTo(DEFAULT_SKILL_MODIFIER);
    }

    @Test
    void createXaracterSkillWithExistingId() throws Exception {
        // Create the XaracterSkill with an existing ID
        xaracterSkill.setId(1L);
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        int databaseSizeBeforeCreate = xaracterSkillRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll().collectList().block();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = xaracterSkillRepository.findAll().collectList().block().size();
        // set the field null
        xaracterSkill.setPoints(null);

        // Create the XaracterSkill, which fails.
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll().collectList().block();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllXaracterSkills() {
        // Initialize the database
        xaracterSkillRepository.save(xaracterSkill).block();

        // Get all the xaracterSkillList
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
            .value(hasItem(xaracterSkill.getId().intValue()))
            .jsonPath("$.[*].points")
            .value(hasItem(DEFAULT_POINTS))
            .jsonPath("$.[*].skillModifier")
            .value(hasItem(DEFAULT_SKILL_MODIFIER));
    }

    @Test
    void getXaracterSkill() {
        // Initialize the database
        xaracterSkillRepository.save(xaracterSkill).block();

        // Get the xaracterSkill
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, xaracterSkill.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(xaracterSkill.getId().intValue()))
            .jsonPath("$.points")
            .value(is(DEFAULT_POINTS))
            .jsonPath("$.skillModifier")
            .value(is(DEFAULT_SKILL_MODIFIER));
    }

    @Test
    void getNonExistingXaracterSkill() {
        // Get the xaracterSkill
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewXaracterSkill() throws Exception {
        // Initialize the database
        xaracterSkillRepository.save(xaracterSkill).block();

        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().collectList().block().size();

        // Update the xaracterSkill
        XaracterSkill updatedXaracterSkill = xaracterSkillRepository.findById(xaracterSkill.getId()).block();
        updatedXaracterSkill.points(UPDATED_POINTS).skillModifier(UPDATED_SKILL_MODIFIER);
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(updatedXaracterSkill);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, xaracterSkillDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll().collectList().block();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
        XaracterSkill testXaracterSkill = xaracterSkillList.get(xaracterSkillList.size() - 1);
        assertThat(testXaracterSkill.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testXaracterSkill.getSkillModifier()).isEqualTo(UPDATED_SKILL_MODIFIER);
    }

    @Test
    void putNonExistingXaracterSkill() throws Exception {
        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().collectList().block().size();
        xaracterSkill.setId(count.incrementAndGet());

        // Create the XaracterSkill
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, xaracterSkillDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll().collectList().block();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchXaracterSkill() throws Exception {
        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().collectList().block().size();
        xaracterSkill.setId(count.incrementAndGet());

        // Create the XaracterSkill
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll().collectList().block();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamXaracterSkill() throws Exception {
        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().collectList().block().size();
        xaracterSkill.setId(count.incrementAndGet());

        // Create the XaracterSkill
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll().collectList().block();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateXaracterSkillWithPatch() throws Exception {
        // Initialize the database
        xaracterSkillRepository.save(xaracterSkill).block();

        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().collectList().block().size();

        // Update the xaracterSkill using partial update
        XaracterSkill partialUpdatedXaracterSkill = new XaracterSkill();
        partialUpdatedXaracterSkill.setId(xaracterSkill.getId());

        partialUpdatedXaracterSkill.points(UPDATED_POINTS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedXaracterSkill.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterSkill))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll().collectList().block();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
        XaracterSkill testXaracterSkill = xaracterSkillList.get(xaracterSkillList.size() - 1);
        assertThat(testXaracterSkill.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testXaracterSkill.getSkillModifier()).isEqualTo(DEFAULT_SKILL_MODIFIER);
    }

    @Test
    void fullUpdateXaracterSkillWithPatch() throws Exception {
        // Initialize the database
        xaracterSkillRepository.save(xaracterSkill).block();

        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().collectList().block().size();

        // Update the xaracterSkill using partial update
        XaracterSkill partialUpdatedXaracterSkill = new XaracterSkill();
        partialUpdatedXaracterSkill.setId(xaracterSkill.getId());

        partialUpdatedXaracterSkill.points(UPDATED_POINTS).skillModifier(UPDATED_SKILL_MODIFIER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedXaracterSkill.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterSkill))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll().collectList().block();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
        XaracterSkill testXaracterSkill = xaracterSkillList.get(xaracterSkillList.size() - 1);
        assertThat(testXaracterSkill.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testXaracterSkill.getSkillModifier()).isEqualTo(UPDATED_SKILL_MODIFIER);
    }

    @Test
    void patchNonExistingXaracterSkill() throws Exception {
        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().collectList().block().size();
        xaracterSkill.setId(count.incrementAndGet());

        // Create the XaracterSkill
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, xaracterSkillDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll().collectList().block();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchXaracterSkill() throws Exception {
        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().collectList().block().size();
        xaracterSkill.setId(count.incrementAndGet());

        // Create the XaracterSkill
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll().collectList().block();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamXaracterSkill() throws Exception {
        int databaseSizeBeforeUpdate = xaracterSkillRepository.findAll().collectList().block().size();
        xaracterSkill.setId(count.incrementAndGet());

        // Create the XaracterSkill
        XaracterSkillDTO xaracterSkillDTO = xaracterSkillMapper.toDto(xaracterSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterSkillDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the XaracterSkill in the database
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll().collectList().block();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteXaracterSkill() {
        // Initialize the database
        xaracterSkillRepository.save(xaracterSkill).block();

        int databaseSizeBeforeDelete = xaracterSkillRepository.findAll().collectList().block().size();

        // Delete the xaracterSkill
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, xaracterSkill.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<XaracterSkill> xaracterSkillList = xaracterSkillRepository.findAll().collectList().block();
        assertThat(xaracterSkillList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
