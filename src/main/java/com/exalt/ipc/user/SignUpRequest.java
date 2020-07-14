package com.exalt.ipc.user;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(description = "User's sign up request information")
public class SignUpRequest {
    @Email
    @NotNull
    @ApiModelProperty(notes = "User's unique email")
    private String email;
    @NotNull
    @Size(min = 8)
    @ApiModelProperty(notes = "User's password, should be at least 8 chars")
    private String password;
    @NotBlank
    @Size(min = 2, max = 30)
    @ApiModelProperty(notes = "User's firstName, should be between 2 and 30 chars")
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 30)
    @ApiModelProperty(notes = "User's lastName, should be between 2 and 30 chars")
    private String lastName;

    public SignUpRequest() {
    }

    public SignUpRequest(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

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

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
