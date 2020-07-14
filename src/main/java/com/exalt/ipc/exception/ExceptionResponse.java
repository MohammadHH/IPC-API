package com.exalt.ipc.exception;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class ExceptionResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private List<Map<String, String>> subErrors;
    private List<StackTraceElement> trace;

    public ExceptionResponse() {
    }

    public ExceptionResponse(CustomException exc) {
        this.timestamp = exc.getTimestamp();
        this.status = exc.getStatus();
        this.error = exc.getError();
        this.message = exc.getMessage();
        this.subErrors = exc.getSubErrors();
        this.trace = exc.getTrace();
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
        return trace;
    }

    public void setTrace(List<StackTraceElement> trace) {
        this.trace = trace;
    }
}
