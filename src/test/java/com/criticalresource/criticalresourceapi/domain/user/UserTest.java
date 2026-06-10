package com.criticalresource.criticalresourceapi.domain.user;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    @Test
    void should_create_user_with_valid_fields() {
        User user = User.builder()
                .username("jdupont")
                .email("jdupont@test.fr")
                .password("encoded_password")
                .role(Role.ADMIN)
                .build();

        assertThat(user.getUsername()).isEqualTo("jdupont");
        assertThat(user.getEmail()).isEqualTo("jdupont@test.fr");
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
        assertThat(user.isEnabled()).isTrue();
    }
}
