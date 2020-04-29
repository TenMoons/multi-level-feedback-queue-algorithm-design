package model;

import java.util.LinkedList;

/**
 * 多级反馈队列
 */
public class MultilevelQueue {
    // 队列优先级
    private int priority;
    // 队列时间片
    private int timeSlice;
    private LinkedList<PCB> queue = new LinkedList<>();

    public MultilevelQueue(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getTimeSlice() {
        return timeSlice;
    }

    public void setTimeSlice(int timeSlice) {
        this.timeSlice = timeSlice;
    }

    public LinkedList<PCB> getQueue() {
        return queue;
    }

    public void setQueue(LinkedList<PCB> queue) {
        this.queue = queue;
    }
}

