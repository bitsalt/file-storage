package com.bitsalt.cloudstorage.mapper;

import com.bitsalt.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT fileId, fileName FROM files WHERE userid = #{userId}")
    List<File> getFiles(Integer userId);

    @Select("SELECT * FROM files WHERE fileid = #{fileId}")
    File getFile(Integer fileId);

    @Select("SELECT count(*) FROM files WHERE filename = #{fileName} AND userid = #{userId}")
    int findExistingFile(String fileName, Integer userId);

    @Insert("INSERT INTO files (filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    Integer insert(File file);

    @Update("UPDATE files SET filename = #{fileName}, contenttype = #{contentType}, filesize = #{fileSize}, filedata = #{fileData} WHERE fileid = #{fileId}")
    Integer update(String fileName, String contentType, long fileSize, byte[] fileData, Integer fileId);

    @Delete("DELETE FROM files WHERE fileid = #{fileId}")
    Integer delete(int fileId);

    @Delete("TRUNCATE TABLE files")
    void deleteAll();
}
