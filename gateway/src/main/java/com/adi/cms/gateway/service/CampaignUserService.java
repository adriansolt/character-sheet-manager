package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.CampaignUser;
import com.adi.cms.gateway.repository.CampaignUserRepository;
import com.adi.cms.gateway.service.dto.CampaignUserDTO;
import com.adi.cms.gateway.service.mapper.CampaignUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<CampaignUserDTO> save(CampaignUserDTO campaignUserDTO) {
        log.debug("Request to save CampaignUser : {}", campaignUserDTO);
        return campaignUserRepository.save(campaignUserMapper.toEntity(campaignUserDTO)).map(campaignUserMapper::toDto);
    }

    /**
     * Partially update a campaignUser.
     *
     * @param campaignUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CampaignUserDTO> partialUpdate(CampaignUserDTO campaignUserDTO) {
        log.debug("Request to partially update CampaignUser : {}", campaignUserDTO);

        return campaignUserRepository
            .findById(campaignUserDTO.getId())
            .map(existingCampaignUser -> {
                campaignUserMapper.partialUpdate(existingCampaignUser, campaignUserDTO);

                return existingCampaignUser;
            })
            .flatMap(campaignUserRepository::save)
            .map(campaignUserMapper::toDto);
    }

    /**
     * Get all the campaignUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CampaignUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CampaignUsers");
        return campaignUserRepository.findAllBy(pageable).map(campaignUserMapper::toDto);
    }

    /**
     * Returns the number of campaignUsers available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return campaignUserRepository.count();
    }

    /**
     * Get one campaignUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CampaignUserDTO> findOne(Long id) {
        log.debug("Request to get CampaignUser : {}", id);
        return campaignUserRepository.findById(id).map(campaignUserMapper::toDto);
    }

    /**
     * Delete the campaignUser by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete CampaignUser : {}", id);
        return campaignUserRepository.deleteById(id);
    }
}
