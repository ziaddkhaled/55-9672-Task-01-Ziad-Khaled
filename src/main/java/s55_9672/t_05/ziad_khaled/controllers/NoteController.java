package s55_9672.t_05.ziad_khaled.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import s55_9672.t_05.ziad_khaled.models.Note;
import s55_9672.t_05.ziad_khaled.services.NoteService;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable String id) {
        return noteService.getNoteById(id);
    }

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        return noteService.createNote(note);
    }

    @GetMapping("/search")
    public List<Note> searchByTitle(@RequestParam String title) {
        return noteService.getAllNotes().stream()
                .filter(n -> n.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable String id, @RequestBody Note note) {
        return noteService.updateNote(id, note);
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
    }
}
