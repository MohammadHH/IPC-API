package com.exalt.ipc.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Job {
	@Embedded
	private File file;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@DateTimeFormat
	private LocalDateTime creationDate;

	private String state;

	private LocalDateTime returnedDate;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH},
						 fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;

	public Job() {
	}

	public Job(LocalDateTime creationDate, String state) {
		this.creationDate = creationDate;
		this.state = state;
	}

	public Job(String state, File file, User user) {
		this.creationDate = LocalDateTime.now();
		this.state = state;
		this.file = file;
		this.user = user;
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

	public LocalDateTime getReturnedDate() {
		return returnedDate;
	}

	public void setReturnedDate(LocalDateTime returnedDate) {
		this.returnedDate = returnedDate;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "Job{id=" + id + ", creationDate=" + creationDate + ", state='" + state + '\'' + ", returnedDate=" +
				returnedDate + ", user=" + user + '}';
	}
}
