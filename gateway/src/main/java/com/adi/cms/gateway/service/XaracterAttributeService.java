package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.XaracterAttribute;
import com.adi.cms.gateway.repository.XaracterAttributeRepository;
import com.adi.cms.gateway.service.dto.XaracterAttributeDTO;
import com.adi.cms.gateway.service.mapper.XaracterAttributeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<XaracterAttributeDTO> save(XaracterAttributeDTO xaracterAttributeDTO) {
        log.debug("Request to save XaracterAttribute : {}", xaracterAttributeDTO);
        return xaracterAttributeRepository.save(xaracterAttributeMapper.toEntity(xaracterAttributeDTO)).map(xaracterAttributeMapper::toDto);
    }

    /**
     * Partially update a xaracterAttribute.
     *
     * @param xaracterAttributeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<XaracterAttributeDTO> partialUpdate(XaracterAttributeDTO xaracterAttributeDTO) {
        log.debug("Request to partially update XaracterAttribute : {}", xaracterAttributeDTO);

        return xaracterAttributeRepository
            .findById(xaracterAttributeDTO.getId())
            .map(existingXaracterAttribute -> {
                xaracterAttributeMapper.partialUpdate(existingXaracterAttribute, xaracterAttributeDTO);

                return existingXaracterAttribute;
            })
            .flatMap(xaracterAttributeRepository::save)
            .map(xaracterAttributeMapper::toDto);
    }

    /**
     * Get all the xaracterAttributes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<XaracterAttributeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all XaracterAttributes");
        return xaracterAttributeRepository.findAllBy(pageable).map(xaracterAttributeMapper::toDto);
    }

    /**
     * Returns the number of xaracterAttributes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return xaracterAttributeRepository.count();
    }

    /**
     * Get one xaracterAttribute by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<XaracterAttributeDTO> findOne(Long id) {
        log.debug("Request to get XaracterAttribute : {}", id);
        return xaracterAttributeRepository.findById(id).map(xaracterAttributeMapper::toDto);
    }

    /**
     * Delete the xaracterAttribute by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete XaracterAttribute : {}", id);
        return xaracterAttributeRepository.deleteById(id);
    }
}
