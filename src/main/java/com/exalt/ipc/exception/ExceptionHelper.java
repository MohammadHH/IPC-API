package com.exalt.ipc.exception;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExceptionHelper {
    public static List<Map<String, String>> subErrors(String... array) {
        List subErrors = new LinkedList();
        Map map = new HashMap();
        for (int i = 0; i < array.length; i++) {
            if (i % 2 == 0) {
                map = new HashMap();
                map.put("fieldName", array[i]);
            } else {
                map.put("errorMessage", array[i]);
                subErrors.add(map);
            }
        }
        return subErrors;
    }

    public static List<Map<String, String>> subErrorsCodes(String... array) {
        List subErrors = new LinkedList();
        Map map = new HashMap();
        for (int i = 0; i < array.length; i++) {
            if (i % 2 == 0) {
                map = new HashMap();
                map.put("codeStatus", array[i]);
            } else {
                map.put("errorMessage", array[i]);
                subErrors.add(map);
            }
        }
        return subErrors;
    }
}
