package com.adi.cms.xaracter.service;

import com.adi.cms.xaracter.domain.XaracterAttribute;
import com.adi.cms.xaracter.repository.XaracterAttributeRepository;
import com.adi.cms.xaracter.service.dto.XaracterAttributeDTO;
import com.adi.cms.xaracter.service.mapper.XaracterAttributeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link XaracterAttribute}.
 */
@Service
@Transactional
public class XaracterAttributeService {

    private final Logger log = LoggerFactory.getLogger(XaracterAttributeService.class);

    private final XaracterAttributeRepository xaracterAttributeRepository;

    private final XaracterAttributeMapper xaracterAttributeMapper;

    public XaracterAttributeService(
        XaracterAttributeRepository xaracterAttributeRepository,
        XaracterAttributeMapper xaracterAttributeMapper
    ) {
        this.xaracterAttributeRepository = xaracterAttributeRepository;
        this.xaracterAttributeMapper = xaracterAttributeMapper;
    }

    /**
     * Save a xaracterAttribute.
     *
     * @param xaracterAttributeDTO the entity to save.
     * @return the persisted entity.
     */
    public XaracterAttributeDTO save(XaracterAttributeDTO xaracterAttributeDTO) {
        log.debug("Request to save XaracterAttribute : {}", xaracterAttributeDTO);
        XaracterAttribute xaracterAttribute = xaracterAttributeMapper.toEntity(xaracterAttributeDTO);
        xaracterAttribute = xaracterAttributeRepository.save(xaracterAttribute);
        return xaracterAttributeMapper.toDto(xaracterAttribute);
    }

    /**
     * Partially update a xaracterAttribute.
     *
     * @param xaracterAttributeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<XaracterAttributeDTO> partialUpdate(XaracterAttributeDTO xaracterAttributeDTO) {
        log.debug("Request to partially update XaracterAttribute : {}", xaracterAttributeDTO);

        return xaracterAttributeRepository
            .findById(xaracterAttributeDTO.getId())
            .map(existingXaracterAttribute -> {
                xaracterAttributeMapper.partialUpdate(existingXaracterAttribute, xaracterAttributeDTO);

                return existingXaracterAttribute;
            })
            .map(xaracterAttributeRepository::save)
            .map(xaracterAttributeMapper::toDto);
    }

    /**
     * Get all the xaracterAttributes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<XaracterAttributeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all XaracterAttributes");
        return xaracterAttributeRepository.findAll(pageable).map(xaracterAttributeMapper::toDto);
    }

    /**
     * Get one xaracterAttribute by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<XaracterAttributeDTO> findOne(Long id) {
        log.debug("Request to get XaracterAttribute : {}", id);
        return xaracterAttributeRepository.findById(id).map(xaracterAttributeMapper::toDto);
    }

    /**
     * Delete the xaracterAttribute by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete XaracterAttribute : {}", id);
        xaracterAttributeRepository.deleteById(id);
    }
}
