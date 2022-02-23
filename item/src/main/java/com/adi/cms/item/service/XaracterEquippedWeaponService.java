package com.adi.cms.item.service;

import com.adi.cms.item.domain.XaracterEquippedWeapon;
import com.adi.cms.item.repository.XaracterEquippedWeaponRepository;
import com.adi.cms.item.service.dto.XaracterEquippedWeaponDTO;
import com.adi.cms.item.service.mapper.XaracterEquippedWeaponMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link XaracterEquippedWeapon}.
 */
@Service
@Transactional
public class XaracterEquippedWeaponService {

    private final Logger log = LoggerFactory.getLogger(XaracterEquippedWeaponService.class);

    private final XaracterEquippedWeaponRepository xaracterEquippedWeaponRepository;

    private final XaracterEquippedWeaponMapper xaracterEquippedWeaponMapper;

    public XaracterEquippedWeaponService(
        XaracterEquippedWeaponRepository xaracterEquippedWeaponRepository,
        XaracterEquippedWeaponMapper xaracterEquippedWeaponMapper
    ) {
        this.xaracterEquippedWeaponRepository = xaracterEquippedWeaponRepository;
        this.xaracterEquippedWeaponMapper = xaracterEquippedWeaponMapper;
    }

    /**
     * Save a xaracterEquippedWeapon.
     *
     * @param xaracterEquippedWeaponDTO the entity to save.
     * @return the persisted entity.
     */
    public XaracterEquippedWeaponDTO save(XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO) {
        log.debug("Request to save XaracterEquippedWeapon : {}", xaracterEquippedWeaponDTO);
        XaracterEquippedWeapon xaracterEquippedWeapon = xaracterEquippedWeaponMapper.toEntity(xaracterEquippedWeaponDTO);
        xaracterEquippedWeapon = xaracterEquippedWeaponRepository.save(xaracterEquippedWeapon);
        return xaracterEquippedWeaponMapper.toDto(xaracterEquippedWeapon);
    }

    /**
     * Partially update a xaracterEquippedWeapon.
     *
     * @param xaracterEquippedWeaponDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<XaracterEquippedWeaponDTO> partialUpdate(XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO) {
        log.debug("Request to partially update XaracterEquippedWeapon : {}", xaracterEquippedWeaponDTO);

        return xaracterEquippedWeaponRepository
            .findById(xaracterEquippedWeaponDTO.getId())
            .map(existingXaracterEquippedWeapon -> {
                xaracterEquippedWeaponMapper.partialUpdate(existingXaracterEquippedWeapon, xaracterEquippedWeaponDTO);

                return existingXaracterEquippedWeapon;
            })
            .map(xaracterEquippedWeaponRepository::save)
            .map(xaracterEquippedWeaponMapper::toDto);
    }

    /**
     * Get all the xaracterEquippedWeapons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<XaracterEquippedWeaponDTO> findAll(Pageable pageable) {
        log.debug("Request to get all XaracterEquippedWeapons");
        return xaracterEquippedWeaponRepository.findAll(pageable).map(xaracterEquippedWeaponMapper::toDto);
    }

    /**
     * Get one xaracterEquippedWeapon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<XaracterEquippedWeaponDTO> findOne(Long id) {
        log.debug("Request to get XaracterEquippedWeapon : {}", id);
        return xaracterEquippedWeaponRepository.findById(id).map(xaracterEquippedWeaponMapper::toDto);
    }

    /**
     * Delete the xaracterEquippedWeapon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete XaracterEquippedWeapon : {}", id);
        xaracterEquippedWeaponRepository.deleteById(id);
    }
}
