CREATE TABLE IF NOT EXISTS rating_mpa (
    rating_id INTEGER PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id INTEGER PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    film_id INTEGER PRIMARY KEY,
    name VARCHAR NOT NULL,
    description TEXT,
    release_date DATE,
    duration TIME,
    rating_id INTEGER,
    FOREIGN KEY (rating_id) REFERENCES rating_mpa(rating_id)
);

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    login VARCHAR NOT NULL,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS user_friendships (
    user_id INTEGER,
    friend_id INTEGER,
    status VARCHAR,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (friend_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id INTEGER,
    user_id INTEGER,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS films_genres (
    film_id INTEGER,
    genre_id INTEGER,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id),
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id)
);
