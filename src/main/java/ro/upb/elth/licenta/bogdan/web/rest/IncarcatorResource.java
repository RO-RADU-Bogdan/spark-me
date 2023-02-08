package ro.upb.elth.licenta.bogdan.web.rest;

import ro.upb.elth.licenta.bogdan.domain.Incarcator;
import ro.upb.elth.licenta.bogdan.domain.Statie;
import ro.upb.elth.licenta.bogdan.domain.enumeration.Disponibilitate;
import ro.upb.elth.licenta.bogdan.domain.enumeration.StatutStatie;
import ro.upb.elth.licenta.bogdan.repository.IncarcatorRepository;
import ro.upb.elth.licenta.bogdan.repository.RezervareRepository;
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
 * REST controller for managing {@link ro.upb.elth.licenta.bogdan.domain.Incarcator}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IncarcatorResource {

    private final Logger log = LoggerFactory.getLogger(IncarcatorResource.class);

    private static final String ENTITY_NAME = "incarcator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IncarcatorRepository incarcatorRepository;

    //
    @Autowired
    private RezervareRepository rezervareRepository;

    //
    @Autowired
    private StatieRepository statieRepository;

    public IncarcatorResource(IncarcatorRepository incarcatorRepository) {
        this.incarcatorRepository = incarcatorRepository;
    }

    /**
     * {@code POST  /incarcators} : Create a new incarcator.
     *
     * @param incarcator the incarcator to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new incarcator, or with status {@code 400 (Bad Request)} if the incarcator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/incarcators")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Incarcator> createIncarcator(@Valid @RequestBody Incarcator incarcator) throws URISyntaxException {
        log.debug("REST request to save Incarcator : {}", incarcator);
        if (incarcator.getId() != null) {
            throw new BadRequestAlertException("A new incarcator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Incarcator result = incarcatorRepository.save(incarcator);
        return ResponseEntity.created(new URI("/api/incarcators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /incarcators} : Updates an existing incarcator.
     *
     * @param incarcator the incarcator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incarcator,
     * or with status {@code 400 (Bad Request)} if the incarcator is not valid,
     * or with status {@code 500 (Internal Server Error)} if the incarcator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/incarcators")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Incarcator> updateIncarcator(@Valid @RequestBody Incarcator incarcator) throws URISyntaxException {
        log.debug("REST request to update Incarcator : {}", incarcator);
        if (incarcator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Incarcator result = incarcatorRepository.save(incarcator);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incarcator.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /incarcators} : get all the incarcators.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of incarcators in body.
     */
    @GetMapping("/incarcators")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<Incarcator>> getAllIncarcators(Pageable pageable) {
        log.debug("REST request to get a page of Incarcators");
         List<Incarcator> listaIncarcatoare = incarcatorRepository.findAll();
         List<Statie> listaStatii = statieRepository.findAll();

         for (Statie statie : listaStatii) {
            if(statie.getStatut().equals(StatutStatie.DISPONIBIL)) {     
            for (Incarcator incarcator : listaIncarcatoare) {
                if (incarcator.getStatie().getId() == statie.getId()
                        && incarcator.getDisponibilitate().equals(Disponibilitate.NECUNOSCUT)) {
                            incarcator.setDisponibilitate(Disponibilitate.DISPONIBIL);
                            incarcatorRepository.save(incarcator);
                        } 
                        else if (incarcator.getStatie().getId() == statie.getId()
                        && incarcator.getDisponibilitate().equals(Disponibilitate.OCUPAT)) {
                            statie.setStatut(StatutStatie.OCUPAT);
                            statieRepository.save(statie);                          
                        }
                        else if (incarcator.getStatie().getId() == statie.getId()
                        && incarcator.getDisponibilitate().equals(Disponibilitate.DISPONIBIL)) {
                            statie.setStatut(StatutStatie.DISPONIBIL);
                            statieRepository.save(statie);
                            break;                          
                        }
                }
            } 
            else if(statie.getStatut().equals(StatutStatie.OCUPAT)) {
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
            }
        }
        Page<Incarcator> page = incarcatorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /incarcators/:id} : get the "id" incarcator.
     *
     * @param id the id of the incarcator to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the incarcator, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/incarcators/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Incarcator> getIncarcator(@PathVariable Long id) {
        log.debug("REST request to get Incarcator : {}", id);
        Optional<Incarcator> incarcator = incarcatorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(incarcator);
    }

    /**
     * {@code DELETE  /incarcators/:id} : delete the "id" incarcator.
     *
     * @param id the id of the incarcator to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/incarcators/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteIncarcator(@PathVariable Long id) {
        log.debug("REST request to delete Incarcator : {}", id);
        incarcatorRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
