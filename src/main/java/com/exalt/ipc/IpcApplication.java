package com.exalt.ipc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

@SpringBootApplication
public class IpcApplication {
	@Autowired
	MessageSource messageSource;

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(IpcApplication.class, args);
	}
}
