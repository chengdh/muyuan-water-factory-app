package com.muyuan.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.muyuan.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemCategory.class);
        SystemCategory systemCategory1 = new SystemCategory();
        systemCategory1.setId(1L);
        SystemCategory systemCategory2 = new SystemCategory();
        systemCategory2.setId(systemCategory1.getId());
        assertThat(systemCategory1).isEqualTo(systemCategory2);
        systemCategory2.setId(2L);
        assertThat(systemCategory1).isNotEqualTo(systemCategory2);
        systemCategory1.setId(null);
        assertThat(systemCategory1).isNotEqualTo(systemCategory2);
    }
}
