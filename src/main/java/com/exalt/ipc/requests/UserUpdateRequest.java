package com.exalt.ipc.requests;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(description = "User's update request")
public class UserUpdateRequest {
	@NotBlank
	@Size(min = 2, max = 30)
	@ApiModelProperty(notes = "User's firstName, should be between 2 and 30 chars")
	private String firstName;

	@NotBlank
	@Size(min = 2, max = 30)
	@ApiModelProperty(notes = "User's lastName, should be between 2 and 30 chars")
	private String lastName;

	@NotNull
	@Size(min = 8)
	@ApiModelProperty(notes = "User's password, should be at least 8 chars")
	private String password;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
