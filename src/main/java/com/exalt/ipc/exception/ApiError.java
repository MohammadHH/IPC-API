package com.exalt.ipc.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {
	private LocalDateTime timestamp;

	private int status;

	private String error;

	private String message;

	private List<SubCodeError> subErrors;

	private List<StackTraceElement> trace;

	public ApiError() {
	}

	public ApiError(int status, String error, String message, List<SubCodeError> subErrors,
			List<StackTraceElement> trace) {
		this.timestamp = LocalDateTime.now();
		this.status = status;
		this.error = error;
		this.message = message;
		this.subErrors = subErrors;
		this.trace = trace;
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

	public List<SubCodeError> getSubErrors() {
		return subErrors;
	}

	public void setSubErrors(List<SubCodeError> subErrors) {
		this.subErrors = subErrors;
	}

	public List<StackTraceElement> getTrace() {
		return trace;
	}

	public void setTrace(List<StackTraceElement> trace) {
		this.trace = trace;
	}
}
