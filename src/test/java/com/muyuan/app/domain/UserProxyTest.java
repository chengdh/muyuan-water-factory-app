package com.muyuan.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.muyuan.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserProxyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProxy.class);
        UserProxy userProxy1 = new UserProxy();
        userProxy1.setId(1L);
        UserProxy userProxy2 = new UserProxy();
        userProxy2.setId(userProxy1.getId());
        assertThat(userProxy1).isEqualTo(userProxy2);
        userProxy2.setId(2L);
        assertThat(userProxy1).isNotEqualTo(userProxy2);
        userProxy1.setId(null);
        assertThat(userProxy1).isNotEqualTo(userProxy2);
    }
}
