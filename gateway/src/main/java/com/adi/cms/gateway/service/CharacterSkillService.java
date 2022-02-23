package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.CharacterSkill;
import com.adi.cms.gateway.repository.CharacterSkillRepository;
import com.adi.cms.gateway.service.dto.CharacterSkillDTO;
import com.adi.cms.gateway.service.mapper.CharacterSkillMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link CharacterSkill}.
 */
@Service
@Transactional
public class CharacterSkillService {

    private final Logger log = LoggerFactory.getLogger(CharacterSkillService.class);

    private final CharacterSkillRepository characterSkillRepository;

    private final CharacterSkillMapper characterSkillMapper;

    public CharacterSkillService(CharacterSkillRepository characterSkillRepository, CharacterSkillMapper characterSkillMapper) {
        this.characterSkillRepository = characterSkillRepository;
        this.characterSkillMapper = characterSkillMapper;
    }

    /**
     * Save a characterSkill.
     *
     * @param characterSkillDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CharacterSkillDTO> save(CharacterSkillDTO characterSkillDTO) {
        log.debug("Request to save CharacterSkill : {}", characterSkillDTO);
        return characterSkillRepository.save(characterSkillMapper.toEntity(characterSkillDTO)).map(characterSkillMapper::toDto);
    }

    /**
     * Partially update a characterSkill.
     *
     * @param characterSkillDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CharacterSkillDTO> partialUpdate(CharacterSkillDTO characterSkillDTO) {
        log.debug("Request to partially update CharacterSkill : {}", characterSkillDTO);

        return characterSkillRepository
            .findById(characterSkillDTO.getId())
            .map(existingCharacterSkill -> {
                characterSkillMapper.partialUpdate(existingCharacterSkill, characterSkillDTO);

                return existingCharacterSkill;
            })
            .flatMap(characterSkillRepository::save)
            .map(characterSkillMapper::toDto);
    }

    /**
     * Get all the characterSkills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CharacterSkillDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CharacterSkills");
        return characterSkillRepository.findAllBy(pageable).map(characterSkillMapper::toDto);
    }

    /**
     * Returns the number of characterSkills available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return characterSkillRepository.count();
    }

    /**
     * Get one characterSkill by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CharacterSkillDTO> findOne(Long id) {
        log.debug("Request to get CharacterSkill : {}", id);
        return characterSkillRepository.findById(id).map(characterSkillMapper::toDto);
    }

    /**
     * Delete the characterSkill by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete CharacterSkill : {}", id);
        return characterSkillRepository.deleteById(id);
    }
}
