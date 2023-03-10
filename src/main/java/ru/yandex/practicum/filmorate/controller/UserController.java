package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private HashMap<Integer, User> userHashMap = new HashMap<>();
    private int id = 0;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        user.setId(++id);
        userHashMap.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (userHashMap.containsKey(user.getId())) {
            userHashMap.put(user.getId(), user);
            log.debug("Пользователь {} был обновлен", user.getId());
            return user;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ArrayList<User> getAllUsers() {
        log.debug("Текущее количество пользователей: {}", userHashMap.size());
        return new ArrayList<>(userHashMap.values());
    }
}