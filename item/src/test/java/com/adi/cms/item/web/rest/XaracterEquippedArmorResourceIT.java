package com.adi.cms.item.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.item.IntegrationTest;
import com.adi.cms.item.domain.XaracterEquippedArmor;
import com.adi.cms.item.repository.XaracterEquippedArmorRepository;
import com.adi.cms.item.service.dto.XaracterEquippedArmorDTO;
import com.adi.cms.item.service.mapper.XaracterEquippedArmorMapper;
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
 * Integration tests for the {@link XaracterEquippedArmorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restXaracterEquippedArmorMockMvc;

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

    @BeforeEach
    public void initTest() {
        xaracterEquippedArmor = createEntity(em);
    }

    @Test
    @Transactional
    void createXaracterEquippedArmor() throws Exception {
        int databaseSizeBeforeCreate = xaracterEquippedArmorRepository.findAll().size();
        // Create the XaracterEquippedArmor
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);
        restXaracterEquippedArmorMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            )
            .andExpect(status().isCreated());

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeCreate + 1);
        XaracterEquippedArmor testXaracterEquippedArmor = xaracterEquippedArmorList.get(xaracterEquippedArmorList.size() - 1);
        assertThat(testXaracterEquippedArmor.getXaracterId()).isEqualTo(DEFAULT_XARACTER_ID);
    }

    @Test
    @Transactional
    void createXaracterEquippedArmorWithExistingId() throws Exception {
        // Create the XaracterEquippedArmor with an existing ID
        xaracterEquippedArmor.setId(1L);
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);

        int databaseSizeBeforeCreate = xaracterEquippedArmorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restXaracterEquippedArmorMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllXaracterEquippedArmors() throws Exception {
        // Initialize the database
        xaracterEquippedArmorRepository.saveAndFlush(xaracterEquippedArmor);

        // Get all the xaracterEquippedArmorList
        restXaracterEquippedArmorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(xaracterEquippedArmor.getId().intValue())))
            .andExpect(jsonPath("$.[*].xaracterId").value(hasItem(DEFAULT_XARACTER_ID.intValue())));
    }

    @Test
    @Transactional
    void getXaracterEquippedArmor() throws Exception {
        // Initialize the database
        xaracterEquippedArmorRepository.saveAndFlush(xaracterEquippedArmor);

        // Get the xaracterEquippedArmor
        restXaracterEquippedArmorMockMvc
            .perform(get(ENTITY_API_URL_ID, xaracterEquippedArmor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(xaracterEquippedArmor.getId().intValue()))
            .andExpect(jsonPath("$.xaracterId").value(DEFAULT_XARACTER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingXaracterEquippedArmor() throws Exception {
        // Get the xaracterEquippedArmor
        restXaracterEquippedArmorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewXaracterEquippedArmor() throws Exception {
        // Initialize the database
        xaracterEquippedArmorRepository.saveAndFlush(xaracterEquippedArmor);

        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().size();

        // Update the xaracterEquippedArmor
        XaracterEquippedArmor updatedXaracterEquippedArmor = xaracterEquippedArmorRepository.findById(xaracterEquippedArmor.getId()).get();
        // Disconnect from session so that the updates on updatedXaracterEquippedArmor are not directly saved in db
        em.detach(updatedXaracterEquippedArmor);
        updatedXaracterEquippedArmor.xaracterId(UPDATED_XARACTER_ID);
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(updatedXaracterEquippedArmor);

        restXaracterEquippedArmorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, xaracterEquippedArmorDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            )
            .andExpect(status().isOk());

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
        XaracterEquippedArmor testXaracterEquippedArmor = xaracterEquippedArmorList.get(xaracterEquippedArmorList.size() - 1);
        assertThat(testXaracterEquippedArmor.getXaracterId()).isEqualTo(UPDATED_XARACTER_ID);
    }

    @Test
    @Transactional
    void putNonExistingXaracterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().size();
        xaracterEquippedArmor.setId(count.incrementAndGet());

        // Create the XaracterEquippedArmor
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXaracterEquippedArmorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, xaracterEquippedArmorDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchXaracterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().size();
        xaracterEquippedArmor.setId(count.incrementAndGet());

        // Create the XaracterEquippedArmor
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterEquippedArmorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamXaracterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().size();
        xaracterEquippedArmor.setId(count.incrementAndGet());

        // Create the XaracterEquippedArmor
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterEquippedArmorMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateXaracterEquippedArmorWithPatch() throws Exception {
        // Initialize the database
        xaracterEquippedArmorRepository.saveAndFlush(xaracterEquippedArmor);

        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().size();

        // Update the xaracterEquippedArmor using partial update
        XaracterEquippedArmor partialUpdatedXaracterEquippedArmor = new XaracterEquippedArmor();
        partialUpdatedXaracterEquippedArmor.setId(xaracterEquippedArmor.getId());

        restXaracterEquippedArmorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXaracterEquippedArmor.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterEquippedArmor))
            )
            .andExpect(status().isOk());

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
        XaracterEquippedArmor testXaracterEquippedArmor = xaracterEquippedArmorList.get(xaracterEquippedArmorList.size() - 1);
        assertThat(testXaracterEquippedArmor.getXaracterId()).isEqualTo(DEFAULT_XARACTER_ID);
    }

    @Test
    @Transactional
    void fullUpdateXaracterEquippedArmorWithPatch() throws Exception {
        // Initialize the database
        xaracterEquippedArmorRepository.saveAndFlush(xaracterEquippedArmor);

        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().size();

        // Update the xaracterEquippedArmor using partial update
        XaracterEquippedArmor partialUpdatedXaracterEquippedArmor = new XaracterEquippedArmor();
        partialUpdatedXaracterEquippedArmor.setId(xaracterEquippedArmor.getId());

        partialUpdatedXaracterEquippedArmor.xaracterId(UPDATED_XARACTER_ID);

        restXaracterEquippedArmorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXaracterEquippedArmor.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXaracterEquippedArmor))
            )
            .andExpect(status().isOk());

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
        XaracterEquippedArmor testXaracterEquippedArmor = xaracterEquippedArmorList.get(xaracterEquippedArmorList.size() - 1);
        assertThat(testXaracterEquippedArmor.getXaracterId()).isEqualTo(UPDATED_XARACTER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingXaracterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().size();
        xaracterEquippedArmor.setId(count.incrementAndGet());

        // Create the XaracterEquippedArmor
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXaracterEquippedArmorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, xaracterEquippedArmorDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchXaracterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().size();
        xaracterEquippedArmor.setId(count.incrementAndGet());

        // Create the XaracterEquippedArmor
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterEquippedArmorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamXaracterEquippedArmor() throws Exception {
        int databaseSizeBeforeUpdate = xaracterEquippedArmorRepository.findAll().size();
        xaracterEquippedArmor.setId(count.incrementAndGet());

        // Create the XaracterEquippedArmor
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXaracterEquippedArmorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xaracterEquippedArmorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the XaracterEquippedArmor in the database
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteXaracterEquippedArmor() throws Exception {
        // Initialize the database
        xaracterEquippedArmorRepository.saveAndFlush(xaracterEquippedArmor);

        int databaseSizeBeforeDelete = xaracterEquippedArmorRepository.findAll().size();

        // Delete the xaracterEquippedArmor
        restXaracterEquippedArmorMockMvc
            .perform(delete(ENTITY_API_URL_ID, xaracterEquippedArmor.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<XaracterEquippedArmor> xaracterEquippedArmorList = xaracterEquippedArmorRepository.findAll();
        assertThat(xaracterEquippedArmorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
