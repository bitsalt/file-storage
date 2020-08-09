package com.bitsalt.cloudstorage.mapper;

import com.bitsalt.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM files WHERE userid = #{userId}")
    List<File> getFiles(int userId);

    @Select("SELECT * FROM files WHERE fileid = #{fileId}")
    File getFile(int fileId);

    @Select("SELECT count(*) FROM files WHERE filename = #{fileName} AND userid = #{userId}")
    int findExistingFile(String fileName, int userId);

    @Insert("INSERT INTO files (filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    int insert(File file);

    @Update("UPDATE files SET filename = #{fileName}, contenttype = #{contentType}, filesize = #{fileSize}, filedata = #{fileData} WHERE fileid = #{fileId}")
    int update(String fileName, String contentType, long fileSize, byte[] fileData, int fileId);

    @Delete("DELETE FROM files WHERE fileid = #{fileId}")
    int delete(int fileId);

}
