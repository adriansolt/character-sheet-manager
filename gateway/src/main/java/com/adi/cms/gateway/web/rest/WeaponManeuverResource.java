package com.adi.cms.gateway.web.rest;

import com.adi.cms.gateway.repository.WeaponManeuverRepository;
import com.adi.cms.gateway.service.WeaponManeuverService;
import com.adi.cms.gateway.service.dto.WeaponManeuverDTO;
import com.adi.cms.gateway.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.adi.cms.gateway.domain.WeaponManeuver}.
 */
@RestController
@RequestMapping("/api")
public class WeaponManeuverResource {

    private final Logger log = LoggerFactory.getLogger(WeaponManeuverResource.class);

    private static final String ENTITY_NAME = "weaponManeuver";

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
    public Mono<ResponseEntity<WeaponManeuverDTO>> createWeaponManeuver(@RequestBody WeaponManeuverDTO weaponManeuverDTO)
        throws URISyntaxException {
        log.debug("REST request to save WeaponManeuver : {}", weaponManeuverDTO);
        if (weaponManeuverDTO.getId() != null) {
            throw new BadRequestAlertException("A new weaponManeuver cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return weaponManeuverService
            .save(weaponManeuverDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/weapon-maneuvers/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
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
    public Mono<ResponseEntity<WeaponManeuverDTO>> updateWeaponManeuver(
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

        return weaponManeuverRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return weaponManeuverService
                    .save(weaponManeuverDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
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
    public Mono<ResponseEntity<WeaponManeuverDTO>> partialUpdateWeaponManeuver(
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

        return weaponManeuverRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<WeaponManeuverDTO> result = weaponManeuverService.partialUpdate(weaponManeuverDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /weapon-maneuvers} : get all the weaponManeuvers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weaponManeuvers in body.
     */
    @GetMapping("/weapon-maneuvers")
    public Mono<ResponseEntity<List<WeaponManeuverDTO>>> getAllWeaponManeuvers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of WeaponManeuvers");
        return weaponManeuverService
            .countAll()
            .zipWith(weaponManeuverService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /weapon-maneuvers/:id} : get the "id" weaponManeuver.
     *
     * @param id the id of the weaponManeuverDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weaponManeuverDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/weapon-maneuvers/{id}")
    public Mono<ResponseEntity<WeaponManeuverDTO>> getWeaponManeuver(@PathVariable Long id) {
        log.debug("REST request to get WeaponManeuver : {}", id);
        Mono<WeaponManeuverDTO> weaponManeuverDTO = weaponManeuverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(weaponManeuverDTO);
    }

    /**
     * {@code DELETE  /weapon-maneuvers/:id} : delete the "id" weaponManeuver.
     *
     * @param id the id of the weaponManeuverDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/weapon-maneuvers/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteWeaponManeuver(@PathVariable Long id) {
        log.debug("REST request to delete WeaponManeuver : {}", id);
        return weaponManeuverService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
