package com.yash.enotes.service.IUserService;

import com.yash.enotes.entity.Note;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NoteService {
    public Note addNote(Note newNote, String email);
    public Note updateNote(Note note, String email);
    public boolean deleteNote(Integer id, String email);

    public Page<Note> getAllNotes(String email, Integer pageNo);

    public Note getNote(Integer id, String email);
}
