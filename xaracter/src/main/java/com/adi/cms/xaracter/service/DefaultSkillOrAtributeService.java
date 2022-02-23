package com.adi.cms.xaracter.service;

import com.adi.cms.xaracter.domain.DefaultSkillOrAtribute;
import com.adi.cms.xaracter.repository.DefaultSkillOrAtributeRepository;
import com.adi.cms.xaracter.service.dto.DefaultSkillOrAtributeDTO;
import com.adi.cms.xaracter.service.mapper.DefaultSkillOrAtributeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public DefaultSkillOrAtributeDTO save(DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO) {
        log.debug("Request to save DefaultSkillOrAtribute : {}", defaultSkillOrAtributeDTO);
        DefaultSkillOrAtribute defaultSkillOrAtribute = defaultSkillOrAtributeMapper.toEntity(defaultSkillOrAtributeDTO);
        defaultSkillOrAtribute = defaultSkillOrAtributeRepository.save(defaultSkillOrAtribute);
        return defaultSkillOrAtributeMapper.toDto(defaultSkillOrAtribute);
    }

    /**
     * Partially update a defaultSkillOrAtribute.
     *
     * @param defaultSkillOrAtributeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DefaultSkillOrAtributeDTO> partialUpdate(DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO) {
        log.debug("Request to partially update DefaultSkillOrAtribute : {}", defaultSkillOrAtributeDTO);

        return defaultSkillOrAtributeRepository
            .findById(defaultSkillOrAtributeDTO.getId())
            .map(existingDefaultSkillOrAtribute -> {
                defaultSkillOrAtributeMapper.partialUpdate(existingDefaultSkillOrAtribute, defaultSkillOrAtributeDTO);

                return existingDefaultSkillOrAtribute;
            })
            .map(defaultSkillOrAtributeRepository::save)
            .map(defaultSkillOrAtributeMapper::toDto);
    }

    /**
     * Get all the defaultSkillOrAtributes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DefaultSkillOrAtributeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DefaultSkillOrAtributes");
        return defaultSkillOrAtributeRepository.findAll(pageable).map(defaultSkillOrAtributeMapper::toDto);
    }

    /**
     * Get one defaultSkillOrAtribute by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DefaultSkillOrAtributeDTO> findOne(Long id) {
        log.debug("Request to get DefaultSkillOrAtribute : {}", id);
        return defaultSkillOrAtributeRepository.findById(id).map(defaultSkillOrAtributeMapper::toDto);
    }

    /**
     * Delete the defaultSkillOrAtribute by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DefaultSkillOrAtribute : {}", id);
        defaultSkillOrAtributeRepository.deleteById(id);
    }
}
