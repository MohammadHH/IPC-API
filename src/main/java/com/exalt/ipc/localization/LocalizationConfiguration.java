package com.exalt.ipc.localization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.exalt.ipc.localization")
public class LocalizationConfiguration {
    @Autowired
    MessageSource messageSource;

    @Bean
    public MessageSource myMessageSource() {
        return messageSource;
    }

}
