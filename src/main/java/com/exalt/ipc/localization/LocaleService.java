package com.exalt.ipc.localization;

import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface LocaleService {
    String getMessage(String code, HttpServletRequest request);

    String getMessage(int code, HttpServletRequest request);

    String[] getMessage(HttpServletRequest request, int... code);

    String[] getMessage(HttpServletRequest request, List<Integer> codes);

    String getMessage(String code);

    String getMessage(String s, WebRequest request);

    String[] getMessage(WebRequest request, int[] codes);
}
