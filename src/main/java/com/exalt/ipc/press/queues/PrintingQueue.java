package com.exalt.ipc.press.queues;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
public class PrintingQueue extends PressQueue {
    @Min(5)
    @Max(20)
    private int itemsLimit;

    public PrintingQueue() {
    }

    public PrintingQueue(PressQueue queue) {
        super(queue);
    }

    public PrintingQueue(PressQueue queue, int itemsLimit) {
        super(queue);
        setItemsLimit(itemsLimit);
    }

    public int getItemsLimit() {
        return itemsLimit;
    }

    public void setItemsLimit(int itemsLimit) {
        this.itemsLimit = itemsLimit;
    }

    @Override
    public String toString() {
        return "PrintingQueue{" +
                "itemsLimit=" + itemsLimit + super.toString() +
                '}';
    }
}
