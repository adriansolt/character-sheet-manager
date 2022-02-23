package com.adi.cms.item.web.rest;

import com.adi.cms.item.repository.CharacterEquippedArmorRepository;
import com.adi.cms.item.service.CharacterEquippedArmorService;
import com.adi.cms.item.service.dto.CharacterEquippedArmorDTO;
import com.adi.cms.item.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.adi.cms.item.domain.CharacterEquippedArmor}.
 */
@RestController
@RequestMapping("/api")
public class CharacterEquippedArmorResource {

    private final Logger log = LoggerFactory.getLogger(CharacterEquippedArmorResource.class);

    private static final String ENTITY_NAME = "itemCharacterEquippedArmor";

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
    public ResponseEntity<CharacterEquippedArmorDTO> createCharacterEquippedArmor(
        @RequestBody CharacterEquippedArmorDTO characterEquippedArmorDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CharacterEquippedArmor : {}", characterEquippedArmorDTO);
        if (characterEquippedArmorDTO.getId() != null) {
            throw new BadRequestAlertException("A new characterEquippedArmor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CharacterEquippedArmorDTO result = characterEquippedArmorService.save(characterEquippedArmorDTO);
        return ResponseEntity
            .created(new URI("/api/character-equipped-armors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    public ResponseEntity<CharacterEquippedArmorDTO> updateCharacterEquippedArmor(
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

        if (!characterEquippedArmorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CharacterEquippedArmorDTO result = characterEquippedArmorService.save(characterEquippedArmorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, characterEquippedArmorDTO.getId().toString()))
            .body(result);
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
    public ResponseEntity<CharacterEquippedArmorDTO> partialUpdateCharacterEquippedArmor(
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

        if (!characterEquippedArmorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CharacterEquippedArmorDTO> result = characterEquippedArmorService.partialUpdate(characterEquippedArmorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, characterEquippedArmorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /character-equipped-armors} : get all the characterEquippedArmors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of characterEquippedArmors in body.
     */
    @GetMapping("/character-equipped-armors")
    public ResponseEntity<List<CharacterEquippedArmorDTO>> getAllCharacterEquippedArmors(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of CharacterEquippedArmors");
        Page<CharacterEquippedArmorDTO> page = characterEquippedArmorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /character-equipped-armors/:id} : get the "id" characterEquippedArmor.
     *
     * @param id the id of the characterEquippedArmorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the characterEquippedArmorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/character-equipped-armors/{id}")
    public ResponseEntity<CharacterEquippedArmorDTO> getCharacterEquippedArmor(@PathVariable Long id) {
        log.debug("REST request to get CharacterEquippedArmor : {}", id);
        Optional<CharacterEquippedArmorDTO> characterEquippedArmorDTO = characterEquippedArmorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(characterEquippedArmorDTO);
    }

    /**
     * {@code DELETE  /character-equipped-armors/:id} : delete the "id" characterEquippedArmor.
     *
     * @param id the id of the characterEquippedArmorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/character-equipped-armors/{id}")
    public ResponseEntity<Void> deleteCharacterEquippedArmor(@PathVariable Long id) {
        log.debug("REST request to delete CharacterEquippedArmor : {}", id);
        characterEquippedArmorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
