package org.etlforge.ui.widget.table;

public interface KongaRowTableModel extends KongaTableModel {
   void addRow(Object var1);

   void addRowAt(int var1, Object var2);

   void deleteRow(int var1);

   Object getRowObjectAt(int var1);
}
