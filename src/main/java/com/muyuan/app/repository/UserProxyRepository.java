package com.muyuan.app.repository;

import com.muyuan.app.domain.UserProxy;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserProxy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProxyRepository extends JpaRepository<UserProxy, Long> {}
