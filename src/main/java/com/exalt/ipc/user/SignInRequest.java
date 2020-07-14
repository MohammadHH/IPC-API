package com.exalt.ipc.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(description = "User's sign in request credentials")
public class SignInRequest {
    @ApiModelProperty(notes = "User's email, should be a well-formed email")
    @Email
    @NotNull
    @NotBlank
    private String email;
    @ApiModelProperty(notes = "User's password, should be at least 8 charachters")
    @NotBlank
    @NotNull
    @Size(min = 8)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
