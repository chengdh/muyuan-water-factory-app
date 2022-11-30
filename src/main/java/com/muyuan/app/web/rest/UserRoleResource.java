package com.muyuan.app.web.rest;

import com.muyuan.app.domain.UserRole;
import com.muyuan.app.repository.UserRoleRepository;
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
 * REST controller for managing {@link com.muyuan.app.domain.UserRole}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserRoleResource {

    private final Logger log = LoggerFactory.getLogger(UserRoleResource.class);

    private static final String ENTITY_NAME = "userRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserRoleRepository userRoleRepository;

    public UserRoleResource(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * {@code POST  /user-roles} : Create a new userRole.
     *
     * @param userRole the userRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userRole, or with status {@code 400 (Bad Request)} if the userRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-roles")
    public ResponseEntity<UserRole> createUserRole(@RequestBody UserRole userRole) throws URISyntaxException {
        log.debug("REST request to save UserRole : {}", userRole);
        if (userRole.getId() != null) {
            throw new BadRequestAlertException("A new userRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserRole result = userRoleRepository.save(userRole);
        return ResponseEntity
            .created(new URI("/api/user-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-roles/:id} : Updates an existing userRole.
     *
     * @param id the id of the userRole to save.
     * @param userRole the userRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userRole,
     * or with status {@code 400 (Bad Request)} if the userRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-roles/{id}")
    public ResponseEntity<UserRole> updateUserRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserRole userRole
    ) throws URISyntaxException {
        log.debug("REST request to update UserRole : {}, {}", id, userRole);
        if (userRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserRole result = userRoleRepository.save(userRole);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userRole.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-roles/:id} : Partial updates given fields of an existing userRole, field will ignore if it is null
     *
     * @param id the id of the userRole to save.
     * @param userRole the userRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userRole,
     * or with status {@code 400 (Bad Request)} if the userRole is not valid,
     * or with status {@code 404 (Not Found)} if the userRole is not found,
     * or with status {@code 500 (Internal Server Error)} if the userRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-roles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserRole> partialUpdateUserRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserRole userRole
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserRole partially : {}, {}", id, userRole);
        if (userRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserRole> result = userRoleRepository
            .findById(userRole.getId())
            .map(existingUserRole -> {
                if (userRole.getNote() != null) {
                    existingUserRole.setNote(userRole.getNote());
                }

                return existingUserRole;
            })
            .map(userRoleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userRole.getId().toString())
        );
    }

    /**
     * {@code GET  /user-roles} : get all the userRoles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userRoles in body.
     */
    @GetMapping("/user-roles")
    public List<UserRole> getAllUserRoles() {
        log.debug("REST request to get all UserRoles");
        return userRoleRepository.findAll();
    }

    /**
     * {@code GET  /user-roles/:id} : get the "id" userRole.
     *
     * @param id the id of the userRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-roles/{id}")
    public ResponseEntity<UserRole> getUserRole(@PathVariable Long id) {
        log.debug("REST request to get UserRole : {}", id);
        Optional<UserRole> userRole = userRoleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userRole);
    }

    /**
     * {@code DELETE  /user-roles/:id} : delete the "id" userRole.
     *
     * @param id the id of the userRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-roles/{id}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable Long id) {
        log.debug("REST request to delete UserRole : {}", id);
        userRoleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
