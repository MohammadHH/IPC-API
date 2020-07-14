package com.exalt.ipc.user;

import com.exalt.ipc.ipc.IPC;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
@ApiModel(description = "Database user entity")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The database generated user ID")
    private int id;
    @Email
    @NotNull
    @ApiModelProperty(notes = "User's unique email")
    private String email;
    @NotBlank
    @Size(min = 2, max = 30)
    @ApiModelProperty(notes = "User's firstName, should be between 2 and 30 chars")
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 30)
    @ApiModelProperty(notes = "User's lastName, should be between 2 and 30 chars")
    private String lastName;
    @JsonIgnore
    private String password;
    @ApiModelProperty(notes = "User's role, could be ADMIN_ROLE  or UESR_ROLE")
    private String role;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ipc_id")
    private IPC ipc;

    public User() {
    }

    public User(@Email String email, @NotBlank @Size(min = 2, max = 30) String firstName, @NotBlank @Size(min = 2, max = 30) String lastName, String password, String role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
    }

    public User(User user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public IPC getIpc() {
        return ipc;
    }

    public void setIpc(IPC ipc) {
        this.ipc = ipc;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lasName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
