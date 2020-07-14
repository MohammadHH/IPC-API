package com.exalt.ipc.security;

import com.exalt.ipc.exception.CustomException;
import com.exalt.ipc.localization.LocaleService;
import com.exalt.ipc.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
//for authorization
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    LocaleService localeService;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.exalt.ipc.user.User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(localeService.getMessage("error.user.not.found"), HttpStatus.NOT_FOUND));
        User.UserBuilder builder = null;

        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        builder = User.withUsername(email);
        builder.password(user.getPassword());
        builder.authorities(user.getRole());
        return builder.build();
    }

}
