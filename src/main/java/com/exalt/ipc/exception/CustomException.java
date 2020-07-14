package com.exalt.ipc.exception;

import com.exalt.ipc.localization.LocaleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomException extends RuntimeException {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private List<Map<String, String>> subErrors;

    public CustomException(String message, HttpStatus httpStatus) {
        super(message);
        this.timestamp = LocalDateTime.now();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.subErrors = new LinkedList<>();
    }

    public CustomException(String message, HttpStatus httpStatus, List<Map<String, String>> subErrors) {
        super(message);
        this.timestamp = LocalDateTime.now();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.subErrors = subErrors;
    }

    public CustomException(String message, HttpServletRequest request, HttpStatus httpStatus, LocaleService localeService, int... codes) {
        super(message);
        this.timestamp = LocalDateTime.now();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = localeService.getMessage("error." + message, request);
        this.subErrors = ExceptionHelper.subErrorsCodes(localeService.getMessage(request, codes));
    }

    public CustomException(String message, HttpServletRequest request, HttpStatus httpStatus, LocaleService localeService, List<Integer> codes) {
        super(message);
        this.timestamp = LocalDateTime.now();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = localeService.getMessage("error." + message, request);
        this.subErrors = ExceptionHelper.subErrorsCodes(localeService.getMessage(request, codes));
    }

    public CustomException(String message, WebRequest request, HttpStatus httpStatus, LocaleService localeService, int... codes) {
        super(message);
        this.timestamp = LocalDateTime.now();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = localeService.getMessage("error." + message, request);
        this.subErrors = ExceptionHelper.subErrorsCodes(localeService.getMessage(request, codes));
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, String>> getSubErrors() {
        return subErrors;
    }

    public void setSubErrors(List<Map<String, String>> subErrors) {
        this.subErrors = subErrors;
    }

    public List<StackTraceElement> getTrace() {
        return Arrays.stream(super.getStackTrace()).limit(5).collect(Collectors.toList());
    }

    public void setTrace(StackTraceElement[] trace) {
        super.setStackTrace(trace);
    }
}
