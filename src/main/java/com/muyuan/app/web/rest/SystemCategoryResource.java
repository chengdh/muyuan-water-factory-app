package com.muyuan.app.web.rest;

import com.muyuan.app.domain.SystemCategory;
import com.muyuan.app.repository.SystemCategoryRepository;
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
 * REST controller for managing {@link com.muyuan.app.domain.SystemCategory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SystemCategoryResource {

    private final Logger log = LoggerFactory.getLogger(SystemCategoryResource.class);

    private static final String ENTITY_NAME = "systemCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemCategoryRepository systemCategoryRepository;

    public SystemCategoryResource(SystemCategoryRepository systemCategoryRepository) {
        this.systemCategoryRepository = systemCategoryRepository;
    }

    /**
     * {@code POST  /system-categories} : Create a new systemCategory.
     *
     * @param systemCategory the systemCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemCategory, or with status {@code 400 (Bad Request)} if the systemCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/system-categories")
    public ResponseEntity<SystemCategory> createSystemCategory(@Valid @RequestBody SystemCategory systemCategory)
        throws URISyntaxException {
        log.debug("REST request to save SystemCategory : {}", systemCategory);
        if (systemCategory.getId() != null) {
            throw new BadRequestAlertException("A new systemCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemCategory result = systemCategoryRepository.save(systemCategory);
        return ResponseEntity
            .created(new URI("/api/system-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /system-categories/:id} : Updates an existing systemCategory.
     *
     * @param id the id of the systemCategory to save.
     * @param systemCategory the systemCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemCategory,
     * or with status {@code 400 (Bad Request)} if the systemCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/system-categories/{id}")
    public ResponseEntity<SystemCategory> updateSystemCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemCategory systemCategory
    ) throws URISyntaxException {
        log.debug("REST request to update SystemCategory : {}, {}", id, systemCategory);
        if (systemCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SystemCategory result = systemCategoryRepository.save(systemCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /system-categories/:id} : Partial updates given fields of an existing systemCategory, field will ignore if it is null
     *
     * @param id the id of the systemCategory to save.
     * @param systemCategory the systemCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemCategory,
     * or with status {@code 400 (Bad Request)} if the systemCategory is not valid,
     * or with status {@code 404 (Not Found)} if the systemCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/system-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemCategory> partialUpdateSystemCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemCategory systemCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update SystemCategory partially : {}, {}", id, systemCategory);
        if (systemCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemCategory> result = systemCategoryRepository
            .findById(systemCategory.getId())
            .map(existingSystemCategory -> {
                if (systemCategory.getName() != null) {
                    existingSystemCategory.setName(systemCategory.getName());
                }
                if (systemCategory.getOrderBy() != null) {
                    existingSystemCategory.setOrderBy(systemCategory.getOrderBy());
                }
                if (systemCategory.getIcon() != null) {
                    existingSystemCategory.setIcon(systemCategory.getIcon());
                }
                if (systemCategory.getNote() != null) {
                    existingSystemCategory.setNote(systemCategory.getNote());
                }
                if (systemCategory.getIsActive() != null) {
                    existingSystemCategory.setIsActive(systemCategory.getIsActive());
                }

                return existingSystemCategory;
            })
            .map(systemCategoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /system-categories} : get all the systemCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemCategories in body.
     */
    @GetMapping("/system-categories")
    public List<SystemCategory> getAllSystemCategories() {
        log.debug("REST request to get all SystemCategories");
        return systemCategoryRepository.findAll();
    }

    /**
     * {@code GET  /system-categories/:id} : get the "id" systemCategory.
     *
     * @param id the id of the systemCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/system-categories/{id}")
    public ResponseEntity<SystemCategory> getSystemCategory(@PathVariable Long id) {
        log.debug("REST request to get SystemCategory : {}", id);
        Optional<SystemCategory> systemCategory = systemCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(systemCategory);
    }

    /**
     * {@code DELETE  /system-categories/:id} : delete the "id" systemCategory.
     *
     * @param id the id of the systemCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/system-categories/{id}")
    public ResponseEntity<Void> deleteSystemCategory(@PathVariable Long id) {
        log.debug("REST request to delete SystemCategory : {}", id);
        systemCategoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
