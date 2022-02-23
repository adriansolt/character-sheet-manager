package com.adi.cms.item.web.rest;

import com.adi.cms.item.repository.CharacterEquippedWeaponRepository;
import com.adi.cms.item.service.CharacterEquippedWeaponService;
import com.adi.cms.item.service.dto.CharacterEquippedWeaponDTO;
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
 * REST controller for managing {@link com.adi.cms.item.domain.CharacterEquippedWeapon}.
 */
@RestController
@RequestMapping("/api")
public class CharacterEquippedWeaponResource {

    private final Logger log = LoggerFactory.getLogger(CharacterEquippedWeaponResource.class);

    private static final String ENTITY_NAME = "itemCharacterEquippedWeapon";

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
    public ResponseEntity<CharacterEquippedWeaponDTO> createCharacterEquippedWeapon(
        @RequestBody CharacterEquippedWeaponDTO characterEquippedWeaponDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CharacterEquippedWeapon : {}", characterEquippedWeaponDTO);
        if (characterEquippedWeaponDTO.getId() != null) {
            throw new BadRequestAlertException("A new characterEquippedWeapon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CharacterEquippedWeaponDTO result = characterEquippedWeaponService.save(characterEquippedWeaponDTO);
        return ResponseEntity
            .created(new URI("/api/character-equipped-weapons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    public ResponseEntity<CharacterEquippedWeaponDTO> updateCharacterEquippedWeapon(
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

        if (!characterEquippedWeaponRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CharacterEquippedWeaponDTO result = characterEquippedWeaponService.save(characterEquippedWeaponDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, characterEquippedWeaponDTO.getId().toString()))
            .body(result);
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
    public ResponseEntity<CharacterEquippedWeaponDTO> partialUpdateCharacterEquippedWeapon(
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

        if (!characterEquippedWeaponRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CharacterEquippedWeaponDTO> result = characterEquippedWeaponService.partialUpdate(characterEquippedWeaponDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, characterEquippedWeaponDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /character-equipped-weapons} : get all the characterEquippedWeapons.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of characterEquippedWeapons in body.
     */
    @GetMapping("/character-equipped-weapons")
    public ResponseEntity<List<CharacterEquippedWeaponDTO>> getAllCharacterEquippedWeapons(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of CharacterEquippedWeapons");
        Page<CharacterEquippedWeaponDTO> page = characterEquippedWeaponService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /character-equipped-weapons/:id} : get the "id" characterEquippedWeapon.
     *
     * @param id the id of the characterEquippedWeaponDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the characterEquippedWeaponDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/character-equipped-weapons/{id}")
    public ResponseEntity<CharacterEquippedWeaponDTO> getCharacterEquippedWeapon(@PathVariable Long id) {
        log.debug("REST request to get CharacterEquippedWeapon : {}", id);
        Optional<CharacterEquippedWeaponDTO> characterEquippedWeaponDTO = characterEquippedWeaponService.findOne(id);
        return ResponseUtil.wrapOrNotFound(characterEquippedWeaponDTO);
    }

    /**
     * {@code DELETE  /character-equipped-weapons/:id} : delete the "id" characterEquippedWeapon.
     *
     * @param id the id of the characterEquippedWeaponDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/character-equipped-weapons/{id}")
    public ResponseEntity<Void> deleteCharacterEquippedWeapon(@PathVariable Long id) {
        log.debug("REST request to delete CharacterEquippedWeapon : {}", id);
        characterEquippedWeaponService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
