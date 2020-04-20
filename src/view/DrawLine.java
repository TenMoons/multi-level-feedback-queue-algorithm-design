package view;

import javax.swing.*;
import java.awt.*;

// 绘制直线，用于连接各个PCB和物理块
class DrawLine extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(0, this.getSize().height / 2,
                this.getSize().width, this.getSize().height / 2);
    }
}
