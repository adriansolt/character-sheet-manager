package com.adi.cms.campaign.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.campaign.IntegrationTest;
import com.adi.cms.campaign.domain.CampaignUser;
import com.adi.cms.campaign.domain.User;
import com.adi.cms.campaign.repository.CampaignUserRepository;
import com.adi.cms.campaign.service.dto.CampaignUserDTO;
import com.adi.cms.campaign.service.mapper.CampaignUserMapper;
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
 * Integration tests for the {@link CampaignUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restCampaignUserMockMvc;

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
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
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
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        campaignUser.setUser(user);
        return campaignUser;
    }

    @BeforeEach
    public void initTest() {
        campaignUser = createEntity(em);
    }

    @Test
    @Transactional
    void createCampaignUser() throws Exception {
        int databaseSizeBeforeCreate = campaignUserRepository.findAll().size();
        // Create the CampaignUser
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);
        restCampaignUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeCreate + 1);
        CampaignUser testCampaignUser = campaignUserList.get(campaignUserList.size() - 1);
    }

    @Test
    @Transactional
    void createCampaignUserWithExistingId() throws Exception {
        // Create the CampaignUser with an existing ID
        campaignUser.setId(1L);
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);

        int databaseSizeBeforeCreate = campaignUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCampaignUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCampaignUsers() throws Exception {
        // Initialize the database
        campaignUserRepository.saveAndFlush(campaignUser);

        // Get all the campaignUserList
        restCampaignUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campaignUser.getId().intValue())));
    }

    @Test
    @Transactional
    void getCampaignUser() throws Exception {
        // Initialize the database
        campaignUserRepository.saveAndFlush(campaignUser);

        // Get the campaignUser
        restCampaignUserMockMvc
            .perform(get(ENTITY_API_URL_ID, campaignUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(campaignUser.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingCampaignUser() throws Exception {
        // Get the campaignUser
        restCampaignUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCampaignUser() throws Exception {
        // Initialize the database
        campaignUserRepository.saveAndFlush(campaignUser);

        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().size();

        // Update the campaignUser
        CampaignUser updatedCampaignUser = campaignUserRepository.findById(campaignUser.getId()).get();
        // Disconnect from session so that the updates on updatedCampaignUser are not directly saved in db
        em.detach(updatedCampaignUser);
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(updatedCampaignUser);

        restCampaignUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, campaignUserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
        CampaignUser testCampaignUser = campaignUserList.get(campaignUserList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingCampaignUser() throws Exception {
        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().size();
        campaignUser.setId(count.incrementAndGet());

        // Create the CampaignUser
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCampaignUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, campaignUserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCampaignUser() throws Exception {
        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().size();
        campaignUser.setId(count.incrementAndGet());

        // Create the CampaignUser
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampaignUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCampaignUser() throws Exception {
        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().size();
        campaignUser.setId(count.incrementAndGet());

        // Create the CampaignUser
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampaignUserMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCampaignUserWithPatch() throws Exception {
        // Initialize the database
        campaignUserRepository.saveAndFlush(campaignUser);

        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().size();

        // Update the campaignUser using partial update
        CampaignUser partialUpdatedCampaignUser = new CampaignUser();
        partialUpdatedCampaignUser.setId(campaignUser.getId());

        restCampaignUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCampaignUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCampaignUser))
            )
            .andExpect(status().isOk());

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
        CampaignUser testCampaignUser = campaignUserList.get(campaignUserList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateCampaignUserWithPatch() throws Exception {
        // Initialize the database
        campaignUserRepository.saveAndFlush(campaignUser);

        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().size();

        // Update the campaignUser using partial update
        CampaignUser partialUpdatedCampaignUser = new CampaignUser();
        partialUpdatedCampaignUser.setId(campaignUser.getId());

        restCampaignUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCampaignUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCampaignUser))
            )
            .andExpect(status().isOk());

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
        CampaignUser testCampaignUser = campaignUserList.get(campaignUserList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingCampaignUser() throws Exception {
        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().size();
        campaignUser.setId(count.incrementAndGet());

        // Create the CampaignUser
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCampaignUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, campaignUserDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCampaignUser() throws Exception {
        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().size();
        campaignUser.setId(count.incrementAndGet());

        // Create the CampaignUser
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampaignUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCampaignUser() throws Exception {
        int databaseSizeBeforeUpdate = campaignUserRepository.findAll().size();
        campaignUser.setId(count.incrementAndGet());

        // Create the CampaignUser
        CampaignUserDTO campaignUserDTO = campaignUserMapper.toDto(campaignUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampaignUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(campaignUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CampaignUser in the database
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCampaignUser() throws Exception {
        // Initialize the database
        campaignUserRepository.saveAndFlush(campaignUser);

        int databaseSizeBeforeDelete = campaignUserRepository.findAll().size();

        // Delete the campaignUser
        restCampaignUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, campaignUser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CampaignUser> campaignUserList = campaignUserRepository.findAll();
        assertThat(campaignUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
