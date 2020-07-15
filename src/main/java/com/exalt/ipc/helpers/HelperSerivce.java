package com.exalt.ipc.helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.exalt.ipc.exception.CustomException;
import com.exalt.ipc.job.File;
import com.exalt.ipc.job.FileRepository;
import com.exalt.ipc.localization.LocaleService;
import com.exalt.ipc.press.Press;
import com.exalt.ipc.press.PressRepository;
import com.exalt.ipc.user.UserRepository;
import com.exalt.ipc.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.exalt.ipc.configuration.Constants.SECRET;
import static com.exalt.ipc.configuration.Constants.TOKEN_PREFIX;

@Service
public class HelperSerivce {
    @Autowired
    UserService userService;
    @Autowired
    PressRepository pressRepository;
    @Autowired
    LocaleService localeService;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    UserRepository userRepository;

    public String getEmail(String jwt) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(jwt.replace(TOKEN_PREFIX, "")).getSubject();
    }

    public boolean isOwner(String email, Object resource, HttpServletRequest request) {
        if (resource instanceof Press) {
            CustomException ex = new CustomException("7040", request, HttpStatus.FORBIDDEN, localeService, 7041);
            pressRepository.
                    findByIdAndUserEmail(((Press) resource).getId(), email).orElseThrow(() -> ex);
            return true;
        } else if (resource instanceof File) {
            CustomException ex = new CustomException("7040", request, HttpStatus.FORBIDDEN, localeService, 7042);
            fileRepository.
                    findByIdAndUserEmail(((File) resource).getId(), email).orElseThrow(() -> ex);
            return true;
        }
        return false;
    }
}
