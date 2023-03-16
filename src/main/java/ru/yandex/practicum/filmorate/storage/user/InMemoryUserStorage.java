package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> userHashMap = new HashMap<>();
    private Long id = 0L;

    @Override
    public User createUser(User user) {
        user.setId(++id);
        userHashMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (userHashMap.containsKey(user.getId())) {
            userHashMap.put(user.getId(), user);
            log.debug("Пользователь {} был обновлен", user.getId());
            return user;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public void deleteUser(Long id) {
        if (userHashMap.containsKey(id)) {
            userHashMap.remove(id);
            log.debug("Пользователь {} был удален", id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        log.debug("Текущее количество пользователей: {}", userHashMap.size());
        return new ArrayList<>(userHashMap.values());
    }

    @Override
    public User getUserById(Long id) {
        if (userHashMap.containsKey(id)) {
            return userHashMap.get(id);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public HashMap<Long, User> getUserHashMap() {
        return userHashMap;
    }
}