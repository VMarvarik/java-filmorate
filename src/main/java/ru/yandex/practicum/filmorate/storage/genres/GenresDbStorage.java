package ru.yandex.practicum.filmorate.storage.genres;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("GenresDbStorage")
@Slf4j
public class GenresDbStorage implements GenresStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenresDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Genre rowMapToGenre(ResultSet resultSet, int i) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genreId"))
                .name(resultSet.getString("genre"))
                .build();
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        if (genreId == null) {
            return null;
        }
        Genre genre = null;
        String sqlQueryGetById = "select * from genreNames where genreId=?";
        try {
            genre = jdbcTemplate.queryForObject(sqlQueryGetById, this::rowMapToGenre, genreId);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу  {}.  id={}", sqlQueryGetById, genreId);
        }
        return genre;
    }

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        String sqlQueryGetAllGenres = "select * from genreNames";
        try {
            genres = jdbcTemplate.query(sqlQueryGetAllGenres, this::rowMapToGenre);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}", sqlQueryGetAllGenres);
        }
        return genres;
    }

    @Override
    public Set<Genre> getGenresOfFilm(Long filmId) {
        Set<Genre> genres = new LinkedHashSet<>();
        String sqlQueryGetGenres = "select gn.genreId, gn.genre " +
                "from genre g left join genreNames gn on g.genreId = gn.genreId " +
                " where g.filmId = ?";
        try {
            genres.addAll(jdbcTemplate.query(sqlQueryGetGenres, this::rowMapToGenre, filmId));
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}. id film = {}", sqlQueryGetGenres, filmId);
        }
        return genres;
    }

    @Override
    public void createGenre(Genre genre) {
        String sqlQueryCreateGenre = "insert into genreNames(genre) values(?)";
        jdbcTemplate.update(sqlQueryCreateGenre, genre.getName());
    }

    @Override
    public Boolean containsGenre(Integer genreId) {
        String sqlQueryContainsGenre = "select count(*) from genreNames where genreId = ?";
        int containsGenre = 0;
        try {
            containsGenre = jdbcTemplate.queryForObject(sqlQueryContainsGenre, Integer.class, genreId);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}.  genreId={}",
                    sqlQueryContainsGenre, genreId);
        }
        return containsGenre > 0;
    }
}