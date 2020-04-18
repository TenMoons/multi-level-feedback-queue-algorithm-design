import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

//进程控制块类
public class PCB {
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
    // 进程请求页序列
    private ArrayList<Integer> pageMap = new ArrayList<>();
    // 进程分配的物理块
    private LinkedList<Page> pageBlock = new LinkedList<>();
    private LinkedList<Page> lruSerial = new LinkedList<>();

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
        // 随机设定页号
        for (int i = 0; i < life; i++) {
            Random random = new Random();
            int randomPage = random.nextInt(10);
            this.pageMap.add(randomPage);
        }
    }

    /**
     * 构造函数
     * @param pid 进程标识符
     * @param status 进程状态
     * @param priority 优先级
     * @param life 轮转时间片
     * @param arrival 到达时间
     * @param pageNum 页号
     */
    public PCB(int pid, String status, int priority, int life, int arrival, int[] pageNum) {
        this.pid = pid;
        this.status = status;
        this.priority = priority;
        this.life = life;
        this.alive = 0;
        this.arrival = arrival;
        for (int page : pageNum)
            pageMap.add(page);
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

    public ArrayList<Integer> getPageMap() {
        return pageMap;
    }

    public void setPageMap(ArrayList<Integer> pageMap) {
        this.pageMap = pageMap;
    }

    public LinkedList<Page> getPageBlock() {
        return pageBlock;
    }

    public void setPageBlock(LinkedList<Page> pageBlock) {
        this.pageBlock = pageBlock;
    }

    public LinkedList<Page> getLruSerial() {
        return lruSerial;
    }

    public void setLruSerial(LinkedList<Page> lruSerial) {
        this.lruSerial = lruSerial;
    }
}

