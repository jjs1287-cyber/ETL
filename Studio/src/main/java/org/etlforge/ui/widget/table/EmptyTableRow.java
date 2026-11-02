package org.etlforge.ui.widget.table;

import com.google.common.collect.BiMap;

class EmptyTableRow extends TableRow {
   public EmptyTableRow(BiMap<TableColumnMetaData, Integer> var1) {
      super(var1, new EmptyTableRow.EmptyRowObject());

      for (int var2 = 0; var2 < var1.size(); var2++) {
         super.setColumnValue(var2, null);
      }
   }

   @Override
   public void setColumnValue(int var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public static final class EmptyRowObject {
      private EmptyRowObject() {
      }
   }
}
