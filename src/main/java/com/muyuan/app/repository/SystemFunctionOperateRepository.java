package com.muyuan.app.repository;

import com.muyuan.app.domain.SystemFunctionOperate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SystemFunctionOperate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemFunctionOperateRepository extends JpaRepository<SystemFunctionOperate, Long> {}
