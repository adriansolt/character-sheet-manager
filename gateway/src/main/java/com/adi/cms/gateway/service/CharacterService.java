package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.Character;
import com.adi.cms.gateway.repository.CharacterRepository;
import com.adi.cms.gateway.service.dto.CharacterDTO;
import com.adi.cms.gateway.service.mapper.CharacterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<CharacterDTO> save(CharacterDTO characterDTO) {
        log.debug("Request to save Character : {}", characterDTO);
        return characterRepository.save(characterMapper.toEntity(characterDTO)).map(characterMapper::toDto);
    }

    /**
     * Partially update a character.
     *
     * @param characterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CharacterDTO> partialUpdate(CharacterDTO characterDTO) {
        log.debug("Request to partially update Character : {}", characterDTO);

        return characterRepository
            .findById(characterDTO.getId())
            .map(existingCharacter -> {
                characterMapper.partialUpdate(existingCharacter, characterDTO);

                return existingCharacter;
            })
            .flatMap(characterRepository::save)
            .map(characterMapper::toDto);
    }

    /**
     * Get all the characters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CharacterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Characters");
        return characterRepository.findAllBy(pageable).map(characterMapper::toDto);
    }

    /**
     * Returns the number of characters available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return characterRepository.count();
    }

    /**
     * Get one character by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CharacterDTO> findOne(Long id) {
        log.debug("Request to get Character : {}", id);
        return characterRepository.findById(id).map(characterMapper::toDto);
    }

    /**
     * Delete the character by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Character : {}", id);
        return characterRepository.deleteById(id);
    }
}
