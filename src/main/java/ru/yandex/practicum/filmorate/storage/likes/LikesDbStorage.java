package ru.yandex.practicum.filmorate.storage.likes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Component("LikesDbStorage")
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void removeLike(Long idFilm, Long userId) {
        String sqlQueryRemoveLike = "delete from filmLikes where filmId = ? and userId = ?";
        jdbcTemplate.update(sqlQueryRemoveLike,
                idFilm,
                userId);
    }

    @Override
    public void addLike(Long id, Long userId) {
        String sqlQueryAdd = "insert into filmLikes(filmId,userId)" +
                " values(?,?)";
        jdbcTemplate.update(sqlQueryAdd,
                id,
                userId);
    }

    @Override
    public Integer getAmountOfLikes(Long filmId, Long userId) {
        int amount = 0;
        String sqlAmountOfLikes = "select count(*) " +
                "from public.filmLikes where filmId=? and userId = ?";
        try {
            amount = jdbcTemplate.queryForObject(sqlAmountOfLikes, Integer.class, filmId, userId);
        } catch (EmptyResultDataAccessException e) {
            log.info("В базе нет информации по запросу {}. filmId={}, userId={}",
                    sqlAmountOfLikes, filmId, userId);
        }
        return amount;
    }

    @Override
    public Set<Long> getTopFilmLikes() {
        String sqlQueryTopFilmLikes =
                "select t.id  " +
                "from films t left join " +
                "(select filmId as id, count(userId) as count from filmLikes group by filmId) AS cn ON t.id = cn.id " +
                "order by (COALESCE(t.RATE, 0) + COALESCE(cn.count, 0)) desc";
        return new LinkedHashSet<>(jdbcTemplate.queryForList(sqlQueryTopFilmLikes, Long.class));
    }
}