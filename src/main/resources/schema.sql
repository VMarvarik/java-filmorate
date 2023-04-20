CREATE TABLE IF NOT EXISTS films(
        id LONG PRIMARY KEY AUTO_INCREMENT,
        name VARCHAR NOT NULL,
        description VARCHAR(200),
        releaseDate DATE,
        duration INTEGER,
        rate LONG,
        ratingMPAId INTEGER
);

CREATE TABLE IF NOT EXISTS users(
        id LONG PRIMARY KEY AUTO_INCREMENT,
        email VARCHAR NOT NULL,
        login VARCHAR NOT NULL,
        name VARCHAR,
        birthday DATE
);

CREATE TABLE IF NOT EXISTS genreNames(
         genreId INTEGER PRIMARY KEY AUTO_INCREMENT,
         genre VARCHAR
);

CREATE TABLE IF NOT EXISTS genre(
         filmId INTEGER,
         genreId INTEGER,
         PRIMARY KEY (filmId, genreId)
);

CREATE TABLE IF NOT EXISTS MPARatings(
        ratingMPAId INTEGER PRIMARY KEY AUTO_INCREMENT,
        name VARCHAR
);

CREATE TABLE IF NOT EXISTS filmLikes(
         filmId INTEGER,
         userId LONG,
         PRIMARY KEY (filmId, userId)
);

CREATE TABLE IF NOT EXISTS userFriends(
        userId LONG,
        friendsId LONG,
        status BOOLEAN,
        PRIMARY KEY (userId, friendsId)
);