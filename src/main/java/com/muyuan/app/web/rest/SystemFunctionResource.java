package com.muyuan.app.web.rest;

import com.muyuan.app.domain.SystemFunction;
import com.muyuan.app.repository.SystemFunctionRepository;
import com.muyuan.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.muyuan.app.domain.SystemFunction}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SystemFunctionResource {

    private final Logger log = LoggerFactory.getLogger(SystemFunctionResource.class);

    private static final String ENTITY_NAME = "systemFunction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemFunctionRepository systemFunctionRepository;

    public SystemFunctionResource(SystemFunctionRepository systemFunctionRepository) {
        this.systemFunctionRepository = systemFunctionRepository;
    }

    /**
     * {@code POST  /system-functions} : Create a new systemFunction.
     *
     * @param systemFunction the systemFunction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemFunction, or with status {@code 400 (Bad Request)} if the systemFunction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/system-functions")
    public ResponseEntity<SystemFunction> createSystemFunction(@Valid @RequestBody SystemFunction systemFunction)
        throws URISyntaxException {
        log.debug("REST request to save SystemFunction : {}", systemFunction);
        if (systemFunction.getId() != null) {
            throw new BadRequestAlertException("A new systemFunction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemFunction result = systemFunctionRepository.save(systemFunction);
        return ResponseEntity
            .created(new URI("/api/system-functions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /system-functions/:id} : Updates an existing systemFunction.
     *
     * @param id the id of the systemFunction to save.
     * @param systemFunction the systemFunction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemFunction,
     * or with status {@code 400 (Bad Request)} if the systemFunction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemFunction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/system-functions/{id}")
    public ResponseEntity<SystemFunction> updateSystemFunction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemFunction systemFunction
    ) throws URISyntaxException {
        log.debug("REST request to update SystemFunction : {}, {}", id, systemFunction);
        if (systemFunction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemFunction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemFunctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SystemFunction result = systemFunctionRepository.save(systemFunction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemFunction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /system-functions/:id} : Partial updates given fields of an existing systemFunction, field will ignore if it is null
     *
     * @param id the id of the systemFunction to save.
     * @param systemFunction the systemFunction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemFunction,
     * or with status {@code 400 (Bad Request)} if the systemFunction is not valid,
     * or with status {@code 404 (Not Found)} if the systemFunction is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemFunction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/system-functions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemFunction> partialUpdateSystemFunction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemFunction systemFunction
    ) throws URISyntaxException {
        log.debug("REST request to partial update SystemFunction partially : {}, {}", id, systemFunction);
        if (systemFunction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemFunction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemFunctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemFunction> result = systemFunctionRepository
            .findById(systemFunction.getId())
            .map(existingSystemFunction -> {
                if (systemFunction.getName() != null) {
                    existingSystemFunction.setName(systemFunction.getName());
                }
                if (systemFunction.getOrderBy() != null) {
                    existingSystemFunction.setOrderBy(systemFunction.getOrderBy());
                }
                if (systemFunction.getIcon() != null) {
                    existingSystemFunction.setIcon(systemFunction.getIcon());
                }
                if (systemFunction.getNote() != null) {
                    existingSystemFunction.setNote(systemFunction.getNote());
                }
                if (systemFunction.getIsActive() != null) {
                    existingSystemFunction.setIsActive(systemFunction.getIsActive());
                }
                if (systemFunction.getDefaultAction() != null) {
                    existingSystemFunction.setDefaultAction(systemFunction.getDefaultAction());
                }

                return existingSystemFunction;
            })
            .map(systemFunctionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemFunction.getId().toString())
        );
    }

    /**
     * {@code GET  /system-functions} : get all the systemFunctions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemFunctions in body.
     */
    @GetMapping("/system-functions")
    public List<SystemFunction> getAllSystemFunctions() {
        log.debug("REST request to get all SystemFunctions");
        return systemFunctionRepository.findAll();
    }

    /**
     * {@code GET  /system-functions/:id} : get the "id" systemFunction.
     *
     * @param id the id of the systemFunction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemFunction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/system-functions/{id}")
    public ResponseEntity<SystemFunction> getSystemFunction(@PathVariable Long id) {
        log.debug("REST request to get SystemFunction : {}", id);
        Optional<SystemFunction> systemFunction = systemFunctionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(systemFunction);
    }

    /**
     * {@code DELETE  /system-functions/:id} : delete the "id" systemFunction.
     *
     * @param id the id of the systemFunction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/system-functions/{id}")
    public ResponseEntity<Void> deleteSystemFunction(@PathVariable Long id) {
        log.debug("REST request to delete SystemFunction : {}", id);
        systemFunctionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
