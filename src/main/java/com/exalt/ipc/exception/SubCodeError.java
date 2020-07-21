package com.exalt.ipc.exception;

public class SubCodeError {
	private int codeStatus;

	private String errorMessage;

	public SubCodeError(int codeStatus, String errorMessage) {
		this.codeStatus = codeStatus;
		this.errorMessage = errorMessage;
	}

	public int getCodeStatus() {
		return codeStatus;
	}

	public void setCodeStatus(int codeStatus) {
		this.codeStatus = codeStatus;
	}

	public String getErrorMessage() {
		return errorMessage;
	}


	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
