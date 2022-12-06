package com.muyuan.app.repository;

import com.muyuan.app.domain.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);

    // 获取的用户信息包含角色/角色权限/组织机构/
    @EntityGraph(
        attributePaths = { "authorities", "applicationUser.roles", "applicationUser.roles.operates", "applicationUser.organizaitons" }
    )
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(
        attributePaths = { "authorities", "applicationUser.roles", "applicationUser.roles.operates", "applicationUser.organizaitons" }
    )
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);
}
