package com.written.app.repository;

import com.written.app.model.Role;
import com.written.app.model.Token;
import com.written.app.model.TokenType;
import com.written.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
    }

    @Test
    public void TokenRepository_FindAllValidTokensByUser_ReturnTokens() {
        // given
        Token token = Token.builder()
                .user(user)
                .token("testJwtToken")
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);

        Token revokedToken = Token.builder()
                .user(user)
                .token("revokedJwtToken")
                .tokenType(TokenType.BEARER)
                .revoked(true)
                .expired(false)
                .build();
        tokenRepository.save(revokedToken);

        Token expiredToken = Token.builder()
                .user(user)
                .token("expiredJwtToken")
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(true)
                .build();
        tokenRepository.save(expiredToken);

        // when
        List<Token> allValidTokensByUser = tokenRepository.findAllValidTokensByUser(user.getId());

        // then
        assertThat(allValidTokensByUser).isNotNull();
        assertThat(allValidTokensByUser.size()).isEqualTo(1);
        assertThat(allValidTokensByUser.get(0).getUser()).isEqualTo(user);
        assertThat(allValidTokensByUser.get(0).isExpired()).isFalse();
        assertThat(allValidTokensByUser.get(0).isRevoked()).isFalse();
    }


    @Test
    public void TokenRepository_FindByToken_ReturnOptionalToken() {
        // given
        Token token = Token.builder()
                .token("jwt-token")
                .expired(false)
                .revoked(false)
                .user(user)
                .build();
        tokenRepository.save(token);

        // when
        Optional<Token> resultToken = tokenRepository.findByToken("jwt-token");

        // then
        assertThat(resultToken).isPresent();
        assertThat(resultToken.get().getToken()).isEqualTo("jwt-token");
        assertThat(resultToken.get().getUser()).isEqualTo(user);
    }

}
