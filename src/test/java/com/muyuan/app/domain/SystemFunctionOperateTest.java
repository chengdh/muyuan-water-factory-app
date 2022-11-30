package com.muyuan.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.muyuan.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemFunctionOperateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemFunctionOperate.class);
        SystemFunctionOperate systemFunctionOperate1 = new SystemFunctionOperate();
        systemFunctionOperate1.setId(1L);
        SystemFunctionOperate systemFunctionOperate2 = new SystemFunctionOperate();
        systemFunctionOperate2.setId(systemFunctionOperate1.getId());
        assertThat(systemFunctionOperate1).isEqualTo(systemFunctionOperate2);
        systemFunctionOperate2.setId(2L);
        assertThat(systemFunctionOperate1).isNotEqualTo(systemFunctionOperate2);
        systemFunctionOperate1.setId(null);
        assertThat(systemFunctionOperate1).isNotEqualTo(systemFunctionOperate2);
    }
}
