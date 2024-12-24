package com.yash.enotes.service;

import com.yash.enotes.entity.Note;
import com.yash.enotes.entity.User;
import com.yash.enotes.repository.NoteRepository;
import com.yash.enotes.repository.UserRepository;
import com.yash.enotes.service.IUserService.NoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class NoteServiceImpl implements NoteService {
    private static final int PAGE_SIZE = 6;
    private final NoteRepository noteRepo;
    private final UserRepository userRepo;

    public NoteServiceImpl(NoteRepository noteRepo, UserRepository userRepo) {
        this.noteRepo = noteRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Note addNote(Note newNote, String email) {
        //fetching current session user
        User user = userRepo.findByEmail(email);

        //populating newNote
        newNote.setCreatedAt(LocalDateTime.now());
        newNote.setUser(user);

        if(user != null) {
            user.getNotes().add(newNote);
            userRepo.save(user);
        }
        return newNote;
    }

    @Override
    public Note updateNote(Note note, String email) {
        if(!isUserAuthorized(note.getId(),email)) throw new IllegalArgumentException("operation not allowed!");
        Note currNote = getNote(note.getId(),email);
        if(currNote!=null) {
           currNote.setTitle(note.getTitle());
           currNote.setDescription(note.getDescription());
           noteRepo.save(currNote);
        }
        return currNote;
    }

    @Override
    public boolean deleteNote(Integer id, String email) {
        if(!isUserAuthorized(id,email)) throw new IllegalArgumentException("operation not allowed!");
        User user = userRepo.findByEmail(email);
        Note note = noteRepo.findById(id).orElse(null);
        user.getNotes().remove(note);
        userRepo.save(user);
        noteRepo.deleteById(id);
        return true;
    }

    @Override
    public Page<Note> getAllNotes(String email, Integer pageNo) {
        User user = userRepo.findByEmail(email);
        Pageable pageable = PageRequest.of(pageNo,PAGE_SIZE);
        return noteRepo.findByUser(user,pageable);

    }

    @Override
    public Note getNote(Integer id, String email) {
        if(!isUserAuthorized(id,email)) throw new IllegalArgumentException("operation not allowed!");

        return noteRepo.findById(id).orElse(null);
    }

    public boolean isUserAuthorized(Integer noteId, String email) {
        User user = userRepo.findByEmail(email);
        return user != null && user.getNotes().stream().anyMatch(n -> Objects.equals(n.getId(),noteId));
    }
}
