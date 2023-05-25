package com.project.SpringTGBot.database;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends CrudRepository<User, Long> {
    @Transactional
    @Modifying
    @Query("update tg_data t set t.msg_numb = t.msg_numb + 1 where t.id is not null and t.id = :id")
    void updateMsgNumberByUserId(@Param("id") long id);

    @Transactional
    @Modifying
    @Query("update tg_data t set t.score = t.score + 1 where t.id is not null and t.id = :id")
    void updateScoreByUserId(@Param("id") long id);

    @Transactional
    @Modifying
    @Query("update tg_data t set t.score = 0 where t.id is not null and t.id = :id")
    void removeScoreByUserId(@Param("id") long id);

    @Transactional
    @Query("select t.score from tg_data t where  t.id = :id")
    Integer findUserById(@Param("id") long id);
}
