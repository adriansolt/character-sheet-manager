package com.adi.cms.xaracter.web.rest;

import com.adi.cms.xaracter.repository.XaracterSkillRepository;
import com.adi.cms.xaracter.service.XaracterSkillService;
import com.adi.cms.xaracter.service.dto.XaracterSkillDTO;
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
 * REST controller for managing {@link com.adi.cms.xaracter.domain.XaracterSkill}.
 */
@RestController
@RequestMapping("/api")
public class XaracterSkillResource {

    private final Logger log = LoggerFactory.getLogger(XaracterSkillResource.class);

    private static final String ENTITY_NAME = "xaracterXaracterSkill";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final XaracterSkillService xaracterSkillService;

    private final XaracterSkillRepository xaracterSkillRepository;

    public XaracterSkillResource(XaracterSkillService xaracterSkillService, XaracterSkillRepository xaracterSkillRepository) {
        this.xaracterSkillService = xaracterSkillService;
        this.xaracterSkillRepository = xaracterSkillRepository;
    }

    /**
     * {@code POST  /xaracter-skills} : Create a new xaracterSkill.
     *
     * @param xaracterSkillDTO the xaracterSkillDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new xaracterSkillDTO, or with status {@code 400 (Bad Request)} if the xaracterSkill has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/xaracter-skills")
    public ResponseEntity<XaracterSkillDTO> createXaracterSkill(@Valid @RequestBody XaracterSkillDTO xaracterSkillDTO)
        throws URISyntaxException {
        log.debug("REST request to save XaracterSkill : {}", xaracterSkillDTO);
        if (xaracterSkillDTO.getId() != null) {
            throw new BadRequestAlertException("A new xaracterSkill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        XaracterSkillDTO result = xaracterSkillService.save(xaracterSkillDTO);
        return ResponseEntity
            .created(new URI("/api/xaracter-skills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /xaracter-skills/:id} : Updates an existing xaracterSkill.
     *
     * @param id the id of the xaracterSkillDTO to save.
     * @param xaracterSkillDTO the xaracterSkillDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xaracterSkillDTO,
     * or with status {@code 400 (Bad Request)} if the xaracterSkillDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the xaracterSkillDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/xaracter-skills/{id}")
    public ResponseEntity<XaracterSkillDTO> updateXaracterSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody XaracterSkillDTO xaracterSkillDTO
    ) throws URISyntaxException {
        log.debug("REST request to update XaracterSkill : {}, {}", id, xaracterSkillDTO);
        if (xaracterSkillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xaracterSkillDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!xaracterSkillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        XaracterSkillDTO result = xaracterSkillService.save(xaracterSkillDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, xaracterSkillDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /xaracter-skills/:id} : Partial updates given fields of an existing xaracterSkill, field will ignore if it is null
     *
     * @param id the id of the xaracterSkillDTO to save.
     * @param xaracterSkillDTO the xaracterSkillDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xaracterSkillDTO,
     * or with status {@code 400 (Bad Request)} if the xaracterSkillDTO is not valid,
     * or with status {@code 404 (Not Found)} if the xaracterSkillDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the xaracterSkillDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/xaracter-skills/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<XaracterSkillDTO> partialUpdateXaracterSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody XaracterSkillDTO xaracterSkillDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update XaracterSkill partially : {}, {}", id, xaracterSkillDTO);
        if (xaracterSkillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xaracterSkillDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!xaracterSkillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<XaracterSkillDTO> result = xaracterSkillService.partialUpdate(xaracterSkillDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, xaracterSkillDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /xaracter-skills} : get all the xaracterSkills.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of xaracterSkills in body.
     */
    @GetMapping("/xaracter-skills")
    public ResponseEntity<List<XaracterSkillDTO>> getAllXaracterSkills(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of XaracterSkills");
        Page<XaracterSkillDTO> page = xaracterSkillService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /xaracter-skills/:id} : get the "id" xaracterSkill.
     *
     * @param id the id of the xaracterSkillDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the xaracterSkillDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/xaracter-skills/{id}")
    public ResponseEntity<XaracterSkillDTO> getXaracterSkill(@PathVariable Long id) {
        log.debug("REST request to get XaracterSkill : {}", id);
        Optional<XaracterSkillDTO> xaracterSkillDTO = xaracterSkillService.findOne(id);
        return ResponseUtil.wrapOrNotFound(xaracterSkillDTO);
    }

    /**
     * {@code DELETE  /xaracter-skills/:id} : delete the "id" xaracterSkill.
     *
     * @param id the id of the xaracterSkillDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/xaracter-skills/{id}")
    public ResponseEntity<Void> deleteXaracterSkill(@PathVariable Long id) {
        log.debug("REST request to delete XaracterSkill : {}", id);
        xaracterSkillService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
