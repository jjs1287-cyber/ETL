package org.etlforge.application.ui.look;

import java.awt.Color;
import java.awt.Insets;
import javax.swing.JTable;
import javax.swing.UIManager;
import org.etlforge.ui.style.AbstractTableStyle;
import org.etlforge.ui.widget.table.TableGridMode;

public final class EtlForgeDarkTableStyle extends AbstractTableStyle {
   public EtlForgeDarkTableStyle() {
      this.setGridMode(TableGridMode.HORIZONTAL);
   }

   @Override
   public void makeOver(JTable table) {
      this.setHeaderRenderer(table, new Insets(2, 3, 2, 3), 0);
      this.setGridColor(table, new Color(77, 81, 88));
      table.setBackground(UIManager.getColor("Table.background"));
      table.setForeground(UIManager.getColor("Table.foreground"));
      super.makeOver(table);
   }
}
