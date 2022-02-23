package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.ArmorPiece;
import com.adi.cms.gateway.repository.ArmorPieceRepository;
import com.adi.cms.gateway.service.dto.ArmorPieceDTO;
import com.adi.cms.gateway.service.mapper.ArmorPieceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ArmorPiece}.
 */
@Service
@Transactional
public class ArmorPieceService {

    private final Logger log = LoggerFactory.getLogger(ArmorPieceService.class);

    private final ArmorPieceRepository armorPieceRepository;

    private final ArmorPieceMapper armorPieceMapper;

    public ArmorPieceService(ArmorPieceRepository armorPieceRepository, ArmorPieceMapper armorPieceMapper) {
        this.armorPieceRepository = armorPieceRepository;
        this.armorPieceMapper = armorPieceMapper;
    }

    /**
     * Save a armorPiece.
     *
     * @param armorPieceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ArmorPieceDTO> save(ArmorPieceDTO armorPieceDTO) {
        log.debug("Request to save ArmorPiece : {}", armorPieceDTO);
        return armorPieceRepository.save(armorPieceMapper.toEntity(armorPieceDTO)).map(armorPieceMapper::toDto);
    }

    /**
     * Partially update a armorPiece.
     *
     * @param armorPieceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ArmorPieceDTO> partialUpdate(ArmorPieceDTO armorPieceDTO) {
        log.debug("Request to partially update ArmorPiece : {}", armorPieceDTO);

        return armorPieceRepository
            .findById(armorPieceDTO.getId())
            .map(existingArmorPiece -> {
                armorPieceMapper.partialUpdate(existingArmorPiece, armorPieceDTO);

                return existingArmorPiece;
            })
            .flatMap(armorPieceRepository::save)
            .map(armorPieceMapper::toDto);
    }

    /**
     * Get all the armorPieces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ArmorPieceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ArmorPieces");
        return armorPieceRepository.findAllBy(pageable).map(armorPieceMapper::toDto);
    }

    /**
     * Returns the number of armorPieces available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return armorPieceRepository.count();
    }

    /**
     * Get one armorPiece by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ArmorPieceDTO> findOne(Long id) {
        log.debug("Request to get ArmorPiece : {}", id);
        return armorPieceRepository.findById(id).map(armorPieceMapper::toDto);
    }

    /**
     * Delete the armorPiece by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ArmorPiece : {}", id);
        return armorPieceRepository.deleteById(id);
    }
}
