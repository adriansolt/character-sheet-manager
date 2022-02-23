package com.adi.cms.item.web.rest;

import com.adi.cms.item.repository.WeaponRepository;
import com.adi.cms.item.service.WeaponService;
import com.adi.cms.item.service.dto.WeaponDTO;
import com.adi.cms.item.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.adi.cms.item.domain.Weapon}.
 */
@RestController
@RequestMapping("/api")
public class WeaponResource {

    private final Logger log = LoggerFactory.getLogger(WeaponResource.class);

    private static final String ENTITY_NAME = "itemWeapon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeaponService weaponService;

    private final WeaponRepository weaponRepository;

    public WeaponResource(WeaponService weaponService, WeaponRepository weaponRepository) {
        this.weaponService = weaponService;
        this.weaponRepository = weaponRepository;
    }

    /**
     * {@code POST  /weapons} : Create a new weapon.
     *
     * @param weaponDTO the weaponDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weaponDTO, or with status {@code 400 (Bad Request)} if the weapon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/weapons")
    public ResponseEntity<WeaponDTO> createWeapon(@Valid @RequestBody WeaponDTO weaponDTO) throws URISyntaxException {
        log.debug("REST request to save Weapon : {}", weaponDTO);
        if (weaponDTO.getId() != null) {
            throw new BadRequestAlertException("A new weapon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WeaponDTO result = weaponService.save(weaponDTO);
        return ResponseEntity
            .created(new URI("/api/weapons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /weapons/:id} : Updates an existing weapon.
     *
     * @param id the id of the weaponDTO to save.
     * @param weaponDTO the weaponDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weaponDTO,
     * or with status {@code 400 (Bad Request)} if the weaponDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the weaponDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/weapons/{id}")
    public ResponseEntity<WeaponDTO> updateWeapon(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WeaponDTO weaponDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Weapon : {}, {}", id, weaponDTO);
        if (weaponDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weaponDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weaponRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WeaponDTO result = weaponService.save(weaponDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weaponDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /weapons/:id} : Partial updates given fields of an existing weapon, field will ignore if it is null
     *
     * @param id the id of the weaponDTO to save.
     * @param weaponDTO the weaponDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weaponDTO,
     * or with status {@code 400 (Bad Request)} if the weaponDTO is not valid,
     * or with status {@code 404 (Not Found)} if the weaponDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the weaponDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/weapons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WeaponDTO> partialUpdateWeapon(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WeaponDTO weaponDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Weapon partially : {}, {}", id, weaponDTO);
        if (weaponDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weaponDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weaponRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WeaponDTO> result = weaponService.partialUpdate(weaponDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weaponDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /weapons} : get all the weapons.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weapons in body.
     */
    @GetMapping("/weapons")
    public ResponseEntity<List<WeaponDTO>> getAllWeapons(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Weapons");
        Page<WeaponDTO> page = weaponService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /weapons/:id} : get the "id" weapon.
     *
     * @param id the id of the weaponDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weaponDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/weapons/{id}")
    public ResponseEntity<WeaponDTO> getWeapon(@PathVariable Long id) {
        log.debug("REST request to get Weapon : {}", id);
        Optional<WeaponDTO> weaponDTO = weaponService.findOne(id);
        return ResponseUtil.wrapOrNotFound(weaponDTO);
    }

    /**
     * {@code DELETE  /weapons/:id} : delete the "id" weapon.
     *
     * @param id the id of the weaponDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/weapons/{id}")
    public ResponseEntity<Void> deleteWeapon(@PathVariable Long id) {
        log.debug("REST request to delete Weapon : {}", id);
        weaponService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
