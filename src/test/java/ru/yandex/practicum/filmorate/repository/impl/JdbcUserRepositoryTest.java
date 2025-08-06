package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcUserRepository.class})
class JdbcUserRepositoryTest {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    @BeforeEach
    public void init() {
        String query1 = "INSERT INTO users (user_id, email, login, name, birthday) VALUES (1, 'test1@test.ru', 'login1', 'name1', '2001-01-01');";
        String query2 = "INSERT INTO users (user_id, email, login, name, birthday) VALUES (2, 'test2@test.ru', 'login2', 'name2', '2002-02-02');";
        String query3 = "INSERT INTO users (user_id, email, login, name, birthday) VALUES (3, 'test3@test.ru', 'login3', 'name3', '2003-03-03');";
        String query4 = "INSERT INTO user_friendships (user_id, friend_id) VALUES (1, 2);";

        jdbcTemplate.execute(query1);
        jdbcTemplate.execute(query2);
        jdbcTemplate.execute(query3);
        jdbcTemplate.execute(query4);
    }

    @Test
    void shouldCheckUserExists() {
        boolean isExists1 = userRepository.userExists(1);
        boolean isExists2 = userRepository.userExists(100);

        assertThat(isExists1)
                .isEqualTo(true);
        assertThat(isExists2)
                .isEqualTo(false);
    }

    @Test
    public void shouldGetUserById() {
        User user = userRepository.getUserById(1);

        assertThat(user)
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday)
                .containsExactly(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1));
    }

    @Test
    void shouldAddFriendShips() {
        userRepository.addFriendShips(1, 3);
        User user1 = userRepository.getUserById(1);
        User user2 = userRepository.getUserById(3);

        assertThat(user1.getFriends())
                .isNotNull()
                .isEqualTo(Set.of(2, 3));
        assertThat(user2.getFriends())
                .isNotNull()
                .isEqualTo(Set.of());
    }

    @Test
    void shouldDeleteFriendShip() {
        userRepository.deleteFriendShip(1, 3);
        User user1 = userRepository.getUserById(1);
        User user2 = userRepository.getUserById(3);

        assertThat(user1.getFriends())
                .isNotNull()
                .isEqualTo(Set.of(2));
        assertThat(user2.getFriends())
                .isNotNull()
                .isEqualTo(Set.of());
    }

    @Test
    void shouldGetAllUsers() {
        List<User> allUsers = userRepository.allUsers();
        User user1 = userRepository.getUserById(1);
        User user2 = userRepository.getUserById(2);
        User user3 = userRepository.getUserById(3);

        AssertionsForInterfaceTypes.assertThat(allUsers)
                .isNotNull()
                .hasSize(3)
                .containsExactly(user1, user2, user3);
    }

    @Test
    void shouldUpdateUser() {
        User user1 = User.builder()
                .id(1)
                .name("updateName")
                .email("updateTest@test.ru")
                .login("updateLogin")
                .birthday(LocalDate.of(2011, 11, 11))
                .friends(new HashSet<>())
                .build();
        userRepository.updateUser(user1);
        user1 = userRepository.getUserById(1);

        assertThat(user1)
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday)
                .containsExactly(1, "updateTest@test.ru", "updateLogin", "updateName", LocalDate.of(2011, 11, 11));
    }

    @Test
    void shouldAddUser() {
        String query = "DELETE FROM users WHERE user_id = 1";
        jdbcTemplate.execute(query);

        User user4 = new User();
        user4.setEmail("test4@test.ru");
        user4.setLogin("login4");
        user4.setName("name4");
        user4.setBirthday(LocalDate.of(2004, 4, 4));

        userRepository.addUser(user4);

        assertThat(userRepository.getUserById(1))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday)
                .containsExactly(1, "test4@test.ru", "login4", "name4", LocalDate.of(2004, 4, 4));
    }

}