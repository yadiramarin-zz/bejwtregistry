package be.jwtregistry.web.rest;

import be.jwtregistry.domain.Industries;
import be.jwtregistry.service.IndustriesService;
import be.jwtregistry.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link be.jwtregistry.domain.Industries}.
 */
@RestController
@RequestMapping("/api")
public class IndustriesResource {

    private final Logger log = LoggerFactory.getLogger(IndustriesResource.class);

    private static final String ENTITY_NAME = "bejwtregistryIndustries";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndustriesService industriesService;

    public IndustriesResource(IndustriesService industriesService) {
        this.industriesService = industriesService;
    }

    /**
     * {@code POST  /industries} : Create a new industries.
     *
     * @param industries the industries to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new industries, or with status {@code 400 (Bad Request)} if the industries has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/industries")
    public ResponseEntity<Industries> createIndustries(@RequestBody Industries industries) throws URISyntaxException {
        log.debug("REST request to save Industries : {}", industries);
        if (industries.getId() != null) {
            throw new BadRequestAlertException("A new industries cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Industries result = industriesService.save(industries);
        return ResponseEntity.created(new URI("/api/industries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /industries} : Updates an existing industries.
     *
     * @param industries the industries to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated industries,
     * or with status {@code 400 (Bad Request)} if the industries is not valid,
     * or with status {@code 500 (Internal Server Error)} if the industries couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/industries")
    public ResponseEntity<Industries> updateIndustries(@RequestBody Industries industries) throws URISyntaxException {
        log.debug("REST request to update Industries : {}", industries);
        if (industries.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Industries result = industriesService.save(industries);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, industries.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /industries} : get all the industries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of industries in body.
     */
    @GetMapping("/industries")
    public List<Industries> getAllIndustries() {
        log.debug("REST request to get all Industries");
        return industriesService.findAll();
    }

    /**
     * {@code GET  /industries/:id} : get the "id" industries.
     *
     * @param id the id of the industries to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the industries, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/industries/{id}")
    public ResponseEntity<Industries> getIndustries(@PathVariable Long id) {
        log.debug("REST request to get Industries : {}", id);
        Optional<Industries> industries = industriesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(industries);
    }

    /**
     * {@code DELETE  /industries/:id} : delete the "id" industries.
     *
     * @param id the id of the industries to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/industries/{id}")
    public ResponseEntity<Void> deleteIndustries(@PathVariable Long id) {
        log.debug("REST request to delete Industries : {}", id);
        industriesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
