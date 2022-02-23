package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.Xaracter;
import com.adi.cms.gateway.repository.XaracterRepository;
import com.adi.cms.gateway.service.dto.XaracterDTO;
import com.adi.cms.gateway.service.mapper.XaracterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<XaracterDTO> save(XaracterDTO xaracterDTO) {
        log.debug("Request to save Xaracter : {}", xaracterDTO);
        return xaracterRepository.save(xaracterMapper.toEntity(xaracterDTO)).map(xaracterMapper::toDto);
    }

    /**
     * Partially update a xaracter.
     *
     * @param xaracterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<XaracterDTO> partialUpdate(XaracterDTO xaracterDTO) {
        log.debug("Request to partially update Xaracter : {}", xaracterDTO);

        return xaracterRepository
            .findById(xaracterDTO.getId())
            .map(existingXaracter -> {
                xaracterMapper.partialUpdate(existingXaracter, xaracterDTO);

                return existingXaracter;
            })
            .flatMap(xaracterRepository::save)
            .map(xaracterMapper::toDto);
    }

    /**
     * Get all the xaracters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<XaracterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Xaracters");
        return xaracterRepository.findAllBy(pageable).map(xaracterMapper::toDto);
    }

    /**
     * Returns the number of xaracters available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return xaracterRepository.count();
    }

    /**
     * Get one xaracter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<XaracterDTO> findOne(Long id) {
        log.debug("Request to get Xaracter : {}", id);
        return xaracterRepository.findById(id).map(xaracterMapper::toDto);
    }

    /**
     * Delete the xaracter by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Xaracter : {}", id);
        return xaracterRepository.deleteById(id);
    }
}
