package com.adi.cms.gateway.service;

import com.adi.cms.gateway.domain.Maneuver;
import com.adi.cms.gateway.repository.ManeuverRepository;
import com.adi.cms.gateway.service.dto.ManeuverDTO;
import com.adi.cms.gateway.service.mapper.ManeuverMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<ManeuverDTO> save(ManeuverDTO maneuverDTO) {
        log.debug("Request to save Maneuver : {}", maneuverDTO);
        return maneuverRepository.save(maneuverMapper.toEntity(maneuverDTO)).map(maneuverMapper::toDto);
    }

    /**
     * Partially update a maneuver.
     *
     * @param maneuverDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ManeuverDTO> partialUpdate(ManeuverDTO maneuverDTO) {
        log.debug("Request to partially update Maneuver : {}", maneuverDTO);

        return maneuverRepository
            .findById(maneuverDTO.getId())
            .map(existingManeuver -> {
                maneuverMapper.partialUpdate(existingManeuver, maneuverDTO);

                return existingManeuver;
            })
            .flatMap(maneuverRepository::save)
            .map(maneuverMapper::toDto);
    }

    /**
     * Get all the maneuvers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ManeuverDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Maneuvers");
        return maneuverRepository.findAllBy(pageable).map(maneuverMapper::toDto);
    }

    /**
     * Returns the number of maneuvers available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return maneuverRepository.count();
    }

    /**
     * Get one maneuver by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ManeuverDTO> findOne(Long id) {
        log.debug("Request to get Maneuver : {}", id);
        return maneuverRepository.findById(id).map(maneuverMapper::toDto);
    }

    /**
     * Delete the maneuver by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Maneuver : {}", id);
        return maneuverRepository.deleteById(id);
    }
}
