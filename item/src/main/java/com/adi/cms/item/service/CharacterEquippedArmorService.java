package com.adi.cms.item.service;

import com.adi.cms.item.domain.CharacterEquippedArmor;
import com.adi.cms.item.repository.CharacterEquippedArmorRepository;
import com.adi.cms.item.service.dto.CharacterEquippedArmorDTO;
import com.adi.cms.item.service.mapper.CharacterEquippedArmorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CharacterEquippedArmor}.
 */
@Service
@Transactional
public class CharacterEquippedArmorService {

    private final Logger log = LoggerFactory.getLogger(CharacterEquippedArmorService.class);

    private final CharacterEquippedArmorRepository characterEquippedArmorRepository;

    private final CharacterEquippedArmorMapper characterEquippedArmorMapper;

    public CharacterEquippedArmorService(
        CharacterEquippedArmorRepository characterEquippedArmorRepository,
        CharacterEquippedArmorMapper characterEquippedArmorMapper
    ) {
        this.characterEquippedArmorRepository = characterEquippedArmorRepository;
        this.characterEquippedArmorMapper = characterEquippedArmorMapper;
    }

    /**
     * Save a characterEquippedArmor.
     *
     * @param characterEquippedArmorDTO the entity to save.
     * @return the persisted entity.
     */
    public CharacterEquippedArmorDTO save(CharacterEquippedArmorDTO characterEquippedArmorDTO) {
        log.debug("Request to save CharacterEquippedArmor : {}", characterEquippedArmorDTO);
        CharacterEquippedArmor characterEquippedArmor = characterEquippedArmorMapper.toEntity(characterEquippedArmorDTO);
        characterEquippedArmor = characterEquippedArmorRepository.save(characterEquippedArmor);
        return characterEquippedArmorMapper.toDto(characterEquippedArmor);
    }

    /**
     * Partially update a characterEquippedArmor.
     *
     * @param characterEquippedArmorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CharacterEquippedArmorDTO> partialUpdate(CharacterEquippedArmorDTO characterEquippedArmorDTO) {
        log.debug("Request to partially update CharacterEquippedArmor : {}", characterEquippedArmorDTO);

        return characterEquippedArmorRepository
            .findById(characterEquippedArmorDTO.getId())
            .map(existingCharacterEquippedArmor -> {
                characterEquippedArmorMapper.partialUpdate(existingCharacterEquippedArmor, characterEquippedArmorDTO);

                return existingCharacterEquippedArmor;
            })
            .map(characterEquippedArmorRepository::save)
            .map(characterEquippedArmorMapper::toDto);
    }

    /**
     * Get all the characterEquippedArmors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CharacterEquippedArmorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CharacterEquippedArmors");
        return characterEquippedArmorRepository.findAll(pageable).map(characterEquippedArmorMapper::toDto);
    }

    /**
     * Get one characterEquippedArmor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CharacterEquippedArmorDTO> findOne(Long id) {
        log.debug("Request to get CharacterEquippedArmor : {}", id);
        return characterEquippedArmorRepository.findById(id).map(characterEquippedArmorMapper::toDto);
    }

    /**
     * Delete the characterEquippedArmor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CharacterEquippedArmor : {}", id);
        characterEquippedArmorRepository.deleteById(id);
    }
}
