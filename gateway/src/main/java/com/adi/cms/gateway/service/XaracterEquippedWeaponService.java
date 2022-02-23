package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.XaracterEquippedWeapon;
import com.adi.cms.gateway.repository.XaracterEquippedWeaponRepository;
import com.adi.cms.gateway.service.dto.XaracterEquippedWeaponDTO;
import com.adi.cms.gateway.service.mapper.XaracterEquippedWeaponMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<XaracterEquippedWeaponDTO> save(XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO) {
        log.debug("Request to save XaracterEquippedWeapon : {}", xaracterEquippedWeaponDTO);
        return xaracterEquippedWeaponRepository
            .save(xaracterEquippedWeaponMapper.toEntity(xaracterEquippedWeaponDTO))
            .map(xaracterEquippedWeaponMapper::toDto);
    }

    /**
     * Partially update a xaracterEquippedWeapon.
     *
     * @param xaracterEquippedWeaponDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<XaracterEquippedWeaponDTO> partialUpdate(XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO) {
        log.debug("Request to partially update XaracterEquippedWeapon : {}", xaracterEquippedWeaponDTO);

        return xaracterEquippedWeaponRepository
            .findById(xaracterEquippedWeaponDTO.getId())
            .map(existingXaracterEquippedWeapon -> {
                xaracterEquippedWeaponMapper.partialUpdate(existingXaracterEquippedWeapon, xaracterEquippedWeaponDTO);

                return existingXaracterEquippedWeapon;
            })
            .flatMap(xaracterEquippedWeaponRepository::save)
            .map(xaracterEquippedWeaponMapper::toDto);
    }

    /**
     * Get all the xaracterEquippedWeapons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<XaracterEquippedWeaponDTO> findAll(Pageable pageable) {
        log.debug("Request to get all XaracterEquippedWeapons");
        return xaracterEquippedWeaponRepository.findAllBy(pageable).map(xaracterEquippedWeaponMapper::toDto);
    }

    /**
     * Returns the number of xaracterEquippedWeapons available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return xaracterEquippedWeaponRepository.count();
    }

    /**
     * Get one xaracterEquippedWeapon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<XaracterEquippedWeaponDTO> findOne(Long id) {
        log.debug("Request to get XaracterEquippedWeapon : {}", id);
        return xaracterEquippedWeaponRepository.findById(id).map(xaracterEquippedWeaponMapper::toDto);
    }

    /**
     * Delete the xaracterEquippedWeapon by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete XaracterEquippedWeapon : {}", id);
        return xaracterEquippedWeaponRepository.deleteById(id);
    }
}
