package com.muyuan.app.repository;

import com.muyuan.app.domain.SystemFunction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SystemFunction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemFunctionRepository extends JpaRepository<SystemFunction, Long> {}
