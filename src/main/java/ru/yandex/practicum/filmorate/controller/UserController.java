package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        log.debug("User is deleted");
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/{id}")
    public User getUserById(@Valid @PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping(value = "/{id}/friends" )
    public Collection<User> getFriends(@Valid @PathVariable Long id) {
        return userService.getListOfFriends(id);
    }

    @GetMapping(value = "/{id}/friends/common/{userId}")
    public Collection<User> getMutualFriends(@Valid @PathVariable Long id, @Valid @PathVariable Long userId) {
        return userService.getListOfMutualFriends(id, userId);
    }

    @PutMapping(value = "/{id}/friends/{userId}")
    public void addFriend(@Valid @PathVariable Long id, @Valid @PathVariable Long userId) {
        userService.addFriend(id, userId);
        log.debug("Friend is added");
    }

    @DeleteMapping(value = "/{id}/friends/{userId}")
    public void removeFriend(@Valid @PathVariable Long id, @Valid @PathVariable Long userId) {
        userService.removeFriend(id, userId);
        log.debug("Friend is deleted");
    }
}