package model;

//进程控制块类
public class PCB implements Cloneable{
    // 进程标识符
    private int pid;
    // 进程状态标识
    private String status;
    // 进程优先级
    private int priority;
    // 进程服务时间
    private int life;
    // 进程已运行时间
    private int alive;
    // 进程到达时间
    private int arrival;
    // 进程剩余时间
    private int leftTime;
    // 进程开始时间
    private int startTime;
    // 进程完成时间
    private int finishTime;
    // 进程等待时间
    private int waitingTime;


    /**
     * 空构造函数
     */
    public PCB() {
    }

    /**
     * 构造函数
     * @param pid 进程标识符
     * @param status 进程状态
     * @param priority 优先级
     * @param life 轮转时间片
     * @param arrival 到达时间
     */
    public PCB(int pid, String status, int priority, int life, int arrival) {
        this.pid = pid;
        this.status = status;
        this.priority = priority;
        this.life = life;
        this.alive = 0;
        this.arrival = arrival;
        this.leftTime = life;
        this.finishTime = -1;
        this.waitingTime = -1;
        this.startTime = -1;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getAlive() {
        return alive;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }

    public int getArrival() {
        return arrival;
    }

    public void setArrival(int arrival) {
        this.arrival = arrival;
    }

    public int getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(int leftTime) {
        this.leftTime = leftTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    @Override
    public Object clone () throws CloneNotSupportedException {
        Object copyPCB = super.clone();
        return copyPCB;
    }
}

