package com.muyuan.app.repository;

import com.muyuan.app.domain.UserOrganization;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserOrganization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserOrganizationRepository extends JpaRepository<UserOrganization, Long> {}
