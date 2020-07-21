package com.exalt.ipc.entities;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
public class PrintingQueue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "press_id")
	private Press press;

	@Min(5)
	@Max(20)
	private int itemsLimit;

	protected PrintingQueue() {
	}

	public PrintingQueue(Press press, int itemsLimit) {
		setItemsLimit(itemsLimit);
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

	public int getItemsLimit() {
		return itemsLimit;
	}

	public void setItemsLimit(int itemsLimit) {
		this.itemsLimit = itemsLimit;
	}
}
