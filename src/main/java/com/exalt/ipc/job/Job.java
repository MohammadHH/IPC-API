package com.exalt.ipc.job;

import com.exalt.ipc.ipc.IPC;
import com.exalt.ipc.user.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@MappedSuperclass
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Size(max = 250)
    private String description;
    @DateTimeFormat
    private LocalDateTime creationDate;
    private String state;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "uesr_id")
    private User user;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ipc_id")
    private IPC ipc;

    public Job() {
    }

    public Job(Job job) {
        this.description = job.description;
        this.creationDate = job.creationDate;
        this.state = job.state;
    }

    public Job(@NotBlank @Max(250) String description, LocalDateTime creationDate, String state) {
        this.description = description;
        this.creationDate = creationDate;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", state='" + state + '\'' +
                '}';
    }
}
