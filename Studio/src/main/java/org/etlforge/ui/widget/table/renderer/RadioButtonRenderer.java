package org.etlforge.ui.widget.table.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import org.etlforge.ui.Empty;

public final class RadioButtonRenderer implements TableCellRenderer {
   private static final Border NO_FOCUS_BORDER = Empty.border(1);
   private final JRadioButton button;

   public RadioButtonRenderer() {
      this(new JRadioButton());
   }

   public RadioButtonRenderer(JRadioButton var1) {
      this.button = var1;
      var1.setHorizontalAlignment(0);
      var1.setName("Table.cellRenderer");
   }

   @Override
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      if (var3) {
         this.button.setForeground(var1.getSelectionForeground());
         this.button.setBackground(var1.getSelectionBackground());
      } else {
         this.button.setForeground(var1.getForeground());
         this.button.setBackground(var1.getBackground());
      }

      this.button.setOpaque(var3);
      if (var4) {
         this.button.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
         if (!var3 && var1.isCellEditable(var5, var6)) {
            Color var7 = UIManager.getColor("Table.focusCellForeground");
            if (var7 != null) {
               this.button.setForeground(var7);
            }

            var7 = UIManager.getColor("Table.focusCellBackground");
            if (var7 != null) {
               this.button.setBackground(var7);
            }
         }
      } else {
         this.button.setBorder(NO_FOCUS_BORDER);
      }

      this.button.setSelected((Boolean)var2);
      return this.button;
   }
}
