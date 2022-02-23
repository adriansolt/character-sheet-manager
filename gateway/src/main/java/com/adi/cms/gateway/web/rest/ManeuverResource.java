package com.adi.cms.gateway.web.rest;

import com.adi.cms.gateway.repository.ManeuverRepository;
import com.adi.cms.gateway.service.ManeuverService;
import com.adi.cms.gateway.service.dto.ManeuverDTO;
import com.adi.cms.gateway.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.adi.cms.gateway.domain.Maneuver}.
 */
@RestController
@RequestMapping("/api")
public class ManeuverResource {

    private final Logger log = LoggerFactory.getLogger(ManeuverResource.class);

    private static final String ENTITY_NAME = "maneuver";

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
    public Mono<ResponseEntity<ManeuverDTO>> createManeuver(@Valid @RequestBody ManeuverDTO maneuverDTO) throws URISyntaxException {
        log.debug("REST request to save Maneuver : {}", maneuverDTO);
        if (maneuverDTO.getId() != null) {
            throw new BadRequestAlertException("A new maneuver cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return maneuverService
            .save(maneuverDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/maneuvers/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
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
    public Mono<ResponseEntity<ManeuverDTO>> updateManeuver(
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

        return maneuverRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return maneuverService
                    .save(maneuverDTO)
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
    public Mono<ResponseEntity<ManeuverDTO>> partialUpdateManeuver(
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

        return maneuverRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ManeuverDTO> result = maneuverService.partialUpdate(maneuverDTO);

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
     * {@code GET  /maneuvers} : get all the maneuvers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maneuvers in body.
     */
    @GetMapping("/maneuvers")
    public Mono<ResponseEntity<List<ManeuverDTO>>> getAllManeuvers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Maneuvers");
        return maneuverService
            .countAll()
            .zipWith(maneuverService.findAll(pageable).collectList())
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
     * {@code GET  /maneuvers/:id} : get the "id" maneuver.
     *
     * @param id the id of the maneuverDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the maneuverDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/maneuvers/{id}")
    public Mono<ResponseEntity<ManeuverDTO>> getManeuver(@PathVariable Long id) {
        log.debug("REST request to get Maneuver : {}", id);
        Mono<ManeuverDTO> maneuverDTO = maneuverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(maneuverDTO);
    }

    /**
     * {@code DELETE  /maneuvers/:id} : delete the "id" maneuver.
     *
     * @param id the id of the maneuverDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/maneuvers/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteManeuver(@PathVariable Long id) {
        log.debug("REST request to delete Maneuver : {}", id);
        return maneuverService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
