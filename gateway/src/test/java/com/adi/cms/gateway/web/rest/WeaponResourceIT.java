package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.Weapon;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.repository.WeaponRepository;
import com.adi.cms.gateway.service.dto.WeaponDTO;
import com.adi.cms.gateway.service.mapper.WeaponMapper;
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
 * Integration tests for the {@link WeaponResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class WeaponResourceIT {

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

    private static final Long DEFAULT_CHARACTER_ID = 1L;
    private static final Long UPDATED_CHARACTER_ID = 2L;

    private static final Long DEFAULT_CAMPAIGN_ID = 1L;
    private static final Long UPDATED_CAMPAIGN_ID = 2L;

    private static final Integer DEFAULT_REACH = 1;
    private static final Integer UPDATED_REACH = 2;

    private static final Integer DEFAULT_BASE_DAMAGE = 0;
    private static final Integer UPDATED_BASE_DAMAGE = 1;

    private static final Integer DEFAULT_REQUIRED_ST = 1;
    private static final Integer UPDATED_REQUIRED_ST = 2;

    private static final Integer DEFAULT_DAMAGE_MODIFIER = 1;
    private static final Integer UPDATED_DAMAGE_MODIFIER = 2;

    private static final String ENTITY_API_URL = "/api/weapons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WeaponRepository weaponRepository;

    @Autowired
    private WeaponMapper weaponMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Weapon weapon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weapon createEntity(EntityManager em) {
        Weapon weapon = new Weapon()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .weight(DEFAULT_WEIGHT)
            .quality(DEFAULT_QUALITY)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .characterId(DEFAULT_CHARACTER_ID)
            .campaignId(DEFAULT_CAMPAIGN_ID)
            .reach(DEFAULT_REACH)
            .baseDamage(DEFAULT_BASE_DAMAGE)
            .requiredST(DEFAULT_REQUIRED_ST)
            .damageModifier(DEFAULT_DAMAGE_MODIFIER);
        return weapon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weapon createUpdatedEntity(EntityManager em) {
        Weapon weapon = new Weapon()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .quality(UPDATED_QUALITY)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .characterId(UPDATED_CHARACTER_ID)
            .campaignId(UPDATED_CAMPAIGN_ID)
            .reach(UPDATED_REACH)
            .baseDamage(UPDATED_BASE_DAMAGE)
            .requiredST(UPDATED_REQUIRED_ST)
            .damageModifier(UPDATED_DAMAGE_MODIFIER);
        return weapon;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Weapon.class).block();
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
        weapon = createEntity(em);
    }

    @Test
    void createWeapon() throws Exception {
        int databaseSizeBeforeCreate = weaponRepository.findAll().collectList().block().size();
        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeCreate + 1);
        Weapon testWeapon = weaponList.get(weaponList.size() - 1);
        assertThat(testWeapon.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWeapon.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWeapon.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testWeapon.getQuality()).isEqualTo(DEFAULT_QUALITY);
        assertThat(testWeapon.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testWeapon.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testWeapon.getCharacterId()).isEqualTo(DEFAULT_CHARACTER_ID);
        assertThat(testWeapon.getCampaignId()).isEqualTo(DEFAULT_CAMPAIGN_ID);
        assertThat(testWeapon.getReach()).isEqualTo(DEFAULT_REACH);
        assertThat(testWeapon.getBaseDamage()).isEqualTo(DEFAULT_BASE_DAMAGE);
        assertThat(testWeapon.getRequiredST()).isEqualTo(DEFAULT_REQUIRED_ST);
        assertThat(testWeapon.getDamageModifier()).isEqualTo(DEFAULT_DAMAGE_MODIFIER);
    }

    @Test
    void createWeaponWithExistingId() throws Exception {
        // Create the Weapon with an existing ID
        weapon.setId(1L);
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        int databaseSizeBeforeCreate = weaponRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().collectList().block().size();
        // set the field null
        weapon.setName(null);

        // Create the Weapon, which fails.
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().collectList().block().size();
        // set the field null
        weapon.setWeight(null);

        // Create the Weapon, which fails.
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkQualityIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().collectList().block().size();
        // set the field null
        weapon.setQuality(null);

        // Create the Weapon, which fails.
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkReachIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().collectList().block().size();
        // set the field null
        weapon.setReach(null);

        // Create the Weapon, which fails.
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkBaseDamageIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().collectList().block().size();
        // set the field null
        weapon.setBaseDamage(null);

        // Create the Weapon, which fails.
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkRequiredSTIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().collectList().block().size();
        // set the field null
        weapon.setRequiredST(null);

        // Create the Weapon, which fails.
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllWeapons() {
        // Initialize the database
        weaponRepository.save(weapon).block();

        // Get all the weaponList
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
            .value(hasItem(weapon.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].weight")
            .value(hasItem(DEFAULT_WEIGHT))
            .jsonPath("$.[*].quality")
            .value(hasItem(DEFAULT_QUALITY))
            .jsonPath("$.[*].pictureContentType")
            .value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE))
            .jsonPath("$.[*].picture")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .jsonPath("$.[*].characterId")
            .value(hasItem(DEFAULT_CHARACTER_ID.intValue()))
            .jsonPath("$.[*].campaignId")
            .value(hasItem(DEFAULT_CAMPAIGN_ID.intValue()))
            .jsonPath("$.[*].reach")
            .value(hasItem(DEFAULT_REACH))
            .jsonPath("$.[*].baseDamage")
            .value(hasItem(DEFAULT_BASE_DAMAGE))
            .jsonPath("$.[*].requiredST")
            .value(hasItem(DEFAULT_REQUIRED_ST))
            .jsonPath("$.[*].damageModifier")
            .value(hasItem(DEFAULT_DAMAGE_MODIFIER));
    }

    @Test
    void getWeapon() {
        // Initialize the database
        weaponRepository.save(weapon).block();

        // Get the weapon
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, weapon.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(weapon.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.weight")
            .value(is(DEFAULT_WEIGHT))
            .jsonPath("$.quality")
            .value(is(DEFAULT_QUALITY))
            .jsonPath("$.pictureContentType")
            .value(is(DEFAULT_PICTURE_CONTENT_TYPE))
            .jsonPath("$.picture")
            .value(is(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .jsonPath("$.characterId")
            .value(is(DEFAULT_CHARACTER_ID.intValue()))
            .jsonPath("$.campaignId")
            .value(is(DEFAULT_CAMPAIGN_ID.intValue()))
            .jsonPath("$.reach")
            .value(is(DEFAULT_REACH))
            .jsonPath("$.baseDamage")
            .value(is(DEFAULT_BASE_DAMAGE))
            .jsonPath("$.requiredST")
            .value(is(DEFAULT_REQUIRED_ST))
            .jsonPath("$.damageModifier")
            .value(is(DEFAULT_DAMAGE_MODIFIER));
    }

    @Test
    void getNonExistingWeapon() {
        // Get the weapon
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewWeapon() throws Exception {
        // Initialize the database
        weaponRepository.save(weapon).block();

        int databaseSizeBeforeUpdate = weaponRepository.findAll().collectList().block().size();

        // Update the weapon
        Weapon updatedWeapon = weaponRepository.findById(weapon.getId()).block();
        updatedWeapon
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .quality(UPDATED_QUALITY)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .characterId(UPDATED_CHARACTER_ID)
            .campaignId(UPDATED_CAMPAIGN_ID)
            .reach(UPDATED_REACH)
            .baseDamage(UPDATED_BASE_DAMAGE)
            .requiredST(UPDATED_REQUIRED_ST)
            .damageModifier(UPDATED_DAMAGE_MODIFIER);
        WeaponDTO weaponDTO = weaponMapper.toDto(updatedWeapon);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, weaponDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
        Weapon testWeapon = weaponList.get(weaponList.size() - 1);
        assertThat(testWeapon.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWeapon.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWeapon.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testWeapon.getQuality()).isEqualTo(UPDATED_QUALITY);
        assertThat(testWeapon.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testWeapon.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testWeapon.getCharacterId()).isEqualTo(UPDATED_CHARACTER_ID);
        assertThat(testWeapon.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
        assertThat(testWeapon.getReach()).isEqualTo(UPDATED_REACH);
        assertThat(testWeapon.getBaseDamage()).isEqualTo(UPDATED_BASE_DAMAGE);
        assertThat(testWeapon.getRequiredST()).isEqualTo(UPDATED_REQUIRED_ST);
        assertThat(testWeapon.getDamageModifier()).isEqualTo(UPDATED_DAMAGE_MODIFIER);
    }

    @Test
    void putNonExistingWeapon() throws Exception {
        int databaseSizeBeforeUpdate = weaponRepository.findAll().collectList().block().size();
        weapon.setId(count.incrementAndGet());

        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, weaponDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchWeapon() throws Exception {
        int databaseSizeBeforeUpdate = weaponRepository.findAll().collectList().block().size();
        weapon.setId(count.incrementAndGet());

        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamWeapon() throws Exception {
        int databaseSizeBeforeUpdate = weaponRepository.findAll().collectList().block().size();
        weapon.setId(count.incrementAndGet());

        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateWeaponWithPatch() throws Exception {
        // Initialize the database
        weaponRepository.save(weapon).block();

        int databaseSizeBeforeUpdate = weaponRepository.findAll().collectList().block().size();

        // Update the weapon using partial update
        Weapon partialUpdatedWeapon = new Weapon();
        partialUpdatedWeapon.setId(weapon.getId());

        partialUpdatedWeapon
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .campaignId(UPDATED_CAMPAIGN_ID)
            .reach(UPDATED_REACH)
            .baseDamage(UPDATED_BASE_DAMAGE)
            .requiredST(UPDATED_REQUIRED_ST);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWeapon.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWeapon))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
        Weapon testWeapon = weaponList.get(weaponList.size() - 1);
        assertThat(testWeapon.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWeapon.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWeapon.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testWeapon.getQuality()).isEqualTo(DEFAULT_QUALITY);
        assertThat(testWeapon.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testWeapon.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testWeapon.getCharacterId()).isEqualTo(DEFAULT_CHARACTER_ID);
        assertThat(testWeapon.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
        assertThat(testWeapon.getReach()).isEqualTo(UPDATED_REACH);
        assertThat(testWeapon.getBaseDamage()).isEqualTo(UPDATED_BASE_DAMAGE);
        assertThat(testWeapon.getRequiredST()).isEqualTo(UPDATED_REQUIRED_ST);
        assertThat(testWeapon.getDamageModifier()).isEqualTo(DEFAULT_DAMAGE_MODIFIER);
    }

    @Test
    void fullUpdateWeaponWithPatch() throws Exception {
        // Initialize the database
        weaponRepository.save(weapon).block();

        int databaseSizeBeforeUpdate = weaponRepository.findAll().collectList().block().size();

        // Update the weapon using partial update
        Weapon partialUpdatedWeapon = new Weapon();
        partialUpdatedWeapon.setId(weapon.getId());

        partialUpdatedWeapon
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .quality(UPDATED_QUALITY)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .characterId(UPDATED_CHARACTER_ID)
            .campaignId(UPDATED_CAMPAIGN_ID)
            .reach(UPDATED_REACH)
            .baseDamage(UPDATED_BASE_DAMAGE)
            .requiredST(UPDATED_REQUIRED_ST)
            .damageModifier(UPDATED_DAMAGE_MODIFIER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWeapon.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWeapon))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
        Weapon testWeapon = weaponList.get(weaponList.size() - 1);
        assertThat(testWeapon.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWeapon.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWeapon.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testWeapon.getQuality()).isEqualTo(UPDATED_QUALITY);
        assertThat(testWeapon.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testWeapon.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testWeapon.getCharacterId()).isEqualTo(UPDATED_CHARACTER_ID);
        assertThat(testWeapon.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
        assertThat(testWeapon.getReach()).isEqualTo(UPDATED_REACH);
        assertThat(testWeapon.getBaseDamage()).isEqualTo(UPDATED_BASE_DAMAGE);
        assertThat(testWeapon.getRequiredST()).isEqualTo(UPDATED_REQUIRED_ST);
        assertThat(testWeapon.getDamageModifier()).isEqualTo(UPDATED_DAMAGE_MODIFIER);
    }

    @Test
    void patchNonExistingWeapon() throws Exception {
        int databaseSizeBeforeUpdate = weaponRepository.findAll().collectList().block().size();
        weapon.setId(count.incrementAndGet());

        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, weaponDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchWeapon() throws Exception {
        int databaseSizeBeforeUpdate = weaponRepository.findAll().collectList().block().size();
        weapon.setId(count.incrementAndGet());

        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamWeapon() throws Exception {
        int databaseSizeBeforeUpdate = weaponRepository.findAll().collectList().block().size();
        weapon.setId(count.incrementAndGet());

        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(weaponDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteWeapon() {
        // Initialize the database
        weaponRepository.save(weapon).block();

        int databaseSizeBeforeDelete = weaponRepository.findAll().collectList().block().size();

        // Delete the weapon
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, weapon.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Weapon> weaponList = weaponRepository.findAll().collectList().block();
        assertThat(weaponList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
