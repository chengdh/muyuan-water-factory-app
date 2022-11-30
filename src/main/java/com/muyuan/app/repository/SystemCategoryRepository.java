package com.muyuan.app.repository;

import com.muyuan.app.domain.SystemCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SystemCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemCategoryRepository extends JpaRepository<SystemCategory, Long> {}
