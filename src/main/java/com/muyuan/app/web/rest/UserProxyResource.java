package com.muyuan.app.web.rest;

import com.muyuan.app.domain.UserProxy;
import com.muyuan.app.repository.UserProxyRepository;
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
 * REST controller for managing {@link com.muyuan.app.domain.UserProxy}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserProxyResource {

    private final Logger log = LoggerFactory.getLogger(UserProxyResource.class);

    private static final String ENTITY_NAME = "userProxy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserProxyRepository userProxyRepository;

    public UserProxyResource(UserProxyRepository userProxyRepository) {
        this.userProxyRepository = userProxyRepository;
    }

    /**
     * {@code POST  /user-proxies} : Create a new userProxy.
     *
     * @param userProxy the userProxy to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userProxy, or with status {@code 400 (Bad Request)} if the userProxy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-proxies")
    public ResponseEntity<UserProxy> createUserProxy(@RequestBody UserProxy userProxy) throws URISyntaxException {
        log.debug("REST request to save UserProxy : {}", userProxy);
        if (userProxy.getId() != null) {
            throw new BadRequestAlertException("A new userProxy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserProxy result = userProxyRepository.save(userProxy);
        return ResponseEntity
            .created(new URI("/api/user-proxies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-proxies/:id} : Updates an existing userProxy.
     *
     * @param id the id of the userProxy to save.
     * @param userProxy the userProxy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userProxy,
     * or with status {@code 400 (Bad Request)} if the userProxy is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userProxy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-proxies/{id}")
    public ResponseEntity<UserProxy> updateUserProxy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserProxy userProxy
    ) throws URISyntaxException {
        log.debug("REST request to update UserProxy : {}, {}", id, userProxy);
        if (userProxy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userProxy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userProxyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserProxy result = userProxyRepository.save(userProxy);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userProxy.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-proxies/:id} : Partial updates given fields of an existing userProxy, field will ignore if it is null
     *
     * @param id the id of the userProxy to save.
     * @param userProxy the userProxy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userProxy,
     * or with status {@code 400 (Bad Request)} if the userProxy is not valid,
     * or with status {@code 404 (Not Found)} if the userProxy is not found,
     * or with status {@code 500 (Internal Server Error)} if the userProxy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-proxies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserProxy> partialUpdateUserProxy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserProxy userProxy
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserProxy partially : {}, {}", id, userProxy);
        if (userProxy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userProxy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userProxyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserProxy> result = userProxyRepository
            .findById(userProxy.getId())
            .map(existingUserProxy -> {
                if (userProxy.getIsActive() != null) {
                    existingUserProxy.setIsActive(userProxy.getIsActive());
                }
                if (userProxy.getNote() != null) {
                    existingUserProxy.setNote(userProxy.getNote());
                }

                return existingUserProxy;
            })
            .map(userProxyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userProxy.getId().toString())
        );
    }

    /**
     * {@code GET  /user-proxies} : get all the userProxies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userProxies in body.
     */
    @GetMapping("/user-proxies")
    public List<UserProxy> getAllUserProxies() {
        log.debug("REST request to get all UserProxies");
        return userProxyRepository.findAll();
    }

    /**
     * {@code GET  /user-proxies/:id} : get the "id" userProxy.
     *
     * @param id the id of the userProxy to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userProxy, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-proxies/{id}")
    public ResponseEntity<UserProxy> getUserProxy(@PathVariable Long id) {
        log.debug("REST request to get UserProxy : {}", id);
        Optional<UserProxy> userProxy = userProxyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userProxy);
    }

    /**
     * {@code DELETE  /user-proxies/:id} : delete the "id" userProxy.
     *
     * @param id the id of the userProxy to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-proxies/{id}")
    public ResponseEntity<Void> deleteUserProxy(@PathVariable Long id) {
        log.debug("REST request to delete UserProxy : {}", id);
        userProxyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
