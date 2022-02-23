package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.CharacterEquippedWeapon;
import com.adi.cms.gateway.repository.CharacterEquippedWeaponRepository;
import com.adi.cms.gateway.service.dto.CharacterEquippedWeaponDTO;
import com.adi.cms.gateway.service.mapper.CharacterEquippedWeaponMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link CharacterEquippedWeapon}.
 */
@Service
@Transactional
public class CharacterEquippedWeaponService {

    private final Logger log = LoggerFactory.getLogger(CharacterEquippedWeaponService.class);

    private final CharacterEquippedWeaponRepository characterEquippedWeaponRepository;

    private final CharacterEquippedWeaponMapper characterEquippedWeaponMapper;

    public CharacterEquippedWeaponService(
        CharacterEquippedWeaponRepository characterEquippedWeaponRepository,
        CharacterEquippedWeaponMapper characterEquippedWeaponMapper
    ) {
        this.characterEquippedWeaponRepository = characterEquippedWeaponRepository;
        this.characterEquippedWeaponMapper = characterEquippedWeaponMapper;
    }

    /**
     * Save a characterEquippedWeapon.
     *
     * @param characterEquippedWeaponDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CharacterEquippedWeaponDTO> save(CharacterEquippedWeaponDTO characterEquippedWeaponDTO) {
        log.debug("Request to save CharacterEquippedWeapon : {}", characterEquippedWeaponDTO);
        return characterEquippedWeaponRepository
            .save(characterEquippedWeaponMapper.toEntity(characterEquippedWeaponDTO))
            .map(characterEquippedWeaponMapper::toDto);
    }

    /**
     * Partially update a characterEquippedWeapon.
     *
     * @param characterEquippedWeaponDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CharacterEquippedWeaponDTO> partialUpdate(CharacterEquippedWeaponDTO characterEquippedWeaponDTO) {
        log.debug("Request to partially update CharacterEquippedWeapon : {}", characterEquippedWeaponDTO);

        return characterEquippedWeaponRepository
            .findById(characterEquippedWeaponDTO.getId())
            .map(existingCharacterEquippedWeapon -> {
                characterEquippedWeaponMapper.partialUpdate(existingCharacterEquippedWeapon, characterEquippedWeaponDTO);

                return existingCharacterEquippedWeapon;
            })
            .flatMap(characterEquippedWeaponRepository::save)
            .map(characterEquippedWeaponMapper::toDto);
    }

    /**
     * Get all the characterEquippedWeapons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CharacterEquippedWeaponDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CharacterEquippedWeapons");
        return characterEquippedWeaponRepository.findAllBy(pageable).map(characterEquippedWeaponMapper::toDto);
    }

    /**
     * Returns the number of characterEquippedWeapons available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return characterEquippedWeaponRepository.count();
    }

    /**
     * Get one characterEquippedWeapon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CharacterEquippedWeaponDTO> findOne(Long id) {
        log.debug("Request to get CharacterEquippedWeapon : {}", id);
        return characterEquippedWeaponRepository.findById(id).map(characterEquippedWeaponMapper::toDto);
    }

    /**
     * Delete the characterEquippedWeapon by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete CharacterEquippedWeapon : {}", id);
        return characterEquippedWeaponRepository.deleteById(id);
    }
}
