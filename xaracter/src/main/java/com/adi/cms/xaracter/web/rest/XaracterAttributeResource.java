package com.adi.cms.xaracter.web.rest;

import com.adi.cms.xaracter.repository.XaracterAttributeRepository;
import com.adi.cms.xaracter.service.XaracterAttributeService;
import com.adi.cms.xaracter.service.dto.XaracterAttributeDTO;
import com.adi.cms.xaracter.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.adi.cms.xaracter.domain.XaracterAttribute}.
 */
@RestController
@RequestMapping("/api")
public class XaracterAttributeResource {

    private final Logger log = LoggerFactory.getLogger(XaracterAttributeResource.class);

    private static final String ENTITY_NAME = "xaracterXaracterAttribute";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final XaracterAttributeService xaracterAttributeService;

    private final XaracterAttributeRepository xaracterAttributeRepository;

    public XaracterAttributeResource(
        XaracterAttributeService xaracterAttributeService,
        XaracterAttributeRepository xaracterAttributeRepository
    ) {
        this.xaracterAttributeService = xaracterAttributeService;
        this.xaracterAttributeRepository = xaracterAttributeRepository;
    }

    /**
     * {@code POST  /xaracter-attributes} : Create a new xaracterAttribute.
     *
     * @param xaracterAttributeDTO the xaracterAttributeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new xaracterAttributeDTO, or with status {@code 400 (Bad Request)} if the xaracterAttribute has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/xaracter-attributes")
    public ResponseEntity<XaracterAttributeDTO> createXaracterAttribute(@Valid @RequestBody XaracterAttributeDTO xaracterAttributeDTO)
        throws URISyntaxException {
        log.debug("REST request to save XaracterAttribute : {}", xaracterAttributeDTO);
        if (xaracterAttributeDTO.getId() != null) {
            throw new BadRequestAlertException("A new xaracterAttribute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        XaracterAttributeDTO result = xaracterAttributeService.save(xaracterAttributeDTO);
        return ResponseEntity
            .created(new URI("/api/xaracter-attributes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /xaracter-attributes/:id} : Updates an existing xaracterAttribute.
     *
     * @param id the id of the xaracterAttributeDTO to save.
     * @param xaracterAttributeDTO the xaracterAttributeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xaracterAttributeDTO,
     * or with status {@code 400 (Bad Request)} if the xaracterAttributeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the xaracterAttributeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/xaracter-attributes/{id}")
    public ResponseEntity<XaracterAttributeDTO> updateXaracterAttribute(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody XaracterAttributeDTO xaracterAttributeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update XaracterAttribute : {}, {}", id, xaracterAttributeDTO);
        if (xaracterAttributeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xaracterAttributeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!xaracterAttributeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        XaracterAttributeDTO result = xaracterAttributeService.save(xaracterAttributeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, xaracterAttributeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /xaracter-attributes/:id} : Partial updates given fields of an existing xaracterAttribute, field will ignore if it is null
     *
     * @param id the id of the xaracterAttributeDTO to save.
     * @param xaracterAttributeDTO the xaracterAttributeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xaracterAttributeDTO,
     * or with status {@code 400 (Bad Request)} if the xaracterAttributeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the xaracterAttributeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the xaracterAttributeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/xaracter-attributes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<XaracterAttributeDTO> partialUpdateXaracterAttribute(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody XaracterAttributeDTO xaracterAttributeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update XaracterAttribute partially : {}, {}", id, xaracterAttributeDTO);
        if (xaracterAttributeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xaracterAttributeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!xaracterAttributeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<XaracterAttributeDTO> result = xaracterAttributeService.partialUpdate(xaracterAttributeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, xaracterAttributeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /xaracter-attributes} : get all the xaracterAttributes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of xaracterAttributes in body.
     */
    @GetMapping("/xaracter-attributes")
    public ResponseEntity<List<XaracterAttributeDTO>> getAllXaracterAttributes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of XaracterAttributes");
        Page<XaracterAttributeDTO> page = xaracterAttributeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /xaracter-attributes/:id} : get the "id" xaracterAttribute.
     *
     * @param id the id of the xaracterAttributeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the xaracterAttributeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/xaracter-attributes/{id}")
    public ResponseEntity<XaracterAttributeDTO> getXaracterAttribute(@PathVariable Long id) {
        log.debug("REST request to get XaracterAttribute : {}", id);
        Optional<XaracterAttributeDTO> xaracterAttributeDTO = xaracterAttributeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(xaracterAttributeDTO);
    }

    /**
     * {@code DELETE  /xaracter-attributes/:id} : delete the "id" xaracterAttribute.
     *
     * @param id the id of the xaracterAttributeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/xaracter-attributes/{id}")
    public ResponseEntity<Void> deleteXaracterAttribute(@PathVariable Long id) {
        log.debug("REST request to delete XaracterAttribute : {}", id);
        xaracterAttributeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
