package com.t3h.land2110e.repository;

import com.t3h.land2110e.entity.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, Integer> {
    @Query(nativeQuery = true, value = "select * from friend where " +
                "sender_id = :userId and receiver_id = :friendId limit 1")
    FriendEntity findOnByUserIdAndFriendId(
            @Param("userId") int userId,
            @Param("friendId") int friendId
    );

    @Query(nativeQuery = true, value =
            "SELECT * FROM friend where sender_id = :senderId and receiver_id = :receiverId limit 1")
    FriendEntity findOneBySenderIdAndReceiverId(
            @Param("senderId") int senderId,
            @Param("receiverId") int receiverId
    );

}
