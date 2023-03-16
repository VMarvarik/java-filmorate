package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.HashMap;

public interface UserStorage {
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
    Collection<User> getAllUsers();
    User getUserById(Long id);
    HashMap<Long, User> getUserHashMap();
}