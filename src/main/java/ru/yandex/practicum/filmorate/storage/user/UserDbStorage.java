package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.error.exception.NullException;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User rowMapToUser(ResultSet resultSet, int i) throws SQLException {
        LocalDate birthday;
        if (resultSet.getDate("birthday") == null) {
            birthday = LocalDate.of(0, 1, 1);
        } else {
            birthday = resultSet.getDate("birthday").toLocalDate();
        }
        return User.builder()
                .id(resultSet.getLong("id"))
                .email((resultSet.getString("email")))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(birthday)
                .build();
    }

    private Map<Long, Boolean> rowMapToFriends(ResultSet resultSet, int i) throws SQLException {
        var statuses = new HashMap<Long, Boolean>();
        statuses.put(resultSet.getLong("friendsId"), resultSet.getBoolean("status"));
        return statuses;
    }

    @Override
    public User add(User user) {
        if (user == null){
            throw new NullException("Пользователь не может быть null");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        String sqlQueryToUsers = "insert into users(email,login,name,birthday) values (?,?,?,?)";
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(sqlQueryToUsers, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf((user.getBirthday())));
            return ps;
        };
        GeneratedKeyHolder gkh = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, gkh);
        user.setId(Objects.requireNonNull(gkh.getKey()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        if (user == null){
            throw new NullException("Пользователь не может быть null");
        }

        String name;
        if (user.getName() == null || user.getName().isBlank()) {
            name = user.getLogin();
        } else {
            name = user.getName();
        }
        String sqlQueryUpdate = "update users " +
                "set email = ?, login = ?, name = ?, birthday = ?" +
                "where id = ?";
        jdbcTemplate.update(sqlQueryUpdate,
                user.getEmail(),
                user.getLogin(),
                name,
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public void delete(Long id) {
        String sqlQueryDelete = "delete from users where id = ?";
        try {
            jdbcTemplate.update(sqlQueryDelete,
                    id);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}", sqlQueryDelete);
        }
    }

    @Override
    public List<User> getAll() {
        String sqlQueryGetAll = "select * from users";
        List<User> users = new ArrayList<>();
        try {
            users = jdbcTemplate.query(sqlQueryGetAll, this::rowMapToUser);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}", sqlQueryGetAll);
        }
        return users;
    }

    @Override
    public User getById(Long id) {
        String sqlQueryGetById = "select * from users where id = ?";
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(sqlQueryGetById, this::rowMapToUser, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}.  id={}", sqlQueryGetById, id);
        }
        return user;
    }

    @Override
    public Map<Long, User> getUsersMap() {
        String sqlQueryUsersMap = "select * from users";
        List<User> userMap = jdbcTemplate.query(sqlQueryUsersMap, this::rowMapToUser);
        Map<Long, User> result = new HashMap<>();
        for (User user : userMap) {
            result.put(user.getId(), user);
        }
        return result;
    }

    @Override
    public void addFriend(Long userId, Long idFriend) {
        String sqlQueryAddFriend = "insert into userFriends(userId,friendsId,status) values (?,?,?)";
        jdbcTemplate.update(sqlQueryAddFriend, userId, idFriend, false);
    }

    @Override
    public void removeFriend(Long id, Long idRemoveFriend) {
        if (id == null || idRemoveFriend == null || id.equals(idRemoveFriend)) {
            return;
        }
        String sqlQueryRemoveFriend = "delete from userFriends t where t.userId = ? and t.friendsId = ?";
        try {
            jdbcTemplate.update(sqlQueryRemoveFriend, id, idRemoveFriend);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}.  userId={}, friendsId={}",
                    sqlQueryRemoveFriend, id, idRemoveFriend);
        }
    }

    @Override
    public List<User> getFriends(Long id) {
        List<User> friends = new ArrayList<>();
        if (id == null) {
            return friends;
        }
        String sqlQueryGetFriends = "select u.* from userFriends t left join users u on t.friendsId = u.id" +
                " where t.userId = ?";
        try {
            friends = jdbcTemplate.query(sqlQueryGetFriends, this::rowMapToUser, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}", sqlQueryGetFriends);
        }
        return friends;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> commonFriends = new ArrayList<>();
        String sqlQueryCommonFriends = "select u.* from userFriends t left join users u on t.friendsId = u.id " +
                " where t.userId= ? and t.friendsId in (select tt.friendsId from  userFriends tt where tt.userId=?)";
        try {
            commonFriends = jdbcTemplate.query(sqlQueryCommonFriends, this::rowMapToUser, id, otherId);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}.  userId={}, friendsId={}",
                    sqlQueryCommonFriends, id, otherId);
        }
        return commonFriends;
    }
}