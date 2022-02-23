package com.adi.cms.character.web.rest;

import com.adi.cms.character.repository.PrereqSkillOrAtributeRepository;
import com.adi.cms.character.service.PrereqSkillOrAtributeService;
import com.adi.cms.character.service.dto.PrereqSkillOrAtributeDTO;
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
 * REST controller for managing {@link com.adi.cms.character.domain.PrereqSkillOrAtribute}.
 */
@RestController
@RequestMapping("/api")
public class PrereqSkillOrAtributeResource {

    private final Logger log = LoggerFactory.getLogger(PrereqSkillOrAtributeResource.class);

    private static final String ENTITY_NAME = "characterPrereqSkillOrAtribute";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrereqSkillOrAtributeService prereqSkillOrAtributeService;

    private final PrereqSkillOrAtributeRepository prereqSkillOrAtributeRepository;

    public PrereqSkillOrAtributeResource(
        PrereqSkillOrAtributeService prereqSkillOrAtributeService,
        PrereqSkillOrAtributeRepository prereqSkillOrAtributeRepository
    ) {
        this.prereqSkillOrAtributeService = prereqSkillOrAtributeService;
        this.prereqSkillOrAtributeRepository = prereqSkillOrAtributeRepository;
    }

    /**
     * {@code POST  /prereq-skill-or-atributes} : Create a new prereqSkillOrAtribute.
     *
     * @param prereqSkillOrAtributeDTO the prereqSkillOrAtributeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prereqSkillOrAtributeDTO, or with status {@code 400 (Bad Request)} if the prereqSkillOrAtribute has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prereq-skill-or-atributes")
    public ResponseEntity<PrereqSkillOrAtributeDTO> createPrereqSkillOrAtribute(
        @Valid @RequestBody PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PrereqSkillOrAtribute : {}", prereqSkillOrAtributeDTO);
        if (prereqSkillOrAtributeDTO.getId() != null) {
            throw new BadRequestAlertException("A new prereqSkillOrAtribute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrereqSkillOrAtributeDTO result = prereqSkillOrAtributeService.save(prereqSkillOrAtributeDTO);
        return ResponseEntity
            .created(new URI("/api/prereq-skill-or-atributes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prereq-skill-or-atributes/:id} : Updates an existing prereqSkillOrAtribute.
     *
     * @param id the id of the prereqSkillOrAtributeDTO to save.
     * @param prereqSkillOrAtributeDTO the prereqSkillOrAtributeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prereqSkillOrAtributeDTO,
     * or with status {@code 400 (Bad Request)} if the prereqSkillOrAtributeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prereqSkillOrAtributeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prereq-skill-or-atributes/{id}")
    public ResponseEntity<PrereqSkillOrAtributeDTO> updatePrereqSkillOrAtribute(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PrereqSkillOrAtribute : {}, {}", id, prereqSkillOrAtributeDTO);
        if (prereqSkillOrAtributeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prereqSkillOrAtributeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prereqSkillOrAtributeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PrereqSkillOrAtributeDTO result = prereqSkillOrAtributeService.save(prereqSkillOrAtributeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prereqSkillOrAtributeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prereq-skill-or-atributes/:id} : Partial updates given fields of an existing prereqSkillOrAtribute, field will ignore if it is null
     *
     * @param id the id of the prereqSkillOrAtributeDTO to save.
     * @param prereqSkillOrAtributeDTO the prereqSkillOrAtributeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prereqSkillOrAtributeDTO,
     * or with status {@code 400 (Bad Request)} if the prereqSkillOrAtributeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prereqSkillOrAtributeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prereqSkillOrAtributeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prereq-skill-or-atributes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrereqSkillOrAtributeDTO> partialUpdatePrereqSkillOrAtribute(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PrereqSkillOrAtribute partially : {}, {}", id, prereqSkillOrAtributeDTO);
        if (prereqSkillOrAtributeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prereqSkillOrAtributeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prereqSkillOrAtributeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrereqSkillOrAtributeDTO> result = prereqSkillOrAtributeService.partialUpdate(prereqSkillOrAtributeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prereqSkillOrAtributeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prereq-skill-or-atributes} : get all the prereqSkillOrAtributes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prereqSkillOrAtributes in body.
     */
    @GetMapping("/prereq-skill-or-atributes")
    public ResponseEntity<List<PrereqSkillOrAtributeDTO>> getAllPrereqSkillOrAtributes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PrereqSkillOrAtributes");
        Page<PrereqSkillOrAtributeDTO> page = prereqSkillOrAtributeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prereq-skill-or-atributes/:id} : get the "id" prereqSkillOrAtribute.
     *
     * @param id the id of the prereqSkillOrAtributeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prereqSkillOrAtributeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prereq-skill-or-atributes/{id}")
    public ResponseEntity<PrereqSkillOrAtributeDTO> getPrereqSkillOrAtribute(@PathVariable Long id) {
        log.debug("REST request to get PrereqSkillOrAtribute : {}", id);
        Optional<PrereqSkillOrAtributeDTO> prereqSkillOrAtributeDTO = prereqSkillOrAtributeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prereqSkillOrAtributeDTO);
    }

    /**
     * {@code DELETE  /prereq-skill-or-atributes/:id} : delete the "id" prereqSkillOrAtribute.
     *
     * @param id the id of the prereqSkillOrAtributeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prereq-skill-or-atributes/{id}")
    public ResponseEntity<Void> deletePrereqSkillOrAtribute(@PathVariable Long id) {
        log.debug("REST request to delete PrereqSkillOrAtribute : {}", id);
        prereqSkillOrAtributeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
