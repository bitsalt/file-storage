package com.bitsalt.cloudstorage.mapper;

import com.bitsalt.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM credentials WHERE userid = #{userId}")
    List<Credential> getCredentials(int userId);

    @Select("SELECT * FROM credentials WHERE credentialid = #{credentialId}")
    Credential getSingleCredential(int credentialId);

    @Insert("INSERT INTO credentials (url, username, password, key, userid) VALUES(#{url}, #{userName}, #{password}, #{key}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    Integer insert(Credential credential);

    @Update("UPDATE credentials SET url = #{url}, username = #{userName}, key = #{key}, password = #{password} WHERE credentialid = #{credentialId}")
    int update(String url, String userName, String key, String password, int credentialId);

    @Delete("DELETE FROM credentials WHERE credentialid = #{credentialId}")
    int delete(int credentialId);
}
