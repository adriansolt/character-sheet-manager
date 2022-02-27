package com.adi.cms.campaign.service;

import com.adi.cms.campaign.domain.CampaignUser;
import com.adi.cms.campaign.repository.CampaignUserRepository;
import com.adi.cms.campaign.service.dto.CampaignUserDTO;
import com.adi.cms.campaign.service.mapper.CampaignUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CampaignUser}.
 */
@Service
@Transactional
public class CampaignUserService {

    private final Logger log = LoggerFactory.getLogger(CampaignUserService.class);

    private final CampaignUserRepository campaignUserRepository;

    private final CampaignUserMapper campaignUserMapper;

    public CampaignUserService(CampaignUserRepository campaignUserRepository, CampaignUserMapper campaignUserMapper) {
        this.campaignUserRepository = campaignUserRepository;
        this.campaignUserMapper = campaignUserMapper;
    }

    /**
     * Save a campaignUser.
     *
     * @param campaignUserDTO the entity to save.
     * @return the persisted entity.
     */
    public CampaignUserDTO save(CampaignUserDTO campaignUserDTO) {
        log.debug("Request to save CampaignUser : {}", campaignUserDTO);
        CampaignUser campaignUser = campaignUserMapper.toEntity(campaignUserDTO);
        campaignUser = campaignUserRepository.save(campaignUser);
        return campaignUserMapper.toDto(campaignUser);
    }

    /**
     * Partially update a campaignUser.
     *
     * @param campaignUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CampaignUserDTO> partialUpdate(CampaignUserDTO campaignUserDTO) {
        log.debug("Request to partially update CampaignUser : {}", campaignUserDTO);

        return campaignUserRepository
            .findById(campaignUserDTO.getId())
            .map(existingCampaignUser -> {
                campaignUserMapper.partialUpdate(existingCampaignUser, campaignUserDTO);

                return existingCampaignUser;
            })
            .map(campaignUserRepository::save)
            .map(campaignUserMapper::toDto);
    }

    /**
     * Get all the campaignUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CampaignUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CampaignUsers");
        return campaignUserRepository.findAll(pageable).map(campaignUserMapper::toDto);
    }

    /**
     * Get all the campaignUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CampaignUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return campaignUserRepository.findAllWithEagerRelationships(pageable).map(campaignUserMapper::toDto);
    }

    /**
     * Get one campaignUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CampaignUserDTO> findOne(Long id) {
        log.debug("Request to get CampaignUser : {}", id);
        return campaignUserRepository.findOneWithEagerRelationships(id).map(campaignUserMapper::toDto);
    }

    /**
     * Delete the campaignUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CampaignUser : {}", id);
        campaignUserRepository.deleteById(id);
    }
}
