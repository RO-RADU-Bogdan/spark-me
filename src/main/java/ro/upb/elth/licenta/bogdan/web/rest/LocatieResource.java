package ro.upb.elth.licenta.bogdan.web.rest;

import ro.upb.elth.licenta.bogdan.domain.Locatie;
import ro.upb.elth.licenta.bogdan.domain.Statie;
import ro.upb.elth.licenta.bogdan.repository.LocatieRepository;
import ro.upb.elth.licenta.bogdan.repository.StatieRepository;
import ro.upb.elth.licenta.bogdan.security.AuthoritiesConstants;
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
 * REST controller for managing {@link ro.upb.elth.licenta.bogdan.domain.Locatie}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LocatieResource {

    private final Logger log = LoggerFactory.getLogger(LocatieResource.class);

    private static final String ENTITY_NAME = "locatie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocatieRepository locatieRepository;

    //
    private final StatieRepository statieRepository;


    public LocatieResource(LocatieRepository locatieRepository, StatieRepository statieRepository) {
        this.locatieRepository = locatieRepository;
        this.statieRepository = statieRepository;
    }

    /**
     * {@code POST  /locaties} : Create a new locatie.
     *
     * @param locatie the locatie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locatie, or with status {@code 400 (Bad Request)} if the locatie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/locaties")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Locatie> createLocatie(@Valid @RequestBody Locatie locatie) throws URISyntaxException {
        log.debug("REST request to save Locatie : {}", locatie);
        if (locatie.getId() != null) {
            throw new BadRequestAlertException("A new locatie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Locatie result = locatieRepository.save(locatie);
        return ResponseEntity.created(new URI("/api/locaties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /locaties} : Updates an existing locatie.
     *
     * @param locatie the locatie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locatie,
     * or with status {@code 400 (Bad Request)} if the locatie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locatie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/locaties")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Locatie> updateLocatie(@Valid @RequestBody Locatie locatie) throws URISyntaxException {
        log.debug("REST request to update Locatie : {}", locatie);
        if (locatie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Locatie result = locatieRepository.save(locatie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locatie.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /locaties} : get all the locaties.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locaties in body.
     */
    @GetMapping("/locaties")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<Locatie>> getAllLocaties(Pageable pageable) {
        log.debug("REST request to get a page of Locaties");
        Page<Locatie> page = locatieRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /locaties/:id} : get the "id" locatie.
     *
     * @param id the id of the locatie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locatie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/locaties/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Locatie> getLocatie(@PathVariable Long id) {
        log.debug("REST request to get Locatie : {}", id);
        Optional<Locatie> locatie = locatieRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(locatie);
    }

    /**
     * {@code DELETE  /locaties/:id} : delete the "id" locatie.
     *
     * @param id the id of the locatie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/locaties/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteLocatie(@PathVariable Long id) {
        // 
        List<Statie> listaStatii = statieRepository.findStatiiByLocatieId(id);
        for (Statie statie : listaStatii) {
            statie.setLocatie(null);
            statieRepository.save(statie);
        }
        //
        log.debug("REST request to delete Locatie : {}", id);
        locatieRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
