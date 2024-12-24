package com.yash.enotes.controller;

import com.yash.enotes.entity.Note;
import com.yash.enotes.entity.User;
import com.yash.enotes.repository.UserRepository;
import com.yash.enotes.service.IUserService.NoteService;
import com.yash.enotes.service.IUserService.UserService;
import com.yash.enotes.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.PrintStream;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository repo;
    private final NoteService noteService;

    public UserController(UserRepository repo, NoteService noteService) {
        this.repo = repo;
        this.noteService = noteService;
    }

    @ModelAttribute
    public void getUser(Principal p, Model m) {
        String email = p.getName();
        User currentUser = repo.findByEmail(email);
        m.addAttribute("currentUser",currentUser);
    }

    @GetMapping("/viewNotes")
    public String viewNotes(@RequestParam(defaultValue = "0") Integer pageNo, Model m, Principal p) {
        Page<Note> page = noteService.getAllNotes(p.getName(),pageNo);
        m.addAttribute("notes",page.getContent());
        m.addAttribute("totalNotes",page.getTotalElements());
        m.addAttribute("totalPages",page.getTotalPages());
        m.addAttribute("currPage",pageNo);
        return "view_notes";
    }

    @GetMapping("/addNotes")
    public String addNotes() {
        return "add_notes";
    }

    @PostMapping("/addNotes")
    public String addingNotes(@ModelAttribute Note note,Principal p) {
        noteService.addNote(note,p.getName());
        return "redirect:viewNotes";
    }


    @GetMapping("/editNotes/{id}")
    public String editNotes(@PathVariable Integer id,Model model, Principal p) {
        Note note = noteService.getNote(id,p.getName());
        if(note == null) return "view_notes";
        model.addAttribute("note",note);
        return "edit_notes";
    }

    @PostMapping("/editNotes/{id}")
    public String saveEditedNotes(@PathVariable Integer id, @ModelAttribute Note note, Principal p) {
        note.setId(id);
        noteService.updateNote(note,p.getName());
        return "redirect:/user/viewNotes";
    }

    @GetMapping("/deleteNotes/{id}")
    public String deleteNote(@PathVariable Integer id, Principal p) {
        noteService.deleteNote(id,p.getName());
        return "redirect:/user/viewNotes";
    }

    @GetMapping("/viewNote/{id}")
    public String viewNote(@PathVariable Integer id, Model model, Principal p) {
        Note note = noteService.getNote(id,p.getName());
        if(note == null) return "redirect:/user/viewNotes";
        model.addAttribute("note",note);
        return "view_note";
    }
}
