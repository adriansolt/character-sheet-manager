package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.WeaponManeuver;
import com.adi.cms.gateway.repository.WeaponManeuverRepository;
import com.adi.cms.gateway.service.dto.WeaponManeuverDTO;
import com.adi.cms.gateway.service.mapper.WeaponManeuverMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<WeaponManeuverDTO> save(WeaponManeuverDTO weaponManeuverDTO) {
        log.debug("Request to save WeaponManeuver : {}", weaponManeuverDTO);
        return weaponManeuverRepository.save(weaponManeuverMapper.toEntity(weaponManeuverDTO)).map(weaponManeuverMapper::toDto);
    }

    /**
     * Partially update a weaponManeuver.
     *
     * @param weaponManeuverDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<WeaponManeuverDTO> partialUpdate(WeaponManeuverDTO weaponManeuverDTO) {
        log.debug("Request to partially update WeaponManeuver : {}", weaponManeuverDTO);

        return weaponManeuverRepository
            .findById(weaponManeuverDTO.getId())
            .map(existingWeaponManeuver -> {
                weaponManeuverMapper.partialUpdate(existingWeaponManeuver, weaponManeuverDTO);

                return existingWeaponManeuver;
            })
            .flatMap(weaponManeuverRepository::save)
            .map(weaponManeuverMapper::toDto);
    }

    /**
     * Get all the weaponManeuvers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WeaponManeuverDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WeaponManeuvers");
        return weaponManeuverRepository.findAllBy(pageable).map(weaponManeuverMapper::toDto);
    }

    /**
     * Returns the number of weaponManeuvers available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return weaponManeuverRepository.count();
    }

    /**
     * Get one weaponManeuver by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<WeaponManeuverDTO> findOne(Long id) {
        log.debug("Request to get WeaponManeuver : {}", id);
        return weaponManeuverRepository.findById(id).map(weaponManeuverMapper::toDto);
    }

    /**
     * Delete the weaponManeuver by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete WeaponManeuver : {}", id);
        return weaponManeuverRepository.deleteById(id);
    }
}
