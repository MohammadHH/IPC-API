package com.exalt.ipc.job;

import com.exalt.ipc.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @DateTimeFormat
    private LocalDateTime creationDate;
    private String state;
    private LocalDateTime returnedDate;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;
    //    @NotBlank
//    @NotNull
    private String address;
//    @JsonIgnore
//    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinColumn(name = "ipc_id")
//    private IPC ipc;

    public Job() {
    }

    public Job(Job job) {
        this.creationDate = job.creationDate;
        this.state = job.state;
    }

    public Job(LocalDateTime creationDate, String state) {
        this.creationDate = creationDate;
        this.state = state;
    }

    public Job(String state) {
        this.creationDate = LocalDateTime.now();
        this.state = state;
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

    public LocalDateTime getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDateTime returnedDate) {
        this.returnedDate = returnedDate;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", state='" + state + '\'' +
                '}';
    }
}
