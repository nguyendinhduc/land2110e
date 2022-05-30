package com.t3h.land2110e.repository;

import com.t3h.land2110e.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageEntityRepository extends JpaRepository<MessageEntity, String> {

    @Query(nativeQuery = true, value =
            "SELECT * FROM message where " +
                    "(sender_id = :userId AND receiver_id = :friendId) OR " +
                    "(sender_id = :friendId AND receiver_id = :userId)")
    List<MessageEntity> getMessages(
            @Param("userId") int userId,
            @Param("friendId") int friendId
    );

}
