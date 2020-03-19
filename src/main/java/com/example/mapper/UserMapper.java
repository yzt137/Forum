package com.example.mapper;

import com.example.dto.QuestionDTO;
import com.example.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    @Select("select * from user")
    List<User> findAll();

    @Select("select * from user where id=#{id}")
    User findById(Integer id);

    @Select("select * from user where token=#{token}")
    User findByToken(String token);

    @Insert("insert into user(account_id,name,token,gmt_create,gmt_modified,avatar_url) values(#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Update("update user set token=#{token}, name=#{name}, avatar_url=#{avatarUrl}, gmt_modified=#{gmtModified} where account_id=#{accountId}")
    void update(User user);

    @Delete("delete from user where id=#{id}")
    void delete(Integer id);

    @Select("select * from user where account_id=#{accountId}")
    User findByAccountId(String accountId);


}
