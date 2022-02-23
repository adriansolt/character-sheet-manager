package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.CharacterAttribute;
import com.adi.cms.gateway.repository.CharacterAttributeRepository;
import com.adi.cms.gateway.service.dto.CharacterAttributeDTO;
import com.adi.cms.gateway.service.mapper.CharacterAttributeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<CharacterAttributeDTO> save(CharacterAttributeDTO characterAttributeDTO) {
        log.debug("Request to save CharacterAttribute : {}", characterAttributeDTO);
        return characterAttributeRepository
            .save(characterAttributeMapper.toEntity(characterAttributeDTO))
            .map(characterAttributeMapper::toDto);
    }

    /**
     * Partially update a characterAttribute.
     *
     * @param characterAttributeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CharacterAttributeDTO> partialUpdate(CharacterAttributeDTO characterAttributeDTO) {
        log.debug("Request to partially update CharacterAttribute : {}", characterAttributeDTO);

        return characterAttributeRepository
            .findById(characterAttributeDTO.getId())
            .map(existingCharacterAttribute -> {
                characterAttributeMapper.partialUpdate(existingCharacterAttribute, characterAttributeDTO);

                return existingCharacterAttribute;
            })
            .flatMap(characterAttributeRepository::save)
            .map(characterAttributeMapper::toDto);
    }

    /**
     * Get all the characterAttributes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CharacterAttributeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CharacterAttributes");
        return characterAttributeRepository.findAllBy(pageable).map(characterAttributeMapper::toDto);
    }

    /**
     * Returns the number of characterAttributes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return characterAttributeRepository.count();
    }

    /**
     * Get one characterAttribute by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CharacterAttributeDTO> findOne(Long id) {
        log.debug("Request to get CharacterAttribute : {}", id);
        return characterAttributeRepository.findById(id).map(characterAttributeMapper::toDto);
    }

    /**
     * Delete the characterAttribute by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete CharacterAttribute : {}", id);
        return characterAttributeRepository.deleteById(id);
    }
}
