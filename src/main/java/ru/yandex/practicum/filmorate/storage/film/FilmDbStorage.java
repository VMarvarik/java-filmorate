package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.error.exception.NullException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.genres.GenresStorage;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenresStorage genresStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenresStorage genresStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genresStorage = genresStorage;
    }

    private Film rowMapToFilm(ResultSet resultSet, int i) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name((resultSet.getString("name")))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("releaseDate").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getDouble("rate"))
                .rating(Rating.builder()
                        .id(resultSet.getInt("ratingMPAId"))
                        .name(resultSet.getString("name"))
                        .build())
                .build();
    }

    @Override
    public Film add(Film film) {
        if (film == null) {
            throw new NullException("Фильм не может быть null");
        }
        String sqlQueryAdd = "insert into films(name,description,releaseDate,duration,rate,ratingMPAId)" +
                " values(?,?,?,?,?,?)";

        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sqlQueryAdd, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, film.getName());
                ps.setString(2, film.getDescription());
                ps.setDate(3, Date.valueOf(film.getReleaseDate()));
                ps.setInt(4, film.getDuration());
                ps.setDouble(5, film.getRate());
                ps.setInt(6, film.getRating().getId());
                return ps;
            }
        };

        GeneratedKeyHolder gkh = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, gkh);
        film.setId(Objects.requireNonNull(gkh.getKey()).longValue());

        if (film.getGenres() != null) {
            String sqlQueryCreateGenreTable = "insert into genre(filmId, genreId) values(?,?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlQueryCreateGenreTable, film.getId(), genre.getId());
            }
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film == null) {
            throw new NullException("Фильм не может быть null");
        }
        String sqlQueryUpdate = "update films " +
                "set name = ?, description = ?, releaseDate = ?, duration = ?, rate= ?, ratingMPAId = ?" +
                "where id = ?";
        jdbcTemplate.update(sqlQueryUpdate,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getRating().getId(),
                film.getId());
        String sqlQueryDeleteGenreForUpdate = "delete from genre where filmId = ?";
        jdbcTemplate.update(sqlQueryDeleteGenreForUpdate, film.getId());
        if (film.getGenres() != null) {
            Set<Genre> newGenres = new HashSet<>();
            String sqlQueryCreateGenreTable = "insert into genre(filmId, genreId) values(?,?)";
            for (Genre genre : film.getGenres()) {
                try {
                    jdbcTemplate.update(sqlQueryCreateGenreTable, film.getId(), genre.getId());
                    newGenres.add(genre);
                } catch (DuplicateKeyException e) {
                    log.info("В таблице genres уже есть такое значение");
                }
            }
            film.setGenres(newGenres);
        }
        return film;
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            return;
        }
        String sqlQueryDelete = "delete from films where id = ?";
        jdbcTemplate.update(sqlQueryDelete,
                id);
    }

    @Override
    public List<Film> getAll() {
        String sqlQueryGetAll = "select *" +
                "from films";
        List<Film> films = new ArrayList<>();
        try {
            films = jdbcTemplate.query(sqlQueryGetAll, this::rowMapToFilm);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет данных по запросу {}", sqlQueryGetAll);
        }
        for (Film film : films) {
            film.setGenres(genresStorage.getGenresOfFilm(film.getId()));
        }
        return films;
    }

    @Override
    public Film getById(Long id) {
        if (id == null) {
            return null;
        }
        String sqlQueryGetById = "select *" +
                " from films " +
                " where id = ?";
        Film film = null;
        try {
            film = jdbcTemplate.queryForObject(sqlQueryGetById, this::rowMapToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Не найдена информация в базе данных по запросу {}.  id={}", sqlQueryGetById, id);
        }
        if (film != null) {
            film.setGenres(genresStorage.getGenresOfFilm(film.getId()));
        }
        return film;
    }

    @Override
    public Map<Long, Film> getFilmMap() {
        String sqlQueryGetFilmMap = "select *" +
                " from films " +
                " where id = ?";
        List<Film> filmList = jdbcTemplate.query(sqlQueryGetFilmMap, this::rowMapToFilm);
        Map<Long, Film> result = new HashMap<>();
        for (Film film : filmList) {
            film.setGenres(genresStorage.getGenresOfFilm(film.getId()));
            result.put(film.getId(), film);
        }
        return result;
    }
}