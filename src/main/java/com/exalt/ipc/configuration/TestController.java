package com.exalt.ipc.configuration;

import com.exalt.ipc.exception.CustomException;
import com.exalt.ipc.localization.LocaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    LocaleService localeService;

    @GetMapping("/hey")
    public String printHello() {
//        System.out.println(" WebRequest" + request.getHeader("Accept-Language"));
        if (2 == 2) throw new CustomException("Hey", HttpStatus.BAD_REQUEST);
        return "Hello";
    }
}
