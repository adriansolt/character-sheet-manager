package com.adi.cms.gateway.web.rest;

import com.adi.cms.gateway.repository.ArmorPieceRepository;
import com.adi.cms.gateway.service.ArmorPieceService;
import com.adi.cms.gateway.service.dto.ArmorPieceDTO;
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
 * REST controller for managing {@link com.adi.cms.gateway.domain.ArmorPiece}.
 */
@RestController
@RequestMapping("/api")
public class ArmorPieceResource {

    private final Logger log = LoggerFactory.getLogger(ArmorPieceResource.class);

    private static final String ENTITY_NAME = "armorPiece";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArmorPieceService armorPieceService;

    private final ArmorPieceRepository armorPieceRepository;

    public ArmorPieceResource(ArmorPieceService armorPieceService, ArmorPieceRepository armorPieceRepository) {
        this.armorPieceService = armorPieceService;
        this.armorPieceRepository = armorPieceRepository;
    }

    /**
     * {@code POST  /armor-pieces} : Create a new armorPiece.
     *
     * @param armorPieceDTO the armorPieceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new armorPieceDTO, or with status {@code 400 (Bad Request)} if the armorPiece has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/armor-pieces")
    public Mono<ResponseEntity<ArmorPieceDTO>> createArmorPiece(@RequestBody ArmorPieceDTO armorPieceDTO) throws URISyntaxException {
        log.debug("REST request to save ArmorPiece : {}", armorPieceDTO);
        if (armorPieceDTO.getId() != null) {
            throw new BadRequestAlertException("A new armorPiece cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return armorPieceService
            .save(armorPieceDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/armor-pieces/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /armor-pieces/:id} : Updates an existing armorPiece.
     *
     * @param id the id of the armorPieceDTO to save.
     * @param armorPieceDTO the armorPieceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated armorPieceDTO,
     * or with status {@code 400 (Bad Request)} if the armorPieceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the armorPieceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/armor-pieces/{id}")
    public Mono<ResponseEntity<ArmorPieceDTO>> updateArmorPiece(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ArmorPieceDTO armorPieceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ArmorPiece : {}, {}", id, armorPieceDTO);
        if (armorPieceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, armorPieceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return armorPieceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return armorPieceService
                    .save(armorPieceDTO)
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
     * {@code PATCH  /armor-pieces/:id} : Partial updates given fields of an existing armorPiece, field will ignore if it is null
     *
     * @param id the id of the armorPieceDTO to save.
     * @param armorPieceDTO the armorPieceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated armorPieceDTO,
     * or with status {@code 400 (Bad Request)} if the armorPieceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the armorPieceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the armorPieceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/armor-pieces/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ArmorPieceDTO>> partialUpdateArmorPiece(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ArmorPieceDTO armorPieceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ArmorPiece partially : {}, {}", id, armorPieceDTO);
        if (armorPieceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, armorPieceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return armorPieceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ArmorPieceDTO> result = armorPieceService.partialUpdate(armorPieceDTO);

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
     * {@code GET  /armor-pieces} : get all the armorPieces.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of armorPieces in body.
     */
    @GetMapping("/armor-pieces")
    public Mono<ResponseEntity<List<ArmorPieceDTO>>> getAllArmorPieces(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of ArmorPieces");
        return armorPieceService
            .countAll()
            .zipWith(armorPieceService.findAll(pageable).collectList())
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
     * {@code GET  /armor-pieces/:id} : get the "id" armorPiece.
     *
     * @param id the id of the armorPieceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the armorPieceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/armor-pieces/{id}")
    public Mono<ResponseEntity<ArmorPieceDTO>> getArmorPiece(@PathVariable Long id) {
        log.debug("REST request to get ArmorPiece : {}", id);
        Mono<ArmorPieceDTO> armorPieceDTO = armorPieceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(armorPieceDTO);
    }

    /**
     * {@code DELETE  /armor-pieces/:id} : delete the "id" armorPiece.
     *
     * @param id the id of the armorPieceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/armor-pieces/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteArmorPiece(@PathVariable Long id) {
        log.debug("REST request to delete ArmorPiece : {}", id);
        return armorPieceService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
