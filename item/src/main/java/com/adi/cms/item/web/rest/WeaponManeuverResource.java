package com.adi.cms.item.web.rest;

import com.adi.cms.item.repository.WeaponManeuverRepository;
import com.adi.cms.item.service.WeaponManeuverService;
import com.adi.cms.item.service.dto.WeaponManeuverDTO;
import com.adi.cms.item.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.adi.cms.item.domain.WeaponManeuver}.
 */
@RestController
@RequestMapping("/api")
public class WeaponManeuverResource {

    private final Logger log = LoggerFactory.getLogger(WeaponManeuverResource.class);

    private static final String ENTITY_NAME = "itemWeaponManeuver";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeaponManeuverService weaponManeuverService;

    private final WeaponManeuverRepository weaponManeuverRepository;

    public WeaponManeuverResource(WeaponManeuverService weaponManeuverService, WeaponManeuverRepository weaponManeuverRepository) {
        this.weaponManeuverService = weaponManeuverService;
        this.weaponManeuverRepository = weaponManeuverRepository;
    }

    /**
     * {@code POST  /weapon-maneuvers} : Create a new weaponManeuver.
     *
     * @param weaponManeuverDTO the weaponManeuverDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weaponManeuverDTO, or with status {@code 400 (Bad Request)} if the weaponManeuver has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/weapon-maneuvers")
    public ResponseEntity<WeaponManeuverDTO> createWeaponManeuver(@RequestBody WeaponManeuverDTO weaponManeuverDTO)
        throws URISyntaxException {
        log.debug("REST request to save WeaponManeuver : {}", weaponManeuverDTO);
        if (weaponManeuverDTO.getId() != null) {
            throw new BadRequestAlertException("A new weaponManeuver cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WeaponManeuverDTO result = weaponManeuverService.save(weaponManeuverDTO);
        return ResponseEntity
            .created(new URI("/api/weapon-maneuvers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /weapon-maneuvers/:id} : Updates an existing weaponManeuver.
     *
     * @param id the id of the weaponManeuverDTO to save.
     * @param weaponManeuverDTO the weaponManeuverDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weaponManeuverDTO,
     * or with status {@code 400 (Bad Request)} if the weaponManeuverDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the weaponManeuverDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/weapon-maneuvers/{id}")
    public ResponseEntity<WeaponManeuverDTO> updateWeaponManeuver(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WeaponManeuverDTO weaponManeuverDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WeaponManeuver : {}, {}", id, weaponManeuverDTO);
        if (weaponManeuverDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weaponManeuverDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weaponManeuverRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WeaponManeuverDTO result = weaponManeuverService.save(weaponManeuverDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weaponManeuverDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /weapon-maneuvers/:id} : Partial updates given fields of an existing weaponManeuver, field will ignore if it is null
     *
     * @param id the id of the weaponManeuverDTO to save.
     * @param weaponManeuverDTO the weaponManeuverDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weaponManeuverDTO,
     * or with status {@code 400 (Bad Request)} if the weaponManeuverDTO is not valid,
     * or with status {@code 404 (Not Found)} if the weaponManeuverDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the weaponManeuverDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/weapon-maneuvers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WeaponManeuverDTO> partialUpdateWeaponManeuver(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WeaponManeuverDTO weaponManeuverDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WeaponManeuver partially : {}, {}", id, weaponManeuverDTO);
        if (weaponManeuverDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weaponManeuverDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weaponManeuverRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WeaponManeuverDTO> result = weaponManeuverService.partialUpdate(weaponManeuverDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weaponManeuverDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /weapon-maneuvers} : get all the weaponManeuvers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weaponManeuvers in body.
     */
    @GetMapping("/weapon-maneuvers")
    public ResponseEntity<List<WeaponManeuverDTO>> getAllWeaponManeuvers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of WeaponManeuvers");
        Page<WeaponManeuverDTO> page = weaponManeuverService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /weapon-maneuvers/:id} : get the "id" weaponManeuver.
     *
     * @param id the id of the weaponManeuverDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weaponManeuverDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/weapon-maneuvers/{id}")
    public ResponseEntity<WeaponManeuverDTO> getWeaponManeuver(@PathVariable Long id) {
        log.debug("REST request to get WeaponManeuver : {}", id);
        Optional<WeaponManeuverDTO> weaponManeuverDTO = weaponManeuverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(weaponManeuverDTO);
    }

    /**
     * {@code DELETE  /weapon-maneuvers/:id} : delete the "id" weaponManeuver.
     *
     * @param id the id of the weaponManeuverDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/weapon-maneuvers/{id}")
    public ResponseEntity<Void> deleteWeaponManeuver(@PathVariable Long id) {
        log.debug("REST request to delete WeaponManeuver : {}", id);
        weaponManeuverService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
