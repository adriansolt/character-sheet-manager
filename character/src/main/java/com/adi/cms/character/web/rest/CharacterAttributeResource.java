package com.adi.cms.character.web.rest;

import com.adi.cms.character.repository.CharacterAttributeRepository;
import com.adi.cms.character.service.CharacterAttributeService;
import com.adi.cms.character.service.dto.CharacterAttributeDTO;
import com.adi.cms.character.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.adi.cms.character.domain.CharacterAttribute}.
 */
@RestController
@RequestMapping("/api")
public class CharacterAttributeResource {

    private final Logger log = LoggerFactory.getLogger(CharacterAttributeResource.class);

    private static final String ENTITY_NAME = "characterCharacterAttribute";

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
    public ResponseEntity<CharacterAttributeDTO> createCharacterAttribute(@Valid @RequestBody CharacterAttributeDTO characterAttributeDTO)
        throws URISyntaxException {
        log.debug("REST request to save CharacterAttribute : {}", characterAttributeDTO);
        if (characterAttributeDTO.getId() != null) {
            throw new BadRequestAlertException("A new characterAttribute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CharacterAttributeDTO result = characterAttributeService.save(characterAttributeDTO);
        return ResponseEntity
            .created(new URI("/api/character-attributes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    public ResponseEntity<CharacterAttributeDTO> updateCharacterAttribute(
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

        if (!characterAttributeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CharacterAttributeDTO result = characterAttributeService.save(characterAttributeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, characterAttributeDTO.getId().toString()))
            .body(result);
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
    public ResponseEntity<CharacterAttributeDTO> partialUpdateCharacterAttribute(
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

        if (!characterAttributeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CharacterAttributeDTO> result = characterAttributeService.partialUpdate(characterAttributeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, characterAttributeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /character-attributes} : get all the characterAttributes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of characterAttributes in body.
     */
    @GetMapping("/character-attributes")
    public ResponseEntity<List<CharacterAttributeDTO>> getAllCharacterAttributes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of CharacterAttributes");
        Page<CharacterAttributeDTO> page;
        if (eagerload) {
            page = characterAttributeService.findAllWithEagerRelationships(pageable);
        } else {
            page = characterAttributeService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /character-attributes/:id} : get the "id" characterAttribute.
     *
     * @param id the id of the characterAttributeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the characterAttributeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/character-attributes/{id}")
    public ResponseEntity<CharacterAttributeDTO> getCharacterAttribute(@PathVariable Long id) {
        log.debug("REST request to get CharacterAttribute : {}", id);
        Optional<CharacterAttributeDTO> characterAttributeDTO = characterAttributeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(characterAttributeDTO);
    }

    /**
     * {@code DELETE  /character-attributes/:id} : delete the "id" characterAttribute.
     *
     * @param id the id of the characterAttributeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/character-attributes/{id}")
    public ResponseEntity<Void> deleteCharacterAttribute(@PathVariable Long id) {
        log.debug("REST request to delete CharacterAttribute : {}", id);
        characterAttributeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
