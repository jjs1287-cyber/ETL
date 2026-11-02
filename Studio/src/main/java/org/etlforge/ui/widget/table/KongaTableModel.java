package org.etlforge.ui.widget.table;

import javax.swing.table.TableModel;

public interface KongaTableModel extends TableModel {
   TableColumnMetaData[] getColumnDescriptors();

   TableColumnMetaData getColumnDescriptor(int var1);

   int indexOfColumn(TableColumnMetaData var1);

   Object getValueAt(int var1, TableColumnMetaData var2);
}
