package com.adi.cms.item.web.rest;

import com.adi.cms.item.repository.ManeuverRepository;
import com.adi.cms.item.service.ManeuverService;
import com.adi.cms.item.service.dto.ManeuverDTO;
import com.adi.cms.item.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.adi.cms.item.domain.Maneuver}.
 */
@RestController
@RequestMapping("/api")
public class ManeuverResource {

    private final Logger log = LoggerFactory.getLogger(ManeuverResource.class);

    private static final String ENTITY_NAME = "itemManeuver";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManeuverService maneuverService;

    private final ManeuverRepository maneuverRepository;

    public ManeuverResource(ManeuverService maneuverService, ManeuverRepository maneuverRepository) {
        this.maneuverService = maneuverService;
        this.maneuverRepository = maneuverRepository;
    }

    /**
     * {@code POST  /maneuvers} : Create a new maneuver.
     *
     * @param maneuverDTO the maneuverDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new maneuverDTO, or with status {@code 400 (Bad Request)} if the maneuver has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/maneuvers")
    public ResponseEntity<ManeuverDTO> createManeuver(@Valid @RequestBody ManeuverDTO maneuverDTO) throws URISyntaxException {
        log.debug("REST request to save Maneuver : {}", maneuverDTO);
        if (maneuverDTO.getId() != null) {
            throw new BadRequestAlertException("A new maneuver cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ManeuverDTO result = maneuverService.save(maneuverDTO);
        return ResponseEntity
            .created(new URI("/api/maneuvers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /maneuvers/:id} : Updates an existing maneuver.
     *
     * @param id the id of the maneuverDTO to save.
     * @param maneuverDTO the maneuverDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maneuverDTO,
     * or with status {@code 400 (Bad Request)} if the maneuverDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the maneuverDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/maneuvers/{id}")
    public ResponseEntity<ManeuverDTO> updateManeuver(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ManeuverDTO maneuverDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Maneuver : {}, {}", id, maneuverDTO);
        if (maneuverDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maneuverDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maneuverRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ManeuverDTO result = maneuverService.save(maneuverDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maneuverDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /maneuvers/:id} : Partial updates given fields of an existing maneuver, field will ignore if it is null
     *
     * @param id the id of the maneuverDTO to save.
     * @param maneuverDTO the maneuverDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maneuverDTO,
     * or with status {@code 400 (Bad Request)} if the maneuverDTO is not valid,
     * or with status {@code 404 (Not Found)} if the maneuverDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the maneuverDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/maneuvers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ManeuverDTO> partialUpdateManeuver(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ManeuverDTO maneuverDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Maneuver partially : {}, {}", id, maneuverDTO);
        if (maneuverDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maneuverDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maneuverRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ManeuverDTO> result = maneuverService.partialUpdate(maneuverDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maneuverDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /maneuvers} : get all the maneuvers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maneuvers in body.
     */
    @GetMapping("/maneuvers")
    public ResponseEntity<List<ManeuverDTO>> getAllManeuvers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Maneuvers");
        Page<ManeuverDTO> page = maneuverService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /maneuvers/:id} : get the "id" maneuver.
     *
     * @param id the id of the maneuverDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the maneuverDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/maneuvers/{id}")
    public ResponseEntity<ManeuverDTO> getManeuver(@PathVariable Long id) {
        log.debug("REST request to get Maneuver : {}", id);
        Optional<ManeuverDTO> maneuverDTO = maneuverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(maneuverDTO);
    }

    /**
     * {@code DELETE  /maneuvers/:id} : delete the "id" maneuver.
     *
     * @param id the id of the maneuverDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/maneuvers/{id}")
    public ResponseEntity<Void> deleteManeuver(@PathVariable Long id) {
        log.debug("REST request to delete Maneuver : {}", id);
        maneuverService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
