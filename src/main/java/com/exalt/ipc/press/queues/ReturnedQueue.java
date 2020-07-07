package com.exalt.ipc.press.queues;

import javax.persistence.Entity;

@Entity
public class ReturnedQueue extends PressQueue {
    public ReturnedQueue() {
    }

    public ReturnedQueue(PressQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        return "ReturnedQueue{" + super.toString() + "}";
    }
}
