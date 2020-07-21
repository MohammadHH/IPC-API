package com.exalt.ipc.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class IPCUpdateRequest {
	@NotBlank
	@NotNull
	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
