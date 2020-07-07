package com.exalt.ipc.press.queues;

import javax.persistence.Entity;
import javax.validation.constraints.Max;

@Entity
public class HeldQueue extends PressQueue {
    @Max(150_000_000)
    private long sizeLimit;

    public HeldQueue() {
    }

    public HeldQueue(PressQueue queue, long sizeLimit) {
        super(queue);
        this.sizeLimit = sizeLimit;
    }

    public long getSizeLimit() {
        return sizeLimit;
    }

    public void setSizeLimit(long sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    @Override
    public String toString() {
        return "HeldQueue{" +
                "sizeLimit=" + sizeLimit + super.toString() +
                '}';
    }
}
