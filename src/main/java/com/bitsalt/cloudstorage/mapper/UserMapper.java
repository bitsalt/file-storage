package com.bitsalt.cloudstorage.mapper;

import com.bitsalt.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{userName}")
    User getUser(String username);

    @Insert("INSERT INTO users (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    int insert(User user);

    @Update("UPDATE users SET username = #{username}, password = #{password}, firstname = #{firstName}, lastname = #{lastName} WHERE userid = #{userId}")
    int update(int userId);

    @Delete("DELETE * FROM users WHERE userid = #{userId}")
    int delete(int userId);

}
