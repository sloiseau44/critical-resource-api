package com.criticalresource.criticalresourceapi.domain.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void should_save_and_find_user_by_username() {
        User user = User.builder()
                .username("jdupont")
                .email("jdupont@defense.fr")
                .password("encoded_password")
                .build();

        userRepository.save(user);

        assertThat(userRepository.findByUsername("jdupont")).isPresent();
    }
}