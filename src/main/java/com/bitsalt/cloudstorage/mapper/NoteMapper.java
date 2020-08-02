package com.bitsalt.cloudstorage.mapper;

import com.bitsalt.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT noteid, notetitle, notedescription FROM notes WHERE userid = #{userId}")
    List<Note> getNotes(int userId);

    @Select("SELECT * FROM notes WHERE noteid = #{noteId}")
    Note getSingleNote(int noteId);

    @Insert("INSERT INTO notes (notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insert(Note note);

    @Update("UPDATE notes SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId}")
    int update(String noteTitle, String noteDescription, int noteId);

    @Delete("DELETE FROM notes WHERE noteid = #{noteId}")
    int delete(int noteId);
}
