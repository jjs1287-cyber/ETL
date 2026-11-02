package org.etlforge.integration.client.ui.script.builder.dataelement;

import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public class DataElementTableModel extends TypedAbstractKongaTableModel<DataElement> {
   public static final TableColumnMetaData NAME = new TableColumnMetaData("Name", String.class, false, 250);
   public static final TableColumnMetaData VALUE = new TableColumnMetaData("Value", String.class, true, 200);
   public static final TableColumnMetaData IS_NULL = new TableColumnMetaData("Is Null", Boolean.class, true, 60);

   public DataElementTableModel() {
      super(DataElement.class, NAME, VALUE, IS_NULL);
   }

   public DataElementTableModel(Iterable<DataElement> var1) {
      super(DataElement.class, new TableColumnMetaData[]{NAME, VALUE, IS_NULL}, var1);
   }

   protected Object[] getColumnDataForTypedObject(DataElement var1) {
      return new Object[]{var1.getDisplayName(), var1.getValue(), var1.getValue() == null};
   }

   @Override
   public boolean isCellEditable(int var1, int var2) {
      TableColumnMetaData var3 = this.getColumnDescriptor(var2);
      return var3 == VALUE ? !this.getRowObjectAt(var1).isArray() : super.isCellEditable(var1, var2);
   }

   @Override
   public void setValueAt(Object var1, int var2, int var3) {
      super.setValueAt(var1, var2, var3);
      TableColumnMetaData var4 = this.getColumnDescriptor(var3);
      if (var4 == VALUE && var1 != null) {
         this.uncheckNullBox(var2);
      } else if (var4 == IS_NULL) {
         if (Boolean.TRUE.equals(var1)) {
            this.blankOutValue(var2);
         } else {
            this.ensureDataElementValueIsNotNull(var2);
         }
      }
   }

   private void uncheckNullBox(int var1) {
      this.setValueAt(false, var1, IS_NULL);
   }

   private void blankOutValue(int var1) {
      this.setValueAt(null, var1, VALUE);
   }

   private void ensureDataElementValueIsNotNull(int var1) {
      String var2 = (String)this.getValueAt(var1, VALUE);
      if (var2 == null) {
         var2 = "";
      }

      DataElement var3 = this.getRowObjectAt(var1);
      var3.setValue(var2);
   }

   @Override
   protected boolean updateObjectWithColumnData(Object var1, int var2, int var3, Object var4) {
      ((DataElement)var1).setValue(this.getEnteredValue(var2));
      return true;
   }

   private String getEnteredValue(int var1) {
      return this.isNull(var1) ? null : (String)this.getValueAt(var1, VALUE);
   }

   public boolean isNull(int var1) {
      return Boolean.TRUE.equals(this.getValueAt(var1, IS_NULL));
   }
}
