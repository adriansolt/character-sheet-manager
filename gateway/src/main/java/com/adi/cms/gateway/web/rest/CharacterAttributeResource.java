package com.adi.cms.gateway.web.rest;

import com.adi.cms.gateway.repository.CharacterAttributeRepository;
import com.adi.cms.gateway.service.CharacterAttributeService;
import com.adi.cms.gateway.service.dto.CharacterAttributeDTO;
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
 * REST controller for managing {@link com.adi.cms.gateway.domain.CharacterAttribute}.
 */
@RestController
@RequestMapping("/api")
public class CharacterAttributeResource {

    private final Logger log = LoggerFactory.getLogger(CharacterAttributeResource.class);

    private static final String ENTITY_NAME = "characterAttribute";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CharacterAttributeService characterAttributeService;

    private final CharacterAttributeRepository characterAttributeRepository;

    public CharacterAttributeResource(
        CharacterAttributeService characterAttributeService,
        CharacterAttributeRepository characterAttributeRepository
    ) {
        this.characterAttributeService = characterAttributeService;
        this.characterAttributeRepository = characterAttributeRepository;
    }

    /**
     * {@code POST  /character-attributes} : Create a new characterAttribute.
     *
     * @param characterAttributeDTO the characterAttributeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new characterAttributeDTO, or with status {@code 400 (Bad Request)} if the characterAttribute has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/character-attributes")
    public Mono<ResponseEntity<CharacterAttributeDTO>> createCharacterAttribute(
        @Valid @RequestBody CharacterAttributeDTO characterAttributeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CharacterAttribute : {}", characterAttributeDTO);
        if (characterAttributeDTO.getId() != null) {
            throw new BadRequestAlertException("A new characterAttribute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return characterAttributeService
            .save(characterAttributeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/character-attributes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /character-attributes/:id} : Updates an existing characterAttribute.
     *
     * @param id the id of the characterAttributeDTO to save.
     * @param characterAttributeDTO the characterAttributeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated characterAttributeDTO,
     * or with status {@code 400 (Bad Request)} if the characterAttributeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the characterAttributeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/character-attributes/{id}")
    public Mono<ResponseEntity<CharacterAttributeDTO>> updateCharacterAttribute(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CharacterAttributeDTO characterAttributeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CharacterAttribute : {}, {}", id, characterAttributeDTO);
        if (characterAttributeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, characterAttributeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return characterAttributeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return characterAttributeService
                    .save(characterAttributeDTO)
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
     * {@code PATCH  /character-attributes/:id} : Partial updates given fields of an existing characterAttribute, field will ignore if it is null
     *
     * @param id the id of the characterAttributeDTO to save.
     * @param characterAttributeDTO the characterAttributeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated characterAttributeDTO,
     * or with status {@code 400 (Bad Request)} if the characterAttributeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the characterAttributeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the characterAttributeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/character-attributes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CharacterAttributeDTO>> partialUpdateCharacterAttribute(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CharacterAttributeDTO characterAttributeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CharacterAttribute partially : {}, {}", id, characterAttributeDTO);
        if (characterAttributeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, characterAttributeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return characterAttributeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CharacterAttributeDTO> result = characterAttributeService.partialUpdate(characterAttributeDTO);

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
     * {@code GET  /character-attributes} : get all the characterAttributes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of characterAttributes in body.
     */
    @GetMapping("/character-attributes")
    public Mono<ResponseEntity<List<CharacterAttributeDTO>>> getAllCharacterAttributes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of CharacterAttributes");
        return characterAttributeService
            .countAll()
            .zipWith(characterAttributeService.findAll(pageable).collectList())
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
     * {@code GET  /character-attributes/:id} : get the "id" characterAttribute.
     *
     * @param id the id of the characterAttributeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the characterAttributeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/character-attributes/{id}")
    public Mono<ResponseEntity<CharacterAttributeDTO>> getCharacterAttribute(@PathVariable Long id) {
        log.debug("REST request to get CharacterAttribute : {}", id);
        Mono<CharacterAttributeDTO> characterAttributeDTO = characterAttributeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(characterAttributeDTO);
    }

    /**
     * {@code DELETE  /character-attributes/:id} : delete the "id" characterAttribute.
     *
     * @param id the id of the characterAttributeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/character-attributes/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCharacterAttribute(@PathVariable Long id) {
        log.debug("REST request to delete CharacterAttribute : {}", id);
        return characterAttributeService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
