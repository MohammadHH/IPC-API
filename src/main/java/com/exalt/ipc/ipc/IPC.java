package com.exalt.ipc.ipc;

import com.exalt.ipc.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.exalt.ipc.configuration.Constants.IPC_QUEUE_LIMIT;

@Entity
public class IPC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @DateTimeFormat
    private LocalDateTime creationDate;
    @Min(5)
    @Max(100)
    private int queueLimit;
    @JsonIgnore
    @OneToOne(mappedBy = "ipc", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    public IPC() {
        this.creationDate = LocalDateTime.now();
        this.queueLimit = IPC_QUEUE_LIMIT;
    }

    public IPC(LocalDateTime creationDate, @Size(min = 5, max = 100) int queueLimit) {
        this.creationDate = creationDate;
        this.queueLimit = queueLimit;
    }

    public IPC(@Size(min = 5, max = 100) int queueLimit) {
        this.creationDate = LocalDateTime.now();
        this.queueLimit = queueLimit;
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

    public int getQueueLimit() {
        return queueLimit;
    }

    public void setQueueLimit(int queueLimit) {
        this.queueLimit = queueLimit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonIgnore
    public Map<String, Object> getIPCMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", getId());
        map.put("creationDate", getCreationDate());
        map.put("queueLimit", getQueueLimit());
        return map;
    }

    @Override
    public String toString() {
        return "IPC{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", queueLimit=" + queueLimit +
                '}';
    }
}
