package com.exalt.ipc.file;

import com.exalt.ipc.job.Job;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Entity
public class File extends Job {
    @NotBlank
    private String name;
    private String type;
    @Max(200_000_000)
    private long size;
    private byte[] data;

    public File() {
        super();
    }

    public File(Job job) {
        super(job);
    }

    public void setFileInfo(@NotBlank String name, String type, @Max(200_000_000) long size, byte[] data) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.data = data;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + super.getId() +
                ", description='" + super.getDescription() + '\'' +
                ", creationDate=" + super.getCreationDate() +
                ", state='" + super.getState() + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                (super.getUser() != null ? ", userId=" + super.getUser().getId() : "") +
                (super.getIpc() != null ? ", ipcId=" + super.getIpc().getId() : "") +
                ", size=" + size +
                '}';
    }
}
