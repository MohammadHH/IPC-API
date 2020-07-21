package com.exalt.ipc.entities;

import javax.persistence.*;

@Entity
public class RetainedQueue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "press_id")
	private Press press;

	protected RetainedQueue() {
	}

	public RetainedQueue(Press press) {
		setPress(press);
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
}
