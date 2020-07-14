package com.exalt.ipc.configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Encoder {
    public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
}
