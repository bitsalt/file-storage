package com.bitsalt.cloudstorage.mapper;

import com.bitsalt.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT noteid, notetitle, notedescription FROM notes WHERE userid = #{userId}")
    List<Note> getNotes(Integer userId);

    @Select("SELECT * FROM notes WHERE noteid = #{noteId}")
    Note getSingleNote(Integer noteId);

    @Insert("INSERT INTO notes (notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    Integer insert(Note note);

    @Update("UPDATE notes SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId}")
    int update(String noteTitle, String noteDescription, Integer noteId);

    @Delete("DELETE FROM notes WHERE noteid = #{noteId}")
    int delete(Integer noteId);
}
