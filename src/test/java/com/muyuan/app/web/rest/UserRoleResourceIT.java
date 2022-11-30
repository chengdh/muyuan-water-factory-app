package com.muyuan.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.muyuan.app.IntegrationTest;
import com.muyuan.app.domain.UserRole;
import com.muyuan.app.repository.UserRoleRepository;
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
 * Integration tests for the {@link UserRoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserRoleResourceIT {

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserRoleMockMvc;

    private UserRole userRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRole createEntity(EntityManager em) {
        UserRole userRole = new UserRole().note(DEFAULT_NOTE);
        return userRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRole createUpdatedEntity(EntityManager em) {
        UserRole userRole = new UserRole().note(UPDATED_NOTE);
        return userRole;
    }

    @BeforeEach
    public void initTest() {
        userRole = createEntity(em);
    }

    @Test
    @Transactional
    void createUserRole() throws Exception {
        int databaseSizeBeforeCreate = userRoleRepository.findAll().size();
        // Create the UserRole
        restUserRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userRole)))
            .andExpect(status().isCreated());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeCreate + 1);
        UserRole testUserRole = userRoleList.get(userRoleList.size() - 1);
        assertThat(testUserRole.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createUserRoleWithExistingId() throws Exception {
        // Create the UserRole with an existing ID
        userRole.setId(1L);

        int databaseSizeBeforeCreate = userRoleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userRole)))
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserRoles() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList
        restUserRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getUserRole() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

        // Get the userRole
        restUserRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, userRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userRole.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingUserRole() throws Exception {
        // Get the userRole
        restUserRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserRole() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

        int databaseSizeBeforeUpdate = userRoleRepository.findAll().size();

        // Update the userRole
        UserRole updatedUserRole = userRoleRepository.findById(userRole.getId()).get();
        // Disconnect from session so that the updates on updatedUserRole are not directly saved in db
        em.detach(updatedUserRole);
        updatedUserRole.note(UPDATED_NOTE);

        restUserRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserRole))
            )
            .andExpect(status().isOk());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeUpdate);
        UserRole testUserRole = userRoleList.get(userRoleList.size() - 1);
        assertThat(testUserRole.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingUserRole() throws Exception {
        int databaseSizeBeforeUpdate = userRoleRepository.findAll().size();
        userRole.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserRole() throws Exception {
        int databaseSizeBeforeUpdate = userRoleRepository.findAll().size();
        userRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserRole() throws Exception {
        int databaseSizeBeforeUpdate = userRoleRepository.findAll().size();
        userRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserRoleWithPatch() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

        int databaseSizeBeforeUpdate = userRoleRepository.findAll().size();

        // Update the userRole using partial update
        UserRole partialUpdatedUserRole = new UserRole();
        partialUpdatedUserRole.setId(userRole.getId());

        restUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserRole))
            )
            .andExpect(status().isOk());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeUpdate);
        UserRole testUserRole = userRoleList.get(userRoleList.size() - 1);
        assertThat(testUserRole.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateUserRoleWithPatch() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

        int databaseSizeBeforeUpdate = userRoleRepository.findAll().size();

        // Update the userRole using partial update
        UserRole partialUpdatedUserRole = new UserRole();
        partialUpdatedUserRole.setId(userRole.getId());

        partialUpdatedUserRole.note(UPDATED_NOTE);

        restUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserRole))
            )
            .andExpect(status().isOk());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeUpdate);
        UserRole testUserRole = userRoleList.get(userRoleList.size() - 1);
        assertThat(testUserRole.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingUserRole() throws Exception {
        int databaseSizeBeforeUpdate = userRoleRepository.findAll().size();
        userRole.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserRole() throws Exception {
        int databaseSizeBeforeUpdate = userRoleRepository.findAll().size();
        userRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserRole() throws Exception {
        int databaseSizeBeforeUpdate = userRoleRepository.findAll().size();
        userRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserRole() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

        int databaseSizeBeforeDelete = userRoleRepository.findAll().size();

        // Delete the userRole
        restUserRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, userRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
