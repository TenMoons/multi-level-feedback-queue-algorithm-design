package view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Initialization extends JWindow implements Runnable {
    private Thread splashThread;  //进度条更新线程
    private JProgressBar progress; //进度条

    public Initialization(String filename) {
        Container container = getContentPane(); // 得到容器
        JPanel pane = new JPanel();
        pane.setOpaque(true);
        pane.setBorder(null);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // 设置光标

        URL url = this.getClass().getResource(filename);
        JLabel j = new JLabel(new ImageIcon(url));
        pane.add(j);
        container.add(pane, BorderLayout.CENTER); // 增加图片

        progress = new JProgressBar(1, 100); // 实例化进度条
        progress.setStringPainted(true); // 描绘文字
        progress.setString("加载程序中,请稍候......"); // 设置显示文字
        progress.setBackground(Color.white); // 设置背景色
        container.add(progress, BorderLayout.SOUTH); // 增加进度条到容器上


        this.setSize(400, 258);
        Dimension screen = getToolkit().getScreenSize(); // 得到屏幕尺寸
        this.setBounds(screen.width / 2 - getSize().width / 2,
                (screen.height - getSize().height) / 2, 400 ,258);
        pack(); // 窗口适应组件尺寸

    }

    public void start() {
        this.toFront(); // 窗口前端显示
        splashThread = new Thread(this); // 实例化线程
        splashThread.start(); // 开始运行线程
    }

    public void run() {
        setVisible(true); // 显示窗口
        try {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(50); // 线程休眠
                progress.setValue(progress.getValue() + 1); // 设置进度条值
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        dispose(); // 释放窗口
        new Simulator().initWindow();
    }
}
