package ru.yandex.practicum.filmorate.storage.rating;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("RatingDbStorage")
public class RatingDbStorage implements RatingStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private MPA rowMapToMpa(ResultSet resultSet, int i) throws SQLException {
        return MPA.builder()
                .id(resultSet.getInt("ratingMPAId"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public Optional<MPA> getRatingById(Integer mpaId) {
        String sqlQueryGetById = "SELECT * FROM MPARatings WHERE ratingMPAId = ?";
        List<MPA> MPAS = jdbcTemplate.query(sqlQueryGetById, (rs, rowNum) -> rowMapToMpa(rs, mpaId), mpaId);
        if (MPAS.isEmpty()) {
            log.info("Рейтинг с идентификатором {} не найден.", mpaId);
            return Optional.empty();
        }
        log.info("Найден рейтинг: {}", MPAS.get(0));
        return Optional.of(MPAS.get(0));
    }

    @Override
    public List<MPA> getRatingAll() {
        List<MPA> MPAS = new ArrayList<>();
        String sqlQueryGetAll = "SELECT * FROM MPARatings";
        try {
            MPAS = jdbcTemplate.query(sqlQueryGetAll, this::rowMapToMpa);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}", sqlQueryGetAll);
        }
        return MPAS;
    }
}