package com.adi.cms.item.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.item.IntegrationTest;
import com.adi.cms.item.domain.XaracterEquippedWeapon;
import com.adi.cms.item.domain.enumeration.Handedness;
import com.adi.cms.item.repository.XaracterEquippedWeaponRepository;
import com.adi.cms.item.service.dto.XaracterEquippedWeaponDTO;
import com.adi.cms.item.service.mapper.XaracterEquippedWeaponMapper;
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

/**
 * Integration tests for the {@link XaracterEquippedWeaponResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restXaracterEquippedWeaponMockMvc;

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

    @BeforeEach
    public void initTest() {
        xaracterEquippedWeapon = createEntity(em);
    }

    @Test
    @Transactional
    void createXaracterEquippedWeapon() throws Exception {
        int databaseSizeBeforeCreate = xaracterEquippedWeaponRepository.findAll().size();
        // Create the XaracterEquippedWeapon
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);
        restXaracterEquippedWeaponMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            )
            .andExpect(status().isCreated());

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeCreate + 1);
        XaracterEquippedWeapon testXaracterEquippedWeapon = xaracterEquippedWeaponList.get(xaracterEquippedWeaponList.size() - 1);
        assertThat(testXaracterEquippedWeapon.getXaracterId()).isEqualTo(DEFAULT_XARACTER_ID);
        assertThat(testXaracterEquippedWeapon.getHand()).isEqualTo(DEFAULT_HAND);
    }

    @Test
    @Transactional
    void createXaracterEquippedWeaponWithExistingId() throws Exception {
        // Create the XaracterEquippedWeapon with an existing ID
        xaracterEquippedWeapon.setId(1L);
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);

        int databaseSizeBeforeCreate = xaracterEquippedWeaponRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restXaracterEquippedWeaponMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllXaracterEquippedWeapons() throws Exception {
        // Initialize the database
        xaracterEquippedWeaponRepository.saveAndFlush(xaracterEquippedWeapon);

        // Get all the xaracterEquippedWeaponList
        restXaracterEquippedWeaponMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(xaracterEquippedWeapon.getId().intValue())))
            .andExpect(jsonPath("$.[*].xaracterId").value(hasItem(DEFAULT_XARACTER_ID.intValue())))
            .andExpect(jsonPath("$.[*].hand").value(hasItem(DEFAULT_HAND.toString())));
    }

    @Test
    @Transactional
    void getXaracterEquippedWeapon() throws Exception {
        // Initialize the database
        xaracterEquippedWeaponRepository.saveAndFlush(xaracterEquippedWeapon);

        // Get the xaracterEquippedWeapon
        restXaracterEquippedWeaponMockMvc
            .perform(get(ENTITY_API_URL_ID, xaracterEquippedWeapon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(xaracterEquippedWeapon.getId().intValue()))
            .andExpect(jsonPath("$.xaracterId").value(DEFAULT_XARACTER_ID.intValue()))
            .andExpect(jsonPath("$.hand").value(DEFAULT_HAND.toString()));
    }

    @Test
    @Transactional
    void getNonExistingXaracterEquippedWeapon() throws Exception {
        // Get the xaracterEquippedWeapon
        restXaracterEquippedWeaponMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewXaracterEquippedWeapon() throws Exception {
        // Initialize the database
        xaracterEquippedWeaponRepository.saveAndFlush(xaracterEquippedWeapon);

        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().size();

        // Update the xaracterEquippedWeapon
        XaracterEquippedWeapon updatedXaracterEquippedWeapon = xaracterEquippedWeaponRepository
            .findById(xaracterEquippedWeapon.getId())
            .get();
        // Disconnect from session so that the updates on updatedXaracterEquippedWeapon are not directly saved in db
        em.detach(updatedXaracterEquippedWeapon);
        updatedXaracterEquippedWeapon.xaracterId(UPDATED_XARACTER_ID).hand(UPDATED_HAND);
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(updatedXaracterEquippedWeapon);

        restXaracterEquippedWeaponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, xaracterEquippedWeaponDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            )
            .andExpect(status().isOk());

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
        XaracterEquippedWeapon testXaracterEquippedWeapon = xaracterEquippedWeaponList.get(xaracterEquippedWeaponList.size() - 1);
        assertThat(testXaracterEquippedWeapon.getXaracterId()).isEqualTo(UPDATED_XARACTER_ID);
        assertThat(testXaracterEquippedWeapon.getHand()).isEqualTo(UPDATED_HAND);
    }

    @Test
    @Transactional
    void putNonExistingXaracterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().size();
        xaracterEquippedWeapon.setId(count.incrementAndGet());

        // Create the XaracterEquippedWeapon
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXaracterEquippedWeaponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, xaracterEquippedWeaponDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchXaracterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().size();
        xaracterEquippedWeapon.setId(count.incrementAndGet());

        // Create the XaracterEquippedWeapon
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterEquippedWeaponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamXaracterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().size();
        xaracterEquippedWeapon.setId(count.incrementAndGet());

        // Create the XaracterEquippedWeapon
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterEquippedWeaponMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateXaracterEquippedWeaponWithPatch() throws Exception {
        // Initialize the database
        xaracterEquippedWeaponRepository.saveAndFlush(xaracterEquippedWeapon);

        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().size();

        // Update the xaracterEquippedWeapon using partial update
        XaracterEquippedWeapon partialUpdatedXaracterEquippedWeapon = new XaracterEquippedWeapon();
        partialUpdatedXaracterEquippedWeapon.setId(xaracterEquippedWeapon.getId());

        restXaracterEquippedWeaponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXaracterEquippedWeapon.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterEquippedWeapon))
            )
            .andExpect(status().isOk());

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
        XaracterEquippedWeapon testXaracterEquippedWeapon = xaracterEquippedWeaponList.get(xaracterEquippedWeaponList.size() - 1);
        assertThat(testXaracterEquippedWeapon.getXaracterId()).isEqualTo(DEFAULT_XARACTER_ID);
        assertThat(testXaracterEquippedWeapon.getHand()).isEqualTo(DEFAULT_HAND);
    }

    @Test
    @Transactional
    void fullUpdateXaracterEquippedWeaponWithPatch() throws Exception {
        // Initialize the database
        xaracterEquippedWeaponRepository.saveAndFlush(xaracterEquippedWeapon);

        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().size();

        // Update the xaracterEquippedWeapon using partial update
        XaracterEquippedWeapon partialUpdatedXaracterEquippedWeapon = new XaracterEquippedWeapon();
        partialUpdatedXaracterEquippedWeapon.setId(xaracterEquippedWeapon.getId());

        partialUpdatedXaracterEquippedWeapon.xaracterId(UPDATED_XARACTER_ID).hand(UPDATED_HAND);

        restXaracterEquippedWeaponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXaracterEquippedWeapon.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterEquippedWeapon))
            )
            .andExpect(status().isOk());

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
        XaracterEquippedWeapon testXaracterEquippedWeapon = xaracterEquippedWeaponList.get(xaracterEquippedWeaponList.size() - 1);
        assertThat(testXaracterEquippedWeapon.getXaracterId()).isEqualTo(UPDATED_XARACTER_ID);
        assertThat(testXaracterEquippedWeapon.getHand()).isEqualTo(UPDATED_HAND);
    }

    @Test
    @Transactional
    void patchNonExistingXaracterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().size();
        xaracterEquippedWeapon.setId(count.incrementAndGet());

        // Create the XaracterEquippedWeapon
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXaracterEquippedWeaponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, xaracterEquippedWeaponDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchXaracterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().size();
        xaracterEquippedWeapon.setId(count.incrementAndGet());

        // Create the XaracterEquippedWeapon
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterEquippedWeaponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamXaracterEquippedWeapon() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedWeaponRepository.findAll().size();
        xaracterEquippedWeapon.setId(count.incrementAndGet());

        // Create the XaracterEquippedWeapon
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterEquippedWeaponMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedWeaponDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the XaracterEquippedWeapon in the database
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteXaracterEquippedWeapon() throws Exception {
        // Initialize the database
        xaracterEquippedWeaponRepository.saveAndFlush(xaracterEquippedWeapon);

        int databaseSizeBeforeDelete = xaracterEquippedWeaponRepository.findAll().size();

        // Delete the xaracterEquippedWeapon
        restXaracterEquippedWeaponMockMvc
            .perform(delete(ENTITY_API_URL_ID, xaracterEquippedWeapon.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<XaracterEquippedWeapon> xaracterEquippedWeaponList = xaracterEquippedWeaponRepository.findAll();
        assertThat(xaracterEquippedWeaponList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
