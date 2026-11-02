package org.etlforge.ui.widget.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.text.KongaTextField;

public class TableStringEditor extends DefaultCellEditor {
   private TextStyle style;

   public TableStringEditor() {
      super(createField());
   }

   private static KongaTextField createField() {
      KongaTextField var0 = new KongaTextField();
      var0.setBorder(new BorderBuilder().line(Color.BLACK).empty(0, 2, 0, 2));
      return var0;
   }

   public KongaTextField getComponent() {
      return (KongaTextField)super.getComponent();
   }

   @Override
   public Component getTableCellEditorComponent(JTable var1, Object var2, boolean var3, int var4, int var5) {
      final JTextField var6 = (JTextField)super.getTableCellEditorComponent(var1, var2, var3, var4, var5);
      if (this.style == null) {
         var6.setFont(var1.getFont());
      } else {
         this.style.makeOver(var6);
      }

      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var6.requestFocusInWindow();
            var6.setCaretPosition(var6.getDocument().getLength());
         }
      });
      return var6;
   }

   public void makeSelected() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TableStringEditor.this.getComponent().selectAll();
         }
      });
   }

   public void setTextStyle(TextStyle var1) {
      this.style = var1;
   }
}
