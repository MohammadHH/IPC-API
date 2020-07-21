package com.exalt.ipc.responses;

import java.time.LocalDateTime;

public class IPCResponse {
	private int id;

	private LocalDateTime creationDate;

	private String address;

	private int queueLimit;

	private int residual;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public int getQueueLimit() {
		return queueLimit;
	}

	public void setQueueLimit(int queueLimit) {
		this.queueLimit = queueLimit;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getResidual() {
		return residual;
	}

	public void setResidual(int residual) {
		this.residual = residual;
	}
}
