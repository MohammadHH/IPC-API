package com.exalt.ipc;

import com.exalt.ipc.configuration.PobulateIPCDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import java.util.Arrays;

@SpringBootApplication
public class IpcApplication {
	private static final Logger logger = LoggerFactory.getLogger(PobulateIPCDB.class);

	private static String beans = "\n The Beans \n";
	@Autowired
	MessageSource messageSource;

	public int add(int a,int b){
		return a+b;
	}
	protected String shsmo(){
		return "nothing";
	}

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(IpcApplication.class, args);
		Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach((bean) -> {
			if (!bean.contains("org")) {
				beans += bean + "\n";
			}
		});
		logger.info(beans);
	}
}
