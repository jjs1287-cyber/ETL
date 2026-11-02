package org.etlforge.ui.widget.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public final class RadioButtonEditor extends AbstractCellEditor implements TableCellEditor {
   private final JRadioButton button;

   public RadioButtonEditor() {
      this(new JRadioButton());
   }

   public RadioButtonEditor(JRadioButton var1) {
      this.button = var1;
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            RadioButtonEditor.this.fireEditingStopped();
         }
      });
      var1.setRequestFocusEnabled(false);
      var1.setHorizontalAlignment(0);
      var1.setOpaque(false);
   }

   @Override
   public Object getCellEditorValue() {
      return this.button.isSelected();
   }

   @Override
   public Component getTableCellEditorComponent(JTable var1, Object var2, boolean var3, int var4, int var5) {
      boolean var6 = (Boolean)var2;
      this.button.setSelected(var6);
      TableCellRenderer var7 = var1.getCellRenderer(var4, var5);
      Component var8 = var7.getTableCellRendererComponent(var1, var2, var3, true, var4, var5);
      if (var8 != null) {
         this.button.setOpaque(true);
         this.button.setBackground(var8.getBackground());
         if (var8 instanceof JComponent) {
            this.button.setBorder(((JComponent)var8).getBorder());
         }
      } else {
         this.button.setOpaque(false);
      }

      return this.button;
   }
}
