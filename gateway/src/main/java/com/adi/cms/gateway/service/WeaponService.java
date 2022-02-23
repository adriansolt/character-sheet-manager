package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.Weapon;
import com.adi.cms.gateway.repository.WeaponRepository;
import com.adi.cms.gateway.service.dto.WeaponDTO;
import com.adi.cms.gateway.service.mapper.WeaponMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<WeaponDTO> save(WeaponDTO weaponDTO) {
        log.debug("Request to save Weapon : {}", weaponDTO);
        return weaponRepository.save(weaponMapper.toEntity(weaponDTO)).map(weaponMapper::toDto);
    }

    /**
     * Partially update a weapon.
     *
     * @param weaponDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<WeaponDTO> partialUpdate(WeaponDTO weaponDTO) {
        log.debug("Request to partially update Weapon : {}", weaponDTO);

        return weaponRepository
            .findById(weaponDTO.getId())
            .map(existingWeapon -> {
                weaponMapper.partialUpdate(existingWeapon, weaponDTO);

                return existingWeapon;
            })
            .flatMap(weaponRepository::save)
            .map(weaponMapper::toDto);
    }

    /**
     * Get all the weapons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WeaponDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Weapons");
        return weaponRepository.findAllBy(pageable).map(weaponMapper::toDto);
    }

    /**
     * Returns the number of weapons available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return weaponRepository.count();
    }

    /**
     * Get one weapon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<WeaponDTO> findOne(Long id) {
        log.debug("Request to get Weapon : {}", id);
        return weaponRepository.findById(id).map(weaponMapper::toDto);
    }

    /**
     * Delete the weapon by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Weapon : {}", id);
        return weaponRepository.deleteById(id);
    }
}
