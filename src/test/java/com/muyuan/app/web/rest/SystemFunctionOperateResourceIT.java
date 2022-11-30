package com.muyuan.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.muyuan.app.IntegrationTest;
import com.muyuan.app.domain.SystemFunctionOperate;
import com.muyuan.app.repository.SystemFunctionOperateRepository;
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
 * Integration tests for the {@link SystemFunctionOperateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemFunctionOperateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER_BY = 1;
    private static final Integer UPDATED_ORDER_BY = 2;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/system-function-operates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SystemFunctionOperateRepository systemFunctionOperateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemFunctionOperateMockMvc;

    private SystemFunctionOperate systemFunctionOperate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemFunctionOperate createEntity(EntityManager em) {
        SystemFunctionOperate systemFunctionOperate = new SystemFunctionOperate()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .orderBy(DEFAULT_ORDER_BY)
            .note(DEFAULT_NOTE)
            .isActive(DEFAULT_IS_ACTIVE);
        return systemFunctionOperate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemFunctionOperate createUpdatedEntity(EntityManager em) {
        SystemFunctionOperate systemFunctionOperate = new SystemFunctionOperate()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .orderBy(UPDATED_ORDER_BY)
            .note(UPDATED_NOTE)
            .isActive(UPDATED_IS_ACTIVE);
        return systemFunctionOperate;
    }

    @BeforeEach
    public void initTest() {
        systemFunctionOperate = createEntity(em);
    }

    @Test
    @Transactional
    void createSystemFunctionOperate() throws Exception {
        int databaseSizeBeforeCreate = systemFunctionOperateRepository.findAll().size();
        // Create the SystemFunctionOperate
        restSystemFunctionOperateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemFunctionOperate))
            )
            .andExpect(status().isCreated());

        // Validate the SystemFunctionOperate in the database
        List<SystemFunctionOperate> systemFunctionOperateList = systemFunctionOperateRepository.findAll();
        assertThat(systemFunctionOperateList).hasSize(databaseSizeBeforeCreate + 1);
        SystemFunctionOperate testSystemFunctionOperate = systemFunctionOperateList.get(systemFunctionOperateList.size() - 1);
        assertThat(testSystemFunctionOperate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSystemFunctionOperate.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSystemFunctionOperate.getOrderBy()).isEqualTo(DEFAULT_ORDER_BY);
        assertThat(testSystemFunctionOperate.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testSystemFunctionOperate.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createSystemFunctionOperateWithExistingId() throws Exception {
        // Create the SystemFunctionOperate with an existing ID
        systemFunctionOperate.setId(1L);

        int databaseSizeBeforeCreate = systemFunctionOperateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemFunctionOperateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemFunctionOperate))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemFunctionOperate in the database
        List<SystemFunctionOperate> systemFunctionOperateList = systemFunctionOperateRepository.findAll();
        assertThat(systemFunctionOperateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemFunctionOperateRepository.findAll().size();
        // set the field null
        systemFunctionOperate.setName(null);

        // Create the SystemFunctionOperate, which fails.

        restSystemFunctionOperateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemFunctionOperate))
            )
            .andExpect(status().isBadRequest());

        List<SystemFunctionOperate> systemFunctionOperateList = systemFunctionOperateRepository.findAll();
        assertThat(systemFunctionOperateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemFunctionOperateRepository.findAll().size();
        // set the field null
        systemFunctionOperate.setCode(null);

        // Create the SystemFunctionOperate, which fails.

        restSystemFunctionOperateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemFunctionOperate))
            )
            .andExpect(status().isBadRequest());

        List<SystemFunctionOperate> systemFunctionOperateList = systemFunctionOperateRepository.findAll();
        assertThat(systemFunctionOperateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemFunctionOperates() throws Exception {
        // Initialize the database
        systemFunctionOperateRepository.saveAndFlush(systemFunctionOperate);

        // Get all the systemFunctionOperateList
        restSystemFunctionOperateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemFunctionOperate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].orderBy").value(hasItem(DEFAULT_ORDER_BY)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getSystemFunctionOperate() throws Exception {
        // Initialize the database
        systemFunctionOperateRepository.saveAndFlush(systemFunctionOperate);

        // Get the systemFunctionOperate
        restSystemFunctionOperateMockMvc
            .perform(get(ENTITY_API_URL_ID, systemFunctionOperate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemFunctionOperate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.orderBy").value(DEFAULT_ORDER_BY))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSystemFunctionOperate() throws Exception {
        // Get the systemFunctionOperate
        restSystemFunctionOperateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemFunctionOperate() throws Exception {
        // Initialize the database
        systemFunctionOperateRepository.saveAndFlush(systemFunctionOperate);

        int databaseSizeBeforeUpdate = systemFunctionOperateRepository.findAll().size();

        // Update the systemFunctionOperate
        SystemFunctionOperate updatedSystemFunctionOperate = systemFunctionOperateRepository.findById(systemFunctionOperate.getId()).get();
        // Disconnect from session so that the updates on updatedSystemFunctionOperate are not directly saved in db
        em.detach(updatedSystemFunctionOperate);
        updatedSystemFunctionOperate
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .orderBy(UPDATED_ORDER_BY)
            .note(UPDATED_NOTE)
            .isActive(UPDATED_IS_ACTIVE);

        restSystemFunctionOperateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSystemFunctionOperate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSystemFunctionOperate))
            )
            .andExpect(status().isOk());

        // Validate the SystemFunctionOperate in the database
        List<SystemFunctionOperate> systemFunctionOperateList = systemFunctionOperateRepository.findAll();
        assertThat(systemFunctionOperateList).hasSize(databaseSizeBeforeUpdate);
        SystemFunctionOperate testSystemFunctionOperate = systemFunctionOperateList.get(systemFunctionOperateList.size() - 1);
        assertThat(testSystemFunctionOperate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemFunctionOperate.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSystemFunctionOperate.getOrderBy()).isEqualTo(UPDATED_ORDER_BY);
        assertThat(testSystemFunctionOperate.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testSystemFunctionOperate.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingSystemFunctionOperate() throws Exception {
        int databaseSizeBeforeUpdate = systemFunctionOperateRepository.findAll().size();
        systemFunctionOperate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemFunctionOperateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemFunctionOperate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemFunctionOperate))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemFunctionOperate in the database
        List<SystemFunctionOperate> systemFunctionOperateList = systemFunctionOperateRepository.findAll();
        assertThat(systemFunctionOperateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemFunctionOperate() throws Exception {
        int databaseSizeBeforeUpdate = systemFunctionOperateRepository.findAll().size();
        systemFunctionOperate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemFunctionOperateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemFunctionOperate))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemFunctionOperate in the database
        List<SystemFunctionOperate> systemFunctionOperateList = systemFunctionOperateRepository.findAll();
        assertThat(systemFunctionOperateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemFunctionOperate() throws Exception {
        int databaseSizeBeforeUpdate = systemFunctionOperateRepository.findAll().size();
        systemFunctionOperate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemFunctionOperateMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemFunctionOperate))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemFunctionOperate in the database
        List<SystemFunctionOperate> systemFunctionOperateList = systemFunctionOperateRepository.findAll();
        assertThat(systemFunctionOperateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemFunctionOperateWithPatch() throws Exception {
        // Initialize the database
        systemFunctionOperateRepository.saveAndFlush(systemFunctionOperate);

        int databaseSizeBeforeUpdate = systemFunctionOperateRepository.findAll().size();

        // Update the systemFunctionOperate using partial update
        SystemFunctionOperate partialUpdatedSystemFunctionOperate = new SystemFunctionOperate();
        partialUpdatedSystemFunctionOperate.setId(systemFunctionOperate.getId());

        partialUpdatedSystemFunctionOperate.name(UPDATED_NAME).code(UPDATED_CODE);

        restSystemFunctionOperateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemFunctionOperate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemFunctionOperate))
            )
            .andExpect(status().isOk());

        // Validate the SystemFunctionOperate in the database
        List<SystemFunctionOperate> systemFunctionOperateList = systemFunctionOperateRepository.findAll();
        assertThat(systemFunctionOperateList).hasSize(databaseSizeBeforeUpdate);
        SystemFunctionOperate testSystemFunctionOperate = systemFunctionOperateList.get(systemFunctionOperateList.size() - 1);
        assertThat(testSystemFunctionOperate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemFunctionOperate.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSystemFunctionOperate.getOrderBy()).isEqualTo(DEFAULT_ORDER_BY);
        assertThat(testSystemFunctionOperate.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testSystemFunctionOperate.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateSystemFunctionOperateWithPatch() throws Exception {
        // Initialize the database
        systemFunctionOperateRepository.saveAndFlush(systemFunctionOperate);

        int databaseSizeBeforeUpdate = systemFunctionOperateRepository.findAll().size();

        // Update the systemFunctionOperate using partial update
        SystemFunctionOperate partialUpdatedSystemFunctionOperate = new SystemFunctionOperate();
        partialUpdatedSystemFunctionOperate.setId(systemFunctionOperate.getId());

        partialUpdatedSystemFunctionOperate
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .orderBy(UPDATED_ORDER_BY)
            .note(UPDATED_NOTE)
            .isActive(UPDATED_IS_ACTIVE);

        restSystemFunctionOperateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemFunctionOperate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemFunctionOperate))
            )
            .andExpect(status().isOk());

        // Validate the SystemFunctionOperate in the database
        List<SystemFunctionOperate> systemFunctionOperateList = systemFunctionOperateRepository.findAll();
        assertThat(systemFunctionOperateList).hasSize(databaseSizeBeforeUpdate);
        SystemFunctionOperate testSystemFunctionOperate = systemFunctionOperateList.get(systemFunctionOperateList.size() - 1);
        assertThat(testSystemFunctionOperate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemFunctionOperate.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSystemFunctionOperate.getOrderBy()).isEqualTo(UPDATED_ORDER_BY);
        assertThat(testSystemFunctionOperate.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testSystemFunctionOperate.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingSystemFunctionOperate() throws Exception {
        int databaseSizeBeforeUpdate = systemFunctionOperateRepository.findAll().size();
        systemFunctionOperate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemFunctionOperateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemFunctionOperate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemFunctionOperate))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemFunctionOperate in the database
        List<SystemFunctionOperate> systemFunctionOperateList = systemFunctionOperateRepository.findAll();
        assertThat(systemFunctionOperateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemFunctionOperate() throws Exception {
        int databaseSizeBeforeUpdate = systemFunctionOperateRepository.findAll().size();
        systemFunctionOperate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemFunctionOperateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemFunctionOperate))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemFunctionOperate in the database
        List<SystemFunctionOperate> systemFunctionOperateList = systemFunctionOperateRepository.findAll();
        assertThat(systemFunctionOperateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemFunctionOperate() throws Exception {
        int databaseSizeBeforeUpdate = systemFunctionOperateRepository.findAll().size();
        systemFunctionOperate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemFunctionOperateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemFunctionOperate))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemFunctionOperate in the database
        List<SystemFunctionOperate> systemFunctionOperateList = systemFunctionOperateRepository.findAll();
        assertThat(systemFunctionOperateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemFunctionOperate() throws Exception {
        // Initialize the database
        systemFunctionOperateRepository.saveAndFlush(systemFunctionOperate);

        int databaseSizeBeforeDelete = systemFunctionOperateRepository.findAll().size();

        // Delete the systemFunctionOperate
        restSystemFunctionOperateMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemFunctionOperate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SystemFunctionOperate> systemFunctionOperateList = systemFunctionOperateRepository.findAll();
        assertThat(systemFunctionOperateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
