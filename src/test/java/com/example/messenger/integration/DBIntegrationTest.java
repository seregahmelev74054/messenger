package com.example.messenger.integration;

import com.example.messenger.USER.entity.User;
import com.example.messenger.USER.repository.UserRepository;
import com.example.messenger.TestPostgresDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DBIntegrationTest extends TestPostgresDb {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void dbIsInit() {

        Integer usersCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users",
                Integer.class
        );

        assertThat(usersCount).isOne();

        Integer conversationsCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM conversations",
                Integer.class
        );

        assertThat(conversationsCount).isZero();

        Integer membersCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM members",
                Integer.class
        );

        assertThat(membersCount).isZero();

        Integer messagesCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM messages",
                Integer.class
        );

        assertThat(messagesCount).isZero();

        Integer messageDeletionsCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM message_deletions",
                Integer.class
        );

        assertThat(messageDeletionsCount).isZero();
    }

    @Autowired
    UserRepository userRepository;

    @Test
    void shouldSaveUserToPostgres() {
        String email = "testEmail";
        User user = new User();
        user.setEmail(email);
        user.setLogin("testLogin");
        user.setDisplayName("testDisplayName");
        user.setPasswordHash("testPasswordHash");
        userRepository.save(user);
        assertThat(userRepository.findByEmail(email)).isPresent();
    }
}
