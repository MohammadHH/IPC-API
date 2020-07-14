package com.exalt.ipc.press.queues;

import com.exalt.ipc.press.Press;

import javax.persistence.*;

@MappedSuperclass
public class PressQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "press_id")
    private Press press;

    public PressQueue() {
    }

    public PressQueue(PressQueue queue) {
        this.id = queue.id;
        this.press = queue.press;
    }

    public PressQueue(Press press) {
        this.press = press;
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

    @Override
    public String toString() {
        return "PressQueue{" +
                "id=" + id +
                '}';
    }
}
