package com.adi.cms.gateway.web.rest;

import com.adi.cms.gateway.repository.CharacterSkillRepository;
import com.adi.cms.gateway.service.CharacterSkillService;
import com.adi.cms.gateway.service.dto.CharacterSkillDTO;
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
 * REST controller for managing {@link com.adi.cms.gateway.domain.CharacterSkill}.
 */
@RestController
@RequestMapping("/api")
public class CharacterSkillResource {

    private final Logger log = LoggerFactory.getLogger(CharacterSkillResource.class);

    private static final String ENTITY_NAME = "characterSkill";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CharacterSkillService characterSkillService;

    private final CharacterSkillRepository characterSkillRepository;

    public CharacterSkillResource(CharacterSkillService characterSkillService, CharacterSkillRepository characterSkillRepository) {
        this.characterSkillService = characterSkillService;
        this.characterSkillRepository = characterSkillRepository;
    }

    /**
     * {@code POST  /character-skills} : Create a new characterSkill.
     *
     * @param characterSkillDTO the characterSkillDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new characterSkillDTO, or with status {@code 400 (Bad Request)} if the characterSkill has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/character-skills")
    public Mono<ResponseEntity<CharacterSkillDTO>> createCharacterSkill(@Valid @RequestBody CharacterSkillDTO characterSkillDTO)
        throws URISyntaxException {
        log.debug("REST request to save CharacterSkill : {}", characterSkillDTO);
        if (characterSkillDTO.getId() != null) {
            throw new BadRequestAlertException("A new characterSkill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return characterSkillService
            .save(characterSkillDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/character-skills/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /character-skills/:id} : Updates an existing characterSkill.
     *
     * @param id the id of the characterSkillDTO to save.
     * @param characterSkillDTO the characterSkillDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated characterSkillDTO,
     * or with status {@code 400 (Bad Request)} if the characterSkillDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the characterSkillDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/character-skills/{id}")
    public Mono<ResponseEntity<CharacterSkillDTO>> updateCharacterSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CharacterSkillDTO characterSkillDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CharacterSkill : {}, {}", id, characterSkillDTO);
        if (characterSkillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, characterSkillDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return characterSkillRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return characterSkillService
                    .save(characterSkillDTO)
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
     * {@code PATCH  /character-skills/:id} : Partial updates given fields of an existing characterSkill, field will ignore if it is null
     *
     * @param id the id of the characterSkillDTO to save.
     * @param characterSkillDTO the characterSkillDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated characterSkillDTO,
     * or with status {@code 400 (Bad Request)} if the characterSkillDTO is not valid,
     * or with status {@code 404 (Not Found)} if the characterSkillDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the characterSkillDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/character-skills/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CharacterSkillDTO>> partialUpdateCharacterSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CharacterSkillDTO characterSkillDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CharacterSkill partially : {}, {}", id, characterSkillDTO);
        if (characterSkillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, characterSkillDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return characterSkillRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CharacterSkillDTO> result = characterSkillService.partialUpdate(characterSkillDTO);

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
     * {@code GET  /character-skills} : get all the characterSkills.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of characterSkills in body.
     */
    @GetMapping("/character-skills")
    public Mono<ResponseEntity<List<CharacterSkillDTO>>> getAllCharacterSkills(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of CharacterSkills");
        return characterSkillService
            .countAll()
            .zipWith(characterSkillService.findAll(pageable).collectList())
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
     * {@code GET  /character-skills/:id} : get the "id" characterSkill.
     *
     * @param id the id of the characterSkillDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the characterSkillDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/character-skills/{id}")
    public Mono<ResponseEntity<CharacterSkillDTO>> getCharacterSkill(@PathVariable Long id) {
        log.debug("REST request to get CharacterSkill : {}", id);
        Mono<CharacterSkillDTO> characterSkillDTO = characterSkillService.findOne(id);
        return ResponseUtil.wrapOrNotFound(characterSkillDTO);
    }

    /**
     * {@code DELETE  /character-skills/:id} : delete the "id" characterSkill.
     *
     * @param id the id of the characterSkillDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/character-skills/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCharacterSkill(@PathVariable Long id) {
        log.debug("REST request to delete CharacterSkill : {}", id);
        return characterSkillService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
