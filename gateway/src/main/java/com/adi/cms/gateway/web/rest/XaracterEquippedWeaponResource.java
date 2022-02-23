package com.adi.cms.gateway.web.rest;

import com.adi.cms.gateway.repository.XaracterEquippedWeaponRepository;
import com.adi.cms.gateway.service.XaracterEquippedWeaponService;
import com.adi.cms.gateway.service.dto.XaracterEquippedWeaponDTO;
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
 * REST controller for managing {@link com.adi.cms.gateway.domain.XaracterEquippedWeapon}.
 */
@RestController
@RequestMapping("/api")
public class XaracterEquippedWeaponResource {

    private final Logger log = LoggerFactory.getLogger(XaracterEquippedWeaponResource.class);

    private static final String ENTITY_NAME = "xaracterEquippedWeapon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final XaracterEquippedWeaponService xaracterEquippedWeaponService;

    private final XaracterEquippedWeaponRepository xaracterEquippedWeaponRepository;

    public XaracterEquippedWeaponResource(
        XaracterEquippedWeaponService xaracterEquippedWeaponService,
        XaracterEquippedWeaponRepository xaracterEquippedWeaponRepository
    ) {
        this.xaracterEquippedWeaponService = xaracterEquippedWeaponService;
        this.xaracterEquippedWeaponRepository = xaracterEquippedWeaponRepository;
    }

    /**
     * {@code POST  /xaracter-equipped-weapons} : Create a new xaracterEquippedWeapon.
     *
     * @param xaracterEquippedWeaponDTO the xaracterEquippedWeaponDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new xaracterEquippedWeaponDTO, or with status {@code 400 (Bad Request)} if the xaracterEquippedWeapon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/xaracter-equipped-weapons")
    public Mono<ResponseEntity<XaracterEquippedWeaponDTO>> createXaracterEquippedWeapon(
        @RequestBody XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO
    ) throws URISyntaxException {
        log.debug("REST request to save XaracterEquippedWeapon : {}", xaracterEquippedWeaponDTO);
        if (xaracterEquippedWeaponDTO.getId() != null) {
            throw new BadRequestAlertException("A new xaracterEquippedWeapon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return xaracterEquippedWeaponService
            .save(xaracterEquippedWeaponDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/xaracter-equipped-weapons/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /xaracter-equipped-weapons/:id} : Updates an existing xaracterEquippedWeapon.
     *
     * @param id the id of the xaracterEquippedWeaponDTO to save.
     * @param xaracterEquippedWeaponDTO the xaracterEquippedWeaponDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xaracterEquippedWeaponDTO,
     * or with status {@code 400 (Bad Request)} if the xaracterEquippedWeaponDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the xaracterEquippedWeaponDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/xaracter-equipped-weapons/{id}")
    public Mono<ResponseEntity<XaracterEquippedWeaponDTO>> updateXaracterEquippedWeapon(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO
    ) throws URISyntaxException {
        log.debug("REST request to update XaracterEquippedWeapon : {}, {}", id, xaracterEquippedWeaponDTO);
        if (xaracterEquippedWeaponDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xaracterEquippedWeaponDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return xaracterEquippedWeaponRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return xaracterEquippedWeaponService
                    .save(xaracterEquippedWeaponDTO)
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
     * {@code PATCH  /xaracter-equipped-weapons/:id} : Partial updates given fields of an existing xaracterEquippedWeapon, field will ignore if it is null
     *
     * @param id the id of the xaracterEquippedWeaponDTO to save.
     * @param xaracterEquippedWeaponDTO the xaracterEquippedWeaponDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xaracterEquippedWeaponDTO,
     * or with status {@code 400 (Bad Request)} if the xaracterEquippedWeaponDTO is not valid,
     * or with status {@code 404 (Not Found)} if the xaracterEquippedWeaponDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the xaracterEquippedWeaponDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/xaracter-equipped-weapons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<XaracterEquippedWeaponDTO>> partialUpdateXaracterEquippedWeapon(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update XaracterEquippedWeapon partially : {}, {}", id, xaracterEquippedWeaponDTO);
        if (xaracterEquippedWeaponDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xaracterEquippedWeaponDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return xaracterEquippedWeaponRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<XaracterEquippedWeaponDTO> result = xaracterEquippedWeaponService.partialUpdate(xaracterEquippedWeaponDTO);

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
     * {@code GET  /xaracter-equipped-weapons} : get all the xaracterEquippedWeapons.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of xaracterEquippedWeapons in body.
     */
    @GetMapping("/xaracter-equipped-weapons")
    public Mono<ResponseEntity<List<XaracterEquippedWeaponDTO>>> getAllXaracterEquippedWeapons(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of XaracterEquippedWeapons");
        return xaracterEquippedWeaponService
            .countAll()
            .zipWith(xaracterEquippedWeaponService.findAll(pageable).collectList())
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
     * {@code GET  /xaracter-equipped-weapons/:id} : get the "id" xaracterEquippedWeapon.
     *
     * @param id the id of the xaracterEquippedWeaponDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the xaracterEquippedWeaponDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/xaracter-equipped-weapons/{id}")
    public Mono<ResponseEntity<XaracterEquippedWeaponDTO>> getXaracterEquippedWeapon(@PathVariable Long id) {
        log.debug("REST request to get XaracterEquippedWeapon : {}", id);
        Mono<XaracterEquippedWeaponDTO> xaracterEquippedWeaponDTO = xaracterEquippedWeaponService.findOne(id);
        return ResponseUtil.wrapOrNotFound(xaracterEquippedWeaponDTO);
    }

    /**
     * {@code DELETE  /xaracter-equipped-weapons/:id} : delete the "id" xaracterEquippedWeapon.
     *
     * @param id the id of the xaracterEquippedWeaponDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/xaracter-equipped-weapons/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteXaracterEquippedWeapon(@PathVariable Long id) {
        log.debug("REST request to delete XaracterEquippedWeapon : {}", id);
        return xaracterEquippedWeaponService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
