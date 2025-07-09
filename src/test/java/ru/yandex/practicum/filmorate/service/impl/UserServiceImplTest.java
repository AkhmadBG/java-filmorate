package ru.yandex.practicum.filmorate.service.impl;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserServiceImplTest {

    private InMemoryUserStorage inMemoryUserStorage;
    private UserService userService;

    @BeforeEach
    public void init() {
        inMemoryUserStorage = new InMemoryUserStorage();
        userService = new UserServiceImpl(inMemoryUserStorage);
    }

    @Test
    void shouldGetUserById() {
        Set<Integer> friendsList = Set.of(1, 2, 3);
        User user = new User();
        user.setEmail("test1@test.ru");
        user.setBirthday(LocalDate.of(2001, 1, 1));
        user.setLogin("login1");
        user.setName("name1");
        user.setId(1);
        user.setFriendsId(friendsList);

        userService.addUser(user);

        assertThat(userService.getUserById(1))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriendsId)
                .containsExactly(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList);
    }

    @Test
    void shouldGetAllUsers() {
        Set<Integer> friendsList1 = Set.of(1, 2, 3);
        User user1 = new User();
        user1.setEmail("test1@test.ru");
        user1.setBirthday(LocalDate.of(2001, 1, 1));
        user1.setLogin("login1");
        user1.setName("name1");
        user1.setId(1);
        user1.setFriendsId(friendsList1);

        userService.addUser(user1);

        Set<Integer> friendsList2 = Set.of(4, 5, 6);
        User user2 = new User();
        user2.setEmail("test2@test.ru");
        user2.setBirthday(LocalDate.of(2002, 2, 2));
        user2.setLogin("login2");
        user2.setName("name2");
        user2.setId(2);
        user2.setFriendsId(friendsList2);

        userService.addUser(user2);

        AssertionsForInterfaceTypes.assertThat(userService.getAllUsers())
                .isNotNull()
                .hasSize(2)
                .containsExactly(user1, user2);
    }

    @Test
    void shouldAddUser() {
        Set<Integer> friendsList = Set.of(1, 2, 3);
        User user = new User();
        user.setEmail("test1@test.ru");
        user.setBirthday(LocalDate.of(2001, 1, 1));
        user.setLogin("login1");
        user.setName("name1");
        user.setId(1);
        user.setFriendsId(friendsList);

        userService.addUser(user);

        assertThat(userService.getUserById(1))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriendsId)
                .containsExactly(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), friendsList);
    }

    @Test
    void shouldUpdateUser() {
        Set<Integer> friendsList1 = Set.of(1, 2, 3);
        User user1 = new User();
        user1.setEmail("test1@test.ru");
        user1.setBirthday(LocalDate.of(2001, 1, 1));
        user1.setLogin("login1");
        user1.setName("name1");
        user1.setId(1);
        user1.setFriendsId(friendsList1);

        userService.addUser(user1);

        Set<Integer> friendsList2 = Set.of(4, 5, 6);
        User user2 = new User();
        user2.setEmail("test2@test.ru");
        user2.setBirthday(LocalDate.of(2002, 2, 2));
        user2.setLogin("login2");
        user2.setName("name2");
        user2.setId(1);
        user2.setFriendsId(friendsList2);

        userService.updateUser(user2);

        assertThat(userService.getUserById(1))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriendsId)
                .containsExactly(1, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), friendsList2);
    }

    @Test
    void shouldAddFriend() {
        Set<Integer> friendsList1 = new HashSet<>();
        User user1 = new User();
        user1.setEmail("test1@test.ru");
        user1.setBirthday(LocalDate.of(2001, 1, 1));
        user1.setLogin("login1");
        user1.setName("name1");
        user1.setId(1);
        user1.setFriendsId(friendsList1);

        userService.addUser(user1);

        Set<Integer> friendsList2 = new HashSet<>();
        User user2 = new User();
        user2.setEmail("test2@test.ru");
        user2.setBirthday(LocalDate.of(2002, 2, 2));
        user2.setLogin("login2");
        user2.setName("name2");
        user2.setId(2);
        user2.setFriendsId(friendsList2);

        userService.addUser(user2);
        userService.addFriend(user1.getId(), user2.getId());

        assertThat(userService.userFriends(user1.getId()).get(0))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriendsId)
                .containsExactly(2, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), friendsList2);
    }

    @Test
    void shouldDeleteFriend() {
        Set<Integer> friendsList1 = new HashSet<>();
        User user1 = new User();
        user1.setEmail("test1@test.ru");
        user1.setBirthday(LocalDate.of(2001, 1, 1));
        user1.setLogin("login1");
        user1.setName("name1");
        user1.setId(1);
        user1.setFriendsId(friendsList1);

        userService.addUser(user1);

        Set<Integer> friendsList2 = new HashSet<>();
        User user2 = new User();
        user2.setEmail("test2@test.ru");
        user2.setBirthday(LocalDate.of(2002, 2, 2));
        user2.setLogin("login2");
        user2.setName("name2");
        user2.setId(2);
        user2.setFriendsId(friendsList2);

        userService.addUser(user2);
        userService.addFriend(user1.getId(), user2.getId());

        assertThat(userService.userFriends(user1.getId()).get(0))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriendsId)
                .containsExactly(2, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), friendsList2);

        userService.deleteFriend(user1.getId(), user2.getId());

        assertThat(userService.getUserById(1))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriendsId)
                .containsExactly(1, "test1@test.ru", "login1", "name1", LocalDate.of(2001, 1, 1), Set.of());

        assertThat(inMemoryUserStorage.getUserById(2))
                .isNotNull()
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday, User::getFriendsId)
                .containsExactly(2, "test2@test.ru", "login2", "name2", LocalDate.of(2002, 2, 2), Set.of());
    }

    @Test
    void shouldReturnUserFriends() {
        Set<Integer> friendsList1 = new HashSet<>();
        User user1 = new User();
        user1.setEmail("test1@test.ru");
        user1.setBirthday(LocalDate.of(2001, 1, 1));
        user1.setLogin("login1");
        user1.setName("name1");
        user1.setId(1);
        user1.setFriendsId(friendsList1);

        userService.addUser(user1);

        Set<Integer> friendsList2 = new HashSet<>();
        User user2 = new User();
        user2.setEmail("test2@test.ru");
        user2.setBirthday(LocalDate.of(2002, 2, 2));
        user2.setLogin("login2");
        user2.setName("name2");
        user2.setId(2);
        user2.setFriendsId(friendsList2);

        userService.addUser(user2);
        userService.addFriend(user1.getId(), user2.getId());

        assertThat(userService.userFriends(1))
                .isNotNull()
                .isEqualTo(List.of(user2));
    }

}