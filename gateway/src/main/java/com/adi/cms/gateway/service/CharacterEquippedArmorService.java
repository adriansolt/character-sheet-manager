package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.CharacterEquippedArmor;
import com.adi.cms.gateway.repository.CharacterEquippedArmorRepository;
import com.adi.cms.gateway.service.dto.CharacterEquippedArmorDTO;
import com.adi.cms.gateway.service.mapper.CharacterEquippedArmorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<CharacterEquippedArmorDTO> save(CharacterEquippedArmorDTO characterEquippedArmorDTO) {
        log.debug("Request to save CharacterEquippedArmor : {}", characterEquippedArmorDTO);
        return characterEquippedArmorRepository
            .save(characterEquippedArmorMapper.toEntity(characterEquippedArmorDTO))
            .map(characterEquippedArmorMapper::toDto);
    }

    /**
     * Partially update a characterEquippedArmor.
     *
     * @param characterEquippedArmorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CharacterEquippedArmorDTO> partialUpdate(CharacterEquippedArmorDTO characterEquippedArmorDTO) {
        log.debug("Request to partially update CharacterEquippedArmor : {}", characterEquippedArmorDTO);

        return characterEquippedArmorRepository
            .findById(characterEquippedArmorDTO.getId())
            .map(existingCharacterEquippedArmor -> {
                characterEquippedArmorMapper.partialUpdate(existingCharacterEquippedArmor, characterEquippedArmorDTO);

                return existingCharacterEquippedArmor;
            })
            .flatMap(characterEquippedArmorRepository::save)
            .map(characterEquippedArmorMapper::toDto);
    }

    /**
     * Get all the characterEquippedArmors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CharacterEquippedArmorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CharacterEquippedArmors");
        return characterEquippedArmorRepository.findAllBy(pageable).map(characterEquippedArmorMapper::toDto);
    }

    /**
     * Returns the number of characterEquippedArmors available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return characterEquippedArmorRepository.count();
    }

    /**
     * Get one characterEquippedArmor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CharacterEquippedArmorDTO> findOne(Long id) {
        log.debug("Request to get CharacterEquippedArmor : {}", id);
        return characterEquippedArmorRepository.findById(id).map(characterEquippedArmorMapper::toDto);
    }

    /**
     * Delete the characterEquippedArmor by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete CharacterEquippedArmor : {}", id);
        return characterEquippedArmorRepository.deleteById(id);
    }
}
