package com.exalt.ipc.press;


import com.exalt.ipc.ipc.IPC;
import com.exalt.ipc.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class Press {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //    @NotNull
//    @NotBlank
    private String name;
    //    @NotNull
//    @NotBlank
    private String address;
    @DateTimeFormat
    private LocalDateTime creationDate;
    @Size(max = 250)
    private String description;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ipc_id")
    private IPC ipc;

    public Press() {
    }

    public Press(User user, @Size(max = 250) String description) {
        this.creationDate = LocalDateTime.now();
        this.description = description;
        this.user = user;
    }

    public Press(LocalDateTime creationDate, @Max(250) String description) {
        this.creationDate = creationDate;
        this.description = description;
        this.ipc = null;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IPC getIpc() {
        return ipc;
    }

    public void setIpc(IPC ipc) {
        this.ipc = ipc;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Press{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", description='" + description + '\'' +
                (getIpc() != null ? ", ipcId=" + getIpc().getId() : "") +
                '}';
    }
}
