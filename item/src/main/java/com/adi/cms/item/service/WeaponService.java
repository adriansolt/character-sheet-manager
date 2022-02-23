package com.adi.cms.item.service;

import com.adi.cms.item.domain.Weapon;
import com.adi.cms.item.repository.WeaponRepository;
import com.adi.cms.item.service.dto.WeaponDTO;
import com.adi.cms.item.service.mapper.WeaponMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Weapon}.
 */
@Service
@Transactional
public class WeaponService {

    private final Logger log = LoggerFactory.getLogger(WeaponService.class);

    private final WeaponRepository weaponRepository;

    private final WeaponMapper weaponMapper;

    public WeaponService(WeaponRepository weaponRepository, WeaponMapper weaponMapper) {
        this.weaponRepository = weaponRepository;
        this.weaponMapper = weaponMapper;
    }

    /**
     * Save a weapon.
     *
     * @param weaponDTO the entity to save.
     * @return the persisted entity.
     */
    public WeaponDTO save(WeaponDTO weaponDTO) {
        log.debug("Request to save Weapon : {}", weaponDTO);
        Weapon weapon = weaponMapper.toEntity(weaponDTO);
        weapon = weaponRepository.save(weapon);
        return weaponMapper.toDto(weapon);
    }

    /**
     * Partially update a weapon.
     *
     * @param weaponDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WeaponDTO> partialUpdate(WeaponDTO weaponDTO) {
        log.debug("Request to partially update Weapon : {}", weaponDTO);

        return weaponRepository
            .findById(weaponDTO.getId())
            .map(existingWeapon -> {
                weaponMapper.partialUpdate(existingWeapon, weaponDTO);

                return existingWeapon;
            })
            .map(weaponRepository::save)
            .map(weaponMapper::toDto);
    }

    /**
     * Get all the weapons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WeaponDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Weapons");
        return weaponRepository.findAll(pageable).map(weaponMapper::toDto);
    }

    /**
     * Get one weapon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WeaponDTO> findOne(Long id) {
        log.debug("Request to get Weapon : {}", id);
        return weaponRepository.findById(id).map(weaponMapper::toDto);
    }

    /**
     * Delete the weapon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Weapon : {}", id);
        weaponRepository.deleteById(id);
    }
}
