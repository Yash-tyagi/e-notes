package com.yash.enotes.repository;

import com.yash.enotes.entity.Note;
import com.yash.enotes.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note,Integer> {
    public Page<Note> findByUser(User user, Pageable pageable);
}
