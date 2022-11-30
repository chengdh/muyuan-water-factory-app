package com.muyuan.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.muyuan.app.IntegrationTest;
import com.muyuan.app.domain.UserProxy;
import com.muyuan.app.repository.UserProxyRepository;
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
 * Integration tests for the {@link UserProxyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserProxyResourceIT {

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-proxies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserProxyRepository userProxyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserProxyMockMvc;

    private UserProxy userProxy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProxy createEntity(EntityManager em) {
        UserProxy userProxy = new UserProxy().isActive(DEFAULT_IS_ACTIVE).note(DEFAULT_NOTE);
        return userProxy;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProxy createUpdatedEntity(EntityManager em) {
        UserProxy userProxy = new UserProxy().isActive(UPDATED_IS_ACTIVE).note(UPDATED_NOTE);
        return userProxy;
    }

    @BeforeEach
    public void initTest() {
        userProxy = createEntity(em);
    }

    @Test
    @Transactional
    void createUserProxy() throws Exception {
        int databaseSizeBeforeCreate = userProxyRepository.findAll().size();
        // Create the UserProxy
        restUserProxyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProxy)))
            .andExpect(status().isCreated());

        // Validate the UserProxy in the database
        List<UserProxy> userProxyList = userProxyRepository.findAll();
        assertThat(userProxyList).hasSize(databaseSizeBeforeCreate + 1);
        UserProxy testUserProxy = userProxyList.get(userProxyList.size() - 1);
        assertThat(testUserProxy.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testUserProxy.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createUserProxyWithExistingId() throws Exception {
        // Create the UserProxy with an existing ID
        userProxy.setId(1L);

        int databaseSizeBeforeCreate = userProxyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserProxyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProxy)))
            .andExpect(status().isBadRequest());

        // Validate the UserProxy in the database
        List<UserProxy> userProxyList = userProxyRepository.findAll();
        assertThat(userProxyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserProxies() throws Exception {
        // Initialize the database
        userProxyRepository.saveAndFlush(userProxy);

        // Get all the userProxyList
        restUserProxyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProxy.getId().intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getUserProxy() throws Exception {
        // Initialize the database
        userProxyRepository.saveAndFlush(userProxy);

        // Get the userProxy
        restUserProxyMockMvc
            .perform(get(ENTITY_API_URL_ID, userProxy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userProxy.getId().intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingUserProxy() throws Exception {
        // Get the userProxy
        restUserProxyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserProxy() throws Exception {
        // Initialize the database
        userProxyRepository.saveAndFlush(userProxy);

        int databaseSizeBeforeUpdate = userProxyRepository.findAll().size();

        // Update the userProxy
        UserProxy updatedUserProxy = userProxyRepository.findById(userProxy.getId()).get();
        // Disconnect from session so that the updates on updatedUserProxy are not directly saved in db
        em.detach(updatedUserProxy);
        updatedUserProxy.isActive(UPDATED_IS_ACTIVE).note(UPDATED_NOTE);

        restUserProxyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserProxy.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserProxy))
            )
            .andExpect(status().isOk());

        // Validate the UserProxy in the database
        List<UserProxy> userProxyList = userProxyRepository.findAll();
        assertThat(userProxyList).hasSize(databaseSizeBeforeUpdate);
        UserProxy testUserProxy = userProxyList.get(userProxyList.size() - 1);
        assertThat(testUserProxy.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testUserProxy.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingUserProxy() throws Exception {
        int databaseSizeBeforeUpdate = userProxyRepository.findAll().size();
        userProxy.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProxyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userProxy.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userProxy))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProxy in the database
        List<UserProxy> userProxyList = userProxyRepository.findAll();
        assertThat(userProxyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserProxy() throws Exception {
        int databaseSizeBeforeUpdate = userProxyRepository.findAll().size();
        userProxy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProxyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userProxy))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProxy in the database
        List<UserProxy> userProxyList = userProxyRepository.findAll();
        assertThat(userProxyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserProxy() throws Exception {
        int databaseSizeBeforeUpdate = userProxyRepository.findAll().size();
        userProxy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProxyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProxy)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserProxy in the database
        List<UserProxy> userProxyList = userProxyRepository.findAll();
        assertThat(userProxyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserProxyWithPatch() throws Exception {
        // Initialize the database
        userProxyRepository.saveAndFlush(userProxy);

        int databaseSizeBeforeUpdate = userProxyRepository.findAll().size();

        // Update the userProxy using partial update
        UserProxy partialUpdatedUserProxy = new UserProxy();
        partialUpdatedUserProxy.setId(userProxy.getId());

        restUserProxyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserProxy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProxy))
            )
            .andExpect(status().isOk());

        // Validate the UserProxy in the database
        List<UserProxy> userProxyList = userProxyRepository.findAll();
        assertThat(userProxyList).hasSize(databaseSizeBeforeUpdate);
        UserProxy testUserProxy = userProxyList.get(userProxyList.size() - 1);
        assertThat(testUserProxy.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testUserProxy.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateUserProxyWithPatch() throws Exception {
        // Initialize the database
        userProxyRepository.saveAndFlush(userProxy);

        int databaseSizeBeforeUpdate = userProxyRepository.findAll().size();

        // Update the userProxy using partial update
        UserProxy partialUpdatedUserProxy = new UserProxy();
        partialUpdatedUserProxy.setId(userProxy.getId());

        partialUpdatedUserProxy.isActive(UPDATED_IS_ACTIVE).note(UPDATED_NOTE);

        restUserProxyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserProxy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProxy))
            )
            .andExpect(status().isOk());

        // Validate the UserProxy in the database
        List<UserProxy> userProxyList = userProxyRepository.findAll();
        assertThat(userProxyList).hasSize(databaseSizeBeforeUpdate);
        UserProxy testUserProxy = userProxyList.get(userProxyList.size() - 1);
        assertThat(testUserProxy.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testUserProxy.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingUserProxy() throws Exception {
        int databaseSizeBeforeUpdate = userProxyRepository.findAll().size();
        userProxy.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProxyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userProxy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userProxy))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProxy in the database
        List<UserProxy> userProxyList = userProxyRepository.findAll();
        assertThat(userProxyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserProxy() throws Exception {
        int databaseSizeBeforeUpdate = userProxyRepository.findAll().size();
        userProxy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProxyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userProxy))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProxy in the database
        List<UserProxy> userProxyList = userProxyRepository.findAll();
        assertThat(userProxyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserProxy() throws Exception {
        int databaseSizeBeforeUpdate = userProxyRepository.findAll().size();
        userProxy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProxyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userProxy))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserProxy in the database
        List<UserProxy> userProxyList = userProxyRepository.findAll();
        assertThat(userProxyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserProxy() throws Exception {
        // Initialize the database
        userProxyRepository.saveAndFlush(userProxy);

        int databaseSizeBeforeDelete = userProxyRepository.findAll().size();

        // Delete the userProxy
        restUserProxyMockMvc
            .perform(delete(ENTITY_API_URL_ID, userProxy.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserProxy> userProxyList = userProxyRepository.findAll();
        assertThat(userProxyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
