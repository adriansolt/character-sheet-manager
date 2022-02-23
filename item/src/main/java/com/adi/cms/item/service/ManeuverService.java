package com.adi.cms.item.service;

import com.adi.cms.item.domain.Maneuver;
import com.adi.cms.item.repository.ManeuverRepository;
import com.adi.cms.item.service.dto.ManeuverDTO;
import com.adi.cms.item.service.mapper.ManeuverMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Maneuver}.
 */
@Service
@Transactional
public class ManeuverService {

    private final Logger log = LoggerFactory.getLogger(ManeuverService.class);

    private final ManeuverRepository maneuverRepository;

    private final ManeuverMapper maneuverMapper;

    public ManeuverService(ManeuverRepository maneuverRepository, ManeuverMapper maneuverMapper) {
        this.maneuverRepository = maneuverRepository;
        this.maneuverMapper = maneuverMapper;
    }

    /**
     * Save a maneuver.
     *
     * @param maneuverDTO the entity to save.
     * @return the persisted entity.
     */
    public ManeuverDTO save(ManeuverDTO maneuverDTO) {
        log.debug("Request to save Maneuver : {}", maneuverDTO);
        Maneuver maneuver = maneuverMapper.toEntity(maneuverDTO);
        maneuver = maneuverRepository.save(maneuver);
        return maneuverMapper.toDto(maneuver);
    }

    /**
     * Partially update a maneuver.
     *
     * @param maneuverDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ManeuverDTO> partialUpdate(ManeuverDTO maneuverDTO) {
        log.debug("Request to partially update Maneuver : {}", maneuverDTO);

        return maneuverRepository
            .findById(maneuverDTO.getId())
            .map(existingManeuver -> {
                maneuverMapper.partialUpdate(existingManeuver, maneuverDTO);

                return existingManeuver;
            })
            .map(maneuverRepository::save)
            .map(maneuverMapper::toDto);
    }

    /**
     * Get all the maneuvers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ManeuverDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Maneuvers");
        return maneuverRepository.findAll(pageable).map(maneuverMapper::toDto);
    }

    /**
     * Get one maneuver by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ManeuverDTO> findOne(Long id) {
        log.debug("Request to get Maneuver : {}", id);
        return maneuverRepository.findById(id).map(maneuverMapper::toDto);
    }

    /**
     * Delete the maneuver by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Maneuver : {}", id);
        maneuverRepository.deleteById(id);
    }
}
