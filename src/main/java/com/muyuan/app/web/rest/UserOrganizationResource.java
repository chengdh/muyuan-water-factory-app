package com.muyuan.app.web.rest;

import com.muyuan.app.domain.UserOrganization;
import com.muyuan.app.repository.UserOrganizationRepository;
import com.muyuan.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.muyuan.app.domain.UserOrganization}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserOrganizationResource {

    private final Logger log = LoggerFactory.getLogger(UserOrganizationResource.class);

    private static final String ENTITY_NAME = "userOrganization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserOrganizationRepository userOrganizationRepository;

    public UserOrganizationResource(UserOrganizationRepository userOrganizationRepository) {
        this.userOrganizationRepository = userOrganizationRepository;
    }

    /**
     * {@code POST  /user-organizations} : Create a new userOrganization.
     *
     * @param userOrganization the userOrganization to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userOrganization, or with status {@code 400 (Bad Request)} if the userOrganization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-organizations")
    public ResponseEntity<UserOrganization> createUserOrganization(@RequestBody UserOrganization userOrganization)
        throws URISyntaxException {
        log.debug("REST request to save UserOrganization : {}", userOrganization);
        if (userOrganization.getId() != null) {
            throw new BadRequestAlertException("A new userOrganization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserOrganization result = userOrganizationRepository.save(userOrganization);
        return ResponseEntity
            .created(new URI("/api/user-organizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-organizations/:id} : Updates an existing userOrganization.
     *
     * @param id the id of the userOrganization to save.
     * @param userOrganization the userOrganization to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userOrganization,
     * or with status {@code 400 (Bad Request)} if the userOrganization is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userOrganization couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-organizations/{id}")
    public ResponseEntity<UserOrganization> updateUserOrganization(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserOrganization userOrganization
    ) throws URISyntaxException {
        log.debug("REST request to update UserOrganization : {}, {}", id, userOrganization);
        if (userOrganization.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userOrganization.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userOrganizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserOrganization result = userOrganizationRepository.save(userOrganization);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userOrganization.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-organizations/:id} : Partial updates given fields of an existing userOrganization, field will ignore if it is null
     *
     * @param id the id of the userOrganization to save.
     * @param userOrganization the userOrganization to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userOrganization,
     * or with status {@code 400 (Bad Request)} if the userOrganization is not valid,
     * or with status {@code 404 (Not Found)} if the userOrganization is not found,
     * or with status {@code 500 (Internal Server Error)} if the userOrganization couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-organizations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserOrganization> partialUpdateUserOrganization(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserOrganization userOrganization
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserOrganization partially : {}, {}", id, userOrganization);
        if (userOrganization.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userOrganization.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userOrganizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserOrganization> result = userOrganizationRepository
            .findById(userOrganization.getId())
            .map(existingUserOrganization -> {
                if (userOrganization.getNote() != null) {
                    existingUserOrganization.setNote(userOrganization.getNote());
                }

                return existingUserOrganization;
            })
            .map(userOrganizationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userOrganization.getId().toString())
        );
    }

    /**
     * {@code GET  /user-organizations} : get all the userOrganizations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userOrganizations in body.
     */
    @GetMapping("/user-organizations")
    public List<UserOrganization> getAllUserOrganizations() {
        log.debug("REST request to get all UserOrganizations");
        return userOrganizationRepository.findAll();
    }

    /**
     * {@code GET  /user-organizations/:id} : get the "id" userOrganization.
     *
     * @param id the id of the userOrganization to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userOrganization, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-organizations/{id}")
    public ResponseEntity<UserOrganization> getUserOrganization(@PathVariable Long id) {
        log.debug("REST request to get UserOrganization : {}", id);
        Optional<UserOrganization> userOrganization = userOrganizationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userOrganization);
    }

    /**
     * {@code DELETE  /user-organizations/:id} : delete the "id" userOrganization.
     *
     * @param id the id of the userOrganization to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-organizations/{id}")
    public ResponseEntity<Void> deleteUserOrganization(@PathVariable Long id) {
        log.debug("REST request to delete UserOrganization : {}", id);
        userOrganizationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
