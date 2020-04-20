package view;

import javax.swing.table.DefaultTableModel;

/**
 * 状态区表格模型类
 */

public class StatusTableModel extends DefaultTableModel {
    private static int dataLength;

    public StatusTableModel() {
        super();
        dataLength = 0;
        addColumn("进程号");
        addColumn("到达时间");
        addColumn("服务时间");
        addColumn("开始时间");
        addColumn("剩余时间");
        addColumn("完成时间");
        addColumn("等待时间");
        addColumn("状态");
//        addColumn("周转时间");
//        addColumn("带权周转时间");
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    /**
     * 设置为不可编辑
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * 状态表设置行位置
     *
     */
    public void addRowLocation(Object[] object) {
        //rowData[0] = "文件" + (this.getRowCount() + 1);
        addRow(object);//增加一行数据
        dataLength++;
    }

    public void removeRowLocation(int index) {
        removeRow(index);
        dataLength--;
    }

    public void insertRowLocation(int row, Object[] rowData) {
        insertRow(row, rowData);
        dataLength++;
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        // TODO Auto-generated method stub
        //	return super.getColumnClass(columnIndex);
        return getValueAt(0, columnIndex).getClass();
    }
}