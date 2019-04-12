package GUI;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.LinkedHashMap;

public class MyTableModel implements TableModel {
    private int RowCount;
    private int ColumnCount;
    private LinkedHashMap<String,String> map;

    MyTableModel(LinkedHashMap<String,String> map){
        this.map = map;
    }

    @Override
    public int getRowCount() {
        return map.keySet().size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0)
            return "属性名";
        if (columnIndex == 1)
            return "属性值";
        return "不合法的输入";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex!=0)
            return true;
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        int num = 0;
        for(String key:map.keySet()){
            if (rowIndex == num) {
                switch (columnIndex) {
                    case 0:
                        return key;
                    case 1:
                        return map.get(key);
                    default:
                }
            }
            num++;
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
