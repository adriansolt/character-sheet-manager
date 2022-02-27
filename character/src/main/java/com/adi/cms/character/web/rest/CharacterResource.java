package com.adi.cms.character.web.rest;

import com.adi.cms.character.repository.CharacterRepository;
import com.adi.cms.character.service.CharacterService;
import com.adi.cms.character.service.dto.CharacterDTO;
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
 * REST controller for managing {@link com.adi.cms.character.domain.Character}.
 */
@RestController
@RequestMapping("/api")
public class CharacterResource {

    private final Logger log = LoggerFactory.getLogger(CharacterResource.class);

    private static final String ENTITY_NAME = "characterCharacter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CharacterService characterService;

    private final CharacterRepository characterRepository;

    public CharacterResource(CharacterService characterService, CharacterRepository characterRepository) {
        this.characterService = characterService;
        this.characterRepository = characterRepository;
    }

    /**
     * {@code POST  /characters} : Create a new character.
     *
     * @param characterDTO the characterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new characterDTO, or with status {@code 400 (Bad Request)} if the character has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/characters")
    public ResponseEntity<CharacterDTO> createCharacter(@Valid @RequestBody CharacterDTO characterDTO) throws URISyntaxException {
        log.debug("REST request to save Character : {}", characterDTO);
        if (characterDTO.getId() != null) {
            throw new BadRequestAlertException("A new character cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CharacterDTO result = characterService.save(characterDTO);
        return ResponseEntity
            .created(new URI("/api/characters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /characters/:id} : Updates an existing character.
     *
     * @param id the id of the characterDTO to save.
     * @param characterDTO the characterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated characterDTO,
     * or with status {@code 400 (Bad Request)} if the characterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the characterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/characters/{id}")
    public ResponseEntity<CharacterDTO> updateCharacter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CharacterDTO characterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Character : {}, {}", id, characterDTO);
        if (characterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, characterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!characterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CharacterDTO result = characterService.save(characterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, characterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /characters/:id} : Partial updates given fields of an existing character, field will ignore if it is null
     *
     * @param id the id of the characterDTO to save.
     * @param characterDTO the characterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated characterDTO,
     * or with status {@code 400 (Bad Request)} if the characterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the characterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the characterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/characters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CharacterDTO> partialUpdateCharacter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CharacterDTO characterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Character partially : {}, {}", id, characterDTO);
        if (characterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, characterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!characterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CharacterDTO> result = characterService.partialUpdate(characterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, characterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /characters} : get all the characters.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of characters in body.
     */
    @GetMapping("/characters")
    public ResponseEntity<List<CharacterDTO>> getAllCharacters(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Characters");
        Page<CharacterDTO> page;
        if (eagerload) {
            page = characterService.findAllWithEagerRelationships(pageable);
        } else {
            page = characterService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /characters/:id} : get the "id" character.
     *
     * @param id the id of the characterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the characterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/characters/{id}")
    public ResponseEntity<CharacterDTO> getCharacter(@PathVariable Long id) {
        log.debug("REST request to get Character : {}", id);
        Optional<CharacterDTO> characterDTO = characterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(characterDTO);
    }

    /**
     * {@code DELETE  /characters/:id} : delete the "id" character.
     *
     * @param id the id of the characterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/characters/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        log.debug("REST request to delete Character : {}", id);
        characterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
