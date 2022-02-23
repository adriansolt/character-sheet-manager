package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.DefaultSkillOrAtribute;
import com.adi.cms.gateway.repository.DefaultSkillOrAtributeRepository;
import com.adi.cms.gateway.service.dto.DefaultSkillOrAtributeDTO;
import com.adi.cms.gateway.service.mapper.DefaultSkillOrAtributeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link DefaultSkillOrAtribute}.
 */
@Service
@Transactional
public class DefaultSkillOrAtributeService {

    private final Logger log = LoggerFactory.getLogger(DefaultSkillOrAtributeService.class);

    private final DefaultSkillOrAtributeRepository defaultSkillOrAtributeRepository;

    private final DefaultSkillOrAtributeMapper defaultSkillOrAtributeMapper;

    public DefaultSkillOrAtributeService(
        DefaultSkillOrAtributeRepository defaultSkillOrAtributeRepository,
        DefaultSkillOrAtributeMapper defaultSkillOrAtributeMapper
    ) {
        this.defaultSkillOrAtributeRepository = defaultSkillOrAtributeRepository;
        this.defaultSkillOrAtributeMapper = defaultSkillOrAtributeMapper;
    }

    /**
     * Save a defaultSkillOrAtribute.
     *
     * @param defaultSkillOrAtributeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DefaultSkillOrAtributeDTO> save(DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO) {
        log.debug("Request to save DefaultSkillOrAtribute : {}", defaultSkillOrAtributeDTO);
        return defaultSkillOrAtributeRepository
            .save(defaultSkillOrAtributeMapper.toEntity(defaultSkillOrAtributeDTO))
            .map(defaultSkillOrAtributeMapper::toDto);
    }

    /**
     * Partially update a defaultSkillOrAtribute.
     *
     * @param defaultSkillOrAtributeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<DefaultSkillOrAtributeDTO> partialUpdate(DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO) {
        log.debug("Request to partially update DefaultSkillOrAtribute : {}", defaultSkillOrAtributeDTO);

        return defaultSkillOrAtributeRepository
            .findById(defaultSkillOrAtributeDTO.getId())
            .map(existingDefaultSkillOrAtribute -> {
                defaultSkillOrAtributeMapper.partialUpdate(existingDefaultSkillOrAtribute, defaultSkillOrAtributeDTO);

                return existingDefaultSkillOrAtribute;
            })
            .flatMap(defaultSkillOrAtributeRepository::save)
            .map(defaultSkillOrAtributeMapper::toDto);
    }

    /**
     * Get all the defaultSkillOrAtributes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DefaultSkillOrAtributeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DefaultSkillOrAtributes");
        return defaultSkillOrAtributeRepository.findAllBy(pageable).map(defaultSkillOrAtributeMapper::toDto);
    }

    /**
     * Returns the number of defaultSkillOrAtributes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return defaultSkillOrAtributeRepository.count();
    }

    /**
     * Get one defaultSkillOrAtribute by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<DefaultSkillOrAtributeDTO> findOne(Long id) {
        log.debug("Request to get DefaultSkillOrAtribute : {}", id);
        return defaultSkillOrAtributeRepository.findById(id).map(defaultSkillOrAtributeMapper::toDto);
    }

    /**
     * Delete the defaultSkillOrAtribute by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete DefaultSkillOrAtribute : {}", id);
        return defaultSkillOrAtributeRepository.deleteById(id);
    }
}
