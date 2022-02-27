package com.adi.cms.character.web.rest;

import com.adi.cms.character.repository.CharacterSkillRepository;
import com.adi.cms.character.service.CharacterSkillService;
import com.adi.cms.character.service.dto.CharacterSkillDTO;
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
 * REST controller for managing {@link com.adi.cms.character.domain.CharacterSkill}.
 */
@RestController
@RequestMapping("/api")
public class CharacterSkillResource {

    private final Logger log = LoggerFactory.getLogger(CharacterSkillResource.class);

    private static final String ENTITY_NAME = "characterCharacterSkill";

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
    public ResponseEntity<CharacterSkillDTO> createCharacterSkill(@Valid @RequestBody CharacterSkillDTO characterSkillDTO)
        throws URISyntaxException {
        log.debug("REST request to save CharacterSkill : {}", characterSkillDTO);
        if (characterSkillDTO.getId() != null) {
            throw new BadRequestAlertException("A new characterSkill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CharacterSkillDTO result = characterSkillService.save(characterSkillDTO);
        return ResponseEntity
            .created(new URI("/api/character-skills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    public ResponseEntity<CharacterSkillDTO> updateCharacterSkill(
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

        if (!characterSkillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CharacterSkillDTO result = characterSkillService.save(characterSkillDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, characterSkillDTO.getId().toString()))
            .body(result);
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
    public ResponseEntity<CharacterSkillDTO> partialUpdateCharacterSkill(
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

        if (!characterSkillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CharacterSkillDTO> result = characterSkillService.partialUpdate(characterSkillDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, characterSkillDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /character-skills} : get all the characterSkills.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of characterSkills in body.
     */
    @GetMapping("/character-skills")
    public ResponseEntity<List<CharacterSkillDTO>> getAllCharacterSkills(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of CharacterSkills");
        Page<CharacterSkillDTO> page;
        if (eagerload) {
            page = characterSkillService.findAllWithEagerRelationships(pageable);
        } else {
            page = characterSkillService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /character-skills/:id} : get the "id" characterSkill.
     *
     * @param id the id of the characterSkillDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the characterSkillDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/character-skills/{id}")
    public ResponseEntity<CharacterSkillDTO> getCharacterSkill(@PathVariable Long id) {
        log.debug("REST request to get CharacterSkill : {}", id);
        Optional<CharacterSkillDTO> characterSkillDTO = characterSkillService.findOne(id);
        return ResponseUtil.wrapOrNotFound(characterSkillDTO);
    }

    /**
     * {@code DELETE  /character-skills/:id} : delete the "id" characterSkill.
     *
     * @param id the id of the characterSkillDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/character-skills/{id}")
    public ResponseEntity<Void> deleteCharacterSkill(@PathVariable Long id) {
        log.debug("REST request to delete CharacterSkill : {}", id);
        characterSkillService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
