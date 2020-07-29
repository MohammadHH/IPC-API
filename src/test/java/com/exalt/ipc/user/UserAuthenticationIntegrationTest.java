package com.exalt.ipc.user;

import com.exalt.ipc.entities.User;
import com.exalt.ipc.requests.SignInRequest;
import com.exalt.ipc.requests.SignUpRequest;
import com.exalt.ipc.responses.JwtResponse;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static com.exalt.ipc.configuration.RoleConstants.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:integration_test.properties")
public class UserAuthenticationIntegrationTest {

	public static final String URL = "/v1/users/signup";

	public static final String SIGNUP_URL = URL;

	public static final String EMAIL = "mohammad@gmail.com";

	public static final String PASSWORD = "12345678";

	public static final String FIRST_NAME = "Mohammad";

	public static final String LAST_NAME = "Hasan";

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	@Order(1)
	public void givenSignupResquest_whenSignup_thenUserIsCreated() {
		ResponseEntity<User> response =
				restTemplate.postForEntity(SIGNUP_URL, new SignUpRequest(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME), User.class);
		assertThat(response.getBody().getFirstName()).isEqualTo(FIRST_NAME);
		assertThat(response.getBody().getLastName()).isEqualTo(LAST_NAME);
		assertThat(response.getBody().getEmail()).isEqualTo(EMAIL);
		assertThat(response.getBody().getRole()).isEqualTo(ROLE_USER);
		assertThat(response.getStatusCode().equals(HttpStatus.OK));
	}

	@Test
	@Order(2)
	public void givenSignInResquest_whenLogin_thenJwtIsReturned() {
		ResponseEntity<JwtResponse> response =
				restTemplate.postForEntity("/v1/users/login", new SignInRequest(EMAIL, PASSWORD), JwtResponse.class);
		assertThat(response.getBody().getEmail()).isEqualTo(EMAIL);
		assertThat(response.getBody().getRole()).isEqualTo(ROLE_USER);
		assertThat(response.getBody().getToken()).isNotEmpty().isNotNull();
		assertThat(response.getStatusCode().equals(HttpStatus.OK));
	}

}
