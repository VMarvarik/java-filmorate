package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(Long id, Long friendId) {
        if (containsUser(id) && containsUser(friendId)) {
            User user = getUserById(id);
            User friend = getUserById(friendId);
            user.getFriends().add(friend.getId());
            friend.getFriends().add(user.getId());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void removeFriend(Long id, Long friendId) {
        if (containsUser(id) && containsUser(friendId)) {
            User user = getUserById(id);
            User friend = getUserById(friendId);
            user.getFriends().remove(friend.getId());
            friend.getFriends().remove(user.getId());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Collection<User> getListOfFriends(Long id) {
        ArrayList<User> friends = new ArrayList<>();
        if (containsUser(id)) {
            Set<Long> friendIds = getUserById(id).getFriends();
            for (Long friendId : friendIds) {
                friends.add(getUserById(friendId));
            }
            return friends;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Collection<User> getListOfMutualFriends(Long id, Long otherId) {
        ArrayList<User> mutualFriends = new ArrayList<>();
        if (containsUser(id) && containsUser(otherId)) {
            Set<Long> userFriends = getUserById(id).getFriends();
            Set<Long> otherUserFriends = getUserById(otherId).getFriends();
            Set<Long> mutualFriendIds = new HashSet<>(userFriends);
            mutualFriendIds.retainAll(otherUserFriends);
            for (Long friendId : mutualFriendIds) {
                mutualFriends.add(getUserById(friendId));
            }
            return mutualFriends;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    private boolean containsUser(Long id) {
        return userStorage.getUserHashMap().containsKey(id);
    }
}