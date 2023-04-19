package ru.yandex.practicum.filmorate.storageTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final UserDbStorage userStorage;

    @BeforeEach
    public void createTestUserBeforeEach() {
        User userBeforeEach = User.builder()
                .id(1L)
                .name("name")
                .email("test@test.test")
                .login("login")
                .birthday(LocalDate.of(2023, 1, 1))
                .build();
        if (userStorage.getById(1L) != null) {
            userStorage.add(userBeforeEach);
            return;
        }
        userStorage.update(userBeforeEach);
    }

    @AfterEach
    public void deleteUsersAfterTest() {
        List<User> listUser = userStorage.getAll();
        for (User user : listUser) {
            if (user.getId() != 1L) {
                userStorage.delete(user.getId());
            }
        }
    }

    @Test
    public void getById() {
        Optional<User> userOptional = Optional.ofNullable(userStorage.getById(1L));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                )
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "login1"));
    }

    @Test
    public void getByIdFail() {
        Optional<User> userOptional = Optional.ofNullable(userStorage.getById(3L));
        assertThat(userOptional)
                .isNotPresent()
        ;
    }

    @Test
    public void updateUser() {
        User userTestUpdate = User.builder()
                .id(1L)
                .name("name_update")
                .email("test1@test.test")
                .login("login1")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        userStorage.update(userTestUpdate);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getById(1L));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "name_update"));
    }

    @Test
    public void updateUserFail() {
        User userTestUpdate = User.builder()
                .id(2L)
                .name("name_update")
                .email("test1@test.test")
                .login("login1")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        userStorage.update(userTestUpdate);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getById(2L));
        assertThat(userOptional)
                .isNotPresent();
    }

    @Test
    public void deleteUser() {
        User userTestDelete = User.builder()
                .id(2L)
                .email("tests@test.test")
                .login("login11")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        userStorage.add(userTestDelete);
        userStorage.delete(userTestDelete.getId());
        Optional<User> userOptional = Optional.ofNullable(userStorage.getById(2L));
        assertThat(userOptional).isNotPresent();
    }

    @Test
    public void deleteUserFail() {
        User userTestDelete = User.builder()
                .id(2L)
                .build();
        userStorage.delete(userTestDelete.getId());
        Optional<User> userOptional = Optional.ofNullable(userStorage.getById(1L));
        assertThat(userOptional).isPresent();
    }

    @Test
    public void getAll() {
        User userTestGetAll = User.builder()
                .id(2L)
                .name("name_getAll")
                .email("test2@test.test")
                .login("login2")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        userStorage.add(userTestGetAll);
        List<User> result = userStorage.getAll();
        Assertions.assertEquals(1, result.size());
    }
}