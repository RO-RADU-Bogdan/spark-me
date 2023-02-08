package ro.upb.elth.licenta.bogdan.web.rest;

import ro.upb.elth.licenta.bogdan.domain.Incarcator;
import ro.upb.elth.licenta.bogdan.domain.Statie;
import ro.upb.elth.licenta.bogdan.domain.enumeration.Disponibilitate;
import ro.upb.elth.licenta.bogdan.domain.enumeration.StatutStatie;
import ro.upb.elth.licenta.bogdan.repository.IncarcatorRepository;
import ro.upb.elth.licenta.bogdan.repository.StatieRepository;
import ro.upb.elth.licenta.bogdan.security.AuthoritiesConstants;
import ro.upb.elth.licenta.bogdan.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing
 * {@link ro.upb.elth.licenta.bogdan.domain.Statie}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StatieResource {

    private final Logger log = LoggerFactory.getLogger(StatieResource.class);

    private static final String ENTITY_NAME = "statie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StatieRepository statieRepository;

    //
    @Autowired
    private IncarcatorRepository incarcatorRepository;

    public StatieResource(StatieRepository statieRepository) {
        this.statieRepository = statieRepository;
    }

    /**
     * {@code POST  /staties} : Create a new statie.
     *
     * @param statie the statie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new statie, or with status {@code 400 (Bad Request)} if the
     *         statie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/staties")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Statie> createStatie(@Valid @RequestBody Statie statie) throws URISyntaxException {
        log.debug("REST request to save Statie : {}", statie);
        if (statie.getId() != null) {
            throw new BadRequestAlertException("A new statie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Statie result = statieRepository.save(statie);
        return ResponseEntity
                .created(new URI("/api/staties/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /staties} : Updates an existing statie.
     *
     * @param statie the statie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated statie, or with status {@code 400 (Bad Request)} if the
     *         statie is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the statie couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/staties")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Statie> updateStatie(@Valid @RequestBody Statie statie) throws URISyntaxException {
        log.debug("REST request to update Statie : {}", statie);
        if (statie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Statie result = statieRepository.save(statie);
        return ResponseEntity.ok().headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statie.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /staties} : get all the staties.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of staties in body.
     */
    @GetMapping("/staties")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<Statie>> getAllStaties(Pageable pageable) {
        log.debug("REST request to get a page of Staties");
        Page<Statie> page = statieRepository.findAll(pageable);

        List<Incarcator> listaIncarcatoare = incarcatorRepository.findAll();
        List<Statie> listaStatii = page.getContent();

        for (Statie statie : listaStatii) {
            if(statie.getStatut().equals(StatutStatie.OCUPAT) ) {
            for (Incarcator incarcator : listaIncarcatoare) {
                if (incarcator.getStatie().getId() == statie.getId()
                        && incarcator.getDisponibilitate().equals(Disponibilitate.DISPONIBIL)) {
                    statie.setStatut(StatutStatie.DISPONIBIL);
                    statieRepository.save(statie);
                    break; 
                } else if (incarcator.getStatie().getId() == statie.getId()
                        && incarcator.getDisponibilitate().equals(Disponibilitate.OCUPAT))  {
                    statie.setStatut(StatutStatie.OCUPAT);
                    statieRepository.save(statie);
                }
              }
            } else if(statie.getStatut().equals(StatutStatie.INDISPONIBIL) || statie.getStatut().equals(StatutStatie.NECUNOSCUT)) {
                for (Incarcator incarcator : listaIncarcatoare) {
                    if (incarcator.getStatie().getId() == statie.getId()) {
                    incarcator.setDisponibilitate(Disponibilitate.NECUNOSCUT);
                    incarcatorRepository.save(incarcator);
                    }
                  }
            } else if(statie.getStatut().equals(StatutStatie.DISPONIBIL)) {
                for (Incarcator incarcator : listaIncarcatoare) {
                    if (incarcator.getStatie().getId() == statie.getId() && !incarcator.getDisponibilitate().equals(Disponibilitate.OCUPAT)) {
                    incarcator.setDisponibilitate(Disponibilitate.DISPONIBIL);
                    incarcatorRepository.save(incarcator);
                    }
                }
            }
        }
        page = new PageImpl<>(listaStatii);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /staties/:id} : get the "id" statie.
     *
     * @param id the id of the statie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the statie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/staties/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Statie> getStatie(@PathVariable Long id) {
        log.debug("REST request to get Statie : {}", id);
        Optional<Statie> statie = statieRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(statie);
    }

    /**
     * {@code DELETE  /staties/:id} : delete the "id" statie.
     *
     * @param id the id of the statie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/staties/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteStatie(@PathVariable Long id) {
        log.debug("REST request to delete Statie : {}", id);
        statieRepository.deleteById(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    // Intoarce lista tuturor statiilor:
    @GetMapping("/listaStatii")
    public @ResponseBody ResponseEntity<List<Statie>> getListaStatii() {
        log.debug("REST request to get Rezervare : {}");
        List<Statie> statii = statieRepository.findAll();
        try {
        return new ResponseEntity<List<Statie>>(statii, HttpStatus.OK);
        } catch(ResponseStatusException e) {
            return null;
        }
    }
    //
}
