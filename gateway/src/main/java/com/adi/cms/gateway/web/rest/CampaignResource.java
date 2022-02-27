package com.adi.cms.gateway.web.rest;

import com.adi.cms.gateway.repository.CampaignRepository;
import com.adi.cms.gateway.service.CampaignService;
import com.adi.cms.gateway.service.dto.CampaignDTO;
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
 * REST controller for managing {@link com.adi.cms.gateway.domain.Campaign}.
 */
@RestController
@RequestMapping("/api")
public class CampaignResource {

    private final Logger log = LoggerFactory.getLogger(CampaignResource.class);

    private static final String ENTITY_NAME = "campaign";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CampaignService campaignService;

    private final CampaignRepository campaignRepository;

    public CampaignResource(CampaignService campaignService, CampaignRepository campaignRepository) {
        this.campaignService = campaignService;
        this.campaignRepository = campaignRepository;
    }

    /**
     * {@code POST  /campaigns} : Create a new campaign.
     *
     * @param campaignDTO the campaignDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new campaignDTO, or with status {@code 400 (Bad Request)} if the campaign has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/campaigns")
    public Mono<ResponseEntity<CampaignDTO>> createCampaign(@Valid @RequestBody CampaignDTO campaignDTO) throws URISyntaxException {
        log.debug("REST request to save Campaign : {}", campaignDTO);
        if (campaignDTO.getId() != null) {
            throw new BadRequestAlertException("A new campaign cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return campaignService
            .save(campaignDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/campaigns/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /campaigns/:id} : Updates an existing campaign.
     *
     * @param id the id of the campaignDTO to save.
     * @param campaignDTO the campaignDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated campaignDTO,
     * or with status {@code 400 (Bad Request)} if the campaignDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the campaignDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/campaigns/{id}")
    public Mono<ResponseEntity<CampaignDTO>> updateCampaign(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CampaignDTO campaignDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Campaign : {}, {}", id, campaignDTO);
        if (campaignDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, campaignDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return campaignRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return campaignService
                    .save(campaignDTO)
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
     * {@code PATCH  /campaigns/:id} : Partial updates given fields of an existing campaign, field will ignore if it is null
     *
     * @param id the id of the campaignDTO to save.
     * @param campaignDTO the campaignDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated campaignDTO,
     * or with status {@code 400 (Bad Request)} if the campaignDTO is not valid,
     * or with status {@code 404 (Not Found)} if the campaignDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the campaignDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/campaigns/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CampaignDTO>> partialUpdateCampaign(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CampaignDTO campaignDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Campaign partially : {}, {}", id, campaignDTO);
        if (campaignDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, campaignDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return campaignRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CampaignDTO> result = campaignService.partialUpdate(campaignDTO);

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
     * {@code GET  /campaigns} : get all the campaigns.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of campaigns in body.
     */
    @GetMapping("/campaigns")
    public Mono<ResponseEntity<List<CampaignDTO>>> getAllCampaigns(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Campaigns");
        return campaignService
            .countAll()
            .zipWith(campaignService.findAll(pageable).collectList())
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
     * {@code GET  /campaigns/:id} : get the "id" campaign.
     *
     * @param id the id of the campaignDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the campaignDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/campaigns/{id}")
    public Mono<ResponseEntity<CampaignDTO>> getCampaign(@PathVariable Long id) {
        log.debug("REST request to get Campaign : {}", id);
        Mono<CampaignDTO> campaignDTO = campaignService.findOne(id);
        return ResponseUtil.wrapOrNotFound(campaignDTO);
    }

    /**
     * {@code DELETE  /campaigns/:id} : delete the "id" campaign.
     *
     * @param id the id of the campaignDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/campaigns/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCampaign(@PathVariable Long id) {
        log.debug("REST request to delete Campaign : {}", id);
        return campaignService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
