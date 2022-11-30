package com.muyuan.app.web.rest;

import com.muyuan.app.domain.SystemFunctionOperate;
import com.muyuan.app.repository.SystemFunctionOperateRepository;
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
 * REST controller for managing {@link com.muyuan.app.domain.SystemFunctionOperate}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SystemFunctionOperateResource {

    private final Logger log = LoggerFactory.getLogger(SystemFunctionOperateResource.class);

    private static final String ENTITY_NAME = "systemFunctionOperate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemFunctionOperateRepository systemFunctionOperateRepository;

    public SystemFunctionOperateResource(SystemFunctionOperateRepository systemFunctionOperateRepository) {
        this.systemFunctionOperateRepository = systemFunctionOperateRepository;
    }

    /**
     * {@code POST  /system-function-operates} : Create a new systemFunctionOperate.
     *
     * @param systemFunctionOperate the systemFunctionOperate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemFunctionOperate, or with status {@code 400 (Bad Request)} if the systemFunctionOperate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/system-function-operates")
    public ResponseEntity<SystemFunctionOperate> createSystemFunctionOperate(
        @Valid @RequestBody SystemFunctionOperate systemFunctionOperate
    ) throws URISyntaxException {
        log.debug("REST request to save SystemFunctionOperate : {}", systemFunctionOperate);
        if (systemFunctionOperate.getId() != null) {
            throw new BadRequestAlertException("A new systemFunctionOperate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemFunctionOperate result = systemFunctionOperateRepository.save(systemFunctionOperate);
        return ResponseEntity
            .created(new URI("/api/system-function-operates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /system-function-operates/:id} : Updates an existing systemFunctionOperate.
     *
     * @param id the id of the systemFunctionOperate to save.
     * @param systemFunctionOperate the systemFunctionOperate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemFunctionOperate,
     * or with status {@code 400 (Bad Request)} if the systemFunctionOperate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemFunctionOperate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/system-function-operates/{id}")
    public ResponseEntity<SystemFunctionOperate> updateSystemFunctionOperate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemFunctionOperate systemFunctionOperate
    ) throws URISyntaxException {
        log.debug("REST request to update SystemFunctionOperate : {}, {}", id, systemFunctionOperate);
        if (systemFunctionOperate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemFunctionOperate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemFunctionOperateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SystemFunctionOperate result = systemFunctionOperateRepository.save(systemFunctionOperate);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemFunctionOperate.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /system-function-operates/:id} : Partial updates given fields of an existing systemFunctionOperate, field will ignore if it is null
     *
     * @param id the id of the systemFunctionOperate to save.
     * @param systemFunctionOperate the systemFunctionOperate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemFunctionOperate,
     * or with status {@code 400 (Bad Request)} if the systemFunctionOperate is not valid,
     * or with status {@code 404 (Not Found)} if the systemFunctionOperate is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemFunctionOperate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/system-function-operates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemFunctionOperate> partialUpdateSystemFunctionOperate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemFunctionOperate systemFunctionOperate
    ) throws URISyntaxException {
        log.debug("REST request to partial update SystemFunctionOperate partially : {}, {}", id, systemFunctionOperate);
        if (systemFunctionOperate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemFunctionOperate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemFunctionOperateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemFunctionOperate> result = systemFunctionOperateRepository
            .findById(systemFunctionOperate.getId())
            .map(existingSystemFunctionOperate -> {
                if (systemFunctionOperate.getName() != null) {
                    existingSystemFunctionOperate.setName(systemFunctionOperate.getName());
                }
                if (systemFunctionOperate.getCode() != null) {
                    existingSystemFunctionOperate.setCode(systemFunctionOperate.getCode());
                }
                if (systemFunctionOperate.getOrderBy() != null) {
                    existingSystemFunctionOperate.setOrderBy(systemFunctionOperate.getOrderBy());
                }
                if (systemFunctionOperate.getNote() != null) {
                    existingSystemFunctionOperate.setNote(systemFunctionOperate.getNote());
                }
                if (systemFunctionOperate.getIsActive() != null) {
                    existingSystemFunctionOperate.setIsActive(systemFunctionOperate.getIsActive());
                }

                return existingSystemFunctionOperate;
            })
            .map(systemFunctionOperateRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemFunctionOperate.getId().toString())
        );
    }

    /**
     * {@code GET  /system-function-operates} : get all the systemFunctionOperates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemFunctionOperates in body.
     */
    @GetMapping("/system-function-operates")
    public List<SystemFunctionOperate> getAllSystemFunctionOperates() {
        log.debug("REST request to get all SystemFunctionOperates");
        return systemFunctionOperateRepository.findAll();
    }

    /**
     * {@code GET  /system-function-operates/:id} : get the "id" systemFunctionOperate.
     *
     * @param id the id of the systemFunctionOperate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemFunctionOperate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/system-function-operates/{id}")
    public ResponseEntity<SystemFunctionOperate> getSystemFunctionOperate(@PathVariable Long id) {
        log.debug("REST request to get SystemFunctionOperate : {}", id);
        Optional<SystemFunctionOperate> systemFunctionOperate = systemFunctionOperateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(systemFunctionOperate);
    }

    /**
     * {@code DELETE  /system-function-operates/:id} : delete the "id" systemFunctionOperate.
     *
     * @param id the id of the systemFunctionOperate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/system-function-operates/{id}")
    public ResponseEntity<Void> deleteSystemFunctionOperate(@PathVariable Long id) {
        log.debug("REST request to delete SystemFunctionOperate : {}", id);
        systemFunctionOperateRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
