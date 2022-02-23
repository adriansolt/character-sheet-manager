package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.XaracterEquippedArmor;
import com.adi.cms.gateway.repository.XaracterEquippedArmorRepository;
import com.adi.cms.gateway.service.dto.XaracterEquippedArmorDTO;
import com.adi.cms.gateway.service.mapper.XaracterEquippedArmorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link XaracterEquippedArmor}.
 */
@Service
@Transactional
public class XaracterEquippedArmorService {

    private final Logger log = LoggerFactory.getLogger(XaracterEquippedArmorService.class);

    private final XaracterEquippedArmorRepository xaracterEquippedArmorRepository;

    private final XaracterEquippedArmorMapper xaracterEquippedArmorMapper;

    public XaracterEquippedArmorService(
        XaracterEquippedArmorRepository xaracterEquippedArmorRepository,
        XaracterEquippedArmorMapper xaracterEquippedArmorMapper
    ) {
        this.xaracterEquippedArmorRepository = xaracterEquippedArmorRepository;
        this.xaracterEquippedArmorMapper = xaracterEquippedArmorMapper;
    }

    /**
     * Save a xaracterEquippedArmor.
     *
     * @param xaracterEquippedArmorDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<XaracterEquippedArmorDTO> save(XaracterEquippedArmorDTO xaracterEquippedArmorDTO) {
        log.debug("Request to save XaracterEquippedArmor : {}", xaracterEquippedArmorDTO);
        return xaracterEquippedArmorRepository
            .save(xaracterEquippedArmorMapper.toEntity(xaracterEquippedArmorDTO))
            .map(xaracterEquippedArmorMapper::toDto);
    }

    /**
     * Partially update a xaracterEquippedArmor.
     *
     * @param xaracterEquippedArmorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<XaracterEquippedArmorDTO> partialUpdate(XaracterEquippedArmorDTO xaracterEquippedArmorDTO) {
        log.debug("Request to partially update XaracterEquippedArmor : {}", xaracterEquippedArmorDTO);

        return xaracterEquippedArmorRepository
            .findById(xaracterEquippedArmorDTO.getId())
            .map(existingXaracterEquippedArmor -> {
                xaracterEquippedArmorMapper.partialUpdate(existingXaracterEquippedArmor, xaracterEquippedArmorDTO);

                return existingXaracterEquippedArmor;
            })
            .flatMap(xaracterEquippedArmorRepository::save)
            .map(xaracterEquippedArmorMapper::toDto);
    }

    /**
     * Get all the xaracterEquippedArmors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<XaracterEquippedArmorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all XaracterEquippedArmors");
        return xaracterEquippedArmorRepository.findAllBy(pageable).map(xaracterEquippedArmorMapper::toDto);
    }

    /**
     * Returns the number of xaracterEquippedArmors available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return xaracterEquippedArmorRepository.count();
    }

    /**
     * Get one xaracterEquippedArmor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<XaracterEquippedArmorDTO> findOne(Long id) {
        log.debug("Request to get XaracterEquippedArmor : {}", id);
        return xaracterEquippedArmorRepository.findById(id).map(xaracterEquippedArmorMapper::toDto);
    }

    /**
     * Delete the xaracterEquippedArmor by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete XaracterEquippedArmor : {}", id);
        return xaracterEquippedArmorRepository.deleteById(id);
    }
}
