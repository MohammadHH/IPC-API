package com.exalt.ipc.entities;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class Press {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@NotBlank
	private String name;

	@NotNull
	@NotBlank
	private String address;

	@DateTimeFormat
	private LocalDateTime creationDate;

	@Size(max = 250)
	private String description;

	protected Press() {
	}

	public Press(@NotNull @NotBlank String name, @NotNull @NotBlank String address, @Size(max = 250) String description) {
		this.name = name;
		this.address = address;
		this.description = description;
		setCreationDate(LocalDateTime.now());
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Press{" + "id=" + id + ", creationDate=" + creationDate + ", description='" + description + '}';
	}

}
