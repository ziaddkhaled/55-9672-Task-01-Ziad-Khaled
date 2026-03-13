package s55_9672.t_05.ziad_khaled.repositories;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import s55_9672.t_05.ziad_khaled.models.Note;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Repository
public class NoteRepository {

    private List<Note> notes;
    private java.io.File jsonFile;

    public NoteRepository() {
        InputStream inputStream = getClass().getResourceAsStream("/notes.json");

        if (inputStream == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to read notes.json");
        }

        try {
            this.jsonFile = new java.io.File(getClass().getResource("/notes.json").toURI());
        } catch (Exception e) {
            this.jsonFile = new java.io.File("/data/notes.json");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        this.notes = objectMapper.readValue(inputStream, new TypeReference<List<Note>>() {});
    }

    public List<Note> findAll() {
        return notes;
    }

    public Optional<Note> findById(String id) {
        return notes.stream().filter(n -> n.getId().equals(id)).findFirst();
    }

    public List<Note> findByUserId(String userId) {
        return notes.stream().filter(n -> n.getUserId().equals(userId)).collect(Collectors.toList());
    }

    public Note save(Note note) {
        note.setId(UUID.randomUUID().toString());
        notes.add(note);
        new ObjectMapper().writeValue(jsonFile, notes);
        return note;
    }

    public Optional<Note> update(String id, Note updated) {
        Optional<Note> existing = findById(id);
        if (existing.isPresent()) {
            Note note = existing.get();
            note.setTitle(updated.getTitle());
            note.setContent(updated.getContent());
            note.setUserId(updated.getUserId());
            new ObjectMapper().writeValue(jsonFile, notes);
            return Optional.of(note);
        }
        return Optional.empty();
    }

    public Optional<Note> findByTitle(String title) {
        return notes.stream()
                .filter(n -> n.getTitle().toLowerCase().contains(title.toLowerCase()))
                .findFirst();
    }

    public boolean deleteById(String id) {
        boolean removed = notes.removeIf(n -> n.getId().equals(id));
        if (removed) {
            new ObjectMapper().writeValue(jsonFile, notes);
        }
        return removed;
    }
}
