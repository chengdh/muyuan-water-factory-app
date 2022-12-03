package com.muyuan.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.muyuan.app.IntegrationTest;
import com.muyuan.app.domain.SystemFunction;
import com.muyuan.app.repository.SystemFunctionRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SystemFunctionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemFunctionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER_BY = 1;
    private static final Integer UPDATED_ORDER_BY = 2;

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_ACTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/system-functions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SystemFunctionRepository systemFunctionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemFunctionMockMvc;

    private SystemFunction systemFunction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemFunction createEntity(EntityManager em) {
        SystemFunction systemFunction = new SystemFunction()
            .name(DEFAULT_NAME)
            .orderBy(DEFAULT_ORDER_BY)
            .icon(DEFAULT_ICON)
            .note(DEFAULT_NOTE)
            .isActive(DEFAULT_IS_ACTIVE)
            .defaultAction(DEFAULT_DEFAULT_ACTION);
        return systemFunction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemFunction createUpdatedEntity(EntityManager em) {
        SystemFunction systemFunction = new SystemFunction()
            .name(UPDATED_NAME)
            .orderBy(UPDATED_ORDER_BY)
            .icon(UPDATED_ICON)
            .note(UPDATED_NOTE)
            .isActive(UPDATED_IS_ACTIVE)
            .defaultAction(UPDATED_DEFAULT_ACTION);
        return systemFunction;
    }

    @BeforeEach
    public void initTest() {
        systemFunction = createEntity(em);
    }

    @Test
    @Transactional
    void createSystemFunction() throws Exception {
        int databaseSizeBeforeCreate = systemFunctionRepository.findAll().size();
        // Create the SystemFunction
        restSystemFunctionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemFunction))
            )
            .andExpect(status().isCreated());

        // Validate the SystemFunction in the database
        List<SystemFunction> systemFunctionList = systemFunctionRepository.findAll();
        assertThat(systemFunctionList).hasSize(databaseSizeBeforeCreate + 1);
        SystemFunction testSystemFunction = systemFunctionList.get(systemFunctionList.size() - 1);
        assertThat(testSystemFunction.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSystemFunction.getOrderBy()).isEqualTo(DEFAULT_ORDER_BY);
        assertThat(testSystemFunction.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testSystemFunction.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testSystemFunction.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testSystemFunction.getDefaultAction()).isEqualTo(DEFAULT_DEFAULT_ACTION);
    }

    @Test
    @Transactional
    void createSystemFunctionWithExistingId() throws Exception {
        // Create the SystemFunction with an existing ID
        systemFunction.setId(1L);

        int databaseSizeBeforeCreate = systemFunctionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemFunctionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemFunction))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemFunction in the database
        List<SystemFunction> systemFunctionList = systemFunctionRepository.findAll();
        assertThat(systemFunctionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemFunctionRepository.findAll().size();
        // set the field null
        systemFunction.setName(null);

        // Create the SystemFunction, which fails.

        restSystemFunctionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemFunction))
            )
            .andExpect(status().isBadRequest());

        List<SystemFunction> systemFunctionList = systemFunctionRepository.findAll();
        assertThat(systemFunctionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemFunctions() throws Exception {
        // Initialize the database
        systemFunctionRepository.saveAndFlush(systemFunction);

        // Get all the systemFunctionList
        restSystemFunctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemFunction.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].orderBy").value(hasItem(DEFAULT_ORDER_BY)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].defaultAction").value(hasItem(DEFAULT_DEFAULT_ACTION)));
    }

    @Test
    @Transactional
    void getSystemFunction() throws Exception {
        // Initialize the database
        systemFunctionRepository.saveAndFlush(systemFunction);

        // Get the systemFunction
        restSystemFunctionMockMvc
            .perform(get(ENTITY_API_URL_ID, systemFunction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemFunction.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.orderBy").value(DEFAULT_ORDER_BY))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.defaultAction").value(DEFAULT_DEFAULT_ACTION));
    }

    @Test
    @Transactional
    void getNonExistingSystemFunction() throws Exception {
        // Get the systemFunction
        restSystemFunctionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemFunction() throws Exception {
        // Initialize the database
        systemFunctionRepository.saveAndFlush(systemFunction);

        int databaseSizeBeforeUpdate = systemFunctionRepository.findAll().size();

        // Update the systemFunction
        SystemFunction updatedSystemFunction = systemFunctionRepository.findById(systemFunction.getId()).get();
        // Disconnect from session so that the updates on updatedSystemFunction are not directly saved in db
        em.detach(updatedSystemFunction);
        updatedSystemFunction
            .name(UPDATED_NAME)
            .orderBy(UPDATED_ORDER_BY)
            .icon(UPDATED_ICON)
            .note(UPDATED_NOTE)
            .isActive(UPDATED_IS_ACTIVE)
            .defaultAction(UPDATED_DEFAULT_ACTION);

        restSystemFunctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSystemFunction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSystemFunction))
            )
            .andExpect(status().isOk());

        // Validate the SystemFunction in the database
        List<SystemFunction> systemFunctionList = systemFunctionRepository.findAll();
        assertThat(systemFunctionList).hasSize(databaseSizeBeforeUpdate);
        SystemFunction testSystemFunction = systemFunctionList.get(systemFunctionList.size() - 1);
        assertThat(testSystemFunction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemFunction.getOrderBy()).isEqualTo(UPDATED_ORDER_BY);
        assertThat(testSystemFunction.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testSystemFunction.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testSystemFunction.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testSystemFunction.getDefaultAction()).isEqualTo(UPDATED_DEFAULT_ACTION);
    }

    @Test
    @Transactional
    void putNonExistingSystemFunction() throws Exception {
        int databaseSizeBeforeUpdate = systemFunctionRepository.findAll().size();
        systemFunction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemFunctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemFunction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemFunction))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemFunction in the database
        List<SystemFunction> systemFunctionList = systemFunctionRepository.findAll();
        assertThat(systemFunctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemFunction() throws Exception {
        int databaseSizeBeforeUpdate = systemFunctionRepository.findAll().size();
        systemFunction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemFunctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemFunction))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemFunction in the database
        List<SystemFunction> systemFunctionList = systemFunctionRepository.findAll();
        assertThat(systemFunctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemFunction() throws Exception {
        int databaseSizeBeforeUpdate = systemFunctionRepository.findAll().size();
        systemFunction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemFunctionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemFunction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemFunction in the database
        List<SystemFunction> systemFunctionList = systemFunctionRepository.findAll();
        assertThat(systemFunctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemFunctionWithPatch() throws Exception {
        // Initialize the database
        systemFunctionRepository.saveAndFlush(systemFunction);

        int databaseSizeBeforeUpdate = systemFunctionRepository.findAll().size();

        // Update the systemFunction using partial update
        SystemFunction partialUpdatedSystemFunction = new SystemFunction();
        partialUpdatedSystemFunction.setId(systemFunction.getId());

        partialUpdatedSystemFunction.name(UPDATED_NAME).orderBy(UPDATED_ORDER_BY).defaultAction(UPDATED_DEFAULT_ACTION);

        restSystemFunctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemFunction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemFunction))
            )
            .andExpect(status().isOk());

        // Validate the SystemFunction in the database
        List<SystemFunction> systemFunctionList = systemFunctionRepository.findAll();
        assertThat(systemFunctionList).hasSize(databaseSizeBeforeUpdate);
        SystemFunction testSystemFunction = systemFunctionList.get(systemFunctionList.size() - 1);
        assertThat(testSystemFunction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemFunction.getOrderBy()).isEqualTo(UPDATED_ORDER_BY);
        assertThat(testSystemFunction.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testSystemFunction.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testSystemFunction.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testSystemFunction.getDefaultAction()).isEqualTo(UPDATED_DEFAULT_ACTION);
    }

    @Test
    @Transactional
    void fullUpdateSystemFunctionWithPatch() throws Exception {
        // Initialize the database
        systemFunctionRepository.saveAndFlush(systemFunction);

        int databaseSizeBeforeUpdate = systemFunctionRepository.findAll().size();

        // Update the systemFunction using partial update
        SystemFunction partialUpdatedSystemFunction = new SystemFunction();
        partialUpdatedSystemFunction.setId(systemFunction.getId());

        partialUpdatedSystemFunction
            .name(UPDATED_NAME)
            .orderBy(UPDATED_ORDER_BY)
            .icon(UPDATED_ICON)
            .note(UPDATED_NOTE)
            .isActive(UPDATED_IS_ACTIVE)
            .defaultAction(UPDATED_DEFAULT_ACTION);

        restSystemFunctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemFunction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemFunction))
            )
            .andExpect(status().isOk());

        // Validate the SystemFunction in the database
        List<SystemFunction> systemFunctionList = systemFunctionRepository.findAll();
        assertThat(systemFunctionList).hasSize(databaseSizeBeforeUpdate);
        SystemFunction testSystemFunction = systemFunctionList.get(systemFunctionList.size() - 1);
        assertThat(testSystemFunction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemFunction.getOrderBy()).isEqualTo(UPDATED_ORDER_BY);
        assertThat(testSystemFunction.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testSystemFunction.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testSystemFunction.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testSystemFunction.getDefaultAction()).isEqualTo(UPDATED_DEFAULT_ACTION);
    }

    @Test
    @Transactional
    void patchNonExistingSystemFunction() throws Exception {
        int databaseSizeBeforeUpdate = systemFunctionRepository.findAll().size();
        systemFunction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemFunctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemFunction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemFunction))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemFunction in the database
        List<SystemFunction> systemFunctionList = systemFunctionRepository.findAll();
        assertThat(systemFunctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemFunction() throws Exception {
        int databaseSizeBeforeUpdate = systemFunctionRepository.findAll().size();
        systemFunction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemFunctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemFunction))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemFunction in the database
        List<SystemFunction> systemFunctionList = systemFunctionRepository.findAll();
        assertThat(systemFunctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemFunction() throws Exception {
        int databaseSizeBeforeUpdate = systemFunctionRepository.findAll().size();
        systemFunction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemFunctionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(systemFunction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemFunction in the database
        List<SystemFunction> systemFunctionList = systemFunctionRepository.findAll();
        assertThat(systemFunctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemFunction() throws Exception {
        // Initialize the database
        systemFunctionRepository.saveAndFlush(systemFunction);

        int databaseSizeBeforeDelete = systemFunctionRepository.findAll().size();

        // Delete the systemFunction
        restSystemFunctionMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemFunction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SystemFunction> systemFunctionList = systemFunctionRepository.findAll();
        assertThat(systemFunctionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
