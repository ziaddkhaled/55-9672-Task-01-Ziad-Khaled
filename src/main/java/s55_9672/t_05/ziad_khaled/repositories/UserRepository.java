package s55_9672.t_05.ziad_khaled.repositories;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import s55_9672.t_05.ziad_khaled.models.User;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Repository
public class UserRepository {

    private List<User> users;
    private java.io.File jsonFile;

    public UserRepository() {
        InputStream inputStream = getClass().getResourceAsStream("/users.json");

        if (inputStream == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to read users.json");
        }

        try {
            this.jsonFile = new java.io.File(getClass().getResource("/users.json").toURI());
        } catch (Exception e) {
            this.jsonFile = new java.io.File("/data/users.json");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        this.users = objectMapper.readValue(inputStream, new TypeReference<List<User>>() {});
    }

    public List<User> findAll() {
        return users;
    }

    public Optional<User> findById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public Optional<User> findByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    public User save(User user) {
        user.setId(UUID.randomUUID().toString());
        users.add(user);
        new ObjectMapper().writeValue(jsonFile, users);
        return user;
    }

    public Optional<User> update(String id, User updated) {
        Optional<User> existing = findById(id);
        if (existing.isPresent()) {
            User user = existing.get();
            user.setUsername(updated.getUsername());
            user.setEmail(updated.getEmail());
            new ObjectMapper().writeValue(jsonFile, users);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public boolean deleteById(String id) {
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        if (removed) {
            new ObjectMapper().writeValue(jsonFile, users);
        }
        return removed;
    }
}
