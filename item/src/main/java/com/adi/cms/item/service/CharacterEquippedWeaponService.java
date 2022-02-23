package com.adi.cms.item.service;

import com.adi.cms.item.domain.CharacterEquippedWeapon;
import com.adi.cms.item.repository.CharacterEquippedWeaponRepository;
import com.adi.cms.item.service.dto.CharacterEquippedWeaponDTO;
import com.adi.cms.item.service.mapper.CharacterEquippedWeaponMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CharacterEquippedWeaponDTO save(CharacterEquippedWeaponDTO characterEquippedWeaponDTO) {
        log.debug("Request to save CharacterEquippedWeapon : {}", characterEquippedWeaponDTO);
        CharacterEquippedWeapon characterEquippedWeapon = characterEquippedWeaponMapper.toEntity(characterEquippedWeaponDTO);
        characterEquippedWeapon = characterEquippedWeaponRepository.save(characterEquippedWeapon);
        return characterEquippedWeaponMapper.toDto(characterEquippedWeapon);
    }

    /**
     * Partially update a characterEquippedWeapon.
     *
     * @param characterEquippedWeaponDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CharacterEquippedWeaponDTO> partialUpdate(CharacterEquippedWeaponDTO characterEquippedWeaponDTO) {
        log.debug("Request to partially update CharacterEquippedWeapon : {}", characterEquippedWeaponDTO);

        return characterEquippedWeaponRepository
            .findById(characterEquippedWeaponDTO.getId())
            .map(existingCharacterEquippedWeapon -> {
                characterEquippedWeaponMapper.partialUpdate(existingCharacterEquippedWeapon, characterEquippedWeaponDTO);

                return existingCharacterEquippedWeapon;
            })
            .map(characterEquippedWeaponRepository::save)
            .map(characterEquippedWeaponMapper::toDto);
    }

    /**
     * Get all the characterEquippedWeapons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CharacterEquippedWeaponDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CharacterEquippedWeapons");
        return characterEquippedWeaponRepository.findAll(pageable).map(characterEquippedWeaponMapper::toDto);
    }

    /**
     * Get one characterEquippedWeapon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CharacterEquippedWeaponDTO> findOne(Long id) {
        log.debug("Request to get CharacterEquippedWeapon : {}", id);
        return characterEquippedWeaponRepository.findById(id).map(characterEquippedWeaponMapper::toDto);
    }

    /**
     * Delete the characterEquippedWeapon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CharacterEquippedWeapon : {}", id);
        characterEquippedWeaponRepository.deleteById(id);
    }
}
