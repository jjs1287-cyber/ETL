package org.etlforge.ui.widget.table;

import java.util.Collection;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;

public class TableEnumEditor extends DefaultCellEditor {
   public TableEnumEditor(Class<? extends Enum> var1) {
      super(createComboBox(var1));
   }

   public TableEnumEditor(Object[] var1) {
      super(createComboBox(var1));
   }

   public void setComboBoxRenderer(ListCellRenderer var1) {
      ((JComboBox)this.getComponent()).setRenderer(var1);
   }

   public TableEnumEditor(Collection<?> var1) {
      super(createComboBox(var1));
   }

   private static JComboBox createComboBox(Object[] var0) {
      return new JComboBox<>(var0);
   }

   private static JComboBox createComboBox(Collection<?> var0) {
      return new JComboBox<>(var0.toArray());
   }

   private static JComboBox createComboBox(Class<? extends Enum> var0) {
      Object[] var1 = var0.getEnumConstants();
      return createComboBox(var1);
   }
}
