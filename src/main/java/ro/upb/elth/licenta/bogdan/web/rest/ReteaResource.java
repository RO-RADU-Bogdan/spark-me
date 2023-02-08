package ro.upb.elth.licenta.bogdan.web.rest;

import ro.upb.elth.licenta.bogdan.domain.Retea;
import ro.upb.elth.licenta.bogdan.domain.Statie;
import ro.upb.elth.licenta.bogdan.repository.ReteaRepository;
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
 * REST controller for managing {@link ro.upb.elth.licenta.bogdan.domain.Retea}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReteaResource {

    private final Logger log = LoggerFactory.getLogger(ReteaResource.class);

    private static final String ENTITY_NAME = "retea";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReteaRepository reteaRepository;
    //
    private final StatieRepository statieRepository;

    public ReteaResource(ReteaRepository reteaRepository, StatieRepository statieRepository) {
        this.reteaRepository = reteaRepository;
        this.statieRepository = statieRepository;
    }

    /**
     * {@code POST  /reteas} : Create a new retea.
     *
     * @param retea the retea to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new retea, or with status {@code 400 (Bad Request)} if the retea has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reteas")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Retea> createRetea(@Valid @RequestBody Retea retea) throws URISyntaxException {
        log.debug("REST request to save Retea : {}", retea);
        if (retea.getId() != null) {
            throw new BadRequestAlertException("A new retea cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Retea result = reteaRepository.save(retea);
        return ResponseEntity.created(new URI("/api/reteas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reteas} : Updates an existing retea.
     *
     * @param retea the retea to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retea,
     * or with status {@code 400 (Bad Request)} if the retea is not valid,
     * or with status {@code 500 (Internal Server Error)} if the retea couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reteas")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Retea> updateRetea(@Valid @RequestBody Retea retea) throws URISyntaxException {
        log.debug("REST request to update Retea : {}", retea);
        if (retea.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Retea result = reteaRepository.save(retea);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, retea.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /reteas} : get all the reteas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reteas in body.
     */
    @GetMapping("/reteas")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<Retea>> getAllReteas(Pageable pageable) {
        log.debug("REST request to get a page of Reteas");
        Page<Retea> page = reteaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reteas/:id} : get the "id" retea.
     *
     * @param id the id of the retea to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the retea, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reteas/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Retea> getRetea(@PathVariable Long id) {
        log.debug("REST request to get Retea : {}", id);
        Optional<Retea> retea = reteaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(retea);
    }

    /**
     * {@code DELETE  /reteas/:id} : delete the "id" retea.
     *
     * @param id the id of the retea to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reteas/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteRetea(@PathVariable Long id) {
        // 
        List<Statie> listaStatii = statieRepository.findStatiiByReteaId(id);
        for (Statie statie : listaStatii) {
            statie.setRetea(null);
            statieRepository.save(statie);
        }
        //
        log.debug("REST request to delete Retea : {}", id);
        reteaRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
