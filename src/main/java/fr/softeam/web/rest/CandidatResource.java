package fr.softeam.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.softeam.domain.Candidat;
import fr.softeam.repository.CandidatRepository;
import fr.softeam.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Candidat.
 */
@RestController
@RequestMapping("/api")
public class CandidatResource {

    private final Logger log = LoggerFactory.getLogger(CandidatResource.class);
        
    @Inject
    private CandidatRepository candidatRepository;
    
    /**
     * POST  /candidats : Create a new candidat.
     *
     * @param candidat the candidat to create
     * @return the ResponseEntity with status 201 (Created) and with body the new candidat, or with status 400 (Bad Request) if the candidat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/candidats",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Candidat> createCandidat(@Valid @RequestBody Candidat candidat) throws URISyntaxException {
        log.debug("REST request to save Candidat : {}", candidat);
        if (candidat.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("candidat", "idexists", "A new candidat cannot already have an ID")).body(null);
        }
        Candidat result = candidatRepository.save(candidat);
        return ResponseEntity.created(new URI("/api/candidats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("candidat", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /candidats : Updates an existing candidat.
     *
     * @param candidat the candidat to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated candidat,
     * or with status 400 (Bad Request) if the candidat is not valid,
     * or with status 500 (Internal Server Error) if the candidat couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/candidats",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Candidat> updateCandidat(@Valid @RequestBody Candidat candidat) throws URISyntaxException {
        log.debug("REST request to update Candidat : {}", candidat);
        if (candidat.getId() == null) {
            return createCandidat(candidat);
        }
        Candidat result = candidatRepository.save(candidat);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("candidat", candidat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /candidats : get all the candidats.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of candidats in body
     */
    @RequestMapping(value = "/candidats",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Candidat> getAllCandidats() {
        log.debug("REST request to get all Candidats");
        List<Candidat> candidats = candidatRepository.findAll();
        return candidats;
    }

    /**
     * GET  /candidats/:id : get the "id" candidat.
     *
     * @param id the id of the candidat to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the candidat, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/candidats/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Candidat> getCandidat(@PathVariable Long id) {
        log.debug("REST request to get Candidat : {}", id);
        Candidat candidat = candidatRepository.findOne(id);
        return Optional.ofNullable(candidat)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /candidats/:id : delete the "id" candidat.
     *
     * @param id the id of the candidat to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/candidats/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCandidat(@PathVariable Long id) {
        log.debug("REST request to delete Candidat : {}", id);
        candidatRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("candidat", id.toString())).build();
    }

}