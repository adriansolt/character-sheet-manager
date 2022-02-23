package com.adi.cms.xaracter.service;

import com.adi.cms.xaracter.domain.Xaracter;
import com.adi.cms.xaracter.repository.XaracterRepository;
import com.adi.cms.xaracter.service.dto.XaracterDTO;
import com.adi.cms.xaracter.service.mapper.XaracterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Xaracter}.
 */
@Service
@Transactional
public class XaracterService {

    private final Logger log = LoggerFactory.getLogger(XaracterService.class);

    private final XaracterRepository xaracterRepository;

    private final XaracterMapper xaracterMapper;

    public XaracterService(XaracterRepository xaracterRepository, XaracterMapper xaracterMapper) {
        this.xaracterRepository = xaracterRepository;
        this.xaracterMapper = xaracterMapper;
    }

    /**
     * Save a xaracter.
     *
     * @param xaracterDTO the entity to save.
     * @return the persisted entity.
     */
    public XaracterDTO save(XaracterDTO xaracterDTO) {
        log.debug("Request to save Xaracter : {}", xaracterDTO);
        Xaracter xaracter = xaracterMapper.toEntity(xaracterDTO);
        xaracter = xaracterRepository.save(xaracter);
        return xaracterMapper.toDto(xaracter);
    }

    /**
     * Partially update a xaracter.
     *
     * @param xaracterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<XaracterDTO> partialUpdate(XaracterDTO xaracterDTO) {
        log.debug("Request to partially update Xaracter : {}", xaracterDTO);

        return xaracterRepository
            .findById(xaracterDTO.getId())
            .map(existingXaracter -> {
                xaracterMapper.partialUpdate(existingXaracter, xaracterDTO);

                return existingXaracter;
            })
            .map(xaracterRepository::save)
            .map(xaracterMapper::toDto);
    }

    /**
     * Get all the xaracters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<XaracterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Xaracters");
        return xaracterRepository.findAll(pageable).map(xaracterMapper::toDto);
    }

    /**
     * Get one xaracter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<XaracterDTO> findOne(Long id) {
        log.debug("Request to get Xaracter : {}", id);
        return xaracterRepository.findById(id).map(xaracterMapper::toDto);
    }

    /**
     * Delete the xaracter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Xaracter : {}", id);
        xaracterRepository.deleteById(id);
    }
}
