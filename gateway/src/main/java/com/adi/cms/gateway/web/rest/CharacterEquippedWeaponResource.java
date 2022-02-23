package com.adi.cms.gateway.web.rest;

import com.adi.cms.gateway.repository.CharacterEquippedWeaponRepository;
import com.adi.cms.gateway.service.CharacterEquippedWeaponService;
import com.adi.cms.gateway.service.dto.CharacterEquippedWeaponDTO;
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
 * REST controller for managing {@link com.adi.cms.gateway.domain.CharacterEquippedWeapon}.
 */
@RestController
@RequestMapping("/api")
public class CharacterEquippedWeaponResource {

    private final Logger log = LoggerFactory.getLogger(CharacterEquippedWeaponResource.class);

    private static final String ENTITY_NAME = "characterEquippedWeapon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CharacterEquippedWeaponService characterEquippedWeaponService;

    private final CharacterEquippedWeaponRepository characterEquippedWeaponRepository;

    public CharacterEquippedWeaponResource(
        CharacterEquippedWeaponService characterEquippedWeaponService,
        CharacterEquippedWeaponRepository characterEquippedWeaponRepository
    ) {
        this.characterEquippedWeaponService = characterEquippedWeaponService;
        this.characterEquippedWeaponRepository = characterEquippedWeaponRepository;
    }

    /**
     * {@code POST  /character-equipped-weapons} : Create a new characterEquippedWeapon.
     *
     * @param characterEquippedWeaponDTO the characterEquippedWeaponDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new characterEquippedWeaponDTO, or with status {@code 400 (Bad Request)} if the characterEquippedWeapon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/character-equipped-weapons")
    public Mono<ResponseEntity<CharacterEquippedWeaponDTO>> createCharacterEquippedWeapon(
        @RequestBody CharacterEquippedWeaponDTO characterEquippedWeaponDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CharacterEquippedWeapon : {}", characterEquippedWeaponDTO);
        if (characterEquippedWeaponDTO.getId() != null) {
            throw new BadRequestAlertException("A new characterEquippedWeapon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return characterEquippedWeaponService
            .save(characterEquippedWeaponDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/character-equipped-weapons/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /character-equipped-weapons/:id} : Updates an existing characterEquippedWeapon.
     *
     * @param id the id of the characterEquippedWeaponDTO to save.
     * @param characterEquippedWeaponDTO the characterEquippedWeaponDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated characterEquippedWeaponDTO,
     * or with status {@code 400 (Bad Request)} if the characterEquippedWeaponDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the characterEquippedWeaponDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/character-equipped-weapons/{id}")
    public Mono<ResponseEntity<CharacterEquippedWeaponDTO>> updateCharacterEquippedWeapon(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CharacterEquippedWeaponDTO characterEquippedWeaponDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CharacterEquippedWeapon : {}, {}", id, characterEquippedWeaponDTO);
        if (characterEquippedWeaponDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, characterEquippedWeaponDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return characterEquippedWeaponRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return characterEquippedWeaponService
                    .save(characterEquippedWeaponDTO)
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
     * {@code PATCH  /character-equipped-weapons/:id} : Partial updates given fields of an existing characterEquippedWeapon, field will ignore if it is null
     *
     * @param id the id of the characterEquippedWeaponDTO to save.
     * @param characterEquippedWeaponDTO the characterEquippedWeaponDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated characterEquippedWeaponDTO,
     * or with status {@code 400 (Bad Request)} if the characterEquippedWeaponDTO is not valid,
     * or with status {@code 404 (Not Found)} if the characterEquippedWeaponDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the characterEquippedWeaponDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/character-equipped-weapons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CharacterEquippedWeaponDTO>> partialUpdateCharacterEquippedWeapon(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CharacterEquippedWeaponDTO characterEquippedWeaponDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CharacterEquippedWeapon partially : {}, {}", id, characterEquippedWeaponDTO);
        if (characterEquippedWeaponDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, characterEquippedWeaponDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return characterEquippedWeaponRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CharacterEquippedWeaponDTO> result = characterEquippedWeaponService.partialUpdate(characterEquippedWeaponDTO);

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
     * {@code GET  /character-equipped-weapons} : get all the characterEquippedWeapons.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of characterEquippedWeapons in body.
     */
    @GetMapping("/character-equipped-weapons")
    public Mono<ResponseEntity<List<CharacterEquippedWeaponDTO>>> getAllCharacterEquippedWeapons(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of CharacterEquippedWeapons");
        return characterEquippedWeaponService
            .countAll()
            .zipWith(characterEquippedWeaponService.findAll(pageable).collectList())
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
     * {@code GET  /character-equipped-weapons/:id} : get the "id" characterEquippedWeapon.
     *
     * @param id the id of the characterEquippedWeaponDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the characterEquippedWeaponDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/character-equipped-weapons/{id}")
    public Mono<ResponseEntity<CharacterEquippedWeaponDTO>> getCharacterEquippedWeapon(@PathVariable Long id) {
        log.debug("REST request to get CharacterEquippedWeapon : {}", id);
        Mono<CharacterEquippedWeaponDTO> characterEquippedWeaponDTO = characterEquippedWeaponService.findOne(id);
        return ResponseUtil.wrapOrNotFound(characterEquippedWeaponDTO);
    }

    /**
     * {@code DELETE  /character-equipped-weapons/:id} : delete the "id" characterEquippedWeapon.
     *
     * @param id the id of the characterEquippedWeaponDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/character-equipped-weapons/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCharacterEquippedWeapon(@PathVariable Long id) {
        log.debug("REST request to delete CharacterEquippedWeapon : {}", id);
        return characterEquippedWeaponService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
