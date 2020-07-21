package com.exalt.ipc.entities;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Embeddable
public class File {
	@NotBlank
	private String name;

	private String type;

	@Max(200_000_000)
	private long size;

	public File() {
	}

	public File(@NotBlank String name, String type, @Max(200_000_000) long size) {
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

	@Override
	public String toString() {
		return "File{" + "name='" + name + '\'' + ", type='" + type + '\'' + ", size=" + size + '}';
	}
}
