package com.adi.cms.character.service;

import com.adi.cms.character.domain.CharacterSkill;
import com.adi.cms.character.repository.CharacterSkillRepository;
import com.adi.cms.character.service.dto.CharacterSkillDTO;
import com.adi.cms.character.service.mapper.CharacterSkillMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CharacterSkillDTO save(CharacterSkillDTO characterSkillDTO) {
        log.debug("Request to save CharacterSkill : {}", characterSkillDTO);
        CharacterSkill characterSkill = characterSkillMapper.toEntity(characterSkillDTO);
        characterSkill = characterSkillRepository.save(characterSkill);
        return characterSkillMapper.toDto(characterSkill);
    }

    /**
     * Partially update a characterSkill.
     *
     * @param characterSkillDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CharacterSkillDTO> partialUpdate(CharacterSkillDTO characterSkillDTO) {
        log.debug("Request to partially update CharacterSkill : {}", characterSkillDTO);

        return characterSkillRepository
            .findById(characterSkillDTO.getId())
            .map(existingCharacterSkill -> {
                characterSkillMapper.partialUpdate(existingCharacterSkill, characterSkillDTO);

                return existingCharacterSkill;
            })
            .map(characterSkillRepository::save)
            .map(characterSkillMapper::toDto);
    }

    /**
     * Get all the characterSkills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CharacterSkillDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CharacterSkills");
        return characterSkillRepository.findAll(pageable).map(characterSkillMapper::toDto);
    }

    /**
     * Get one characterSkill by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CharacterSkillDTO> findOne(Long id) {
        log.debug("Request to get CharacterSkill : {}", id);
        return characterSkillRepository.findById(id).map(characterSkillMapper::toDto);
    }

    /**
     * Delete the characterSkill by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CharacterSkill : {}", id);
        characterSkillRepository.deleteById(id);
    }
}
