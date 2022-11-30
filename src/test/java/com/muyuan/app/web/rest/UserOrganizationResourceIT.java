package com.muyuan.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.muyuan.app.IntegrationTest;
import com.muyuan.app.domain.UserOrganization;
import com.muyuan.app.repository.UserOrganizationRepository;
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
 * Integration tests for the {@link UserOrganizationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserOrganizationResourceIT {

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-organizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserOrganizationRepository userOrganizationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserOrganizationMockMvc;

    private UserOrganization userOrganization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserOrganization createEntity(EntityManager em) {
        UserOrganization userOrganization = new UserOrganization().note(DEFAULT_NOTE);
        return userOrganization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserOrganization createUpdatedEntity(EntityManager em) {
        UserOrganization userOrganization = new UserOrganization().note(UPDATED_NOTE);
        return userOrganization;
    }

    @BeforeEach
    public void initTest() {
        userOrganization = createEntity(em);
    }

    @Test
    @Transactional
    void createUserOrganization() throws Exception {
        int databaseSizeBeforeCreate = userOrganizationRepository.findAll().size();
        // Create the UserOrganization
        restUserOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userOrganization))
            )
            .andExpect(status().isCreated());

        // Validate the UserOrganization in the database
        List<UserOrganization> userOrganizationList = userOrganizationRepository.findAll();
        assertThat(userOrganizationList).hasSize(databaseSizeBeforeCreate + 1);
        UserOrganization testUserOrganization = userOrganizationList.get(userOrganizationList.size() - 1);
        assertThat(testUserOrganization.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createUserOrganizationWithExistingId() throws Exception {
        // Create the UserOrganization with an existing ID
        userOrganization.setId(1L);

        int databaseSizeBeforeCreate = userOrganizationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userOrganization))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserOrganization in the database
        List<UserOrganization> userOrganizationList = userOrganizationRepository.findAll();
        assertThat(userOrganizationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserOrganizations() throws Exception {
        // Initialize the database
        userOrganizationRepository.saveAndFlush(userOrganization);

        // Get all the userOrganizationList
        restUserOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userOrganization.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getUserOrganization() throws Exception {
        // Initialize the database
        userOrganizationRepository.saveAndFlush(userOrganization);

        // Get the userOrganization
        restUserOrganizationMockMvc
            .perform(get(ENTITY_API_URL_ID, userOrganization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userOrganization.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingUserOrganization() throws Exception {
        // Get the userOrganization
        restUserOrganizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserOrganization() throws Exception {
        // Initialize the database
        userOrganizationRepository.saveAndFlush(userOrganization);

        int databaseSizeBeforeUpdate = userOrganizationRepository.findAll().size();

        // Update the userOrganization
        UserOrganization updatedUserOrganization = userOrganizationRepository.findById(userOrganization.getId()).get();
        // Disconnect from session so that the updates on updatedUserOrganization are not directly saved in db
        em.detach(updatedUserOrganization);
        updatedUserOrganization.note(UPDATED_NOTE);

        restUserOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserOrganization.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserOrganization))
            )
            .andExpect(status().isOk());

        // Validate the UserOrganization in the database
        List<UserOrganization> userOrganizationList = userOrganizationRepository.findAll();
        assertThat(userOrganizationList).hasSize(databaseSizeBeforeUpdate);
        UserOrganization testUserOrganization = userOrganizationList.get(userOrganizationList.size() - 1);
        assertThat(testUserOrganization.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingUserOrganization() throws Exception {
        int databaseSizeBeforeUpdate = userOrganizationRepository.findAll().size();
        userOrganization.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userOrganization.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userOrganization))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserOrganization in the database
        List<UserOrganization> userOrganizationList = userOrganizationRepository.findAll();
        assertThat(userOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserOrganization() throws Exception {
        int databaseSizeBeforeUpdate = userOrganizationRepository.findAll().size();
        userOrganization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userOrganization))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserOrganization in the database
        List<UserOrganization> userOrganizationList = userOrganizationRepository.findAll();
        assertThat(userOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserOrganization() throws Exception {
        int databaseSizeBeforeUpdate = userOrganizationRepository.findAll().size();
        userOrganization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userOrganization))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserOrganization in the database
        List<UserOrganization> userOrganizationList = userOrganizationRepository.findAll();
        assertThat(userOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserOrganizationWithPatch() throws Exception {
        // Initialize the database
        userOrganizationRepository.saveAndFlush(userOrganization);

        int databaseSizeBeforeUpdate = userOrganizationRepository.findAll().size();

        // Update the userOrganization using partial update
        UserOrganization partialUpdatedUserOrganization = new UserOrganization();
        partialUpdatedUserOrganization.setId(userOrganization.getId());

        partialUpdatedUserOrganization.note(UPDATED_NOTE);

        restUserOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserOrganization))
            )
            .andExpect(status().isOk());

        // Validate the UserOrganization in the database
        List<UserOrganization> userOrganizationList = userOrganizationRepository.findAll();
        assertThat(userOrganizationList).hasSize(databaseSizeBeforeUpdate);
        UserOrganization testUserOrganization = userOrganizationList.get(userOrganizationList.size() - 1);
        assertThat(testUserOrganization.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateUserOrganizationWithPatch() throws Exception {
        // Initialize the database
        userOrganizationRepository.saveAndFlush(userOrganization);

        int databaseSizeBeforeUpdate = userOrganizationRepository.findAll().size();

        // Update the userOrganization using partial update
        UserOrganization partialUpdatedUserOrganization = new UserOrganization();
        partialUpdatedUserOrganization.setId(userOrganization.getId());

        partialUpdatedUserOrganization.note(UPDATED_NOTE);

        restUserOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserOrganization))
            )
            .andExpect(status().isOk());

        // Validate the UserOrganization in the database
        List<UserOrganization> userOrganizationList = userOrganizationRepository.findAll();
        assertThat(userOrganizationList).hasSize(databaseSizeBeforeUpdate);
        UserOrganization testUserOrganization = userOrganizationList.get(userOrganizationList.size() - 1);
        assertThat(testUserOrganization.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingUserOrganization() throws Exception {
        int databaseSizeBeforeUpdate = userOrganizationRepository.findAll().size();
        userOrganization.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userOrganization))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserOrganization in the database
        List<UserOrganization> userOrganizationList = userOrganizationRepository.findAll();
        assertThat(userOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserOrganization() throws Exception {
        int databaseSizeBeforeUpdate = userOrganizationRepository.findAll().size();
        userOrganization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userOrganization))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserOrganization in the database
        List<UserOrganization> userOrganizationList = userOrganizationRepository.findAll();
        assertThat(userOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserOrganization() throws Exception {
        int databaseSizeBeforeUpdate = userOrganizationRepository.findAll().size();
        userOrganization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userOrganization))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserOrganization in the database
        List<UserOrganization> userOrganizationList = userOrganizationRepository.findAll();
        assertThat(userOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserOrganization() throws Exception {
        // Initialize the database
        userOrganizationRepository.saveAndFlush(userOrganization);

        int databaseSizeBeforeDelete = userOrganizationRepository.findAll().size();

        // Delete the userOrganization
        restUserOrganizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, userOrganization.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserOrganization> userOrganizationList = userOrganizationRepository.findAll();
        assertThat(userOrganizationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
