package com.adi.cms.xaracter.web.rest;

import com.adi.cms.xaracter.repository.XaracterRepository;
import com.adi.cms.xaracter.service.XaracterService;
import com.adi.cms.xaracter.service.dto.XaracterDTO;
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
 * REST controller for managing {@link com.adi.cms.xaracter.domain.Xaracter}.
 */
@RestController
@RequestMapping("/api")
public class XaracterResource {

    private final Logger log = LoggerFactory.getLogger(XaracterResource.class);

    private static final String ENTITY_NAME = "xaracterXaracter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final XaracterService xaracterService;

    private final XaracterRepository xaracterRepository;

    public XaracterResource(XaracterService xaracterService, XaracterRepository xaracterRepository) {
        this.xaracterService = xaracterService;
        this.xaracterRepository = xaracterRepository;
    }

    /**
     * {@code POST  /xaracters} : Create a new xaracter.
     *
     * @param xaracterDTO the xaracterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new xaracterDTO, or with status {@code 400 (Bad Request)} if the xaracter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/xaracters")
    public ResponseEntity<XaracterDTO> createXaracter(@Valid @RequestBody XaracterDTO xaracterDTO) throws URISyntaxException {
        log.debug("REST request to save Xaracter : {}", xaracterDTO);
        if (xaracterDTO.getId() != null) {
            throw new BadRequestAlertException("A new xaracter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        XaracterDTO result = xaracterService.save(xaracterDTO);
        return ResponseEntity
            .created(new URI("/api/xaracters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /xaracters/:id} : Updates an existing xaracter.
     *
     * @param id the id of the xaracterDTO to save.
     * @param xaracterDTO the xaracterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xaracterDTO,
     * or with status {@code 400 (Bad Request)} if the xaracterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the xaracterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/xaracters/{id}")
    public ResponseEntity<XaracterDTO> updateXaracter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody XaracterDTO xaracterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Xaracter : {}, {}", id, xaracterDTO);
        if (xaracterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xaracterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!xaracterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        XaracterDTO result = xaracterService.save(xaracterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, xaracterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /xaracters/:id} : Partial updates given fields of an existing xaracter, field will ignore if it is null
     *
     * @param id the id of the xaracterDTO to save.
     * @param xaracterDTO the xaracterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xaracterDTO,
     * or with status {@code 400 (Bad Request)} if the xaracterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the xaracterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the xaracterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/xaracters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<XaracterDTO> partialUpdateXaracter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody XaracterDTO xaracterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Xaracter partially : {}, {}", id, xaracterDTO);
        if (xaracterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xaracterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!xaracterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<XaracterDTO> result = xaracterService.partialUpdate(xaracterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, xaracterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /xaracters} : get all the xaracters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of xaracters in body.
     */
    @GetMapping("/xaracters")
    public ResponseEntity<List<XaracterDTO>> getAllXaracters(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Xaracters");
        Page<XaracterDTO> page = xaracterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /xaracters/:id} : get the "id" xaracter.
     *
     * @param id the id of the xaracterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the xaracterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/xaracters/{id}")
    public ResponseEntity<XaracterDTO> getXaracter(@PathVariable Long id) {
        log.debug("REST request to get Xaracter : {}", id);
        Optional<XaracterDTO> xaracterDTO = xaracterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(xaracterDTO);
    }

    /**
     * {@code DELETE  /xaracters/:id} : delete the "id" xaracter.
     *
     * @param id the id of the xaracterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/xaracters/{id}")
    public ResponseEntity<Void> deleteXaracter(@PathVariable Long id) {
        log.debug("REST request to delete Xaracter : {}", id);
        xaracterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
