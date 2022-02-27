package com.adi.cms.character.service;

import com.adi.cms.character.domain.Character;
import com.adi.cms.character.repository.CharacterRepository;
import com.adi.cms.character.service.dto.CharacterDTO;
import com.adi.cms.character.service.mapper.CharacterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Character}.
 */
@Service
@Transactional
public class CharacterService {

    private final Logger log = LoggerFactory.getLogger(CharacterService.class);

    private final CharacterRepository characterRepository;

    private final CharacterMapper characterMapper;

    public CharacterService(CharacterRepository characterRepository, CharacterMapper characterMapper) {
        this.characterRepository = characterRepository;
        this.characterMapper = characterMapper;
    }

    /**
     * Save a character.
     *
     * @param characterDTO the entity to save.
     * @return the persisted entity.
     */
    public CharacterDTO save(CharacterDTO characterDTO) {
        log.debug("Request to save Character : {}", characterDTO);
        Character character = characterMapper.toEntity(characterDTO);
        character = characterRepository.save(character);
        return characterMapper.toDto(character);
    }

    /**
     * Partially update a character.
     *
     * @param characterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CharacterDTO> partialUpdate(CharacterDTO characterDTO) {
        log.debug("Request to partially update Character : {}", characterDTO);

        return characterRepository
            .findById(characterDTO.getId())
            .map(existingCharacter -> {
                characterMapper.partialUpdate(existingCharacter, characterDTO);

                return existingCharacter;
            })
            .map(characterRepository::save)
            .map(characterMapper::toDto);
    }

    /**
     * Get all the characters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CharacterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Characters");
        return characterRepository.findAll(pageable).map(characterMapper::toDto);
    }

    /**
     * Get all the characters with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CharacterDTO> findAllWithEagerRelationships(Pageable pageable) {
        return characterRepository.findAllWithEagerRelationships(pageable).map(characterMapper::toDto);
    }

    /**
     * Get one character by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CharacterDTO> findOne(Long id) {
        log.debug("Request to get Character : {}", id);
        return characterRepository.findOneWithEagerRelationships(id).map(characterMapper::toDto);
    }

    /**
     * Delete the character by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Character : {}", id);
        characterRepository.deleteById(id);
    }
}
