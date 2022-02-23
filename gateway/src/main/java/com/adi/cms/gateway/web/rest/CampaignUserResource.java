package com.adi.cms.gateway.web.rest;

import com.adi.cms.gateway.repository.CampaignUserRepository;
import com.adi.cms.gateway.service.CampaignUserService;
import com.adi.cms.gateway.service.dto.CampaignUserDTO;
import com.adi.cms.gateway.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.adi.cms.gateway.domain.CampaignUser}.
 */
@RestController
@RequestMapping("/api")
public class CampaignUserResource {

    private final Logger log = LoggerFactory.getLogger(CampaignUserResource.class);

    private static final String ENTITY_NAME = "campaignUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CampaignUserService campaignUserService;

    private final CampaignUserRepository campaignUserRepository;

    public CampaignUserResource(CampaignUserService campaignUserService, CampaignUserRepository campaignUserRepository) {
        this.campaignUserService = campaignUserService;
        this.campaignUserRepository = campaignUserRepository;
    }

    /**
     * {@code POST  /campaign-users} : Create a new campaignUser.
     *
     * @param campaignUserDTO the campaignUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new campaignUserDTO, or with status {@code 400 (Bad Request)} if the campaignUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/campaign-users")
    public Mono<ResponseEntity<CampaignUserDTO>> createCampaignUser(@Valid @RequestBody CampaignUserDTO campaignUserDTO)
        throws URISyntaxException {
        log.debug("REST request to save CampaignUser : {}", campaignUserDTO);
        if (campaignUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new campaignUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return campaignUserService
            .save(campaignUserDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/campaign-users/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /campaign-users/:id} : Updates an existing campaignUser.
     *
     * @param id the id of the campaignUserDTO to save.
     * @param campaignUserDTO the campaignUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated campaignUserDTO,
     * or with status {@code 400 (Bad Request)} if the campaignUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the campaignUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/campaign-users/{id}")
    public Mono<ResponseEntity<CampaignUserDTO>> updateCampaignUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CampaignUserDTO campaignUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CampaignUser : {}, {}", id, campaignUserDTO);
        if (campaignUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, campaignUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return campaignUserRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return campaignUserService
                    .save(campaignUserDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /campaign-users/:id} : Partial updates given fields of an existing campaignUser, field will ignore if it is null
     *
     * @param id the id of the campaignUserDTO to save.
     * @param campaignUserDTO the campaignUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated campaignUserDTO,
     * or with status {@code 400 (Bad Request)} if the campaignUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the campaignUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the campaignUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/campaign-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CampaignUserDTO>> partialUpdateCampaignUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CampaignUserDTO campaignUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CampaignUser partially : {}, {}", id, campaignUserDTO);
        if (campaignUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, campaignUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return campaignUserRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CampaignUserDTO> result = campaignUserService.partialUpdate(campaignUserDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /campaign-users} : get all the campaignUsers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of campaignUsers in body.
     */
    @GetMapping("/campaign-users")
    public Mono<ResponseEntity<List<CampaignUserDTO>>> getAllCampaignUsers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of CampaignUsers");
        return campaignUserService
            .countAll()
            .zipWith(campaignUserService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /campaign-users/:id} : get the "id" campaignUser.
     *
     * @param id the id of the campaignUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the campaignUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/campaign-users/{id}")
    public Mono<ResponseEntity<CampaignUserDTO>> getCampaignUser(@PathVariable Long id) {
        log.debug("REST request to get CampaignUser : {}", id);
        Mono<CampaignUserDTO> campaignUserDTO = campaignUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(campaignUserDTO);
    }

    /**
     * {@code DELETE  /campaign-users/:id} : delete the "id" campaignUser.
     *
     * @param id the id of the campaignUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/campaign-users/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCampaignUser(@PathVariable Long id) {
        log.debug("REST request to delete CampaignUser : {}", id);
        return campaignUserService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
