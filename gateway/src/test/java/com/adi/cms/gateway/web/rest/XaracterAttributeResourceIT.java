package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.XaracterAttribute;
import com.adi.cms.gateway.domain.enumeration.AttributeName;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.repository.XaracterAttributeRepository;
import com.adi.cms.gateway.service.dto.XaracterAttributeDTO;
import com.adi.cms.gateway.service.mapper.XaracterAttributeMapper;
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
 * Integration tests for the {@link XaracterAttributeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(XaracterAttribute.class).block();
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
        xaracterAttribute = createEntity(em);
    }

    @Test
    void createXaracterAttribute() throws Exception {
        int databaseSizeBeforeCreate = xaracterAttributeRepository.findAll().collectList().block().size();
        // Create the XaracterAttribute
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll().collectList().block();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeCreate + 1);
        XaracterAttribute testXaracterAttribute = xaracterAttributeList.get(xaracterAttributeList.size() - 1);
        assertThat(testXaracterAttribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testXaracterAttribute.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testXaracterAttribute.getAttributeModifier()).isEqualTo(DEFAULT_ATTRIBUTE_MODIFIER);
    }

    @Test
    void createXaracterAttributeWithExistingId() throws Exception {
        // Create the XaracterAttribute with an existing ID
        xaracterAttribute.setId(1L);
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        int databaseSizeBeforeCreate = xaracterAttributeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll().collectList().block();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = xaracterAttributeRepository.findAll().collectList().block().size();
        // set the field null
        xaracterAttribute.setName(null);

        // Create the XaracterAttribute, which fails.
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll().collectList().block();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = xaracterAttributeRepository.findAll().collectList().block().size();
        // set the field null
        xaracterAttribute.setPoints(null);

        // Create the XaracterAttribute, which fails.
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll().collectList().block();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllXaracterAttributes() {
        // Initialize the database
        xaracterAttributeRepository.save(xaracterAttribute).block();

        // Get all the xaracterAttributeList
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
            .value(hasItem(xaracterAttribute.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME.toString()))
            .jsonPath("$.[*].points")
            .value(hasItem(DEFAULT_POINTS))
            .jsonPath("$.[*].attributeModifier")
            .value(hasItem(DEFAULT_ATTRIBUTE_MODIFIER));
    }

    @Test
    void getXaracterAttribute() {
        // Initialize the database
        xaracterAttributeRepository.save(xaracterAttribute).block();

        // Get the xaracterAttribute
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, xaracterAttribute.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(xaracterAttribute.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME.toString()))
            .jsonPath("$.points")
            .value(is(DEFAULT_POINTS))
            .jsonPath("$.attributeModifier")
            .value(is(DEFAULT_ATTRIBUTE_MODIFIER));
    }

    @Test
    void getNonExistingXaracterAttribute() {
        // Get the xaracterAttribute
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewXaracterAttribute() throws Exception {
        // Initialize the database
        xaracterAttributeRepository.save(xaracterAttribute).block();

        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().collectList().block().size();

        // Update the xaracterAttribute
        XaracterAttribute updatedXaracterAttribute = xaracterAttributeRepository.findById(xaracterAttribute.getId()).block();
        updatedXaracterAttribute.name(UPDATED_NAME).points(UPDATED_POINTS).attributeModifier(UPDATED_ATTRIBUTE_MODIFIER);
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(updatedXaracterAttribute);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, xaracterAttributeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll().collectList().block();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
        XaracterAttribute testXaracterAttribute = xaracterAttributeList.get(xaracterAttributeList.size() - 1);
        assertThat(testXaracterAttribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testXaracterAttribute.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testXaracterAttribute.getAttributeModifier()).isEqualTo(UPDATED_ATTRIBUTE_MODIFIER);
    }

    @Test
    void putNonExistingXaracterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().collectList().block().size();
        xaracterAttribute.setId(count.incrementAndGet());

        // Create the XaracterAttribute
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, xaracterAttributeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll().collectList().block();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchXaracterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().collectList().block().size();
        xaracterAttribute.setId(count.incrementAndGet());

        // Create the XaracterAttribute
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll().collectList().block();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamXaracterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().collectList().block().size();
        xaracterAttribute.setId(count.incrementAndGet());

        // Create the XaracterAttribute
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll().collectList().block();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateXaracterAttributeWithPatch() throws Exception {
        // Initialize the database
        xaracterAttributeRepository.save(xaracterAttribute).block();

        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().collectList().block().size();

        // Update the xaracterAttribute using partial update
        XaracterAttribute partialUpdatedXaracterAttribute = new XaracterAttribute();
        partialUpdatedXaracterAttribute.setId(xaracterAttribute.getId());

        partialUpdatedXaracterAttribute.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedXaracterAttribute.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterAttribute))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll().collectList().block();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
        XaracterAttribute testXaracterAttribute = xaracterAttributeList.get(xaracterAttributeList.size() - 1);
        assertThat(testXaracterAttribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testXaracterAttribute.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testXaracterAttribute.getAttributeModifier()).isEqualTo(DEFAULT_ATTRIBUTE_MODIFIER);
    }

    @Test
    void fullUpdateXaracterAttributeWithPatch() throws Exception {
        // Initialize the database
        xaracterAttributeRepository.save(xaracterAttribute).block();

        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().collectList().block().size();

        // Update the xaracterAttribute using partial update
        XaracterAttribute partialUpdatedXaracterAttribute = new XaracterAttribute();
        partialUpdatedXaracterAttribute.setId(xaracterAttribute.getId());

        partialUpdatedXaracterAttribute.name(UPDATED_NAME).points(UPDATED_POINTS).attributeModifier(UPDATED_ATTRIBUTE_MODIFIER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedXaracterAttribute.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterAttribute))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll().collectList().block();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
        XaracterAttribute testXaracterAttribute = xaracterAttributeList.get(xaracterAttributeList.size() - 1);
        assertThat(testXaracterAttribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testXaracterAttribute.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testXaracterAttribute.getAttributeModifier()).isEqualTo(UPDATED_ATTRIBUTE_MODIFIER);
    }

    @Test
    void patchNonExistingXaracterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().collectList().block().size();
        xaracterAttribute.setId(count.incrementAndGet());

        // Create the XaracterAttribute
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, xaracterAttributeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll().collectList().block();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchXaracterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().collectList().block().size();
        xaracterAttribute.setId(count.incrementAndGet());

        // Create the XaracterAttribute
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll().collectList().block();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamXaracterAttribute() throws Exception {
        int databaseSizeBeforeUpdate = xaracterAttributeRepository.findAll().collectList().block().size();
        xaracterAttribute.setId(count.incrementAndGet());

        // Create the XaracterAttribute
        XaracterAttributeDTO xaracterAttributeDTO = xaracterAttributeMapper.toDto(xaracterAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(xaracterAttributeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the XaracterAttribute in the database
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll().collectList().block();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteXaracterAttribute() {
        // Initialize the database
        xaracterAttributeRepository.save(xaracterAttribute).block();

        int databaseSizeBeforeDelete = xaracterAttributeRepository.findAll().collectList().block().size();

        // Delete the xaracterAttribute
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, xaracterAttribute.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<XaracterAttribute> xaracterAttributeList = xaracterAttributeRepository.findAll().collectList().block();
        assertThat(xaracterAttributeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
