package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> userHashMap = new HashMap<>();
    private Long id = 0L;

    @Override
    public User add(@Valid User user) {
        user.setId(++id);
        userHashMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(@Valid User user) {
        if (userHashMap.containsKey(user.getId())) {
            userHashMap.put(user.getId(), user);
            return user;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public User getById(Long id) {
        if (userHashMap.containsKey(id)) {
            return userHashMap.get(id);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public void delete(Long id) {
        if (userHashMap.containsKey(id)) {
            userHashMap.remove(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Collection<User> getAll() {
        return new ArrayList<>(userHashMap.values());
    }

    @Override
    public Boolean contains(Long id) {
        return userHashMap.containsKey(id);
    }
}