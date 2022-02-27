package com.adi.cms.character.service;

import com.adi.cms.character.domain.PrereqSkillOrAtribute;
import com.adi.cms.character.repository.PrereqSkillOrAtributeRepository;
import com.adi.cms.character.service.dto.PrereqSkillOrAtributeDTO;
import com.adi.cms.character.service.mapper.PrereqSkillOrAtributeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PrereqSkillOrAtribute}.
 */
@Service
@Transactional
public class PrereqSkillOrAtributeService {

    private final Logger log = LoggerFactory.getLogger(PrereqSkillOrAtributeService.class);

    private final PrereqSkillOrAtributeRepository prereqSkillOrAtributeRepository;

    private final PrereqSkillOrAtributeMapper prereqSkillOrAtributeMapper;

    public PrereqSkillOrAtributeService(
        PrereqSkillOrAtributeRepository prereqSkillOrAtributeRepository,
        PrereqSkillOrAtributeMapper prereqSkillOrAtributeMapper
    ) {
        this.prereqSkillOrAtributeRepository = prereqSkillOrAtributeRepository;
        this.prereqSkillOrAtributeMapper = prereqSkillOrAtributeMapper;
    }

    /**
     * Save a prereqSkillOrAtribute.
     *
     * @param prereqSkillOrAtributeDTO the entity to save.
     * @return the persisted entity.
     */
    public PrereqSkillOrAtributeDTO save(PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO) {
        log.debug("Request to save PrereqSkillOrAtribute : {}", prereqSkillOrAtributeDTO);
        PrereqSkillOrAtribute prereqSkillOrAtribute = prereqSkillOrAtributeMapper.toEntity(prereqSkillOrAtributeDTO);
        prereqSkillOrAtribute = prereqSkillOrAtributeRepository.save(prereqSkillOrAtribute);
        return prereqSkillOrAtributeMapper.toDto(prereqSkillOrAtribute);
    }

    /**
     * Partially update a prereqSkillOrAtribute.
     *
     * @param prereqSkillOrAtributeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PrereqSkillOrAtributeDTO> partialUpdate(PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO) {
        log.debug("Request to partially update PrereqSkillOrAtribute : {}", prereqSkillOrAtributeDTO);

        return prereqSkillOrAtributeRepository
            .findById(prereqSkillOrAtributeDTO.getId())
            .map(existingPrereqSkillOrAtribute -> {
                prereqSkillOrAtributeMapper.partialUpdate(existingPrereqSkillOrAtribute, prereqSkillOrAtributeDTO);

                return existingPrereqSkillOrAtribute;
            })
            .map(prereqSkillOrAtributeRepository::save)
            .map(prereqSkillOrAtributeMapper::toDto);
    }

    /**
     * Get all the prereqSkillOrAtributes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PrereqSkillOrAtributeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrereqSkillOrAtributes");
        return prereqSkillOrAtributeRepository.findAll(pageable).map(prereqSkillOrAtributeMapper::toDto);
    }

    /**
     * Get all the prereqSkillOrAtributes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PrereqSkillOrAtributeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return prereqSkillOrAtributeRepository.findAllWithEagerRelationships(pageable).map(prereqSkillOrAtributeMapper::toDto);
    }

    /**
     * Get one prereqSkillOrAtribute by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PrereqSkillOrAtributeDTO> findOne(Long id) {
        log.debug("Request to get PrereqSkillOrAtribute : {}", id);
        return prereqSkillOrAtributeRepository.findOneWithEagerRelationships(id).map(prereqSkillOrAtributeMapper::toDto);
    }

    /**
     * Delete the prereqSkillOrAtribute by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PrereqSkillOrAtribute : {}", id);
        prereqSkillOrAtributeRepository.deleteById(id);
    }
}
