package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User createUser(User user) {
        return userStorage.add(user);
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public void deleteUser(Long id) {
        userStorage.delete(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User getUserById(Long id) {
        return userStorage.getById(id);
    }

    public void addFriend(Long id, Long friendId) {
        if (contains(id)) {
            if (contains(friendId)) {
                final User user = getUserById(id);
                final User friend = getUserById(friendId);
                user.getFriends().add(friend.getId());
                friend.getFriends().add(user.getId());
            } else {
                log.info("Пользователь " + friendId + " не найден");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else {
            log.info("Пользователь " + id + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void removeFriend(Long id, Long friendId) {
        if (contains(id)) {
            if (contains(friendId)) {
                final User user = getUserById(id);
                final User friend = getUserById(friendId);
                user.getFriends().remove(friend.getId());
                friend.getFriends().remove(user.getId());
            } else {
                log.info("Пользователь " + friendId + " не найден");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else {
            log.info("Пользователь " + id + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Collection<User> getListOfFriends(Long id) {
        final ArrayList<User> friends = new ArrayList<>();
        if (contains(id)) {
            final Set<Long> friendIds = getUserById(id).getFriends();
            for (Long friendId : friendIds) {
                friends.add(getUserById(friendId));
            }
            return friends;
        }
        log.info("Пользователь " + id + " не найден");
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Collection<User> getListOfMutualFriends(Long id, Long otherId) {
        final ArrayList<User> mutualFriends = new ArrayList<>();
        if (contains(id)) {
            if (contains(otherId)) {
                final Set<Long> userFriends = getUserById(id).getFriends();
                final Set<Long> otherUserFriends = getUserById(otherId).getFriends();
                final Set<Long> mutualFriendIds = new HashSet<>(userFriends);
                mutualFriendIds.retainAll(otherUserFriends);
                for (Long friendId : mutualFriendIds) {
                    mutualFriends.add(getUserById(friendId));
                }
                return mutualFriends;
            } else {
                log.info("Пользователь " + otherId + " не найден");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        log.info("Пользователь " + id + " не найден");
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    private boolean contains(Long id) {
        return userStorage.contains(id);
    }
}