package org.etlforge.ui.widget.table.renderer;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public interface TableRendererPreparer {
   boolean doDefaultPreparation();

   Component prepare(JTable var1, TableCellRenderer var2, Component var3, int var4, int var5);
}
