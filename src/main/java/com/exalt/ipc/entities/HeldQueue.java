package com.exalt.ipc.entities;

import javax.persistence.*;
import javax.validation.constraints.Max;

@Entity
public class HeldQueue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "press_id")
	private Press press;

	@Max(150_000_000)
	private long sizeLimit;

	protected HeldQueue() {
	}

	public HeldQueue(Press press, @Max(150_000_000) long sizeLimit) {
		setPress(press);
		setSizeLimit(sizeLimit);
	}

	public HeldQueue(long sizeLimit) {
		this.sizeLimit = sizeLimit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Press getPress() {
		return press;
	}

	public void setPress(Press press) {
		this.press = press;
	}

	public long getSizeLimit() {
		return sizeLimit;
	}

	public void setSizeLimit(long sizeLimit) {
		this.sizeLimit = sizeLimit;
	}

}
