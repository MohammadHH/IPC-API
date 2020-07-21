package com.exalt.ipc.exception;


import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

	private HttpStatus status;

	private int mainCode;

	private int[] subCodes;

	public ApiException(HttpStatus status, int mainCode, int... subCodes) {
		this.status = status;
		this.mainCode = mainCode;
		this.subCodes = subCodes;
	}

	public static void throwApiException(HttpStatus status, int mainCode, int... subCodes) {
		throw new ApiException(status, mainCode, subCodes);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public int getMainCode() {
		return mainCode;
	}

	public void setMainCode(int mainCode) {
		this.mainCode = mainCode;
	}

	public int[] getSubCodes() {
		return subCodes;
	}

	public void setSubCodes(int[] subCodes) {
		this.subCodes = subCodes;
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return super.getStackTrace();
	}
}
