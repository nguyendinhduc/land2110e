package com.t3h.land2110e.repository;

import com.t3h.land2110e.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Integer> {

    @Query(nativeQuery = true, value =
            "select * from user_profile where username = :username limit 1")
    UserProfileEntity findOneByUsername(
            @Param("username") String username
    );

    @Query(nativeQuery = true, value =
            "select * from user_profile where id = :id limit 1")
    UserProfileEntity findById(
            @Param("id") int id
    );
}
