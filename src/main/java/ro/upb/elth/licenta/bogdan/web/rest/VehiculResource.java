package ro.upb.elth.licenta.bogdan.web.rest;

import ro.upb.elth.licenta.bogdan.domain.Vehicul;
import ro.upb.elth.licenta.bogdan.repository.VehiculRepository;
import ro.upb.elth.licenta.bogdan.security.AuthoritiesConstants;
import ro.upb.elth.licenta.bogdan.security.SecurityUtils;
import ro.upb.elth.licenta.bogdan.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ro.upb.elth.licenta.bogdan.domain.Vehicul}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VehiculResource {

    private final Logger log = LoggerFactory.getLogger(VehiculResource.class);

    private static final String ENTITY_NAME = "vehicul";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehiculRepository vehiculRepository;

    public VehiculResource(VehiculRepository vehiculRepository) {
        this.vehiculRepository = vehiculRepository;
    }

    /**
     * {@code POST  /vehiculs} : Create a new vehicul.
     *
     * @param vehicul the vehicul to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicul, or with status {@code 400 (Bad Request)} if the vehicul has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vehiculs")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Vehicul> createVehicul(@Valid @RequestBody Vehicul vehicul) throws URISyntaxException {
        log.debug("REST request to save Vehicul : {}", vehicul);
        if (vehicul.getId() != null) {
            throw new BadRequestAlertException("A new vehicul cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vehicul result = vehiculRepository.save(vehicul);
        return ResponseEntity.created(new URI("/api/vehiculs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vehiculs} : Updates an existing vehicul.
     *
     * @param vehicul the vehicul to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicul,
     * or with status {@code 400 (Bad Request)} if the vehicul is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicul couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vehiculs")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Vehicul> updateVehicul(@Valid @RequestBody Vehicul vehicul) throws URISyntaxException {
        log.debug("REST request to update Vehicul : {}", vehicul);
        if (vehicul.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Vehicul result = vehiculRepository.save(vehicul);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicul.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /vehiculs} : get all the vehiculs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehiculs in body.
     */
    @GetMapping("/vehiculs")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<Vehicul>> getAllVehiculs(Pageable pageable) {
        log.debug("REST request to get a page of Vehiculs");
        Page<Vehicul> page = null;
        HttpHeaders headers = null;

        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            page = vehiculRepository.findAll(pageable);
            headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } else {
            page = vehiculRepository.findByUserIsCurrentUser(pageable);
            headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
    }

    /**
     * {@code GET  /vehiculs/:id} : get the "id" vehicul.
     *
     * @param id the id of the vehicul to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicul, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vehiculs/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Vehicul> getVehicul(@PathVariable Long id) {
        log.debug("REST request to get Vehicul : {}", id);

        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            Optional<Vehicul> vehicul = vehiculRepository.findById(id);
            return ResponseUtil.wrapOrNotFound(vehicul);
        } else {
            Optional<Vehicul> vehicul = vehiculRepository.findByUserIsCurrentUserById(id);
            return ResponseUtil.wrapOrNotFound(vehicul);   
        }
    }

    /**
     * {@code DELETE  /vehiculs/:id} : delete the "id" vehicul.
     *
     * @param id the id of the vehicul to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vehiculs/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> deleteVehicul(@PathVariable Long id) {
        log.debug("REST request to delete Vehicul : {}", id);
        vehiculRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
