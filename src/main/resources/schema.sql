CREATE TABLE IF NOT EXISTS rating_mpa (
    rating_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    film_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    release_date DATE,
    duration INTEGER,
    rating_id INTEGER,
    FOREIGN KEY (rating_id) REFERENCES rating_mpa(rating_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL UNIQUE,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS user_friendships (
    user_id INTEGER,
    friend_id INTEGER,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CHECK (user_id <> friend_id)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id INTEGER,
    user_id INTEGER,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS films_genres (
    film_id INTEGER,
    genre_id INTEGER,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE
);

CREATE TABLE reviews (
    review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    is_positive BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    film_id BIGINT NOT NULL,
    useful INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id)
);

CREATE TABLE review_reactions (
    review_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    is_like BOOLEAN NOT NULL,
    PRIMARY KEY (review_id, user_id),
    FOREIGN KEY (review_id) REFERENCES reviews(review_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);