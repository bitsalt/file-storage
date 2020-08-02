package com.bitsalt.cloudstorage.mapper;

import com.bitsalt.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FileMapper {

    @Select("SELECT fileid, filename FROM files WHERE userid = #{userId}")
    File getFiles(int userId);

    @Select("SELECT * FROM files WHERE fileid = #{fileId}")
    File getFile(int fileId);

    @Insert("INSERT INTO files (filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    int insert(File file);

    @Update("UPDATE files SET filename = #{fileName}, contenttype = #{contentType}, filesize = #{fileSize}, filedata = #{fileData} WHERE fileid = #{fileId}")
    int update(String fileName, String contentType, String fileSize, String fileData, int fileId);

    @Delete("DELETE * FROM files WHERE fileid = #{fileId}")
    int delete(int fileId);
}
