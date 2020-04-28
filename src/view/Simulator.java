package view;

import model.MultilevelQueue;
import model.PCB;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.ButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulator {
    // 设置第一级队列的时间片大小默认值
    public static int timeSlice = 2;
    // 队列数量
    public static final int QUEUE_SIZE = 5;
    // 最大进程数
    public static final int MEMORY_SIZE = 11;
    // 时间片
    public static int[] PCBsQueuesTimeSlice = new int[QUEUE_SIZE];
    // 多级反馈队列
    public static MultilevelQueue[] MultilevelQueues = new MultilevelQueue[QUEUE_SIZE];
    // 即将到达的PCB队列
    public static LinkedList<PCB> curQueue = new LinkedList<>();
    // 已经使用的pid
    public static int[] pidsUsed = new int[MEMORY_SIZE];
    // 当前内存中的进程数
    public static int currentPCBsNum_MFQ = 0;
    public static int currentPCBsNum_FCFS = 0;
    // 全部已运行时间
    public static int currentTime = 0;
    // 内存中能够容纳的最大进程数（这里取决于可分配的pid的个数）
    public static final int PCB_MAX_NUM = 10;
    // 是否开始执行
    public static boolean isStartScheduling;
    // 是否停止执行
    public static boolean isStopScheduling;
    // 是否暂停执行
    public static boolean isPauseScheduling;

    // 界面组件
    private static JFrame frame;
    private static Container container;

    // MFQ的界面
    private static JPanel panel = new JPanel();

    private static JScrollPane scrollPane_MFQ;
    // 菜单组件
    private static JMenuBar menuBar;
    // 设置栏
    private static JMenu processSettingsMenu;
    private static JMenuItem createProcessItem;
    private static JMenuItem setTimeSliceItem;
    private static JMenuItem exitSystemItem;
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
    // 开始按钮-MFQ
    private static JButton startButton_MFQ;
    // 开始按钮-FCFS
    private static JButton startButton_FCFS;
    // 刷新按钮
    private static JButton clearButton;
    // 程序状态栏:就绪或已执行时间
    private static JLabel statusLabel;
    // 统一ButtonUI
    private static ButtonUI myButtonUI = new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal);
    // 状态表
    private static JTable statusTable;
    // 状态区表格模型
    private static StatusTableModel statusTableModel = new StatusTableModel();
    private static JScrollPane FCFSscrollPane = new JScrollPane();

    private static LinkedList<PCB> FCFSQueue = new LinkedList<>();

    private static boolean isPCBStart_FCFS = false;

    public static void initMenus() {
        // 菜单组件
        menuBar = new JMenuBar();
        // 设置栏
        processSettingsMenu = new JMenu("<html><u>N</u><html>ew");
        createProcessItem = new JMenuItem("Create PCB");
        setTimeSliceItem = new JMenuItem("Set Default Time Slice");
        exitSystemItem = new JMenuItem("Exit");
        // 帮助栏
        helpMenu = new JMenu("<html><u>H</u><html>elp");
        tutorialItemforMFQ = new JMenuItem("MFQ Help");
        tutorialItemforLRU = new JMenuItem("FCFS Help");
        aboutItem = new JMenuItem("About");

        // 创建菜单栏-设置
        processSettingsMenu.add(createProcessItem);
        processSettingsMenu.addSeparator();
        processSettingsMenu.add(setTimeSliceItem);
        processSettingsMenu.addSeparator();
        processSettingsMenu.add(exitSystemItem);
        // 创建菜单栏-帮助
        helpMenu.add(tutorialItemforMFQ);
        helpMenu.addSeparator();
        helpMenu.add(tutorialItemforLRU);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);

        menuBar.add(processSettingsMenu);
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
            e.printStackTrace();
        }

        // 主框架
        frame = new JFrame("Operating System");
        container = frame.getContentPane();
        frame.setContentPane(container);

        // 初始化菜单栏
        initMenus();

        // 暂停按钮
        pauseButton = new JButton("Pause");
        pauseButton.setFocusPainted(false);
        // 继续按钮
        contButton = new JButton("Continue");
        contButton.setFocusPainted(false);
        // 停止按钮
        stopButton = new JButton("Stop");
        stopButton.setFocusPainted(false);
        // 开始按钮
        startButton_MFQ = new JButton("Run MFQ");
        startButton_MFQ.setFocusPainted(false);
        startButton_FCFS = new JButton("Run FCFS");
        startButton_FCFS.setFocusPainted(false);
        clearButton = new JButton("Clear");
        clearButton.setFocusPainted(false);
        // 程序状态栏:就绪或已执行时间
        statusLabel = new JLabel();

        Font f = new Font("宋体",Font.BOLD,24);
        pauseButton.setBounds(10, 360, 135, 40);
        pauseButton.setFont(f);
        pauseButton.setUI(myButtonUI);
        contButton.setBounds(10, 440, 135, 40);
        contButton.setFont(f);
        contButton.setUI(myButtonUI);
        stopButton.setBounds(10, 520, 135, 40);
        stopButton.setFont(f);
        stopButton.setUI(myButtonUI);
        startButton_MFQ.setBounds(10, 200, 135, 40);
        startButton_MFQ.setFont(f);
        startButton_MFQ.setUI(myButtonUI);
        startButton_FCFS.setBounds(10, 280, 135, 40);
        startButton_FCFS.setFont(f);
        startButton_FCFS.setUI(myButtonUI);
        clearButton.setBounds(10, 600, 135, 40);
        clearButton.setFont(f);
        clearButton.setUI(myButtonUI);

        statusLabel.setBounds(0, 860, 1268, 30);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 14));
        statusLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        statusTable = new JTable(statusTableModel);
        statusTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // 设置列的宽度
        statusTable.getColumnModel().getColumn(0).setPreferredWidth(138);
        statusTable.getColumnModel().getColumn(1).setPreferredWidth(138);
        statusTable.getColumnModel().getColumn(2).setPreferredWidth(138);
        statusTable.getColumnModel().getColumn(3).setPreferredWidth(138);
        statusTable.getColumnModel().getColumn(4).setPreferredWidth(138);
        statusTable.getColumnModel().getColumn(5).setPreferredWidth(138);
        statusTable.getColumnModel().getColumn(6).setPreferredWidth(138);
        statusTable.getColumnModel().getColumn(7).setPreferredWidth(138);

        statusTable.setRowHeight(40);  // 设置行高30像素

        DefaultTableCellRenderer cellrender = new DefaultTableCellRenderer();
        cellrender.setHorizontalAlignment(JLabel.CENTER);
		statusTable.setDefaultRenderer(Object.class, cellrender);  // 表格内容居中
        ((DefaultTableCellRenderer)statusTable.getTableHeader().
                getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);  // 表头内容居中

        FCFSscrollPane = new JScrollPane(statusTable);
        FCFSscrollPane.setBounds(152, 5, 1115, 295);

        scrollPane_MFQ = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane_MFQ.setBounds(150, 310, 1120, 550);

        // 初始化内存
        initMemory();
        container.add(FCFSscrollPane);
        container.add(scrollPane_MFQ);
        container.add(pauseButton);
        container.add(contButton);
        container.add(stopButton);
        container.add(startButton_MFQ);
        container.add(startButton_FCFS);
        container.add(clearButton);
        container.add(statusLabel);

        frame.setLocation(200, 50);
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
        currentPCBsNum_MFQ = 0;
        currentPCBsNum_FCFS = 0;
        // 运行总时间
        currentTime = 0;

        curQueue = new LinkedList<>();
        FCFSQueue = new LinkedList<>();

        // 初始化pid数组
        Arrays.fill(pidsUsed, 0);

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
        statusLabel.setText("      就绪                          (温馨提示：建议先运行FCFS算法，再运行MLFQ，切不可同时调度)");
    }

    // 给窗口中所有控件绑定监听器
    public static void setComponentsListeners() {
        createProcessItem.addActionListener(e -> createProcess());
        setTimeSliceItem.addActionListener(e -> setTimeSlice());
        exitSystemItem.addActionListener(e -> System.exit(0));
        pauseButton.addActionListener(e -> pauseSimulation());
        contButton.addActionListener(e -> contSimulation());
        startButton_MFQ.addActionListener(e -> startMLFQ());
        startButton_FCFS.addActionListener(e -> startFCFS());
        stopButton.addActionListener(e -> stopSimulation());
        clearButton.addActionListener(e -> clear());
        tutorialItemforMFQ.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Multi-level Feedback Queue(view.MFQ) algorithm"));
        tutorialItemforLRU.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "First Come First Serve(FCFS) algorithm"));
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Operating system course design\n\n" +
                        "Multi-level feedback queue algorithm simulator\n\n" +
                        "Copyright © 2020, E21714067@AHU, All Rights Reserved."));
    }

    // 创建新进程
    public static void createProcess() {
        if (currentPCBsNum_MFQ == PCB_MAX_NUM || currentPCBsNum_FCFS == PCB_MAX_NUM) {
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
            int index = 0;
            for (PCB e : queue) {
                // 到达时间更早，要插入队列中
                if (pcb.getArrival() < e.getArrival()) {
                    index = queue.indexOf(e);
                    queue.add(index, pcb);
                    isAdd = true;
                    break;
                } else if (pcb.getArrival() == e.getArrival()) {
                    // 到达时间一致，按剩余服务时间插入
                    if (pcb.getLife() - pcb.getAlive() < e.getLife() - e.getAlive()) {
                        index = queue.indexOf(e);
                        queue.add(index, pcb);
                        isAdd = true;
                        break;
                    } else if (pcb.getLife() - pcb.getAlive() == e.getLife() - e.getAlive()) {
                        index = queue.indexOf(e);
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
            // 更新PCB总数
            currentPCBsNum_MFQ++;
            currentPCBsNum_FCFS++;

            // 更新当前队列
            curQueue = queue;
            try {
                FCFSQueue.add((PCB)pcb.clone());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 更新数据面板显示
            showMLFQ(MultilevelQueues);
            if (!isAdd)
                showFCFS(pcb);
            else
                showFCFS(index, pcb);
        }
    }

    // 开始调度MFQ
    public static void startMLFQ() {
        currentTime = 0;
        isStartScheduling = true;
        isStopScheduling = false;
        isPauseScheduling = false;

        // 更新界面使用多线程实现
        new Thread(() -> {
            // 当前内存中还留有进程未执行，且没有强制停止，则将到达时间为当前时间的PCB都添加到最高级队列中，便于调度
            while (currentPCBsNum_MFQ != 0 && !isStopScheduling) {
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
                            showMLFQ(MultilevelQueues);

                            // 修改pcb属性
                            alive++;
                            curTimeSlice--;
                            // 修改系统状态
                            statusLabel.setText("   已执行时间： " + currentTime);
                            currentTime++;

                            // 当前时间，有PCB即将到达
                            if (!curQueue.isEmpty() && curQueue.getFirst().getArrival() == currentTime) {
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
                            currentPCBsNum_MFQ--;
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
            showMLFQ(MultilevelQueues);
            // 所有进程均执行完成，进程调度完成
            if (!isStopScheduling && isStartScheduling && currentPCBsNum_MFQ == 0) {
                isStartScheduling = false;
                JOptionPane.showMessageDialog(frame, "撒花✿✿ヽ(°▽°)ノ✿\n\nMFQ执行完成!");
            }
        }).start();
    }

    // 开始调度FCFS
    public static void startFCFS() {
        isStartScheduling = true;
        isStopScheduling = false;
        isPauseScheduling = false;
        currentTime = 0;
        AtomicInteger index = new AtomicInteger();

        new Thread(() -> {
            while (currentPCBsNum_FCFS != 0 && !isStopScheduling) {
                // 取出到达时间最早的PCB
                PCB pcb = FCFSQueue.get(index.get());
                // 当前时间有pcb
                if (currentTime >= pcb.getArrival()) {
                    pcb.setStatus("Running");
                    int pid = pcb.getPid();
                    int alive = pcb.getAlive();
                    int leftTime = pcb.getLeftTime();
                    if (!isPCBStart_FCFS) {
                        pcb.setStartTime(currentTime);
                        isPCBStart_FCFS = true;
                    }

                    // 暂停
                    while (isPauseScheduling) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    showFCFSTable();

                    // 修改系统状态
                    statusLabel.setText("   已执行时间： " + currentTime);
                    currentTime++;

                    // 修改调度pcb属性
                    alive++;
                    leftTime--;
                    pcb.setAlive(alive);
                    pcb.setLeftTime(leftTime);
                    pcb.setWaitingTime(pcb.getStartTime() - pcb.getArrival());

                    // 修改其他PCB状态
                    for (int i = index.get() + 1; i < FCFSQueue.size(); i++) {
                        // 已到达的PCB才更新状态
                        PCB otherPCB = FCFSQueue.get(i);
                        if (currentTime >= otherPCB.getArrival()) {
                            otherPCB.setWaitingTime(currentTime - otherPCB.getArrival());
                        }
                    }

                    // 延时模拟执行过程，方便观察
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // PCB调度已完成
                    if (pcb.getLeftTime() <= 0) {
                        // 修改PCB状态
                        pcb.setStatus("Finish");
                        pcb.setFinishTime(currentTime);
                        // 该PCB的pid之后可以被创建的其他PCB使用
                        pidsUsed[pid] = 0;
                        currentPCBsNum_FCFS--;
                        index.getAndIncrement();
                        isPCBStart_FCFS = false;
                    }
                } else {
                    currentTime++;
                }
            }
            // 延时模拟执行过程，方便观察
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            showFCFSTable();
            // 所有进程均执行完成，进程调度完成
            if (!isStopScheduling && currentPCBsNum_FCFS == 0) {
                currentTime++;
                JOptionPane.showMessageDialog(frame, "撒花✿✿ヽ(°▽°)ノ✿\n\nFCFS执行完成!");
            }
        }).start();
    }

    // 点击按钮,强制结束进程调度
    public static void stopSimulation() {
        isStopScheduling = true;
        initMemory();
    }

    // 点击按钮,暂停进程调度
    public static void pauseSimulation() {
        isPauseScheduling = true;
    }

    // 点击按钮,继续进程调度
    public static void contSimulation() {
        isPauseScheduling = false;
    }

    // 设置时间片大小
    public static void setTimeSlice() {
        int timeSliceInput = Integer.parseInt(JOptionPane.showInputDialog(frame,
                "输入时间片大小(单位:s): ", 2));
        while (timeSliceInput <= 0) {
            JOptionPane.showMessageDialog(frame, "非法输入！");
            timeSliceInput = Integer.parseInt(JOptionPane.showInputDialog(frame,
                    "输入时间片大小(单位:s): ", 2));
        }
        // 更新默认时间片
        timeSlice = timeSliceInput;
        initMemory();
    }

    // 显示内存中的多级反馈队列
    public static void showMLFQ(MultilevelQueue[] MultilevelQueues) {
        int queueLocationY = 0;
        JPanel queuesPanel = new JPanel();

        // 即将到达的PCBs
        if (curQueue.size() > 0) {
            // 创建一个PCB队列
            JPanel PCBsQueue = new JPanel();
            PCBsQueue.setLayout(new FlowLayout(FlowLayout.LEFT));
            PCBsQueue.setBounds(0, queueLocationY, 800, 700);
            // 控制PCB显示位置
            queueLocationY += 50;

            // 就绪队列显示块
            JButton PCBsQueuePriorityLabel = new JButton("即将到达");
            PCBsQueuePriorityLabel.setFont(new Font("宋体", Font.BOLD, 24));
            PCBsQueuePriorityLabel.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.blue));

            JPanel PCBsQueuePriorityBlock = new JPanel();
            PCBsQueuePriorityBlock.add(PCBsQueuePriorityLabel);

            PCBsQueue.add(PCBsQueuePriorityBlock);

            for (PCB pcb : curQueue) {
                // 绘制一个pcb
                JPanel PCBPanel = drawSinglePCB(pcb);

                //将PCB加入队列
                PCBsQueue.add(new DrawLine());
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
                PCBsQueue.setLayout(new FlowLayout(FlowLayout.LEFT));
                PCBsQueue.setBounds(0, queueLocationY, 800, 700);

                queueLocationY += 50;

                // 创建多级队列前面的优先级提示块
                // 显示的时候，priority越小，优先级越高
                JButton PCBsQueuePriorityLabel = new JButton("Priority:" + (QUEUE_SIZE - i));
                PCBsQueuePriorityLabel.setFont(new Font("宋体", Font.BOLD, 18));
                PCBsQueuePriorityLabel.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));

                JPanel PCBsQueuePriorityBlock = new JPanel();
                PCBsQueuePriorityBlock.add(PCBsQueuePriorityLabel);

                PCBsQueue.add(PCBsQueuePriorityBlock);

                // 逐个显示PCB
                for (PCB pcb : queue) {
                    // 绘制一个PCB
                    JPanel PCBPanel = drawSinglePCB(pcb);

                    // 绘制PCB连接线，将PCB加入队列
                    PCBsQueue.add(new DrawLine());
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

    // 画单个PCB并返回该PCB的panel
    public static JPanel drawSinglePCB(PCB pcb) {
        // 设置pid
        JButton pidLabel = new JButton("pid: " + pcb.getPid());
        pidLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        pidLabel.setUI(myButtonUI);

        // 设置status标签
        JButton statusLabel = new JButton("状态: " + pcb.getStatus());
        statusLabel.setFont(new Font("宋体", Font.PLAIN, 13));
        statusLabel.setUI(myButtonUI);

        // 设置life标签
        JButton lifeLabel = new JButton("剩余时间: " + (pcb.getLife() - pcb.getAlive()));
        lifeLabel.setFont(new Font("宋体", Font.PLAIN, 13));
        pidLabel.setUI(myButtonUI);

        // 设置alive标签
        JButton aliveLabel = new JButton("占用时间: " + pcb.getAlive());
        aliveLabel.setFont(new Font("宋体", Font.PLAIN, 13));
        aliveLabel.setUI(myButtonUI);

        // 设置arrival标签
        JButton arrivalLabel = new JButton("到达时间: " + pcb.getArrival());
        arrivalLabel.setFont(new Font("宋体", Font.PLAIN, 13));
        arrivalLabel.setUI(myButtonUI);

        JButton pageLabel = new JButton("生命周期: " + pcb.getLife());
        pageLabel.setFont(new Font("宋体", Font.PLAIN, 13));
        pageLabel.setUI(myButtonUI);

        //绘制一个PCB
        JPanel PCBPanel = new JPanel();
        PCBPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        PCBPanel.setLayout(new GridLayout(3, 2));
        PCBPanel.setBackground(Color.LIGHT_GRAY);
        PCBPanel.add(pidLabel);
        PCBPanel.add(arrivalLabel);
        PCBPanel.add(pageLabel);
        PCBPanel.add(statusLabel);
        PCBPanel.add(aliveLabel);
        PCBPanel.add(lifeLabel);
        return PCBPanel;
    }

    public static void showFCFSTable() {
        statusTableModel.getDataVector().removeAllElements();

        for (int i = 0; i < FCFSQueue.size(); i++) {
            showFCFS(FCFSQueue.get(i));
        }
    }

    // 直接插入表尾
    public static void showFCFS(PCB pcb) {
        statusTableModel.addRowLocation(new String[] {
                String.valueOf(pcb.getPid()),
                String.valueOf(pcb.getArrival()),
                String.valueOf(pcb.getLife()),
                pcb.getStartTime() == -1 ? "-" : String.valueOf(pcb.getStartTime()),
                String.valueOf(pcb.getLeftTime()),
                pcb.getFinishTime() == -1 ? "-" : String.valueOf(pcb.getFinishTime()),
                pcb.getWaitingTime() == -1 ? "-" : String.valueOf(pcb.getWaitingTime()),
                pcb.getStatus()
        });
    }

    // 插入第index行
    public static void showFCFS(int index, PCB pcb) {
        statusTableModel.insertRowLocation(index, new String[] {
                String.valueOf(pcb.getPid()),
                String.valueOf(pcb.getArrival()),
                String.valueOf(pcb.getLife()),
                pcb.getStartTime() == -1 ? "-" : String.valueOf(pcb.getStartTime()),
                String.valueOf(pcb.getLeftTime()),
                pcb.getFinishTime() == -1 ? "-" : String.valueOf(pcb.getFinishTime()),
                pcb.getWaitingTime() == -1 ? "-" : String.valueOf(pcb.getWaitingTime()),
                pcb.getStatus()
        });
    }

    public static void clear() {
        initMemory();
        panel.removeAll();
        statusTableModel.getDataVector().removeAllElements();
        panel.repaint();
        statusTable.repaint();
        //showFCFSTable();
    }

}
