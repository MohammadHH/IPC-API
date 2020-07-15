package com.exalt.ipc.job;

import com.exalt.ipc.user.User;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "Job")
public class File extends Job {
    @NotBlank
    private String name;
    private String type;
    @Max(200_000_000)
    private long size;

    public File() {
        super();
    }

    public File(Job job) {
        super(job);
    }

    public File(String state, User user, MultipartFile file) {
        super(state);
        super.setUser(user);
        this.name = StringUtils.cleanPath(file.getOriginalFilename());
        this.type = file.getContentType();
        this.size = file.getSize();
    }

    public void setFileInfo(@NotBlank String name, String type, @Max(200_000_000) long size) {
        this.name = name;
        this.type = type;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public LocalDateTime getReturnedDate() {
        return super.getReturnedDate();
    }

    public void setReturnedDate(LocalDateTime returnedDate) {
        super.setReturnedDate(returnedDate);
    }

    public String getState() {
        return super.getState();
    }

    public void setState(String state) {
        super.setState(state);
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + super.getId() +
                ", creationDate=" + super.getCreationDate() +
                ", state='" + super.getState() + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                (super.getUser() != null ? ", userId=" + super.getUser().getId() : "") +
                ", size=" + size +
                '}';
    }
}
