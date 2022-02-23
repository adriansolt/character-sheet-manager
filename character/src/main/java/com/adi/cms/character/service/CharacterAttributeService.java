package com.adi.cms.character.service;

import com.adi.cms.character.domain.CharacterAttribute;
import com.adi.cms.character.repository.CharacterAttributeRepository;
import com.adi.cms.character.service.dto.CharacterAttributeDTO;
import com.adi.cms.character.service.mapper.CharacterAttributeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CharacterAttribute}.
 */
@Service
@Transactional
public class CharacterAttributeService {

    private final Logger log = LoggerFactory.getLogger(CharacterAttributeService.class);

    private final CharacterAttributeRepository characterAttributeRepository;

    private final CharacterAttributeMapper characterAttributeMapper;

    public CharacterAttributeService(
        CharacterAttributeRepository characterAttributeRepository,
        CharacterAttributeMapper characterAttributeMapper
    ) {
        this.characterAttributeRepository = characterAttributeRepository;
        this.characterAttributeMapper = characterAttributeMapper;
    }

    /**
     * Save a characterAttribute.
     *
     * @param characterAttributeDTO the entity to save.
     * @return the persisted entity.
     */
    public CharacterAttributeDTO save(CharacterAttributeDTO characterAttributeDTO) {
        log.debug("Request to save CharacterAttribute : {}", characterAttributeDTO);
        CharacterAttribute characterAttribute = characterAttributeMapper.toEntity(characterAttributeDTO);
        characterAttribute = characterAttributeRepository.save(characterAttribute);
        return characterAttributeMapper.toDto(characterAttribute);
    }

    /**
     * Partially update a characterAttribute.
     *
     * @param characterAttributeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CharacterAttributeDTO> partialUpdate(CharacterAttributeDTO characterAttributeDTO) {
        log.debug("Request to partially update CharacterAttribute : {}", characterAttributeDTO);

        return characterAttributeRepository
            .findById(characterAttributeDTO.getId())
            .map(existingCharacterAttribute -> {
                characterAttributeMapper.partialUpdate(existingCharacterAttribute, characterAttributeDTO);

                return existingCharacterAttribute;
            })
            .map(characterAttributeRepository::save)
            .map(characterAttributeMapper::toDto);
    }

    /**
     * Get all the characterAttributes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CharacterAttributeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CharacterAttributes");
        return characterAttributeRepository.findAll(pageable).map(characterAttributeMapper::toDto);
    }

    /**
     * Get one characterAttribute by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CharacterAttributeDTO> findOne(Long id) {
        log.debug("Request to get CharacterAttribute : {}", id);
        return characterAttributeRepository.findById(id).map(characterAttributeMapper::toDto);
    }

    /**
     * Delete the characterAttribute by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CharacterAttribute : {}", id);
        characterAttributeRepository.deleteById(id);
    }
}
