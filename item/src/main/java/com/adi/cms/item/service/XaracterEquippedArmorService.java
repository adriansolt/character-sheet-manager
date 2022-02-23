package com.adi.cms.item.service;

import com.adi.cms.item.domain.XaracterEquippedArmor;
import com.adi.cms.item.repository.XaracterEquippedArmorRepository;
import com.adi.cms.item.service.dto.XaracterEquippedArmorDTO;
import com.adi.cms.item.service.mapper.XaracterEquippedArmorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public XaracterEquippedArmorDTO save(XaracterEquippedArmorDTO xaracterEquippedArmorDTO) {
        log.debug("Request to save XaracterEquippedArmor : {}", xaracterEquippedArmorDTO);
        XaracterEquippedArmor xaracterEquippedArmor = xaracterEquippedArmorMapper.toEntity(xaracterEquippedArmorDTO);
        xaracterEquippedArmor = xaracterEquippedArmorRepository.save(xaracterEquippedArmor);
        return xaracterEquippedArmorMapper.toDto(xaracterEquippedArmor);
    }

    /**
     * Partially update a xaracterEquippedArmor.
     *
     * @param xaracterEquippedArmorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<XaracterEquippedArmorDTO> partialUpdate(XaracterEquippedArmorDTO xaracterEquippedArmorDTO) {
        log.debug("Request to partially update XaracterEquippedArmor : {}", xaracterEquippedArmorDTO);

        return xaracterEquippedArmorRepository
            .findById(xaracterEquippedArmorDTO.getId())
            .map(existingXaracterEquippedArmor -> {
                xaracterEquippedArmorMapper.partialUpdate(existingXaracterEquippedArmor, xaracterEquippedArmorDTO);

                return existingXaracterEquippedArmor;
            })
            .map(xaracterEquippedArmorRepository::save)
            .map(xaracterEquippedArmorMapper::toDto);
    }

    /**
     * Get all the xaracterEquippedArmors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<XaracterEquippedArmorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all XaracterEquippedArmors");
        return xaracterEquippedArmorRepository.findAll(pageable).map(xaracterEquippedArmorMapper::toDto);
    }

    /**
     * Get one xaracterEquippedArmor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<XaracterEquippedArmorDTO> findOne(Long id) {
        log.debug("Request to get XaracterEquippedArmor : {}", id);
        return xaracterEquippedArmorRepository.findById(id).map(xaracterEquippedArmorMapper::toDto);
    }

    /**
     * Delete the xaracterEquippedArmor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete XaracterEquippedArmor : {}", id);
        xaracterEquippedArmorRepository.deleteById(id);
    }
}
