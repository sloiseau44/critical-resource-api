package com.criticalresource.criticalresourceapi.auth;

import com.criticalresource.criticalresourceapi.domain.user.Role;
import com.criticalresource.criticalresourceapi.domain.user.User;
import com.criticalresource.criticalresourceapi.domain.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    public void should_extract_token_when_header_is_valid() {
        String header = "Bearer fake.jwt.token";

        String token = jwtAuthenticationFilter.extractTokenFromHeader(header);

        assertThat(token).isEqualTo("fake.jwt.token");
    }

    @Test
    public void should_return_null_when_header_is_missing() {
        String token = jwtAuthenticationFilter.extractTokenFromHeader(null);

        assertThat(token).isNull();
    }

    @Test
    public void  should_return_null_when_header_does_not_start_with_bearer() {
        String header = "fake.jwt.token";

        String token = jwtAuthenticationFilter.extractTokenFromHeader(header);

        assertThat(token).isNull();
    }

    @Test
    public void should_not_authenticate_when_token_is_missing() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    public void should_authenticate_user_when_token_is_valid() throws ServletException, IOException {
        User user = User.builder()
                .username("jdupont")
                .email("jdupont@test.fr")
                .password("encoded_password")
                .role(Role.OPERATEUR)
                .build();

        when(request.getHeader("Authorization")).thenReturn("Bearer fake.jwt.token");
        when(jwtService.isTokenValid("fake.jwt.token")).thenReturn(true);
        when(jwtService.extractUsername("fake.jwt.token")).thenReturn("jdupont");
        when(userRepository.findByUsername("jdupont")).thenReturn(Optional.of(user));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("jdupont");

    }
}
