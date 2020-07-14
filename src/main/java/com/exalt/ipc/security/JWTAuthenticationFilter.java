package com.exalt.ipc.security;

import com.auth0.jwt.JWT;
import com.exalt.ipc.exception.CustomException;
import com.exalt.ipc.localization.LocaleService;
import com.exalt.ipc.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.exalt.ipc.configuration.Constants.*;

/* when a user request a page, a login form popup with username and password
 * when credentials are entered they go to a UsernamePasswordAuthenticationFilter
 * this filter has attemptAuth and successAuth methods
 * here we get the credentials from the request
 * authenticationManager.authenticate attempts to authinticate the user
 * */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    @Autowired
    LocaleService localeService;

    @Autowired
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    // we parse the user's credentials and issue them to the AuthenticationManager
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
//            com.exalt.ipc.user.User credentials = new ObjectMapper().readValue(request.getInputStream(), com.exalt.ipc.user.User.class);
            BufferedReader x = request.getReader();
            System.out.println("BufferedReader");
            String s = x.lines().collect(Collectors.joining(System.lineSeparator()));
            s = URLDecoder.decode(s, String.valueOf(StandardCharsets.UTF_8));
            String s2[] = s.split("&");
            String username = s2[0].split("=")[1];
            String password = s2[1].split("=")[1];
            System.out.println("username: " + username + " password: " + password);
//            Map<String, String> credentials = new ObjectMapper().readValue(x, Map.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password,
                            new ArrayList<>())
            );
        } catch (IOException e) {
            //this exception is thrown if readValue method fails
            throw new RuntimeException(e);
        }
    }

    @Override
    //This method is called when the authentication successes
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((User) authResult.getPrincipal()).getUsername();
        String token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("role", userRepository.findByEmail(username).orElseThrow(() -> new CustomException(localeService.getMessage("error.user.not.found", request), HttpStatus.NOT_FOUND)).getRole())
                .sign(HMAC512(SECRET.getBytes()));
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}

