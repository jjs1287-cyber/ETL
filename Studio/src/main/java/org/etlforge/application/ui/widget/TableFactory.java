package org.etlforge.application.ui.widget;

import org.etlforge.ui.widget.table.KongaRowTableModel;
import org.etlforge.ui.widget.table.KongaTable;

public interface TableFactory {
   KongaTable newTable(KongaRowTableModel var1);

   KongaTable newTable(KongaRowTableModel var1, int var2);

   KongaTable newSortedTable(KongaRowTableModel var1);

   KongaTable newSortedTable(KongaRowTableModel var1, int var2);
}
