package com.adi.cms.item.service;

import com.adi.cms.item.domain.ArmorPiece;
import com.adi.cms.item.repository.ArmorPieceRepository;
import com.adi.cms.item.service.dto.ArmorPieceDTO;
import com.adi.cms.item.service.mapper.ArmorPieceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ArmorPieceDTO save(ArmorPieceDTO armorPieceDTO) {
        log.debug("Request to save ArmorPiece : {}", armorPieceDTO);
        ArmorPiece armorPiece = armorPieceMapper.toEntity(armorPieceDTO);
        armorPiece = armorPieceRepository.save(armorPiece);
        return armorPieceMapper.toDto(armorPiece);
    }

    /**
     * Partially update a armorPiece.
     *
     * @param armorPieceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ArmorPieceDTO> partialUpdate(ArmorPieceDTO armorPieceDTO) {
        log.debug("Request to partially update ArmorPiece : {}", armorPieceDTO);

        return armorPieceRepository
            .findById(armorPieceDTO.getId())
            .map(existingArmorPiece -> {
                armorPieceMapper.partialUpdate(existingArmorPiece, armorPieceDTO);

                return existingArmorPiece;
            })
            .map(armorPieceRepository::save)
            .map(armorPieceMapper::toDto);
    }

    /**
     * Get all the armorPieces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ArmorPieceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ArmorPieces");
        return armorPieceRepository.findAll(pageable).map(armorPieceMapper::toDto);
    }

    /**
     * Get one armorPiece by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArmorPieceDTO> findOne(Long id) {
        log.debug("Request to get ArmorPiece : {}", id);
        return armorPieceRepository.findById(id).map(armorPieceMapper::toDto);
    }

    /**
     * Delete the armorPiece by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ArmorPiece : {}", id);
        armorPieceRepository.deleteById(id);
    }
}
