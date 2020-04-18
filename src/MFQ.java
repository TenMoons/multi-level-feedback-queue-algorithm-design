import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.*;


public class MFQ {
    // 设置第一级队列的时间片大小默认值
    public static int timeSlice = 2;
    // 队列数量
    public static final int QUEUE_SIZE = 5;
    // 最大进程数
    public static final int MEMORY_SIZE = 11;
    // 页面数量
    public static final int PAGE_LIST_SIZE = 10;
    // 物理块大小
    public static final int STACK_SIZE = 10;
    // 时间片
    public static int[] PCBsQueuesTimeSlice = new int[QUEUE_SIZE];
    // 所有页面
    public static Page[] pages = new Page[PAGE_LIST_SIZE];
    // 多级反馈队列
    public static MultilevelQueue[] MultilevelQueues = new MultilevelQueue[QUEUE_SIZE];
    // 即将到达的PCB队列
    public static LinkedList<PCB> curQueue = new LinkedList<>();
    // 已经使用的pid
    public static int[] pidsUsed = new int[MEMORY_SIZE];
    // 当前内存中的进程数
    public static int currentPCBsNum = 0;
    // 全部已运行时间
    public static int currentTime = 0;
    // 内存中能够容纳的最大进程数（这里取决于可分配的pid的个数）
    public static final int PCB_MAX_NUM = 10;
    // 是否停止执行
    public static boolean isStopScheduling;
    // 是否暂停执行
    public static boolean isPauseScheduling;

    // 界面组件
    private static JFrame frame;
    private static Container container;

    // LRU的界面
    private static JPanel mpanel = new JPanel();
    // MFQ的界面
    private static JPanel panel = new JPanel();

    private static JScrollPane scrollPane;
    // 菜单组件
    private static JMenuBar menuBar;
    // 设置栏
    private static JMenu processSettingsMenu;
    private static JMenuItem createProcessItem;
    private static JMenuItem startMFQItem;
    private static JMenuItem stopMFQItem;
    private static JMenuItem setTimeSliceItem;
    private static JMenuItem exitSystemItem;
    // 预设栏
    //private static JMenu presetMenu = new JMenu("预设");
    //private static JMenuItem presetItem = new JMenuItem("default1");
    // 帮助栏
    private static JMenu helpMenu;
    private static JMenuItem tutorialItemforMFQ;
    private static JMenuItem tutorialItemforLRU;
    private static JMenuItem aboutItem;

    // 暂停按钮
    private static JButton pauseButton;
    // 继续按钮
    private static JButton contButton;
    // 停止按钮
    private static JButton stopButton;
    // 开始按钮
    private static JButton startButton;
    // 程序状态栏:就绪或已执行时间
    private static JLabel statusLabel;


    /**
     * ！！！！！主函数！！！
     * @param args
     */
    public static void main(String[] args) {
        new MFQ().initWindow();
    }

    public static void initMenus() {
        // 菜单组件
        menuBar = new JMenuBar();
        // 设置栏
        processSettingsMenu = new JMenu("<html><u>R</u><html>un");
        createProcessItem = new JMenuItem("Create PCB");
        startMFQItem = new JMenuItem("Start Scheduling");
        stopMFQItem = new JMenuItem("Stop Scheduling");
        setTimeSliceItem = new JMenuItem("Set Default Time Slice");
        exitSystemItem = new JMenuItem("Exit");
        // 预设栏
        //private static JMenu presetMenu = new JMenu("预设");
        //private static JMenuItem presetItem = new JMenuItem("default1");
        // 帮助栏
        helpMenu = new JMenu("<html><u>H</u><html>elp");
        tutorialItemforMFQ = new JMenuItem("MFQ Help");
        tutorialItemforLRU = new JMenuItem("LRU Help");
        aboutItem = new JMenuItem("About");

        // 创建菜单栏-设置
        processSettingsMenu.add(createProcessItem);
        processSettingsMenu.addSeparator();
        processSettingsMenu.add(startMFQItem);
        processSettingsMenu.addSeparator();
        processSettingsMenu.add(stopMFQItem);
        processSettingsMenu.addSeparator();
        processSettingsMenu.add(setTimeSliceItem);
        processSettingsMenu.addSeparator();
        processSettingsMenu.add(exitSystemItem);

        //presetMenu.add(presetItem);
        // 创建菜单栏-帮助
        helpMenu.add(tutorialItemforMFQ);
        helpMenu.addSeparator();
        helpMenu.add(tutorialItemforLRU);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);

        menuBar.add(processSettingsMenu);
        //menuBar.add(presetMenu);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);
    }

    //执行窗口初始化
    public void initWindow() {
        try {
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
            UIManager.put("ToolBar.isPaintPlainBackground", Boolean.TRUE);
        }
        catch(Exception e) {
            //TODO exception
        }

        // 主框架
        frame = new JFrame("MFQ");
        container = frame.getContentPane();
        //frame.setFont(new Font("宋体",Font.BOLD,24));

        // 初始化菜单栏
        initMenus();

        // LRU的界面
        //mpanel = new JPanel();

        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        frame.setContentPane(container);

        // 暂停按钮
        pauseButton = new JButton("Pause");
        // 继续按钮
        contButton = new JButton("Continue");
        // 停止按钮
        stopButton = new JButton("Stop");
        // 开始按钮
        startButton = new JButton("Start");
        // 程序状态栏:就绪或已执行时间
        statusLabel = new JLabel();

        Font f = new Font("宋体",Font.BOLD,24);
        pauseButton.setBounds(10, 280, 135, 40);
        pauseButton.setFont(f);
        pauseButton.setUI(new BEButtonUI(). setNormalColor(BEButtonUI.NormalColor.normal));
        contButton.setBounds(10, 360, 135, 40);
        contButton.setFont(f);
        contButton.setUI(new BEButtonUI(). setNormalColor(BEButtonUI.NormalColor.normal));
        stopButton.setBounds(10, 440, 135, 40);
        stopButton.setFont(f);
        stopButton.setUI(new BEButtonUI(). setNormalColor(BEButtonUI.NormalColor.normal));
        startButton.setBounds(10, 200, 135, 40);
        startButton.setFont(f);
        startButton.setUI(new BEButtonUI(). setNormalColor(BEButtonUI.NormalColor.normal));

        statusLabel.setBounds(0, 860, 1268, 30);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 14));
        statusLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        scrollPane = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(150, 310, 1120, 550);

        // 初始化内存
        initMemory();

        mpanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        mpanel.setBounds(152, 5, 1115, 295);
        container.add(mpanel);
        container.add(scrollPane);
        container.add(pauseButton);
        container.add(contButton);
        container.add(stopButton);
        container.add(startButton);
        container.add(statusLabel);

        // TODO: 设置窗口 ICON


        frame.setLocation(50, 50);
        frame.setSize(1280, 960);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);

        //为控件绑定监听器
        setComponentsListeners();
    }

    // 初始化相关内存参数
    public static void initMemory() {
        // PCB总数
        currentPCBsNum = 0;
        // 运行总时间
        currentTime = 0;

        // 初始化pid数组
        Arrays.fill(pidsUsed, 1, 11, 0);

        // 初始化多级队列和优先级
        for (int i = 0; i < MultilevelQueues.length; i++) {
            // i越大，优先级越高
            MultilevelQueues[i] = new MultilevelQueue(i);
        }
        // 初始化多级队列的时间片
        for (int i = PCBsQueuesTimeSlice.length - 1; i >= 0; i--) {
            // 队列优先级每降一级，时间片翻倍
            PCBsQueuesTimeSlice[i] = timeSlice;
            timeSlice *= 2;
        }

        // 初始化程序状态
        statusLabel.setText("      就绪");
    }

    // 给窗口中所有控件绑定监听器
    public static void setComponentsListeners() {
        //createProcessItem.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_MASK));
        createProcessItem.addActionListener(e -> createProcess());

        //startMFQItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK));
        startMFQItem.addActionListener(e -> start());

        //stopMFQItem.setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.CTRL_MASK));
        stopMFQItem.addActionListener(e -> stopMFQSimulation());

        //setTimeSliceItem.setAccelerator(KeyStroke.getKeyStroke('T', InputEvent.CTRL_MASK));
        setTimeSliceItem.addActionListener(e -> setTimeSlice());

        //exitSystemItem.setAccelerator(KeyStroke.getKeyStroke('E', InputEvent.CTRL_MASK));
        exitSystemItem.addActionListener(e -> System.exit(0));

        //tutorialItemforMFQ.setAccelerator(KeyStroke.getKeyStroke('T', InputEvent.CTRL_MASK));
        tutorialItemforMFQ.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Multi-level Feedback Queue(MFQ) algorithm"));

        //tutorialItemforLRU.setAccelerator(KeyStroke.getKeyStroke('T', InputEvent.CTRL_MASK));
        tutorialItemforLRU.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Least Recently Used(LRU) algorithm"));

        //aboutItem.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_MASK));
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Operating system course design\n\n" +
                        "Multi-level feedback queue algorithm simulation\n\n" +
                        "Copyright © 2020, E21714067@AHU, All Rights Reserved."));

        //stopButton.setMnemonic(KeyEvent.VK_A);
        pauseButton.addActionListener(e -> pauseMFQSimulation());

        //contButton.setMnemonic(KeyEvent.VK_D);
        contButton.addActionListener(e -> contMFQSimulation());

        startButton.addActionListener(e -> start());

        stopButton.addActionListener(e -> stopMFQSimulation());

    }

    // TODO : 优化为创建进程时在一个dialog中输入信息，而不是多个dialog
    // 创建新进程
    public static void createProcess() {
        if (currentPCBsNum == PCB_MAX_NUM) {
            JOptionPane.showMessageDialog(frame, "内存已满！");
        } else {
            int curPid = Integer.parseInt(JOptionPane.showInputDialog(frame, "输入PID：", 0));
            // 输入pid不合理
            while (curPid < 0 || pidsUsed[curPid] == 1) {
                JOptionPane.showMessageDialog(frame, "非法输入！");
                curPid = Integer.parseInt(JOptionPane.showInputDialog(frame, "输入PID：", 0));
            }
            int arvInput = Integer.parseInt(JOptionPane.showInputDialog(frame, "输入到达时间：", 0));
            // 输入到达时间不合理
            while (arvInput < currentTime) {
                JOptionPane.showMessageDialog(frame, "非法输入！");
                arvInput = Integer.parseInt(JOptionPane.showInputDialog(frame, "输入到达时间：", 0));
            }
            int srvInput = Integer.parseInt(JOptionPane.showInputDialog(frame, "输入服务时间：", 1));
            // 输入轮转时间片不合理
            while (srvInput <= 0) {
                JOptionPane.showMessageDialog(frame, "非法输入！");
                srvInput = Integer.parseInt(JOptionPane.showInputDialog(frame, "输入服务时间：", 1));
            }
            // 更新PCB总数
            currentPCBsNum++;

            // 将该进程的pid添加进已用pid列表
            pidsUsed[curPid] = 1;

            // 设置优先级，默认添加到最高优先级队列
            int curPriority = MultilevelQueues.length - 1;

            // 创建PCB
            PCB pcb = new PCB(curPid, "Ready", curPriority, srvInput, arvInput);

            // 添加到队列中
            LinkedList<PCB> queue = curQueue;
            // 是否添加成功
            boolean isAdd = false;
            for (PCB e : queue) {
                // 到达时间更早，要插入队列中
                if (pcb.getArrival() < e.getArrival()) {
                    int index = queue.indexOf(e);
                    queue.add(index, pcb);
                    isAdd = true;
                    break;
                } else if (pcb.getArrival() == e.getArrival()) {
                    // 到达时间一致，按剩余服务时间插入
                    if (pcb.getLife() - pcb.getAlive() < e.getLife() - e.getAlive()) {
                        int index = queue.indexOf(e);
                        queue.add(index, pcb);
                        isAdd = true;
                        break;
                    } else if (pcb.getLife() - pcb.getAlive() == e.getLife() - e.getAlive()) {
                        int index = queue.indexOf(e);
                        index++;
                        queue.add(index, pcb);
                        isAdd = true;
                        break;
                    }
                }
            }
            // 前面都不符合，说明应该直接添加到队尾
            if (!isAdd) {
                queue.addLast(pcb);
            }
            // 更新当前队列
            curQueue = queue;
            // 更新数据面板显示
            showMFQ(MultilevelQueues);
        }
    }

    // 开始调度
    public static void start() {
        isStopScheduling = false;
        isPauseScheduling = false;

        // 更新界面使用多线程实现
        new Thread(() -> {
            // 当前内存中还留有进程未执行，且没有强制停止，则将到达时间为当前时间的PCB都添加到最高级队列中，便于调度
            while (currentPCBsNum != 0 && !isStopScheduling) {
                boolean isEmpty = false;
                if (!curQueue.isEmpty() && curQueue.getFirst().getArrival() == currentTime) {
                    for (int i = 0; i < curQueue.size(); i++) {
                        if (curQueue.get(i).getArrival() == currentTime) {
                            // 获取队首PCB
                            PCB newPcb = curQueue.getFirst();
                            // 添加到最高级队列中
                            MultilevelQueues[MultilevelQueues.length - 1].getQueue().offer(newPcb);
                            // 从当前队列中移除
                            curQueue.remove(i);
                            i--;
                        }
                    }
                }

                // 按优先级调度队列，从最高级开始
                for (int i = MultilevelQueues.length - 1; i >= 0; i--) {
                    // 获取当前被调度队列
                    LinkedList<PCB> queue = MultilevelQueues[i].getQueue();

                    if (queue.size() > 0) {
                        // 系统中没有未执行进程
                        isEmpty = true;
                        // 获取队首PCB信息
                        PCB pcb = queue.element();
                        pcb.setStatus("Running");
                        int pid = pcb.getPid();
                        int priority = pcb.getPriority();
                        int life = pcb.getLife();
                        int alive = pcb.getAlive();
                        // 获取当前队列时间片
                        int curTimeSlice = PCBsQueuesTimeSlice[i];

                        // 当前时间片调度PCB
                        while (curTimeSlice > 0) {
                            // 暂停
                            while (isPauseScheduling) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            // 刷新数据面板信息
                            showMFQ(MultilevelQueues);

                            // 显示PCB状态
                            if (pcb.getPageBlock() != null) {
                                showBlock(pcb);
                            }

                            // 修改pcb属性
                            //life--;
                            alive++;
                            curTimeSlice--;
                            // 修改系统状态
                            statusLabel.setText("已执行时间： " + currentTime);
                            currentTime++;

                            // 当前时间，有PCB即将到达
                            if (!curQueue.isEmpty() && curQueue.getFirst().getArrival() == currentTime) {
                                   /* PCB newPcb = tmpQueue.getFirst();
                                    PCBsQueues[PCBsQueues.length - 1].getQueue().offer(newPcb);
                                    tmpQueue.remove(0);*/
                                for (int j = 0; j < curQueue.size(); j++) {
                                    if (curQueue.get(j).getArrival() == currentTime) {
                                        PCB newPcb = curQueue.getFirst();
                                        MultilevelQueues[MultilevelQueues.length - 1].getQueue().offer(newPcb);
                                        curQueue.remove(j);
                                        j--;
                                    }
                                }
                            }
                            pcb.setAlive(alive);
                            // 执行LRU算法
                            runLru(pcb);
                            // 延时模拟执行过程，方便观察
                            try {
                                Thread.sleep(2500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            // 时间片还没结束，PCB调度就已完成
                            if (life - alive <= 0) {
                                break;
                            }
                        }
                        // 不管是否调度完成，都要移除该队列的首个PCB
                        queue.poll();
                        // 若该进程执行完成
                        if (life - alive <= 0) {
                            // 该PCB的pid之后可以被创建的其他PCB使用
                            pidsUsed[pid] = 0;
                            currentPCBsNum--;
                        }
                        // 若该进程还未执行完成,则改变其PCB的相关参数,并插入其优先级所对应的队列尾部
                        else {
                            // 更新优先级
                            priority = priority - 1;
                            pcb.setPriority(priority);
                            pcb.setLife(life);
                            pcb.setStatus("Ready");
                            // 放入下一级队列
                            LinkedList<PCB> nextQueue = MultilevelQueues[priority].getQueue();
                            nextQueue.offer(pcb);
                            // 更新下一级队列
                            MultilevelQueues[priority].setQueue(nextQueue);
                        }
                        break;
                    }
                }
                // 当前内存内还有PCB未调度完
                if (!isEmpty) {
                    currentTime++;
                }
            }
            // 初始化
            initMemory();
            showBlock(null);
            showMFQ(MultilevelQueues);
            // 所有进程均执行完成，进程调度完成
            if (!isStopScheduling)
                JOptionPane.showMessageDialog(frame, "撒花✿✿ヽ(°▽°)ノ✿\n\n进程调度完成!");
        }).start();
    }

    // 点击按钮,强制结束进程调度
    public static void stopMFQSimulation() {
        isStopScheduling = true;
        initMemory();
    }

    // 点击按钮,暂停进程调度
    public static void pauseMFQSimulation() {
        isPauseScheduling = true;
    }

    // 点击按钮,继续进程调度
    public static void contMFQSimulation() {
        isPauseScheduling = false;
    }

    // 设置时间片大小
    public static void setTimeSlice() {
        int timeSliceInput = Integer.parseInt(JOptionPane.showInputDialog(frame, "输入时间片大小(单位:s): ", 2));

        while (timeSliceInput <= 0) {
            JOptionPane.showMessageDialog(frame, "非法输入！");
            timeSliceInput = Integer.parseInt(JOptionPane.showInputDialog(frame, "输入时间片大小(单位:s): ", 2));
        }
        // 更新默认时间片
        timeSlice = timeSliceInput;
        initMemory();
    }

    // 显示内存中的多级反馈队列
    public static void showMFQ(MultilevelQueue[] MultilevelQueues) {
        int queueLocationY = 0;
        JPanel queuesPanel = new JPanel();

        // 即将到达的PCBs
        if (curQueue.size() > 0) {
            // 创建一个PCB队列
            JPanel PCBsQueue = new JPanel();
            // PCBsQueue.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            PCBsQueue.setLayout(new FlowLayout(FlowLayout.LEFT));
            PCBsQueue.setBounds(0, queueLocationY, 800, 700);
            // 控制PCB显示位置
            queueLocationY += 50;

            // 就绪队列显示块
            JLabel PCBsQueuePriorityLabel = new JLabel("即将到达");
            PCBsQueuePriorityLabel.setFont(new Font("宋体", Font.BOLD, 24));
            PCBsQueuePriorityLabel.setOpaque(true);
            //Border border = BorderFactory.createLineBorder(Color.BLACK);
            //PCBsQueuePriorityLabel.setBorder(border);

            JPanel PCBsQueuePriorityBlock = new JPanel();
            PCBsQueuePriorityBlock.add(PCBsQueuePriorityLabel);

            PCBsQueue.add(PCBsQueuePriorityBlock);

            for (PCB pcb : curQueue) {
                // 设置pid
                JLabel pidLabel = new JLabel(" pid: " + pcb.getPid());
                pidLabel.setFont(new Font("宋体", Font.PLAIN, 14));
                pidLabel.setOpaque(true);
                pidLabel.setBackground(Color.WHITE);
                pidLabel.setForeground(Color.ORANGE);
                //pidLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                // 设置status标签
                JLabel statusLabel = new JLabel(" 状态: " + pcb.getStatus());
                statusLabel.setFont(new Font("宋体", Font.PLAIN, 13));
                statusLabel.setOpaque(true);
                statusLabel.setBackground(Color.WHITE);
                statusLabel.setForeground(Color.ORANGE);
                //statusLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                //设置life标签
                JLabel lifeLabel = new JLabel(" 剩余时间: " + (pcb.getLife() - pcb.getAlive()));
                lifeLabel.setFont(new Font("宋体", Font.PLAIN, 13));
                lifeLabel.setOpaque(true);
                lifeLabel.setBackground(Color.WHITE);
                lifeLabel.setForeground(Color.ORANGE);
                //lifeLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                //设置life标签
                JLabel aliveLabel = new JLabel(" 占用时间: " + pcb.getAlive());
                aliveLabel.setFont(new Font("宋体", Font.PLAIN, 13));
                aliveLabel.setOpaque(true);
                aliveLabel.setBackground(Color.WHITE);
                aliveLabel.setForeground(Color.ORANGE);
                //aliveLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                //设置arrival标签
                JLabel arrivalLabel = new JLabel(" 到达时间: " + pcb.getArrival());
                arrivalLabel.setFont(new Font("宋体", Font.PLAIN, 13));
                arrivalLabel.setOpaque(true);
                arrivalLabel.setBackground(Color.WHITE);
                arrivalLabel.setForeground(Color.ORANGE);
                //arrivalLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                JLabel pageLabel = new JLabel(" 剩余页数: " + pcb.getPageMap().size());
                pageLabel.setFont(new Font("宋体", Font.PLAIN, 13));
                pageLabel.setOpaque(true);
                pageLabel.setBackground(Color.WHITE);
                pageLabel.setForeground(Color.ORANGE);
                //pageLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                //绘制一个PCB
                JPanel PCBPanel = new JPanel();
                PCBPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                PCBPanel.setLayout(new GridLayout(2, 3));
                PCBPanel.setBackground(Color.LIGHT_GRAY);
                PCBPanel.add(pidLabel);
                PCBPanel.add(arrivalLabel);
                PCBPanel.add(pageLabel);
                PCBPanel.add(statusLabel);
                PCBPanel.add(aliveLabel);
                PCBPanel.add(lifeLabel);

                //将PCB加入队列
                PCBsQueue.add(new DrawLinePanel());
                PCBsQueue.add(PCBPanel);
            }

            queuesPanel.add(PCBsQueue);
        }

        for (int i = MultilevelQueues.length - 1; i >= 0; i--) {
            // 从优先级最高的队列开始调度
            LinkedList<PCB> queue = MultilevelQueues[i].getQueue();

            if (queue.size() > 0) {
                // 显示一个PCB队列
                JPanel PCBsQueue = new JPanel();
                // PCBsQueue.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                PCBsQueue.setLayout(new FlowLayout(FlowLayout.LEFT));
                PCBsQueue.setBounds(0, queueLocationY, 800, 700);

                queueLocationY += 50;

                // 创建多级队列前面的优先级提示块
                // 显示的时候，priority越小，优先级越高
                JLabel PCBsQueuePriorityLabel = new JLabel("Priority:" + (QUEUE_SIZE - i) + "  ");
                PCBsQueuePriorityLabel.setFont(new Font("宋体", Font.BOLD, 18));
                PCBsQueuePriorityLabel.setOpaque(true);
                //Border border = BorderFactory.createLineBorder(Color.BLACK);
                //PCBsQueuePriorityLabel.setBorder(border);

                JPanel PCBsQueuePriorityBlock = new JPanel();
                PCBsQueuePriorityBlock.add(PCBsQueuePriorityLabel);

                PCBsQueue.add(PCBsQueuePriorityBlock);

                // 逐个显示PCB
                for (PCB pcb : queue) {
                    JLabel pidLabel = new JLabel(" pid: " + pcb.getPid());
                    pidLabel.setFont(new Font("宋体", Font.PLAIN, 14));
                    pidLabel.setOpaque(true);
                    pidLabel.setBackground(Color.WHITE);
                    pidLabel.setForeground(Color.ORANGE);
                    //pidLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                    // 设置status标签
                    JLabel statusLabel = new JLabel(" 状态: " + pcb.getStatus());
                    statusLabel.setFont(new Font("宋体", Font.PLAIN, 13));
                    statusLabel.setOpaque(true);
                    statusLabel.setBackground(Color.WHITE);
                    statusLabel.setForeground(Color.ORANGE);
                    //statusLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                    //设置life标签
                    JLabel lifeLabel = new JLabel(" 剩余时间: " + (pcb.getLife() - pcb.getAlive()));
                    lifeLabel.setFont(new Font("宋体", Font.PLAIN, 13));
                    lifeLabel.setOpaque(true);
                    lifeLabel.setBackground(Color.WHITE);
                    lifeLabel.setForeground(Color.ORANGE);
                    //lifeLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                    //设置life标签
                    JLabel aliveLabel = new JLabel(" 占用时间: " + pcb.getAlive());
                    aliveLabel.setFont(new Font("宋体", Font.PLAIN, 13));
                    aliveLabel.setOpaque(true);
                    aliveLabel.setBackground(Color.WHITE);
                    aliveLabel.setForeground(Color.ORANGE);
                    //aliveLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                    //设置arrival标签
                    JLabel arrivalLabel = new JLabel(" 到达时间: " + pcb.getArrival());
                    arrivalLabel.setFont(new Font("宋体", Font.PLAIN, 13));
                    arrivalLabel.setOpaque(true);
                    arrivalLabel.setBackground(Color.WHITE);
                    arrivalLabel.setForeground(Color.ORANGE);
                    //arrivalLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                    JLabel pageLabel = new JLabel(" 剩余页数: " + pcb.getPageMap().size());
                    pageLabel.setFont(new Font("宋体", Font.PLAIN, 13));
                    pageLabel.setOpaque(true);
                    pageLabel.setBackground(Color.WHITE);
                    pageLabel.setForeground(Color.ORANGE);
                    //pageLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                    //绘制一个PCB
                    JPanel PCBPanel = new JPanel();
                    PCBPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                    PCBPanel.setLayout(new GridLayout(2, 3));
                    PCBPanel.setBackground(Color.LIGHT_GRAY);
                    PCBPanel.add(pidLabel);
                    PCBPanel.add(arrivalLabel);
                    PCBPanel.add(pageLabel);
                    PCBPanel.add(statusLabel);
                    PCBPanel.add(aliveLabel);
                    PCBPanel.add(lifeLabel);

                    //将PCB加入队列
                    PCBsQueue.add(new DrawLinePanel());
                    PCBsQueue.add(PCBPanel);
                }

                queuesPanel.add(PCBsQueue);
            }
        }

        //设置queuesPanel中的所有PCB队列（PCBsQueue组件）按垂直方向排列
        BoxLayout boxLayout = new BoxLayout(queuesPanel, BoxLayout.Y_AXIS);
        queuesPanel.setLayout(boxLayout);

        queuesPanel.setSize(800, 700);

        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.removeAll();
        panel.add(queuesPanel);
        panel.updateUI();
        panel.repaint();
    }

    // 运行先来先服务算法-curQueue
    public static void runFCFS() {
        // 存放就绪队列的PCB
        LinkedList<PCB> fcfsQueue = curQueue;

    }

    // 运行短作业优先算法
    public static void runSJF() {
        LinkedList<PCB> sjfQueue = curQueue;
        // 对PCB按照到达时间排序，到达时间相同则按照服务时间排序
        Collections.sort(sjfQueue, new Comparator<PCB>() {
            @Override
            public int compare(PCB o1, PCB o2) {
                if (o1.getArrival() < o2.getArrival())
                    return 1;
                else if (o1.getArrival() == o2.getArrival())
                    return o1.getLife() - o2.getLife();
                else return -1;
            }
        });
        showSJF(sjfQueue);
    }

    public static void showSJF(LinkedList<PCB> sjfQueue) {
        int queueLocationY = 0;
        JPanel queuesPanel = new JPanel();
        if (sjfQueue.size() > 0) {
            // running PCB队列
            JPanel runQueue = new JPanel();
            // PCBsQueue.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            runQueue.setLayout(new FlowLayout(FlowLayout.LEFT));
            runQueue.setBounds(0, queueLocationY, 800, 700);

            queueLocationY += 50;

            // ready PCB队列
            JPanel readyQueue = new JPanel();
            readyQueue.setLayout(new FlowLayout(FlowLayout.LEFT));
            readyQueue.setBounds(0, queueLocationY, 800, 700);

            queueLocationY += 50;
            // waiting PCB队列
            JPanel waitQueue = new JPanel();
            waitQueue.setLayout(new FlowLayout(FlowLayout.LEFT));
            waitQueue.setBounds(0, queueLocationY, 800, 700);

            // 队列显示块
            JLabel PCBsQueuePriorityLabel = new JLabel("正在调度");
            PCBsQueuePriorityLabel.setFont(new Font("宋体", Font.BOLD, 24));
            PCBsQueuePriorityLabel.setOpaque(true);
            //Border border = BorderFactory.createLineBorder(Color.BLACK);
            //PCBsQueuePriorityLabel.setBorder(border);

            JPanel PCBsQueuePriorityBlock = new JPanel();
            PCBsQueuePriorityBlock.add(PCBsQueuePriorityLabel);

            runQueue.add(PCBsQueuePriorityBlock);

            for (PCB pcb : sjfQueue) {
                // 设置pid
                JLabel pidLabel = new JLabel(" pid: " + pcb.getPid());
                pidLabel.setFont(new Font("宋体", Font.PLAIN, 14));
                pidLabel.setOpaque(true);
                pidLabel.setBackground(Color.WHITE);
                pidLabel.setForeground(Color.ORANGE);
                //pidLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                // 设置status标签
                JLabel statusLabel = new JLabel(" 状态: " + pcb.getStatus());
                statusLabel.setFont(new Font("宋体", Font.PLAIN, 13));
                statusLabel.setOpaque(true);
                statusLabel.setBackground(Color.WHITE);
                statusLabel.setForeground(Color.ORANGE);
                //statusLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                //设置life标签
                JLabel lifeLabel = new JLabel(" 服务时间: " + pcb.getLife());
                lifeLabel.setFont(new Font("宋体", Font.PLAIN, 13));
                lifeLabel.setOpaque(true);
                lifeLabel.setBackground(Color.WHITE);
                lifeLabel.setForeground(Color.ORANGE);
                //lifeLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                //设置life标签
                JLabel aliveLabel = new JLabel(" 占用时间: " + pcb.getAlive());
                aliveLabel.setFont(new Font("宋体", Font.PLAIN, 13));
                aliveLabel.setOpaque(true);
                aliveLabel.setBackground(Color.WHITE);
                aliveLabel.setForeground(Color.ORANGE);
                //aliveLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                //设置arrival标签
                JLabel arrivalLabel = new JLabel(" 到达时间: " + pcb.getArrival());
                arrivalLabel.setFont(new Font("宋体", Font.PLAIN, 13));
                arrivalLabel.setOpaque(true);
                arrivalLabel.setBackground(Color.WHITE);
                arrivalLabel.setForeground(Color.ORANGE);
                //arrivalLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                JLabel pageLabel = new JLabel(" 剩余时间: " + (pcb.getLife() - pcb.getAlive()));
                pageLabel.setFont(new Font("宋体", Font.PLAIN, 13));
                pageLabel.setOpaque(true);
                pageLabel.setBackground(Color.WHITE);
                pageLabel.setForeground(Color.ORANGE);
                //pageLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                //绘制一个PCB
                JPanel PCBPanel = new JPanel();
                PCBPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                PCBPanel.setLayout(new GridLayout(2, 3));
                PCBPanel.setBackground(Color.LIGHT_GRAY);
                PCBPanel.add(pidLabel);
                PCBPanel.add(arrivalLabel);
                PCBPanel.add(pageLabel);
                PCBPanel.add(statusLabel);
                PCBPanel.add(aliveLabel);
                PCBPanel.add(lifeLabel);

                // TODO: 在PCB类修改进程标识符
                if (pcb.getStatus().equals("running")) {
                    //将PCB加入队列
                    runQueue.add(new DrawLinePanel());
                    runQueue.add(PCBPanel);
                } else if (pcb.getStatus().equals("ready")) {
                    readyQueue.add(new DrawLinePanel());
                    readyQueue.add(PCBPanel);
                } else {
                    waitQueue.add(new DrawLinePanel());
                    waitQueue.add(PCBPanel);
                }


            }
            // 添加三类队列
            queuesPanel.add(runQueue);
            queuesPanel.add(readyQueue);
            queuesPanel.add(waitQueue);
        }
    }


    //LRU
    public static PCB runLru(PCB pcb) {
        ArrayList<Integer> arr = pcb.getPageMap();
        LinkedList<Page> pageBlock = pcb.getPageBlock();
        LinkedList<Page> pageStack = pcb.getLruSerial();
        int pageId = arr.get(0);
        for (int i = 0; i < pageStack.size(); i++) {
            if (pageStack.get(i).getNum() == pageId) {
                Page newPage = new Page();
                newPage.setNum(pageId);
                pageStack.remove(i);
                pageStack.add(0, newPage);
                arr.remove(0);
                pcb.setPageMap(arr);
                pcb.setLruSerial(pageStack);
                return pcb;
            }
        }
        Page p = new Page(pageId);
        pageStack.add(0, p);
        if (pageStack.size() > 5) {
            for (int j = 0; j < pageBlock.size(); j++) {
                if (pageBlock.get(j).getNum() == pageStack.getLast().getNum()) {
                    pageBlock.set(j, p);
                    break;
                }
            }
            pageStack.remove(5);
        } else {
            pageBlock.add(0, p);
        }
        arr.remove(0);
        pcb.setPageMap(arr);
        pcb.setLruSerial(pageStack);
        pcb.setPageBlock(pageBlock);
        return pcb;
    }

    //显示物理块状态
    public static void showBlock(PCB pcb) {
        if (pcb != null) {

            int queueLocationY = 0;
            JPanel queuesPanel = new JPanel();
            // 物理块状态
            LinkedList<Page> blockQue = pcb.getPageBlock();
            // LRU算法栈
            LinkedList<Page> pageStack = pcb.getLruSerial();
            // pcb的请求物理块序列
            ArrayList<Integer> pageMap = pcb.getPageMap();
            //访问序列
            if (pageMap.size() > 0) {
                //创建一个PCB队列
                JPanel pageQue = new JPanel();
                // pageQue.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                pageQue.setLayout(new FlowLayout(FlowLayout.LEFT));
                pageQue.setBounds(0, queueLocationY, 800, 700);

                queueLocationY += 50;

                //创建队列前面的优先级提示块
                JLabel pageQuePriorityLabel = new JLabel("进程号 " + pcb.getPid() + " 请求页面序列 ");
                pageQuePriorityLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
                pageQuePriorityLabel.setOpaque(true);
                Border border = BorderFactory.createLineBorder(Color.BLACK);
                pageQuePriorityLabel.setBorder(border);

                JPanel pageQuePriorityBlock = new JPanel();
                pageQuePriorityBlock.add(pageQuePriorityLabel);

                pageQue.add(pageQuePriorityBlock);

                for (Integer entry : pageMap) {

                    JLabel keyLabel = new JLabel(" 页号: " + String.valueOf(entry));
                    keyLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
                    keyLabel.setOpaque(true);
                    keyLabel.setBackground(Color.DARK_GRAY);
                    keyLabel.setForeground(Color.BLUE);
                    keyLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    JPanel PCBPanel = new JPanel();
                    PCBPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    PCBPanel.setBackground(Color.LIGHT_GRAY);
                    PCBPanel.add(keyLabel);
                    if (entry == pageMap.get(0)) {
                        JLabel stLabel = new JLabel("waiting");
                        stLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
                        stLabel.setOpaque(true);
                        stLabel.setBackground(Color.DARK_GRAY);
                        stLabel.setForeground(Color.ORANGE);
                        stLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        PCBPanel.add(stLabel);
                    }

                    pageQue.add(new DrawLinePanel());
                    pageQue.add(PCBPanel);
                }

                queuesPanel.add(pageQue);
            }
            //物理块状态
            if (blockQue.size() > 0) {
                JPanel PCBsQueue = new JPanel();
                // PCBsQueue.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                PCBsQueue.setLayout(new FlowLayout(FlowLayout.LEFT));
                PCBsQueue.setBounds(0, queueLocationY, 800, 700);

                queueLocationY += 50;

                JLabel PCBsQueuePriorityLabel = new JLabel("进程号 " + pcb.getPid() + " 物理块状态 ");
                PCBsQueuePriorityLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
                PCBsQueuePriorityLabel.setOpaque(true);
                Border border = BorderFactory.createLineBorder(Color.BLACK);
                PCBsQueuePriorityLabel.setBorder(border);

                JPanel PCBsQueuePriorityBlock = new JPanel();
                PCBsQueuePriorityBlock.add(PCBsQueuePriorityLabel);

                PCBsQueue.add(PCBsQueuePriorityBlock);

                for (Page page : blockQue) {

                    JLabel pidLabel = new JLabel("   " + page.getNum() + "   ");
                    pidLabel.setOpaque(true);
                    pidLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
                    pidLabel.setBackground(Color.DARK_GRAY);
                    pidLabel.setForeground(Color.cyan);
                    pidLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    //绘制一个PCB
                    JPanel PCBPanel = new JPanel();
                    PCBPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    PCBPanel.setBackground(Color.LIGHT_GRAY);
                    PCBPanel.add(pidLabel);

                    //将PCB加入队列
                    PCBsQueue.add(new DrawLinePanel());
                    PCBsQueue.add(PCBPanel);
                }

                queuesPanel.add(PCBsQueue);
            }
            // LRU算法栈
            if (pageStack.size() > 0) {
                JPanel stackQue = new JPanel();
                // stackQue.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                stackQue.setLayout(new FlowLayout(FlowLayout.LEFT));
                stackQue.setBounds(0, queueLocationY, 800, 700);

                queueLocationY += 50;

                JLabel pageQuePriorityLabel = new JLabel("进程号 " + pcb.getPid() + "     LRU      ");
                pageQuePriorityLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
                pageQuePriorityLabel.setOpaque(true);
                Border border = BorderFactory.createLineBorder(Color.BLACK);
                pageQuePriorityLabel.setBorder(border);

                JPanel pageQuePriorityBlock = new JPanel();
                pageQuePriorityBlock.add(pageQuePriorityLabel);

                stackQue.add(pageQuePriorityBlock);

                for (Page page : pageStack) {

                    JLabel pidLabel = new JLabel("   " + page.getNum() + "   ");
                    pidLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
                    pidLabel.setOpaque(true);
                    pidLabel.setBackground(Color.DARK_GRAY);
                    pidLabel.setForeground(Color.ORANGE);
                    pidLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    //绘制一个PCB
                    JPanel PCBPanel = new JPanel();
                    PCBPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    PCBPanel.setBackground(Color.LIGHT_GRAY);
                    PCBPanel.add(pidLabel);

                    //将PCB加入队列
                    stackQue.add(new DrawLinePanel());
                    stackQue.add(PCBPanel);
                }

                queuesPanel.add(stackQue);
            }


            //设置queuesPanel中的所有PCB队列（PCBsQueue组件）按垂直方向排列
            BoxLayout boxLayout = new BoxLayout(queuesPanel, BoxLayout.Y_AXIS);
            queuesPanel.setLayout(boxLayout);

            queuesPanel.setSize(800, 700);

            mpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            mpanel.removeAll();
            mpanel.add(queuesPanel);
        } else {
            mpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            mpanel.removeAll();
        }
        mpanel.updateUI();
        mpanel.repaint();
    }

}


// 绘制直线
class DrawLinePanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(0, this.getSize().height / 2,
                this.getSize().width, this.getSize().height / 2);
    }
}


