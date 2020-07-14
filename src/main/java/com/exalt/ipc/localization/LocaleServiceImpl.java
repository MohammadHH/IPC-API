package com.exalt.ipc.localization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class LocaleServiceImpl implements LocaleService {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LocaleResolver localeResolver;

    @Override
    public String getMessage(String code, HttpServletRequest request) {
        return messageSource.getMessage(code, null, localeResolver.resolveLocale(request));
    }

    @Override
    public String getMessage(int code, HttpServletRequest request) {
        return messageSource.getMessage("error." + code, null, localeResolver.resolveLocale(request));
    }

    @Override
    public String[] getMessage(HttpServletRequest request, int... codes) {
        String[] list = new String[codes.length * 2];
        for (int i = 0; i < codes.length * 2; i += 2) {
            list[i] = "" + codes[i / 2];
            list[i + 1] = messageSource.getMessage("error." + codes[i / 2], null, localeResolver.resolveLocale(request));
        }
        return list;
    }

    @Override
    public String[] getMessage(HttpServletRequest request, List<Integer> codes) {
        String[] list = new String[codes.size() * 2];
        for (int i = 0; i < codes.size() * 2; i += 2) {
            list[i] = "" + codes.get(i / 2);
            list[i + 1] = messageSource.getMessage("error." + codes.get(i / 2), null, localeResolver.resolveLocale(request));
        }
        return list;
    }

    @Override
    public String getMessage(String code) {
        return messageSource.getMessage(code, null, localeResolver.getDefaultLocale());
    }

    @Override
    public String getMessage(String code, WebRequest request) {
        return messageSource.getMessage(code, null, localeResolver.resolveLocale(request));
    }

    @Override
    public String[] getMessage(WebRequest request, int... codes) {
        String[] list = new String[codes.length * 2];
        for (int i = 0; i < codes.length * 2; i += 2) {
            list[i] = "" + codes[i / 2];
            list[i + 1] = messageSource.getMessage("error." + codes[i / 2], null, localeResolver.resolveLocale(request));
        }
        return list;
    }

}
