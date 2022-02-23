package com.adi.cms.gateway.web.rest;

import com.adi.cms.gateway.repository.CharacterEquippedArmorRepository;
import com.adi.cms.gateway.service.CharacterEquippedArmorService;
import com.adi.cms.gateway.service.dto.CharacterEquippedArmorDTO;
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
 * REST controller for managing {@link com.adi.cms.gateway.domain.CharacterEquippedArmor}.
 */
@RestController
@RequestMapping("/api")
public class CharacterEquippedArmorResource {

    private final Logger log = LoggerFactory.getLogger(CharacterEquippedArmorResource.class);

    private static final String ENTITY_NAME = "characterEquippedArmor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CharacterEquippedArmorService characterEquippedArmorService;

    private final CharacterEquippedArmorRepository characterEquippedArmorRepository;

    public CharacterEquippedArmorResource(
        CharacterEquippedArmorService characterEquippedArmorService,
        CharacterEquippedArmorRepository characterEquippedArmorRepository
    ) {
        this.characterEquippedArmorService = characterEquippedArmorService;
        this.characterEquippedArmorRepository = characterEquippedArmorRepository;
    }

    /**
     * {@code POST  /character-equipped-armors} : Create a new characterEquippedArmor.
     *
     * @param characterEquippedArmorDTO the characterEquippedArmorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new characterEquippedArmorDTO, or with status {@code 400 (Bad Request)} if the characterEquippedArmor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/character-equipped-armors")
    public Mono<ResponseEntity<CharacterEquippedArmorDTO>> createCharacterEquippedArmor(
        @RequestBody CharacterEquippedArmorDTO characterEquippedArmorDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CharacterEquippedArmor : {}", characterEquippedArmorDTO);
        if (characterEquippedArmorDTO.getId() != null) {
            throw new BadRequestAlertException("A new characterEquippedArmor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return characterEquippedArmorService
            .save(characterEquippedArmorDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/character-equipped-armors/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /character-equipped-armors/:id} : Updates an existing characterEquippedArmor.
     *
     * @param id the id of the characterEquippedArmorDTO to save.
     * @param characterEquippedArmorDTO the characterEquippedArmorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated characterEquippedArmorDTO,
     * or with status {@code 400 (Bad Request)} if the characterEquippedArmorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the characterEquippedArmorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/character-equipped-armors/{id}")
    public Mono<ResponseEntity<CharacterEquippedArmorDTO>> updateCharacterEquippedArmor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CharacterEquippedArmorDTO characterEquippedArmorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CharacterEquippedArmor : {}, {}", id, characterEquippedArmorDTO);
        if (characterEquippedArmorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, characterEquippedArmorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return characterEquippedArmorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return characterEquippedArmorService
                    .save(characterEquippedArmorDTO)
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
     * {@code PATCH  /character-equipped-armors/:id} : Partial updates given fields of an existing characterEquippedArmor, field will ignore if it is null
     *
     * @param id the id of the characterEquippedArmorDTO to save.
     * @param characterEquippedArmorDTO the characterEquippedArmorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated characterEquippedArmorDTO,
     * or with status {@code 400 (Bad Request)} if the characterEquippedArmorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the characterEquippedArmorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the characterEquippedArmorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/character-equipped-armors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CharacterEquippedArmorDTO>> partialUpdateCharacterEquippedArmor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CharacterEquippedArmorDTO characterEquippedArmorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CharacterEquippedArmor partially : {}, {}", id, characterEquippedArmorDTO);
        if (characterEquippedArmorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, characterEquippedArmorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return characterEquippedArmorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CharacterEquippedArmorDTO> result = characterEquippedArmorService.partialUpdate(characterEquippedArmorDTO);

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
     * {@code GET  /character-equipped-armors} : get all the characterEquippedArmors.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of characterEquippedArmors in body.
     */
    @GetMapping("/character-equipped-armors")
    public Mono<ResponseEntity<List<CharacterEquippedArmorDTO>>> getAllCharacterEquippedArmors(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of CharacterEquippedArmors");
        return characterEquippedArmorService
            .countAll()
            .zipWith(characterEquippedArmorService.findAll(pageable).collectList())
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
     * {@code GET  /character-equipped-armors/:id} : get the "id" characterEquippedArmor.
     *
     * @param id the id of the characterEquippedArmorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the characterEquippedArmorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/character-equipped-armors/{id}")
    public Mono<ResponseEntity<CharacterEquippedArmorDTO>> getCharacterEquippedArmor(@PathVariable Long id) {
        log.debug("REST request to get CharacterEquippedArmor : {}", id);
        Mono<CharacterEquippedArmorDTO> characterEquippedArmorDTO = characterEquippedArmorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(characterEquippedArmorDTO);
    }

    /**
     * {@code DELETE  /character-equipped-armors/:id} : delete the "id" characterEquippedArmor.
     *
     * @param id the id of the characterEquippedArmorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/character-equipped-armors/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCharacterEquippedArmor(@PathVariable Long id) {
        log.debug("REST request to delete CharacterEquippedArmor : {}", id);
        return characterEquippedArmorService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
