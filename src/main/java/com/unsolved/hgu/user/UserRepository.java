package com.unsolved.hgu.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByUsername(String username);

    Optional<SiteUser> findByEmail(String email);

    @Query("SELECT u "
            + "FROM SiteUser u "
            + "WHERE u.provider = :provider and u.providerId = :providerId ")
    Optional<SiteUser> findByLoginId(@Param("provider") String provider, @Param("providerId") String providerId);
}
