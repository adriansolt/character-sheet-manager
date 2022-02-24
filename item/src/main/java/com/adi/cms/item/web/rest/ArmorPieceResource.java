package com.adi.cms.item.web.rest;

import com.adi.cms.item.repository.ArmorPieceRepository;
import com.adi.cms.item.service.ArmorPieceService;
import com.adi.cms.item.service.dto.ArmorPieceDTO;
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
 * REST controller for managing {@link com.adi.cms.item.domain.ArmorPiece}.
 */
@RestController
@RequestMapping("/api")
public class ArmorPieceResource {

    private final Logger log = LoggerFactory.getLogger(ArmorPieceResource.class);

    private static final String ENTITY_NAME = "itemArmorPiece";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArmorPieceService armorPieceService;

    private final ArmorPieceRepository armorPieceRepository;

    public ArmorPieceResource(ArmorPieceService armorPieceService, ArmorPieceRepository armorPieceRepository) {
        this.armorPieceService = armorPieceService;
        this.armorPieceRepository = armorPieceRepository;
    }

    /**
     * {@code POST  /armor-pieces} : Create a new armorPiece.
     *
     * @param armorPieceDTO the armorPieceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new armorPieceDTO, or with status {@code 400 (Bad Request)} if the armorPiece has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/armor-pieces")
    public ResponseEntity<ArmorPieceDTO> createArmorPiece(@Valid @RequestBody ArmorPieceDTO armorPieceDTO) throws URISyntaxException {
        log.debug("REST request to save ArmorPiece : {}", armorPieceDTO);
        if (armorPieceDTO.getId() != null) {
            throw new BadRequestAlertException("A new armorPiece cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArmorPieceDTO result = armorPieceService.save(armorPieceDTO);
        return ResponseEntity
            .created(new URI("/api/armor-pieces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /armor-pieces/:id} : Updates an existing armorPiece.
     *
     * @param id the id of the armorPieceDTO to save.
     * @param armorPieceDTO the armorPieceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated armorPieceDTO,
     * or with status {@code 400 (Bad Request)} if the armorPieceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the armorPieceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/armor-pieces/{id}")
    public ResponseEntity<ArmorPieceDTO> updateArmorPiece(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArmorPieceDTO armorPieceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ArmorPiece : {}, {}", id, armorPieceDTO);
        if (armorPieceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, armorPieceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!armorPieceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ArmorPieceDTO result = armorPieceService.save(armorPieceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, armorPieceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /armor-pieces/:id} : Partial updates given fields of an existing armorPiece, field will ignore if it is null
     *
     * @param id the id of the armorPieceDTO to save.
     * @param armorPieceDTO the armorPieceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated armorPieceDTO,
     * or with status {@code 400 (Bad Request)} if the armorPieceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the armorPieceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the armorPieceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/armor-pieces/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArmorPieceDTO> partialUpdateArmorPiece(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArmorPieceDTO armorPieceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ArmorPiece partially : {}, {}", id, armorPieceDTO);
        if (armorPieceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, armorPieceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!armorPieceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArmorPieceDTO> result = armorPieceService.partialUpdate(armorPieceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, armorPieceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /armor-pieces} : get all the armorPieces.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of armorPieces in body.
     */
    @GetMapping("/armor-pieces")
    public ResponseEntity<List<ArmorPieceDTO>> getAllArmorPieces(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ArmorPieces");
        Page<ArmorPieceDTO> page = armorPieceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /armor-pieces/:id} : get the "id" armorPiece.
     *
     * @param id the id of the armorPieceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the armorPieceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/armor-pieces/{id}")
    public ResponseEntity<ArmorPieceDTO> getArmorPiece(@PathVariable Long id) {
        log.debug("REST request to get ArmorPiece : {}", id);
        Optional<ArmorPieceDTO> armorPieceDTO = armorPieceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(armorPieceDTO);
    }

    /**
     * {@code DELETE  /armor-pieces/:id} : delete the "id" armorPiece.
     *
     * @param id the id of the armorPieceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/armor-pieces/{id}")
    public ResponseEntity<Void> deleteArmorPiece(@PathVariable Long id) {
        log.debug("REST request to delete ArmorPiece : {}", id);
        armorPieceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
