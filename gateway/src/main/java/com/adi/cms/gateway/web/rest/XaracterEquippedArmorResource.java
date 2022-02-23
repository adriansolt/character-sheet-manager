package com.adi.cms.gateway.web.rest;

import com.adi.cms.gateway.repository.XaracterEquippedArmorRepository;
import com.adi.cms.gateway.service.XaracterEquippedArmorService;
import com.adi.cms.gateway.service.dto.XaracterEquippedArmorDTO;
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
 * REST controller for managing {@link com.adi.cms.gateway.domain.XaracterEquippedArmor}.
 */
@RestController
@RequestMapping("/api")
public class XaracterEquippedArmorResource {

    private final Logger log = LoggerFactory.getLogger(XaracterEquippedArmorResource.class);

    private static final String ENTITY_NAME = "xaracterEquippedArmor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final XaracterEquippedArmorService xaracterEquippedArmorService;

    private final XaracterEquippedArmorRepository xaracterEquippedArmorRepository;

    public XaracterEquippedArmorResource(
        XaracterEquippedArmorService xaracterEquippedArmorService,
        XaracterEquippedArmorRepository xaracterEquippedArmorRepository
    ) {
        this.xaracterEquippedArmorService = xaracterEquippedArmorService;
        this.xaracterEquippedArmorRepository = xaracterEquippedArmorRepository;
    }

    /**
     * {@code POST  /xaracter-equipped-armors} : Create a new xaracterEquippedArmor.
     *
     * @param xaracterEquippedArmorDTO the xaracterEquippedArmorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new xaracterEquippedArmorDTO, or with status {@code 400 (Bad Request)} if the xaracterEquippedArmor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/xaracter-equipped-armors")
    public Mono<ResponseEntity<XaracterEquippedArmorDTO>> createXaracterEquippedArmor(
        @RequestBody XaracterEquippedArmorDTO xaracterEquippedArmorDTO
    ) throws URISyntaxException {
        log.debug("REST request to save XaracterEquippedArmor : {}", xaracterEquippedArmorDTO);
        if (xaracterEquippedArmorDTO.getId() != null) {
            throw new BadRequestAlertException("A new xaracterEquippedArmor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return xaracterEquippedArmorService
            .save(xaracterEquippedArmorDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/xaracter-equipped-armors/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /xaracter-equipped-armors/:id} : Updates an existing xaracterEquippedArmor.
     *
     * @param id the id of the xaracterEquippedArmorDTO to save.
     * @param xaracterEquippedArmorDTO the xaracterEquippedArmorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xaracterEquippedArmorDTO,
     * or with status {@code 400 (Bad Request)} if the xaracterEquippedArmorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the xaracterEquippedArmorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/xaracter-equipped-armors/{id}")
    public Mono<ResponseEntity<XaracterEquippedArmorDTO>> updateXaracterEquippedArmor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody XaracterEquippedArmorDTO xaracterEquippedArmorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update XaracterEquippedArmor : {}, {}", id, xaracterEquippedArmorDTO);
        if (xaracterEquippedArmorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xaracterEquippedArmorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return xaracterEquippedArmorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return xaracterEquippedArmorService
                    .save(xaracterEquippedArmorDTO)
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
     * {@code PATCH  /xaracter-equipped-armors/:id} : Partial updates given fields of an existing xaracterEquippedArmor, field will ignore if it is null
     *
     * @param id the id of the xaracterEquippedArmorDTO to save.
     * @param xaracterEquippedArmorDTO the xaracterEquippedArmorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xaracterEquippedArmorDTO,
     * or with status {@code 400 (Bad Request)} if the xaracterEquippedArmorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the xaracterEquippedArmorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the xaracterEquippedArmorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/xaracter-equipped-armors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<XaracterEquippedArmorDTO>> partialUpdateXaracterEquippedArmor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody XaracterEquippedArmorDTO xaracterEquippedArmorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update XaracterEquippedArmor partially : {}, {}", id, xaracterEquippedArmorDTO);
        if (xaracterEquippedArmorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xaracterEquippedArmorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return xaracterEquippedArmorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<XaracterEquippedArmorDTO> result = xaracterEquippedArmorService.partialUpdate(xaracterEquippedArmorDTO);

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
     * {@code GET  /xaracter-equipped-armors} : get all the xaracterEquippedArmors.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of xaracterEquippedArmors in body.
     */
    @GetMapping("/xaracter-equipped-armors")
    public Mono<ResponseEntity<List<XaracterEquippedArmorDTO>>> getAllXaracterEquippedArmors(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of XaracterEquippedArmors");
        return xaracterEquippedArmorService
            .countAll()
            .zipWith(xaracterEquippedArmorService.findAll(pageable).collectList())
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
     * {@code GET  /xaracter-equipped-armors/:id} : get the "id" xaracterEquippedArmor.
     *
     * @param id the id of the xaracterEquippedArmorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the xaracterEquippedArmorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/xaracter-equipped-armors/{id}")
    public Mono<ResponseEntity<XaracterEquippedArmorDTO>> getXaracterEquippedArmor(@PathVariable Long id) {
        log.debug("REST request to get XaracterEquippedArmor : {}", id);
        Mono<XaracterEquippedArmorDTO> xaracterEquippedArmorDTO = xaracterEquippedArmorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(xaracterEquippedArmorDTO);
    }

    /**
     * {@code DELETE  /xaracter-equipped-armors/:id} : delete the "id" xaracterEquippedArmor.
     *
     * @param id the id of the xaracterEquippedArmorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/xaracter-equipped-armors/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteXaracterEquippedArmor(@PathVariable Long id) {
        log.debug("REST request to delete XaracterEquippedArmor : {}", id);
        return xaracterEquippedArmorService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
