package ru.yandex.practicum.filmorate.service.rating;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import java.util.List;

@Service("RatingService")
@Slf4j
public class RatingService {

    private final RatingStorage ratingStorage;

    @Autowired
    public RatingService(@Qualifier("RatingDbStorage") RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public Rating getRatingById(Integer id) {
        Rating rating = ratingStorage.getRatingById(id);
        if (rating == null) {
            log.info("Rating с id " + id + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return rating;
    }

    public List<Rating> getRatingAll() {
        return ratingStorage.getRatingAll();
    }
}