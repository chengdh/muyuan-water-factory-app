package com.muyuan.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.muyuan.app.IntegrationTest;
import com.muyuan.app.domain.SystemCategory;
import com.muyuan.app.repository.SystemCategoryRepository;
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
 * Integration tests for the {@link SystemCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemCategoryResourceIT {

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

    private static final String ENTITY_API_URL = "/api/system-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SystemCategoryRepository systemCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemCategoryMockMvc;

    private SystemCategory systemCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemCategory createEntity(EntityManager em) {
        SystemCategory systemCategory = new SystemCategory()
            .name(DEFAULT_NAME)
            .orderBy(DEFAULT_ORDER_BY)
            .icon(DEFAULT_ICON)
            .note(DEFAULT_NOTE)
            .isActive(DEFAULT_IS_ACTIVE);
        return systemCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemCategory createUpdatedEntity(EntityManager em) {
        SystemCategory systemCategory = new SystemCategory()
            .name(UPDATED_NAME)
            .orderBy(UPDATED_ORDER_BY)
            .icon(UPDATED_ICON)
            .note(UPDATED_NOTE)
            .isActive(UPDATED_IS_ACTIVE);
        return systemCategory;
    }

    @BeforeEach
    public void initTest() {
        systemCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createSystemCategory() throws Exception {
        int databaseSizeBeforeCreate = systemCategoryRepository.findAll().size();
        // Create the SystemCategory
        restSystemCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemCategory))
            )
            .andExpect(status().isCreated());

        // Validate the SystemCategory in the database
        List<SystemCategory> systemCategoryList = systemCategoryRepository.findAll();
        assertThat(systemCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        SystemCategory testSystemCategory = systemCategoryList.get(systemCategoryList.size() - 1);
        assertThat(testSystemCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSystemCategory.getOrderBy()).isEqualTo(DEFAULT_ORDER_BY);
        assertThat(testSystemCategory.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testSystemCategory.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testSystemCategory.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createSystemCategoryWithExistingId() throws Exception {
        // Create the SystemCategory with an existing ID
        systemCategory.setId(1L);

        int databaseSizeBeforeCreate = systemCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemCategory in the database
        List<SystemCategory> systemCategoryList = systemCategoryRepository.findAll();
        assertThat(systemCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemCategoryRepository.findAll().size();
        // set the field null
        systemCategory.setName(null);

        // Create the SystemCategory, which fails.

        restSystemCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemCategory))
            )
            .andExpect(status().isBadRequest());

        List<SystemCategory> systemCategoryList = systemCategoryRepository.findAll();
        assertThat(systemCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemCategories() throws Exception {
        // Initialize the database
        systemCategoryRepository.saveAndFlush(systemCategory);

        // Get all the systemCategoryList
        restSystemCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].orderBy").value(hasItem(DEFAULT_ORDER_BY)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getSystemCategory() throws Exception {
        // Initialize the database
        systemCategoryRepository.saveAndFlush(systemCategory);

        // Get the systemCategory
        restSystemCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, systemCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.orderBy").value(DEFAULT_ORDER_BY))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSystemCategory() throws Exception {
        // Get the systemCategory
        restSystemCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemCategory() throws Exception {
        // Initialize the database
        systemCategoryRepository.saveAndFlush(systemCategory);

        int databaseSizeBeforeUpdate = systemCategoryRepository.findAll().size();

        // Update the systemCategory
        SystemCategory updatedSystemCategory = systemCategoryRepository.findById(systemCategory.getId()).get();
        // Disconnect from session so that the updates on updatedSystemCategory are not directly saved in db
        em.detach(updatedSystemCategory);
        updatedSystemCategory
            .name(UPDATED_NAME)
            .orderBy(UPDATED_ORDER_BY)
            .icon(UPDATED_ICON)
            .note(UPDATED_NOTE)
            .isActive(UPDATED_IS_ACTIVE);

        restSystemCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSystemCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSystemCategory))
            )
            .andExpect(status().isOk());

        // Validate the SystemCategory in the database
        List<SystemCategory> systemCategoryList = systemCategoryRepository.findAll();
        assertThat(systemCategoryList).hasSize(databaseSizeBeforeUpdate);
        SystemCategory testSystemCategory = systemCategoryList.get(systemCategoryList.size() - 1);
        assertThat(testSystemCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemCategory.getOrderBy()).isEqualTo(UPDATED_ORDER_BY);
        assertThat(testSystemCategory.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testSystemCategory.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testSystemCategory.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingSystemCategory() throws Exception {
        int databaseSizeBeforeUpdate = systemCategoryRepository.findAll().size();
        systemCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemCategory in the database
        List<SystemCategory> systemCategoryList = systemCategoryRepository.findAll();
        assertThat(systemCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemCategory() throws Exception {
        int databaseSizeBeforeUpdate = systemCategoryRepository.findAll().size();
        systemCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemCategory in the database
        List<SystemCategory> systemCategoryList = systemCategoryRepository.findAll();
        assertThat(systemCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemCategory() throws Exception {
        int databaseSizeBeforeUpdate = systemCategoryRepository.findAll().size();
        systemCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemCategory in the database
        List<SystemCategory> systemCategoryList = systemCategoryRepository.findAll();
        assertThat(systemCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemCategoryWithPatch() throws Exception {
        // Initialize the database
        systemCategoryRepository.saveAndFlush(systemCategory);

        int databaseSizeBeforeUpdate = systemCategoryRepository.findAll().size();

        // Update the systemCategory using partial update
        SystemCategory partialUpdatedSystemCategory = new SystemCategory();
        partialUpdatedSystemCategory.setId(systemCategory.getId());

        partialUpdatedSystemCategory.name(UPDATED_NAME).note(UPDATED_NOTE);

        restSystemCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemCategory))
            )
            .andExpect(status().isOk());

        // Validate the SystemCategory in the database
        List<SystemCategory> systemCategoryList = systemCategoryRepository.findAll();
        assertThat(systemCategoryList).hasSize(databaseSizeBeforeUpdate);
        SystemCategory testSystemCategory = systemCategoryList.get(systemCategoryList.size() - 1);
        assertThat(testSystemCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemCategory.getOrderBy()).isEqualTo(DEFAULT_ORDER_BY);
        assertThat(testSystemCategory.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testSystemCategory.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testSystemCategory.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateSystemCategoryWithPatch() throws Exception {
        // Initialize the database
        systemCategoryRepository.saveAndFlush(systemCategory);

        int databaseSizeBeforeUpdate = systemCategoryRepository.findAll().size();

        // Update the systemCategory using partial update
        SystemCategory partialUpdatedSystemCategory = new SystemCategory();
        partialUpdatedSystemCategory.setId(systemCategory.getId());

        partialUpdatedSystemCategory
            .name(UPDATED_NAME)
            .orderBy(UPDATED_ORDER_BY)
            .icon(UPDATED_ICON)
            .note(UPDATED_NOTE)
            .isActive(UPDATED_IS_ACTIVE);

        restSystemCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemCategory))
            )
            .andExpect(status().isOk());

        // Validate the SystemCategory in the database
        List<SystemCategory> systemCategoryList = systemCategoryRepository.findAll();
        assertThat(systemCategoryList).hasSize(databaseSizeBeforeUpdate);
        SystemCategory testSystemCategory = systemCategoryList.get(systemCategoryList.size() - 1);
        assertThat(testSystemCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemCategory.getOrderBy()).isEqualTo(UPDATED_ORDER_BY);
        assertThat(testSystemCategory.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testSystemCategory.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testSystemCategory.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingSystemCategory() throws Exception {
        int databaseSizeBeforeUpdate = systemCategoryRepository.findAll().size();
        systemCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemCategory in the database
        List<SystemCategory> systemCategoryList = systemCategoryRepository.findAll();
        assertThat(systemCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemCategory() throws Exception {
        int databaseSizeBeforeUpdate = systemCategoryRepository.findAll().size();
        systemCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemCategory in the database
        List<SystemCategory> systemCategoryList = systemCategoryRepository.findAll();
        assertThat(systemCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemCategory() throws Exception {
        int databaseSizeBeforeUpdate = systemCategoryRepository.findAll().size();
        systemCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(systemCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemCategory in the database
        List<SystemCategory> systemCategoryList = systemCategoryRepository.findAll();
        assertThat(systemCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemCategory() throws Exception {
        // Initialize the database
        systemCategoryRepository.saveAndFlush(systemCategory);

        int databaseSizeBeforeDelete = systemCategoryRepository.findAll().size();

        // Delete the systemCategory
        restSystemCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SystemCategory> systemCategoryList = systemCategoryRepository.findAll();
        assertThat(systemCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
