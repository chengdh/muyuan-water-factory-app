package com.muyuan.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.muyuan.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemFunctionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemFunction.class);
        SystemFunction systemFunction1 = new SystemFunction();
        systemFunction1.setId(1L);
        SystemFunction systemFunction2 = new SystemFunction();
        systemFunction2.setId(systemFunction1.getId());
        assertThat(systemFunction1).isEqualTo(systemFunction2);
        systemFunction2.setId(2L);
        assertThat(systemFunction1).isNotEqualTo(systemFunction2);
        systemFunction1.setId(null);
        assertThat(systemFunction1).isNotEqualTo(systemFunction2);
    }
}
