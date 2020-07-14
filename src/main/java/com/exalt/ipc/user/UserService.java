package com.exalt.ipc.user;

import com.auth0.jwt.JWT;
import com.exalt.ipc.configuration.Encoder;
import com.exalt.ipc.exception.CustomException;
import com.exalt.ipc.helpers.HelperSerivce;
import com.exalt.ipc.ipc.IPC;
import com.exalt.ipc.ipc.IPCRepository;
import com.exalt.ipc.localization.LocaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.exalt.ipc.configuration.Constants.EXPIRATION_TIME;
import static com.exalt.ipc.configuration.Constants.SECRET;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocaleService localeService;
    @Autowired
    private HelperSerivce helperSerivce;
    @Autowired
    IPCRepository ipcRepository;

    public String login(SignInRequest signInRequest, HttpServletRequest request) {
        CustomException customException = new CustomException("7000", request, HttpStatus.UNAUTHORIZED, localeService, 7001);
        User user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(() -> customException);
        if (Encoder.passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            System.out.println("inside signIn and true authentication");
            String token = JWT.create()
                    .withSubject(user.getEmail())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .withClaim("role", user.getRole())
                    .sign(HMAC512(SECRET.getBytes()));
            return token;
        } else
            throw customException;
    }

    public User signUP(SignUpRequest signUpRequest, String role, HttpServletRequest request) {
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(Encoder.passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(role);
        user.setIpc(new IPC());
        if (!userRepository.findByEmail(user.getEmail()).isPresent()) {
            User savedUser = userRepository.save(user);
            return savedUser;
        } else {
            throw new CustomException("7006", request, HttpStatus.BAD_REQUEST, localeService, 7007);
        }
    }

    public User updateUser(SignUpRequest updateRequest, HttpServletRequest request) {
        Optional<User> userToUpdate = userRepository.findByEmail(updateRequest.getEmail());
        if (userToUpdate.isPresent()) {
            User user = userToUpdate.get();
            user.setFirstName(updateRequest.getFirstName());
            user.setLastName(updateRequest.getLastName());
            user.setEmail(updateRequest.getEmail());
            user.setPassword(Encoder.passwordEncoder.encode(updateRequest.getPassword()));
            return userRepository.save(user);
        } else {
            throw new CustomException("7008", request, HttpStatus.BAD_REQUEST, localeService, 7003, 7004);
        }
    }


    public Boolean deleteUser(User userToDelete) {
        userRepository.delete(userToDelete);
        return true;
    }


    //try getting the user,throw 403 forbidden error if the user is deleted
    public User getUser(String jwt, HttpServletRequest request) {
        CustomException ex = new CustomException("7020", request, HttpStatus.FORBIDDEN, localeService, 7021);
        return userRepository.findByEmail(helperSerivce.getEmail(jwt)).orElseThrow(() -> ex);
    }
}
