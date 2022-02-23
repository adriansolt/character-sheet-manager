package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.XaracterSkill;
import com.adi.cms.gateway.repository.XaracterSkillRepository;
import com.adi.cms.gateway.service.dto.XaracterSkillDTO;
import com.adi.cms.gateway.service.mapper.XaracterSkillMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link XaracterSkill}.
 */
@Service
@Transactional
public class XaracterSkillService {

    private final Logger log = LoggerFactory.getLogger(XaracterSkillService.class);

    private final XaracterSkillRepository xaracterSkillRepository;

    private final XaracterSkillMapper xaracterSkillMapper;

    public XaracterSkillService(XaracterSkillRepository xaracterSkillRepository, XaracterSkillMapper xaracterSkillMapper) {
        this.xaracterSkillRepository = xaracterSkillRepository;
        this.xaracterSkillMapper = xaracterSkillMapper;
    }

    /**
     * Save a xaracterSkill.
     *
     * @param xaracterSkillDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<XaracterSkillDTO> save(XaracterSkillDTO xaracterSkillDTO) {
        log.debug("Request to save XaracterSkill : {}", xaracterSkillDTO);
        return xaracterSkillRepository.save(xaracterSkillMapper.toEntity(xaracterSkillDTO)).map(xaracterSkillMapper::toDto);
    }

    /**
     * Partially update a xaracterSkill.
     *
     * @param xaracterSkillDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<XaracterSkillDTO> partialUpdate(XaracterSkillDTO xaracterSkillDTO) {
        log.debug("Request to partially update XaracterSkill : {}", xaracterSkillDTO);

        return xaracterSkillRepository
            .findById(xaracterSkillDTO.getId())
            .map(existingXaracterSkill -> {
                xaracterSkillMapper.partialUpdate(existingXaracterSkill, xaracterSkillDTO);

                return existingXaracterSkill;
            })
            .flatMap(xaracterSkillRepository::save)
            .map(xaracterSkillMapper::toDto);
    }

    /**
     * Get all the xaracterSkills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<XaracterSkillDTO> findAll(Pageable pageable) {
        log.debug("Request to get all XaracterSkills");
        return xaracterSkillRepository.findAllBy(pageable).map(xaracterSkillMapper::toDto);
    }

    /**
     * Returns the number of xaracterSkills available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return xaracterSkillRepository.count();
    }

    /**
     * Get one xaracterSkill by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<XaracterSkillDTO> findOne(Long id) {
        log.debug("Request to get XaracterSkill : {}", id);
        return xaracterSkillRepository.findById(id).map(xaracterSkillMapper::toDto);
    }

    /**
     * Delete the xaracterSkill by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete XaracterSkill : {}", id);
        return xaracterSkillRepository.deleteById(id);
    }
}
