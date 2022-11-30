package com.muyuan.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.muyuan.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserOrganizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserOrganization.class);
        UserOrganization userOrganization1 = new UserOrganization();
        userOrganization1.setId(1L);
        UserOrganization userOrganization2 = new UserOrganization();
        userOrganization2.setId(userOrganization1.getId());
        assertThat(userOrganization1).isEqualTo(userOrganization2);
        userOrganization2.setId(2L);
        assertThat(userOrganization1).isNotEqualTo(userOrganization2);
        userOrganization1.setId(null);
        assertThat(userOrganization1).isNotEqualTo(userOrganization2);
    }
}
