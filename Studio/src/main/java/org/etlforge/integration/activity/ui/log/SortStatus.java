package org.etlforge.integration.activity.ui.log;

import com.google.common.base.Preconditions;
import javax.swing.SortOrder;

public class SortStatus {
   public final OperationLogTableColumn column;
   public final SortOrder sortOrder;

   public static SortStatus descending(OperationLogTableColumn var0) {
      return new SortStatus(var0, SortOrder.DESCENDING);
   }

   public SortStatus(OperationLogTableColumn var1, SortOrder var2) {
      this.column = Preconditions.checkNotNull(var1);
      this.sortOrder = Preconditions.checkNotNull(var2);
   }

   @Override
   public String toString() {
      return this.column.getName() + " " + this.sortOrder;
   }
}
