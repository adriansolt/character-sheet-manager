package com.adi.cms.item.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.item.IntegrationTest;
import com.adi.cms.item.domain.Weapon;
import com.adi.cms.item.repository.WeaponRepository;
import com.adi.cms.item.service.dto.WeaponDTO;
import com.adi.cms.item.service.mapper.WeaponMapper;
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
 * Integration tests for the {@link WeaponResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restWeaponMockMvc;

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
            .reach(UPDATED_REACH)
            .baseDamage(UPDATED_BASE_DAMAGE)
            .requiredST(UPDATED_REQUIRED_ST)
            .damageModifier(UPDATED_DAMAGE_MODIFIER);
        return weapon;
    }

    @BeforeEach
    public void initTest() {
        weapon = createEntity(em);
    }

    @Test
    @Transactional
    void createWeapon() throws Exception {
        int databaseSizeBeforeCreate = weaponRepository.findAll().size();
        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);
        restWeaponMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeCreate + 1);
        Weapon testWeapon = weaponList.get(weaponList.size() - 1);
        assertThat(testWeapon.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWeapon.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWeapon.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testWeapon.getQuality()).isEqualTo(DEFAULT_QUALITY);
        assertThat(testWeapon.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testWeapon.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testWeapon.getReach()).isEqualTo(DEFAULT_REACH);
        assertThat(testWeapon.getBaseDamage()).isEqualTo(DEFAULT_BASE_DAMAGE);
        assertThat(testWeapon.getRequiredST()).isEqualTo(DEFAULT_REQUIRED_ST);
        assertThat(testWeapon.getDamageModifier()).isEqualTo(DEFAULT_DAMAGE_MODIFIER);
    }

    @Test
    @Transactional
    void createWeaponWithExistingId() throws Exception {
        // Create the Weapon with an existing ID
        weapon.setId(1L);
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        int databaseSizeBeforeCreate = weaponRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeaponMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().size();
        // set the field null
        weapon.setName(null);

        // Create the Weapon, which fails.
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        restWeaponMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponDTO))
            )
            .andExpect(status().isBadRequest());

        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().size();
        // set the field null
        weapon.setWeight(null);

        // Create the Weapon, which fails.
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        restWeaponMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponDTO))
            )
            .andExpect(status().isBadRequest());

        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQualityIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().size();
        // set the field null
        weapon.setQuality(null);

        // Create the Weapon, which fails.
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        restWeaponMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponDTO))
            )
            .andExpect(status().isBadRequest());

        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReachIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().size();
        // set the field null
        weapon.setReach(null);

        // Create the Weapon, which fails.
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        restWeaponMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponDTO))
            )
            .andExpect(status().isBadRequest());

        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBaseDamageIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().size();
        // set the field null
        weapon.setBaseDamage(null);

        // Create the Weapon, which fails.
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        restWeaponMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponDTO))
            )
            .andExpect(status().isBadRequest());

        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequiredSTIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().size();
        // set the field null
        weapon.setRequiredST(null);

        // Create the Weapon, which fails.
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        restWeaponMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponDTO))
            )
            .andExpect(status().isBadRequest());

        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWeapons() throws Exception {
        // Initialize the database
        weaponRepository.saveAndFlush(weapon);

        // Get all the weaponList
        restWeaponMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weapon.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].quality").value(hasItem(DEFAULT_QUALITY)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].reach").value(hasItem(DEFAULT_REACH)))
            .andExpect(jsonPath("$.[*].baseDamage").value(hasItem(DEFAULT_BASE_DAMAGE)))
            .andExpect(jsonPath("$.[*].requiredST").value(hasItem(DEFAULT_REQUIRED_ST)))
            .andExpect(jsonPath("$.[*].damageModifier").value(hasItem(DEFAULT_DAMAGE_MODIFIER)));
    }

    @Test
    @Transactional
    void getWeapon() throws Exception {
        // Initialize the database
        weaponRepository.saveAndFlush(weapon);

        // Get the weapon
        restWeaponMockMvc
            .perform(get(ENTITY_API_URL_ID, weapon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(weapon.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.quality").value(DEFAULT_QUALITY))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.reach").value(DEFAULT_REACH))
            .andExpect(jsonPath("$.baseDamage").value(DEFAULT_BASE_DAMAGE))
            .andExpect(jsonPath("$.requiredST").value(DEFAULT_REQUIRED_ST))
            .andExpect(jsonPath("$.damageModifier").value(DEFAULT_DAMAGE_MODIFIER));
    }

    @Test
    @Transactional
    void getNonExistingWeapon() throws Exception {
        // Get the weapon
        restWeaponMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWeapon() throws Exception {
        // Initialize the database
        weaponRepository.saveAndFlush(weapon);

        int databaseSizeBeforeUpdate = weaponRepository.findAll().size();

        // Update the weapon
        Weapon updatedWeapon = weaponRepository.findById(weapon.getId()).get();
        // Disconnect from session so that the updates on updatedWeapon are not directly saved in db
        em.detach(updatedWeapon);
        updatedWeapon
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .quality(UPDATED_QUALITY)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .reach(UPDATED_REACH)
            .baseDamage(UPDATED_BASE_DAMAGE)
            .requiredST(UPDATED_REQUIRED_ST)
            .damageModifier(UPDATED_DAMAGE_MODIFIER);
        WeaponDTO weaponDTO = weaponMapper.toDto(updatedWeapon);

        restWeaponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weaponDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponDTO))
            )
            .andExpect(status().isOk());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
        Weapon testWeapon = weaponList.get(weaponList.size() - 1);
        assertThat(testWeapon.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWeapon.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWeapon.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testWeapon.getQuality()).isEqualTo(UPDATED_QUALITY);
        assertThat(testWeapon.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testWeapon.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testWeapon.getReach()).isEqualTo(UPDATED_REACH);
        assertThat(testWeapon.getBaseDamage()).isEqualTo(UPDATED_BASE_DAMAGE);
        assertThat(testWeapon.getRequiredST()).isEqualTo(UPDATED_REQUIRED_ST);
        assertThat(testWeapon.getDamageModifier()).isEqualTo(UPDATED_DAMAGE_MODIFIER);
    }

    @Test
    @Transactional
    void putNonExistingWeapon() throws Exception {
        int databaseSizeBeforeUpdate = weaponRepository.findAll().size();
        weapon.setId(count.incrementAndGet());

        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeaponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weaponDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWeapon() throws Exception {
        int databaseSizeBeforeUpdate = weaponRepository.findAll().size();
        weapon.setId(count.incrementAndGet());

        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeaponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWeapon() throws Exception {
        int databaseSizeBeforeUpdate = weaponRepository.findAll().size();
        weapon.setId(count.incrementAndGet());

        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeaponMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weaponDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWeaponWithPatch() throws Exception {
        // Initialize the database
        weaponRepository.saveAndFlush(weapon);

        int databaseSizeBeforeUpdate = weaponRepository.findAll().size();

        // Update the weapon using partial update
        Weapon partialUpdatedWeapon = new Weapon();
        partialUpdatedWeapon.setId(weapon.getId());

        partialUpdatedWeapon
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .baseDamage(UPDATED_BASE_DAMAGE)
            .requiredST(UPDATED_REQUIRED_ST)
            .damageModifier(UPDATED_DAMAGE_MODIFIER);

        restWeaponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeapon.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeapon))
            )
            .andExpect(status().isOk());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
        Weapon testWeapon = weaponList.get(weaponList.size() - 1);
        assertThat(testWeapon.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWeapon.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWeapon.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testWeapon.getQuality()).isEqualTo(DEFAULT_QUALITY);
        assertThat(testWeapon.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testWeapon.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testWeapon.getReach()).isEqualTo(DEFAULT_REACH);
        assertThat(testWeapon.getBaseDamage()).isEqualTo(UPDATED_BASE_DAMAGE);
        assertThat(testWeapon.getRequiredST()).isEqualTo(UPDATED_REQUIRED_ST);
        assertThat(testWeapon.getDamageModifier()).isEqualTo(UPDATED_DAMAGE_MODIFIER);
    }

    @Test
    @Transactional
    void fullUpdateWeaponWithPatch() throws Exception {
        // Initialize the database
        weaponRepository.saveAndFlush(weapon);

        int databaseSizeBeforeUpdate = weaponRepository.findAll().size();

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
            .reach(UPDATED_REACH)
            .baseDamage(UPDATED_BASE_DAMAGE)
            .requiredST(UPDATED_REQUIRED_ST)
            .damageModifier(UPDATED_DAMAGE_MODIFIER);

        restWeaponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeapon.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeapon))
            )
            .andExpect(status().isOk());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
        Weapon testWeapon = weaponList.get(weaponList.size() - 1);
        assertThat(testWeapon.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWeapon.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWeapon.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testWeapon.getQuality()).isEqualTo(UPDATED_QUALITY);
        assertThat(testWeapon.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testWeapon.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testWeapon.getReach()).isEqualTo(UPDATED_REACH);
        assertThat(testWeapon.getBaseDamage()).isEqualTo(UPDATED_BASE_DAMAGE);
        assertThat(testWeapon.getRequiredST()).isEqualTo(UPDATED_REQUIRED_ST);
        assertThat(testWeapon.getDamageModifier()).isEqualTo(UPDATED_DAMAGE_MODIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingWeapon() throws Exception {
        int databaseSizeBeforeUpdate = weaponRepository.findAll().size();
        weapon.setId(count.incrementAndGet());

        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeaponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, weaponDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weaponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWeapon() throws Exception {
        int databaseSizeBeforeUpdate = weaponRepository.findAll().size();
        weapon.setId(count.incrementAndGet());

        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeaponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weaponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWeapon() throws Exception {
        int databaseSizeBeforeUpdate = weaponRepository.findAll().size();
        weapon.setId(count.incrementAndGet());

        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeaponMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weaponDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWeapon() throws Exception {
        // Initialize the database
        weaponRepository.saveAndFlush(weapon);

        int databaseSizeBeforeDelete = weaponRepository.findAll().size();

        // Delete the weapon
        restWeaponMockMvc
            .perform(delete(ENTITY_API_URL_ID, weapon.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
