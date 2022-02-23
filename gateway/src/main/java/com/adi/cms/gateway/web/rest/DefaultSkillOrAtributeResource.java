package com.adi.cms.gateway.web.rest;

import com.adi.cms.gateway.repository.DefaultSkillOrAtributeRepository;
import com.adi.cms.gateway.service.DefaultSkillOrAtributeService;
import com.adi.cms.gateway.service.dto.DefaultSkillOrAtributeDTO;
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
 * REST controller for managing {@link com.adi.cms.gateway.domain.DefaultSkillOrAtribute}.
 */
@RestController
@RequestMapping("/api")
public class DefaultSkillOrAtributeResource {

    private final Logger log = LoggerFactory.getLogger(DefaultSkillOrAtributeResource.class);

    private static final String ENTITY_NAME = "defaultSkillOrAtribute";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DefaultSkillOrAtributeService defaultSkillOrAtributeService;

    private final DefaultSkillOrAtributeRepository defaultSkillOrAtributeRepository;

    public DefaultSkillOrAtributeResource(
        DefaultSkillOrAtributeService defaultSkillOrAtributeService,
        DefaultSkillOrAtributeRepository defaultSkillOrAtributeRepository
    ) {
        this.defaultSkillOrAtributeService = defaultSkillOrAtributeService;
        this.defaultSkillOrAtributeRepository = defaultSkillOrAtributeRepository;
    }

    /**
     * {@code POST  /default-skill-or-atributes} : Create a new defaultSkillOrAtribute.
     *
     * @param defaultSkillOrAtributeDTO the defaultSkillOrAtributeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new defaultSkillOrAtributeDTO, or with status {@code 400 (Bad Request)} if the defaultSkillOrAtribute has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/default-skill-or-atributes")
    public Mono<ResponseEntity<DefaultSkillOrAtributeDTO>> createDefaultSkillOrAtribute(
        @Valid @RequestBody DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save DefaultSkillOrAtribute : {}", defaultSkillOrAtributeDTO);
        if (defaultSkillOrAtributeDTO.getId() != null) {
            throw new BadRequestAlertException("A new defaultSkillOrAtribute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return defaultSkillOrAtributeService
            .save(defaultSkillOrAtributeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/default-skill-or-atributes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /default-skill-or-atributes/:id} : Updates an existing defaultSkillOrAtribute.
     *
     * @param id the id of the defaultSkillOrAtributeDTO to save.
     * @param defaultSkillOrAtributeDTO the defaultSkillOrAtributeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated defaultSkillOrAtributeDTO,
     * or with status {@code 400 (Bad Request)} if the defaultSkillOrAtributeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the defaultSkillOrAtributeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/default-skill-or-atributes/{id}")
    public Mono<ResponseEntity<DefaultSkillOrAtributeDTO>> updateDefaultSkillOrAtribute(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DefaultSkillOrAtribute : {}, {}", id, defaultSkillOrAtributeDTO);
        if (defaultSkillOrAtributeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, defaultSkillOrAtributeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return defaultSkillOrAtributeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return defaultSkillOrAtributeService
                    .save(defaultSkillOrAtributeDTO)
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
     * {@code PATCH  /default-skill-or-atributes/:id} : Partial updates given fields of an existing defaultSkillOrAtribute, field will ignore if it is null
     *
     * @param id the id of the defaultSkillOrAtributeDTO to save.
     * @param defaultSkillOrAtributeDTO the defaultSkillOrAtributeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated defaultSkillOrAtributeDTO,
     * or with status {@code 400 (Bad Request)} if the defaultSkillOrAtributeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the defaultSkillOrAtributeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the defaultSkillOrAtributeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/default-skill-or-atributes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DefaultSkillOrAtributeDTO>> partialUpdateDefaultSkillOrAtribute(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DefaultSkillOrAtribute partially : {}, {}", id, defaultSkillOrAtributeDTO);
        if (defaultSkillOrAtributeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, defaultSkillOrAtributeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return defaultSkillOrAtributeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DefaultSkillOrAtributeDTO> result = defaultSkillOrAtributeService.partialUpdate(defaultSkillOrAtributeDTO);

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
     * {@code GET  /default-skill-or-atributes} : get all the defaultSkillOrAtributes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of defaultSkillOrAtributes in body.
     */
    @GetMapping("/default-skill-or-atributes")
    public Mono<ResponseEntity<List<DefaultSkillOrAtributeDTO>>> getAllDefaultSkillOrAtributes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of DefaultSkillOrAtributes");
        return defaultSkillOrAtributeService
            .countAll()
            .zipWith(defaultSkillOrAtributeService.findAll(pageable).collectList())
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
     * {@code GET  /default-skill-or-atributes/:id} : get the "id" defaultSkillOrAtribute.
     *
     * @param id the id of the defaultSkillOrAtributeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the defaultSkillOrAtributeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/default-skill-or-atributes/{id}")
    public Mono<ResponseEntity<DefaultSkillOrAtributeDTO>> getDefaultSkillOrAtribute(@PathVariable Long id) {
        log.debug("REST request to get DefaultSkillOrAtribute : {}", id);
        Mono<DefaultSkillOrAtributeDTO> defaultSkillOrAtributeDTO = defaultSkillOrAtributeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(defaultSkillOrAtributeDTO);
    }

    /**
     * {@code DELETE  /default-skill-or-atributes/:id} : delete the "id" defaultSkillOrAtribute.
     *
     * @param id the id of the defaultSkillOrAtributeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/default-skill-or-atributes/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDefaultSkillOrAtribute(@PathVariable Long id) {
        log.debug("REST request to delete DefaultSkillOrAtribute : {}", id);
        return defaultSkillOrAtributeService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
