package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.PrereqSkillOrAtribute;
import com.adi.cms.gateway.repository.PrereqSkillOrAtributeRepository;
import com.adi.cms.gateway.service.dto.PrereqSkillOrAtributeDTO;
import com.adi.cms.gateway.service.mapper.PrereqSkillOrAtributeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<PrereqSkillOrAtributeDTO> save(PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO) {
        log.debug("Request to save PrereqSkillOrAtribute : {}", prereqSkillOrAtributeDTO);
        return prereqSkillOrAtributeRepository
            .save(prereqSkillOrAtributeMapper.toEntity(prereqSkillOrAtributeDTO))
            .map(prereqSkillOrAtributeMapper::toDto);
    }

    /**
     * Partially update a prereqSkillOrAtribute.
     *
     * @param prereqSkillOrAtributeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PrereqSkillOrAtributeDTO> partialUpdate(PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO) {
        log.debug("Request to partially update PrereqSkillOrAtribute : {}", prereqSkillOrAtributeDTO);

        return prereqSkillOrAtributeRepository
            .findById(prereqSkillOrAtributeDTO.getId())
            .map(existingPrereqSkillOrAtribute -> {
                prereqSkillOrAtributeMapper.partialUpdate(existingPrereqSkillOrAtribute, prereqSkillOrAtributeDTO);

                return existingPrereqSkillOrAtribute;
            })
            .flatMap(prereqSkillOrAtributeRepository::save)
            .map(prereqSkillOrAtributeMapper::toDto);
    }

    /**
     * Get all the prereqSkillOrAtributes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PrereqSkillOrAtributeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrereqSkillOrAtributes");
        return prereqSkillOrAtributeRepository.findAllBy(pageable).map(prereqSkillOrAtributeMapper::toDto);
    }

    /**
     * Returns the number of prereqSkillOrAtributes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return prereqSkillOrAtributeRepository.count();
    }

    /**
     * Get one prereqSkillOrAtribute by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PrereqSkillOrAtributeDTO> findOne(Long id) {
        log.debug("Request to get PrereqSkillOrAtribute : {}", id);
        return prereqSkillOrAtributeRepository.findById(id).map(prereqSkillOrAtributeMapper::toDto);
    }

    /**
     * Delete the prereqSkillOrAtribute by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PrereqSkillOrAtribute : {}", id);
        return prereqSkillOrAtributeRepository.deleteById(id);
    }
}
