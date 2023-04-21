package ru.yandex.practicum.filmorate.storage.rating;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("RatingDbStorage")
public class RatingDbStorage implements RatingStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Rating rowMapToMpa(ResultSet resultSet, int i) throws SQLException {
        return Rating.builder()
                .id(resultSet.getInt("ratingMPAId"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public Rating getRatingById(Integer mpaId) {
        String sqlQueryGetById = "SELECT * FROM MPARatings WHERE ratingMPAId = ?";
        Rating rating = null;
        try {
            rating = jdbcTemplate.queryForObject(sqlQueryGetById, this::rowMapToMpa, mpaId);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}.  id={}", sqlQueryGetById, mpaId);
        }
        return rating;
    }

    @Override
    public List<Rating> getRatingAll() {
        List<Rating> ratings = new ArrayList<>();
        String sqlQueryGetAll = "SELECT * FROM MPARatings";
        try {
            ratings = jdbcTemplate.query(sqlQueryGetAll, this::rowMapToMpa);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}", sqlQueryGetAll);
        }
        return ratings;
    }
}