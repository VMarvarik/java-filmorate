package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> userHashMap = new HashMap<>();
    private Long id = 0L;

    @Override
    public User add(@Valid User user) {
        if (user != null) {
            user.setId(++id);
            userHashMap.put(user.getId(), user);
            log.info("Пользователь добавлен");
            return user;
        }
        log.info("Объект пользователь был null");
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public User update(@Valid User user) {
        if (user != null) {
            if (userHashMap.containsKey(user.getId())) {
                userHashMap.put(user.getId(), user);
                log.info("Пользователь обновлен");
                return user;
            }
            log.info("Пользователь " + user.getId() + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("Объект пользователь был null");
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public User getById(Long id) {
        if (userHashMap.containsKey(id)) {
            return userHashMap.get(id);
        }
        log.info("Пользователь " + id + " не найден");
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public void delete(Long id) {
        if (userHashMap.containsKey(id)) {
            userHashMap.remove(id);
        } else {
            log.info("Пользователь " + id + " не найден");
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