package com.adi.cms.xaracter.service;

import com.adi.cms.xaracter.domain.XaracterSkill;
import com.adi.cms.xaracter.repository.XaracterSkillRepository;
import com.adi.cms.xaracter.service.dto.XaracterSkillDTO;
import com.adi.cms.xaracter.service.mapper.XaracterSkillMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public XaracterSkillDTO save(XaracterSkillDTO xaracterSkillDTO) {
        log.debug("Request to save XaracterSkill : {}", xaracterSkillDTO);
        XaracterSkill xaracterSkill = xaracterSkillMapper.toEntity(xaracterSkillDTO);
        xaracterSkill = xaracterSkillRepository.save(xaracterSkill);
        return xaracterSkillMapper.toDto(xaracterSkill);
    }

    /**
     * Partially update a xaracterSkill.
     *
     * @param xaracterSkillDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<XaracterSkillDTO> partialUpdate(XaracterSkillDTO xaracterSkillDTO) {
        log.debug("Request to partially update XaracterSkill : {}", xaracterSkillDTO);

        return xaracterSkillRepository
            .findById(xaracterSkillDTO.getId())
            .map(existingXaracterSkill -> {
                xaracterSkillMapper.partialUpdate(existingXaracterSkill, xaracterSkillDTO);

                return existingXaracterSkill;
            })
            .map(xaracterSkillRepository::save)
            .map(xaracterSkillMapper::toDto);
    }

    /**
     * Get all the xaracterSkills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<XaracterSkillDTO> findAll(Pageable pageable) {
        log.debug("Request to get all XaracterSkills");
        return xaracterSkillRepository.findAll(pageable).map(xaracterSkillMapper::toDto);
    }

    /**
     * Get one xaracterSkill by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<XaracterSkillDTO> findOne(Long id) {
        log.debug("Request to get XaracterSkill : {}", id);
        return xaracterSkillRepository.findById(id).map(xaracterSkillMapper::toDto);
    }

    /**
     * Delete the xaracterSkill by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete XaracterSkill : {}", id);
        xaracterSkillRepository.deleteById(id);
    }
}
