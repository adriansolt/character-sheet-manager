package com.adi.cms.item.service;

import com.adi.cms.item.domain.WeaponManeuver;
import com.adi.cms.item.repository.WeaponManeuverRepository;
import com.adi.cms.item.service.dto.WeaponManeuverDTO;
import com.adi.cms.item.service.mapper.WeaponManeuverMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WeaponManeuver}.
 */
@Service
@Transactional
public class WeaponManeuverService {

    private final Logger log = LoggerFactory.getLogger(WeaponManeuverService.class);

    private final WeaponManeuverRepository weaponManeuverRepository;

    private final WeaponManeuverMapper weaponManeuverMapper;

    public WeaponManeuverService(WeaponManeuverRepository weaponManeuverRepository, WeaponManeuverMapper weaponManeuverMapper) {
        this.weaponManeuverRepository = weaponManeuverRepository;
        this.weaponManeuverMapper = weaponManeuverMapper;
    }

    /**
     * Save a weaponManeuver.
     *
     * @param weaponManeuverDTO the entity to save.
     * @return the persisted entity.
     */
    public WeaponManeuverDTO save(WeaponManeuverDTO weaponManeuverDTO) {
        log.debug("Request to save WeaponManeuver : {}", weaponManeuverDTO);
        WeaponManeuver weaponManeuver = weaponManeuverMapper.toEntity(weaponManeuverDTO);
        weaponManeuver = weaponManeuverRepository.save(weaponManeuver);
        return weaponManeuverMapper.toDto(weaponManeuver);
    }

    /**
     * Partially update a weaponManeuver.
     *
     * @param weaponManeuverDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WeaponManeuverDTO> partialUpdate(WeaponManeuverDTO weaponManeuverDTO) {
        log.debug("Request to partially update WeaponManeuver : {}", weaponManeuverDTO);

        return weaponManeuverRepository
            .findById(weaponManeuverDTO.getId())
            .map(existingWeaponManeuver -> {
                weaponManeuverMapper.partialUpdate(existingWeaponManeuver, weaponManeuverDTO);

                return existingWeaponManeuver;
            })
            .map(weaponManeuverRepository::save)
            .map(weaponManeuverMapper::toDto);
    }

    /**
     * Get all the weaponManeuvers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WeaponManeuverDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WeaponManeuvers");
        return weaponManeuverRepository.findAll(pageable).map(weaponManeuverMapper::toDto);
    }

    /**
     * Get one weaponManeuver by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WeaponManeuverDTO> findOne(Long id) {
        log.debug("Request to get WeaponManeuver : {}", id);
        return weaponManeuverRepository.findById(id).map(weaponManeuverMapper::toDto);
    }

    /**
     * Delete the weaponManeuver by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WeaponManeuver : {}", id);
        weaponManeuverRepository.deleteById(id);
    }
}
