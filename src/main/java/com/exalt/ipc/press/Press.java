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
    @DateTimeFormat
    private LocalDateTime creationDate;
    @Size(max = 250)
    private String description;
    private boolean mapped;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ipc_id")
    private IPC ipc;

    public Press() {
    }

    public Press(User user, @Size(max = 250) String description) {
        this.creationDate = LocalDateTime.now();
        this.description = description;
        this.user = user;
        this.mapped = false;
    }

    public Press(LocalDateTime creationDate, @Max(250) String description) {
        this.creationDate = creationDate;
        this.description = description;
        this.ipc = ipc;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public IPC getIpc() {
        return ipc;
    }

    public void setIpc(IPC ipc) {
        this.ipc = ipc;
    }

    public boolean isMapped() {
        return mapped;
    }

    public void setMapped(boolean mapped) {
        this.mapped = mapped;
    }
    

    @Override
    public String toString() {
        return "Press{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", description='" + description + '\'' +
                (getUser() != null ? ", userId=" + getUser().getId() : "") +
                (getIpc() != null ? ", ipcId=" + getIpc().getId() : "") +
                '}';
    }
}
