package ro.upb.elth.licenta.bogdan.web.rest;

import ro.upb.elth.licenta.bogdan.domain.Rezervare;
import ro.upb.elth.licenta.bogdan.domain.enumeration.Disponibilitate;
import ro.upb.elth.licenta.bogdan.domain.enumeration.StatutRezervare;
import ro.upb.elth.licenta.bogdan.repository.RezervareRepository;
import ro.upb.elth.licenta.bogdan.security.AuthoritiesConstants;
import ro.upb.elth.licenta.bogdan.security.SecurityUtils;
import ro.upb.elth.licenta.bogdan.service.MailService;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ro.upb.elth.licenta.bogdan.domain.Rezervare}.
 */
@RestController
@RequestMapping("/api")
@Transactional
@Controller
public class RezervareResource {

    private final Logger log = LoggerFactory.getLogger(RezervareResource.class);

    private static final String ENTITY_NAME = "rezervare";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private final RezervareRepository rezervareRepository;

    @Autowired
    private final MailService mailService;

    static Thread threadCheckDates;
    static Object syncObject = new Object();


    public RezervareResource(RezervareRepository rezervareRepository, MailService mailService) {
        this.rezervareRepository = rezervareRepository;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /rezervares} : Create a new rezervare.
     *
     * @param rezervare the rezervare to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rezervare, or with status {@code 400 (Bad Request)} if the rezervare has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rezervares")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Rezervare> createRezervare(@Valid @RequestBody Rezervare rezervare) throws URISyntaxException {
        log.debug("REST request to save Rezervare : {}", rezervare);
        if (rezervare.getId() != null) {
            throw new BadRequestAlertException("A new rezervare cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rezervare result = rezervareRepository.save(rezervare);
        return ResponseEntity.created(new URI("/api/rezervares/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rezervares} : Updates an existing rezervare.
     *
     * @param rezervare the rezervare to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rezervare,
     * or with status {@code 400 (Bad Request)} if the rezervare is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rezervare couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rezervares")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Rezervare> updateRezervare(@Valid @RequestBody Rezervare rezervare) throws URISyntaxException {
        log.debug("REST request to update Rezervare : {}", rezervare);
        if (rezervare.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Rezervare result = rezervareRepository.save(rezervare);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rezervare.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rezervares} : get all the rezervares.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rezervares in body.
     */
    @GetMapping("/rezervares")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<Rezervare>> getAllRezervares(Pageable pageable) {
        Page<Rezervare> page = null;
        HttpHeaders headers = null;
        checkAllRezervari();
        
        log.debug("REST request to get a page of Rezervares");
        
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
        page = rezervareRepository.findAll(pageable);
        headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        } else {
            page = rezervareRepository.findByUserIsCurrentUser(pageable);
            headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
    }

    // Api pt. pornirea thread-ului (singleton) ce verifica daca rezervarile au expirat/finalizat la fiecare 5 sec.
    @GetMapping("/startThreadCheckRezervari")
    public void checkDateRezervari() {
        if(threadCheckDates == null) {
        Runnable checkRezervariTask = () -> {
            while(true) {
            try {
                synchronized(syncObject) {
                checkAllRezervari();
                }
               Thread.sleep(15000);
           } catch (Exception e) {
               e.printStackTrace();
               try {
                Thread.sleep(15000);
               } catch (Exception ex) {
                   ex.printStackTrace();
               }
           }
        }
        };
        threadCheckDates = new Thread(checkRezervariTask);
        threadCheckDates.start();
      }
    }

    // Admin Only:
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    @GetMapping("/pauseThreadCheckRezervari")
    public void pauseThreadCheckRezervari() {
        try {
            threadCheckDates.suspend();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    // Admin Only:
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    @GetMapping("/resumeThreadCheckRezervari") 
    public void resumeThreadCheckRezervari() {
        try {
            threadCheckDates.resume();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }
    //

    @GetMapping("/checkAllRezervari")
    public void checkAllRezervari() {
        log.debug("------------------------------------------------------------------------------------------------------------------");
        log.debug("REST request to check dates for all Rezervares");
        // Verifica data expirare / final pt. toate rezervarile cand oricare user intra in pagina de rezervari:
        Instant now = Instant.now();
        int val = 0;

        ArrayList<Rezervare> listaRezervari = (ArrayList<Rezervare>) rezervareRepository.findAll();
        for(Rezervare rezervare : listaRezervari) {
            
            if(rezervare.getStatut().equals(StatutRezervare.NECONFIRMAT)) {
                val = now.compareTo(rezervare.getDataExpirare());
                    if(val > 0) {
                        rezervare.setStatut(StatutRezervare.EXPIRAT);
                        rezervareRepository.save(rezervare);
                        mailService.sendRezervareExpirataEmail(rezervare.getUser(), rezervare);
                    }
             } else if(rezervare.getStatut().equals(StatutRezervare.CONFIRMAT)) {
                val = now.compareTo(rezervare.getDataFinal());
                    if(val > 0) {
                        rezervare.setStatut(StatutRezervare.FINALIZAT);
                        rezervareRepository.save(rezervare);
                        mailService.sendRezervareFinalizataEmail(rezervare.getUser(), rezervare);
                    }
             }
        }
        // end verificare data

        // Verificare si modificare statut incarcator daca rezervarea a fost finalizata / a expirat:  
        for(Rezervare rezervare : listaRezervari) {
            if(rezervare.getStatut().equals(StatutRezervare.FINALIZAT) || rezervare.getStatut().equals(StatutRezervare.EXPIRAT) || rezervare.getStatut().equals(StatutRezervare.NECUNOSCUT)) {
                rezervare.getIncarcator().setDisponibilitate(Disponibilitate.DISPONIBIL);
                rezervareRepository.save(rezervare);
            } 
        }
        //
        log.debug("------------------------------------------------------------------------------------------------------------------");
    }

    // Intoarce lista tuturor rezervarilor:
    @GetMapping("/listaRezervari")
    public @ResponseBody ResponseEntity<List<Rezervare>> getListaRezervari() {
        log.debug("REST request to get Rezervare : {}");
        List<Rezervare> rezervari = rezervareRepository.findAll();
        try {
        return new ResponseEntity<List<Rezervare>>(rezervari, HttpStatus.OK);
        } catch(ResponseStatusException e) {
            return null;
        }
    }
    //

    

    /**
     * {@code GET  /rezervares/:id} : get the "id" rezervare.
     *
     * @param id the id of the rezervare to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rezervare, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rezervares/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Rezervare> getRezervare(@PathVariable Long id) {
        log.debug("REST request to get Rezervare : {}", id);

        // Pentru a bloca accesul unui user la alte rezervari folosind URL:
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            Optional<Rezervare> rezervare = rezervareRepository.findById(id);
            return ResponseUtil.wrapOrNotFound(rezervare);
        } else {
            Optional<Rezervare> rezervare = rezervareRepository.findByUserIsCurrentUserById(id);
            return ResponseUtil.wrapOrNotFound(rezervare);  
        }
        //
    }

    // Intoarce rezervarile user-ului logat curent:
    @GetMapping("/rezervareCurrentUser")
    public @ResponseBody ResponseEntity<List<Rezervare>> getRezervareCurrentUser() {
        log.debug("REST request to get Rezervare : {}");
        List<Rezervare> rezervari = rezervareRepository.findByUserIsCurrentUser();
        try {
        return new ResponseEntity<List<Rezervare>>(rezervari, HttpStatus.OK);
        } catch(ResponseStatusException e) {
            return null;
        }
    }
    //

    /**
     * {@code DELETE  /rezervares/:id} : delete the "id" rezervare.
     *
     * @param id the id of the rezervare to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rezervares/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" +
AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> deleteRezervare(@PathVariable Long id) {
        log.debug("REST request to delete Rezervare : {}", id);
        rezervareRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
