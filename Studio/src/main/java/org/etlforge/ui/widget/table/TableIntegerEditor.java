package org.etlforge.ui.widget.table;

import java.awt.Color;
import javax.swing.border.LineBorder;
import org.etlforge.ui.widget.text.IntegerDocument;
import org.etlforge.ui.widget.text.KongaTextField;

public final class TableIntegerEditor extends TableStringEditor {
   private Integer value;

   public TableIntegerEditor() {
      this.configureField();
   }

   private void configureField() {
      KongaTextField var1 = this.getComponent();
      var1.setSelectAllWhenFocused(true);
      var1.setHorizontalAlignment(4);
      var1.setDocument(new IntegerDocument());
   }

   @Override
   public boolean stopCellEditing() {
      try {
         String var1 = (String)super.getCellEditorValue();
         this.value = Integer.valueOf(var1);
         return super.stopCellEditing();
      } catch (NumberFormatException var2) {
         this.getComponent().setBorder(new LineBorder(Color.RED));
         return false;
      }
   }

   @Override
   public Object getCellEditorValue() {
      return this.value;
   }
}
